package com.smate.center.task.model.sns.quartz;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "TEM_TASK_SNSBRIEF")
public class TemTaskSnsBrief {

  private Long pubId;
  private int status = 0;
  private String errMsg;

  public TemTaskSnsBrief(Long pubId, int status, String errMsg) {
    super();
    this.pubId = pubId;
    this.status = status;
    this.errMsg = errMsg;
  }

  public TemTaskSnsBrief() {
    super();
  }

  @Id
  @Column(name = "PUB_ID")
  public Long getPubId() {
    return pubId;
  }

  public void setPubId(Long pubId) {
    this.pubId = pubId;
  }

  @Column(name = "STATUS")
  public int getStatus() {
    return status;
  }

  public void setStatus(int status) {
    this.status = status;
  }

  @Column(name = "ERROR_MSG")
  public String getErrMsg() {
    return errMsg;
  }

  public void setErrMsg(String errMsg) {
    this.errMsg = errMsg;
  }

}
