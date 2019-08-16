package com.smate.center.task.quartz;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.smate.center.task.base.TaskAbstract;
import com.smate.center.task.exception.SingleTaskException;
import com.smate.center.task.model.rcmd.quartz.RecomMsgForm;
import com.smate.center.task.service.sns.quartz.RecomMsgService;

public class RecomMsgTask extends TaskAbstract {
  private final Logger logger = LoggerFactory.getLogger(getClass());
  private static int batchSize = 300;
  @Autowired
  private RecomMsgService recomMsgService;

  public void doRun() throws SingleTaskException {
    if (!super.isAllowExecution()) {
      return;
    }
    boolean pStatus = true;
    Long count = 0L;
    do {
      count++;
      try {
        List<RecomMsgForm> pubList = recomMsgService.getRecomMsgFormList(null, batchSize);
        if (pubList != null && pubList.size() > 0) {
          for (RecomMsgForm r : pubList) {
            recomMsgService.buildRecomMsg(r);
          }
        } else {
          pStatus = false;
        }
      } catch (Exception e) {
        logger.error("发送推荐消息task异常", e);
      }
    } while (pStatus);
  }

  public RecomMsgTask() {
    super();
  }

  public RecomMsgTask(String beanName) {
    super(beanName);
  }
}
