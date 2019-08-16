package com.smate.center.task.model.snsbak.bdsp;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "BDSP_PROPOSAL_PERSON")
public class BdspProposalPerson implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = -8431918065459710283L;
  @Id
  @Column(name = "PRP_CODE")
  private Long prpCode;
  @Column(name = "ZH_NAME")
  private String zhName;
  @Column(name = "ORG_NAME")
  private String orgName;
  @Column(name = "COUNTRY")
  private Long country;
  @Column(name = "PROVINCE")
  private Long province;
  @Column(name = "CITY")
  private Long city;
  @Column(name = "COUNTY")
  private String county;
  @Column(name = "COUNTRY_ID")
  private String countryId;
  @Column(name = "PROVINCE_ID")
  private Long provinceId;
  @Column(name = "CITY_ID")
  private Long cityId;
  @Column(name = "COUNTY_ID")
  private Long countyId;

  public Long getPrpCode() {
    return prpCode;
  }

  public void setPrpCode(Long prpCode) {
    this.prpCode = prpCode;
  }

  public String getZhName() {
    return zhName;
  }

  public void setZhName(String zhName) {
    this.zhName = zhName;
  }

  public String getOrgName() {
    return orgName;
  }

  public void setOrgName(String orgName) {
    this.orgName = orgName;
  }

  public Long getCountry() {
    return country;
  }

  public void setCountry(Long country) {
    this.country = country;
  }

  public Long getProvince() {
    return province;
  }

  public void setProvince(Long province) {
    this.province = province;
  }

  public Long getCity() {
    return city;
  }

  public void setCity(Long city) {
    this.city = city;
  }

  public String getCounty() {
    return county;
  }

  public void setCounty(String county) {
    this.county = county;
  }

  public String getCountryId() {
    return countryId;
  }

  public void setCountryId(String countryId) {
    this.countryId = countryId;
  }

  public Long getProvinceId() {
    return provinceId;
  }

  public void setProvinceId(Long provinceId) {
    this.provinceId = provinceId;
  }

  public Long getCityId() {
    return cityId;
  }

  public void setCityId(Long cityId) {
    this.cityId = cityId;
  }

  public Long getCountyId() {
    return countyId;
  }

  public void setCountyId(Long countyId) {
    this.countyId = countyId;
  }

}
