package com.smate.center.task.model.pdwh.quartz;

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
@Table(name = "TMP_PUBLICATION_ALL_SNSGROUP")
public class TmpPublicationForSnsGroup implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = 266737839239812905L;

  @Id
  @Column(name = "ID")
  private Long pubAllId;

  @Column(name = "PUB_ID")
  private Long pubId;

  @Column(name = "DBID")
  private Integer dbId;

  @Column(name = "STATUS")
  private Integer status;

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
