package com.smate.center.batch.model.solr;

import java.io.Serializable;

public class SearchIndexPub implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = 5102727613307249311L;

  private String businessType;
  private String runEnv;
  private Long id;
  private Long pubId;
  private String zhTitle;
  private String enTitle;
  private String authors;
  private String doi;
  private String zhAbstract;
  private String enAbstract;
  private String zhKeywords;
  private String enKeywords;
  private Integer pubYear;
  private Integer pubTypeId;
  private String journalName;

  public String getBusinessType() {
    return businessType;
  }

  public void setBusinessType(String businessType) {
    this.businessType = businessType;
  }

  public String getRunEnv() {
    return runEnv;
  }

  public void setRunEnv(String runEnv) {
    this.runEnv = runEnv;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Long getPubId() {
    return pubId;
  }

  public void setPubId(Long pubId) {
    this.pubId = pubId;
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

  public String getAuthors() {
    return authors;
  }

  public void setAuthors(String authors) {
    this.authors = authors;
  }

  public String getDoi() {
    return doi;
  }

  public void setDoi(String doi) {
    this.doi = doi;
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

  public Integer getPubYear() {
    return pubYear;
  }

  public void setPubYear(Integer pubYear) {
    this.pubYear = pubYear;
  }

  public Integer getPubTypeId() {
    return pubTypeId;
  }

  public void setPubTypeId(Integer pubTypeId) {
    this.pubTypeId = pubTypeId;
  }

  public String getJournalName() {
    return journalName;
  }

  public void setJournalName(String journalName) {
    this.journalName = journalName;
  }

}
