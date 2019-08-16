package com.smate.sie.center.task.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@SuppressWarnings("serial")
@Entity
@Table(name = "DATA_SRV_PUB_TMP_REFRESH")
public class SieDataSrvPubTmpRefresh implements Serializable {

  @Id
  @Column(name = "PUB_ID")
  private Long pubId;

  @Column(name = "STATUS")
  private Integer status;

  public SieDataSrvPubTmpRefresh() {
    super();
  }

  public SieDataSrvPubTmpRefresh(Long pubId, Integer status) {
    super();
    this.pubId = pubId;
    this.status = status;
  }

  public Long getPubId() {
    return pubId;
  }

  public Integer getStatus() {
    return status;
  }

  public void setPubId(Long pubId) {
    this.pubId = pubId;
  }

  public void setStatus(Integer status) {
    this.status = status;
  }

}
