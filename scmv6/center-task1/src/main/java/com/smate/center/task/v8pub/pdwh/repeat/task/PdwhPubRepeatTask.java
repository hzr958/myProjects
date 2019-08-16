package com.smate.center.task.v8pub.pdwh.repeat.task;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.smate.center.task.base.TaskAbstract;
import com.smate.center.task.v8pub.pdwh.po.PdwhDataTaskPO;
import com.smate.center.task.v8pub.service.PdwhPubRepeatService;

/**
 * 基准库筛选重复成果任务
 * 
 * @author YJ
 *
 *         2019年6月17日
 */
public class PdwhPubRepeatTask extends TaskAbstract {

  @Autowired
  private PdwhPubRepeatService pdwhPubRepeatService;

  private Long startId;

  private Long endId;

  private Integer size = 100;

  public PdwhPubRepeatTask() {
    super();
  }

  public PdwhPubRepeatTask(String beanName) {
    super(beanName);
  }

  public void doRun() {
    try {
      if (!super.isAllowExecution()) {
        return;
      }
      while (true) {
        List<PdwhDataTaskPO> pubList = pdwhPubRepeatService.findPdwhId(size, startId, endId);
        if (CollectionUtils.isNotEmpty(pubList)) {
          for (PdwhDataTaskPO pubData : pubList) {
            try {
              pdwhPubRepeatService.dealWithRepeatPub(pubData);
              pubData.setStatus(1);
            } catch (Exception e) {
              logger.error("基准库重复成果分组处理出错！pubId={}", pubData.getPubId(), e);
              pubData.setError(StringUtils.substring(e.getMessage(), 0, 500));
              pubData.setStatus(-1);
            }
            pdwhPubRepeatService.save(pubData);
          }
        } else {
          break;
        }
      }
    } catch (Exception e) {
      logger.error("PdwhPubRepeatTask运行异常", e);
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
