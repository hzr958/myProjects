package com.smate.center.task.model.thirdparty;

import com.smate.core.base.utils.string.StringUtils;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 *
 * 基金机会(主要注意区分唯一主键的问题，因为来源系统不一致)
 *
 * @author aijiangbin
 * @create 2019-04-23 16:33
 **/
@Entity
@Table(name = "v_third_sources_fund")
public class ThirdSourcesFund {

  @Id
  @Column(name = "ID")
  private Long id;

  @Column(name = "fund_title_cn")
  private String fundTitleCn = ""; // 基金机会名称 - 中文 string(500)

  @Column(name = "fund_title_en")
  private String fundTitleEn = ""; // 基金机会名称 - 英文 string(500)

  @Column(name = "fund_title_abbr")
  private String fundTitleAbbr = ""; // 基金机会简称 string(100)

  @Column(name = "fund_number")
  private String fundNumber = ""; // 基金机会编号

  @Column(name = "fund_desc")
  private String fundDesc = ""; // 基金机会描述 string(2000) not null

  @Column(name = "fund_type")
  private String fundType = ""; // 五大类基金类型 string(20) not null

  @Column(name = "discipline_classification_type")
  private String disciplineClassificationType = ""; // 分类标准 string(100) not null

  @Column(name = "discipline_limit")
  private String disciplineLimit = ""; // 适用分类 string(100)

  @Column(name = "fund_keywords")
  private String fundKeywords = ""; // 关键词 string(500)

  @Column(name = "fund_year")
  private Integer fundYear; // 基金年度

  @Column(name = "apply_date_start")
  private Date applyDateStart; // 申请日期开始 yyyy-MM-dd not null

  @Column(name = "apply_date_end")
  private Date applyDateEnd; // 申请日期结束 yyyy-MM-dd not null

  @Column(name = "funding_agency")
  private String fundingAgency = ""; // 资助机构名称 string(100) not null

  @Column(name = "declare_guide_url")
  private String declareGuideUrl = ""; // 申报指南网址 string(200) not null

  @Column(name = "declare_url")
  private String declareUrl = ""; // 申报网址 string(200) not null

  @Column(name = "accessorys")
  private String accessoryUrl = ""; // 附件地址 string(200)

  @Column(name = "update_time")
  private Date updateTime; // 记录更新时间 yyyy-MM-dd not null

  @Column(name = "create_time")
  private Date createTime; // 记录创建时间 yyyy-MM-dd not null

  @Column(name = "audit_status")
  private Integer auditStatus = 0;// 审核状态 默认0 未审核 ；

  public String getFundType() {
    return fundType;
  }

  public void setFundType(String fundType) {
    fundType = getMaxString(fundType, 20);
    this.fundType = fundType;
  }

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



  public Date getCreateTime() {
    return createTime;
  }

  public void setCreateTime(Date createTime) {
    this.createTime = createTime;
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

  public Integer getAuditStatus() {
    return auditStatus;
  }

  public void setAuditStatus(Integer auditStatus) {
    this.auditStatus = auditStatus;
  }
}
