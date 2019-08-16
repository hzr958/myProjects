package com.smate.center.task.quartz.sns;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.smate.center.task.base.TaskAbstract;
import com.smate.center.task.exception.SingleTaskException;
import com.smate.center.task.exception.TaskException;
import com.smate.center.task.service.publicpub.HandlePubRelationService;
import com.smate.center.task.service.sns.quartz.TaskMarkerService;
import com.smate.core.base.utils.cache.CacheService;

public class HandlePubRelationErrorDataTask extends TaskAbstract {
  private final static Integer SIZE = 1000; // 每次刷新获取的个数
  @Autowired
  private HandlePubRelationService handlePubRelationService;
  @Autowired
  private TaskMarkerService taskMarkerService;
  @Autowired
  private CacheService cacheService;
  public static String ERROR_DATA_PUB_ID_CACHE = "error_data_pub_id_cache";

  public HandlePubRelationErrorDataTask() {
    super();
  }

  public HandlePubRelationErrorDataTask(String beanName) {
    super(beanName);
  }

  public void doRun() throws SingleTaskException {
    if (!super.isAllowExecution()) {
      logger.info("=========HandlePubRelationErrorDataTask 已关闭==========");
      return;
    }
    // 是否移除pub_id缓存
    if (taskMarkerService.getApplicationQuartzSettingValue("HandlePubRelationErrorDataTask_removePubIdCache") == 1) {
      cacheService.remove(ERROR_DATA_PUB_ID_CACHE, "last_pub_id");
    }

    Long lastPubId = (Long) cacheService.get(ERROR_DATA_PUB_ID_CACHE, "last_pub_id");
    if (lastPubId == null) {
      lastPubId = 0L;
    }
    List<Long> pdwhPubIds = handlePubRelationService.getPdwhPubIds(lastPubId, SIZE);
    if (pdwhPubIds == null || pdwhPubIds.size() < 1) {
      try {
        super.closeOneTimeTask();
      } catch (TaskException e) {
        logger.error("HandlePubRelationErrorDataTask---关闭任务出错");
        e.printStackTrace();
      }
    }
    for (Long pdwhPubId : pdwhPubIds) {
      try {
        handlePubRelationService.deleteErrorData(pdwhPubId);
      } catch (Exception e) {
        logger.error("HandlePubRelationErrorDataTask处理数据出错----pdwhPubId:" + pdwhPubId);
      }
    }
    cacheService.put(ERROR_DATA_PUB_ID_CACHE, 60 * 60 * 24, "last_pub_id", pdwhPubIds.get(pdwhPubIds.size() - 1));

  }
}
