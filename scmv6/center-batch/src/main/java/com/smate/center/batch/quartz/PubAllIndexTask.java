package com.smate.center.batch.quartz;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.smate.center.batch.service.TaskMarkerService;
import com.smate.center.batch.service.solr.task.SolrIndexDifService;
import com.smate.core.base.utils.exception.BatchTaskException;

/**
 * 重构文献索引.（已经迁移到task，任务关闭）
 * 
 * @author cwli
 * 
 */
@Deprecated
public class PubAllIndexTask {
  private final Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private SolrIndexDifService solrIndexDifService;
  @Autowired
  private TaskMarkerService taskMarkerService;

  public void run() throws BatchTaskException {
    logger.debug("====================================SyncPubAllKeywordsTask===开始运行");
    if (isRun() == false) {
      logger.debug("====================================SyncPubAllKeywordsTask===开关关闭");
      return;
    } else {
      try {
        doRun();
      } catch (BatchTaskException e) {
        logger.error("SyncPubAllKeywordsTask,运行异常", e);
        throw new BatchTaskException(e);
      }
    }
  }

  public void doRun() throws BatchTaskException {
    try {
      solrIndexDifService.runIndex();

    } catch (Exception e) {
      logger.error("SyncPubAllKeywordsTask,运行异常", e);
    }

  }

  public boolean isRun() throws BatchTaskException {
    // 任务开关控制逻辑
    return taskMarkerService.getApplicationQuartzSettingValue("pubAllIndexTask") == 1;
    // return true;
  }
}
