package com.smate.web.prj.model.wechat;

import java.io.Serializable;
import java.util.Date;

/**
 * 
 * @author tj
 * 
 *         该实体类用于界面显示
 *
 */
public class FundWeChat implements Serializable {
  /**
   * 
   */
  private static final long serialVersionUID = -5252744867013166313L;
  // 基金id
  private String fundId;
  private String des3FundId;// 加密基金id
  private Long fundAgencyId; // 资助机构ID
  private String encryptedFundId; // 加密的基金ID
  // 用于页面显示-基金名称
  private String fundName;
  // 基金名称
  private String fundNameEn;
  // 基金结构
  private String fundAgency;
  // 基金结构
  private String fundAgencyEn;
  // 截止时间
  private String time;
  // 状态
  private String status;// 0表示“即将开始申请”，1表示“申请中”，2表示“即将截止”，3表示“已截止”
  // 基金开始时间
  private Date startDate;
  // 基金结束时间
  private Date endDate;
  // 地域
  private String viewName;
  // 学科
  private String discipline;
  // 申报指南网址
  private String guideUrl;
  // 申报网址
  private String declareUrl;
  // 简介
  private String description;
  // 开始截止时间
  private String showDate;
  // 基金logo图片
  private String logoUrl;
  private boolean hasAward = false; // 已赞过
  private Integer shareCount; // 分享统计数
  private boolean hasCollected = false; // 是否已收藏
  private Integer awardCount; // 赞统计数
  private String zhTitle; // 中文标题
  private String enTitle; // 英文标题
  private String zhShowDesc; // 中文的描述（资助机构名称，科技领域，申请时间）
  private String enShowDesc; // 英文的描述
  private String zhShowDescBr; // 中文的描述（资助机构名称，科技领域，申请时间）用br换行分隔
  private String enShowDescBr; // 英文的描述

  public FundWeChat() {
    super();
  }

  public FundWeChat(String fundName, Date startDate, Date endDate, String fundAgency) {
    super();
    this.fundName = fundName;
    this.fundAgency = fundAgency;
    this.startDate = startDate;
    this.endDate = endDate;
  }

  public FundWeChat(String fundName, String fundAgency, String time, String status, Date startDate, Date endDate,
      String viewName, String fieldName) {
    super();
    this.fundName = fundName;
    this.fundAgency = fundAgency;
    this.time = time;
    this.status = status;
    this.startDate = startDate;
    this.endDate = endDate;
    this.viewName = viewName;
    this.setDiscipline(fieldName);
  }

  public String getFundName() {
    return fundName;
  }

  public void setFundName(String fundName) {
    this.fundName = fundName;
  }

  public String getFundAgency() {
    return fundAgency;
  }

  public void setFundAgency(String fundAgency) {
    this.fundAgency = fundAgency;
  }

  public String getTime() {
    return time;
  }

  public void setTime(String time) {
    this.time = time;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public Date getStartDate() {
    return startDate;
  }

  public void setStartDate(Date startDate) {
    this.startDate = startDate;
  }

  public Date getEndDate() {
    return endDate;
  }

  public void setEndDate(Date endDate) {
    this.endDate = endDate;
  }

  public String getViewName() {
    return viewName;
  }

  public void setViewName(String viewName) {
    this.viewName = viewName;
  }

  public String getGuideUrl() {
    return guideUrl;
  }

  public void setGuideUrl(String guideUrl) {
    this.guideUrl = guideUrl;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public String getDiscipline() {
    return discipline;
  }

  public void setDiscipline(String discipline) {
    this.discipline = discipline;
  }

  public String getFundId() {
    return fundId;
  }

  public void setFundId(String fundId) {
    this.fundId = fundId;
  }

  public String getShowDate() {
    return showDate;
  }

  public void setShowDate(String showDate) {
    this.showDate = showDate;
  }

  public String getLogoUrl() {
    return logoUrl;
  }

  public void setLogoUrl(String logoUrl) {
    this.logoUrl = logoUrl;
  }

  public Integer getShareCount() {
    return shareCount;
  }

  public void setShareCount(Integer shareCount) {
    this.shareCount = shareCount;
  }

  public Integer getAwardCount() {
    return awardCount;
  }

  public void setAwardCount(Integer awardCount) {
    this.awardCount = awardCount;
  }

  public String getEncryptedFundId() {
    return encryptedFundId;
  }

  public void setEncryptedFundId(String encryptedFundId) {
    this.encryptedFundId = encryptedFundId;
  }

  public String getZhTitle() {
    return zhTitle;
  }

  public void setZhTitle(String zhTitle) {
    this.zhTitle = zhTitle;
  }

  public String getEnTitle() {
    return enTitle;
  }

  public void setEnTitle(String enTitle) {
    this.enTitle = enTitle;
  }

  public String getZhShowDesc() {
    return zhShowDesc;
  }

  public void setZhShowDesc(String zhShowDesc) {
    this.zhShowDesc = zhShowDesc;
  }

  public String getEnShowDesc() {
    return enShowDesc;
  }

  public void setEnShowDesc(String enShowDesc) {
    this.enShowDesc = enShowDesc;
  }

  public String getZhShowDescBr() {
    return zhShowDescBr;
  }

  public void setZhShowDescBr(String zhShowDescBr) {
    this.zhShowDescBr = zhShowDescBr;
  }

  public String getEnShowDescBr() {
    return enShowDescBr;
  }

  public void setEnShowDescBr(String enShowDescBr) {
    this.enShowDescBr = enShowDescBr;
  }

  public Long getFundAgencyId() {
    return fundAgencyId;
  }

  public void setFundAgencyId(Long fundAgencyId) {
    this.fundAgencyId = fundAgencyId;
  }

  public boolean getHasAward() {
    return hasAward;
  }

  public void setHasAward(boolean hasAward) {
    this.hasAward = hasAward;
  }

  public boolean getHasCollected() {
    return hasCollected;
  }

  public void setHasCollected(boolean hasCollected) {
    this.hasCollected = hasCollected;
  }

  public String getFundNameEn() {
    return fundNameEn;
  }

  public void setFundNameEn(String fundNameEn) {
    this.fundNameEn = fundNameEn;
  }

  public String getDeclareUrl() {
    return declareUrl;
  }

  public void setDeclareUrl(String declareUrl) {
    this.declareUrl = declareUrl;
  }

  public String getFundAgencyEn() {
    return fundAgencyEn;
  }

  public void setFundAgencyEn(String fundAgencyEn) {
    this.fundAgencyEn = fundAgencyEn;
  }

  public String getDes3FundId() {
    return des3FundId;
  }

  public void setDes3FundId(String des3FundId) {
    this.des3FundId = des3FundId;
  }

}
