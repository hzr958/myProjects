package com.smate.center.task.quartz;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.smate.center.task.base.TaskAbstract;
import com.smate.center.task.exception.SingleTaskException;
import com.smate.center.task.service.solrindex.SolrIndexDifService;

/**
 * 重构文献索引.
 * 
 * @author cwli
 * 
 */
public class PubAllIndexTask extends TaskAbstract {
  private final Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private SolrIndexDifService solrIndexDifService;

  public PubAllIndexTask() {
    super();
  }

  public PubAllIndexTask(String beanName) {
    super(beanName);
  }

  public void doRun() throws SingleTaskException {
    if (!super.isAllowExecution()) {
      logger.info("=========PubAllIndexTask 已关闭==========");
      return;
    }
    logger.info("=========PubAllIndexTask开始运行==========");
    try {
      solrIndexDifService.runIndex();
    } catch (Exception e) {
      logger.error("PubAllIndexTask,运行异常", e);
    }

  }

}
