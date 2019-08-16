package com.smate.center.task.v8pub.repair.task;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.smate.center.task.base.TaskAbstract;
import com.smate.center.task.v8pub.pdwh.po.PdwhDataTaskPO;
import com.smate.center.task.v8pub.service.PubRepairService;

public class PdwhPerfectMemberIdTask extends TaskAbstract {

  private Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private PubRepairService pubRepairService;

  private Long startId;

  private Long endId;

  private Integer size = 100;

  public PdwhPerfectMemberIdTask() {
    super();
  }

  public PdwhPerfectMemberIdTask(String beanName) {
    super(beanName);
  }


  public void doRun() {
    try {
      if (!super.isAllowExecution()) {
        return;
      }
      while (true) {
        List<PdwhDataTaskPO> pubList = pubRepairService.findPdwhId(size, startId, endId);
        if (CollectionUtils.isNotEmpty(pubList)) {
          for (PdwhDataTaskPO pubData : pubList) {
            try {
              pubRepairService.perferPdwhMemberId(pubData);
            } catch (Exception e) {
              logger.error("完善基准库作者id字段数据出错！", e);
              pubData.setError(StringUtils.substring(e.getMessage(), 0, 500));
              pubData.setStatus(-1);
            }
            pubRepairService.save(pubData);
          }
        } else {
          break;
        }
      }
    } catch (Exception e) {
      logger.error("PdwhPerfectMemberIdTask运行异常", e);
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
