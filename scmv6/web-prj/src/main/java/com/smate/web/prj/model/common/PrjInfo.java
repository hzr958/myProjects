package com.smate.web.prj.model.common;

import java.math.BigDecimal;

import com.smate.core.base.project.model.Project;
import com.smate.core.base.project.model.ProjectStatistics;

/**
 * 项目信息
 * 
 * @author zk
 *
 */
public class PrjInfo {

  // 要准备的数据
  private Project prj;
  private ProjectStatistics projectStatistics;

  // 要显示的数据
  private Long prjId;// 项目id
  private String des3PrjId;// 项目加密id
  private String title; // 标题
  private String briefDesc; // 来源
  private String authors; // 作者

  private Integer awardCount; // 点赞次数
  private Integer shareCount; // 分享次数
  private Integer commentCount; // 评论次数
  private Integer award;// 是否点赞
  private String prjAbstract; // 摘要
  private String anyUser;// 项目隐私，7、公开，4隐私

  // 资金总数
  private BigDecimal amount;
  // 资金单位
  private String amountUnit;
  // 资助机构名称
  private String agencyName;
  // 资助机构名称(英文)
  private String enAgencyName;
  // 开始年份
  private Integer startYear;
  // 开始月份
  private Integer startMonth;
  // 开始日
  private Integer startDay;
  // 结束年份
  private Integer endYear;
  // 结束月份
  private Integer endMonth;
  // 结束日
  private Integer endDay;
  // 依托单位名称
  private String insName;
  // 资助类别名称
  private String schemeName;
  // 资助类别名称(英文)
  private String enSchemeName;

  public Long getPrjId() {
    return prjId;
  }

  public void setPrjId(Long prjId) {
    this.prjId = prjId;
  }

  public Project getPrj() {
    return prj;
  }

  public void setPrj(Project prj) {
    this.prj = prj;
  }

  public ProjectStatistics getProjectStatistics() {
    return projectStatistics;
  }

  public void setProjectStatistics(ProjectStatistics projectStatistics) {
    this.projectStatistics = projectStatistics;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getBriefDesc() {
    return briefDesc;
  }

  public void setBriefDesc(String briefDesc) {
    this.briefDesc = briefDesc;
  }

  public String getAuthors() {
    return authors;
  }

  public void setAuthors(String authors) {
    this.authors = authors;
  }

  public Integer getAwardCount() {
    return awardCount;
  }

  public void setAwardCount(Integer awardCount) {
    this.awardCount = awardCount;
  }

  public Integer getShareCount() {
    return shareCount;
  }

  public void setShareCount(Integer shareCount) {
    this.shareCount = shareCount;
  }

  public Integer getCommentCount() {
    return commentCount;
  }

  public void setCommentCount(Integer commentCount) {
    this.commentCount = commentCount;
  }

  public Integer getAward() {
    return award;
  }

  public void setAward(Integer award) {
    this.award = award;
  }

  public String getPrjAbstract() {
    return prjAbstract;
  }

  public void setPrjAbstract(String prjAbstract) {
    this.prjAbstract = prjAbstract;
  }

  public String getAnyUser() {
    return anyUser;
  }

  public void setAnyUser(String anyUser) {
    this.anyUser = anyUser;
  }

  public String getDes3PrjId() {
    return des3PrjId;
  }

  public void setDes3PrjId(String des3PrjId) {
    this.des3PrjId = des3PrjId;
  }

  public BigDecimal getAmount() {
    return amount;
  }

  public void setAmount(BigDecimal amount) {
    this.amount = amount;
  }

  public String getAmountUnit() {
    return amountUnit;
  }

  public void setAmountUnit(String amountUnit) {
    this.amountUnit = amountUnit;
  }

  public String getAgencyName() {
    return agencyName;
  }

  public void setAgencyName(String agencyName) {
    this.agencyName = agencyName;
  }

  public String getEnAgencyName() {
    return enAgencyName;
  }

  public void setEnAgencyName(String enAgencyName) {
    this.enAgencyName = enAgencyName;
  }

  public Integer getStartYear() {
    return startYear;
  }

  public void setStartYear(Integer startYear) {
    this.startYear = startYear;
  }

  public Integer getStartMonth() {
    return startMonth;
  }

  public void setStartMonth(Integer startMonth) {
    this.startMonth = startMonth;
  }

  public Integer getStartDay() {
    return startDay;
  }

  public void setStartDay(Integer startDay) {
    this.startDay = startDay;
  }

  public Integer getEndYear() {
    return endYear;
  }

  public void setEndYear(Integer endYear) {
    this.endYear = endYear;
  }

  public Integer getEndMonth() {
    return endMonth;
  }

  public void setEndMonth(Integer endMonth) {
    this.endMonth = endMonth;
  }

  public Integer getEndDay() {
    return endDay;
  }

  public void setEndDay(Integer endDay) {
    this.endDay = endDay;
  }

  public String getInsName() {
    return insName;
  }

  public void setInsName(String insName) {
    this.insName = insName;
  }

  public String getSchemeName() {
    return schemeName;
  }

  public void setSchemeName(String schemeName) {
    this.schemeName = schemeName;
  }

  public String getEnSchemeName() {
    return enSchemeName;
  }

  public void setEnSchemeName(String enSchemeName) {
    this.enSchemeName = enSchemeName;
  }


}
