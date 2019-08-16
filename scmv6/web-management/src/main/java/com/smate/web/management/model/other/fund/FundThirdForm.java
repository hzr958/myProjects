package com.smate.web.management.model.other.fund;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.smate.core.base.utils.model.Page;

public class FundThirdForm implements Serializable {
  private static final long serialVersionUID = 5349977916118226573L;
  private Long id;
  private String fundTitleCn = ""; // 基金机会名称 - 中文 string(500)
  private String fundTitleEn = ""; // 基金机会名称 - 英文 string(500)
  private String fundTitleAbbr = ""; // 基金机会简称 string(100)
  private String fundNumber = ""; // 基金机会编号
  private String fundDesc = ""; // 基金机会描述 string(2000) not null
  private String disciplineClassificationType = ""; // 分类标准 string(100) not null
  private String disciplineLimit = ""; // 适用分类 string(100)
  private String fundKeywords = ""; // 关键词 string(500)
  private Integer fundYear; // 基金年度
  private Date applyDateStart; // 申请日期开始 yyyy-MM-dd not null
  private Date applyDateEnd; // 申请日期结束 yyyy-MM-dd not null
  private String fundingAgency = ""; // 资助机构名称 string(100) not null
  private String declareGuideUrl = ""; // 申报指南网址 string(200) not null
  private String declareUrl = ""; // 申报网址 string(200) not null
  private String accessoryUrl = ""; // 附件地址 string(200)
  private Date updateTime; // 记录更新时间 yyyy-MM-dd not null
  private Integer auditStatus;// 审核状态 默认0 未审核 ；
  private Page<ThirdSourcesFund> page;
  private List<ThirdSourcesFund> thirdSourcesFundlist;
  private ThirdSourcesFund thirdSourcesFund;
  private String searchKey;
  private String fundType = ""; // 五大类基金类型 string(20) not null
  private Date createTime; // 记录创建时间 yyyy-MM-dd not null
  private String ids = "";
  private String agencyViewName;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getFundTitleCn() {
    return fundTitleCn;
  }

  public void setFundTitleCn(String fundTitleCn) {
    fundTitleCn = getMaxString(fundTitleCn, 500);
    this.fundTitleCn = fundTitleCn;
  }

  public String getFundTitleEn() {
    return fundTitleEn;
  }

  public void setFundTitleEn(String fundTitleEn) {
    fundTitleEn = getMaxString(fundTitleEn, 500);
    this.fundTitleEn = fundTitleEn;
  }

  public String getFundTitleAbbr() {
    return fundTitleAbbr;
  }

  public void setFundTitleAbbr(String fundTitleAbbr) {
    fundTitleAbbr = getMaxString(fundTitleAbbr, 100);
    this.fundTitleAbbr = fundTitleAbbr;
  }

  public String getFundNumber() {
    return fundNumber;
  }

  public void setFundNumber(String fundNumber) {
    this.fundNumber = fundNumber;
  }

  public String getFundDesc() {
    return fundDesc;
  }

  public void setFundDesc(String fundDesc) {
    fundDesc = getMaxString(fundDesc, 2000);
    this.fundDesc = fundDesc;
  }

  public String getDisciplineClassificationType() {
    return disciplineClassificationType;
  }

  public void setDisciplineClassificationType(String disciplineClassificationType) {
    disciplineClassificationType = getMaxString(disciplineClassificationType, 1000);
    this.disciplineClassificationType = disciplineClassificationType;
  }

  public String getDisciplineLimit() {
    return disciplineLimit;
  }

  public void setDisciplineLimit(String disciplineLimit) {
    disciplineLimit = getMaxString(disciplineLimit, 100);
    this.disciplineLimit = disciplineLimit;
  }

  public String getFundKeywords() {
    return fundKeywords;
  }

  public void setFundKeywords(String fundKeywords) {
    fundKeywords = getMaxString(fundKeywords, 500);
    this.fundKeywords = fundKeywords;
  }

  public Integer getFundYear() {
    return fundYear;
  }

  public void setFundYear(Integer fundYear) {
    this.fundYear = fundYear;
  }

  public Date getApplyDateStart() {
    return applyDateStart;
  }

  public void setApplyDateStart(Date applyDateStart) {
    this.applyDateStart = applyDateStart;
  }

  public Date getApplyDateEnd() {
    return applyDateEnd;
  }

  public void setApplyDateEnd(Date applyDateEnd) {
    this.applyDateEnd = applyDateEnd;
  }

  public String getFundingAgency() {
    return fundingAgency;
  }

  public void setFundingAgency(String fundingAgency) {
    fundingAgency = getMaxString(fundingAgency, 100);
    this.fundingAgency = fundingAgency;
  }

  public String getDeclareGuideUrl() {
    return declareGuideUrl;
  }

  public void setDeclareGuideUrl(String declareGuideUrl) {
    declareGuideUrl = getMaxString(declareGuideUrl, 200);
    this.declareGuideUrl = declareGuideUrl;
  }

  public String getDeclareUrl() {
    return declareUrl;
  }

  public void setDeclareUrl(String declareUrl) {
    declareUrl = getMaxString(declareUrl, 200);
    this.declareUrl = declareUrl;
  }

  public String getAccessoryUrl() {
    return accessoryUrl;
  }

  public void setAccessoryUrl(String accessoryUrl) {
    accessoryUrl = getMaxString(accessoryUrl, 1000);
    this.accessoryUrl = accessoryUrl;
  }

  public Date getUpdateTime() {
    return updateTime;
  }

  public void setUpdateTime(Date updateTime) {
    this.updateTime = updateTime;
  }

  public String getMaxString(String s, int max) {
    if (StringUtils.isBlank(s)) {
      return "";
    }
    s = s.trim();
    if (s.length() <= max) {
      return s;
    } else {
      return s.substring(0, max);
    }
  }

  public Page<ThirdSourcesFund> getPage() {
    return page;
  }

  public void setPage(Page<ThirdSourcesFund> page) {
    this.page = page;
  }

  public Integer getAuditStatus() {
    return auditStatus;
  }

  public void setAuditStatus(Integer auditStatus) {
    this.auditStatus = auditStatus;
  }

  public List<ThirdSourcesFund> getThirdSourcesFundlist() {
    return thirdSourcesFundlist;
  }

  public void setThirdSourcesFundlist(List<ThirdSourcesFund> thirdSourcesFundlist) {
    this.thirdSourcesFundlist = thirdSourcesFundlist;
  }

  public ThirdSourcesFund getThirdSourcesFund() {
    if (thirdSourcesFund == null) {
      thirdSourcesFund = new ThirdSourcesFund();
      thirdSourcesFund.setId(id);
      thirdSourcesFund.setFundTitleCn(fundTitleCn);
      thirdSourcesFund.setFundTitleEn(fundTitleEn);
      thirdSourcesFund.setFundTitleAbbr(fundTitleAbbr);
      thirdSourcesFund.setFundNumber(fundNumber);
      thirdSourcesFund.setFundDesc(fundDesc);
      thirdSourcesFund.setDisciplineClassificationType(disciplineClassificationType);
      thirdSourcesFund.setDisciplineLimit(disciplineLimit);
      thirdSourcesFund.setFundKeywords(fundKeywords);
      thirdSourcesFund.setFundYear(fundYear);
      thirdSourcesFund.setApplyDateStart(applyDateStart);
      thirdSourcesFund.setApplyDateEnd(applyDateEnd);
      thirdSourcesFund.setFundingAgency(fundingAgency);
      thirdSourcesFund.setDeclareGuideUrl(declareGuideUrl);
      thirdSourcesFund.setDeclareUrl(declareUrl);
      thirdSourcesFund.setAccessoryUrl(accessoryUrl);
      thirdSourcesFund.setUpdateTime(updateTime);
      thirdSourcesFund.setAuditStatus(auditStatus);
      thirdSourcesFund.setSearchKey(searchKey);
      thirdSourcesFund.setFundType(fundType);
      thirdSourcesFund.setCreateTime(createTime);
      thirdSourcesFund.setAgencyViewName(agencyViewName);
    }
    return thirdSourcesFund;
  }

  public void setThirdSourcesFund(ThirdSourcesFund thirdSourcesFund) {
    this.thirdSourcesFund = thirdSourcesFund;
  }

  public String getSearchKey() {
    return searchKey;
  }

  public void setSearchKey(String searchKey) {
    this.searchKey = searchKey;
  }

  public String getFundType() {
    return fundType;
  }

  public void setFundType(String fundType) {
    this.fundType = fundType;
  }

  public Date getCreateTime() {
    return createTime;
  }

  public void setCreateTime(Date createTime) {
    this.createTime = createTime;
  }

  public String getIds() {
    return ids;
  }

  public void setIds(String ids) {
    this.ids = ids;
  }

  public String getAgencyViewName() {
    return agencyViewName;
  }

  public void setAgencyViewName(String agencyViewName) {
    this.agencyViewName = agencyViewName;
  }

}
