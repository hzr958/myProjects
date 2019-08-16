package com.smate.center.batch.model.sns.pub;

import java.io.Serializable;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;


/**
 * NsfcPrjRptPub entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "NSFC_PRJ_RPT_PUB")
public class NsfcPrjRptPub implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = -6277083747458278168L;
  // Fields

  private NsfcPrjRptPubId id;
  private Integer version = null;
  private Integer pubType = null;
  private Integer pubYear = null;
  private String authors;
  private String title;
  private String source;
  // 给基金委的source，跟SCM的不一样
  private String nsfcSource;
  private Integer isTag;
  private Integer isOpen;
  private String needSyc = "0";
  private String listInfo;
  private Integer seqNo;
  private String pubTypeName;
  private Integer nodeId;
  private Long pubOwnerPsnId;
  private PublicationCw pubCw;
  // 是否匹配到V3成果（v2.6数据同步）
  private Integer matched;
  private String listInfoSource;
  // 影响因子
  private String impactFactors;
  // 引用次数
  private Integer citedTimes;

  /** default constructor. */
  public NsfcPrjRptPub() {}

  /** minimal constructor. */
  public NsfcPrjRptPub(NsfcPrjRptPubId id) {
    this.id = id;
  }

  @Column(name = "SEQ_NO")
  public Integer getSeqNo() {
    return seqNo;
  }

  public void setSeqNo(Integer seqNo) {
    this.seqNo = seqNo;
  }

  // Constructors
  @Column(name = "LIST_INFO", precision = 50)
  public String getListInfo() {
    return listInfo;
  }

  public void setListInfo(String listInfo) {
    this.listInfo = listInfo;
  }

  // Property accessors
  @EmbeddedId
  @AttributeOverrides({
      @AttributeOverride(name = "rptId",
          column = @Column(name = "RPT_ID", nullable = false, precision = 22, scale = 0)),
      @AttributeOverride(name = "pubId",
          column = @Column(name = "PUB_ID", nullable = false, precision = 22, scale = 0))})
  public NsfcPrjRptPubId getId() {
    return this.id;
  }

  public void setId(NsfcPrjRptPubId id) {
    this.id = id;
  }

  @Transient
  public String getPubTypeName() {
    return pubTypeName;
  }

  public void setPubTypeName(String pubTypeName) {
    this.pubTypeName = pubTypeName;
  }

  @Column(name = "VERSION")
  public Integer getVersion() {
    return this.version;
  }

  public void setVersion(Integer version) {
    this.version = version;
  }

  @Column(name = "PUB_TYPE")
  public Integer getPubType() {
    return this.pubType;
  }

  public void setPubType(Integer pubType) {
    this.pubType = pubType;
  }

  @Column(name = "PUB_OWNER_PSNID")
  public Long getPubOwnerPsnId() {
    return pubOwnerPsnId;
  }

  public void setPubOwnerPsnId(Long pubOwnerPsnId) {
    this.pubOwnerPsnId = pubOwnerPsnId;
  }

  @Column(name = "PUB_YEAR", precision = 4)
  public Integer getPubYear() {
    return this.pubYear;
  }

  public void setPubYear(Integer pubYear) {
    this.pubYear = pubYear;
  }

  @Column(name = "AUTHORS", length = 200)
  public String getAuthors() {
    return this.authors;
  }

  public void setAuthors(String authors) {
    this.authors = authors;
  }

  @Column(name = "TITLE", length = 200)
  public String getTitle() {
    return this.title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  @Column(name = "SOURCE", length = 500)
  public String getSource() {
    return this.source;
  }

  public void setSource(String source) {
    this.source = source;
  }

  @Transient
  public String getNsfcSource() {
    return nsfcSource;
  }

  public void setNsfcSource(String nsfcSource) {
    this.nsfcSource = nsfcSource;
  }

  @Column(name = "IS_TAG")
  public Integer getIsTag() {
    return this.isTag;
  }

  public void setIsTag(Integer isTag) {
    this.isTag = isTag;
  }

  @Column(name = "IS_OPEN")
  public Integer getIsOpen() {
    return this.isOpen;
  }

  public void setIsOpen(Integer isOpen) {
    this.isOpen = isOpen;
  }

  @Column(name = "NEED_SYC", length = 1)
  public String getNeedSyc() {
    return this.needSyc;
  }

  @Column(name = "NODE_ID")
  public Integer getNodeId() {
    return nodeId;
  }

  public void setNodeId(Integer nodeId) {
    this.nodeId = nodeId;
  }

  public void setNeedSyc(String needSyc) {
    this.needSyc = needSyc;
  }

  @Transient
  public PublicationCw getPubCw() {
    return pubCw;
  }

  public void setPubCw(PublicationCw pubCw) {
    this.pubCw = pubCw;
  }

  @Column(name = "MATCHED")
  public Integer getMatched() {
    return matched;
  }

  public void setMatched(Integer matched) {
    this.matched = matched;
  }

  @Column(name = "LIST_INFO_SOURCE")
  public String getListInfoSource() {
    return listInfoSource;
  }

  public void setListInfoSource(String listInfoSource) {
    this.listInfoSource = listInfoSource;
  }

  @Column(name = "IMPACT_FACTORS")
  public String getImpactFactors() {
    return impactFactors;
  }

  public void setImpactFactors(String impactFactors) {
    this.impactFactors = impactFactors;
  }

  @Column(name = "CITED_TIMES")
  public Integer getCitedTimes() {
    return citedTimes;
  }

  public void setCitedTimes(Integer citedTimes) {
    this.citedTimes = citedTimes;
  }

}
