package com.smate.center.task.quartz.sns;

import java.util.List;
import java.util.Objects;

import org.apache.commons.lang.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;

import com.smate.center.task.base.TaskAbstract;
import com.smate.center.task.exception.SingleTaskException;
import com.smate.center.task.exception.TaskException;
import com.smate.center.task.service.sns.quartz.InitPsnRecommendFundService;
import com.smate.center.task.service.sns.quartz.TaskMarkerService;
import com.smate.core.base.utils.cache.CacheService;

public class InitPsnRecommendFundTask extends TaskAbstract {
  private final Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private TaskMarkerService taskMarkerService;
  @Autowired
  private CacheService cacheService;
  @Autowired
  private InitPsnRecommendFundService initPsnRecommendFundService;
  public static String PSN_ID_CACHE = "fund_recommend_psn_cache";

  public InitPsnRecommendFundTask() {
    super();
  }

  public InitPsnRecommendFundTask(String beanName) {
    super(beanName);
  }

  public void doRun() throws SingleTaskException {
    if (!super.isAllowExecution()) {
      logger.info("=========InitPsnRecommendFundTask已关闭==========");
      return;
    }

    // 是否移除psn_id缓存
    if (taskMarkerService.getApplicationQuartzSettingValue("InitPsnRecommendFundTask_removePatCache") == 1) {
      cacheService.remove(PSN_ID_CACHE, "last_psn_id");
    }
    Long lastPsnId = NumberUtils.toLong(Objects.toString(cacheService.get(PSN_ID_CACHE, "last_psn_id")), 0L);
    List<Long> psnIdList = initPsnRecommendFundService.getPsnIds(lastPsnId);
    if (!CollectionUtils.isEmpty(psnIdList)) {
      try {
        initPsnRecommendFundService.initPsnFundRecommend(psnIdList);
        this.cacheService.put(PSN_ID_CACHE, 60 * 60 * 24, "last_psn_id", psnIdList.get(psnIdList.size() - 1));
      } catch (Exception e) {
        logger.error("InitPsnRecommendFundTask出错！", e);
        return;
      }
    } else {
      try {
        cacheService.remove(PSN_ID_CACHE, "last_psn_id");
        super.closeOneTimeTask();
      } catch (TaskException e) {
        logger.error("关闭InitPsnRecommendFundTask出错！", e);
        return;
      }
    }
  }
}
