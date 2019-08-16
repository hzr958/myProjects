package com.smate.sie.core.base.utils.model.validate.tmp;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 
 * @author ztg
 *
 */
@Entity
@Table(name = "TASK_KPI_VDDETAIL_SPLIT_PSN")
public class SieTaskKpiVdDetailSplitPsn implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = -5514969967517495776L;

  @Id
  @Column(name = "DETAIL_ID")
  private Long detailId;

  @Column(name = "UUID")
  private String uuId;

  @Column(name = "INS_NAME")
  private String insName;

  @Column(name = "NAME")
  private String name;

  @Column(name = "CERT_NO")
  private String certNo;

  @Column(name = "CERT_TYPE")
  private String certType;

  @Column(name = "PHONE")
  private String phone;

  @Column(name = "EMAIL")
  private String email;

  @Column(name = "V_PHONE")
  private Integer vPhone;

  @Column(name = "V_EMAIL")
  private Integer vEmail;

  @Column(name = "C_PHONE1")
  private String cPhone1;

  @Column(name = "C_EMAIL1")
  private String cEmail1;

  @Column(name = "C_PHONE2")
  private String cPhone2;

  @Column(name = "C_EMAIL2")
  private String cEmail2;

  @Column(name = "STAUTS")
  private Integer status;

  @Column(name = "INTERFACE_RESULT")
  private String interfaceResult;

  public SieTaskKpiVdDetailSplitPsn() {
    super();
  }

  public SieTaskKpiVdDetailSplitPsn(Long detailId, String uuId, String insName, String name, String certNo, String certType,
      String phone, String email, Integer vPhone, Integer vEmail, String cPhone1, String cEmail1, String cPhone2,
      String cEmail2, Integer status, String interfaceResult) {
    super();
    this.detailId = detailId;
    this.uuId = uuId;
    this.insName = insName;
    this.name = name;
    this.certNo = certNo;
    this.certType = certType;
    this.phone = phone;
    this.email = email;
    this.vPhone = vPhone;
    this.vEmail = vEmail;
    this.cPhone1 = cPhone1;
    this.cEmail1 = cEmail1;
    this.cPhone2 = cPhone2;
    this.cEmail2 = cEmail2;
    this.status = status;
    this.interfaceResult = interfaceResult;
  }

  public Long getDetailId() {
    return detailId;
  }

  public void setDetailId(Long detailId) {
    this.detailId = detailId;
  }

  public String getUuId() {
    return uuId;
  }

  public void setUuId(String uuId) {
    this.uuId = uuId;
  }

  public String getInsName() {
    return insName;
  }

  public void setInsName(String insName) {
    this.insName = insName;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getCertNo() {
    return certNo;
  }

  public void setCertNo(String certNo) {
    this.certNo = certNo;
  }

  public String getCertType() {
    return certType;
  }

  public void setCertType(String certType) {
    this.certType = certType;
  }

  public String getPhone() {
    return phone;
  }

  public void setPhone(String phone) {
    this.phone = phone;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public Integer getvPhone() {
    return vPhone;
  }

  public void setvPhone(Integer vPhone) {
    this.vPhone = vPhone;
  }

  public Integer getvEmail() {
    return vEmail;
  }

  public void setvEmail(Integer vEmail) {
    this.vEmail = vEmail;
  }

  public String getcPhone1() {
    return cPhone1;
  }

  public void setcPhone1(String cPhone1) {
    this.cPhone1 = cPhone1;
  }

  public String getcEmail1() {
    return cEmail1;
  }

  public void setcEmail1(String cEmail1) {
    this.cEmail1 = cEmail1;
  }

  public String getcPhone2() {
    return cPhone2;
  }

  public void setcPhone2(String cPhone2) {
    this.cPhone2 = cPhone2;
  }

  public String getcEmail2() {
    return cEmail2;
  }

  public void setcEmail2(String cEmail2) {
    this.cEmail2 = cEmail2;
  }

  public Integer getStatus() {
    return status;
  }

  public void setStatus(Integer status) {
    this.status = status;
  }

  public String getInterfaceResult() {
    return interfaceResult;
  }

  public void setInterfaceResult(String interfaceResult) {
    this.interfaceResult = interfaceResult;
  }

  @Override
  public String toString() {
    return "TmpKpiVdDetailSplitPsn [detailId=" + detailId + ", uuId=" + uuId + ", insName=" + insName + ", name=" + name
        + ", certNo=" + certNo + ", certType=" + certType + ", phone=" + phone + ", email=" + email + ", vPhone="
        + vPhone + ", vEmail=" + vEmail + ", cPhone1=" + cPhone1 + ", cEmail1=" + cEmail1 + ", cPhone2=" + cPhone2
        + ", cEmail2=" + cEmail2 + ", status=" + status + ", interfaceResult=" + interfaceResult + "]";
  }
}
