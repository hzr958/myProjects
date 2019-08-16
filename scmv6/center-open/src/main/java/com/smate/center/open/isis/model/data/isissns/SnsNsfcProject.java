package com.smate.center.open.isis.model.data.isissns;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;


/**
 * The persistent class for the NSFC_PROJECT database table.
 * 
 * @author hp
 * @date 2015-10-27
 */
@Entity
@Table(name = "NSFC_PROJECT")
public class SnsNsfcProject implements Serializable {
  private static final long serialVersionUID = 1L;

  @Id
  @Column(name = "PRJ_ID")
  private long prjId;

  @Column(name = "APP_AMOINT")
  private BigDecimal appAmoint;
  @Column(name = "CKEYWORDS")
  private String ckeywords;

  @Column(name = "COMMENCE_DATE")
  private Date commenceDate;

  @Column(name = "COMPLETION_DATE")
  private Date completionDate;
  @Column(name = "CSUMMARY")
  private String csummary;
  @Column(name = "CTITLE")
  private String ctitle;

  @Column(name = "DIS_NO1")
  private String disNo1;

  @Column(name = "DIS_NO2")
  private String disNo2;
  @Column(name = "DIVNO")
  private String divno;
  @Column(name = "EKEYWORDS")
  private String ekeywords;
  @Column(name = "ESUMMARY")
  private String esummary;
  @Column(name = "ETITLE")
  private String etitle;

  @Column(name = "GRANT_DESCRIPTION")
  private String grantDescription;

  @Column(name = "GRANT_TYPE_ID")
  private String grantTypeId;

  @Column(name = "INS_ID")
  private Long insId;

  @Column(name = "INS_NAME")
  private String insName;

  @Column(name = "NSFC_PRJ_ID")
  private Long nsfcPrjId;

  @Column(name = "PI_PSN_ID")
  private Long piPsnId;
  @Column(name = "PNO")
  private String pno;

  @Column(name = "STAT_YEAR")
  private Long statYear;
  @Column(name = "STATUS")
  private Long status;

  @Column(name = "SUB_GRANT_TYPE_ID")
  private String subGrantTypeId;

  @Column(name = "depno")
  private String depno;
  @Column(name = "depname")
  private String depname;
  @Column(name = "granttypeid")
  private String granttypeid;
  @Column(name = "granttypename")
  private String granttypename;



  public long getPrjId() {
    return this.prjId;
  }

  public void setPrjId(long prjId) {
    this.prjId = prjId;
  }

  public BigDecimal getAppAmoint() {
    return this.appAmoint;
  }

  public void setAppAmoint(BigDecimal appAmoint) {
    this.appAmoint = appAmoint;
  }

  public String getCkeywords() {
    return this.ckeywords;
  }

  public void setCkeywords(String ckeywords) {
    this.ckeywords = ckeywords;
  }

  public Date getCommenceDate() {
    return this.commenceDate;
  }

  public void setCommenceDate(Date commenceDate) {
    this.commenceDate = commenceDate;
  }

  public Date getCompletionDate() {
    return this.completionDate;
  }

  public void setCompletionDate(Date completionDate) {
    this.completionDate = completionDate;
  }

  public String getCsummary() {
    return this.csummary;
  }

  public void setCsummary(String csummary) {
    this.csummary = csummary;
  }

  public String getCtitle() {
    return this.ctitle;
  }

  public void setCtitle(String ctitle) {
    this.ctitle = ctitle;
  }

  public String getDisNo1() {
    return this.disNo1;
  }

  public void setDisNo1(String disNo1) {
    this.disNo1 = disNo1;
  }

  public String getDisNo2() {
    return this.disNo2;
  }

  public void setDisNo2(String disNo2) {
    this.disNo2 = disNo2;
  }

  public String getDivno() {
    return this.divno;
  }

  public void setDivno(String divno) {
    this.divno = divno;
  }

  public String getEkeywords() {
    return this.ekeywords;
  }

  public void setEkeywords(String ekeywords) {
    this.ekeywords = ekeywords;
  }

  public String getEsummary() {
    return this.esummary;
  }

  public void setEsummary(String esummary) {
    this.esummary = esummary;
  }

  public String getEtitle() {
    return this.etitle;
  }

  public void setEtitle(String etitle) {
    this.etitle = etitle;
  }

  public String getGrantDescription() {
    return this.grantDescription;
  }

  public void setGrantDescription(String grantDescription) {
    this.grantDescription = grantDescription;
  }

  public String getGrantTypeId() {
    return this.grantTypeId;
  }

  public void setGrantTypeId(String grantTypeId) {
    this.grantTypeId = grantTypeId;
  }

  public Long getInsId() {
    return insId;
  }

  public void setInsId(Long insId) {
    this.insId = insId;
  }

  public String getInsName() {
    return insName;
  }

  public void setInsName(String insName) {
    this.insName = insName;
  }

  public Long getNsfcPrjId() {
    return nsfcPrjId;
  }

  public void setNsfcPrjId(Long nsfcPrjId) {
    this.nsfcPrjId = nsfcPrjId;
  }

  public Long getPiPsnId() {
    return piPsnId;
  }

  public void setPiPsnId(Long piPsnId) {
    this.piPsnId = piPsnId;
  }

  public String getPno() {
    return pno;
  }

  public void setPno(String pno) {
    this.pno = pno;
  }

  public Long getStatYear() {
    return statYear;
  }

  public void setStatYear(Long statYear) {
    this.statYear = statYear;
  }

  public Long getStatus() {
    return status;
  }

  public void setStatus(Long status) {
    this.status = status;
  }

  public String getSubGrantTypeId() {
    return subGrantTypeId;
  }

  public void setSubGrantTypeId(String subGrantTypeId) {
    this.subGrantTypeId = subGrantTypeId;
  }

  public String getDepno() {
    return depno;
  }

  public void setDepno(String depno) {
    this.depno = depno;
  }

  public String getDepname() {
    return depname;
  }

  public void setDepname(String depname) {
    this.depname = depname;
  }

  public String getGranttypeid() {
    return granttypeid;
  }

  public void setGranttypeid(String granttypeid) {
    this.granttypeid = granttypeid;
  }

  public String getGranttypename() {
    return granttypename;
  }

  public void setGranttypename(String granttypename) {
    this.granttypename = granttypename;
  }



}
