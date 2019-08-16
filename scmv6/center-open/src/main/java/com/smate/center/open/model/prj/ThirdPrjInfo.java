package com.smate.center.open.model.prj;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * 第三方项目信息
 * 
 * @author lhd
 *
 */
@Entity
@Table(name = "THIRD_SYS_PRJ_INFO")
public class ThirdPrjInfo implements Serializable {

  private static final long serialVersionUID = 3860078751023333021L;
  @Id
  @Column(name = "ID")
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "V_SEQ_THIRD_SYS_PRJ_INFO", allocationSize = 1)
  @GeneratedValue(generator = "SEQ_STORE", strategy = GenerationType.SEQUENCE)
  private Long id;// 主键
  @Column(name = "OPENID")
  private String openId;// openid
  @Column(name = "FROM_SYS")
  private String fromSys;// 第三方系统标识
  @Column(name = "GROUP_CODE")
  private String groupCode;// 群组认证码
  @Column(name = "PRJ_NAME")
  private String prjName;// 项目名称(标题)
  @Column(name = "APPLY_CODE")
  private String applyCode;// 申请代码
  @Column(name = "FUNDING_TYPES")
  private String fundingTypes;// 资助类别
  @Column(name = "SCHEME_AGENCY_NAME")
  private String schemeAgencyName;// 资助机构
  @Column(name = "PRJ_EXTERNAL_NO")
  private String prjExternalNo;// 项目批准号
  @Column(name = "AMOUNT")
  private double amount;// 资助金额
  @Column(name = "CURRENCY")
  private String currency;// 币种
  @Column(name = "START_YEAR")
  private Integer startYear;// 开始年份
  @Column(name = "START_MONTH")
  private Integer startMonth;// 开始月份
  @Column(name = "START_DAY")
  private Integer startDay;// 开始日

  @Column(name = "END_YEAR")
  private Integer endYear;// 结束年份
  @Column(name = "END_MONTH")
  private Integer endMonth;// 结束月份
  @Column(name = "END_DAY")
  private Integer endDay;// 结束日

  @Column(name = "INS_NAME")
  private String insName;// 依托单位
  @Column(name = "PART_PSN_NAMES")
  private String partPsnNames;// 参与人员名称列表(分号分割)

  @Column(name = "KEYWORDS_ZH")
  private String keywordsZh;// 中文关键词

  @Column(name = "KEYWORDS_EN")
  private String keywordsEn;// 英文关键词

  @Column(name = "PRJ_STATUS")
  private Integer prjStatus;// 项目状态 1-申请；2-在研；3-完成

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getOpenId() {
    return openId;
  }

  public void setOpenId(String openId) {
    this.openId = openId;
  }

  public String getFromSys() {
    return fromSys;
  }

  public void setFromSys(String fromSys) {
    this.fromSys = fromSys;
  }

  public String getGroupCode() {
    return groupCode;
  }

  public void setGroupCode(String groupCode) {
    this.groupCode = groupCode;
  }

  public String getPrjName() {
    return prjName;
  }

  public void setPrjName(String prjName) {
    this.prjName = prjName;
  }

  public String getApplyCode() {
    return applyCode;
  }

  public void setApplyCode(String applyCode) {
    this.applyCode = applyCode;
  }

  public String getFundingTypes() {
    return fundingTypes;
  }

  public void setFundingTypes(String fundingTypes) {
    this.fundingTypes = fundingTypes;
  }

  public String getPrjExternalNo() {
    return prjExternalNo;
  }

  public void setPrjExternalNo(String prjExternalNo) {
    this.prjExternalNo = prjExternalNo;
  }

  public double getAmount() {
    return amount;
  }

  public void setAmount(double amount) {
    this.amount = amount;
  }

  public Integer getStartYear() {
    return startYear;
  }

  public void setStartYear(Integer startYear) {
    this.startYear = startYear;
  }

  public Integer getStartMonth() {
    return startMonth;
  }

  public void setStartMonth(Integer startMonth) {
    this.startMonth = startMonth;
  }

  public Integer getEndYear() {
    return endYear;
  }

  public void setEndYear(Integer endYear) {
    this.endYear = endYear;
  }

  public Integer getEndMonth() {
    return endMonth;
  }

  public void setEndMonth(Integer endMonth) {
    this.endMonth = endMonth;
  }

  public String getInsName() {
    return insName;
  }

  public void setInsName(String insName) {
    this.insName = insName;
  }

  public String getPartPsnNames() {
    return partPsnNames;
  }

  public void setPartPsnNames(String partPsnNames) {
    this.partPsnNames = partPsnNames;
  }

  public Integer getPrjStatus() {
    return prjStatus;
  }

  public void setPrjStatus(Integer prjStatus) {
    this.prjStatus = prjStatus;
  }

  public String getCurrency() {
    return currency;
  }

  public void setCurrency(String currency) {
    this.currency = currency;
  }

  public Integer getStartDay() {
    return startDay;
  }

  public void setStartDay(Integer startDay) {
    this.startDay = startDay;
  }

  public Integer getEndDay() {
    return endDay;
  }

  public void setEndDay(Integer endDay) {
    this.endDay = endDay;
  }

  public String getKeywordsZh() {
    return keywordsZh;
  }

  public void setKeywordsZh(String keywordsZh) {
    this.keywordsZh = keywordsZh;
  }

  public String getKeywordsEn() {
    return keywordsEn;
  }

  public void setKeywordsEn(String keywordsEn) {
    this.keywordsEn = keywordsEn;
  }

  public String getSchemeAgencyName() {
    return schemeAgencyName;
  }

  public void setSchemeAgencyName(String schemeAgencyName) {
    this.schemeAgencyName = schemeAgencyName;
  }



}
