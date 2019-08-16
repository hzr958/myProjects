package com.smate.center.mail.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 待发送邮件日记表.
 * 
 * @author zzx
 * 
 */
@Entity
@Table(name = "V_MAIL_RECORD_HIS")
public class MailRecordLog implements Serializable {
  private static final long serialVersionUID = 1L;
  @Id
  @Column(name = "MAIL_ID")
  private Long mailId; // 邮件id
  @Column(name = "SENDER")
  private String sender; // 发送者邮箱
  @Column(name = "RECEIVER")
  private String receiver; // 接收者邮箱
  @Column(name = "MAIL_CLIENT")
  private String mailClient; // 发送客户端
  @Column(name = "SUBJECT")
  private String subject; // 邮件主题
  @Column(name = "SENDER_NAME")
  private String senderName; // 发送者名字
  @Column(name = "MAIL_TEMPLATE_CODE")
  private int mailTemplateCode; // 邮件模板
  @Column(name = "PRIOR_LEVEL")
  private String priorLevel; // 优先级 A>B>C>D
  @Column(name = "CREATE_DATE")
  private Date createDate; // 创建时间
  @Column(name = "UPDATE_DATE")
  private Date updateDate; // 更新时间
  @Column(name = "DISTRIBUTE_DATE")
  private Date distributeDate; // 分配时间
  @Column(name = "STATUS")
  private int status; // 状态 0=待分配 1=待发送 9=调度出错 2=发送成功 3=黑名单
  // 4=receiver不存在,5邮件不在白名单 ,8邮件发送出错 ,9邮件调度出错,10邮件正在发送
  // 11构造邮件发送信息出错
  @Column(name = "MSG")
  private String msg;// 描述

  public Long getMailId() {
    return mailId;
  }

  public void setMailId(Long mailId) {
    this.mailId = mailId;
  }

  public String getSender() {
    return sender;
  }

  public void setSender(String sender) {
    this.sender = sender;
  }

  public String getReceiver() {
    return receiver;
  }

  public void setReceiver(String receiver) {
    this.receiver = receiver;
  }

  public String getMailClient() {
    return mailClient;
  }

  public void setMailClient(String mailClient) {
    this.mailClient = mailClient;
  }

  public String getSubject() {
    return subject;
  }

  public void setSubject(String subject) {
    this.subject = subject;
  }

  public String getSenderName() {
    return senderName;
  }

  public void setSenderName(String senderName) {
    this.senderName = senderName;
  }

  public int getMailTemplateCode() {
    return mailTemplateCode;
  }

  public void setMailTemplateCode(int mailTemplateCode) {
    this.mailTemplateCode = mailTemplateCode;
  }

  public String getPriorLevel() {
    return priorLevel;
  }

  public void setPriorLevel(String priorLevel) {
    this.priorLevel = priorLevel;
  }

  public Date getCreateDate() {
    return createDate;
  }

  public void setCreateDate(Date createDate) {
    this.createDate = createDate;
  }

  public Date getUpdateDate() {
    return updateDate;
  }

  public void setUpdateDate(Date updateDate) {
    this.updateDate = updateDate;
  }

  public Date getDistributeDate() {
    return distributeDate;
  }

  public void setDistributeDate(Date distributeDate) {
    this.distributeDate = distributeDate;
  }

  public int getStatus() {
    return status;
  }

  public void setStatus(int status) {
    this.status = status;
  }

  public String getMsg() {
    return msg;
  }

  public void setMsg(String msg) {
    this.msg = msg;
  }

}
