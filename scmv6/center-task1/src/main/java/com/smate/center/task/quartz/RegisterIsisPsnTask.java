package com.smate.center.task.quartz;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.smate.center.task.base.TaskAbstract;
import com.smate.center.task.exception.SingleTaskException;
import com.smate.center.task.exception.TaskException;
import com.smate.center.task.model.sns.psn.RegisterIsisPersonTmp;
import com.smate.center.task.service.sns.quartz.RegisterIsisPsnService;

/**
 * isis注册后台任务
 * 
 * @author zzx
 *
 */
public class RegisterIsisPsnTask extends TaskAbstract {
  private final Logger logger = LoggerFactory.getLogger(getClass());
  private static int batchSize = 200;
  @Autowired
  private RegisterIsisPsnService registerIsisPsnService;

  public void doRun() throws SingleTaskException {
    if (!super.isAllowExecution()) {
      return;
    }
    List<RegisterIsisPersonTmp> list = null;
    boolean status = true;
    Long count = 0L;
    do {
      count++;
      try {
        list = registerIsisPsnService.getList(batchSize);
      } catch (Exception e) {
        logger.error("isis注册后台任务查询列表出错", e);
      }
      if (list == null || list.size() <= 0) {
        status = false;
        try {
          super.closeOneTimeTask();
        } catch (TaskException e) {
          logger.error("关闭SnsDupPubGroupingTask出错！", e);
        }
      } else {
        for (RegisterIsisPersonTmp r : list) {
          try {
            registerIsisPsnService.doRegister(r);
            r.setDealStatus(1);
          } catch (Exception e) {
            logger.error("isis注册处理出错,irisPsnId=" + r.getIsisPsnId(), e);
            r.setDealStatus(2);
          }
          registerIsisPsnService.save(r);
        }
      }
    } while (status && count < 77777);
  }


  public RegisterIsisPsnTask() {
    super();
  }

  public RegisterIsisPsnTask(String beanName) {
    super(beanName);
  }
}
