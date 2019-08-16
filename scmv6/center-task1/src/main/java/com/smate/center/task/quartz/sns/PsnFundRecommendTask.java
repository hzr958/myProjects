package com.smate.center.task.quartz.sns;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.smate.center.task.base.TaskAbstract;
import com.smate.center.task.exception.SingleTaskException;
import com.smate.center.task.model.fund.rcmd.ConstFundCategory;
import com.smate.center.task.model.fund.rcmd.FundRecommendContext;
import com.smate.center.task.model.fund.sns.PsnFundRecommend;
import com.smate.center.task.service.fund.PsnFundRecommendService;
import com.smate.center.task.service.sns.quartz.TaskMarkerService;
import com.smate.center.task.service.solrindex.SolrIndexDifSerivceImpl;
import com.smate.core.base.solr.service.SolrIndexService;
import com.smate.core.base.utils.cache.CacheService;
import com.smate.core.base.utils.json.JacksonUtils;

/**
 * 基金推荐给人员（登录过系统且关联过其他业务系统）
 * 
 * @author Administrator
 *
 */
public class PsnFundRecommendTask extends TaskAbstract {
  private final Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private PsnFundRecommendService psnFundRecommendService;
  @Autowired
  private SolrIndexService solrIndexSerivce;
  @Autowired
  private CacheService cacheService;
  @Autowired
  private TaskMarkerService taskMarkerService;
  private String fundpsnCache;
  private String remCacheName;
  private Integer psnType;


  public PsnFundRecommendTask() {
    super();
  }

  public PsnFundRecommendTask(String beanName) {
    super(beanName);
  }

  // 修改这里的逻辑切记修改基金列表的逻辑
  @SuppressWarnings("unchecked")
  public void doRun() throws SingleTaskException {
    if (!super.isAllowExecution()) {
      logger.info("=========PsnFundRecommendTask已关闭==========");
      return;
    }
    // 是否移除psn_id缓存
    if (taskMarkerService.getApplicationQuartzSettingValue(remCacheName) == 1) {
      cacheService.remove(fundpsnCache, "last_psn_id");
    }
    try {
      FundRecommendContext context = new FundRecommendContext();
      // 初始化基金列表数据.
      List<ConstFundCategory> fundList = psnFundRecommendService.initFundList();
      if (CollectionUtils.isEmpty(fundList)) {
        // 没有基金时移除psn_id缓存
        cacheService.remove(fundpsnCache, "last_psn_id");
        return;
      }
      Long lastPsnId = (Long) cacheService.get(fundpsnCache, "last_psn_id");
      if (lastPsnId == null) {
        lastPsnId = 0L;
      }
      List<Long> psnIdList = psnFundRecommendService.getTaskPsnList(lastPsnId, psnType);
      if (CollectionUtils.isEmpty(psnIdList)) {
        // 没有人员可推荐时移除psn_id缓存
        cacheService.remove(fundpsnCache, "last_psn_id");
        return;
      }
      for (Long psnId : psnIdList) {
          // 封装构建人员信息地区,科技领域,身份信息.
          psnFundRecommendService.buildPsnInfo(psnId, context);
          Integer enterprise = null;
          Integer researchIns = null;
          if (StringUtils.isNotBlank(context.getSeniority())) {
            if (context.getSeniority().contains("1")) {
              enterprise = 1;
            }
            if (context.getSeniority().contains("2")) {
              researchIns = 1;
            }
            if (enterprise != null && enterprise == 1 && researchIns != null && researchIns == 1) {
              enterprise = null;
              researchIns = null;
            }
          }
          List<PsnFundRecommend> list = new ArrayList<PsnFundRecommend>();
          Map<String, Object> rsMap = solrIndexSerivce.getRecommendFundRecommend(0, 1000, context.getAgencyIds(),
              context.getScienceAreaIds(), context.getGrade(), null, enterprise, researchIns, 1, new ArrayList<Long>());
          String items = (String) rsMap.get(SolrIndexDifSerivceImpl.RESULT_ITEMS);
          // 构建基金信息
          List<Map<String, Object>> listItems = JacksonUtils.jsonToList(items);
          for (Map<String, Object> item : listItems) {
            // 基金ID
            Long fundId = NumberUtils.toLong(ObjectUtils.toString(item.get("fundId")));
            // 基金无地区限制或者人员地区信息不为空，发送邮件
            if (StringUtils.isNotBlank(context.getAgencyIds())) {
              // 推荐分数
              Double score = Double.parseDouble((item.get("score").toString()));
              PsnFundRecommend psnFundRecommend = new PsnFundRecommend(fundId, psnId, score, new Date());
              list.add(psnFundRecommend);
            }
          }
          // 修改保存人员推荐基金的结果记录
          if (CollectionUtils.isNotEmpty(list)) {
            for (PsnFundRecommend reFund : list) {
              // 遍历保存新的推荐记录.
              psnFundRecommendService.saveReFundList(reFund);
            }
          } else {
            psnFundRecommendService.deletePsnFundRecommend(psnId);
          }
          this.clearPersonDate(context);
        }
      this.cacheService.put(fundpsnCache, 60 * 60 * 24, "last_psn_id", psnIdList.get(psnIdList.size() - 1));
    } catch (Exception e) {
      logger.error("人员基金推荐出错 ", e);
    }
  }

  /**
   * 清除人员信息参数.
   * 
   * @param context
   */
  private void clearPersonDate(FundRecommendContext context) {
    context.setPerson(null);
    context.setAgencyIds(null);
    context.setScienceAreaEnNames(null);
    context.setScienceAreaZhNames(null);
    context.setGrade(null);
    context.setSeniority(null);
    context.setScienceAreaIds(null);

  }


  public String getFundpsnCache() {
    return fundpsnCache;
  }

  public void setFundpsnCache(String fundpsnCache) {
    this.fundpsnCache = fundpsnCache;
  }

  public String getRemCacheName() {
    return remCacheName;
  }

  public void setRemCacheName(String remCacheName) {
    this.remCacheName = remCacheName;
  }

  public Integer getPsnType() {
    return psnType;
  }

  public void setPsnType(Integer psnType) {
    this.psnType = psnType;
  }

}
