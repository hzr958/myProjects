package com.smate.center.task.model.snsbak.bdsp;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "BDSP_PROJECT_PERSON")
public class BdspProjectPerson implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = -8431918065459710283L;
  @Id
  @Column(name = "PRJ_CODE")
  private Long prjCode;
  @Column(name = "ZH_NAME")
  private String zhName;
  @Column(name = "ORG_NAME")
  private String orgName;
  @Column(name = "COUNTRY")
  private String country;
  @Column(name = "PROVINCE")
  private String province;
  @Column(name = "CITY")
  private String city;
  @Column(name = "COUNTY")
  private String county;
  @Column(name = "COUNTRY_ID")
  private String countryId;
  @Column(name = "PROVINCE_ID")
  private String provinceId;
  @Column(name = "CITY_ID")
  private String cityId;
  @Column(name = "COUNTY_ID")
  private String countyId;



  public BdspProjectPerson(String zhName, String orgName) {
    super();
    this.zhName = zhName;
    this.orgName = orgName;
  }

  public BdspProjectPerson() {
    super();
  }

  public BdspProjectPerson(Long prjCode, String zhName, String orgName, String country, String province, String city,
      String county, String countryId, String provinceId, String cityId, String countyId) {
    super();
    this.prjCode = prjCode;
    this.zhName = zhName;
    this.orgName = orgName;
    this.country = country;
    this.province = province;
    this.city = city;
    this.county = county;
    this.countryId = countryId;
    this.provinceId = provinceId;
    this.cityId = cityId;
    this.countyId = countyId;
  }

  public Long getPrjCode() {
    return prjCode;
  }

  public void setPrjCode(Long prjCode) {
    this.prjCode = prjCode;
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

  public String getCountry() {
    return country;
  }

  public void setCountry(String country) {
    this.country = country;
  }

  public String getProvince() {
    return province;
  }

  public void setProvince(String province) {
    this.province = province;
  }

  public String getCity() {
    return city;
  }

  public void setCity(String city) {
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

  public String getProvinceId() {
    return provinceId;
  }

  public void setProvinceId(String provinceId) {
    this.provinceId = provinceId;
  }

  public String getCityId() {
    return cityId;
  }

  public void setCityId(String cityId) {
    this.cityId = cityId;
  }

  public String getCountyId() {
    return countyId;
  }

  public void setCountyId(String countyId) {
    this.countyId = countyId;
  }

}
