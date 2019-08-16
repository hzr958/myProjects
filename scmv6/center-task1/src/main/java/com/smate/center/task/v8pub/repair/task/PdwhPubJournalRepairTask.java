package com.smate.center.task.v8pub.repair.task;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.smate.center.task.base.TaskAbstract;
import com.smate.center.task.v8pub.pdwh.po.PdwhDataTaskPO;
import com.smate.center.task.v8pub.service.PubRepairService;

/**
 * 基准库成果期刊数据修复
 * 
 * @author YJ
 *
 *         2019年7月22日
 */
public class PdwhPubJournalRepairTask extends TaskAbstract {

  @Autowired
  private PubRepairService pubRepairService;

  private Long startId;

  private Long endId;

  private Integer size = 100;

  public PdwhPubJournalRepairTask() {
    super();
  }

  public PdwhPubJournalRepairTask(String beanName) {
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
              pubRepairService.repairPdwhJournal(pubData);
              pubData.setStatus(1);
            } catch (Exception e) {
              logger.error("修复基准库期刊数据出错！pubId={}", pubData.getPubId(), e);
              pubData.setError(StringUtils.substring(e.getMessage(), 0, 500));
              pubData.setStatus(-1);
            }
            pubRepairService.save(pubData);
          }
        } else {
          super.closeOneTimeTask();
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
