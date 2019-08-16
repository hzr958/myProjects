package com.smate.center.batch.joblaunch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.smate.center.batch.service.WeChatPreProcessPsnTaskLaunchService;
import com.smate.core.base.utils.exception.BatchTaskException;

public class WechatPreProcessPsnTaskLaunch {
  private final Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  WeChatPreProcessPsnTaskLaunchService weChatPreProcessPsnTaskLaunchService;

  public void run() throws BatchTaskException {
    logger.debug("====================================WechatPreProcessPsnTask开始运行");
    if (isRun() == false) {
      logger.debug("====================================WechatPreProcessPsnTask开关关闭");
      return;
    } else {
      try {

        doRun();

      } catch (BatchTaskException e) {
        logger.error("WechatPreProcessPsnTask运行异常", e);
        throw new BatchTaskException(e);
      }
    }
  }

  public void doRun() throws BatchTaskException {
    try {
      weChatPreProcessPsnTaskLaunchService.jobLauncher();

    } catch (Exception e) {
      logger.error("WechatPreProcessPsnTask运行异常", e);
    }

  }

  public boolean isRun() throws BatchTaskException {
    // 任务开关控制逻辑
    return true;
    // return false;
  }
}
