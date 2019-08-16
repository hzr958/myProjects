package com.smate.sie.center.task.service;

import java.util.List;
import java.util.Map;

/***
 * 
 * @author 叶星源
 * @Date 201903
 */
public interface SieAuditLogService {
  /**
   * 获取待解析的日志记录.
   */
  public List<Map<String, Object>> getAuditLogList(Integer fetchSize);

  /**
   * 解析日志记录.
   */
  public void parseAuditLog(Map<String, Object> auditLog) throws Exception;

  /**
   * 修改com_audit_trail日志表状态.
   */
  public void updateAuditTrail(Long audId, Integer status);

}
