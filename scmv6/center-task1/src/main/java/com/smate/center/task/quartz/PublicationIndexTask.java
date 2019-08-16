package com.smate.center.task.quartz;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.smate.center.task.base.TaskAbstract;
import com.smate.center.task.exception.SingleTaskException;
import com.smate.center.task.service.sns.quartz.TaskMarkerService;
import com.smate.center.task.single.service.pub.PublicationIndexService;
import com.smate.core.base.utils.cache.CacheService;

/*
 * 从PubAllIndexTask--indexPublication()迁出的 作为单独任务PublicationIndexTask,
 */
/**
 * Publication索引重构任务
 * 
 * @author lj
 *
 */
@Deprecated
public class PublicationIndexTask extends TaskAbstract {
  private final Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  PublicationIndexService publicationIndexService;
  @Autowired
  private TaskMarkerService taskMarkerService;
  @Autowired
  private CacheService cacheService;

  public static String INDEX_TYPE_PUB = "publication_index";

  public PublicationIndexTask() {
    super();
  }

  public PublicationIndexTask(String beanName) {
    super(beanName);
  }

  public void doRun() throws SingleTaskException {

    if (!super.isAllowExecution()) {
      return;
    }

    if (taskMarkerService.getApplicationQuartzSettingValue("PubAllIndexTask_removePubCache") == 1) {
      cacheService.remove(INDEX_TYPE_PUB, "last_pub1_id");
    }

    logger.info("=========PublicationIndexTask开始运行==========");
    try {
      publicationIndexService.publicationIndex();
    } catch (Exception e) {
      logger.error("PublicationIndexTask,运行异常", e);
    }

  }

}
