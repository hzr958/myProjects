package com.smate.center.batch.model.sns.pub;

import java.io.Serializable;
import java.util.Date;

public class ReschProjectReportModel implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = 821415942144729964L;
  private Long rptId;
  private String encryptRptId;
  private Long nsfcRptId;
  private Integer rptYear;
  private Integer pubYear;
  private Integer status;
  private Date deliverDate;
  private Long prjId;
  private String encryptPrjId;
  private Long nsfcPrjId;
  private String pno;
  private String ctitle;
  private Long piPsnId;
  private Integer rptType;
  private Long insId;
  private Integer statYear;
  private String disNo1;
  private String cname;
  private String insCname;

  public String getEncryptRptId() {
    return encryptRptId;
  }

  public void setEncryptRptId(String encryptRptId) {
    this.encryptRptId = encryptRptId;
  }

  public String getEncryptPrjId() {
    return encryptPrjId;
  }

  public void setEncryptPrjId(String encryptPrjId) {
    this.encryptPrjId = encryptPrjId;
  }

  public Integer getRptType() {
    return rptType;
  }

  public void setRptType(Integer rptType) {
    this.rptType = rptType;
  }

  public Integer getPubYear() {
    return pubYear;
  }

  public void setPubYear(Integer pubYear) {
    this.pubYear = pubYear;
  }

  public String getInsCname() {
    return insCname;
  }

  public void setInsCname(String insCname) {
    this.insCname = insCname;
  }

  public Long getRptId() {
    return rptId;
  }

  public String getCname() {
    return cname;
  }

  public void setCname(String cname) {
    this.cname = cname;
  }

  public void setRptId(Long rptId) {
    this.rptId = rptId;
  }

  public Long getPrjId() {
    return prjId;
  }

  public void setPrjId(Long prjId) {
    this.prjId = prjId;
  }

  public Long getNsfcPrjId() {
    return nsfcPrjId;
  }

  public void setNsfcPrjId(Long nsfcPrjId) {
    this.nsfcPrjId = nsfcPrjId;
  }

  public String getPno() {
    return pno;
  }

  public void setPno(String pno) {
    this.pno = pno;
  }

  public String getCtitle() {
    return ctitle;
  }

  public void setCtitle(String ctitle) {
    this.ctitle = ctitle;
  }

  public Long getPiPsnId() {
    return piPsnId;
  }

  public void setPiPsnId(Long piPsnId) {
    this.piPsnId = piPsnId;
  }

  public Long getInsId() {
    return insId;
  }

  public void setInsId(Long insId) {
    this.insId = insId;
  }

  public Integer getStatYear() {
    return statYear;
  }

  public void setStatYear(Integer statYear) {
    this.statYear = statYear;
  }

  public String getDisNo1() {
    return disNo1;
  }

  public void setDisNo1(String disNo1) {
    this.disNo1 = disNo1;
  }

  public Long getNsfcRptId() {
    return nsfcRptId;
  }

  public void setNsfcRptId(Long nsfcRptId) {
    this.nsfcRptId = nsfcRptId;
  }

  public Integer getRptYear() {
    return rptYear;
  }

  public void setRptYear(Integer rptYear) {
    this.rptYear = rptYear;
  }

  public Integer getStatus() {
    return status;
  }

  public void setStatus(Integer status) {
    this.status = status;
  }

  public Date getDeliverDate() {
    return deliverDate;
  }

  public void setDeliverDate(Date deliverDate) {
    this.deliverDate = deliverDate;
  }
}
