package com.smate.web.psn.form;

import com.smate.core.base.utils.model.security.Person;


public class PersonEmailInfo {
  private Long id;
  private Person person;
  private String email;

  private String leftPart;
  private String rightPart;
  private Long firstMail;
  private Long loginMail;
  private Long isVerify;
  private Boolean resend = false;
  private Long sendDate = 0L; // 发送邮件的时间，单位毫秒
  private Long delaySendDate = 0L; // 延迟发送的时间,单位秒

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Person getPerson() {
    return person;
  }

  public void setPerson(Person person) {
    this.person = person;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getLeftPart() {
    return leftPart;
  }

  public void setLeftPart(String leftPart) {
    this.leftPart = leftPart;
  }

  public String getRightPart() {
    return rightPart;
  }

  public void setRightPart(String rightPart) {
    this.rightPart = rightPart;
  }

  public Long getFirstMail() {
    return firstMail;
  }

  public void setFirstMail(Long firstMail) {
    this.firstMail = firstMail;
  }

  public Long getLoginMail() {
    return loginMail;
  }

  public void setLoginMail(Long loginMail) {
    this.loginMail = loginMail;
  }

  public Long getIsVerify() {
    return isVerify;
  }

  public void setIsVerify(Long isVerify) {
    this.isVerify = isVerify;
  }

  public Boolean getResend() {
    return resend;
  }

  public void setResend(Boolean resend) {
    this.resend = resend;
  }

  public Long getSendDate() {
    return sendDate;
  }

  public void setSendDate(Long sendDate) {
    this.sendDate = sendDate;
  }

  public Long getDelaySendDate() {
    return delaySendDate;
  }

  public void setDelaySendDate(Long delaySendDate) {
    this.delaySendDate = delaySendDate;
  }



}
