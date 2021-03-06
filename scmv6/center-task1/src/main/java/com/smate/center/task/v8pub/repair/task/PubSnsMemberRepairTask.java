package com.smate.center.task.v8pub.repair.task;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.smate.center.task.base.TaskAbstract;
import com.smate.center.task.v8pub.service.DataTaskService;
import com.smate.center.task.v8pub.service.PubRepairService;
import com.smate.center.task.v8pub.sns.po.PubDataTaskPO;

/**
 * 个人库作者数据修复的任务
 * 
 * @author YJ
 *
 *         2019年7月23日
 */
public class PubSnsMemberRepairTask extends TaskAbstract {
  @Autowired
  private PubRepairService pubRepairService;
  @Autowired
  private DataTaskService dataTaskService;

  private Long startId;

  private Long endId;


  public PubSnsMemberRepairTask() {
    super();
  }

  public PubSnsMemberRepairTask(String beanName) {
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
              pubRepairService.repairSnsPubMember(pubData);
              pubData.setStatus(1);
            } catch (Exception e) {
              logger.error("修复个人库作者数据出错！", e);
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
      logger.error("PubSnsMemberRepairTask运行异常", e);
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
