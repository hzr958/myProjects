package com.smate.center.open.model.profile;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * 人员科技领域
 *
 * @author wsn
 * @createTime 2017年3月14日 下午6:01:37
 *
 */

@Entity
@Table(name = "PSN_SCIENCE_AREA")
public class PsnScienceArea implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = 7172583112181040355L;
  private Long id; // 主键
  private Long psnId; // 人员ID
  private String scienceArea; // 科技领域名称
  private String enScienceArea; // 英文科技领域名称
  private Integer scienceAreaId; // 科技领域ID,对应category_map_base
  private Integer status; // 状态， 0：无效， 1：有效
  private Integer identificationSum; // 认同数
  private String identifyPsnIds;// 认同人员ID
  private boolean hasIdentified; // 已认同过
  private String showScienceArea; // 页面显示的科技领域名称
  private Integer areaOrder;// 领域排序
  private Date updateDate;// 更新时间

  public PsnScienceArea() {
    super();
  }

  public PsnScienceArea(Integer scienceAreaId, String scienceArea, String enScienceArea) {
    this.scienceAreaId = scienceAreaId;
    this.scienceArea = scienceArea;
    this.enScienceArea = enScienceArea;
  }

  public PsnScienceArea(Long id, Long psnId, String scienceArea, Integer scienceAreaId, Integer status) {
    super();
    this.id = id;
    this.psnId = psnId;
    this.scienceArea = scienceArea;
    this.scienceAreaId = scienceAreaId;
    this.status = status;
  }

  @Id
  @Column(name = "ID")
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_PSN_SCIENCE_AREA", allocationSize = 1)
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  @Column(name = "PSN_ID")
  public Long getPsnId() {
    return psnId;
  }

  public void setPsnId(Long psnId) {
    this.psnId = psnId;
  }

  @Column(name = "SCIENCE_AREA")
  public String getScienceArea() {
    return scienceArea;
  }

  public void setScienceArea(String scienceArea) {
    this.scienceArea = scienceArea;
  }

  @Column(name = "SCIENCE_AREA_ID")
  public Integer getScienceAreaId() {
    return scienceAreaId;
  }

  public void setScienceAreaId(Integer scienceAreaId) {
    this.scienceAreaId = scienceAreaId;
  }

  @Column(name = "STATUS")
  public Integer getStatus() {
    return status;
  }

  public void setStatus(Integer status) {
    this.status = status;
  }

  @Column(name = "IDENTIFICATION_SUM")
  public Integer getIdentificationSum() {
    return identificationSum;
  }

  public void setIdentificationSum(Integer identificationSum) {
    this.identificationSum = identificationSum;
  }

  @Transient
  public String getIdentifyPsnIds() {
    return identifyPsnIds;
  }

  public void setIdentifyPsnIds(String identifyPsnIds) {
    this.identifyPsnIds = identifyPsnIds;
  }

  @Transient
  public boolean getHasIdentified() {
    return hasIdentified;
  }

  public void setHasIdentified(boolean hasIdentified) {
    this.hasIdentified = hasIdentified;
  }

  @Column(name = "SCIENCE_AREA_EN")
  public String getEnScienceArea() {
    return enScienceArea;
  }

  public void setEnScienceArea(String enScienceArea) {
    this.enScienceArea = enScienceArea;
  }

  @Transient
  public String getShowScienceArea() {
    return showScienceArea;
  }

  public void setShowScienceArea(String showScienceArea) {
    this.showScienceArea = showScienceArea;
  }

  @Column(name = "AREAORDER")
  public Integer getAreaOrder() {
    return areaOrder;
  }

  public void setAreaOrder(Integer areaOrder) {
    this.areaOrder = areaOrder;
  }

  @Column(name = "UPDATE_DATE")
  public Date getUpdateDate() {
    return updateDate;
  }

  public void setUpdateDate(Date updateDate) {
    this.updateDate = updateDate;
  }

}
