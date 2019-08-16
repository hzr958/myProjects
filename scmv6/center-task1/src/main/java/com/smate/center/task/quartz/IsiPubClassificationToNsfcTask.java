package com.smate.center.task.quartz;

import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.smate.center.task.base.TaskAbstract;
import com.smate.center.task.exception.SingleTaskException;
import com.smate.center.task.model.pdwh.quartz.PdwhPubForClassificationNsfc;
import com.smate.center.task.service.pdwh.quartz.ScmClassificationService;

public class IsiPubClassificationToNsfcTask extends TaskAbstract {
  private final Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private ScmClassificationService scmClassificationService;

  public IsiPubClassificationToNsfcTask() {
    super();
  }

  public IsiPubClassificationToNsfcTask(String beanName) {
    super(beanName);
  }

  public void doRun() throws SingleTaskException {
    if (!super.isAllowExecution()) {
      logger.info("=========IsiPubClassificationTask已关闭==========");
      return;
    }

    List<PdwhPubForClassificationNsfc> pubList = this.scmClassificationService.getIsiPdwhPubsNsfc();
    if (CollectionUtils.isEmpty(pubList)) {
      return;
    }

    for (PdwhPubForClassificationNsfc pub : pubList) {
      try {
        Integer executionStatus = this.scmClassificationService.IsiCategoryToNsfc(pub);
        this.scmClassificationService.updateClassificationStatusNsfc(pub, executionStatus);
      } catch (Exception e) {
        logger.error("IsiPubClassificationTask,运行异常", e);
        this.scmClassificationService.updateClassificationStatusNsfc(pub, 3);
      }
    }
  }

}
