package com.smate.center.task.model.snsbak.bdsp;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/*
 * 
 * 项目
 * 
 * 
 */
@Entity
@Table(name = "BDSP_PROJECT")
public class BdspProject implements Serializable {

  private static final long serialVersionUID = 1L;
  @Id
  @Column(name = "PRJ_CODE")
  private Long prjCode;
  @Column(name = "PRP_CODE")
  private Long prpCode;
  @Column(name = "ZH_TITLE")
  private String zhTitle;
  @Column(name = "EN_TITLE")
  private String enTitle;
  @Column(name = "PRP_NO")
  private String prpNo;
  @Column(name = "ORG_NAME")
  private String orgName;
  @Column(name = "PSN_NAME")
  private String psnName;
  @Column(name = "RECOMMEND_ORG_CODE")
  private Long recommendOrgCode;
  @Column(name = "ZH_GRANT_NAME")
  private String zhGrantName;
  @Column(name = "SUBJECT_CODE")
  private String subjectCode;
  @Column(name = "DOMAIN_CODE")
  private String domainCode;
  @Column(name = "STAT_YEAR")
  private String statYear;
  @Column(name = "REQ_AMT")
  private Double reqAmt;
  @Column(name = "ADMIN_ORG_CODE")
  private Long adminOrgCode;
  @Column(name = "GRANT_ORG_NAME")
  private String grantOrgName;
  @Column(name = "RESEARCH_VALUE")
  private String researchValue;
  @Column(name = "GRANT_CODE")
  private Long grantCode;
  @Column(name = "SUBJECT_ID")
  private String subjectId;
  @Column(name = "SUBJECT_NAME")
  private String subjectName;


  public BdspProject(Long prjCode, Long prpCode, String prpNo, String orgName, String statYear, Double reqAmt,
      Long grantCode, String subjectId) {
    super();
    this.prjCode = prjCode;
    this.prpCode = prpCode;
    this.prpNo = prpNo;
    this.orgName = orgName;
    this.statYear = statYear;
    this.reqAmt = reqAmt;
    this.grantCode = grantCode;
    this.subjectId = subjectId;
  }

  public BdspProject() {
    super();
  }

  public BdspProject(Long prjCode, Long prpCode, String zhTitle, String enTitle, String prpNo, String orgName,
      String psnName, Long recommendOrgCode, String zhGrantName, String subjectCode, String domainCode, String statYear,
      Double reqAmt, Long adminOrgCode, String grantOrgName, String researchValue, Long grantCode, String subjectId,
      String subjectName) {
    super();
    this.prjCode = prjCode;
    this.prpCode = prpCode;
    this.zhTitle = zhTitle;
    this.enTitle = enTitle;
    this.prpNo = prpNo;
    this.orgName = orgName;
    this.psnName = psnName;
    this.recommendOrgCode = recommendOrgCode;
    this.zhGrantName = zhGrantName;
    this.subjectCode = subjectCode;
    this.domainCode = domainCode;
    this.statYear = statYear;
    this.reqAmt = reqAmt;
    this.adminOrgCode = adminOrgCode;
    this.grantOrgName = grantOrgName;
    this.researchValue = researchValue;
    this.grantCode = grantCode;
    this.subjectId = subjectId;
    this.subjectName = subjectName;
  }

  public Long getPrjCode() {
    return prjCode;
  }

  public void setPrjCode(Long prjCode) {
    this.prjCode = prjCode;
  }

  public Long getPrpCode() {
    return prpCode;
  }

  public void setPrpCode(Long prpCode) {
    this.prpCode = prpCode;
  }

  public String getZhTitle() {
    return zhTitle;
  }

  public void setZhTitle(String zhTitle) {
    this.zhTitle = zhTitle;
  }

  public String getEnTitle() {
    return enTitle;
  }

  public void setEnTitle(String enTitle) {
    this.enTitle = enTitle;
  }

  public String getPrpNo() {
    return prpNo;
  }

  public void setPrpNo(String prpNo) {
    this.prpNo = prpNo;
  }

  public String getOrgName() {
    return orgName;
  }

  public void setOrgName(String orgName) {
    this.orgName = orgName;
  }

  public String getPsnName() {
    return psnName;
  }

  public void setPsnName(String psnName) {
    this.psnName = psnName;
  }

  public Long getRecommendOrgCode() {
    return recommendOrgCode;
  }

  public void setRecommendOrgCode(Long recommendOrgCode) {
    this.recommendOrgCode = recommendOrgCode;
  }

  public String getZhGrantName() {
    return zhGrantName;
  }

  public void setZhGrantName(String zhGrantName) {
    this.zhGrantName = zhGrantName;
  }

  public String getSubjectCode() {
    return subjectCode;
  }

  public void setSubjectCode(String subjectCode) {
    this.subjectCode = subjectCode;
  }

  public String getDomainCode() {
    return domainCode;
  }

  public void setDomainCode(String domainCode) {
    this.domainCode = domainCode;
  }

  public String getStatYear() {
    return statYear;
  }

  public void setStatYear(String statYear) {
    this.statYear = statYear;
  }

  public Double getReqAmt() {
    return reqAmt;
  }

  public void setReqAmt(Double reqAmt) {
    this.reqAmt = reqAmt;
  }

  public Long getAdminOrgCode() {
    return adminOrgCode;
  }

  public void setAdminOrgCode(Long adminOrgCode) {
    this.adminOrgCode = adminOrgCode;
  }

  public String getGrantOrgName() {
    return grantOrgName;
  }

  public void setGrantOrgName(String grantOrgName) {
    this.grantOrgName = grantOrgName;
  }

  public String getResearchValue() {
    return researchValue;
  }

  public void setResearchValue(String researchValue) {
    this.researchValue = researchValue;
  }

  public Long getGrantCode() {
    return grantCode;
  }

  public void setGrantCode(Long grantCode) {
    this.grantCode = grantCode;
  }

  public String getSubjectId() {
    return subjectId;
  }

  public void setSubjectId(String subjectId) {
    this.subjectId = subjectId;
  }

  public String getSubjectName() {
    return subjectName;
  }

  public void setSubjectName(String subjectName) {
    this.subjectName = subjectName;
  }


}
