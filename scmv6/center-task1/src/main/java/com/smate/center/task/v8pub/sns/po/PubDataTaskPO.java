package com.smate.center.task.v8pub.sns.po;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "PUB_INITDATA_TASK")
public class PubDataTaskPO {

  @Id
  @Column(name = "PUB_ID")
  private Long pubId; // 成果id
  @Column(name = "STATUS")
  private Integer status; // 0为未处理，1为成功，-1为处理失败
  @Column(name = "ERROR")
  private String error; // 失败的错误信息

  public Long getPubId() {
    return pubId;
  }

  public void setPubId(Long pubId) {
    this.pubId = pubId;
  }

  public Integer getStatus() {
    return status;
  }

  public void setStatus(Integer status) {
    this.status = status;
  }

  public String getError() {
    return error;
  }

  public void setError(String error) {
    this.error = error;
  }

}
