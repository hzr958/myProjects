package com.smate.center.task.quartz;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.smate.center.task.base.TaskAbstract;
import com.smate.center.task.exception.SingleTaskException;
import com.smate.center.task.model.pdwh.pub.OriginalPdwhPubRelation;
import com.smate.center.task.model.pdwh.pub.OriginalPdwhPubRelationHis;
import com.smate.center.task.single.service.pub.SavePdwhPubDataService;
import com.smate.core.base.utils.common.BeanUtils;

public class RemOriPdwhPubRelationDataTask extends TaskAbstract {
  private final Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private SavePdwhPubDataService savePdwhPubDataService;

  public RemOriPdwhPubRelationDataTask() {
    super();
  }

  public RemOriPdwhPubRelationDataTask(String beanName) {
    super(beanName);
  }

  public void doRun() throws SingleTaskException {
    if (!super.isAllowExecution()) {
      logger.info("=========PdwhPubAssignTask已关闭==========");
      return;
    }
    List<OriginalPdwhPubRelation> Datalist = savePdwhPubDataService.getRemoveData();
    for (OriginalPdwhPubRelation originalPdwhPubRelation : Datalist) {
      try {
        OriginalPdwhPubRelationHis relationHis = new OriginalPdwhPubRelationHis();
        BeanUtils.copyProperties(relationHis, originalPdwhPubRelation);
        savePdwhPubDataService.saveOriginalPdwhPubRelationHis(relationHis);
        savePdwhPubDataService.deleteOriginalPdwhPubRelation(originalPdwhPubRelation);
      } catch (Exception e) {
        logger.error("RemOriginalPdwhPubRelationDataTask 备份originalPdwhPubRelation 数据出错");
      }
    }
  }
}
