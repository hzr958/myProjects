package com.smate.center.batch.model.solr;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

/*
 * 与publication_all相同，用来查重后生产solrindex
 */
@Entity
@Table(name = "PUB_INDEX_INFO_PDWH")
public class PubInfoPdwh implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = 6205254798244027180L;

  @Id
  @Column(name = "ID")
  private Long pubAllId;

  @Column(name = "PUB_ID")
  private Long pubId;

  @Column(name = "DBID")
  private Integer dbId;

  @Column(name = "PUB_YEAR")
  private Integer pubYear;

  @Column(name = "PUB_TYPE")
  private Integer pubType;

  @Column(name = "ZH_TITLE")
  private String zhTitle;

  @Column(name = "EN_TITLE")
  private String enTitle;

  @Column(name = "AUTHOR_NAMES")
  private String authorNames;

  @Column(name = "ZH_BRIEF_DESC")
  private String zhBrief;

  @Column(name = "EN_BRIEF_DESC")
  private String enBrief;

  @Column(name = "ZH_TITLE_HASH")
  private Long zhTitleHash;

  @Column(name = "EN_TITLE_HASH")
  private Long enTitleHash;

  @Column(name = "JNL_ID")
  private Long journalId;

  @Column(name = "LANGUAGE")
  private String language;

  @Column(name = "IDX")
  private Integer status;

  @Transient
  private String enAbstract;
  @Transient
  private String zhAbstract;
  @Transient
  private String enKeywords;
  @Transient
  private String zhKeywords;
  @Transient
  private String doi;
  // patent
  @Transient
  private String organization;
  @Transient
  private String patentNo;
  @Transient
  private Integer patentCategory;

  public PubInfoPdwh() {
    super();
  }

  public PubInfoPdwh(Long pubAllId, Long zhTitleHash, Long enTitleHash) {
    super();
    this.pubAllId = pubAllId;
    this.zhTitleHash = zhTitleHash;
    this.enTitleHash = enTitleHash;
  }

  public PubInfoPdwh(Long pubAllId, Long pubId, Integer dbId, Long zhTitleHash, Long enTitleHash) {
    super();
    this.pubAllId = pubAllId;
    this.pubId = pubId;
    this.dbId = dbId;
    this.zhTitleHash = zhTitleHash;
    this.enTitleHash = enTitleHash;
  }

  public Long getPubAllId() {
    return pubAllId;
  }

  public void setPubAllId(Long pubAllId) {
    this.pubAllId = pubAllId;
  }

  public Long getPubId() {
    return pubId;
  }

  public void setPubId(Long pubId) {
    this.pubId = pubId;
  }

  public Integer getDbId() {
    return dbId;
  }

  public void setDbId(Integer dbId) {
    this.dbId = dbId;
  }

  public Integer getPubYear() {
    return pubYear;
  }

  public void setPubYear(Integer pubYear) {
    this.pubYear = pubYear;
  }

  public Integer getPubType() {
    return pubType;
  }

  public void setPubType(Integer pubType) {
    this.pubType = pubType;
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

  public String getAuthorNames() {
    return authorNames;
  }

  public void setAuthorNames(String authorNames) {
    this.authorNames = authorNames;
  }

  public String getZhBrief() {
    return zhBrief;
  }

  public void setZhBrief(String zhBrief) {
    this.zhBrief = zhBrief;
  }

  public String getEnBrief() {
    return enBrief;
  }

  public void setEnBrief(String enBrief) {
    this.enBrief = enBrief;
  }

  public Long getJournalId() {
    return journalId;
  }

  public void setJournalId(Long journalId) {
    this.journalId = journalId;
  }

  public String getLanguage() {
    return language;
  }

  public void setLanguage(String language) {
    this.language = language;
  }

  public Integer getStatus() {
    return status;
  }

  public void setStatus(Integer status) {
    this.status = status;
  }

  public Long getZhTitleHash() {
    return zhTitleHash;
  }

  public void setZhTitleHash(Long zhTitleHash) {
    this.zhTitleHash = zhTitleHash;
  }

  public Long getEnTitleHash() {
    return enTitleHash;
  }

  public void setEnTitleHash(Long enTitleHash) {
    this.enTitleHash = enTitleHash;
  }

  public String getEnAbstract() {
    return enAbstract;
  }

  public void setEnAbstract(String enAbstract) {
    this.enAbstract = enAbstract;
  }

  public String getZhAbstract() {
    return zhAbstract;
  }

  public void setZhAbstract(String zhAbstract) {
    this.zhAbstract = zhAbstract;
  }

  public String getEnKeywords() {
    return enKeywords;
  }

  public void setEnKeywords(String enKeywords) {
    this.enKeywords = enKeywords;
  }

  public String getZhKeywords() {
    return zhKeywords;
  }

  public void setZhKeywords(String zhKeywords) {
    this.zhKeywords = zhKeywords;
  }

  public String getOrganization() {
    return organization;
  }

  public void setOrganization(String organization) {
    this.organization = organization;
  }

  public String getPatentNo() {
    return patentNo;
  }

  public void setPatentNo(String patentNo) {
    this.patentNo = patentNo;
  }

  public String getDoi() {
    return doi;
  }

  public void setDoi(String doi) {
    this.doi = doi;
  }

  public Integer getPatentCategory() {
    return patentCategory;
  }

  public void setPatentCategory(Integer patentCategory) {
    this.patentCategory = patentCategory;
  }

}
