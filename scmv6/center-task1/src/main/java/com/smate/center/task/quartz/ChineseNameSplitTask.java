package com.smate.center.task.quartz;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.smate.center.task.base.TaskAbstract;
import com.smate.center.task.model.sns.pub.NameSplit;
import com.smate.center.task.single.person.service.ChineseNameSplitService;
import com.smate.core.base.utils.exception.BatchTaskException;

public class ChineseNameSplitTask extends TaskAbstract {
  private final Logger logger = LoggerFactory.getLogger(getClass());

  private final static Integer SIZE = 5000; // 每次刷新获取的个数

  @Autowired
  private ChineseNameSplitService chineseNameSplitService;

  public ChineseNameSplitTask() {
    super();
  }

  public ChineseNameSplitTask(String beanName) {
    super(beanName);
  }


  public void doRun() throws BatchTaskException {
    if (!super.isAllowExecution()) {
      logger.info("=========ChineseNameSplitTask已关闭==========");
      return;
    }

    List<NameSplit> nameList = this.chineseNameSplitService.getToHandleList(SIZE);

    for (NameSplit name : nameList) {
      try {
        this.chineseNameSplitService.getLastNameAndFirstName(name);
      } catch (Exception e) {
        this.chineseNameSplitService.updateNameListStatus(name, 3);
      }

    }

  }


}
