package com.smate.center.batch.quartz;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.smate.center.batch.service.isisstat.IsisPubStatService;

public class IsisPubStatTask {

  private final Logger logger = LoggerFactory.getLogger(getClass());


  @Autowired
  private IsisPubStatService isisPubStatService;

  public void run() throws Exception {
    logger.debug("====================================IsisPubStatTask===开始运行");
    if (isRun() == false) {
      logger.debug("====================================IsisPubStatTask===开关关闭");
      return;
    } else {
      try {

        doRun();

      } catch (Exception e) {
        logger.error("IsisPubStatTask,运行异常", e);
        throw new Exception(e);
      }
    }
  }

  public void doRun() throws Exception {
    isisPubStatService.doIsisPubStatProc();
  }

  public boolean isRun() throws Exception {
    // 任务开关控制逻辑
    return true;
  }
}
