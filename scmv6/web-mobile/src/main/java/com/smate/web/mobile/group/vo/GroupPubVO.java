package com.smate.web.mobile.group.vo;

import java.util.Calendar;

import com.smate.core.base.utils.model.Page;

public class GroupPubVO {
  private String searchKey;
  private String publishYear; // 发表年份 如果是多年用逗号隔开
  private String pubType; // 成果类型 如果是多个就用逗号隔开 用常量数字
  private String includeType;// 收录类别
  private String orderBy = "createDate"; // createDate-最新添加,publishYear-最近发表,citedTimes-引用次数
  private Page page = new Page();
  private String des3GrpId;
  private Long grpId;
  private Integer currentYear;
  private String recentYear5;// 近5年
  private String recentYear10;// 近10年
  private Integer grpCategory;// 群组分类 10:课程群组 ， 11项目群组,12,兴趣群组
  private Integer showPrjPub = 0; // 显示项目成果 1 是显示
  private Integer showRefPub = 0; // 显示 文献成果 1显示
  private Integer psnRole; // 4申请中,9群组外成员
  private String fromPage; // 来源
  private String dynText; // 发表动态时的文字
  private String pubListType;// 显示成果列表类型 grpPub:群组成果 ，myPub:我的成果

  public String getSearchKey() {
    return searchKey;
  }

  public void setSearchKey(String searchKey) {
    this.searchKey = searchKey;
  }

  public String getPublishYear() {
    return publishYear;
  }

  public void setPublishYear(String publishYear) {
    this.publishYear = publishYear;
  }

  public String getPubType() {
    return pubType;
  }

  public void setPubType(String pubType) {
    this.pubType = pubType;
  }

  public String getIncludeType() {
    return includeType;
  }

  public void setIncludeType(String includeType) {
    this.includeType = includeType;
  }

  public String getOrderBy() {
    return orderBy;
  }

  public void setOrderBy(String orderBy) {
    this.orderBy = orderBy;
  }

  public Page getPage() {
    return page;
  }

  public void setPage(Page page) {
    this.page = page;
  }

  public String getDes3GrpId() {
    return des3GrpId;
  }

  public void setDes3GrpId(String des3GrpId) {
    this.des3GrpId = des3GrpId;
  }

  public Long getGrpId() {
    return grpId;
  }

  public void setGrpId(Long grpId) {
    this.grpId = grpId;
  }

  public Integer getCurrentYear() {
    if (currentYear == null) {
      currentYear = Calendar.getInstance().get(Calendar.YEAR);
    }
    return currentYear;
  }

  public void setCurrentYear(Integer currentYear) {
    this.currentYear = currentYear;
  }

  public String getRecentYear5() {
    return recentYear5;
  }

  public void setRecentYear5(String recentYear5) {
    this.recentYear5 = recentYear5;
  }

  public String getRecentYear10() {
    return recentYear10;
  }

  public void setRecentYear10(String recentYear10) {
    this.recentYear10 = recentYear10;
  }

  public Integer getGrpCategory() {
    return grpCategory;
  }

  public void setGrpCategory(Integer grpCategory) {
    this.grpCategory = grpCategory;
  }

  public Integer getShowPrjPub() {
    return showPrjPub;
  }

  public void setShowPrjPub(Integer showPrjPub) {
    this.showPrjPub = showPrjPub;
  }

  public Integer getShowRefPub() {
    return showRefPub;
  }

  public void setShowRefPub(Integer showRefPub) {
    this.showRefPub = showRefPub;
  }

  public Integer getPsnRole() {
    return psnRole;
  }

  public void setPsnRole(Integer psnRole) {
    this.psnRole = psnRole;
  }

  public String getFromPage() {
    return fromPage;
  }

  public void setFromPage(String fromPage) {
    this.fromPage = fromPage;
  }

  public String getDynText() {
    return dynText;
  }

  public void setDynText(String dynText) {
    this.dynText = dynText;
  }

  public String getPubListType() {
    return pubListType;
  }

  public void setPubListType(String pubListType) {
    this.pubListType = pubListType;
  }

}
