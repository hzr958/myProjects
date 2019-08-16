package com.smate.core.base.utils.model.consts;

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
 * @author hd
 * 
 */
@Entity
@Table(name = "CONST_REGION")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class SieConstRegion implements Serializable {
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
  private SieConstRegion superRegion;

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
  // 国家别名，期刊基础比对添加
  private String otherName;

  // 百度地图X轴坐标
  private String xAxis;

  // 百度地图y轴坐标
  private String yAxis;

  public SieConstRegion() {

  }

  public SieConstRegion(Long id, String zhName, String enName) {
    super();
    this.id = id;
    this.zhName = zhName;
    this.enName = enName;
  }

  public SieConstRegion(Long id, String zhName, String enName, Long superRegionId) {
    super();
    this.id = id;
    this.zhName = zhName;
    this.enName = enName;
    this.superRegionId = superRegionId;
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

  public void setSuperRegionCode(String superRegionCode) {
    this.superRegionCode = superRegionCode;
  }

  @Column(name = "OTHER_NAME")
  public String getOtherName() {
    return otherName;
  }


  public void setOtherName(String otherName) {
    this.otherName = otherName;
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
  public SieConstRegion getSuperRegion() {
    return superRegion;
  }

  public void setSuperRegion(SieConstRegion superRegion) {
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
    SieConstRegion other = (SieConstRegion) obj;
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
