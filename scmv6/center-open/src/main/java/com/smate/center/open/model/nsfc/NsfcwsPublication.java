package com.smate.center.open.model.nsfc;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * 基金委webservice集成smate成果接口获取GoogleScholar库相关人员信息.
 * 
 * @author lichangwen
 * 
 */
@Entity
@Table(name = "NSFC_WS_PUBLICATION")
public class NsfcwsPublication implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = 3489030855516345947L;
  @Id
  @Column(name = "PUB_ID")
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_NSFCWS_PUBLICATION", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
  private Long id;
  @Column(name = "PSN_ID")
  private Long psnId;
  // 来源db
  @Column(name = "SOURCE_DB")
  private String sourcedb;
  // 成果年份
  @Column(name = "PUB_YEAR")
  private Integer pubYear;
  // 成果类型
  @Column(name = "PUB_TYPE")
  private Integer pubTypeId;
  // 中文标题
  @Column(name = "ZH_TITLE")
  private String zhTitle;
  // 外文标题
  @Column(name = "EN_TITLE")
  private String enTitle;
  // "中文标题hash_code，查重时使用，统一调用publicationhash.titlecode(title) 取得hash_code"
  @Column(name = "ZH_TITLE_HASH")
  private Integer zhTitleHash;
  // "英文标题hash_code，查重时使用 统一调用publicationhash.titlecode(title) 取得hash_code"
  @Column(name = "EN_TITLE_HASH")
  private Integer enTitleHash;
  // 来源
  @Column(name = "BRIEF_DESC_EN")
  private String briefDescZh;
  // 来源
  @Column(name = "BRIEF_DESC_ZH")
  private String briefDescEn;
  // 作者
  @Column(name = "AUTHOR_NAMES")
  private String authorNames;
  // 出版年份
  @Column(name = "PUBLISH_YEAR")
  private Integer publishYear;
  // 出版月份
  @Column(name = "PUBLISH_MONTH")
  private Integer publishMonth;
  // 出版日期
  @Column(name = "PUBLISH_DAY")
  private Integer publishDay;
  // 引用情况，用逗号分隔(如：SCI,EI)
  @Column(name = "CITED_LIST")
  private String citedList;
  // 引用次数
  @Column(name = "CITED_TIMES")
  private Integer citedTimes;
  @Column(name = "DOI")
  private String doi;
  @Column(name = "STATUS")
  private Integer status = 0;
  // 创建时间
  @Column(name = "CREATE_DATE")
  private Date createDate;

  public NsfcwsPublication() {
    super();
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Long getPsnId() {
    return psnId;
  }

  public void setPsnId(Long psnId) {
    this.psnId = psnId;
  }

  public String getSourcedb() {
    return sourcedb;
  }

  public void setSourcedb(String sourcedb) {
    this.sourcedb = sourcedb;
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

  public Integer getZhTitleHash() {
    return zhTitleHash;
  }

  public void setZhTitleHash(Integer zhTitleHash) {
    this.zhTitleHash = zhTitleHash;
  }

  public Integer getEnTitleHash() {
    return enTitleHash;
  }

  public void setEnTitleHash(Integer enTitleHash) {
    this.enTitleHash = enTitleHash;
  }

  public String getBriefDescZh() {
    return briefDescZh;
  }

  public void setBriefDescZh(String briefDescZh) {
    this.briefDescZh = briefDescZh;
  }

  public String getBriefDescEn() {
    return briefDescEn;
  }

  public void setBriefDescEn(String briefDescEn) {
    this.briefDescEn = briefDescEn;
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

  public String getCitedList() {
    return citedList;
  }

  public void setCitedList(String citedList) {
    this.citedList = citedList;
  }

  public Integer getCitedTimes() {
    return citedTimes;
  }

  public void setCitedTimes(Integer citedTimes) {
    this.citedTimes = citedTimes;
  }

  public String getDoi() {
    return doi;
  }

  public void setDoi(String doi) {
    this.doi = doi;
  }

  public Integer getStatus() {
    return status;
  }

  public void setStatus(Integer status) {
    this.status = status;
  }

  public Date getCreateDate() {
    return createDate;
  }

  public void setCreateDate(Date createDate) {
    this.createDate = createDate;
  }

}
