package com.smate.center.batch.joblaunch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.smate.center.batch.service.JobLaunchMultiThreadService;
import com.smate.core.base.utils.exception.BatchTaskException;

/*
 * 发起多线程任务，用于分配任务，和执行任务
 * 
 */
public class JobMultiThreadExecute {

  private final Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  JobLaunchMultiThreadService jobLaunchMultiThreadService;

  public void run() throws BatchTaskException {
    logger.debug("==================BatchMultiTask运行，Batch任务开始==================");

    if (isRun() == false) {
      logger.debug("==================BatchMultiTask开关关闭==================");
      return;
    } else {
      try {
        doRun();
      } catch (BatchTaskException e) {
        logger.error("BatchMultiTask运行异常", e);
        throw new BatchTaskException(e);
      }
    }
  }

  public void doRun() throws BatchTaskException {
    try {
      jobLaunchMultiThreadService.JobLauchMultiThread();
    } catch (Exception e) {
      logger.error("BatchMultiTask运行异常", e);
    }

  }

  public boolean isRun() throws BatchTaskException {
    // return true;
    return false;
  }
}
