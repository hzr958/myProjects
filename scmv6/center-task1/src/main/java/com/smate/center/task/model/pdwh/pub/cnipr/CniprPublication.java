package com.smate.center.task.model.pdwh.pub.cnipr;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * CNIPR成果表.
 * 
 * @author liqinghua
 * 
 */
@Entity
@Table(name = "CNIPR_PUBLICATION")
public class CniprPublication implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = -5722863273272161408L;

  private Long pubId;
  // 发表年份
  private Integer pubYear;
  // 网站ID
  private Integer dbId;
  private String doi;
  // 中文标题
  private String zhTitle;
  // 外文标题
  private String enTitle;
  // 成果类型
  private Integer pubType;
  // 作者
  private String authorNames;
  // 专利号
  private String patentNo;
  // 专利公开号
  private String patentOpenNo;
  // 文献抓取人员psn_id，批量抓取为空
  private Long createPsn;
  // 抓取日期，批量抓取为导入时间
  private Date fetchDate;
  // 文献抓取人员ins_id，批量抓取为空
  private Long createIns;
  // 单位
  private String organization;
  // 标题hash
  private Long titleHash;
  // 组合hash
  private Long patentHash;
  // 组合hash
  private Long patentOpenHash;
  // 文献地址列表
  private List<CniprPubAddr> pubAddrs;

  @Id
  @Column(name = "PUB_ID")
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_CNIPR_PUBLICATION", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
  public Long getPubId() {
    return pubId;
  }

  @Column(name = "PUBYEAR")
  public Integer getPubYear() {
    return pubYear;
  }

  @Column(name = "DBID")
  public Integer getDbId() {
    return dbId;
  }

  @Column(name = "DOI")
  public String getDoi() {
    return doi;
  }

  @Column(name = "ZH_TITLE")
  public String getZhTitle() {
    return zhTitle;
  }

  @Column(name = "EN_TITLE")
  public String getEnTitle() {
    return enTitle;
  }

  @Column(name = "PUB_TYPE")
  public Integer getPubType() {
    return pubType;
  }

  @Column(name = "AUTHOR_NAMES")
  public String getAuthorNames() {
    return authorNames;
  }

  @Column(name = "PATENT_NO")
  public String getPatentNo() {
    return patentNo;
  }

  @Column(name = "CREATE_PSN")
  public Long getCreatePsn() {
    return createPsn;
  }

  @Column(name = "FETCH_DATE")
  public Date getFetchDate() {
    return fetchDate;
  }

  @Column(name = "CREATE_INS")
  public Long getCreateIns() {
    return createIns;
  }

  @Column(name = "PATENT_OPEN_NO")
  public String getPatentOpenNo() {
    return patentOpenNo;
  }

  public void setPatentOpenNo(String patentOpenNo) {
    this.patentOpenNo = patentOpenNo;
  }

  @Transient
  public String getOrganization() {
    return organization;
  }

  @Transient
  public Long getTitleHash() {
    return titleHash;
  }

  @Transient
  public Long getPatentHash() {
    return patentHash;
  }

  @Transient
  public List<CniprPubAddr> getPubAddrs() {
    return pubAddrs;
  }

  @Transient
  public Long getPatentOpenHash() {
    return patentOpenHash;
  }

  public void setPatentOpenHash(Long patentOpenHash) {
    this.patentOpenHash = patentOpenHash;
  }

  public void setOrganization(String organization) {
    this.organization = organization;
  }

  public void setTitleHash(Long titleHash) {
    this.titleHash = titleHash;
  }

  public void setPatentHash(Long patentHash) {
    this.patentHash = patentHash;
  }

  public void setPubAddrs(List<CniprPubAddr> pubAddrs) {
    this.pubAddrs = pubAddrs;
  }

  public void setCreateIns(Long createIns) {
    this.createIns = createIns;
  }

  public void setPubId(Long pubId) {
    this.pubId = pubId;
  }

  public void setPubYear(Integer pubYear) {
    this.pubYear = pubYear;
  }

  public void setDbId(Integer dbId) {
    this.dbId = dbId;
  }

  public void setDoi(String doi) {
    this.doi = doi;
  }

  public void setZhTitle(String zhTitle) {
    this.zhTitle = zhTitle;
  }

  public void setEnTitle(String enTitle) {
    this.enTitle = enTitle;
  }

  public void setPubType(Integer pubType) {
    this.pubType = pubType;
  }

  public void setAuthorNames(String authorNames) {
    this.authorNames = authorNames;
  }

  public void setPatentNo(String patentNo) {
    this.patentNo = patentNo;
  }

  public void setCreatePsn(Long createPsn) {
    this.createPsn = createPsn;
  }

  public void setFetchDate(Date fetchDate) {
    this.fetchDate = fetchDate;
  }

}
