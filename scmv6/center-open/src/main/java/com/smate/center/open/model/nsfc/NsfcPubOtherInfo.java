package com.smate.center.open.model.nsfc;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 成果其他信息.
 * 
 * @author zhanglingling
 * 
 */
@Entity
@Table(name = "NSFC_PUB_OTHERINFO")
public class NsfcPubOtherInfo implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = 1L;
  // Publication ID
  private Long pubId;
  // 成果类型 const_pub_type
  private Integer typeId;
  // 出版年份
  private Integer publishYear;
  // 出版月份
  private Integer publishMonth;
  // 出版日期
  private Integer publishDay;
  // 出版状态
  private Integer publicationStatus;
  // 专利状态
  private Integer patentStatus;
  // 会议开始日期（会议论文）/开始生效日期（专利）
  private Integer startYear;
  private Integer startMonth;
  private Integer startDay;
  private Integer endYear;
  private Integer endMonth;
  private Integer endDay;
  // 起止页码
  private String startPage;
  private String endPage;
  // 会议名称
  private String confName;
  // 城市
  private String city;
  // 奖励等级
  private String awardGrade;
  // 奖励类别
  private String awardCategory;
  // 颁奖机构
  private String issueInsName;
  // 出版社
  private String publisher;
  // 总字数
  private Integer totalWords;
  // 语言
  private String language;
  // 论文类别
  private String paperType;
  // 期刊名称
  private String jname;
  // 国家名称
  private String countryName;
  // 专利号
  private String patentNo;
  // 专利公开号
  private String patentOpenNO;
  // volume of the journal or book, i.e. 13
  private String volume;
  // issue no. of journal or book
  private String issue;
  // 来源(申请书成果)
  private String source;
  // 专利语言
  private Integer bookLanguage;

  @Id
  @Column(name = "PUB_ID")
  public Long getPubId() {
    return pubId;
  }

  public void setPubId(Long id) {
    this.pubId = id;
  }

  @Column(name = "PUB_TYPE")
  public Integer getTypeId() {
    return typeId;
  }

  public void setTypeId(Integer typeId) {
    this.typeId = typeId;
  }

  @Column(name = "PUBLISH_YEAR")
  public Integer getPublishYear() {
    return publishYear;
  }

  public void setPublishYear(Integer publishYear) {
    this.publishYear = publishYear;
  }

  @Column(name = "PUBLISH_MONTH")
  public Integer getPublishMonth() {
    return publishMonth;
  }

  public void setPublishMonth(Integer publishMonth) {
    this.publishMonth = publishMonth;
  }

  @Column(name = "PUBLISH_DAY")
  public Integer getPublishDay() {
    return publishDay;
  }

  public void setPublishDay(Integer publishDay) {
    this.publishDay = publishDay;
  }

  @Column(name = "PUBLICATION_STATUS")
  public Integer getPublicationStatus() {
    return publicationStatus;
  }

  public void setPublicationStatus(Integer publicationStatus) {
    this.publicationStatus = publicationStatus;
  }

  @Column(name = "PATENT_STATUS")
  public Integer getPatentStatus() {
    return patentStatus;
  }

  public void setPatentStatus(Integer patentStatus) {
    this.patentStatus = patentStatus;
  }


  @Column(name = "START_YEAR")
  public Integer getStartYear() {
    return startYear;
  }

  public void setStartYear(Integer startYear) {
    this.startYear = startYear;
  }

  @Column(name = "START_MONTH")
  public Integer getStartMonth() {
    return startMonth;
  }

  public void setStartMonth(Integer startMonth) {
    this.startMonth = startMonth;
  }

  @Column(name = "START_DAY")
  public Integer getStartDay() {
    return startDay;
  }

  public void setStartDay(Integer startDay) {
    this.startDay = startDay;
  }

  @Column(name = "END_YEAR")
  public Integer getEndYear() {
    return endYear;
  }

  public void setEndYear(Integer endYear) {
    this.endYear = endYear;
  }

  @Column(name = "END_MONTH")
  public Integer getEndMonth() {
    return endMonth;
  }

  public void setEndMonth(Integer endMonth) {
    this.endMonth = endMonth;
  }

  @Column(name = "END_DAY")
  public Integer getEndDay() {
    return endDay;
  }

  public void setEndDay(Integer endDay) {
    this.endDay = endDay;
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

  @Column(name = "CONF_NAME")
  public String getConfName() {
    return confName;
  }

  public void setConfName(String confName) {
    this.confName = confName;
  }

  @Column(name = "CITY")
  public String getCity() {
    return city;
  }

  public void setCity(String city) {
    this.city = city;
  }

  @Column(name = "AWARD_GRADE")
  public String getAwardGrade() {
    return awardGrade;
  }

  public void setAwardGrade(String awardGrade) {
    this.awardGrade = awardGrade;
  }

  @Column(name = "AWARD_CATEGORY")
  public String getAwardCategory() {
    return awardCategory;
  }

  public void setAwardCategory(String awardCategory) {
    this.awardCategory = awardCategory;
  }

  @Column(name = "ISSUE_INS_NAME")
  public String getIssueInsName() {
    return issueInsName;
  }

  public void setIssueInsName(String issueInsName) {
    this.issueInsName = issueInsName;
  }

  @Column(name = "PUBLISHER")
  public String getPublisher() {
    return publisher;
  }

  public void setPublisher(String publisher) {
    this.publisher = publisher;
  }

  @Column(name = "TOTAL_WORDS")
  public Integer getTotalWords() {
    return totalWords;
  }

  public void setTotalWords(Integer totalWords) {
    this.totalWords = totalWords;
  }

  @Column(name = "LANGUAGE")
  public String getLanguage() {
    return language;
  }

  public void setLanguage(String language) {
    this.language = language;
  }

  @Column(name = "PAPER_TYPE")
  public String getPaperType() {
    return paperType;
  }

  public void setPaperType(String paperType) {
    this.paperType = paperType;
  }

  @Column(name = "JNAME")
  public String getJname() {
    return jname;
  }

  public void setJname(String jname) {
    this.jname = jname;
  }

  @Column(name = "COUNTRY_NAME")
  public String getCountryName() {
    return countryName;
  }

  public void setCountryName(String countryName) {
    this.countryName = countryName;
  }

  @Column(name = "PATENT_NO")
  public String getPatentNo() {
    return patentNo;
  }

  public void setPatentNo(String patentNo) {
    this.patentNo = patentNo;
  }

  @Column(name = "PATENT_OPEN_NO")
  public String getPatentOpenNO() {
    return patentOpenNO;
  }

  public void setPatentOpenNO(String patentOpenNO) {
    this.patentOpenNO = patentOpenNO;
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

  @Column(name = "SOURCE")
  public String getSource() {
    return source;
  }

  public void setSource(String source) {
    this.source = source;
  }

  @Column(name = "BOOK_LANGUAGE")
  public Integer getBookLanguage() {
    return bookLanguage;
  }

  public void setBookLanguage(Integer bookLanguage) {
    this.bookLanguage = bookLanguage;
  }

  public static long getSerialversionuid() {
    return serialVersionUID;
  }



}
