package com.smate.center.task.quartz;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.smate.center.task.base.TaskAbstract;
import com.smate.center.task.exception.SingleTaskException;
import com.smate.center.task.model.sns.pub.PubPdwhScmRol;
import com.smate.center.task.single.service.pub.PdwhScmRolPubRelationService;

public class TmpPdwhScmRolPubRelationTask extends TaskAbstract {
  private final Logger logger = LoggerFactory.getLogger(getClass());

  private final static Integer SIZE = 1000; // 每次刷新获取的个数
  private Long startPubId;
  private Long endPubId;

  public TmpPdwhScmRolPubRelationTask() {
    super();
  }

  public TmpPdwhScmRolPubRelationTask(String beanName) {
    super(beanName);
  }

  @Autowired
  private PdwhScmRolPubRelationService pdwhScmRolPubRelationService;


  public void doRun() throws SingleTaskException {
    if (!super.isAllowExecution()) {
      logger.info("=========PdwhScmRolPubRelationTask已关闭==========");
      return;
    }

    List<PubPdwhScmRol> toDoList = this.pdwhScmRolPubRelationService.getPdwhToHandlePub(SIZE, startPubId, endPubId);
    for (PubPdwhScmRol item : toDoList) {
      Integer rs = 1;
      try {
        if (item.getFrom() == 1) {// sns
          rs = pdwhScmRolPubRelationService.handleScmPub(item);
        } else if (item.getFrom() == 2) {// rol
          rs = pdwhScmRolPubRelationService.handleRolPub(item);
        }
      } catch (Exception e) {
        rs = 3;
        logger.error("PdwhScmRolPubRelationTask,运行异常", e);
      }
      this.pdwhScmRolPubRelationService.updatePubPdwhScmRol(item, rs);
    }

  }

  public Long getEndPubId() {
    return endPubId;
  }

  public void setEndPubId(Long endPubId) {
    this.endPubId = endPubId;
  }

  public Long getStartPubId() {
    return startPubId;
  }

  public void setStartPubId(Long startPubId) {
    this.startPubId = startPubId;
  }

}
