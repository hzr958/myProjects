package com.smate.center.open.isis.model.data.isissns;

import java.io.Serializable;

import javax.persistence.*;


/**
 * The persistent class for the NSFC_PRJ_RPT_PUB database table.
 * 
 * @author hp
 * @date 2015-10-27
 */
@Entity
@Table(name = "NSFC_PRJ_RPT_PUB")
public class SnsNsfcPrjRptPub implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = -4550671239143109491L;
  @Column(name = "ROWID", insertable = false, updatable = false)
  private String rowid;
  @EmbeddedId
  private SnsNsfcPrjRptPubPK nsfcPrjRptPubPK;
  @Column(name = "AUTHORS")
  private String authors;


  @Column(name = "IMPACT_FACTORS")
  private String impactFactors;

  @Column(name = "IS_OPEN")
  private Long isOpen;

  @Column(name = "IS_TAG")
  private Long isTag;

  @Column(name = "LIST_INFO")
  private String listInfo;

  @Column(name = "LIST_INFO_SOURCE")
  private String listInfoSource;
  @Column(name = "MATCHED")
  private Long matched;

  @Column(name = "NEED_SYC")
  private String needSyc;

  @Column(name = "NODE_ID")
  private Long nodeId;

  @Column(name = "PUB_OWNER_PSNID")
  private Long pubOwnerPsnid;

  @Column(name = "PUB_TYPE")
  private Long pubType;

  @Column(name = "PUB_YEAR")
  private Long pubYear;

  @Column(name = "SEQ_NO")
  private Long seqNo;
  @Column(name = "SOURCE")
  private String source;
  @Column(name = "TITLE")
  private String title;

  @Column(name = "VERSION")
  private Integer version;



  public SnsNsfcPrjRptPubPK getNsfcPrjRptPubPK() {
    return nsfcPrjRptPubPK;
  }



  public void setNsfcPrjRptPubPK(SnsNsfcPrjRptPubPK nsfcPrjRptPubPK) {
    this.nsfcPrjRptPubPK = nsfcPrjRptPubPK;
  }



  public String getAuthors() {
    return this.authors;
  }

  public void setAuthors(String authors) {
    this.authors = authors;
  }



  public String getImpactFactors() {
    return this.impactFactors;
  }

  public void setImpactFactors(String impactFactors) {
    this.impactFactors = impactFactors;
  }


  public Long getIsOpen() {
    return isOpen;
  }

  public void setIsOpen(Long isOpen) {
    this.isOpen = isOpen;
  }

  public Long getIsTag() {
    return isTag;
  }

  public void setIsTag(Long isTag) {
    this.isTag = isTag;
  }

  public Long getMatched() {
    return matched;
  }

  public void setMatched(Long matched) {
    this.matched = matched;
  }

  public Long getNodeId() {
    return nodeId;
  }

  public void setNodeId(Long nodeId) {
    this.nodeId = nodeId;
  }

  public Long getPubOwnerPsnid() {
    return pubOwnerPsnid;
  }

  public void setPubOwnerPsnid(Long pubOwnerPsnid) {
    this.pubOwnerPsnid = pubOwnerPsnid;
  }

  public Long getPubType() {
    return pubType;
  }

  public void setPubType(Long pubType) {
    this.pubType = pubType;
  }

  public Long getPubYear() {
    return pubYear;
  }

  public void setPubYear(Long pubYear) {
    this.pubYear = pubYear;
  }

  public Long getSeqNo() {
    return seqNo;
  }

  public void setSeqNo(Long seqNo) {
    this.seqNo = seqNo;
  }

  public String getSource() {
    return source;
  }

  public void setSource(String source) {
    this.source = source;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public Integer getVersion() {
    return version;
  }

  public void setVersion(Integer version) {
    this.version = version;
  }

  public void setNeedSyc(String needSyc) {
    this.needSyc = needSyc;
  }

  public String getListInfo() {
    return this.listInfo;
  }

  public void setListInfo(String listInfo) {
    this.listInfo = listInfo;
  }

  public String getListInfoSource() {
    return this.listInfoSource;
  }

  public void setListInfoSource(String listInfoSource) {
    this.listInfoSource = listInfoSource;
  }


  public String getNeedSyc() {
    return this.needSyc;
  }

}
