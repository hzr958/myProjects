package com.smate.center.task.model.bdsp;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 单位常量
 * 
 * @author zzx
 *
 */
@Entity
@Table(name = "BDSP_INS_CONSTANT")
public class BdspInsConstant implements Serializable {
  private static final long serialVersionUID = 1L;
  /**
   * 单位id
   */
  @Id
  @Column(name = "INS_ID")
  private Long insId;
  /**
   * 单位名称
   */
  @Column(name = "INS_NAME")
  private String insName;
  /**
   * 单位性质id
   */
  @Column(name = "INS_NATURE_ID")
  private Long insNatureId;
  /**
   * 地区id
   */
  @Column(name = "REGION_ID")
  private Long regionId;
  /**
   * 国家id
   */
  @Column(name = "NATION_ID")
  private Long nationId;
  /**
   * 省id
   */
  @Column(name = "PRO_ID")
  private Long proId;
  /**
   * 市id
   */
  @Column(name = "CITY_ID")
  private Long cityId;

  public Long getInsId() {
    return insId;
  }

  public void setInsId(Long insId) {
    this.insId = insId;
  }

  public String getInsName() {
    return insName;
  }

  public void setInsName(String insName) {
    this.insName = insName;
  }

  public Long getInsNatureId() {
    return insNatureId;
  }

  public void setInsNatureId(Long insNatureId) {
    this.insNatureId = insNatureId;
  }

  public Long getRegionId() {
    return regionId;
  }

  public void setRegionId(Long regionId) {
    this.regionId = regionId;
  }

  public Long getNationId() {
    return nationId;
  }

  public void setNationId(Long nationId) {
    this.nationId = nationId;
  }

  public Long getProId() {
    return proId;
  }

  public void setProId(Long proId) {
    this.proId = proId;
  }

  public Long getCityId() {
    return cityId;
  }

  public void setCityId(Long cityId) {
    this.cityId = cityId;
  }


}
