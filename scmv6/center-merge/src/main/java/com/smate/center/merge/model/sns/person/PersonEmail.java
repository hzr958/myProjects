package com.smate.center.merge.model.sns.person;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * 
 * 个人电子邮件.
 * 
 * @author zk
 * 
 */
@Entity
@Table(name = "PSN_EMAIL")
public class PersonEmail implements Serializable {

  private static final long serialVersionUID = 8415937191247237798L;
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

  public PersonEmail() {
    super();
  }

  public PersonEmail(Long psnId, String email) {
    this.psnId = psnId;
    this.email = email;
  }

  /**
   * @return the id
   */
  @Id
  @Column(name = "ID")
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

}
