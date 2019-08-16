package com.smate.center.batch.quartz;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.smate.center.batch.service.TaskMarkerService;
import com.smate.center.batch.service.pub.GroupPublicationService;
import com.smate.core.base.utils.exception.BatchTaskException;

/**
 * 重构文献索引.
 * 
 * @author cwli
 * 
 */
public class UpdateRelevanceAndLabeledForAllGroupPubs {
  private final Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private GroupPublicationService groupPublicationService;
  @Autowired
  private TaskMarkerService taskMarkerService;

  public void run() throws BatchTaskException {
    logger.debug("======UpdateRelevanceAndLabeledForAllGroupPubs===开始运行");
    if (isRun() == false) {
      logger.debug("======UpdateRelevanceAndLabeledForAllGroupPubs===开关关闭");
      return;
    } else {
      try {
        doRun();
      } catch (BatchTaskException e) {
        logger.error("UpdateRelevanceAndLabeledForAllGroupPubs,运行异常", e);
        throw new BatchTaskException(e);
      }
    }
  }

  public void doRun() throws BatchTaskException {
    try {
      groupPublicationService.updateGroupPubsInfo();
    } catch (Exception e) {
      logger.error("UpdateRelevanceAndLabeledForAllGroupPubs,运行异常", e);
    }

  }

  public boolean isRun() throws BatchTaskException {
    // 任务开关控制逻辑
    return taskMarkerService.getApplicationQuartzSettingValue("updateRelevanceAndLabeledForAllGroupPubs") == 1;
  }
}
