package com.smate.core.base.psn.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 单位类型与所属国家.
 * 
 * @author liangguokeng
 * 
 */
@Entity
@Table(name = "CONST_INS_TYPE")
public class ConstInsType implements Serializable {
  /**
   * 
   */
  private static final long serialVersionUID = 3613158130598835113L;
  // 单位ID
  private Long insId;
  // 单位类型1A、1B、2A...
  private String insType;
  // 国内＝1、国外＝2
  private Long countryType;

  public ConstInsType() {
    super();
  }

  public ConstInsType(Long insId, String insType, Long countryType) {
    super();
    this.insId = insId;
    this.insType = insType;
    this.countryType = countryType;
  }

  @Id
  @Column(name = "INS_ID")
  public Long getInsId() {
    return insId;
  }

  @Column(name = "INS_TYPE")
  public String getInsType() {
    return insType;
  }

  @Column(name = "COUNTRY_TYPE")
  public Long getCountryType() {
    return countryType;
  }

  public void setInsId(Long insId) {
    this.insId = insId;
  }

  public void setInsType(String insType) {
    this.insType = insType;
  }

  public void setCountryType(Long countryType) {
    this.countryType = countryType;
  }

}
