package com.smate.center.batch.model.sns.pub;

import java.io.Serializable;

/**
 * 结题报告成果显示层Model.
 * 
 * @author LY
 * 
 */
public class ReschProjectReportPubModel implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = 8908265173919499835L;
  private Integer version;
  private Long prjId;
  // cite@write使用Isis中的项目编号 唯一.
  private Long nsfcPrjId;
  private Long pubType = null;
  private Integer pubYear = null;
  private String authors;
  private String title;
  private String source;
  private Integer isTag;
  private Integer isOpen;
  private Long rptId;
  private String encryptRptId;
  private String encryptPubId;
  private Long pubId;
  private String pubTypeName;
  private String listInfo = null;
  private Integer seqNo;
  private String citation;
  private String pubIds;
  private String jsonParams;
  private Long rptYear;
  private boolean ignore;
  private Integer defType;
  private Integer status;

  public Long getRptYear() {
    return rptYear;
  }

  public void setRptYear(Long rptYear) {
    this.rptYear = rptYear;
  }

  public Long getNsfcPrjId() {
    return nsfcPrjId;
  }

  public void setNsfcPrjId(Long nsfcPrjId) {
    this.nsfcPrjId = nsfcPrjId;
  }

  public Long getPrjId() {
    return prjId;
  }

  public void setPrjId(Long prjId) {
    this.prjId = prjId;
  }

  public String getJsonParams() {
    return jsonParams;
  }

  public void setJsonParams(String jsonParams) {
    this.jsonParams = jsonParams;
  }

  public String getPubIds() {
    return pubIds;
  }

  public void setPubIds(String pubIds) {
    this.pubIds = pubIds;
  }

  public String getCitation() {
    return citation;
  }

  public void setCitation(String citation) {
    this.citation = citation;
  }

  public Integer getSeqNo() {
    return seqNo;
  }

  public void setSeqNo(Integer seqNo) {
    this.seqNo = seqNo;
  }

  public String getListInfo() {
    return listInfo;
  }

  public void setListInfo(String listInfo) {
    this.listInfo = listInfo;
  }

  public Integer getVersion() {
    return version;
  }

  public void setVersion(Integer version) {
    this.version = version;
  }

  public Long getPubType() {
    return pubType;
  }

  public void setPubType(Long pubType) {
    this.pubType = pubType;
  }

  public Integer getPubYear() {
    return pubYear;
  }

  public void setPubYear(Integer pubYear) {
    this.pubYear = pubYear;
  }

  public String getAuthors() {
    return authors;
  }

  public void setAuthors(String authors) {
    this.authors = authors;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getSource() {
    return source;
  }

  public void setSource(String source) {
    this.source = source;
  }

  public Integer getIsTag() {
    return isTag;
  }

  public void setIsTag(Integer isTag) {
    this.isTag = isTag;
  }

  public Integer getIsOpen() {
    return isOpen;
  }

  public void setIsOpen(Integer isOpen) {
    this.isOpen = isOpen;
  }

  public Long getRptId() {
    return rptId;
  }

  public void setRptId(Long rptId) {
    this.rptId = rptId;
  }

  public Long getPubId() {
    return pubId;
  }

  public void setPubId(Long pubId) {
    this.pubId = pubId;
  }

  public String getEncryptRptId() {
    return encryptRptId;
  }

  public void setEncryptRptId(String encryptRptId) {
    this.encryptRptId = encryptRptId;
  }

  public String getEncryptPubId() {
    return encryptPubId;
  }

  public void setEncryptPubId(String encryptPubId) {
    this.encryptPubId = encryptPubId;
  }

  public String getPubTypeName() {
    return pubTypeName;
  }

  public void setPubTypeName(String pubTypeName) {
    this.pubTypeName = pubTypeName;
  }

  /**
   * @return the ignore
   */
  public boolean isIgnore() {
    return ignore;
  }

  /**
   * @param ignore the ignore to set
   */
  public void setIgnore(boolean ignore) {
    this.ignore = ignore;
  }

  public Integer getDefType() {
    return defType;
  }

  public void setDefType(Integer defType) {
    this.defType = defType;
  }

  public Integer getStatus() {
    return status;
  }

  public void setStatus(Integer status) {
    this.status = status;
  }

}
