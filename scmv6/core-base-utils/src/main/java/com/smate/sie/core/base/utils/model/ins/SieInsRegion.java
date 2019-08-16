package com.smate.sie.core.base.utils.model.ins;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 高校所在地表，当常量表使用.
 * 
 * @author hd
 * 
 */
@Entity
@Table(name = "INS_REGION")
public class SieInsRegion implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = 107561612702657001L;
  private Long insId;
  // 机构国家ID（region_id）
  private Long countryId;
  // 机构省份ID（region_id）
  private Long prvId;
  // 机构市级地区ID（region_id）
  private Long cyId;
  // 机构市级地区辖区管理ID（例如南山区）
  private Long disId;

  public SieInsRegion() {
    super();
  }

  @Id
  @Column(name = "INS_ID")
  public Long getInsId() {
    return insId;
  }

  @Column(name = "COUNTRY_ID")
  public Long getCountryId() {
    return countryId;
  }

  @Column(name = "PRV_ID")
  public Long getPrvId() {
    return prvId;
  }

  @Column(name = "CY_ID")
  public Long getCyId() {
    return cyId;
  }

  @Column(name = "DIS_ID")
  public Long getDisId() {
    return disId;
  }

  public void setDisId(Long disId) {
    this.disId = disId;
  }

  public void setInsId(Long insId) {
    this.insId = insId;
  }

  public void setPrvId(Long prvId) {
    this.prvId = prvId;
  }

  public void setCyId(Long cyId) {
    this.cyId = cyId;
  }

  public void setCountryId(Long countryId) {
    this.countryId = countryId;
  }


}
