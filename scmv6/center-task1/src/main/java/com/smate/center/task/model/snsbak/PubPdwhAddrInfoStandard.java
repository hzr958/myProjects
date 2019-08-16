package com.smate.center.task.model.snsbak;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "PUB_PDWH_ADDR_STANDARD")
public class PubPdwhAddrInfoStandard implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = -2580161225993923901L;

  private Long id;
  private Long pdwhPubId;
  private Integer pubType;
  private Integer dbId;
  private Integer pubYear;
  private String addr;
  private Integer insId;
  private Integer cityId;
  private Integer provinceId;
  private Integer countryId;

  public PubPdwhAddrInfoStandard() {

  }

  public PubPdwhAddrInfoStandard(Integer pubYear, Integer provinceId) {
    this.pubYear = pubYear;
    this.provinceId = provinceId;
  }

  @Id
  @Column(name = "ID")
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

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

  @Column(name = "ADDR")
  public String getAddr() {
    return addr;
  }

  public void setAddr(String addr) {
    this.addr = addr;
  }

  @Column(name = "INS_ID")
  public Integer getInsId() {
    return insId;
  }

  public void setInsId(Integer insId) {
    this.insId = insId;
  }

  @Column(name = "CITY_ID")
  public Integer getCityId() {
    return cityId;
  }

  public void setCityId(Integer cityId) {
    this.cityId = cityId;
  }

  @Column(name = "PROVINCE_ID")
  public Integer getProvinceId() {
    return provinceId;
  }

  public void setProvinceId(Integer provinceId) {
    this.provinceId = provinceId;
  }

  @Column(name = "COUNTRY_ID")
  public Integer getCountryId() {
    return countryId;
  }

  public void setCountryId(Integer countryId) {
    this.countryId = countryId;
  }

}
