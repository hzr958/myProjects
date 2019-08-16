package com.smate.center.open.model.sie.publication;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;


/**
 * 单位成果.
 * 
 * @author xys
 * 
 */
@Entity
@Table(name = "PUBLICATION")
public class PublicationRol implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = 2092370568965104178L;
  @Id
  @Column(name = "PUB_ID")
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_PUBLICATION", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
  private Long id;// 成果编号

  @Column(name = "INS_ID") // 所有者(对应 Institution的insId)
  private Long insId;

  @Column(name = "PUB_TYPE")
  private Integer typeId;// 成果类型 const_pub_type

  @Column(name = "PUBLISH_YEAR")
  private Integer publishYear;// 出版年份

  @Column(name = "PUBLISH_MONTH")
  private Integer publishMonth;// 出版月份

  @Column(name = "PUBLISH_DAY")
  private Integer publishDay;// 出版日期

  @Column(name = "STATUS")
  private Integer status;// 1未批准/2已批准/3已删除/4待单位确认

  @Column(name = "JID") // 成果所属 期刊ID
  private Long jid;

  @Column(name = "CITED_TIMES")
  private Integer citedTimes;// 引用次数

  @Column(name = "ZH_TITLE")
  private String zhTitle;// 中文标题

  @Column(name = "EN_TITLE")
  private String enTitle;// 外文标题

  @Column(name = "UPDATE_DATE")
  private Date updateDate;// 最后更新时间

  @Column(name = "AUTHOR_NAMES")
  private String authorNames;// 作者

  @Column(name = "BRIEF_DESC")
  private String briefDesc;// 中文来源

  @Column(name = "BRIEF_DESC_EN")
  private String briefDescEn;// 英文来源

  @Column(name = "DOI")
  private String doi;

  @Column(name = "ISSUE")
  private String issue;

  @Column(name = "VOLUME")
  private String volume;

  @Column(name = "START_PAGE")
  private String startPage;

  @Column(name = "END_PAGE")
  private String endPage;

  @Column(name = "ARTICLE_NO")
  private String articleNo;

  @Column(name = "CITED_LIST")
  private String citedList;


  @Transient
  private boolean isFirstAuthorIns = false;// 是否第一作者单位

  @Transient
  private String paperIns;// 论文单位

  @Transient
  private String jnlZhName;// 期刊中文名

  @Transient
  private String jnlEnName;// 期刊英文名

  @Transient
  private Integer listEi;// 是否被EI收录

  @Transient
  private Integer listSci;// 是否被SCI收录

  @Transient
  private Integer listIstp;// 是否被ISTP收录

  @Transient
  private Integer listSsci;// 是否被SSCI收录

  @Transient
  private String resDirection;

  @Transient
  private String catagoryNo;


  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Long getInsId() {
    return insId;
  }

  public void setInsId(Long insId) {
    this.insId = insId;
  }

  public Integer getStatus() {
    return status;
  }

  public String getAuthorNames() {
    return authorNames;
  }

  public String getBriefDesc() {
    return briefDesc;
  }

  public String getBriefDescEn() {
    return briefDescEn;
  }

  public void setBriefDescEn(String briefDescEn) {
    this.briefDescEn = briefDescEn;
  }

  public void setStatus(Integer status) {
    this.status = status;
  }

  public Integer getTypeId() {
    return typeId;
  }

  public void setTypeId(Integer typeId) {
    this.typeId = typeId;
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

  public Long getJid() {
    return jid;
  }

  public void setJid(Long jid) {
    this.jid = jid;
  }

  public Integer getCitedTimes() {
    return citedTimes;
  }

  public void setCitedTimes(Integer citedTimes) {
    this.citedTimes = citedTimes;
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

  public Date getUpdateDate() {
    return updateDate;
  }

  public void setUpdateDate(Date updateDate) {
    this.updateDate = updateDate;
  }

  public String getDoi() {
    return doi;
  }

  public void setDoi(String doi) {
    this.doi = doi;
  }

  public boolean isFirstAuthorIns() {
    return isFirstAuthorIns;
  }

  public void setFirstAuthorIns(boolean isFirstAuthorIns) {
    this.isFirstAuthorIns = isFirstAuthorIns;
  }

  public String getPaperIns() {
    return paperIns;
  }

  public void setPaperIns(String paperIns) {
    this.paperIns = paperIns;
  }

  public String getJnlZhName() {
    return jnlZhName;
  }

  public void setJnlZhName(String jnlZhName) {
    this.jnlZhName = jnlZhName;
  }

  public String getJnlEnName() {
    return jnlEnName;
  }

  public void setJnlEnName(String jnlEnName) {
    this.jnlEnName = jnlEnName;
  }

  public Integer getListEi() {
    return listEi;
  }

  public void setListEi(Integer listEi) {
    this.listEi = listEi;
  }

  public Integer getListSci() {
    return listSci;
  }

  public void setListSci(Integer listSci) {
    this.listSci = listSci;
  }

  public Integer getListIstp() {
    return listIstp;
  }

  public void setListIstp(Integer listIstp) {
    this.listIstp = listIstp;
  }

  public Integer getListSsci() {
    return listSsci;
  }

  public void setListSsci(Integer listSsci) {
    this.listSsci = listSsci;
  }

  public String getResDirection() {
    return resDirection;
  }

  public void setResDirection(String resDirection) {
    this.resDirection = resDirection;
  }

  public String getCatagoryNo() {
    return catagoryNo;
  }

  public void setCatagoryNo(String catagoryNo) {
    this.catagoryNo = catagoryNo;
  }

  public String getIssue() {
    return issue;
  }

  public void setIssue(String issue) {
    this.issue = issue;
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

  public String getArticleNo() {
    return articleNo;
  }

  public void setArticleNo(String articleNo) {
    this.articleNo = articleNo;
  }

  public String getCitedList() {
    return citedList;
  }

  public void setCitedList(String citedList) {
    this.citedList = citedList;
  }

  public void setAuthorNames(String authorNames) {
    this.authorNames = authorNames;
  }

  public void setBriefDesc(String briefDesc) {
    this.briefDesc = briefDesc;
  }


}
