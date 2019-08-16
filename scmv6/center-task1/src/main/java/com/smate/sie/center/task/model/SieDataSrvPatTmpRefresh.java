package com.smate.sie.center.task.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@SuppressWarnings("serial")
@Entity
@Table(name = "DATA_SRV_PAT_TMP_REFRESH")
public class SieDataSrvPatTmpRefresh implements Serializable {

  @Id
  @Column(name = "PAT_ID")
  private Long patId;

  @Column(name = "STATUS")
  private Integer status;

  public SieDataSrvPatTmpRefresh() {
    super();
  }

  public SieDataSrvPatTmpRefresh(Long patId, Integer status) {
    super();
    this.patId = patId;
    this.status = status;
  }

  public Long getPatId() {
    return patId;
  }

  public Integer getStatus() {
    return status;
  }

  public void setPatId(Long patId) {
    this.patId = patId;
  }

  public void setStatus(Integer status) {
    this.status = status;
  }

}
