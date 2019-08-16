package com.smate.center.task.journal.task;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.smate.center.task.base.TaskAbstract;
import com.smate.center.task.exception.TaskException;
import com.smate.center.task.service.journal.BaseJournalTitleRepairService;

public class BaseJournalTitleRepairTask extends TaskAbstract {

  @Autowired
  private BaseJournalTitleRepairService baseJournalTitleRepairService;

  public BaseJournalTitleRepairTask() {
    super();
  }

  public BaseJournalTitleRepairTask(String beanName) {
    super(beanName);
  }

  public void doRun() {
    if (!isAllowExecution()) {
      return;
    }
    try {
      closeOneTimeTask();
    } catch (TaskException e) {
      logger.error("BaseJournalTitleRepairTask关闭失败", e);
      e.printStackTrace();
    }
    List<Map<String, Object>> needRepairJnlIds = baseJournalTitleRepairService.getNeedRepairJnlIds();
    if (CollectionUtils.isNotEmpty(needRepairJnlIds)) {
      for (Map<String, Object> jnlIdmap : needRepairJnlIds) {
        if (jnlIdmap != null && jnlIdmap.get("JNL_ID") != null) {
          try {
            baseJournalTitleRepairService.repairData(((BigDecimal) jnlIdmap.get("JNL_ID")).longValue());
          } catch (Exception e) {
            logger.error("BaseJournalTitleRepairTask任务异常,jnlId:" + jnlIdmap.get("JNL_ID"), e);
          }
        }
      }
    }
  }
}
