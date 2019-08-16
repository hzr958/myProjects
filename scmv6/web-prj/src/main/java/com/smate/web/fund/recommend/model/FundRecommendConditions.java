package com.smate.web.fund.recommend.model;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang3.StringUtils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

/**
 * 基金推荐条件
 * 
 * @author WSN
 *
 *         2017年8月21日 上午10:11:20
 *
 */
@Entity
@Table(name = "V_FUND_CONDITIONS")
public class FundRecommendConditions implements Serializable {

  private static final long serialVersionUID = 1785118695275098205L;
  private Long psnId; // 人员ID
  private String seniority; // 申请资格， 1：企业，2：科研机构, code1,code2形式
  private String scienceArea; // 科技领域 code1,code2,code3形式
  // 关注的地区,json字符串,{"seqNo":"1","regionName":"XXX", "regionId":"12"}
  private String interestRegion;
  private List<FundInterestRegion> regionInfo; // 关注的地区，取值方便

  public FundRecommendConditions(Long psnId, String seniority, String scienceArea, String interestRegion) {
    super();
    this.psnId = psnId;
    this.seniority = seniority;
    this.scienceArea = scienceArea;
    this.interestRegion = interestRegion;
  }

  public FundRecommendConditions() {
    super();
  }

  @Id
  @Column(name = "PSN_ID")
  public Long getPsnId() {
    return psnId;
  }

  public void setPsnId(Long psnId) {
    this.psnId = psnId;
  }

  @Column(name = "SENIORITY")
  public String getSeniority() {
    return seniority;
  }

  public void setSeniority(String seniority) {
    this.seniority = seniority;
  }

  @Column(name = "SCIENCE_AREA_ID")
  public String getScienceArea() {
    return scienceArea;
  }

  public void setScienceArea(String scienceArea) {
    this.scienceArea = scienceArea;
  }

  @Column(name = "INTEREST_REGION")
  public String getInterestRegion() {
    return interestRegion;
  }

  public void setInterestRegion(String interestRegion) {
    this.interestRegion = interestRegion;
  }

  @Transient
  @SuppressWarnings("unchecked")
  public List<FundInterestRegion> getRegionInfo() {
    if (regionInfo == null && StringUtils.isNotBlank(interestRegion)) {
      regionInfo = new Gson().fromJson(interestRegion, new TypeToken<List<FundInterestRegion>>() {}.getType());
    }
    return regionInfo;
  }

  public void setRegionInfo(List<FundInterestRegion> regionInfo) {
    this.regionInfo = regionInfo;
  }

}
