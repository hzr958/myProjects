package com.smate.web.prj.form.fileimport;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 文件解析成 项目信息
 */
public class PrjInfoDTO implements Serializable {

  private static final long serialVersionUID = -4007618737841418376L;
  private Long prjId  ;  // 项目id
  private String dupValue = "";//重复的项目id，查重
  private String dupOpt = "";// 重复的操作  refresh ，skip
  private String template = "";// 模板
  private String zhTitle =""; // 项目名称
  private String enTitle=""; // 项目名称
  private String leader =""; // 项目负责人
  private String zhkeywords ="";//    关键词中文 （分号分隔）
  private String enKeywords ="";//    关键词 （分号分隔）
  private String zhAbstract ="";//    项目摘要
  private String enAbstract ="";//    项目摘要
  private String projectNo ="";// 项目批准号、编号
  private String fundingYear ="";//   立项年度
  private String agency =""; // 资助机构名称

  private String scheme =""; // 资助类别名称
  private String insName ="";//  依托单位
  private String secInsName ="";//  二级单位名称
  private String startDate ="";//    开始日期
  private String endDate ="";//    开始日期
  private String prjAmount ="";//   项目金额

  private String applicationCode = "" ;//申请代码
  private String subjectCode="";//     学科代码
  private String economicCoode = "";// 行业代码
  private String cseiCoode = "";//产业代码
  private List<PrjReportDTO> prjReportDTOS =new ArrayList<>() ;  // 项目报告
  private List<PrjFundPlanDTO> prjFundPlanDTOS = new ArrayList<>();  //项目经费计划
  private List<PrjMemberDTO> members = new ArrayList<>();  //项目成员

  private String amountUnit ="RMB";//   金额单元
  //
  private String prjType ="";//   项目类型

  private String prjStatus ="";//   项目状态

  private String scienceArea ="";//    项目领域

  private String fullLink ="";//    全文链接

  private String remark ="";//     备注
  @JsonIgnore
  private String prjMembers ="";//     项目成员  （分号分隔）
  @JsonIgnore
  private List<Map<String ,String>> membersList = new ArrayList<>();
  @JsonIgnore
  private String sourceDbCode ="";//     数据来源 SCMEXCEL
  @JsonIgnore
  private String subject_code1 ="";//     学科代码 1
  @JsonIgnore
  private String subject_code2 ="";//     学科代码 2



  public String getTemplate() {
    return template;
  }

  public void setTemplate(String template) {
    this.template = template;
  }

  public String getDupOpt() {
    return dupOpt;
  }

  public void setDupOpt(String dupOpt) {
    this.dupOpt = dupOpt;
  }

  public Long getPrjId() {
    return prjId;
  }

  public void setPrjId(Long prjId) {
    this.prjId = prjId;
  }

  public String getDupValue() {
    return dupValue;
  }

  public void setDupValue(String dupValue) {
    this.dupValue = dupValue;
  }

  public String getSubjectCode() {
    return subjectCode;
  }

  public void setSubjectCode(String subjectCode) {
    this.subjectCode = subjectCode;
  }

  public String getEconomicCoode() {
    return economicCoode;
  }

  public void setEconomicCoode(String economicCoode) {
    this.economicCoode = economicCoode;
  }

  public String getCseiCoode() {
    return cseiCoode;
  }

  public void setCseiCoode(String cseiCoode) {
    this.cseiCoode = cseiCoode;
  }

  public List<PrjReportDTO> getPrjReportDTOS() {
    return prjReportDTOS;
  }

  public void setPrjReportDTOS(List<PrjReportDTO> prjReportDTOS) {
    this.prjReportDTOS = prjReportDTOS;
  }

  public List<PrjMemberDTO> getMembers() {
    return members;
  }

  public void setMembers(List<PrjMemberDTO> members) {
    this.members = members;
  }

  public String getSecInsName() {
    return secInsName;
  }

  public void setSecInsName(String secInsName) {
    this.secInsName = secInsName;
  }

  public String getApplicationCode() {
    return applicationCode;
  }

  public void setApplicationCode(String applicationCode) {
    this.applicationCode = applicationCode;
  }


  public List<PrjFundPlanDTO> getPrjFundPlanDTOS() {
    return prjFundPlanDTOS;
  }

  public void setPrjFundPlanDTOS(List<PrjFundPlanDTO> prjFundPlanDTOS) {
    this.prjFundPlanDTOS = prjFundPlanDTOS;
  }


  public List<Map<String, String>> getMembersList() {
    return membersList;
  }

  public void setMembersList(List<Map<String, String>> membersList) {
    this.membersList = membersList;
  }

  public String getSubject_code1() {
    return subject_code1;
  }

  public void setSubject_code1(String subject_code1) {
    this.subject_code1 = subject_code1;
  }

  public String getSubject_code2() {
    return subject_code2;
  }

  public void setSubject_code2(String subject_code2) {
    this.subject_code2 = subject_code2;
  }

  public String getEnKeywords() {
    return enKeywords;
  }

  public void setEnKeywords(String enKeywords) {
    this.enKeywords = enKeywords;
  }

  public String getAmountUnit() {
    return amountUnit;
  }

  public void setAmountUnit(String amountUnit) {
    this.amountUnit = amountUnit;
  }

  public String getEnTitle() {
    return enTitle;
  }

  public void setEnTitle(String enTitle) {
    this.enTitle = enTitle;
  }

  public String getEnAbstract() {
    return enAbstract;
  }

  public void setEnAbstract(String enAbstract) {
    this.enAbstract = enAbstract;
  }

  public String getSourceDbCode() {
    return sourceDbCode;
  }

  public void setSourceDbCode(String sourceDbCode) {
    this.sourceDbCode = sourceDbCode;
  }

  public String getZhTitle() {
    return zhTitle;
  }

  public void setZhTitle(String zhTitle) {
    this.zhTitle = zhTitle;
  }

  public String getLeader() {
    return leader;
  }

  public void setLeader(String leader) {
    this.leader = leader;
  }

  public String getAgency() {
    return agency;
  }

  public void setAgency(String agency) {
    this.agency = agency;
  }

  public String getScheme() {
    return scheme;
  }

  public void setScheme(String scheme) {
    this.scheme = scheme;
  }

  public String getProjectNo() {
    return projectNo;
  }

  public void setProjectNo(String projectNo) {
    this.projectNo = projectNo;
  }

  public String getInsName() {
    return insName;
  }

  public void setInsName(String insName) {
    this.insName = insName;
  }

  public String getFundingYear() {
    return fundingYear;
  }

  public void setFundingYear(String fundingYear) {
    this.fundingYear = fundingYear;
  }

  public String getPrjType() {
    return prjType;
  }

  public void setPrjType(String prjType) {
    this.prjType = prjType;
  }

  public String getPrjStatus() {
    return prjStatus;
  }

  public void setPrjStatus(String prjStatus) {
    this.prjStatus = prjStatus;
  }

  public String getPrjAmount() {
    return prjAmount;
  }

  public void setPrjAmount(String prjAmount) {
    this.prjAmount = prjAmount;
  }

  public String getStartDate() {
    return startDate;
  }

  public void setStartDate(String startDate) {
    this.startDate = startDate;
  }

  public String getEndDate() {
    return endDate;
  }

  public void setEndDate(String endDate) {
    this.endDate = endDate;
  }

  public String getZhAbstract() {
    return zhAbstract;
  }

  public void setZhAbstract(String zhAbstract) {
    this.zhAbstract = zhAbstract;
  }

  public String getScienceArea() {
    return scienceArea;
  }

  public void setScienceArea(String scienceArea) {
    this.scienceArea = scienceArea;
  }

  public String getZhkeywords() {
    return zhkeywords;
  }

  public void setZhkeywords(String zhkeywords) {
    this.zhkeywords = zhkeywords;
  }

  public String getFullLink() {
    return fullLink;
  }

  public void setFullLink(String fullLink) {
    this.fullLink = fullLink;
  }

  public String getRemark() {
    return remark;
  }

  public void setRemark(String remark) {
    this.remark = remark;
  }

  public String getPrjMembers() {
    return prjMembers;
  }

  public void setPrjMembers(String prjMembers) {
    this.prjMembers = prjMembers;
  }
}
