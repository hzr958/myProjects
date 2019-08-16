package com.smate.center.batch.model.emailsimplify;

import java.io.Serializable;
import java.util.Date;

/**
 * 推广邮件统计记录model
 * 
 * @author zk
 * 
 */
public class MailPromoteStatLog implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = -6382516480144340188L;
  private Integer tempId;
  private Integer successNum;
  private Integer openSum;
  private Date auditDate;
  private Date lastDate;

  public MailPromoteStatLog() {}

  public MailPromoteStatLog(Integer tempId, Integer successNum, Integer openSum, Date auditDate, Date lastDate) {
    this.tempId = tempId;
    this.successNum = successNum;
    this.openSum = openSum;
    this.auditDate = auditDate;
    this.lastDate = lastDate;
  }

  public Integer getTempId() {
    return tempId;
  }

  public Integer getSuccessNum() {
    return successNum;
  }

  public Integer getOpenSum() {
    return openSum;
  }

  public Date getAuditDate() {
    return auditDate;
  }

  public Date getLastDate() {
    return lastDate;
  }

  public void setTempId(Integer tempId) {
    this.tempId = tempId;
  }

  public void setSuccessNum(Integer successNum) {
    this.successNum = successNum;
  }

  public void setOpenSum(Integer openSum) {
    this.openSum = openSum;
  }

  public void setAuditDate(Date auditDate) {
    this.auditDate = auditDate;
  }

  public void setLastDate(Date lastDate) {
    this.lastDate = lastDate;
  }

}
