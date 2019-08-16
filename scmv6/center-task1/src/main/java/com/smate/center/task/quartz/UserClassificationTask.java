package com.smate.center.task.quartz;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.smate.center.task.base.TaskAbstract;
import com.smate.center.task.exception.SingleTaskException;
import com.smate.center.task.model.sns.pub.ScmUserForClassification;
import com.smate.center.task.service.pdwh.quartz.ScmClassificationService;

public class UserClassificationTask extends TaskAbstract {
  private final Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private ScmClassificationService scmClassificationService;

  public UserClassificationTask() {
    super();
  }

  public UserClassificationTask(String beanName) {
    super(beanName);
  }


  public void doRun() throws SingleTaskException {

    if (!super.isAllowExecution()) {
      logger.info("=========UserClassificationTask已关闭==========");
      return;
    }

    List<ScmUserForClassification> userList = this.scmClassificationService.getScmUserForClassification();

    for (ScmUserForClassification user : userList) {

      try {
        Integer status = this.scmClassificationService.psnClassifyBasedOnNsfcInfo(user);
        this.scmClassificationService.updateScmUserForClassificationStatus(user, status);
      } catch (Exception e) {
        logger.error("UserClassificationTask运行出错，ScmUserForClassification中psnId = " + user.getPsnId() + "==", e);
        this.scmClassificationService.updateScmUserForClassificationStatus(user, 3);
      }
    }

  }


}
