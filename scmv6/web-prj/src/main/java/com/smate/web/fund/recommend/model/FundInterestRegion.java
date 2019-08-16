package com.smate.web.fund.recommend.model;

import java.io.Serializable;

/**
 * 基金推荐条件中的关注的地区
 * 
 * @author WSN
 *
 *         2017年8月21日 上午10:15:22
 *
 */
public class FundInterestRegion implements Serializable {

  private Integer seqNo; // 第几个地区
  private String regionName; // 地区名称
  private Long regionId; // 地区Id
  private String showRegionName; // 显示的地区名称
  private String searchKey; // 检索用的，包含上级地区
  private Long superRegionId; // 父地区ID
  private boolean notSelfRegion = false; // 是否是自身的地区信息

  public FundInterestRegion(Integer seqNo, String regionName, Long regionId) {
    super();
    this.seqNo = seqNo;
    this.regionName = regionName;
    this.regionId = regionId;
  }

  public FundInterestRegion(Integer seqNo, String regionName, Long regionId, String showRegionName) {
    super();
    this.seqNo = seqNo;
    this.regionName = regionName;
    this.regionId = regionId;
    this.showRegionName = showRegionName;
  }

  public FundInterestRegion(Integer seqNo, String regionName, Long regionId, String showRegionName,
      Long superRegionId) {
    super();
    this.seqNo = seqNo;
    this.regionName = regionName;
    this.regionId = regionId;
    this.showRegionName = showRegionName;
    this.superRegionId = superRegionId;
  }

  public FundInterestRegion() {
    super();
  }

  public Integer getSeqNo() {
    return seqNo;
  }

  public void setSeqNo(Integer seqNo) {
    this.seqNo = seqNo;
  }

  public String getRegionName() {
    return regionName;
  }

  public void setRegionName(String regionName) {
    this.regionName = regionName;
  }

  public Long getRegionId() {
    return regionId;
  }

  public void setRegionId(Long regionId) {
    this.regionId = regionId;
  }

  public String getShowRegionName() {
    return showRegionName;
  }

  public void setShowRegionName(String showRegionName) {
    this.showRegionName = showRegionName;
  }

  public String getSearchKey() {
    return searchKey;
  }

  public void setSearchKey(String searchKey) {
    this.searchKey = searchKey;
  }

  public Long getSuperRegionId() {
    return superRegionId;
  }

  public void setSuperRegionId(Long superRegionId) {
    this.superRegionId = superRegionId;
  }

  public boolean getNotSelfRegion() {
    return notSelfRegion;
  }

  public void setNotSelfRegion(boolean notSelfRegion) {
    this.notSelfRegion = notSelfRegion;
  }

}
