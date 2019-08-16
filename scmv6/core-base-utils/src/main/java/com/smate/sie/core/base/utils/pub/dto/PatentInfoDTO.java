package com.smate.sie.core.base.utils.pub.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.smate.sie.core.base.utils.pub.dom.PatAppliersBean;

/**
 * 专利信息
 * 
 * @author ZSJ
 *
 * @date 2019年1月31日
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class PatentInfoDTO extends PubTypeInfoDTO implements Serializable {

  private static final long serialVersionUID = -847234542756756302L;

  private String applicationNo; // 申请号
  private String publicationOpenNo; // 公开号

  private String typeCode; // 专利类型，发明专利51/实用新型52/外观设计53/植物专利54

  private String typeName;

  private String applicationDate; // 申请日期

  private String authDate; // 授权日期

  private String authNo; // 授权号

  private String patentStatusCode; // 专利状态

  private String patentStatusName; // 专利状态

  private List<PatAppliersBean> appliers = new ArrayList<PatAppliersBean>();

  private String IPC; // IPC号

  private String CPC; // cpc号

  private String ceritficateNo; // 证书编号

  private String issuingAuthority; // 发证单位

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
   * ", authDate=" + authDate + ", authNo=" + authNo + ", patentStatusCode=" + patentStatusCode +
   * ", patentStatusName=" + patentStatusName + ", IPC=" + IPC + ", CPC=" + CPC + ", ceritficateNo=" +
   * ceritficateNo + ", issueOrg=" + issueOrg + "]"; }
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


  public String getPatentStatusCode() {
    return patentStatusCode;
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

  public String getIssuingAuthority() {
    return issuingAuthority;
  }

  public void setIssuingAuthority(String issuingAuthority) {
    this.issuingAuthority = issuingAuthority;
  }


}
