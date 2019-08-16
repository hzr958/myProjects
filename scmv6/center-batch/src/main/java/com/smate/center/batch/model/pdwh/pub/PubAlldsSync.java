package com.smate.center.batch.model.pdwh.pub;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * 需要同步到puball的ids
 * 
 * @author lichangwen
 * 
 */
@Entity
@Table(name = "PUB_ALL_IDS_SYNC")
public class PubAlldsSync implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = 7170170194655933289L;
  // 主键
  @Id
  @Column(name = "ID")
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_PUB_ALL_IDS_SYNC", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
  private Long id;
  @Column(name = "PUB_ID")
  private Long pubId;
  @Column(name = "DBID")
  private Integer dbid;
  @Column(name = "STATUS")
  private Integer status;

  public PubAlldsSync() {
    super();
    this.status = 0;
  }

  public PubAlldsSync(Long pubId, Integer dbid) {
    super();
    this.pubId = pubId;
    this.dbid = dbid;
    this.status = 0;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Long getPubId() {
    return pubId;
  }

  public void setPubId(Long pubId) {
    this.pubId = pubId;
  }

  public Integer getDbid() {
    return dbid;
  }

  public void setDbid(Integer dbid) {
    this.dbid = dbid;
  }

  public Integer getStatus() {
    return status;
  }

  public void setStatus(Integer status) {
    this.status = status;
  }

}
