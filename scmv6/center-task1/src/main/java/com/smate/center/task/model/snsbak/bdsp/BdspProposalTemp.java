package com.smate.center.task.model.snsbak.bdsp;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "BDSP_PROPOSAL_TEMP")
public class BdspProposalTemp implements Serializable {
  private static final long serialVersionUID = 1L;
  @Id
  @Column(name = "PRP_CODE")
  private Long prpCode;// 主键
  @Column(name = "STAT_YEAR")
  private String statYear;// 年份
  @Column(name = "PROV_ID")
  private Long provId;// 省id
  @Column(name = "CITY_ID")
  private Long cityId;// 城市id
  @Column(name = "GRANT_TYPE_ID")
  private Long grantTypeId;// 资助类别
  @Column(name = "TECHFILED_ID")
  private String techfiledId;// 学科领域
  @Column(name = "ORG_ID")
  private Long orgId;// 所在单位ID
  @Column(name = "ORG_NAME")
  private String orgName;// 所在单位名称
  @Column(name = "REQ_AMT")
  private Double reqAmt;


  public Double getReqAmt() {
    return reqAmt;
  }

  public void setReqAmt(Double reqAmt) {
    this.reqAmt = reqAmt;
  }

  public Long getPrpCode() {
    return prpCode;
  }

  public void setPrpCode(Long prpCode) {
    this.prpCode = prpCode;
  }

  public String getStatYear() {
    return statYear;
  }

  public void setStatYear(String statYear) {
    this.statYear = statYear;
  }

  public Long getProvId() {
    return provId;
  }

  public void setProvId(Long provId) {
    this.provId = provId;
  }

  public Long getCityId() {
    return cityId;
  }

  public void setCityId(Long cityId) {
    this.cityId = cityId;
  }

  public Long getGrantTypeId() {
    return grantTypeId;
  }

  public void setGrantTypeId(Long grantTypeId) {
    this.grantTypeId = grantTypeId;
  }

  public String getTechfiledId() {
    return techfiledId;
  }

  public void setTechfiledId(String techfiledId) {
    this.techfiledId = techfiledId;
  }

  public Long getOrgId() {
    return orgId;
  }

  public void setOrgId(Long orgId) {
    this.orgId = orgId;
  }

  public String getOrgName() {
    return orgName;
  }

  public void setOrgName(String orgName) {
    this.orgName = orgName;
  }


}
