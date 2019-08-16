package com.smate.center.batch.model.pdwh.pub.isi;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 需要同步冗余数据.
 * 
 * @author pwl
 * 
 */
@Entity
@Table(name = "ISI_PUBLICATION_SYNC")
public class IsiPublicationSync implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = 8515228814170684799L;

  public Long pubId;

  public Integer status;

  @Id
  @Column(name = "PUB_ID")
  public Long getPubId() {
    return pubId;
  }

  public void setPubId(Long pubId) {
    this.pubId = pubId;
  }

  @Column(name = "STATUS")
  public Integer getStatus() {
    return status;
  }

  public void setStatus(Integer status) {
    this.status = status;
  }

}
