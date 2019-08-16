package com.smate.center.task.model.pdwh.pub;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "PDWH_PUB_CITED_TIMES")
public class PdwhPubCitedTimes implements Serializable {
  /**
   * 
   */
  private static final long serialVersionUID = -2297723976704566732L;
  private Long Id;
  private Long pdwhPubId;
  private Integer dbId;
  private Integer citedTimes;
  private Date updateDate;
  private Integer type;// 手动更新1，后台更新0

  public PdwhPubCitedTimes() {
    super();
  }

  public PdwhPubCitedTimes(Long id, Long pdwhPubId, Integer dbId, Integer citedTimes, Date updateDate, Integer type) {
    super();
    Id = id;
    this.pdwhPubId = pdwhPubId;
    this.dbId = dbId;
    this.citedTimes = citedTimes;
    this.updateDate = updateDate;
    this.type = type;
  }

  @Id
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_PDWH_PUB_CITED_TIMES", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
  @Column(name = "ID")
  public Long getId() {
    return Id;
  }

  public void setId(Long id) {
    Id = id;
  }

  @Column(name = "DB_ID")
  public Integer getDbId() {
    return dbId;
  }

  public void setDbId(Integer dbId) {
    this.dbId = dbId;
  }

  @Column(name = "PDWH_PUB_ID")
  public Long getPdwhPubId() {
    return pdwhPubId;
  }

  public void setPdwhPubId(Long pdwhPubId) {
    this.pdwhPubId = pdwhPubId;
  }

  @Column(name = "CITED_TIMES")
  public int getCitedTimes() {
    return citedTimes;
  }

  public void setCitedTimes(Integer citedTimes) {
    this.citedTimes = citedTimes;
  }

  @Column(name = "UPDATE_DATE")
  public Date getUpdateDate() {
    return updateDate;
  }

  public void setUpdateDate(Date updateDate) {
    this.updateDate = updateDate;
  }

  @Column(name = "TYPE")
  public int getType() {
    return type;
  }

  public void setType(Integer type) {
    this.type = type;
  }

}
