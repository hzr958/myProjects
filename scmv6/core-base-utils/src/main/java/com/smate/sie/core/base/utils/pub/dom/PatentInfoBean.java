package com.smate.sie.core.base.utils.pub.dom;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * 专利
 * 
 * @author sjzhou
 *
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class PatentInfoBean extends PubTypeInfoBean {

  private String applicationNo = new String(); // 申请号
  private String publicationOpenNo = new String(); // 公开号

  private String typeCode = new String(); // 专利类型，发明专利51/实用新型52/外观设计53/植物专利54

  private String typeName = new String();

  private String applicationDate = new String(); // 申请日期

  private String authDate = new String(); // 授权日期

  private String authNo = new String(); // 授权号

  private String issuingAuthority = new String(); // 发证单位

  private String patentStatusCode = new String(); // 专利状态

  private String patentStatusName = new String(); // 专利状态

  private List<PatAppliersBean> appliers = new ArrayList<PatAppliersBean>();

  private String IPC = new String(); // IPC号

  private String CPC = new String(); // cpc号

  private String ceritficateNo = new String(); // 证书编号

  /**
   * 申请日期
   * 
   * @return
   */
  public String getApplicationDate() {
    return applicationDate;
  }

  /*
   * @Override public String toString() { return "PatentInfoBean [applicationNo=" + applicationNo +
   * ", typeCode=" + typeCode + ", typeName=" + typeName + ", applicationDate=" + applicationDate +
   * ", authDate=" + authDate + ", authNo=" + authNo + ", issuingAuthority=" + issuingAuthority +
   * ", patentStatusCode=" + patentStatusCode + ", patentStatusName=" + patentStatusName +
   * ", applier=" + applier + ", IPC=" + IPC + ", CPC=" + CPC + ", ceritficateNo=" + ceritficateNo +
   * ", issueOrg=" + issueOrg + "]"; }
   */

  public String getApplicationNo() {
    return applicationNo;
  }

  public String getTypeCode() {
    return typeCode;
  }

  public String getTypeName() {
    return typeName;
  }

  public String getAuthDate() {
    return authDate;
  }

  public String getAuthNo() {
    return authNo;
  }

  public String getIssuingAuthority() {
    return issuingAuthority;
  }

  public String getPatentStatusCode() {
    return patentStatusCode;
  }

  public String getPatentStatusName() {
    return patentStatusName;
  }

  public List<PatAppliersBean> getAppliers() {
    return appliers;
  }

  public String getIPC() {
    return IPC;
  }

  public String getCPC() {
    return CPC;
  }

  public String getCeritficateNo() {
    return ceritficateNo;
  }

  public void setApplicationNo(String applicationNo) {
    this.applicationNo = applicationNo;
  }

  public void setTypeCode(String typeCode) {
    this.typeCode = typeCode;
  }

  public void setTypeName(String typeName) {
    this.typeName = typeName;
  }

  public void setAuthDate(String authDate) {
    this.authDate = authDate;
  }

  public void setAuthNo(String authNo) {
    this.authNo = authNo;
  }

  public void setIssuingAuthority(String issuingAuthority) {
    this.issuingAuthority = issuingAuthority;
  }

  public void setPatentStatusCode(String patentStatusCode) {
    this.patentStatusCode = patentStatusCode;
  }

  public void setPatentStatusName(String patentStatusName) {
    this.patentStatusName = patentStatusName;
  }

  public void setAppliers(List<PatAppliersBean> appliers) {
    this.appliers = appliers;
  }

  public void setIPC(String iPC) {
    IPC = iPC;
  }

  public void setCPC(String cPC) {
    CPC = cPC;
  }

  public void setCeritficateNo(String ceritficateNo) {
    this.ceritficateNo = ceritficateNo;
  }

  public void setApplicationDate(String applicationDate) {
    this.applicationDate = applicationDate;
  }

  public String getPublicationOpenNo() {
    return publicationOpenNo;
  }

  public void setPublicationOpenNo(String publicationOpenNo) {
    this.publicationOpenNo = publicationOpenNo;
  }

}
