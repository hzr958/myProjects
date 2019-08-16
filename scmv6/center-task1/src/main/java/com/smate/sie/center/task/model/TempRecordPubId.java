package com.smate.sie.center.task.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "TEMP_RECORD_PUB_ID")
public class TempRecordPubId implements Serializable {

  private static final long serialVersionUID = -8071654336738481535L;

  private Long pubId;
  private String status;

  public TempRecordPubId() {
    super();
  }

  public TempRecordPubId(Long pubId) {
    super();
    this.pubId = pubId;
  }

  public TempRecordPubId(Long pubId, String status) {
    super();
    this.pubId = pubId;
    this.status = status;
  }

  @Id
  @Column(name = "PUB_ID")
  public Long getPubId() {
    return pubId;
  }

  @Column(name = "STATUS")
  public String getStatus() {
    return status;
  }

  public void setPubId(Long pubId) {
    this.pubId = pubId;
  }

  public void setStatus(String status) {
    this.status = status;
  }

}
