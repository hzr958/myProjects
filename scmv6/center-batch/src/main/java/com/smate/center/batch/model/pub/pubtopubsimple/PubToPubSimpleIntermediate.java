package com.smate.center.batch.model.pub.pubtopubsimple;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "PUB_TOPUBSIMPLE_INTERMEDIATE")
public class PubToPubSimpleIntermediate implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = 520600764146423824L;

  private Long pubId;
  private Integer status;

  @Id
  @Column(name = "pub_id")
  public Long getPubId() {
    return pubId;
  }

  public void setPubId(Long pubId) {
    this.pubId = pubId;
  }

  @Column(name = "status")
  public Integer getStatus() {
    return status;
  }

  public void setStatus(Integer status) {
    this.status = status;
  }



}


