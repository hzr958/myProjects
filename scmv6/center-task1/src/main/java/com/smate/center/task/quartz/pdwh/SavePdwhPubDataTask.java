package com.smate.center.task.quartz.pdwh;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.smate.center.task.base.TaskAbstract;
import com.smate.center.task.exception.SingleTaskException;
import com.smate.center.task.model.pdwh.pub.OriginalPdwhPubRelation;
import com.smate.center.task.single.service.pub.SavePdwhPubDataService;

public class SavePdwhPubDataTask extends TaskAbstract {
  private final Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private SavePdwhPubDataService savePdwhPubDataService;

  public SavePdwhPubDataTask() {
    super();
  }

  public SavePdwhPubDataTask(String beanName) {
    super(beanName);
  }

  public void doRun() throws SingleTaskException {
    if (!super.isAllowExecution()) {
      return;
    }
    List<OriginalPdwhPubRelation> OriginalPdwhPubRelationList = savePdwhPubDataService.getHandleData();
    for (OriginalPdwhPubRelation originalPdwhPubRelation : OriginalPdwhPubRelationList) {
      try {
        savePdwhPubDataService.handleOriginalPubData(originalPdwhPubRelation);
      } catch (Exception e) {
        originalPdwhPubRelation.setStatus(9);
        originalPdwhPubRelation.setErrorMsg(e.getMessage());
        originalPdwhPubRelation.setUpdateDate(new Date());
        savePdwhPubDataService.saveHandleResult(originalPdwhPubRelation);
        logger.error("SavePdwhPubDataTask---保存基准库成果出错originalPdwhPubRelation：id" + originalPdwhPubRelation.getId(), e);
      }
    }
  }
}
