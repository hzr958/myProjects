package com.smate.sie.center.task.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@SuppressWarnings("serial")
@Entity
@Table(name = "DATA_SRV_PUB_TMP")
public class SieDataSrvPubTmp implements Serializable {

  // 成果Id
  private Long pubId;
  // 标题
  private String title;
  // 期号
  private String issue;
  // 卷号
  private String volume;
  // 开始页
  private String startPage;
  // 结束页
  private String endPage;
  // doi
  private String doi;
  // 作者
  private String authorName;
  private String applyTime;
  private String journalName;
  private String issn;
  // 文章号
  private String articleNo;
  private String remark;
  private String keyWords;
  private String meetingTitle;
  private String meetingOrganizers;
  private String summary;
  // 成果类型
  private Integer pubType;
  private String startDate;
  private String endDate;
  private String countryName;
  private String city;
  private Integer listEi;
  private Integer listSci;
  private Integer listIstp;
  private Integer listSsci;
  private String http;
  private String zzPubMembers;// 作者列表
  private Long citedTimes; // 引用数

  @Column(name = "PUB_TYPE")
  public Integer getPubType() {
    return pubType;
  }

  public void setPubType(Integer pubType) {
    this.pubType = pubType;
  }

  @Column(name = "VOLUME")
  public String getVolume() {
    return volume;
  }

  public void setVolume(String volume) {
    this.volume = volume;
  }

  @Column(name = "ISSUE")
  public String getIssue() {
    return issue;
  }

  public void setIssue(String issue) {
    this.issue = issue;
  }

  @Column(name = "START_PAGE")
  public String getStartPage() {
    return startPage;
  }

  public void setStartPage(String startPage) {
    this.startPage = startPage;
  }

  @Column(name = "END_PAGE")
  public String getEndPage() {
    return endPage;
  }

  public void setEndPage(String endPage) {
    this.endPage = endPage;
  }

  @Column(name = "ARTICLE_NO")
  public String getArticleNo() {
    return articleNo;
  }

  public void setArticleNo(String articleNo) {
    this.articleNo = articleNo;
  }

  @Column(name = "DOI")
  public String getDoi() {
    return doi;
  }

  public void setDoi(String doi) {
    this.doi = doi;
  }

  @Id
  @Column(name = "PUB_ID")
  /*
   * @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
   * 
   * @SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_PUBLICATION", allocationSize = 1)
   */
  public Long getPubId() {
    return pubId;
  }

  /**
   * @param pubId
   */
  public void setPubId(Long pubId) {
    this.pubId = pubId;
  }

  @Column(name = "TITLE")
  public String getTitle() {
    return title;
  }

  @Column(name = "AUTHOR_NAME")
  public String getAuthorName() {
    return authorName;
  }

  @Column(name = "APPLY_TIME")
  public String getApplyTime() {
    return applyTime;
  }

  @Column(name = "JOURNAL_NAME")
  public String getJournalName() {
    return journalName;
  }

  @Column(name = "ISSN")
  public String getIssn() {
    return issn;
  }

  @Column(name = "REMARK")
  public String getRemark() {
    return remark;
  }

  @Column(name = "KEY_WORDS")
  public String getKeyWords() {
    return keyWords;
  }

  @Column(name = "MEETING_TITLE")
  public String getMeetingTitle() {
    return meetingTitle;
  }

  @Column(name = "MEETING_ORGANIZERS")
  public String getMeetingOrganizers() {
    return meetingOrganizers;
  }

  @Column(name = "SUMMARY")
  public String getSummary() {
    return summary;
  }

  @Column(name = "START_DATE")
  public String getStartDate() {
    return startDate;
  }

  @Column(name = "END_DATE")
  public String getEndDate() {
    return endDate;
  }

  @Column(name = "COUNTRY_NAME")
  public String getCountryName() {
    return countryName;
  }

  @Column(name = "CITY")
  public String getCity() {
    return city;
  }

  @Column(name = "LIST_EI")
  public Integer getListEi() {
    return listEi;
  }

  @Column(name = "LIST_SCI")
  public Integer getListSci() {
    return listSci;
  }

  @Column(name = "LIST_ISTP")
  public Integer getListIstp() {
    return listIstp;
  }

  @Column(name = "LIST_SSCI")
  public Integer getListSsci() {
    return listSsci;
  }

  @Column(name = "HTTP")
  public String getHttp() {
    return http;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public void setAuthorName(String authorName) {
    this.authorName = authorName;
  }

  public void setApplyTime(String applyTime) {
    this.applyTime = applyTime;
  }

  public void setJournalName(String journalName) {
    this.journalName = journalName;
  }

  public void setIssn(String issn) {
    this.issn = issn;
  }

  public void setRemark(String remark) {
    this.remark = remark;
  }

  public void setKeyWords(String keyWords) {
    this.keyWords = keyWords;
  }

  public void setMeetingTitle(String meetingTitle) {
    this.meetingTitle = meetingTitle;
  }

  public void setMeetingOrganizers(String meetingOrganizers) {
    this.meetingOrganizers = meetingOrganizers;
  }

  public void setSummary(String summary) {
    this.summary = summary;
  }

  public void setStartDate(String startDate) {
    this.startDate = startDate;
  }

  public void setEndDate(String endDate) {
    this.endDate = endDate;
  }

  public void setCountryName(String countryName) {
    this.countryName = countryName;
  }

  public void setCity(String city) {
    this.city = city;
  }

  public void setListEi(Integer listEi) {
    this.listEi = listEi;
  }

  public void setListSci(Integer listSci) {
    this.listSci = listSci;
  }

  public void setListIstp(Integer listIstp) {
    this.listIstp = listIstp;
  }

  public void setListSsci(Integer listSsci) {
    this.listSsci = listSsci;
  }

  public void setHttp(String http) {
    this.http = http;
  }

  @Column(name = "PUB_MEMBERS")
  public String getZzPubMembers() {
    return zzPubMembers;
  }

  public void setZzPubMembers(String zzPubMembers) {
    this.zzPubMembers = zzPubMembers;
  }

  @Column(name = "cited_times")
  public Long getCitedTimes() {
    return citedTimes;
  }

  public void setCitedTimes(Long citedTimes) {
    this.citedTimes = citedTimes;
  }

  public SieDataSrvPubTmp(Long pubId, String title, String issue, String volume, String startPage, String endPage,
      String doi, String authorName, String applyTime, String journalName, String issn, String articleNo, String remark,
      String keyWords, String meetingTitle, String meetingOrganizers, String summary, Integer pubType, String startDate,
      String endDate, String countryName, String city, Integer listEi, Integer listSci, Integer listIstp,
      Integer listSsci, String http) {
    super();
    this.pubId = pubId;
    this.title = title;
    this.issue = issue;
    this.volume = volume;
    this.startPage = startPage;
    this.endPage = endPage;
    this.doi = doi;
    this.authorName = authorName;
    this.applyTime = applyTime;
    this.journalName = journalName;
    this.issn = issn;
    this.articleNo = articleNo;
    this.remark = remark;
    this.keyWords = keyWords;
    this.meetingTitle = meetingTitle;
    this.meetingOrganizers = meetingOrganizers;
    this.summary = summary;
    this.pubType = pubType;
    this.startDate = startDate;
    this.endDate = endDate;
    this.countryName = countryName;
    this.city = city;
    this.listEi = listEi;
    this.listSci = listSci;
    this.listIstp = listIstp;
    this.listSsci = listSsci;
    this.http = http;
  }

  public SieDataSrvPubTmp() {
    super();
  }

}
