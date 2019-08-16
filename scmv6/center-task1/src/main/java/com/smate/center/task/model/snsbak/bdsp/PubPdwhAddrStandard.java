package com.smate.center.task.model.snsbak.bdsp;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Table(name = "PUB_PDWH_ADDR_STANDARD")
public class PubPdwhAddrStandard implements Serializable {

  private static final long serialVersionUID = 1L;
  @Id
  @Column(name = "ID")
  private Long id;
  @Column(name = "PDWH_PUB_ID")
  private Long pdwhPubId;
  @Column(name = "PUB_TYPE")
  private Long pubType;
  @Column(name = "DB_ID")
  private Long dbId;
  @Column(name = "PUB_YEAR")
  private Long pubYear;
  @Column(name = "ADDR")
  private String addr;
  @Column(name = "INS_ID")
  private Long insId;
  @Column(name = "CITY_ID")
  private Long cityId;
  @Column(name = "PROVINCE_ID")
  private Long provinceId;
  @Column(name = "COUNTRY_ID")
  private Long countryId;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Long getPdwhPubId() {
    return pdwhPubId;
  }

  public void setPdwhPubId(Long pdwhPubId) {
    this.pdwhPubId = pdwhPubId;
  }

  public Long getPubType() {
    return pubType;
  }

  public void setPubType(Long pubType) {
    this.pubType = pubType;
  }

  public Long getDbId() {
    return dbId;
  }

  public void setDbId(Long dbId) {
    this.dbId = dbId;
  }

  public Long getPubYear() {
    return pubYear;
  }

  public void setPubYear(Long pubYear) {
    this.pubYear = pubYear;
  }

  public String getAddr() {
    return addr;
  }

  public void setAddr(String addr) {
    this.addr = addr;
  }

  public Long getInsId() {
    return insId;
  }

  public void setInsId(Long insId) {
    this.insId = insId;
  }

  public Long getCityId() {
    return cityId;
  }

  public void setCityId(Long cityId) {
    this.cityId = cityId;
  }

  public Long getProvinceId() {
    return provinceId;
  }

  public void setProvinceId(Long provinceId) {
    this.provinceId = provinceId;
  }

  public Long getCountryId() {
    return countryId;
  }

  public void setCountryId(Long countryId) {
    this.countryId = countryId;
  }

}
