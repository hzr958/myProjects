package com.smate.center.batch.quartz;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.smate.center.batch.service.TaskMarkerService;
import com.smate.center.batch.service.solr.task.SolrIndexPreprocessService;
import com.smate.core.base.utils.exception.BatchTaskException;

/**
 * 为pdwh中的成果清理作者信息.
 * 
 * @author ZhiranHE
 * 
 */
public class PdwhPubFullTextForIndexingTask {
  private final Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private SolrIndexPreprocessService solrIndexPreprocessService;
  @Autowired
  private TaskMarkerService taskMarkerService;

  public void run() throws BatchTaskException {
    logger.debug("====================================PdwhPubFullTextForIndexingTask===开始运行");
    if (isRun() == false) {
      logger.debug("====================================PdwhPubFullTextForIndexingTask===开关关闭");
      return;
    } else {
      try {
        doRun();
      } catch (BatchTaskException e) {
        logger.error("PdwhPubFullTextForIndexingTask,运行异常", e);
        throw new BatchTaskException(e);
      }
    }
  }

  public void doRun() throws BatchTaskException {
    try {
      solrIndexPreprocessService.checkFullTextForPdwh();
    } catch (Exception e) {
      logger.error("PdwhPubFullTextForIndexingTask,运行异常", e);
    }

  }

  public boolean isRun() throws BatchTaskException {
    // 任务开关控制逻辑
    return taskMarkerService.getApplicationQuartzSettingValue("PdwhPubCleanForIndexingTask_Fulltext") == 1;
    // return false;
  }
}
