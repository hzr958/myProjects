package com.iris.scm.system.consts.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * 行政 区划 国家 地区.
 * 
 * @author zt
 * 
 */
@Entity
@Table(name = "CONST_REGION")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class ConstRegion implements Serializable {
  /**
   * 
   */
  private static final long serialVersionUID = -3656671091467558450L;
  /**
   * 主健.
   */
  private Long id;
  /**
   * 地区编码.
   */
  private String regionCode;
  /**
   * 中文名.
   */
  private String zhName;
  /**
   * 英文名.
   */
  private String enName;
  /**
   * 上级地区.
   */
  private Long superRegionId;

  /**
   * 上级地区编码.
   */
  private String superRegionCode;

  /**
   * 上级地区.
   */
  private ConstRegion superRegion;

  /**
   * 真实需要使用的名称.
   */
  private String name;

  /**
   * Id，供智能提示用.
   */
  private Long code;

  /**
   * 中文排序字段.
   */
  private int zhSeq;

  /**
   * 英文排序字段.
   */
  private int enSeq;
  // 对应V.26的ID
  private Integer oldId;

  // 国别
  private String countryCode;

  // 百度地图X轴坐标
  private String xAxis;

  // 百度地图y轴坐标
  private String yAxis;

  public ConstRegion() {

  }

  public ConstRegion(Long id, String zhName, String enName) {
    super();
    this.id = id;
    this.zhName = zhName;
    this.enName = enName;
  }

  /**
   * @return
   */
  @Id
  @Column(name = "REGION_ID")
  public Long getId() {
    return id;
  }

  /**
   * @param id
   */
  public void setId(Long id) {
    this.id = id;
  }

  /**
   * @return
   */
  @Column(name = "REGION_CODE")
  public String getRegionCode() {
    return regionCode;
  }

  /**
   * @param regionCode
   */
  public void setRegionCode(String regionCode) {
    this.regionCode = regionCode;
  }

  /**
   * @return
   */
  @Column(name = "ZH_NAME")
  public String getZhName() {
    return zhName;
  }

  /**
   * @param zhName
   */
  public void setZhName(String zhName) {
    this.zhName = zhName;
  }

  /**
   * @return
   */
  @Column(name = "EN_NAME")
  public String getEnName() {
    return enName;
  }

  /**
   * @param enName
   */
  public void setEnName(String enName) {
    this.enName = enName;
  }

  public void setZhSeq(int zhSeq) {
    this.zhSeq = zhSeq;
  }

  @Column(name = "ZH_SEQ")
  public int getZhSeq() {
    return zhSeq;
  }

  public void setEnSeq(int enSeq) {
    this.enSeq = enSeq;
  }

  @Column(name = "EN_SEQ")
  public int getEnSeq() {
    return enSeq;
  }

  @Column(name = "SUPER_REGION_ID")
  public Long getSuperRegionId() {
    return superRegionId;
  }

  @Column(name = "SUPER_REGION_CODE")
  public String getSuperRegionCode() {
    return superRegionCode;
  }

  @Column(name = "OLD_REGION_ID")
  public Integer getOldId() {
    return oldId;
  }

  public void setOldId(Integer oldId) {
    this.oldId = oldId;
  }

  public void setSuperRegionCode(String superRegionCode) {
    this.superRegionCode = superRegionCode;
  }

  @Column(name = "COUNTRY_CODE")
  public String getCountryCode() {
    return countryCode;
  }

  public void setCountryCode(String countryCode) {
    this.countryCode = countryCode;
  }

  @Column(name = "XAXIS")
  public String getxAxis() {
    return xAxis;
  }

  public void setxAxis(String xAxis) {
    this.xAxis = xAxis;
  }

  @Column(name = "YAXIS")
  public String getyAxis() {
    return yAxis;
  }

  public void setyAxis(String yAxis) {
    this.yAxis = yAxis;
  }

  /**
   * @param superRegionId
   */
  public void setSuperRegionId(Long superRegionId) {
    this.superRegionId = superRegionId;
  }

  @Transient
  public ConstRegion getSuperRegion() {
    return superRegion;
  }

  public void setSuperRegion(ConstRegion superRegion) {
    this.superRegion = superRegion;
  }

  @Transient
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  @Transient
  public Long getCode() {
    return code;
  }

  public void setCode(Long code) {
    this.code = code;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((id == null) ? 0 : id.hashCode());
    result = prime * result + ((regionCode == null) ? 0 : regionCode.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    ConstRegion other = (ConstRegion) obj;
    if (id == null) {
      if (other.id != null)
        return false;
    } else if (!id.equals(other.id))
      return false;
    if (regionCode == null) {
      if (other.regionCode != null)
        return false;
    } else if (!regionCode.equals(other.regionCode))
      return false;
    return true;
  }
}
