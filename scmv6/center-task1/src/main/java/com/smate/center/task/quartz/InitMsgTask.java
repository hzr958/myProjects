package com.smate.center.task.quartz;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.smate.center.task.base.TaskAbstract;
import com.smate.center.task.exception.SingleTaskException;
import com.smate.center.task.model.sns.msg.InitMsgForm;
import com.smate.center.task.service.sns.quartz.InitMsgService;

/**
 * 初始化消息task
 * 
 * @author cht
 *
 */
public class InitMsgTask extends TaskAbstract {
  private final Logger logger = LoggerFactory.getLogger(getClass());
  private static int batchSize = 200;
  @Autowired
  private InitMsgService initMsgService;

  public void doRun() throws SingleTaskException {
    if (!super.isAllowExecution()) {
      return;
    }
    List<InitMsgForm> list = null;
    boolean status = true;
    Long count = 0L;
    do {
      count++;
      list = initMsgService.getList(batchSize);
      if (list == null || list.size() <= 0) {
        status = false;
      } else {
        for (InitMsgForm i : list) {
          initMsgService.doInitMsg(i);
        }
      }
    } while (status && count < 77777);
  }

  public InitMsgTask() {
    super();
  }

  public InitMsgTask(String beanName) {
    super(beanName);
  }

}
