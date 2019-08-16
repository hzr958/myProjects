package com.smate.center.task.model.fund.sns;

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

}
