package com.smate.center.batch.model.pub.pubtopubsimple;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "TMP_PUBID_20160406")
public class TmpPubId implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = 2600822740072643255L;

  @Id
  @Column(name = "PUB_ID")
  private Long pubId;
  @Column(name = "STATUS")
  private Integer status;

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


}
