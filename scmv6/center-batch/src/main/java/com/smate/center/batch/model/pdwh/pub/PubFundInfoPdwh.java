package com.smate.center.batch.model.pdwh.pub;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "PUB_FUNDINFO_20150512")
public class PubFundInfoPdwh implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = 3023953933442245443L;

  @Id
  @Column(name = "PUB_ID")
  private Long pubId;
  @Column(name = "FUND_INFO")
  private String fundInfo;
  @Column(name = "ZH_TITLE")
  private String zhTitle;
  @Column(name = "EN_TITLE")
  private String enTitle;
  @Column(name = "AUTHOR_NAMES")
  private String authorNames;
  @Column(name = "PUBLISH_YEAR")
  private String publishYear;
  @Column(name = "RESULT_TYPE")
  private String resultType;
  @Column(name = "ZH_ABSTRACT")
  private String zhAbstract;
  @Column(name = "EN_ABSTRACT")
  private String enAbstract;
  @Column(name = "PUB_LANGUAGE")
  private String pubLanguage;
  @Column(name = "ZH_KEYWORDS")
  private String zhKeywords;
  @Column(name = "EN_KEYWORDS")
  private String enKeywords;
  @Column(name = "JOURNAL_NAME")
  private String journalName;
  @Column(name = "ISSN")
  private String issn;
  @Column(name = "VOLUME")
  private String volume;
  @Column(name = "START_PAGE")
  private String startPage;
  @Column(name = "END_PAGE")
  private String endPage;
  @Column(name = "DOI")
  private String doi;
  @Column(name = "PROVINCE")
  private Long insId;
  @Column(name = "ISSUE")
  private String issue;

  public String getIssue() {
    return issue;
  }

  public void setIssue(String issue) {
    this.issue = issue;
  }

  public Long getPubId() {
    return pubId;
  }

  public void setPubId(Long pubId) {
    this.pubId = pubId;
  }

  public String getFundInfo() {
    return fundInfo;
  }

  public void setFundInfo(String fundInfo) {
    this.fundInfo = fundInfo;
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

  public String getPublishYear() {
    return publishYear;
  }

  public void setPublishYear(String publishYear) {
    this.publishYear = publishYear;
  }

  public String getResultType() {
    return resultType;
  }

  public void setResultType(String resultType) {
    this.resultType = resultType;
  }

  public String getZhAbstract() {
    return zhAbstract;
  }

  public void setZhAbstract(String zhAbstract) {
    this.zhAbstract = zhAbstract;
  }

  public String getEnAbstract() {
    return enAbstract;
  }

  public void setEnAbstract(String enAbstract) {
    this.enAbstract = enAbstract;
  }

  public String getPubLanguage() {
    return pubLanguage;
  }

  public void setPubLanguage(String pubLanguage) {
    this.pubLanguage = pubLanguage;
  }

  public String getZhKeywords() {
    return zhKeywords;
  }

  public void setZhKeywords(String zhKeywords) {
    this.zhKeywords = zhKeywords;
  }

  public String getEnKeywords() {
    return enKeywords;
  }

  public void setEnKeywords(String enKeywords) {
    this.enKeywords = enKeywords;
  }

  public String getJournalName() {
    return journalName;
  }

  public void setJournalName(String journalName) {
    this.journalName = journalName;
  }

  public String getIssn() {
    return issn;
  }

  public void setIssn(String issn) {
    this.issn = issn;
  }

  public String getVolume() {
    return volume;
  }

  public void setVolume(String volume) {
    this.volume = volume;
  }

  public String getStartPage() {
    return startPage;
  }

  public void setStartPage(String startPage) {
    this.startPage = startPage;
  }

  public String getEndPage() {
    return endPage;
  }

  public void setEndPage(String endPage) {
    this.endPage = endPage;
  }

  public String getDoi() {
    return doi;
  }

  public void setDoi(String doi) {
    this.doi = doi;
  }

  public Long getInsId() {
    return insId;
  }

  public void setInsId(Long insId) {
    this.insId = insId;
  }


}
