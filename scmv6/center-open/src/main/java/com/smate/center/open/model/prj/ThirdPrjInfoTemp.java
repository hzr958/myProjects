package com.smate.center.open.model.prj;

/**
 * 保存 第三方系统的 临时 项目信息
 * 
 * @author AiJiangBin
 *
 */
public class ThirdPrjInfoTemp {
  private String fromSys;
  private String groupName; // 群组名称(标题) 项目名称(标题)
  private String applyCode; // 申请代码 可为空
  private String fundingTypes; // 资助类别
  private String prjExternalNo; // 项目批准号
  private String amount; // 资助金额

  private String currency; // 币种

  private String startDate;

  private String endDate;

  private String insName; // 依托单位
  private String partPsnNames; // 参与人员名称列表(分号分割)
  private String keywordsZh;
  private String keywordsEn;
  private String schemeAgencyName; // 项目资助机构
  private String prjStatus = "1"; // 项目状态 1-申请；2-在研；3-完成

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

  public String getGroupName() {
    return groupName;
  }

  public void setGroupName(String groupName) {
    this.groupName = groupName;
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

  public String getAmount() {
    return amount;
  }

  public void setAmount(String amount) {
    this.amount = amount;
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

  public String getPrjStatus() {
    return prjStatus;
  }

  public void setPrjStatus(String prjStatus) {
    this.prjStatus = prjStatus;
  }

  public String getCurrency() {
    return currency;
  }

  public void setCurrency(String currency) {
    this.currency = currency;
  }

  public String getFromSys() {
    return fromSys;
  }

  public void setFromSys(String fromSys) {
    this.fromSys = fromSys;
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
