package com.smate.center.task.quartz;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.smate.center.task.base.TaskAbstract;
import com.smate.center.task.exception.SingleTaskException;
import com.smate.center.task.service.sns.quartz.TaskMarkerService;
import com.smate.center.task.single.service.pub.SimplePublicationIndexService;
import com.smate.core.base.utils.cache.CacheService;

/**
 * pdwh成果索引 简版
 * 
 * @author LJ
 *
 */
@Deprecated
public class SimplePdwhPubIndexTask extends TaskAbstract {
  private final Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  SimplePublicationIndexService simplePublicationIndexService;
  @Autowired
  private TaskMarkerService taskMarkerService;
  @Autowired
  private CacheService cacheService;

  public static String INDEX_TYPE_SIMPLE_PDWH_PUB = "publication_index";

  public SimplePdwhPubIndexTask() {
    super();
  }

  public SimplePdwhPubIndexTask(String beanName) {
    super(beanName);
  }

  /**
   * 任务有缓存，如果需要重新跑索引，需要在跑任务时在app_quartz_setting表设置移除缓存
   * <p>
   * 任务开始跑后请务必改app_quartz_setting表为不移除缓存，否则会导致，一直会重复跑一部分数据
   * 
   * @throws SingleTaskException
   */
  public void doRun() throws SingleTaskException {

    if (!super.isAllowExecution()) {
      return;
    }

    if (taskMarkerService.getApplicationQuartzSettingValue("SimplePdwhPubIndexTask_RmCache") == 1) {
      cacheService.remove(INDEX_TYPE_SIMPLE_PDWH_PUB, "simple_last_pdwhpub_id");
    }
    logger.info("=========SimplePdwhPubIndexTask开始运行==========");
    try {
      simplePublicationIndexService.simplePdwhPubIndex();
    } catch (Exception e) {
      logger.error("SimplePdwhPubIndexTask,运行异常", e);
    }

  }
}
