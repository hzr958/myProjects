package com.smate.sie.center.task.model;

import java.io.Serializable;

/**
 * 成果查重参数.
 * 
 * @author jszhou
 */
public class PubDupParam implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = -7718717272953140847L;
  // 成果对应的外部数据库 refrence to const_ref_db
  private Long sourceDbId;
  // 成果类型 CONST_PUB_type
  private Integer pubType;
  // 文献发表年份
  private Integer pubYear;
  // isi的source_id
  private String isiId;
  private String eiId;
  private String spsId;
  // 文献库doi
  private String doi;
  // 成果所属 期刊ID
  private Long jid;
  // 成果所属 期刊名称
  private String jname;
  // isbn
  private String isbn;
  // volume of the journal or book, i.e. 13
  private String volume;
  // issue no. of journal or book
  private String issue;
  // 起始页码
  private String startPage;
  // articleNum
  private String articleNo;
  // 中文标题
  private String zhTitle;
  // 英文标题
  private String enTitle;
  // 作者名称
  private String authorNames;
  // 会议名称
  private String confName;
  private String issn;
  // jname+issn
  private Long jfingerPrint;

  public Long getSourceDbId() {
    return sourceDbId;
  }

  public Integer getPubType() {
    return pubType;
  }

  public Integer getPubYear() {
    return pubYear;
  }

  public String getIsiId() {
    return isiId;
  }

  public String getDoi() {
    return doi;
  }

  public Long getJid() {
    return jid;
  }

  public String getIsbn() {
    return isbn;
  }

  public String getVolume() {
    return volume;
  }

  public String getIssue() {
    return issue;
  }

  public String getStartPage() {
    return startPage;
  }

  public String getArticleNo() {
    return articleNo;
  }

  public String getZhTitle() {
    return zhTitle;
  }

  public String getEnTitle() {
    return enTitle;
  }

  public void setSourceDbId(Long sourceDbId) {
    this.sourceDbId = sourceDbId;
  }

  public void setPubType(Integer pubType) {
    this.pubType = pubType;
  }

  public void setPubYear(Integer pubYear) {
    this.pubYear = pubYear;
  }

  public void setIsiId(String isiId) {
    this.isiId = isiId;
  }

  public void setDoi(String doi) {
    this.doi = doi;
  }

  public void setJid(Long jid) {
    this.jid = jid;
  }

  public void setIsbn(String isbn) {
    this.isbn = isbn;
  }

  public void setVolume(String volume) {
    this.volume = volume;
  }

  public void setIssue(String issue) {
    this.issue = issue;
  }

  public void setStartPage(String startPage) {
    this.startPage = startPage;
  }

  public Long getJfingerPrint() {
    return jfingerPrint;
  }

  public void setJfingerPrint(Long jfingerPrint) {
    this.jfingerPrint = jfingerPrint;
  }

  public void setArticleNo(String articleNo) {
    this.articleNo = articleNo;
  }

  public void setZhTitle(String zhTitle) {
    this.zhTitle = zhTitle;
  }

  public void setEnTitle(String enTitle) {
    this.enTitle = enTitle;
  }

  public String getEiId() {
    return eiId;
  }

  public String getSpsId() {
    return spsId;
  }

  public void setEiId(String eiId) {
    this.eiId = eiId;
  }

  public void setSpsId(String spsId) {
    this.spsId = spsId;
  }

  public String getAuthorNames() {
    return authorNames;
  }

  public void setAuthorNames(String authorNames) {
    this.authorNames = authorNames;
  }

  public String getConfName() {
    return confName;
  }

  public void setConfName(String confName) {
    this.confName = confName;
  }

  public String getIssn() {
    return issn;
  }

  public void setIssn(String issn) {
    this.issn = issn;
  }

  public String getJname() {
    return jname;
  }

  public void setJname(String jname) {
    this.jname = jname;
  }

}
