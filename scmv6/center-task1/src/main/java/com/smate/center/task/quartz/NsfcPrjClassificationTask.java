package com.smate.center.task.quartz;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.smate.center.task.base.TaskAbstract;
import com.smate.center.task.exception.SingleTaskException;
import com.smate.center.task.model.sns.pub.NsfcPrjForClassification;
import com.smate.center.task.service.pdwh.quartz.ScmClassificationService;

public class NsfcPrjClassificationTask extends TaskAbstract {
  private final Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private ScmClassificationService scmClassificationService;

  public NsfcPrjClassificationTask() {
    super();
  }

  public NsfcPrjClassificationTask(String beanName) {
    super(beanName);
  }

  public void doRun() throws SingleTaskException {
    if (!super.isAllowExecution()) {
      logger.info("=========NsfcPrjClassificationTask已关闭==========");
      return;
    }

    List<NsfcPrjForClassification> nsfcPrjs = this.scmClassificationService.getNsfcPrjs();

    for (NsfcPrjForClassification nsfcPrj : nsfcPrjs) {
      try {
        Integer rs = this.scmClassificationService.classifyNsfcProjectToScm(nsfcPrj);
        this.scmClassificationService.updateNsfcPrjClassificationStatus(nsfcPrj, rs);
      } catch (Exception e) {
        logger.error("NsfcPrjClassificationTask,运行异常", e);
        this.scmClassificationService.updateNsfcPrjClassificationStatus(nsfcPrj, 3);
      }
    }
  }

}
