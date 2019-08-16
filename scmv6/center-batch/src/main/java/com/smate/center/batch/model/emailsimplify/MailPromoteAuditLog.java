package com.smate.center.batch.model.emailsimplify;

import java.io.Serializable;
import java.util.Date;

/**
 * 推广邮件审核日志相关信息
 * 
 * @author zk
 * 
 */
public class MailPromoteAuditLog implements Serializable {
  /**
   * 
   */
  private static final long serialVersionUID = 4659500756261233014L;
  private Integer tempId;
  private Integer prepareNum;
  private Integer actualNum;
  private Date auditDate;

  public MailPromoteAuditLog() {}

  public MailPromoteAuditLog(Integer tempId, Integer prepareNum, Integer actualNum, Date auditDate) {
    this.tempId = tempId;
    this.prepareNum = prepareNum;
    this.actualNum = actualNum;
    this.auditDate = auditDate;
  }

  public Integer getTempId() {
    return tempId;
  }

  public Integer getPrepareNum() {
    return prepareNum;
  }

  public Integer getActualNum() {
    return actualNum;
  }

  public Date getAuditDate() {
    return auditDate;
  }

  public void setTempId(Integer tempId) {
    this.tempId = tempId;
  }

  public void setPrepareNum(Integer prepareNum) {
    this.prepareNum = prepareNum;
  }

  public void setActualNum(Integer actualNum) {
    this.actualNum = actualNum;
  }

  public void setAuditDate(Date auditDate) {
    this.auditDate = auditDate;
  }

}
