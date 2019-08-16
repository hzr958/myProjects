package com.smate.center.batch.model.sns.pub;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.smate.core.base.utils.string.ServiceUtil;


/**
 * 
 * 个人电子邮件.
 * 
 * @author zt
 * 
 */
@Entity
@Table(name = "PSN_EMAIL")
public class PersonEmail implements Serializable {


  /**
   * 
   */
  private static final long serialVersionUID = -3481261819897586310L;

  /**
   * 主健.
   */
  private Long id;

  private Long psnId;

  /**
   * email 帐号.
   */
  private String email;
  /**
   * 邮件左半部分,例如devers@sohu.com(leftPart = devers).
   */
  private String leftPart;
  /**
   * 邮件右半部分 ,例如devers@sohu.com(rightPart = sohu.com).
   */
  private String rightPart;
  /**
   * 是否为首邮件.
   */
  private Integer firstMail;
  /**
   * 是否为登录邮件.
   */
  private Integer loginMail;
  /**
   * 是否是校验过的邮件.
   */
  private Integer isVerify;

  /**
   * 用户姓名.
   */
  private String zhPsnName;

  /**
   * 用户英文名.
   */
  private String enPsnName;

  // 加密ID.
  private String des3Id;
  // email列表，显示时使用.
  private List<PersonEmail> personEmails;
  // 登录EMAIL
  private String loginName;
  // 确认结果success,error
  private String confirmResult;
  // 操作结果消息,显示时使用.
  private String msg;

  public PersonEmail() {
    super();
  }

  public PersonEmail(Long psnId, String email, Integer firstMail, Integer loginMail, Integer isVerify) {
    super();
    this.psnId = psnId;
    this.email = email;
    this.firstMail = firstMail;
    this.isVerify = isVerify;
    this.loginMail = loginMail;
    String[] parts = email.split("@");
    this.leftPart = parts[0];
    this.rightPart = parts[1];
  }

  /**
   * 共享须使用的构造函数.
   * 
   * @param psnId
   * @param email
   * @param psnName
   */
  public PersonEmail(Long psnId, String zhPsnName, String enPsnName, String email) {

    this.psnId = psnId;
    this.email = email;
    this.zhPsnName = zhPsnName;
    this.enPsnName = enPsnName;
  }

  /**
   * @return the id
   */
  @Id
  @Column(name = "ID")
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "seq_psn_email", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
  public Long getId() {
    return id;
  }

  @Column(name = "PSN_ID")
  public Long getPsnId() {
    return psnId;
  }

  public void setPsnId(Long psnId) {
    this.psnId = psnId;
  }

  /**
   * @param id the id to set
   */
  public void setId(Long id) {
    this.id = id;
  }

  /**
   * @return the email
   */
  @Column(name = "EMAIL")
  public String getEmail() {
    return email;
  }

  /**
   * @param email the email to set
   */
  public void setEmail(String email) {
    this.email = email;
  }

  /**
   * @return the leftPart
   */
  @Column(name = "LEFT_PART")
  public String getLeftPart() {
    return leftPart;
  }

  /**
   * @param leftPart the leftPart to set
   */
  public void setLeftPart(String leftPart) {
    this.leftPart = leftPart;
  }

  /**
   * @return the rightPart
   */
  @Column(name = "RIGHT_PART")
  public String getRightPart() {
    return rightPart;
  }

  /**
   * @param rightPart the rightPart to set
   */
  public void setRightPart(String rightPart) {
    this.rightPart = rightPart;
  }

  /**
   * @return the firstMail
   */
  @Column(name = "FIRST_MAIL")
  public Integer getFirstMail() {
    return firstMail;
  }

  /**
   * @param firstMail the firstMail to set
   */
  public void setFirstMail(Integer firstMail) {
    this.firstMail = firstMail;
  }

  @Column(name = "LOGIN_MAIL")
  public Integer getLoginMail() {
    return loginMail;
  }

  public void setLoginMail(Integer loginMail) {
    this.loginMail = loginMail;
  }

  /**
   * @return the isVerify
   */
  @Column(name = "IS_VERIFY")
  public Integer getIsVerify() {
    return isVerify;
  }

  /**
   * @param isVerify the isVerify to set
   */
  public void setIsVerify(Integer isVerify) {
    this.isVerify = isVerify;
  }

  @Transient
  public String getZhPsnName() {
    return zhPsnName;
  }

  @Transient
  public String getEnPsnName() {
    return enPsnName;
  }

  public void setZhPsnName(String zhPsnName) {
    this.zhPsnName = zhPsnName;
  }

  public void setEnPsnName(String enPsnName) {
    this.enPsnName = enPsnName;
  }

  @Transient
  public String getDes3Id() {
    if (this.id != null && des3Id == null) {
      des3Id = ServiceUtil.encodeToDes3(this.id.toString());
    }
    return des3Id;
  }

  @Transient
  public List<PersonEmail> getPersonEmails() {
    return personEmails;
  }

  @Transient
  public String getLoginName() {
    return loginName;
  }

  public void setPersonEmails(List<PersonEmail> personEmails) {
    this.personEmails = personEmails;
  }

  public void setLoginName(String loginName) {
    this.loginName = loginName;
  }

  public void setDes3Id(String des3Id) {
    this.des3Id = des3Id;
  }

  @Transient
  public String getMsg() {
    return msg;
  }

  public void setMsg(String msg) {
    this.msg = msg;
  }

  @Transient
  public String getConfirmResult() {
    return confirmResult;
  }

  public void setConfirmResult(String confirmResult) {
    this.confirmResult = confirmResult;
  }

}
