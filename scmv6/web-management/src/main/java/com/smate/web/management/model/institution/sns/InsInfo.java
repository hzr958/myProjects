package com.smate.web.management.model.institution.sns;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 单位信息.
 * 
 * @author zjh
 * 
 */
@Entity
@Table(name = "INS_INFO")
public class InsInfo implements Serializable {
  /**
   * 
   */
  private static final long serialVersionUID = -5815041437161395951L;
  private Long insId;
  private Long country;
  private Long province;
  private Long city;
  private Integer isNsfc;
  private Integer insLevel;
  private Integer isUnderdevelop;
  private Long director;

  public InsInfo() {
    super();
  }

  @Id
  @Column(name = "INS_ID")
  public Long getInsId() {
    return insId;
  }

  public void setInsId(Long insId) {
    this.insId = insId;
  }

  @Column(name = "COUNTRY")
  public Long getCountry() {
    return country;
  }

  public void setCountry(Long country) {
    this.country = country;
  }

  @Column(name = "PROVINCE")
  public Long getProvince() {
    return province;
  }

  public void setProvince(Long province) {
    this.province = province;
  }

  @Column(name = "CITY")
  public Long getCity() {
    return city;
  }

  public void setCity(Long city) {
    this.city = city;
  }

  @Column(name = "IS_NSFC")
  public Integer getIsNsfc() {
    return isNsfc;
  }

  public void setIsNsfc(Integer isNsfc) {
    this.isNsfc = isNsfc;
  }

  @Column(name = "INS_LEVEL")
  public Integer getInsLevel() {
    return insLevel;
  }

  public void setInsLevel(Integer insLevel) {
    this.insLevel = insLevel;
  }

  @Column(name = "IS_UNDERDEVELOP")
  public Integer getIsUnderdevelop() {
    return isUnderdevelop;
  }

  public void setIsUnderdevelop(Integer isUnderdevelop) {
    this.isUnderdevelop = isUnderdevelop;
  }

  @Column(name = "DIRECTOR")
  public Long getDirector() {
    return director;
  }

  public void setDirector(Long director) {
    this.director = director;
  }
}
