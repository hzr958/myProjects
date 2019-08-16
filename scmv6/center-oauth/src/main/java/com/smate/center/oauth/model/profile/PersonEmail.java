package com.smate.center.oauth.model.profile;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.smate.core.base.utils.model.security.Person;

/**
 * 
 * 个人电子邮件.
 * 
 * @author lichangwen
 * 
 */
@Entity
@Table(name = "PSN_EMAIL")
public class PersonEmail implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = 1076564248483204916L;
  /**
   * 主健.
   */
  private Long id;
  /**
   * 邮件所属用户.
   */
  private Person person;
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
  private boolean firstMail;

  /**
   * 是否为登录邮件
   */
  private boolean loginMail;
  /**
   * 是否是校验过的邮件.
   */
  private boolean verify;

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

  /**
   * @param id the id to set
   */
  public void setId(Long id) {
    this.id = id;
  }

  /**
   * @return the person
   */

  @ManyToOne(cascade = {CascadeType.ALL})
  @JoinColumn(name = "PSN_ID", insertable = true, updatable = true)
  public Person getPerson() {
    return person;
  }

  /**
   * @param person the person to set
   */
  public void setPerson(Person person) {
    this.person = person;
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
  public boolean isFirstMail() {
    return firstMail;
  }

  /**
   * @param firstMail the firstMail to set
   */
  public void setFirstMail(boolean firstMail) {
    this.firstMail = firstMail;
  }

  @Column(name = "LOGIN_MAIL")
  public boolean isLoginMail() {
    return loginMail;
  }

  public void setLoginMail(boolean isLoginMail) {
    this.loginMail = isLoginMail;
  }

  @Column(name = "IS_VERIFY")
  public boolean isVerify() {
    return verify;
  }

  public void setVerify(boolean verify) {
    this.verify = verify;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

}
