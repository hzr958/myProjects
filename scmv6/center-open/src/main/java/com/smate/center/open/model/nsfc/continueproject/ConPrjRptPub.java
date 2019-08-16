package com.smate.center.open.model.nsfc.continueproject;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * 延续报告成果信息
 * 
 * @author tsz
 * 
 */
@Entity
@Table(name = "NSFC_CON_PRJ_RPT_PUB")
public class ConPrjRptPub implements Serializable {


  /**
   * 
   */
  private static final long serialVersionUID = -1708483172613260522L;

  // 主键
  @Id
  @Column(name = "ID")
  @SequenceGenerator(sequenceName = "SEQ_NSFC_CON_PRJ_RPT_PUB", name = "SEQ_STORE", allocationSize = 1)
  @GeneratedValue(generator = "SEQ_STORE", strategy = GenerationType.SEQUENCE)
  private Long id;

  @Column(name = "NSFC_RPT_ID")
  private Long nsfcRptId;

  @Column(name = "PUB_ID")
  private Long pubId;

  @Column(name = "SEQ_NO")
  private Integer seqNo;

  @Column(name = "TITLE")
  private String title;

  @Column(name = "AUTHORS")
  private String authors;

  @Column(name = "PUB_TYPE")
  private Integer pubType;

  @Column(name = "PUB_YEAR")
  private Integer pubYear;

  @Column(name = "PUB_MOTH")
  private Integer pubMoth;

  @Column(name = "PUB_DAY")
  private Integer pubDay;

  @Column(name = "LIST_INFO")
  private String listInfo;

  @Column(name = "LIST_INFO_SOURCE")
  private String listInfoSource;

  @Column(name = "SOURCE")
  private String source;

  @Column(name = "PUB_OWNER_PSN_ID")
  private Long pubOwnerPsnId;

  @Column(name = "IMPACT_FACTORS")
  private String impactFactors;

  @Column(name = "CITED_TIMES")
  private Integer citedTimes;

  @Column(name = "IS_TAG")
  private Integer isTag;

  @Transient
  private String pubTypeName;

  public ConPrjRptPub() {
    super();
  }

  public ConPrjRptPub(Long nsfcRptId, Long pubId, Integer seqNo, String title, String authors, Integer pubType,
      Integer pubYear, Integer pubMoth, Integer pubDay, String listInfo, String listInfoSource, String source,
      Long pubOwnerPsnId, String impactFactors, Integer citedTimes, Integer isTag, String pubTypeName) {
    super();
    this.nsfcRptId = nsfcRptId;
    this.pubId = pubId;
    this.seqNo = seqNo;
    this.title = title;
    this.authors = authors;
    this.pubType = pubType;
    this.pubYear = pubYear;
    this.pubMoth = pubMoth;
    this.pubDay = pubDay;
    this.listInfo = listInfo;
    this.listInfoSource = listInfoSource;
    this.source = source;
    this.pubOwnerPsnId = pubOwnerPsnId;
    this.impactFactors = impactFactors;
    this.citedTimes = citedTimes;
    this.isTag = isTag;
    this.pubTypeName = pubTypeName;
  }

  public Long getId() {
    return id;
  }

  public Long getPubId() {
    return pubId;
  }

  public Integer getSeqNo() {
    return seqNo;
  }

  public String getTitle() {
    return title;
  }

  public String getAuthors() {
    return authors;
  }

  public Integer getPubType() {
    return pubType;
  }

  public Integer getPubYear() {
    return pubYear;
  }

  public Integer getPubMoth() {
    return pubMoth;
  }

  public Integer getPubDay() {
    return pubDay;
  }

  public String getListInfo() {
    return listInfo;
  }

  public String getListInfoSource() {
    return listInfoSource;
  }

  public String getSource() {
    return source;
  }

  public Long getPubOwnerPsnId() {
    return pubOwnerPsnId;
  }

  public String getImpactFactors() {
    return impactFactors;
  }

  public Integer getCitedTimes() {
    return citedTimes;
  }

  public Integer getIsTag() {
    return isTag;
  }

  public String getPubTypeName() {
    return pubTypeName;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public void setPubId(Long pubId) {
    this.pubId = pubId;
  }

  public void setSeqNo(Integer seqNo) {
    this.seqNo = seqNo;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public void setAuthors(String authors) {
    this.authors = authors;
  }

  public void setPubType(Integer pubType) {
    this.pubType = pubType;
  }

  public void setPubYear(Integer pubYear) {
    this.pubYear = pubYear;
  }

  public void setPubMoth(Integer pubMoth) {
    this.pubMoth = pubMoth;
  }

  public void setPubDay(Integer pubDay) {
    this.pubDay = pubDay;
  }

  public void setListInfo(String listInfo) {
    this.listInfo = listInfo;
  }

  public void setListInfoSource(String listInfoSource) {
    this.listInfoSource = listInfoSource;
  }

  public void setSource(String source) {
    this.source = source;
  }

  public void setPubOwnerPsnId(Long pubOwnerPsnId) {
    this.pubOwnerPsnId = pubOwnerPsnId;
  }

  public void setImpactFactors(String impactFactors) {
    this.impactFactors = impactFactors;
  }

  public void setCitedTimes(Integer citedTimes) {
    this.citedTimes = citedTimes;
  }

  public void setIsTag(Integer isTag) {
    this.isTag = isTag;
  }

  public void setPubTypeName(String pubTypeName) {
    this.pubTypeName = pubTypeName;
  }

  public Long getNsfcRptId() {
    return nsfcRptId;
  }

  public void setNsfcRptId(Long nsfcRptId) {
    this.nsfcRptId = nsfcRptId;
  }

}
