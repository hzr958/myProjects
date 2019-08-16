package com.smate.center.batch.model.emailsimplify;

import java.io.Serializable;
import java.util.Date;

/**
 * 
 * 推广邮件相关信息，
 * 
 * @author zk
 * 
 */
public class MailPromoteInfo implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = -7862950060830921937L;

  private Integer tempId;
  private String tempName;
  private String tempSubject;
  private Integer startStatus;
  private Integer sendStatus;
  private Date updateDate;

  public Integer getTempId() {
    return tempId;
  }

  public String getTempName() {
    return tempName;
  }

  public String getTempSubject() {
    return tempSubject;
  }

  public Integer getStartStatus() {
    return startStatus;
  }

  public Integer getSendStatus() {
    return sendStatus;
  }

  public Date getUpdateDate() {
    return updateDate;
  }

  public void setUpdateDate(Date updateDate) {
    this.updateDate = updateDate;
  }

  public void setTempId(Integer tempId) {
    this.tempId = tempId;
  }

  public void setTempName(String tempName) {
    this.tempName = tempName;
  }

  public void setTempSubject(String tempSubject) {
    this.tempSubject = tempSubject;
  }

  public void setStartStatus(Integer startStatus) {
    this.startStatus = startStatus;
  }

  public void setSendStatus(Integer sendStatus) {
    this.sendStatus = sendStatus;
  }

}
