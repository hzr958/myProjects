package com.smate.center.task.quartz;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.smate.center.task.base.TaskAbstract;
import com.smate.center.task.exception.SingleTaskException;
import com.smate.center.task.exception.TaskException;
import com.smate.center.task.model.snsbak.PatentHisData;
import com.smate.center.task.single.service.pub.PatentHisDataService;

public class HandlePatentHisDataTask extends TaskAbstract {
  private final Logger logger = LoggerFactory.getLogger(getClass());
  private final static Integer SIZE = 1000;
  @Autowired
  private PatentHisDataService patentHisDataService;

  public HandlePatentHisDataTask() {
    super();
  }

  public HandlePatentHisDataTask(String beanName) {
    super(beanName);
  }

  public void doRun() throws SingleTaskException {
    if (!super.isAllowExecution()) {
      logger.info("=========HandlePatentHisDataTask已关闭==========");
      return;
    }
    try {
      List<PatentHisData> patList = patentHisDataService.getPatList(SIZE);
      if (patList == null || patList.size() == 0) {
        super.closeOneTimeTask();
      }
      for (PatentHisData patData : patList) {
        patentHisDataService.HandlePatentHisData(patData);
      }
    } catch (TaskException e) {
      logger.error("HandlePatentHisDataTask处理出错------------", e);
    }

  }

}
