package com.smate.center.batch.model.sns.pub;

import java.io.Serializable;

/**
 * 
 * @author OYH
 * 
 */
public class NsfcReschPrjRptPubSync implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = 6695060895594029337L;
  private Long pubId;
  private Integer version = null;
  private Integer pubType = null;
  private Integer pubYear = null;
  private String authors;
  private String title;
  private String source;
  private Integer isTag;
  private Integer isOpen;
  private String needSyc;
  private String listInfo;
  private Integer seqNo;
  private String pubTypeName;
  private Integer nodeId;
  private String visitUrl;

  public Long getPubId() {
    return pubId;
  }

  public Integer getVersion() {
    return version;
  }

  public Integer getPubType() {
    return pubType;
  }

  public Integer getPubYear() {
    return pubYear;
  }

  public String getAuthors() {
    return authors;
  }

  public String getTitle() {
    return title;
  }

  public String getSource() {
    return source;
  }

  public Integer getIsTag() {
    return isTag;
  }

  public Integer getIsOpen() {
    return isOpen;
  }

  public String getNeedSyc() {
    return needSyc;
  }

  public String getListInfo() {
    return listInfo;
  }

  public Integer getSeqNo() {
    return seqNo;
  }

  public String getPubTypeName() {
    return pubTypeName;
  }

  public Integer getNodeId() {
    return nodeId;
  }

  public String getVisitUrl() {
    return visitUrl;
  }

  public void setPubId(Long pubId) {
    this.pubId = pubId;
  }

  public void setVersion(Integer version) {
    this.version = version;
  }

  public void setPubType(Integer pubType) {
    this.pubType = pubType;
  }

  public void setPubYear(Integer pubYear) {
    this.pubYear = pubYear;
  }

  public void setAuthors(String authors) {
    this.authors = authors;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public void setSource(String source) {
    this.source = source;
  }

  public void setIsTag(Integer isTag) {
    this.isTag = isTag;
  }

  public void setIsOpen(Integer isOpen) {
    this.isOpen = isOpen;
  }

  public void setNeedSyc(String needSyc) {
    this.needSyc = needSyc;
  }

  public void setListInfo(String listInfo) {
    this.listInfo = listInfo;
  }

  public void setSeqNo(Integer seqNo) {
    this.seqNo = seqNo;
  }

  public void setPubTypeName(String pubTypeName) {
    this.pubTypeName = pubTypeName;
  }

  public void setNodeId(Integer nodeId) {
    this.nodeId = nodeId;
  }

  public void setVisitUrl(String visitUrl) {
    this.visitUrl = visitUrl;
  }

}
