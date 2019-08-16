package com.smate.center.task.quartz;

import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.smate.center.task.base.TaskAbstract;
import com.smate.center.task.exception.SingleTaskException;
import com.smate.center.task.model.pdwh.quartz.PdwhPubForClassification;
import com.smate.center.task.service.pdwh.quartz.ScmClassificationService;

public class IsiPubClassificationTask extends TaskAbstract {
  private final Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private ScmClassificationService scmClassificationService;

  public IsiPubClassificationTask() {
    super();
  }

  public IsiPubClassificationTask(String beanName) {
    super(beanName);
  }

  public void doRun() throws SingleTaskException {
    if (!super.isAllowExecution()) {
      logger.info("=========IsiPubClassificationTask已关闭==========");
      return;
    }

    List<PdwhPubForClassification> pubList = this.scmClassificationService.getIsiPdwhPubs();
    if (CollectionUtils.isEmpty(pubList)) {
      return;
    }

    for (PdwhPubForClassification pub : pubList) {
      try {
        Integer executionStatus = this.scmClassificationService.IsiCategoryToScm(pub);
        this.scmClassificationService.updateClassificationStatus(pub, executionStatus);
      } catch (Exception e) {
        logger.error("IsiPubClassificationTask,运行异常", e);
        this.scmClassificationService.updateClassificationStatus(pub, 3);
      }
    }
  }

}
