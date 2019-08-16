package com.smate.web.psn.model.keyword;

import java.util.Date;

/**
 * 科技领域操作类
 * 
 * @author wsn
 *
 */
public class PsnScienceAreaInfo {

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

  public PsnScienceAreaInfo() {
    super();
  }

  public PsnScienceAreaInfo(Integer scienceAreaId, String scienceArea, String enScienceArea) {
    this.scienceAreaId = scienceAreaId;
    this.scienceArea = scienceArea;
    this.enScienceArea = enScienceArea;
  }

  public PsnScienceAreaInfo(Long id, Long psnId, String scienceArea, Integer scienceAreaId, Integer status) {
    super();
    this.id = id;
    this.psnId = psnId;
    this.scienceArea = scienceArea;
    this.scienceAreaId = scienceAreaId;
    this.status = status;
  }

  public PsnScienceAreaInfo(PsnScienceArea area) {
    this.id = area.getId();
    this.psnId = area.getPsnId();
    this.scienceArea = area.getScienceArea();
    this.enScienceArea = area.getEnScienceArea();
    this.scienceAreaId = area.getScienceAreaId();
    this.status = area.getStatus();
    this.updateDate = area.getUpdateDate();
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Long getPsnId() {
    return psnId;
  }

  public void setPsnId(Long psnId) {
    this.psnId = psnId;
  }

  public String getScienceArea() {
    return scienceArea;
  }

  public void setScienceArea(String scienceArea) {
    this.scienceArea = scienceArea;
  }

  public Integer getScienceAreaId() {
    return scienceAreaId;
  }

  public void setScienceAreaId(Integer scienceAreaId) {
    this.scienceAreaId = scienceAreaId;
  }

  public Integer getStatus() {
    return status;
  }

  public void setStatus(Integer status) {
    this.status = status;
  }

  public Integer getIdentificationSum() {
    return identificationSum;
  }

  public void setIdentificationSum(Integer identificationSum) {
    this.identificationSum = identificationSum;
  }

  public String getIdentifyPsnIds() {
    return identifyPsnIds;
  }

  public void setIdentifyPsnIds(String identifyPsnIds) {
    this.identifyPsnIds = identifyPsnIds;
  }

  public boolean getHasIdentified() {
    return hasIdentified;
  }

  public void setHasIdentified(boolean hasIdentified) {
    this.hasIdentified = hasIdentified;
  }

  public String getEnScienceArea() {
    return enScienceArea;
  }

  public void setEnScienceArea(String enScienceArea) {
    this.enScienceArea = enScienceArea;
  }

  public String getShowScienceArea() {
    return showScienceArea;
  }

  public void setShowScienceArea(String showScienceArea) {
    this.showScienceArea = showScienceArea;
  }

  public Integer getAreaOrder() {
    return areaOrder;
  }

  public void setAreaOrder(Integer areaOrder) {
    this.areaOrder = areaOrder;
  }

  public Date getUpdateDate() {
    return updateDate;
  }

  public void setUpdateDate(Date updateDate) {
    this.updateDate = updateDate;
  }

}
