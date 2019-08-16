package com.smate.center.batch.model.sns.pub;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;


/**
 * NsfcProject entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "NSFC_RESCH_PROJECT")
public class NsfcReschProject implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = 3024908762834654956L;
  private Long prjId;
  private Long nsfcPrjId;
  private String pno;
  private String ctitle;
  private String etitle;
  private String grantTypeId;
  private String subGrantTypeId;
  private String grantDescription;
  private Long piPsnId;
  private Long insId;
  private Integer status;
  private Integer statYear;
  private String disNo1;
  private String disNo2;
  private Long appAmoint;
  private Date commenceDate;
  private Date completionDate;
  private String csummary;
  private String esummary;
  private String ckeywords;
  private String ekeywords;
  private String insName;
  private String divno;
  // 初始化报告状态 0：失败 1：成功
  private boolean initStatus;
  private Set<NsfcReschProjectReport> nsfcProjectReports = new HashSet<NsfcReschProjectReport>(0);

  // Constructors

  /** default constructor. */
  public NsfcReschProject() {}

  /** minimal constructor. */
  public NsfcReschProject(Long prjId) {
    this.prjId = prjId;
  }

  /** full constructor. */
  public NsfcReschProject(Long prjId, Long nsfcPrjId, String pno, String ctitle, String etitle, String grantTypeId,
      String subGrantTypeId, String grantDescription, Long piPsnId, Long insId, Integer status, Integer statYear,
      String disNo1, String disNo2, Long appAmoint, Date commenceDate, Date completionDate, String csummary,
      String esummary, String ckeywords, String ekeywords, Set<NsfcReschProjectReport> nsfcProjectReports) {
    this.prjId = prjId;
    this.nsfcPrjId = nsfcPrjId;
    this.pno = pno;
    this.ctitle = ctitle;
    this.etitle = etitle;
    this.grantTypeId = grantTypeId;
    this.subGrantTypeId = subGrantTypeId;
    this.grantDescription = grantDescription;
    this.piPsnId = piPsnId;
    this.insId = insId;
    this.status = status;
    this.statYear = statYear;
    this.disNo1 = disNo1;
    this.disNo2 = disNo2;
    this.appAmoint = appAmoint;
    this.commenceDate = commenceDate;
    this.completionDate = completionDate;
    this.csummary = csummary;
    this.esummary = esummary;
    this.ckeywords = ckeywords;
    this.ekeywords = ekeywords;
    this.nsfcProjectReports = nsfcProjectReports;
  }

  // Property accessors
  @Id
  @Column(name = "PRJ_ID", unique = true, nullable = false, precision = 22, scale = 0)
  public Long getPrjId() {
    return this.prjId;
  }

  public void setPrjId(Long prjId) {
    this.prjId = prjId;
  }

  @Column(name = "NSFC_PRJ_ID", precision = 22, unique = true)
  public Long getNsfcPrjId() {
    return this.nsfcPrjId;
  }

  public void setNsfcPrjId(Long nsfcPrjId) {
    this.nsfcPrjId = nsfcPrjId;
  }

  @Column(name = "PNO", length = 20)
  public String getPno() {
    return this.pno;
  }

  public void setPno(String pno) {
    this.pno = pno;
  }

  @Column(name = "CTITLE", length = 400)
  public String getCtitle() {
    return this.ctitle;
  }

  public void setCtitle(String ctitle) {
    this.ctitle = ctitle;
  }

  @Column(name = "ETITLE", length = 400)
  public String getEtitle() {
    return this.etitle;
  }

  public void setEtitle(String etitle) {
    this.etitle = etitle;
  }

  @Column(name = "GRANT_TYPE_ID", length = 10)
  public String getGrantTypeId() {
    return this.grantTypeId;
  }

  public void setGrantTypeId(String grantTypeId) {
    this.grantTypeId = grantTypeId;
  }

  @Column(name = "SUB_GRANT_TYPE_ID", length = 10)
  public String getSubGrantTypeId() {
    return this.subGrantTypeId;
  }

  public void setSubGrantTypeId(String subGrantTypeId) {
    this.subGrantTypeId = subGrantTypeId;
  }

  @Column(name = "GRANT_DESCRIPTION", length = 100)
  public String getGrantDescription() {
    return this.grantDescription;
  }

  public void setGrantDescription(String grantDescription) {
    this.grantDescription = grantDescription;
  }

  @Column(name = "PI_PSN_ID", precision = 22)
  public Long getPiPsnId() {
    return this.piPsnId;
  }

  public void setPiPsnId(Long piPsnId) {
    this.piPsnId = piPsnId;
  }

  @Column(name = "INS_ID", precision = 22)
  public Long getInsId() {
    return this.insId;
  }

  public void setInsId(Long insId) {
    this.insId = insId;
  }

  @Column(name = "STATUS", precision = 2)
  public Integer getStatus() {
    return this.status;
  }

  public void setStatus(Integer status) {
    this.status = status;
  }

  @Column(name = "STAT_YEAR", precision = 4)
  public Integer getStatYear() {
    return this.statYear;
  }

  public void setStatYear(Integer statYear) {
    this.statYear = statYear;
  }

  @Column(name = "DIS_NO1", length = 20)
  public String getDisNo1() {
    return this.disNo1;
  }

  public void setDisNo1(String disNo1) {
    this.disNo1 = disNo1;
  }

  @Column(name = "DIS_NO2", length = 20)
  public String getDisNo2() {
    return this.disNo2;
  }

  public void setDisNo2(String disNo2) {
    this.disNo2 = disNo2;
  }

  @Column(name = "APP_AMOINT", precision = 22)
  public Long getAppAmoint() {
    return this.appAmoint;
  }

  public void setAppAmoint(Long appAmoint) {
    this.appAmoint = appAmoint;
  }

  @Column(name = "COMMENCE_DATE")
  public Date getCommenceDate() {
    return this.commenceDate;
  }

  public void setCommenceDate(Date commenceDate) {
    this.commenceDate = commenceDate;
  }

  @Column(name = "COMPLETION_DATE")
  public Date getCompletionDate() {
    return this.completionDate;
  }

  public void setCompletionDate(Date completionDate) {
    this.completionDate = completionDate;
  }

  @Column(name = "CSUMMARY", length = 2000)
  public String getCsummary() {
    return this.csummary;
  }

  public void setCsummary(String csummary) {
    this.csummary = csummary;
  }

  @Column(name = "ESUMMARY", length = 2000)
  public String getEsummary() {
    return this.esummary;
  }

  public void setEsummary(String esummary) {
    this.esummary = esummary;
  }

  @Column(name = "CKEYWORDS", length = 200)
  public String getCkeywords() {
    return this.ckeywords;
  }

  public void setCkeywords(String ckeywords) {
    this.ckeywords = ckeywords;
  }

  @Column(name = "EKEYWORDS", length = 200)
  public String getEkeywords() {
    return this.ekeywords;
  }

  public void setEkeywords(String ekeywords) {
    this.ekeywords = ekeywords;
  }

  @JsonIgnore
  @OneToMany(cascade = CascadeType.ALL)
  @JoinColumn(name = "PRJ_ID", insertable = true, updatable = false)
  public Set<NsfcReschProjectReport> getNsfcProjectReports() {
    return this.nsfcProjectReports;
  }

  public void setNsfcProjectReports(Set<NsfcReschProjectReport> nsfcProjectReports) {
    this.nsfcProjectReports = nsfcProjectReports;
  }

  @Column(name = "INS_NAME", length = 200)
  public String getInsName() {
    return insName;
  }

  public void setInsName(String insName) {
    this.insName = insName;
  }

  @Column(name = "DIVNO")
  public String getDivno() {
    return divno;
  }

  public void setDivno(String divno) {
    this.divno = divno;
  }

  /**
   * @return the initStatus
   */
  @Column(name = "INIT_STATUS")
  public boolean getInitStatus() {
    return initStatus;
  }

  /**
   * @param initStatus the initStatus to set
   */
  public void setInitStatus(boolean initStatus) {
    this.initStatus = initStatus;
  }

}
