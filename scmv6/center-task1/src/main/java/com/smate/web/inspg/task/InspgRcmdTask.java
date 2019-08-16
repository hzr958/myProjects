package com.smate.web.inspg.task;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.smate.core.base.utils.exception.RcmdTaskException;
import com.smate.web.inspg.task.service.InspgRcmdTaskService;

public class InspgRcmdTask {
  private final Logger logger = LoggerFactory.getLogger(getClass());
  private boolean isRunning = false;
  private Integer size = 300;

  @Autowired
  InspgRcmdTaskService inspgRcmdTaskService;

  // 判断任务是否正在进行；
  public void run() throws RcmdTaskException {
    if (isRunning == true) {
      return;
    }
    isRunning = true;

    try {
      doRun();
    } catch (RcmdTaskException e) {
      logger.error("InspgRcmdTask运行异常", e);
      throw new RcmdTaskException(e);
    }
  }

  @Deprecated
  public void doRun() throws RcmdTaskException {
    // 暂时先用创建时间推荐
    Long psnId = 0L;
    try {
      while (inspgRcmdTaskService.getPsnIdList(psnId, size) != null) {
        List<Long> psnIdList = new ArrayList<Long>();
        psnIdList = inspgRcmdTaskService.getPsnIdList(psnId, size);
        psnId += psnIdList.size();
        inspgRcmdTaskService.rcmdInspgResult(psnIdList);
      }
    } catch (RcmdTaskException e) {
      logger.error("InspgRcmdTask运行异常", e);
    }

  }
}
