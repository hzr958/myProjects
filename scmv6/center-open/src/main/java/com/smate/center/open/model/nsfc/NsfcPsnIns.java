package com.smate.center.open.model.nsfc;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.smate.core.base.utils.security.Des3Utils;


/**
 * nsfcrol个人基本信息表psn_ins同义词[SCM-5928【优化】Webservice支持负载均衡,改造时使用]
 * 
 * @author zk
 * 
 */
@Entity
@Table(name = "NSFCROL2_PSN_INS")
public class NsfcPsnIns implements Serializable {
  /**
   * 
   */
  private static final long serialVersionUID = 4520683773548340881L;
  /**
   * 
   */
  /** 主键. */
  private NsfcPsnInsPk pk;
  /** 在职状态. */
  private Long isIns;
  /** 在职状态. */
  private String isInsName;
  /** 职务. */
  private String duty;
  /** 职称. */
  private String title;
  /** 职称. */
  private String titleName;
  /** 人员状态. */
  private String psnApplyStatus;
  /** 部门Id. */
  private Long unitId;
  /** 部门Id. */
  private String unitName;
  /** 人员状态，0申请加入、1已加入、3拒绝加入、9删除. */
  private Integer status;
  /** 人员名. */
  private String psnName;
  private String zhName;
  private String enName;

  // 职称
  private String position;
  private Long posId;
  private Integer posGrades;

  /** 人员邮件. */
  private String psnEmail;
  /** 指派必要字段. */
  private String firstName;
  private String lastName;
  private String otherName;
  /** 部门管理-科研联系人或者科研管理员ID. */
  private Long unitManageOrContactId;
  /** 人员联系方式电话或者手机. */
  private String tel;
  private String mobile;
  // psnId,特殊用途，程序将pk.psnId拷贝到此
  private Long cpPsnId;

  private Integer isLogin;

  private Date createAt;

  private Long rolId;
  // 是否允许ROL-R提交成果
  private Integer allowSubmitPub;

  private Integer toSnsNodeId;

  private Long sendLoginPsnId;

  private String des3PsnId;
  // 是否是管理员
  private boolean manager;
  // 是否是部门联系人
  private boolean contact;

  private String insName;

  public NsfcPsnIns() {
    super();
    // 默认允许提交
    this.allowSubmitPub = 1;
    this.status = 0;
    this.createAt = new Date();
  }

  /**
   * 供自动提示查询用.
   * 
   * @param isIns
   * @param unitId
   * @param psnName
   * @param insId
   */
  public NsfcPsnIns(Long psnId, Long unitId, String zhName, String enName) {
    super();
    cpPsnId = psnId;
    this.unitId = unitId;
    this.zhName = zhName;
    this.enName = enName;
    this.pk = new NsfcPsnInsPk();
  }

  /**
   * @return pk
   */
  @EmbeddedId
  public NsfcPsnInsPk getPk() {
    return pk;
  }

  /**
   * @param pk
   */
  public void setPk(NsfcPsnInsPk pk) {
    this.pk = pk;
  }

  /**
   * @return isIns
   */
  @Column(name = "NOT_IN_JOB")
  public Long getIsIns() {
    return isIns;
  }

  @Transient
  public Long getCpPsnId() {
    return cpPsnId;
  }

  public void setCpPsnId(Long cpPsnId) {
    this.cpPsnId = cpPsnId;
  }

  /**
   * @return unitId
   */
  @Column(name = "UNIT_ID")
  public Long getUnitId() {
    return unitId;
  }

  /**
   * @param unitId
   */
  public void setUnitId(Long unitId) {
    this.unitId = unitId;
  }

  /**
   * @param isIns
   */
  public void setIsIns(Long isIns) {
    this.isIns = isIns;
  }

  /**
   * @return duty
   */
  @Column(name = "DUTY")
  public String getDuty() {
    return duty;
  }

  /**
   * @param duty
   */
  public void setDuty(String duty) {
    this.duty = duty;
  }

  /**
   * @return title
   */
  @Column(name = "TITLE")
  public String getTitle() {
    return title;
  }

  /**
   * @param title
   */
  public void setTitle(String title) {
    this.title = title;
  }

  /**
   * @return psnApplyStatus
   */
  @Transient
  public String getPsnApplyStatus() {
    return psnApplyStatus;
  }

  /**
   * @param psnApplyStatus
   */
  public void setPsnApplyStatus(String psnApplyStatus) {
    this.psnApplyStatus = psnApplyStatus;
  }

  /**
   * @return psnName
   */
  @Transient
  public String getPsnName() {
    return psnName;
  }

  /**
   * @param psnName
   */
  public void setPsnName(String psnName) {
    this.psnName = psnName;
  }

  @Column(name = "ZH_NAME")
  public String getZhName() {
    return zhName;
  }

  @Column(name = "EN_NAME")
  public String getEnName() {
    return enName;
  }

  @Column(name = "POSITION")
  public String getPosition() {
    return position;
  }

  @Column(name = "POS_ID")
  public Long getPosId() {
    return posId;
  }

  @Column(name = "POS_GRADES")
  public Integer getPosGrades() {
    return posGrades;
  }

  public void setZhName(String zhName) {
    this.zhName = zhName;
  }

  public void setEnName(String enName) {
    this.enName = enName;
  }

  public void setPosition(String position) {
    this.position = position;
  }

  public void setPosId(Long posId) {
    this.posId = posId;
  }

  public void setPosGrades(Integer posGrades) {
    this.posGrades = posGrades;
  }

  /**
   * @return psnEmail
   */
  @Column(name = "PSN_EMAIL")
  public String getPsnEmail() {
    return psnEmail;
  }

  /**
   * @return the insName
   */
  @Transient
  public String getInsName() {
    return insName;
  }

  /**
   * @param insName the insName to set
   */
  public void setInsName(String insName) {
    this.insName = insName;
  }

  /**
   * @param psnEmail
   */
  public void setPsnEmail(String psnEmail) {
    this.psnEmail = psnEmail;
  }

  /**
   * @return the status
   */
  @Column(name = "STATUS")
  public Integer getStatus() {
    return status;
  }

  /**
   * @param status the status to set
   */
  public void setStatus(Integer status) {
    this.status = status;
  }

  @Transient
  public String getIsInsName() {
    return isInsName;
  }

  public void setIsInsName(String isInsName) {
    this.isInsName = isInsName;
  }

  @Transient
  public Long getRolId() {
    return rolId;
  }

  public void setRolId(Long rolId) {
    this.rolId = rolId;
  }

  @Transient
  public String getUnitName() {
    return unitName;
  }

  public void setUnitName(String unitName) {
    this.unitName = unitName;
  }

  @Transient
  public Long getUnitManageOrContactId() {
    return unitManageOrContactId;
  }

  public void setUnitManageOrContactId(Long unitManageOrContactId) {
    this.unitManageOrContactId = unitManageOrContactId;
  }

  @Column(name = "TEL")
  public String getTel() {
    return tel;
  }

  public void setTel(String tel) {
    this.tel = tel;
  }

  /**
   * @return the mobile
   */
  @Column(name = "MOBILE")
  public String getMobile() {
    return mobile;
  }

  /**
   * @param mobile the mobile to set
   */
  public void setMobile(String mobile) {
    this.mobile = mobile;
  }

  @Column(name = "IS_LOGIN")
  public Integer getIsLogin() {
    return isLogin;
  }

  public void setIsLogin(Integer isLogin) {
    this.isLogin = isLogin;
  }

  @Transient
  public String getTitleName() {
    return titleName;
  }

  public void setTitleName(String titleName) {
    this.titleName = titleName;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

  /**
   * @return the createAt
   */
  @Column(name = "CREATE_DATE")
  public Date getCreateAt() {
    return createAt;
  }

  @Column(name = "ALLOW_SUBMIT_PUB")
  public Integer getAllowSubmitPub() {
    return allowSubmitPub;
  }

  public void setAllowSubmitPub(Integer allowSubmitPub) {
    this.allowSubmitPub = allowSubmitPub;
  }

  /**
   * @param createAt the createAt to set
   */
  public void setCreateAt(Date createAt) {
    this.createAt = createAt;
  }

  /**
   * @return the firstName
   */
  @Column(name = "FIRST_NAME")
  public String getFirstName() {
    return firstName;
  }

  /**
   * @param firstName the firstName to set
   */
  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  /**
   * @return the lastName
   */
  @Column(name = "LAST_NAME")
  public String getLastName() {
    return lastName;
  }

  /**
   * @param lastName the lastName to set
   */
  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  /**
   * @return the otherName
   */
  @Column(name = "OTHER_NAME")
  public String getOtherName() {
    return otherName;
  }

  /**
   * @param otherName the otherName to set
   */
  public void setOtherName(String otherName) {
    this.otherName = otherName;
  }

  /**
   * @return the sendLoginPsnId
   */
  @Transient
  public Long getSendLoginPsnId() {
    return sendLoginPsnId;
  }

  /**
   * @param sendLoginPsnId the sendLoginPsnId to set
   */
  public void setSendLoginPsnId(Long sendLoginPsnId) {
    this.sendLoginPsnId = sendLoginPsnId;
  }

  /**
   * @return the toSnsNodeIds
   */
  @Transient
  public Integer getToSnsNodeId() {
    return toSnsNodeId;
  }

  /**
   * @param toSnsNodeIds the toSnsNodeIds to set
   */
  public void setToSnsNodeId(Integer toSnsNodeId) {
    this.toSnsNodeId = toSnsNodeId;
  }

  @Transient
  public String getDes3PsnId() {
    if (this.getPsnId() != null && des3PsnId == null) {
      des3PsnId = Des3Utils.encodeToDes3(getPsnId().toString());
    }
    return des3PsnId;
  }

  @Transient
  public Long getPsnId() {
    if (this.pk != null) {
      return this.pk.getPsnId();
    }
    return null;
  }

  public void setDes3PsnId(String des3PsnId) {
    this.des3PsnId = des3PsnId;
  }

  @Transient
  public boolean isManager() {
    return manager;
  }

  public void setManager(boolean manager) {
    this.manager = manager;
  }

  @Transient
  public boolean isContact() {
    return contact;
  }

  public void setContact(boolean contact) {
    this.contact = contact;
  }

}
