package com.smate.center.task.v8pub.repair.task;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.smate.center.task.base.TaskAbstract;
import com.smate.center.task.v8pub.service.DataTaskService;
import com.smate.center.task.v8pub.service.PubRepairService;
import com.smate.center.task.v8pub.sns.po.PubDataTaskPO;

public class PubSnsSituationRepairTask extends TaskAbstract {
  @Autowired
  private PubRepairService pubRepairService;
  @Autowired
  private DataTaskService dataTaskService;

  private Long startId;

  private Long endId;


  public PubSnsSituationRepairTask() {
    super();
  }

  public PubSnsSituationRepairTask(String beanName) {
    super(beanName);
  }

  private Integer SIZE = 100;

  public void doRun() {
    try {
      if (!super.isAllowExecution()) {
        return;
      }
      while (true) {
        List<PubDataTaskPO> pubList = pubRepairService.findPubId(startId, endId, SIZE);
        if (CollectionUtils.isNotEmpty(pubList)) {
          for (PubDataTaskPO pubData : pubList) {
            try {
              pubRepairService.rebuildPubSituation(pubData);
              pubData.setStatus(1);
            } catch (Exception e) {
              logger.error("修复个人库收录情况数据出错！", e);
              pubData.setStatus(-1);
              pubData.setError(StringUtils.substring(e.getMessage(), 0, 500));
            }
            dataTaskService.save(pubData);
          }
        } else {
          super.closeOneTimeTask();
          break;
        }
      }
    } catch (Exception e) {
      logger.error("PubSnsSituationRepairTask运行异常", e);
    }
  }


  public Long getStartId() {
    return startId;
  }

  public void setStartId(Long startId) {
    this.startId = startId;
  }

  public Long getEndId() {
    return endId;
  }

  public void setEndId(Long endId) {
    this.endId = endId;
  }

}
