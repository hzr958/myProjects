package com.smate.core.base.project.vo;

import java.io.Serializable;

import com.smate.core.base.project.model.Project;
import com.smate.core.base.project.model.ProjectStatistics;

/**
 * 操作项目相关信息用
 * 
 * @author Administrator
 *
 */
public class ProjectInfo implements Serializable {

  private static final long serialVersionUID = -4007618737841418376L;
  private String zhTitle; // 项目名称
  private String enTitle; // 英文项目名
  private String authorNames; // 作者名称
  private String authorNamesEn; // 英文作者名称
  private String briefDesc; // 来源
  private String briefDescEn; // 英文来源
  private String showTitle; // 页面显示的标题
  private String showAuthorNames; // 页面显示的作者名
  private String showBriefDesc; // 页面显示的来源
  private String des3Id;
  private String agencyAndScheme;// 资助机构名称+资助类别名称
  private String externalNo;// "资助机构定义的项目编号
  private String showDate;// 研究起止年月
  private String amountAndUnit;// 资金总数
  private String prjState;// 项目状态(已结题或在研)
  private String prjOwner;// 主持或参加
  private Integer wordHrefSeq; // word中添加超链接用id;
  private String prjUrl; // word中的超链接
  private Long prjId; // 项目ID
  private ProjectStatistics projectStatistics;

  private Integer awardCount; // 点赞次数
  private Integer shareCount; // 分享次数
  private Integer commentCount; // 评论次数
  private Integer award;// 是否点赞
  private Long dupPrjId; // 查重找到的重复的项目ID
  private String des3DupPrjId; // 查重找到的加密的重复的项目ID
  private Integer authorMatch; // 当前登录人员是否和项目作者信息匹配上了
  private String tempSourceUrl; // sourceUrl
  private String sourceDbCode; // 来源库code

  private String title; // 标题
  private String authors; // 作者

  public ProjectInfo(String zhTitle, String enTitle, String authorNames, String authorNamesEn, String briefDesc,
      String briefDescEn) {
    super();
    this.zhTitle = zhTitle;
    this.enTitle = enTitle;
    this.authorNames = authorNames;
    this.authorNamesEn = authorNamesEn;
    this.briefDesc = briefDesc;
    this.briefDescEn = briefDescEn;
  }

  public ProjectInfo() {
    super();
  }

  public String getZhTitle() {
    return zhTitle;
  }

  public void setZhTitle(String zhTitle) {
    this.zhTitle = zhTitle;
  }

  public String getAuthorNames() {
    return authorNames;
  }

  public void setAuthorNames(String authorNames) {
    this.authorNames = authorNames;
  }

  public String getBriefDesc() {
    return briefDesc;
  }

  public void setBriefDesc(String briefDesc) {
    this.briefDesc = briefDesc;
  }

  public String getEnTitle() {
    return enTitle;
  }

  public void setEnTitle(String enTitle) {
    this.enTitle = enTitle;
  }

  public String getAuthorNamesEn() {
    return authorNamesEn;
  }

  public void setAuthorNamesEn(String authorNamesEn) {
    this.authorNamesEn = authorNamesEn;
  }

  public String getBriefDescEn() {
    return briefDescEn;
  }

  public void setBriefDescEn(String briefDescEn) {
    this.briefDescEn = briefDescEn;
  }

  public String getShowTitle() {
    return showTitle;
  }

  public void setShowTitle(String showTitle) {
    this.showTitle = showTitle;
  }

  public String getShowAuthorNames() {
    return showAuthorNames;
  }

  public void setShowAuthorNames(String showAuthorNames) {
    this.showAuthorNames = showAuthorNames;
  }

  public String getShowBriefDesc() {
    return showBriefDesc;
  }

  public void setShowBriefDesc(String showBriefDesc) {
    this.showBriefDesc = showBriefDesc;
  }

  public ProjectInfo buildProjectInfo(Project project, ProjectInfo prjInfo) {
    if (project != null) {
      prjInfo = new ProjectInfo();
      prjInfo.setAuthorNames(project.getAuthorNames());
      prjInfo.setBriefDesc(project.getBriefDesc());
      prjInfo.setZhTitle(project.getZhTitle());
      prjInfo.setEnTitle(project.getEnTitle());
      prjInfo.setAuthorNamesEn(project.getAuthorNamesEn());
      prjInfo.setBriefDescEn(project.getBriefDescEn());
    }
    return prjInfo;
  }

  public String getAgencyAndScheme() {
    return agencyAndScheme;
  }

  public void setAgencyAndScheme(String agencyAndScheme) {
    this.agencyAndScheme = agencyAndScheme;
  }

  public String getExternalNo() {
    return externalNo;
  }

  public void setExternalNo(String externalNo) {
    this.externalNo = externalNo;
  }

  public String getShowDate() {
    return showDate;
  }

  public void setShowDate(String showDate) {
    this.showDate = showDate;
  }

  public String getAmountAndUnit() {
    return amountAndUnit;
  }

  public void setAmountAndUnit(String amountAndUnit) {
    this.amountAndUnit = amountAndUnit;
  }

  public String getPrjState() {
    return prjState;
  }

  public void setPrjState(String prjState) {
    this.prjState = prjState;
  }

  public String getPrjOwner() {
    return prjOwner;
  }

  public void setPrjOwner(String prjOwner) {
    this.prjOwner = prjOwner;
  }

  public Integer getWordHrefSeq() {
    return wordHrefSeq;
  }

  public void setWordHrefSeq(Integer wordHrefSeq) {
    this.wordHrefSeq = wordHrefSeq;
  }

  public String getPrjUrl() {
    return prjUrl;
  }

  public void setPrjUrl(String prjUrl) {
    this.prjUrl = prjUrl;
  }

  public Long getPrjId() {
    return prjId;
  }

  public void setPrjId(Long prjId) {
    this.prjId = prjId;
  }

  public ProjectStatistics getProjectStatistics() {
    return projectStatistics;
  }

  public void setProjectStatistics(ProjectStatistics projectStatistics) {
    this.projectStatistics = projectStatistics;
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

  public Long getDupPrjId() {
    return dupPrjId;
  }

  public void setDupPrjId(Long dupPrjId) {
    this.dupPrjId = dupPrjId;
  }

  public String getDes3DupPrjId() {
    return des3DupPrjId;
  }

  public void setDes3DupPrjId(String des3DupPrjId) {
    this.des3DupPrjId = des3DupPrjId;
  }

  public Integer getAuthorMatch() {
    return authorMatch;
  }

  public void setAuthorMatch(Integer authorMatch) {
    this.authorMatch = authorMatch;
  }

  public String getTempSourceUrl() {
    return tempSourceUrl;
  }

  public void setTempSourceUrl(String tempSourceUrl) {
    this.tempSourceUrl = tempSourceUrl;
  }

  public String getSourceDbCode() {
    return sourceDbCode;
  }

  public void setSourceDbCode(String sourceDbCode) {
    this.sourceDbCode = sourceDbCode;
  }

  public String getDes3Id() {
    return des3Id;
  }

  public void setDes3Id(String des3Id) {
    this.des3Id = des3Id;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getAuthors() {
    return authors;
  }

  public void setAuthors(String authors) {
    this.authors = authors;
  }



}
