package com.smate.center.batch.model.pdwh.pubimport;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "TMP_PUBLICATION_ALL")
public class TmpPublicationAll implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = -4642385326779598283L;

  @Id
  @Column(name = "id")
  private Long pubAllId;
  @Column(name = "pub_id")
  private Long pubId;
  @Column(name = "dbid")
  private Integer dbId;
  @Column(name = "status")
  private Integer status;

  public TmpPublicationAll() {
    super();
  }

  public Long getPubAllId() {
    return pubAllId;
  }

  public void setPubAllId(Long pubAllId) {
    this.pubAllId = pubAllId;
  }

  public Long getPubId() {
    return pubId;
  }

  public void setPubId(Long pubId) {
    this.pubId = pubId;
  }

  public Integer getDbId() {
    return dbId;
  }

  public void setDbId(Integer dbId) {
    this.dbId = dbId;
  }

  public Integer getStatus() {
    return status;
  }

  public void setStatus(Integer status) {
    this.status = status;
  }

}
