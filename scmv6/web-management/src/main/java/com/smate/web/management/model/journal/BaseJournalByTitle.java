package com.smate.web.management.model.journal;

import java.io.Serializable;
import java.sql.Clob;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

/**
 * cwli基础期刊主表.
 */
@Entity
@Table(name = "BASE_JOURNAL")
public class BaseJournalByTitle implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = 2577115337638388851L;
  // 主键
  private Long jouId;
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
  private Clob descriptionEn;
  // 期刊描述(中文或者其它语言)
  private Clob descriptionXx;

  // 期刊titel
  private List<BaseJournalTitle> jouTitleList = new ArrayList<BaseJournalTitle>();

  @Id
  @Column(name = "JNL_ID")
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_BASE_JOU", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
  public Long getJouId() {
    return jouId;
  }

  public void setJouId(Long jouId) {
    this.jouId = jouId;
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
  public Clob getDescriptionXx() {
    return descriptionXx;
  }

  public void setDescriptionXx(Clob descriptionXx) {
    this.descriptionXx = descriptionXx;
  }

  @Column(name = "DESCRIPTION_EN")
  public Clob getDescriptionEn() {
    return descriptionEn;
  }

  public void setDescriptionEn(Clob descriptionEn) {
    this.descriptionEn = descriptionEn;
  }

  // @OneToMany(mappedBy = "journal", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
  @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
  @JoinColumn(name = "JNL_ID", insertable = true, updatable = true)
  @Fetch(FetchMode.SUBSELECT)
  @OrderBy("jouTitleId ASC")
  public List<BaseJournalTitle> getJouTitleList() {
    return jouTitleList;
  }

  public void setJouTitleList(List<BaseJournalTitle> jouTitleList) {
    this.jouTitleList = jouTitleList;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

}
