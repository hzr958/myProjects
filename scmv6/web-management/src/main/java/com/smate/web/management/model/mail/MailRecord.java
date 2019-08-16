package com.smate.web.management.model.mail;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 待发送邮件表.
 * 
 * @author tsz
 * 
 */
@Entity
@Table(name = "V_MAIL_RECORD")
public class MailRecord {

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
  private int status; // 状态 0=待分配 1=待发送 9=调度出错 2=发送成功 3=黑名单 4=receiver不存在
  @Column(name = "MSG")
  private String msg;// 描述

  public MailRecord() {
    super();
  }

  public MailRecord(Long mailId, String sender, String receiver, String mailClient, String subject,
      int mailTemplateCode, String priorLevel, Date createDate, Date updateDate, Date distributeDate, int status,
      String msg) {
    super();
    this.mailId = mailId;
    this.sender = sender;
    this.receiver = receiver;
    this.mailClient = mailClient;
    this.subject = subject;
    this.mailTemplateCode = mailTemplateCode;
    this.priorLevel = priorLevel;
    this.createDate = createDate;
    this.updateDate = updateDate;
    this.distributeDate = distributeDate;
    this.status = status;
    this.msg = msg;
  }


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

  @Override
  public String toString() {
    return "MailRecord [mailId=" + mailId + ", sender=" + sender + ", receiver=" + receiver + ", mailClient="
        + mailClient + ", subject=" + subject + ", mailTemplateCode=" + mailTemplateCode + ", priorLevel=" + priorLevel
        + ", createDate=" + createDate + ", updateDate=" + updateDate + ", distributeDate=" + distributeDate
        + ", status=" + status + ", msg=" + msg + "]";
  }

}
