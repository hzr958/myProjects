package com.smate.core.base.psn.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * 账号邮箱验证记录
 * 
 * @author aijiangbin
 *
 */
@Entity
@Table(name = "V_ACCOUNT_EMAIL_CHECK_LOG")
public class AccountEmailCheckLog {

  /**
   * 主键
   */
  @Id
  @Column(name = "ID")
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_V_ACCOUNT_EMAIL_CHECK_LOG", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
  private Long id;

  /**
   * 人员id
   */
  @Column(name = "PSN_ID")
  private Long psnId;

  /**
   * 账号，账号邮箱 ， 邮件接收邮箱
   */
  @Column(name = "ACCOUNT")
  private String account;

  /**
   * 发送邮件时间
   */
  @Column(name = "SEND_TIME")
  private Date sendTime;

  /**
   * 处理状态 0=未处理 ， 1验证成功 ， 9=验证失败 ， 2=重新发送
   */
  @Column(name = "DEAL_STATUS")
  private Integer dealStatus;

  /**
   * 处理时间
   */
  @Column(name = "DEAL_TIME")
  private Date dealTime;
  /**
   * 处理类型， 1邮件链接验证， 2系统弹框验证
   */
  @Column(name = "DEAL_TYPE")
  private Integer dealType;

  /**
   * 产生验证吗 随机8位 数字或者字母
   */
  @Column(name = "VALIDATE_CODE")
  private String validateCode; //

  /**
   * 邮件地址链接专用验证 uuid+psnId+32位随机码做md5
   */
  @Column(name = "VALIDATE_CODE_BIG")
  private String validateCodeBig; //

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Long getPsnId() {
    return psnId;
  }

  public void setPsnId(Long psnId) {
    this.psnId = psnId;
  }

  public String getAccount() {
    return account;
  }

  public void setAccount(String account) {
    this.account = account;
  }

  public Date getSendTime() {
    return sendTime;
  }

  public void setSendTime(Date sendTime) {
    this.sendTime = sendTime;
  }

  public Integer getDealStatus() {
    return dealStatus;
  }

  public void setDealStatus(Integer dealStatus) {
    this.dealStatus = dealStatus;
  }

  public Date getDealTime() {
    return dealTime;
  }

  public void setDealTime(Date dealTime) {
    this.dealTime = dealTime;
  }

  public String getValidateCode() {
    return validateCode;
  }

  public void setValidateCode(String validateCode) {
    this.validateCode = validateCode;
  }

  public String getValidateCodeBig() {
    return validateCodeBig;
  }

  public void setValidateCodeBig(String validateCodeBig) {
    this.validateCodeBig = validateCodeBig;
  }



  public Integer getDealType() {
    return dealType;
  }

  public void setDealType(Integer dealType) {
    this.dealType = dealType;
  }

  public AccountEmailCheckLog() {

  }

  public AccountEmailCheckLog(Long id, Long psnId, String account, Date sendTime, Integer dealStatus, Date dealTime,
      Integer dealType, String validateCode, String validateCodeBig) {
    super();
    this.id = id;
    this.psnId = psnId;
    this.account = account;
    this.sendTime = sendTime;
    this.dealStatus = dealStatus;
    this.dealTime = dealTime;
    this.dealType = dealType;
    this.validateCode = validateCode;
    this.validateCodeBig = validateCodeBig;
  }



}
