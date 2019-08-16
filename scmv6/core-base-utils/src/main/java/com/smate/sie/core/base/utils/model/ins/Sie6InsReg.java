package com.smate.sie.core.base.utils.model.ins;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * 单位注册表.
 * 
 * @author hd
 *
 */
@Entity
@Table(name = "INS_REG")
public class Sie6InsReg implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = -1484834935725136713L;

  // 注册单位ID
  private Long insregId;
  // 单位ID(注册批准后的确ID)
  private Long insId;
  // 单位名称
  private String insName;
  // 单位地址
  private String insAddress;
  // 单位网地址
  private String insUrl;
  // 单位联系人姓名
  private String contactPsName;
  // 单位联系人ID
  private Long contactPsnId;
  // 单位联系电话
  private String contactTel;
  // 单位联系传真
  private String contactEmail;
  // 状态 R:注册 A:批准
  private String status;
  // 单位所在地区
  private Long regionId;
  // 注册时间
  private Date regDate;
  // 审批人ID
  private Long appPsnId;
  // 审批时间
  private Date appDate;
  // 密码
  private String password;
  // 审核步骤（json格式）
  private String approveStepDetail;
  // 审核步骤的当前状态
  private String approveStepIndex;
  // 审核重输的单位名称
  private String approveName;
  // 单位logo保存路径
  private String insLogoPath;
  // 单位开通方式：1 表单注册 3 接口创建
  private Integer regWay;

  private String mobile;

  @Id
  @Column(name = "INSREG_ID")
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_INS_REG", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
  public Long getInsregId() {
    return insregId;
  }

  @Column(name = "INS_ID")
  public Long getInsId() {
    return insId;
  }

  @Column(name = "NAME")
  public String getInsName() {
    return insName;
  }

  @Column(name = "ADDRESS")
  public String getInsAddress() {
    return insAddress;
  }

  @Column(name = "HTTP")
  public String getInsUrl() {
    return insUrl;
  }

  @Column(name = "CONTACT_PSN_NAME")
  public String getContactPsName() {
    return contactPsName;
  }

  @Column(name = "CONTACT_PSN_ID")
  public Long getContactPsnId() {
    return contactPsnId;
  }

  @Column(name = "CONTACT_TEL")
  public String getContactTel() {
    return contactTel;
  }

  @Column(name = "CONTACT_EMAIL")
  public String getContactEmail() {
    return contactEmail;
  }

  @Column(name = "status")
  public String getStatus() {
    return status;
  }

  @Column(name = "REGION_ID")
  public Long getRegionId() {
    return regionId;
  }

  @Column(name = "REG_DATE")
  public Date getRegDate() {
    return regDate;
  }

  @Column(name = "APP_PSN_ID")
  public Long getAppPsnId() {
    return appPsnId;
  }

  @Column(name = "APP_DATE")
  public Date getAppDate() {
    return appDate;
  }

  @Column(name = "PASSWORD")
  public String getPassword() {
    return password;
  }

  @Column(name = "APPROVE_STEP_DETAIL")
  public String getApproveStepDetail() {
    return approveStepDetail;
  }

  @Column(name = "APPROVE_STEP_INDEX")
  public String getApproveStepIndex() {
    return approveStepIndex;
  }

  @Column(name = "APPROVE_NAME")
  public String getApproveName() {
    return approveName;
  }

  public void setInsregId(Long insregId) {
    this.insregId = insregId;
  }

  public void setInsId(Long insId) {
    this.insId = insId;
  }

  public void setInsName(String insName) {
    this.insName = insName;
  }

  public void setInsAddress(String insAddress) {
    this.insAddress = insAddress;
  }

  public void setInsUrl(String insUrl) {
    this.insUrl = insUrl;
  }

  public void setContactPsName(String contactPsName) {
    this.contactPsName = contactPsName;
  }

  public void setContactPsnId(Long contactPsnId) {
    this.contactPsnId = contactPsnId;
  }

  public void setContactTel(String contactTel) {
    this.contactTel = contactTel;
  }

  public void setContactEmail(String contactEmail) {
    this.contactEmail = contactEmail;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public void setRegionId(Long regionId) {
    this.regionId = regionId;
  }

  public void setRegDate(Date regDate) {
    this.regDate = regDate;
  }

  public void setAppPsnId(Long appPsnId) {
    this.appPsnId = appPsnId;
  }

  public void setAppDate(Date appDate) {
    this.appDate = appDate;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public void setApproveStepDetail(String approveStepDetail) {
    this.approveStepDetail = approveStepDetail;
  }

  public void setApproveStepIndex(String approveStepIndex) {
    this.approveStepIndex = approveStepIndex;
  }

  public void setApproveName(String approveName) {
    this.approveName = approveName;
  }

  @Column(name = "LOGO_PATH")
  public String getInsLogoPath() {
    return insLogoPath;
  }

  public void setInsLogoPath(String insLogoPath) {
    this.insLogoPath = insLogoPath;
  }

  @Column(name = "REG_WAY")
  public Integer getRegWay() {
    return regWay;
  }

  public void setRegWay(Integer regWay) {
    this.regWay = regWay;
  }


  @Column(name = "MOBILE")
  public String getMobile() {
    return mobile;
  }

  public void setMobile(String mobile) {
    this.mobile = mobile;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }
}
