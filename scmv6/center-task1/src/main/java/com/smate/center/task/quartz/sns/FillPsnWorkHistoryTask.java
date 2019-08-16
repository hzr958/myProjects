package com.smate.center.task.quartz.sns;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;

import com.smate.center.task.base.TaskAbstract;
import com.smate.center.task.exception.SingleTaskException;
import com.smate.center.task.service.sns.psn.FillPsnWorkHistoryService;
import com.smate.core.base.psn.model.WorkHistory;
import com.smate.core.base.utils.model.security.Person;

public class FillPsnWorkHistoryTask extends TaskAbstract {
  private final Logger logger = LoggerFactory.getLogger(getClass());
  private final static Integer SIZE = 5000; // 每次处理的个数
  @Autowired
  private FillPsnWorkHistoryService fillPsnWorkHistoryService;

  public FillPsnWorkHistoryTask() {
    super();
  }

  public FillPsnWorkHistoryTask(String beanName) {
    super(beanName);
  }

  public void doRun() throws SingleTaskException {
    if (!super.isAllowExecution()) {
      logger.info("=========FillPsnWorkHistoryTask已关闭==========");
      return;
    }
    List<Person> psnList = fillPsnWorkHistoryService.getHandlePsnList(SIZE);
    try {
      if (CollectionUtils.isEmpty(psnList)) {
        super.closeOneTimeTask();
      }
      for (Person person : psnList) {
        WorkHistory psnWork =
            new WorkHistory(person.getInsId(), person.getInsName(), person.getDepartment(), person.getPosition());
        psnWork.setPsnId(person.getPersonId());
        psnWork.setIsPrimary(1L);
        fillPsnWorkHistoryService.saveWorkHistory(psnWork);
      }
    } catch (Exception e) {
      logger.error("FillPsnWorkHistoryTask,运行异常", e);
    }

  }
}
