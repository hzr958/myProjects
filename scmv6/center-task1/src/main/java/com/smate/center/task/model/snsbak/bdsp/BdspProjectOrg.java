package com.smate.center.task.model.snsbak.bdsp;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "BDSP_PROJECT_ORG")
public class BdspProjectOrg implements Serializable {

  private static final long serialVersionUID = 1L;
  @Id
  @Column(name = "PRJ_CODE")
  private Long prjCode;
  @Column(name = "ORG_ZH_NAME")
  private String orgZhName;
  @Column(name = "ORG_EN_NAME")
  private String orgEnName;
  @Column(name = "PROVINCE_ID")
  private Long provinceId;
  @Column(name = "CITY_ID")
  private Long cityId;
  @Column(name = "ORIGINAL_ID")
  private Long originalId;

  public BdspProjectOrg() {
    super();
  }

  public BdspProjectOrg(Long prjCode, String orgZhName, String orgEnName, Long provinceId, Long cityId,
      Long originalId) {
    super();
    this.prjCode = prjCode;
    this.orgZhName = orgZhName;
    this.orgEnName = orgEnName;
    this.provinceId = provinceId;
    this.cityId = cityId;
    this.originalId = originalId;
  }

  public BdspProjectOrg(Long prjCode, String orgZhName, Long provinceId) {
    super();
    this.prjCode = prjCode;
    this.orgZhName = orgZhName;
    this.provinceId = provinceId;

  }

  public Long getOriginalId() {
    return originalId;
  }

  public void setOriginalId(Long originalId) {
    this.originalId = originalId;
  }

  public Long getPrjCode() {
    return prjCode;
  }

  public void setPrjCode(Long prjCode) {
    this.prjCode = prjCode;
  }

  public String getOrgZhName() {
    return orgZhName;
  }

  public void setOrgZhName(String orgZhName) {
    this.orgZhName = orgZhName;
  }

  public String getOrgEnName() {
    return orgEnName;
  }

  public void setOrgEnName(String orgEnName) {
    this.orgEnName = orgEnName;
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

}
