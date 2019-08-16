package com.smate.sie.center.open.model.dept;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "ISIS_PROJECT")
public class IsisProject implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = 89082416134563363L;

  @Id
  @Column(name = "ID")
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_IRIS_PROJECT", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
  private Long id;// 主键

  @Column(name = "PRJ_CODE")
  private Long prjCode;// 项目code

  @Column(name = "ZH_TITLE")
  private String zhTitle;// 项目名称

  @Column(name = "PROJECT_FROM")
  private String projectFrom;// 项目来源

  @Column(name = "GRANT_CODE")
  private Long grantCode;// 资助类别code

  @Column(name = "GRANT_NAME")
  private String grantName;// 资助类别name

  @Column(name = "START_DATE")
  private Date startDate;// 开始时间

  @Column(name = "END_DATE")
  private Date endDate;// 结束时间

  @Column(name = "STAT_YEAR")
  private Long statYear;// 立项年度

  @Column(name = "ORG_CODE")
  private Long orgCode;// 部门code

  @Column(name = "INS_NAME")
  private String insName;// 依托单位

  @Column(name = "APPROVE_NUMBER")
  private String approveNumber;// 项目批准号

  @Column(name = "TOTAL_AMT")
  private BigDecimal totalAmt;// 立项金额

  @Column(name = "PRJ_STATUS")
  private String prjStatus;// 项目状态

  @Column(name = "DIS_CODE1")
  private String disCode1;// 学科代码code1

  @Column(name = "DIS_NAME1")
  private String disName1;// 学科代码name1

  @Column(name = "DIS_CODE2")
  private String disCode2;// 学科代码code2

  @Column(name = "DIS_NAME2")
  private String disName2;// 学科代码name2

  @Column(name = "KEY_WORDS")
  private String keyWords;// 关键词

  @Column(name = "SUMMARY")
  private String summary;// 摘要

  @Column(name = "PSN_NAME")
  private String psnName;// 项目负责人

  @Column(name = "MEMBERS")
  private String members;// 项目参与人

  @Column(name = "INS_ID")
  private Long insId;// 单位id

  @Column(name = "UNIT_NAME")
  private String unitName;// 二级单位名称

  public String getUnitName() {
    return unitName;
  }

  public void setUnitName(String unitName) {
    this.unitName = unitName;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Long getPrjCode() {
    return prjCode;
  }

  public void setPrjCode(Long prjCode) {
    this.prjCode = prjCode;
  }

  public String getZhTitle() {
    return zhTitle;
  }

  public void setZhTitle(String zhTitle) {
    this.zhTitle = zhTitle;
  }

  public String getProjectFrom() {
    return projectFrom;
  }

  public void setProjectFrom(String projectFrom) {
    this.projectFrom = projectFrom;
  }

  public Long getGrantCode() {
    return grantCode;
  }

  public void setGrantCode(Long grantCode) {
    this.grantCode = grantCode;
  }

  public String getGrantName() {
    return grantName;
  }

  public void setGrantName(String grantName) {
    this.grantName = grantName;
  }

  public Date getStartDate() {
    return startDate;
  }

  public void setStartDate(Date startDate) {
    this.startDate = startDate;
  }

  public Date getEndDate() {
    return endDate;
  }

  public void setEndDate(Date endDate) {
    this.endDate = endDate;
  }

  public Long getStatYear() {
    return statYear;
  }

  public void setStatYear(Long statYear) {
    this.statYear = statYear;
  }

  public Long getOrgCode() {
    return orgCode;
  }

  public void setOrgCode(Long orgCode) {
    this.orgCode = orgCode;
  }

  public String getInsName() {
    return insName;
  }

  public void setInsName(String insName) {
    this.insName = insName;
  }

  public String getApproveNumber() {
    return approveNumber;
  }

  public void setApproveNumber(String approveNumber) {
    this.approveNumber = approveNumber;
  }

  public BigDecimal getTotalAmt() {
    return totalAmt;
  }

  public void setTotalAmt(BigDecimal totalAmt) {
    this.totalAmt = totalAmt;
  }

  public String getPrjStatus() {
    return prjStatus;
  }

  public void setPrjStatus(String prjStatus) {
    this.prjStatus = prjStatus;
  }

  public String getDisCode1() {
    return disCode1;
  }

  public void setDisCode1(String disCode1) {
    this.disCode1 = disCode1;
  }

  public String getDisName1() {
    return disName1;
  }

  public void setDisName1(String disName1) {
    this.disName1 = disName1;
  }

  public String getDisCode2() {
    return disCode2;
  }

  public void setDisCode2(String disCode2) {
    this.disCode2 = disCode2;
  }

  public String getDisName2() {
    return disName2;
  }

  public void setDisName2(String disName2) {
    this.disName2 = disName2;
  }

  public String getKeyWords() {
    return keyWords;
  }

  public void setKeyWords(String keyWords) {
    this.keyWords = keyWords;
  }

  public String getSummary() {
    return summary;
  }

  public void setSummary(String summary) {
    this.summary = summary;
  }

  public String getPsnName() {
    return psnName;
  }

  public void setPsnName(String psnName) {
    this.psnName = psnName;
  }

  public String getMembers() {
    return members;
  }

  public void setMembers(String members) {
    this.members = members;
  }

  public Long getInsId() {
    return insId;
  }

  public void setInsId(Long insId) {
    this.insId = insId;
  }

}
