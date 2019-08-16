package com.smate.sie.center.task.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "PUB_PDWH_SIE_RELATION")
public class PubPdwhSieRelation implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = 627854886088064783L;

  @Id
  @Column(name = "SIE_PUB_ID")
  private Long siePubId;

  @Column(name = "PDWH_PUB_ID")
  private Long pdwhPubId;

  @Column(name = "PDWH_FILE_ID")
  private String pdwhFileId;

  public Long getSiePubId() {
    return siePubId;
  }

  public void setSiePubId(Long siePubId) {
    this.siePubId = siePubId;
  }

  public Long getPdwhPubId() {
    return pdwhPubId;
  }

  public void setPdwhPubId(Long pdwhPubId) {
    this.pdwhPubId = pdwhPubId;
  }

  public String getPdwhFileId() {
    return pdwhFileId;
  }

  public void setPdwhFileId(String pdwhFileId) {
    this.pdwhFileId = pdwhFileId;
  }

}
