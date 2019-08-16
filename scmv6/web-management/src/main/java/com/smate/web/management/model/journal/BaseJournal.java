package com.smate.web.management.model.journal;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * cwli基础期刊主表.
 */
@Entity
@Table(name = "BASE_JOURNAL")
public class BaseJournal implements Serializable {

  private static final long serialVersionUID = 900337994016277496L;
  // 主键
  private Long jnlId;
  // 英文刊名
  private String titleEn;
  // 原始刊名
  private String titleXx;
  // Print ISSN
  private String pissn;
  // Electric ISSN
  private String eissn;
  // 国内统一刊号
  private String cn;
  // 出版语言
  private String language;
  // 创刊年
  private String startYear;
  // 停刊年
  private String endYear;
  // 是否OA期刊 1:OA，0:未OA
  private String oaStatus;
  // 是否停刊 1:停刊，0:未停
  private String activeStatus;
  // 发行周期中文或者其它
  private String frequencyZh;
  // 发行周期(英文)
  private String frequencyEn;
  // 出版国家,与国家表id对应
  private Long regionId;
  // 期刊主页
  private String journalUrl;
  // 期刊描述(英文)
  private String descriptionEn;
  // 期刊描述(中文或者其它语言)
  private String descriptionXx;
  // 期刊名缩写
  private String titleAbbr;
  // 在线投稿地址
  private String submissionUrl;
  // OA Type(Open Access Type:开放存储类型)
  private String romeoColour;
  // 期刊titel
  private List<BaseJournalTitle> jouTitleList = new ArrayList<BaseJournalTitle>();
  // 期刊被收录
  private List<BaseJournalDb> jouDbList = new ArrayList<BaseJournalDb>();
  // 期刊被改变
  private List<BaseJournalHistory> jouHistoryList = new ArrayList<BaseJournalHistory>();
  // 期刊影响因子
  private List<BaseJournalIf> jouIfList = new ArrayList<BaseJournalIf>();
  // 期刊关键词
  private List<BaseJournalKeyword> jouKeywordList = new ArrayList<BaseJournalKeyword>();
  // 期刊出版商
  private List<BaseJournalPublisher> publisherList = new ArrayList<BaseJournalPublisher>();
  // 期刊分类
  private List<BaseJournalCategory> categoryList = new ArrayList<BaseJournalCategory>();
  private Boolean isNewTitle;
  private Boolean isNewPissn;
  private Integer throwsType;

  public BaseJournal() {}

  public BaseJournal(String pissn, String titleEn, String titleXx) {
    this.pissn = pissn;
    this.titleEn = titleEn;
    this.titleXx = titleXx;
  }

  public BaseJournal(Long jnlId, String titleEn, String titleXx, String pissn) {
    super();
    this.jnlId = jnlId;
    this.titleEn = titleEn;
    this.titleXx = titleXx;
    this.pissn = pissn;
  }

  @Id
  @Column(name = "JNL_ID")
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_BASE_JOU", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
  public Long getJnlId() {
    return jnlId;
  }

  public void setJnlId(Long jnlId) {
    this.jnlId = jnlId;
  }

  @Column(name = "TITLE_EN")
  public String getTitleEn() {
    return titleEn;
  }

  public void setTitleEn(String titleEn) {
    this.titleEn = titleEn;
  }

  @Column(name = "TITLE_XX")
  public String getTitleXx() {
    return titleXx;
  }

  public void setTitleXx(String titleXx) {
    this.titleXx = titleXx;
  }

  @Column(name = "PISSN")
  public String getPissn() {
    return pissn;
  }

  public void setPissn(String pissn) {
    this.pissn = pissn;
  }

  @Column(name = "EISSN")
  public String getEissn() {
    return eissn;
  }

  public void setEissn(String eissn) {
    this.eissn = eissn;
  }

  @Column(name = "CN")
  public String getCn() {
    return cn;
  }

  public void setCn(String cn) {
    this.cn = cn;
  }

  @Column(name = "LANGUAGE")
  public String getLanguage() {
    return language;
  }

  public void setLanguage(String language) {
    this.language = language;
  }

  @Column(name = "START_YEAR")
  public String getStartYear() {
    return startYear;
  }

  public void setStartYear(String startYear) {
    this.startYear = startYear;
  }

  @Column(name = "END_YEAR")
  public String getEndYear() {
    return endYear;
  }

  public void setEndYear(String endYear) {
    this.endYear = endYear;
  }

  @Column(name = "OA_STATUS")
  public String getOaStatus() {
    return oaStatus;
  }

  public void setOaStatus(String oaStatus) {
    this.oaStatus = oaStatus;
  }

  @Column(name = "ACTIVE_STATUS")
  public String getActiveStatus() {
    return activeStatus;
  }

  public void setActiveStatus(String activeStatus) {
    this.activeStatus = activeStatus;
  }

  @Column(name = "FREQUENCY_ZH")
  public String getFrequencyZh() {
    return frequencyZh;
  }

  public void setFrequencyZh(String frequencyZh) {
    this.frequencyZh = frequencyZh;
  }

  @Column(name = "FREQUENCY_EN")
  public String getFrequencyEn() {
    return frequencyEn;
  }

  public void setFrequencyEn(String frequencyEn) {
    this.frequencyEn = frequencyEn;
  }

  @Column(name = "REGION_ID")
  public Long getRegionId() {
    return regionId;
  }

  public void setRegionId(Long regionId) {
    this.regionId = regionId;
  }

  @Column(name = "JOURNAL_URL")
  public String getJournalUrl() {
    return journalUrl;
  }

  public void setJournalUrl(String journalUrl) {
    this.journalUrl = journalUrl;
  }

  @Column(name = "DESCRIPTION_XX")
  public String getDescriptionXx() {
    return descriptionXx;
  }

  public void setDescriptionXx(String descriptionXx) {
    this.descriptionXx = descriptionXx;
  }

  @Column(name = "DESCRIPTION_EN")
  public String getDescriptionEn() {
    return descriptionEn;
  }

  public void setDescriptionEn(String descriptionEn) {
    this.descriptionEn = descriptionEn;
  }

  @Column(name = "TITLE_ABBR")
  public String getTitleAbbr() {
    return titleAbbr;
  }

  public void setTitleAbbr(String titleAbbr) {
    this.titleAbbr = titleAbbr;
  }

  @Column(name = "SUBMISSION_URL")
  public String getSubmissionUrl() {
    return submissionUrl;
  }

  public void setSubmissionUrl(String submissionUrl) {
    this.submissionUrl = submissionUrl;
  }

  @Column(name = "ROMEO_COLOUR")
  public String getRomeoColour() {
    return romeoColour;
  }

  public void setRomeoColour(String romeoColour) {
    this.romeoColour = romeoColour;
  }

  @Transient
  public Boolean getIsNewTitle() {
    return isNewTitle;
  }

  public void setIsNewTitle(Boolean isNewTitle) {
    this.isNewTitle = isNewTitle;
  }

  @Transient
  public Boolean getIsNewPissn() {
    return isNewPissn;
  }

  public void setIsNewPissn(Boolean isNewPissn) {
    this.isNewPissn = isNewPissn;
  }

  @Transient
  public Integer getThrowsType() {
    return throwsType;
  }

  public void setThrowsType(Integer throwsType) {
    this.throwsType = throwsType;
  }

}
