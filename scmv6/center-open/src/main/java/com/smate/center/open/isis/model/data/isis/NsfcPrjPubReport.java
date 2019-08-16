package com.smate.center.open.isis.model.data.isis;

import java.io.Serializable;

import javax.persistence.*;
import java.util.Date;


/**
 * The persistent class for the NSFC_PRJ_PUB_REPORT database table.
 * 
 */
@Entity
@Table(name = "NSFC_PRJ_PUB_REPORT")
public class NsfcPrjPubReport implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = 7625485184040670233L;

  @Column(name = "ROWID", insertable = false, updatable = false)
  private String rowid;

  @Column(name = "DELIVER_DATE")
  private Date deliverDate;


  @Column(name = "CREATE_DATE")
  private Date createDate;
  @Column(name = "EI")
  private Long ei;
  @Column(name = "GRADE")
  private Long grade;

  @Column(name = "INS_ID")
  private Long insId;

  @Column(name = "INS_NAME")
  private String insName;

  @Column(name = "IS_TAG")
  private Long isTag;
  @Column(name = "ISTP")
  private Long istp;
  @Column(name = "JID")
  private Long jid;

  @EmbeddedId
  private NsfcPrjPubReportPK nsfcPrjPubReportPK;
  @Column(name = "HXJ")
  private Long hxj;


  @Column(name = "PUB_TYPE")
  private Long pubType;

  @Column(name = "ROMEO_COLOUR")
  private String romeoColour;



  @Column(name = "RPT_TYPE")
  private Long rptType;

  @Column(name = "RPT_YEAR")
  private Long rptYear;
  @Column(name = "SCI")
  private Long sci;
  @Column(name = "SSCI")
  private Long ssci;
  @Column(name = "STATUS")
  private Long status;

  @Column(name = "NSFC_PRJ_ID")
  private Long nsfcPrjId;

  @Column(name = "NSFC_RPT_ID")
  private Long nsfcRptId;


  @Column(name = "UPDATE_DATE")
  private Date updateDate;

  @Column(name = "tag_error")
  private int tagError;
  @Column(name = "sci_ssci_error")
  private int sciSsciError;
  @Column(name = "may_sci_ssci")
  private int maySciSsci;
  @Column(name = "is_error")
  private int isError;
  @Column(name = "source_db_id")
  private Integer sourceDbId;
  @Column(name = "fundinfo")
  private String fundinfo;
  @Column(name = "PRJ_EXTERNAL_NO")
  private String prjExternalNo;

  @Column(name = "PUB_TITLE")
  private String pubTitle;

  public NsfcPrjPubReport() {
    // default
    tagError = 0;
    sciSsciError = 0;
    maySciSsci = 0;
    isError = 0;
    hxj = 0L;
  }

  public Long getHxj() {
    return hxj;
  }

  public void setHxj(Long hxj) {
    this.hxj = hxj;
  }

  public Date getCreateDate() {
    return this.createDate;
  }

  public void setCreateDate(Date createDate) {
    this.createDate = createDate;
  }

  public Long getEi() {
    return this.ei;
  }

  public void setEi(Long ei) {
    this.ei = ei;
  }

  public Long getGrade() {
    return this.grade;
  }

  public void setGrade(Long grade) {
    this.grade = grade;
  }

  public Long getInsId() {
    return this.insId;
  }

  public void setInsId(Long insId) {
    this.insId = insId;
  }

  public String getInsName() {
    return this.insName;
  }

  public void setInsName(String insName) {
    this.insName = insName;
  }

  public Long getIsTag() {
    return this.isTag;
  }

  public void setIsTag(Long isTag) {
    this.isTag = isTag;
  }

  public Long getIstp() {
    return this.istp;
  }

  public void setIstp(Long istp) {
    this.istp = istp;
  }

  public Long getJid() {
    return this.jid;
  }

  public void setJid(Long jid) {
    this.jid = jid;
  }



  public Long getPubType() {
    return this.pubType;
  }

  public void setPubType(Long pubType) {
    this.pubType = pubType;
  }

  public String getRomeoColour() {
    return this.romeoColour;
  }

  public void setRomeoColour(String romeoColour) {
    this.romeoColour = romeoColour;
  }


  public Long getRptType() {
    return this.rptType;
  }

  public void setRptType(Long rptType) {
    this.rptType = rptType;
  }

  public Long getRptYear() {
    return this.rptYear;
  }

  public void setRptYear(Long rptYear) {
    this.rptYear = rptYear;
  }

  public Long getSci() {
    return this.sci;
  }

  public void setSci(Long sci) {
    this.sci = sci;
  }

  public Long getSsci() {
    return this.ssci;
  }

  public void setSsci(Long ssci) {
    this.ssci = ssci;
  }

  public Long getStatus() {
    return this.status;
  }

  public void setStatus(Long status) {
    this.status = status;
  }

  public Date getUpdateDate() {
    return this.updateDate;
  }

  public void setUpdateDate(Date updateDate) {
    this.updateDate = updateDate;
  }

  public NsfcPrjPubReportPK getNsfcPrjPubReportPK() {
    return nsfcPrjPubReportPK;
  }

  public void setNsfcPrjPubReportPK(NsfcPrjPubReportPK nsfcPrjPubReportPK) {
    this.nsfcPrjPubReportPK = nsfcPrjPubReportPK;
  }

  public Date getDeliverDate() {
    return deliverDate;
  }

  public void setDeliverDate(Date deliverDate) {
    this.deliverDate = deliverDate;
  }

  public Long getNsfcPrjId() {
    return nsfcPrjId;
  }

  public void setNsfcPrjId(Long nsfcPrjId) {
    this.nsfcPrjId = nsfcPrjId;
  }

  public Long getNsfcRptId() {
    return nsfcRptId;
  }

  public void setNsfcRptId(Long nsfcRptId) {
    this.nsfcRptId = nsfcRptId;
  }

  public int getTagError() {
    return tagError;
  }

  public void setTagError(int tagError) {
    this.tagError = tagError;
  }

  public int getSciSsciError() {
    return sciSsciError;
  }

  public void setSciSsciError(int sciSsciError) {
    this.sciSsciError = sciSsciError;
  }

  public int getMaySciSsci() {
    return maySciSsci;
  }

  public void setMaySciSsci(int maySciSsci) {
    this.maySciSsci = maySciSsci;
  }

  public int getIsError() {
    return isError;
  }

  public void setIsError(int isError) {
    this.isError = isError;
  }

  public Integer getSourceDbId() {
    return sourceDbId;
  }

  public void setSourceDbId(Integer sourceDbId) {
    this.sourceDbId = sourceDbId;
  }

  public String getFundinfo() {
    return fundinfo;
  }

  public void setFundinfo(String fundinfo) {
    this.fundinfo = fundinfo;
  }

  public String getPrjExternalNo() {
    return prjExternalNo;
  }

  public void setPrjExternalNo(String prjExternalNo) {
    this.prjExternalNo = prjExternalNo;
  }

  public String getPubTitle() {
    return pubTitle;
  }

  public void setPubTitle(String pubTitle) {
    this.pubTitle = pubTitle;
  }



}
