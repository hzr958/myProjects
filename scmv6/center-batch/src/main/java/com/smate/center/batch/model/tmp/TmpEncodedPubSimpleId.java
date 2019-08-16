package com.smate.center.batch.model.tmp;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * isi成果导出表，关键词，abstract
 * 
 * @author lichangwen
 * 
 */
@Entity
@Table(name = "TMP_PUB_SIMPE_ID")
public class TmpEncodedPubSimpleId implements Serializable {



  /**
   * 
   */
  private static final long serialVersionUID = 6014065888108686612L;

  @Id
  @Column(name = "PUB_ID")
  private Long pubId;

  @Column(name = "ENCODED_PUB_ID")
  private String encodedPubId;

  public Long getPubId() {
    return pubId;
  }

  public void setPubId(Long pubId) {
    this.pubId = pubId;
  }

  public String getEncodedPubId() {
    return encodedPubId;
  }

  public void setEncodedPubId(String encodedPubId) {
    this.encodedPubId = encodedPubId;
  }

}
