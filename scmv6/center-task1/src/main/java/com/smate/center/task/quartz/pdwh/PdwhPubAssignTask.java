package com.smate.center.task.quartz.pdwh;

import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.smate.center.task.base.TaskAbstract;
import com.smate.center.task.exception.SingleTaskException;
import com.smate.center.task.model.sns.pub.PubAssginMatchContext;
import com.smate.center.task.service.pdwh.quartz.PdwhPublicationService;
import com.smate.center.task.service.sns.pubAssign.handler.PdwhPubAssignMatchService;
import com.smate.center.task.service.sns.quartz.TaskMarkerService;
import com.smate.core.base.utils.cache.CacheService;
import com.smate.web.v8pub.dom.pdwh.PubPdwhDetailDOM;

public class PdwhPubAssignTask extends TaskAbstract {
  private final Logger logger = LoggerFactory.getLogger(getClass());
  private final static Integer SIZE = 2000; // 每次处理的个数

  @Autowired
  private PdwhPublicationService pdwhPublicationService;
  @Resource(name = "pdwhPubAssignMatchEmailHandler")
  private PdwhPubAssignMatchService pdwhPubAssignMatchService;
  @Autowired
  private TaskMarkerService taskMarkerService;
  @Autowired
  private CacheService cacheService;
  public static String ASSIGN_PUB_ID_CACHE = "assign_pub_id_cache";

  public PdwhPubAssignTask() {
    super();
  }

  public PdwhPubAssignTask(String beanName) {
    super(beanName);
  }

  public void doRun() throws SingleTaskException {
    if (!super.isAllowExecution()) {
      logger.info("=========PdwhPubAssignTask已关闭==========");
      return;
    }
    // 是否移除pub_id缓存
    if (taskMarkerService.getApplicationQuartzSettingValue("PdwhPubAssignTask_removePubIdCache") == 1) {
      cacheService.remove(ASSIGN_PUB_ID_CACHE, "last_pub_id");
    }
    try {
      Long lastPubId = (Long) cacheService.get(ASSIGN_PUB_ID_CACHE, "last_pub_id");
      if (lastPubId == null) {
        lastPubId = 0L;
      }
      List<Long> pdwhPubIds = pdwhPublicationService.getNeedAssignPubIds(lastPubId, SIZE);
      if (CollectionUtils.isEmpty(pdwhPubIds)) {
        logger.info("=========成果数据已指派完==========");
        super.closeOneTimeTask();
        return;
      }
      for (Long pdwhPubId : pdwhPubIds) {
        PubAssginMatchContext context = new PubAssginMatchContext();
        try {
          PubPdwhDetailDOM detailDom = pdwhPublicationService.getNeedAssignPub(pdwhPubId);
          if (detailDom == null) {
            continue;
          }

          if (StringUtils.isNotBlank(detailDom.getPublishDate())
              && detailDom.getPublishDate().matches("\\s*\\d{4}.*")) {
            context.setPubyear(
                Long.valueOf(StringUtils.substring(StringUtils.trimToEmpty(detailDom.getPublishDate()), 0, 4)));
          } else {
            context.setPubyear(null);
          }
          List<String> emailList = pdwhPublicationService.getPubMemberEmailList(pdwhPubId);
          if (CollectionUtils.isNotEmpty(emailList)) {
            context.setEmailList(emailList);
          }
          context.setKeywords(detailDom.getKeywords());
          context.setPdwhPubId(pdwhPubId);
          pdwhPubAssignMatchService.handler(context, null);
        } catch (Exception e) {
          logger.error("=========PdwhPubAssignTask从xml取关键词，Email出错，pubId:" + pdwhPubId, e);
        }

      }
      cacheService.put(ASSIGN_PUB_ID_CACHE, 60 * 60 * 24, "last_pub_id", pdwhPubIds.get(pdwhPubIds.size() - 1));
    } catch (

    Exception e) {
      logger.error("=========PdwhPubAssignTask出错===========", e);
    }
  }
}
