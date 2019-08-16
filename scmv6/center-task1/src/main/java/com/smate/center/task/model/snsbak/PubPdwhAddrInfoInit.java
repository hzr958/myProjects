package com.smate.center.task.model.snsbak;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "PUB_PDWH_ADDR_INIT")
public class PubPdwhAddrInfoInit implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = -8926251414948663057L;

  private Long pdwhPubId;
  private Integer pubType;
  private Integer dbId;
  private Integer pubYear;
  private Integer status;
  private Integer statusInternational;
  private Integer statusCity;

  public PubPdwhAddrInfoInit() {
    super();
  }

  @Id
  @Column(name = "PDWH_PUB_ID")
  public Long getPdwhPubId() {
    return pdwhPubId;
  }

  public void setPdwhPubId(Long pdwhPubId) {
    this.pdwhPubId = pdwhPubId;
  }

  @Column(name = "PUB_TYPE")
  public Integer getPubType() {
    return pubType;
  }

  public void setPubType(Integer pubType) {
    this.pubType = pubType;
  }

  @Column(name = "DB_ID")
  public Integer getDbId() {
    return dbId;
  }

  public void setDbId(Integer dbId) {
    this.dbId = dbId;
  }

  @Column(name = "PUB_YEAR")
  public Integer getPubYear() {
    return pubYear;
  }

  public void setPubYear(Integer pubYear) {
    this.pubYear = pubYear;
  }

  @Column(name = "STATUS")
  public Integer getStatus() {
    return status;
  }

  public void setStatus(Integer status) {
    this.status = status;
  }

  @Column(name = "STATUS_COUNTRY")
  public Integer getStatusInternational() {
    return statusInternational;
  }

  public void setStatusInternational(Integer statusInternational) {
    this.statusInternational = statusInternational;
  }

  @Column(name = "STATUS_CITY")
  public Integer getStatusCity() {
    return statusCity;
  }

  public void setStatusCity(Integer statusCity) {
    this.statusCity = statusCity;
  }

}
