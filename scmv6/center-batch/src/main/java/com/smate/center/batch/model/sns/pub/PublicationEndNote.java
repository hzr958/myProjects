package com.smate.center.batch.model.sns.pub;

import java.io.Serializable;

/**
 * endnote 格式.
 * 
 * @author chenxiangrong
 * 
 */
public class PublicationEndNote implements Serializable {
  /**
   * 
   */
  private static final long serialVersionUID = 6315365131487201388L;
  // 成果编号 2
  private Long id;
  // 成果类型 const_pub_type
  private Integer typeId;
  // 类别名称 1
  private String typeName;
  // 作者 3
  private String authorNames;
  // 出版年份 4
  private Integer publishYear;
  // 标题 5
  private String title;
  // 6
  private String jname;
  // 7
  private String volume;
  // 8
  private String issue;
  // 页码9
  private Integer startPage;
  private Integer endPage;

  // 出版月份 10
  private Integer publishMonth;
  // 出版日期 10
  private Integer publishDay;
  // 12
  private String issn;
  // 13
  private String doi;
  // 14
  private String accessionNumber;
  // 关键词 15
  private String keywords;
  // 摘要 16
  private String abstractText;
  // 17
  private String materialsandMethods;
  // 18
  private String results;
  // 19
  private String notes;
  // 引用次数20
  private Integer citedTimes;
  // 21
  private String citedReferencesCount;
  // 22
  private String url;
  // 23
  private String authorAddress;
  // 24
  private String language;
  private String sourceUrl;
  private String proceedingTitle;
  private String confVenue;
  private String original;
  private String articleNumber;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Integer getTypeId() {
    return typeId;
  }

  public void setTypeId(Integer typeId) {
    this.typeId = typeId;
  }

  public String getTypeName() {
    return typeName;
  }

  public void setTypeName(String typeName) {
    this.typeName = typeName;
  }

  public String getAuthorNames() {
    return authorNames;
  }

  public void setAuthorNames(String authorNames) {
    this.authorNames = authorNames;
  }

  public Integer getPublishYear() {
    return publishYear;
  }

  public void setPublishYear(Integer publishYear) {
    this.publishYear = publishYear;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getJname() {
    return jname;
  }

  public void setJname(String jname) {
    this.jname = jname;
  }

  public String getVolume() {
    return volume;
  }

  public void setVolume(String volume) {
    this.volume = volume;
  }

  public String getIssue() {
    return issue;
  }

  public void setIssue(String issue) {
    this.issue = issue;
  }

  public Integer getStartPage() {
    return startPage;
  }

  public void setStartPage(Integer startPage) {
    this.startPage = startPage;
  }

  public Integer getEndPage() {
    return endPage;
  }

  public void setEndPage(Integer endPage) {
    this.endPage = endPage;
  }

  public Integer getPublishMonth() {
    return publishMonth;
  }

  public void setPublishMonth(Integer publishMonth) {
    this.publishMonth = publishMonth;
  }

  public Integer getPublishDay() {
    return publishDay;
  }

  public void setPublishDay(Integer publishDay) {
    this.publishDay = publishDay;
  }

  public String getIssn() {
    return issn;
  }

  public void setIssn(String issn) {
    this.issn = issn;
  }

  public String getDoi() {
    return doi;
  }

  public void setDoi(String doi) {
    this.doi = doi;
  }

  public String getAccessionNumber() {
    return accessionNumber;
  }

  public void setAccessionNumber(String accessionNumber) {
    this.accessionNumber = accessionNumber;
  }

  public String getKeywords() {
    return keywords;
  }

  public void setKeywords(String keywords) {
    this.keywords = keywords;
  }

  public String getAbstractText() {
    return abstractText;
  }

  public void setAbstractText(String abstractText) {
    this.abstractText = abstractText;
  }

  public String getMaterialsandMethods() {
    return materialsandMethods;
  }

  public void setMaterialsandMethods(String materialsandMethods) {
    this.materialsandMethods = materialsandMethods;
  }

  public String getResults() {
    return results;
  }

  public void setResults(String results) {
    this.results = results;
  }

  public String getNotes() {
    return notes;
  }

  public void setNotes(String notes) {
    this.notes = notes;
  }

  public Integer getCitedTimes() {
    return citedTimes;
  }

  public void setCitedTimes(Integer citedTimes) {
    this.citedTimes = citedTimes;
  }

  public String getCitedReferencesCount() {
    return citedReferencesCount;
  }

  public void setCitedReferencesCount(String citedReferencesCount) {
    this.citedReferencesCount = citedReferencesCount;
  }

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  public String getAuthorAddress() {
    return authorAddress;
  }

  public void setAuthorAddress(String authorAddress) {
    this.authorAddress = authorAddress;
  }

  public String getLanguage() {
    return language;
  }

  public void setLanguage(String language) {
    this.language = language;
  }

  public String getSourceUrl() {
    return sourceUrl;
  }

  public void setSourceUrl(String sourceUrl) {
    this.sourceUrl = sourceUrl;
  }

  public String getProceedingTitle() {
    return proceedingTitle;
  }

  public void setProceedingTitle(String proceedingTitle) {
    this.proceedingTitle = proceedingTitle;
  }

  public String getConfVenue() {
    return confVenue;
  }

  public void setConfVenue(String confVenue) {
    this.confVenue = confVenue;
  }

  public String getOriginal() {
    return original;
  }

  public void setOriginal(String original) {
    this.original = original;
  }

  public String getArticleNumber() {
    return articleNumber;
  }

  public void setArticleNumber(String articleNumber) {
    this.articleNumber = articleNumber;
  }

}
