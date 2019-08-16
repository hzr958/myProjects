package com.smate.center.task.model.sns.quartz;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.smate.center.task.model.pdwh.quartz.LeftMenuForm;


/**
 * 人员推荐基金的页面结果参数form实体.
 * 
 * @author mjg
 * 
 */
public class PsnFundReForm implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = -2064958731610760256L;
  private Long fundId;// 基金ID.
  private Long psnId;// 人员ID.
  private double recommendation;// 推荐度.
  private int fundLevel;// 基金星级.
  // 列表显示机构名称(根据当前语言环境显示相应中英文字段)
  private String agencyViewName;
  // 列表显示机构logo
  private String logoUrl;
  // 基金类别列表显示名称
  private String categoryViewName;
  private String guideUrl;// 申报指南网址
  private String declareUrl;// 申报网址
  private Date startDate;// 开始日期
  private Date endDate;// 截止日期
  private List<LeftMenuForm> qualityStarList;// 质量-星级列表.
  private List<LeftMenuForm> discKeywordList;// 研究领域-关键词列表.
  // 以下为原基金推荐所需参数.
  private String appTitle;// 申请书标题_MJG_SCM-3166.
  private String appSummary;// 申请书摘要_MJG_SCM-3166.
  private int fundLoadFlag = 0;// 基金机会信息是否存入缓存的判断参数.
  private String discInfoJson;// 基金对应的学科代码列表(json格式).
  private String keywordJson;// 此次推荐基金对应的关键词列表(json格式).
  private String fundInfoJson;// 基金对应的关键词数列表(json格式).
  private String keywordStr;// 基金对应关键词.
  private String catDes3Id;
  private String discViewStr;// 最佳学科代码.
  private String impProName;// 重点项目
  private String agencyViewNameEn;
  private String categoryViewNameEn;

  public PsnFundReForm() {}

  public PsnFundReForm(Long fundId, Long psnId, double recommendation, int fundLevel, String agencyViewName,
      String categoryViewName, String guideUrl, String declareUrl, Date startDate, Date endDate, String discViewStr,
      List<LeftMenuForm> qualityStarList, List<LeftMenuForm> discKeywordList, String appTitle, String appSummary,
      int fundLoadFlag, String discInfoJson, String keywordJson, String fundInfoJson) {
    super();
    this.fundId = fundId;
    this.psnId = psnId;
    this.recommendation = recommendation;
    this.fundLevel = fundLevel;
    this.agencyViewName = agencyViewName;
    this.categoryViewName = categoryViewName;
    this.guideUrl = guideUrl;
    this.declareUrl = declareUrl;
    this.startDate = startDate;
    this.endDate = endDate;
    this.discViewStr = discViewStr;
    this.qualityStarList = qualityStarList;
    this.discKeywordList = discKeywordList;
    this.appTitle = appTitle;
    this.appSummary = appSummary;
    this.fundLoadFlag = fundLoadFlag;
    this.discInfoJson = discInfoJson;
    this.keywordJson = keywordJson;
    this.fundInfoJson = fundInfoJson;
  }

  public Long getFundId() {
    return fundId;
  }

  public Long getPsnId() {
    return psnId;
  }

  public double getRecommendation() {
    return recommendation;
  }

  public int getFundLevel() {
    return fundLevel;
  }

  public String getAgencyViewName() {
    return agencyViewName;
  }

  public String getCategoryViewName() {
    return categoryViewName;
  }

  public String getGuideUrl() {
    return guideUrl;
  }

  public String getDeclareUrl() {
    return declareUrl;
  }

  public Date getStartDate() {
    return startDate;
  }

  public Date getEndDate() {
    return endDate;
  }

  public String getDiscViewStr() {
    return discViewStr;
  }

  public void setFundId(Long fundId) {
    this.fundId = fundId;
  }

  public void setPsnId(Long psnId) {
    this.psnId = psnId;
  }

  public void setRecommendation(double recommendation) {
    this.recommendation = recommendation;
  }

  public void setFundLevel(int fundLevel) {
    this.fundLevel = fundLevel;
  }

  public void setAgencyViewName(String agencyViewName) {
    this.agencyViewName = agencyViewName;
  }

  public void setCategoryViewName(String categoryViewName) {
    this.categoryViewName = categoryViewName;
  }

  public void setGuideUrl(String guideUrl) {
    this.guideUrl = guideUrl;
  }

  public void setDeclareUrl(String declareUrl) {
    this.declareUrl = declareUrl;
  }

  public void setStartDate(Date startDate) {
    this.startDate = startDate;
  }

  public void setEndDate(Date endDate) {
    this.endDate = endDate;
  }

  public void setDiscViewStr(String discViewStr) {
    this.discViewStr = discViewStr;
  }

  public String getAppTitle() {
    return appTitle;
  }

  public String getAppSummary() {
    return appSummary;
  }

  public int getFundLoadFlag() {
    return fundLoadFlag;
  }

  public void setFundLoadFlag(int fundLoadFlag) {
    this.fundLoadFlag = fundLoadFlag;
  }

  public String getDiscInfoJson() {
    return discInfoJson;
  }

  public String getKeywordJson() {
    return keywordJson;
  }

  public String getFundInfoJson() {
    return fundInfoJson;
  }

  public void setAppTitle(String appTitle) {
    this.appTitle = appTitle;
  }

  public void setAppSummary(String appSummary) {
    this.appSummary = appSummary;
  }

  public void setDiscInfoJson(String discInfoJson) {
    this.discInfoJson = discInfoJson;
  }

  public void setKeywordJson(String keywordJson) {
    this.keywordJson = keywordJson;
  }

  public void setFundInfoJson(String fundInfoJson) {
    this.fundInfoJson = fundInfoJson;
  }

  public List<LeftMenuForm> getQualityStarList() {
    return qualityStarList;
  }

  public List<LeftMenuForm> getDiscKeywordList() {
    return discKeywordList;
  }

  public void setQualityStarList(List<LeftMenuForm> qualityStarList) {
    this.qualityStarList = qualityStarList;
  }

  public void setDiscKeywordList(List<LeftMenuForm> discKeywordList) {
    this.discKeywordList = discKeywordList;
  }

  public String getKeywordStr() {
    return keywordStr;
  }

  public void setKeywordStr(String keywordStr) {
    this.keywordStr = keywordStr;
  }

  public String getCatDes3Id() {
    return catDes3Id;
  }

  public void setCatDes3Id(String catDes3Id) {
    this.catDes3Id = catDes3Id;
  }

  public String getImpProName() {
    return impProName;
  }

  public void setImpProName(String impProName) {
    this.impProName = impProName;
  }

  public String getLogoUrl() {
    return logoUrl;
  }

  public void setLogoUrl(String logoUrl) {
    this.logoUrl = logoUrl;
  }

  public String getAgencyViewNameEn() {
    return agencyViewNameEn;
  }

  public void setAgencyViewNameEn(String agencyViewNameEn) {
    this.agencyViewNameEn = agencyViewNameEn;
  }

  public String getCategoryViewNameEn() {
    return categoryViewNameEn;
  }

  public void setCategoryViewNameEn(String categoryViewNameEn) {
    this.categoryViewNameEn = categoryViewNameEn;
  }


}
