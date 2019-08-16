package com.smate.web.psn.model.autocomplete;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * 单位.
 * 
 * @author liqinghua
 * 
 */
@Entity
@Table(name = "INSTITUTION")
public class AcInstitution implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = 5624117829609282960L;
  // 自然主键，序列:SEQ_INSTITUTION
  private Long code;
  // 中文名称
  private String zhName;
  // 英文名称
  private String enName;
  // 显示的名称
  private String name;
  // 国别或省份
  private Long regionId;
  // 状态
  private int status;
  // 国别名称
  private String country;
  // 是否允许单位在下拉框显示 0-否 1-是
  private int enabled;
  // 对比报表是否开通
  private int commpareSetting;
  // 性质
  private Integer nature;// 性质：1高校，2研究中心，3政府，4企业，5出版社，6协会，7医院，99其他

  /**
   * 以下字段做查询显示用
   * 
   * 2019年4月10日
   */
  private String pinYin;
  private String firstLetter;

  public AcInstitution() {}

  public AcInstitution(Long code, String zhName, String enName, Long regionId, int status, int enabled,
      int commpareSetting) {
    this.code = code;
    this.zhName = zhName;
    this.enName = enName;
    this.regionId = regionId;
    this.status = status;
    this.enabled = enabled;
    this.commpareSetting = commpareSetting;
  }

  @Id
  @Column(name = "INS_ID")
  public Long getCode() {
    return code;
  }

  public void setCode(Long code) {
    this.code = code;
  }

  @Column(name = "ZH_NAME")
  public String getZhName() {
    return zhName;
  }

  public void setZhName(String zhName) {
    this.zhName = zhName;
  }

  @Column(name = "EN_NAME")
  public String getEnName() {
    return enName;
  }

  public void setEnName(String enName) {
    this.enName = enName;
  }

  @Transient
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  @Column(name = "REGION_ID")
  public Long getRegionId() {
    return regionId;
  }

  public void setRegionId(Long regionId) {
    this.regionId = regionId;
  }

  @Column(name = "STATUS")
  public int getStatus() {
    return status;
  }

  public void setStatus(int status) {
    this.status = status;
  }

  @Transient
  public String getCountry() {
    return country;
  }

  public void setCountry(String country) {
    this.country = country;
  }


  @Column(name = "NATURE")
  public Integer getNature() {
    return nature;
  }

  public void setNature(Integer nature) {
    this.nature = nature;
  }

  /**
   * @return the enabled
   */
  @Column(name = "ENABLED")
  public int getEnabled() {
    return enabled;
  }

  /**
   * @param enabled the enabled to set
   */
  public void setEnabled(int enabled) {
    this.enabled = enabled;
  }

  @Transient
  public int getCommpareSetting() {
    return commpareSetting;
  }

  public void setCommpareSetting(int commpareSetting) {
    this.commpareSetting = commpareSetting;
  }


  public AcInstitution(Long code, String zhName, String enName, Long regionId, int status, int enabled) {
    super();
    this.code = code;
    this.zhName = zhName;
    this.enName = enName;
    this.regionId = regionId;
    this.status = status;
    this.enabled = enabled;
  }

  public String getPinYin() {
    return pinYin;
  }

  public void setPinYin(String pinYin) {
    this.pinYin = pinYin;
  }

  public String getFirstLetter() {
    return firstLetter;
  }

  public void setFirstLetter(String firstLetter) {
    this.firstLetter = firstLetter;
  }

  public AcInstitution(Long code, String zhName, String enName, Long regionId, int status, int enabled, String pinYin,
      String firstLetter) {
    super();
    this.code = code;
    this.zhName = zhName;
    this.enName = enName;
    this.regionId = regionId;
    this.status = status;
    this.enabled = enabled;
    this.pinYin = pinYin;
    this.firstLetter = firstLetter;
  }



}
