package com.smate.center.task.v8pub.repair.task;

import com.smate.center.task.base.TaskAbstract;
import com.smate.center.task.v8pub.pdwh.po.PdwhDataTaskPO;
import com.smate.center.task.v8pub.service.DataTaskService;
import com.smate.center.task.v8pub.service.PubRepairService;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class RepairPdwhPublishDateDataTask extends TaskAbstract {

  @Autowired
  private PubRepairService pubRepairService;
  @Autowired
  private DataTaskService dataTaskService;

  private Long startId;

  private Long endId;


  public RepairPdwhPublishDateDataTask() {
    super();
  }

  public RepairPdwhPublishDateDataTask(String beanName) {
    super(beanName);
  }

  private Integer SIZE = 100;

  public void doRun() {
    try {
      if (!super.isAllowExecution()) {
        return;
      }
      while (true) {
        List<PdwhDataTaskPO> pubList = pubRepairService.findPdwhId(SIZE,startId,endId);
        if (CollectionUtils.isNotEmpty(pubList)) {
          for (PdwhDataTaskPO pdwhData : pubList) {
            try {
              pubRepairService.rebuildPdwhPublishDate(pdwhData);
              pdwhData.setStatus(1);
            } catch (Exception e) {
              logger.error("修复基准库成果年份数据出错！", e);
              pdwhData.setStatus(-1);
              pdwhData.setError(StringUtils.substring(e.getMessage(), 0, 500));
            }
            dataTaskService.save(pdwhData);
          }
        } else {
          break;
        }
      }
    } catch (Exception e) {
      logger.error("RepairPdwhPubYearDataTask运行异常", e);
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
