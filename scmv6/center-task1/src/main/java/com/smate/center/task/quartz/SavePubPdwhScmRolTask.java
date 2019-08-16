package com.smate.center.task.quartz;

import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.smate.center.task.base.TaskAbstract;
import com.smate.center.task.exception.SingleTaskException;
import com.smate.center.task.model.sns.pub.PubPdwhScmRol;
import com.smate.center.task.single.service.pub.PdwhScmRolPubRelationService;

public class SavePubPdwhScmRolTask extends TaskAbstract {
  private final Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private PdwhScmRolPubRelationService pdwhScmRolPubRelationService;

  public SavePubPdwhScmRolTask() {
    super();
  }

  public SavePubPdwhScmRolTask(String beanName) {
    super(beanName);
  }

  public void doRun() throws SingleTaskException {
    if (!super.isAllowExecution()) {
      logger.info("=========SavePubPdwhScmRolTask已关闭==========");
      return;
    }
    logger.info("=========SavePubPdwhScmRolTask已开启==========");
    try {
      List<Long> scmPubIdList = pdwhScmRolPubRelationService.getScmPubIds();
      if (CollectionUtils.isEmpty(scmPubIdList)) {
        return;
      }
      for (Long scmPubId : scmPubIdList) {
        PubPdwhScmRol pubPdwhScmRol = new PubPdwhScmRol(scmPubId, 1, 0, 0);
        pdwhScmRolPubRelationService.savePubPdwhScmRol(pubPdwhScmRol);
      }
    } catch (Exception e) {
      logger.error("SavePubPdwhScmRolTask 保存PubPdwhScmRol出错");
    }
  }

}
