package com.smate.sie.center.task.quartz;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;

import com.smate.center.task.base.TaskAbstract;
import com.smate.sie.center.task.service.SieAuditLogService;

/**
 * AOP切面编程日志记录分析任务
 * 
 * @author 叶星源
 * @Date 201903
 */
public class SieAuditLogTask extends TaskAbstract {

  private Logger logger = LoggerFactory.getLogger(getClass());
  private static final int SIZE = 5;

  @Autowired
  private SieAuditLogService auditLogService;

  public SieAuditLogTask() {
    super();
  }

  public SieAuditLogTask(String beanName) {
    super(beanName);
  }

  public void doRun() throws Exception {
    if (!super.isAllowExecution()) {
      return;
    }
    List<Map<String, Object>> list = auditLogService.getAuditLogList(SIZE);
    if (ObjectUtils.isEmpty(list)) {
      return;
    }
    for (Map<String, Object> map : list) {
      Integer status = 0;
      try {
        auditLogService.parseAuditLog(map);
        status = 1;
      } catch (Throwable e) {
        status = 9;
      } finally {
        auditLogService.updateAuditTrail(getId(map), status);
      }
    }
  }

  /**
   * 获取com_audit_trail表的 主键aud_id
   */
  private Long getId(Map<String, Object> map) {
    Long auditLogId = 0L;
    if (map != null && map.get("AUD_ID") != null) {
      auditLogId = Long.valueOf(map.get("AUD_ID").toString());
    }
    return auditLogId;
  }
}
