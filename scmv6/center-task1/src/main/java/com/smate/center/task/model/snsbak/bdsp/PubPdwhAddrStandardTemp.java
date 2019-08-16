package com.smate.center.task.model.snsbak.bdsp;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "PUB_PDWH_ADDR_STANDARD_TEMP")
public class PubPdwhAddrStandardTemp implements Serializable {

  private static final long serialVersionUID = 1L;
  @Id
  @Column(name = "ID")
  private Long id;
  @Column(name = "PUB_TYPE")
  private Long pubType;
  @Column(name = "STAT_YEAR")
  private Long statYear;
  @Column(name = "PROV_ID")
  private Long provId;
  @Column(name = "CITY_ID")
  private Long cityId;
  @Column(name = "GRANT_TYPE_ID")
  private Long grantTypeId;
  @Column(name = "TECHFILED_ID")
  private Long techfiledId;
  @Column(name = "ORG_ID")
  private Long orgId;
  @Column(name = "PUB_ID")
  private Long pubId;

  public Long getPubId() {
    return pubId;
  }

  public void setPubId(Long pubId) {
    this.pubId = pubId;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Long getPubType() {
    return pubType;
  }

  public void setPubType(Long pubType) {
    this.pubType = pubType;
  }

  public Long getStatYear() {
    return statYear;
  }

  public void setStatYear(Long statYear) {
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

  public Long getTechfiledId() {
    return techfiledId;
  }

  public void setTechfiledId(Long techfiledId) {
    this.techfiledId = techfiledId;
  }

  public Long getOrgId() {
    return orgId;
  }

  public void setOrgId(Long orgId) {
    this.orgId = orgId;
  }

}
