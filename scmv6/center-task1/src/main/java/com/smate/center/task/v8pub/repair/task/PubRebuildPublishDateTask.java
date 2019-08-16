package com.smate.center.task.v8pub.repair.task;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.smate.center.task.base.TaskAbstract;
import com.smate.center.task.v8pub.service.PubRepairService;
import com.smate.center.task.v8pub.sns.po.PubDataTaskPO;

public class PubRebuildPublishDateTask extends TaskAbstract {
  @Autowired
  private PubRepairService pubRepairService;

  private Long startId;

  private Long endId;

  private Integer size = 100;

  public PubRebuildPublishDateTask() {
    super();
  }

  public PubRebuildPublishDateTask(String beanName) {
    super(beanName);
  }


  public void doRun() {
    try {
      if (!super.isAllowExecution()) {
        return;
      }
      while (true) {
        List<PubDataTaskPO> pubList = pubRepairService.findPubId(startId, endId, size);
        if (CollectionUtils.isNotEmpty(pubList)) {
          for (PubDataTaskPO pubData : pubList) {
            try {
              pubRepairService.rebuildSnsPublishDate(pubData);
            } catch (Exception e) {
              logger.error("重构个人库出版日期数据出错！", e);
              pubData.setError(StringUtils.substring(e.getMessage(), 0, 500));
              pubData.setStatus(-1);
              pubRepairService.save(pubData);
            }
          }
        } else {
          break;
        }
      }
    } catch (Exception e) {
      logger.error("PubRebuildPublishDateTask运行异常", e);
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
