package com.smate.center.task.quartz.tmp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.smate.center.task.base.TaskAbstract;
import com.smate.center.task.exception.SingleTaskException;
import com.smate.center.task.exception.TaskException;
import com.smate.center.task.service.sns.psn.PreGenerateShortUrlServive;

/**
 * 预生成短地址任务
 * 
 * @author LIJUN
 *
 */
public class PreGenerateShortUrlTask extends TaskAbstract {
  private final Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private PreGenerateShortUrlServive preGenerateShortUrlServive;

  public PreGenerateShortUrlTask() {
    super();
  }

  public PreGenerateShortUrlTask(String beanName) {
    super(beanName);
  }

  public void doRun() throws SingleTaskException {
    if (!super.isAllowExecution()) {
      logger.info("=========SplitPatentCategoryTask已关闭==========");
      return;
    }
    Long count = 0L;
    while (count <= 10000000L) {

      try {
        preGenerateShortUrlServive.startGenerateShortUrl();
        count++;
      } catch (Exception e) {
        logger.error("PreGenerateShortUrlTask生成短地址出错！", e);
      }
    }
    try {
      super.closeOneTimeTask();
    } catch (TaskException e) {
      logger.error("关闭PreGenerateShortUrlTask出错！", e);
    }
  }
}
