package com.smate.center.mail.model;

import java.util.List;

import com.smate.center.mail.connector.mongodb.model.MailContent;

/**
 * 邮件调度 封装类
 * 
 * @author tsz
 *
 */
public class MailDispatchInfo {

  private MailRecord mailRecord;

  private List<MailSender> mailSenderList;

  private List<MailClient> mailClientList;

  private MailContent mailContent;

  public MailDispatchInfo() {
    super();
  }

  public MailDispatchInfo(MailRecord mailRecord) {
    super();
    this.mailRecord = mailRecord;
  }

  public MailDispatchInfo(MailRecord mailRecord, MailContent mailContent) {
    super();
    this.mailRecord = mailRecord;
    this.mailContent = mailContent;
  }

  public MailDispatchInfo(MailRecord mailRecord, List<MailSender> mailSenderList, List<MailClient> mailClientList) {
    super();
    this.mailRecord = mailRecord;
    this.mailSenderList = mailSenderList;
    this.mailClientList = mailClientList;
  }

  public MailRecord getMailRecord() {
    return mailRecord;
  }

  public void setMailRecord(MailRecord mailRecord) {
    this.mailRecord = mailRecord;
  }

  public List<MailSender> getMailSenderList() {
    return mailSenderList;
  }

  public void setMailSenderList(List<MailSender> mailSenderList) {
    this.mailSenderList = mailSenderList;
  }

  public List<MailClient> getMailClientList() {
    return mailClientList;
  }

  public void setMailClientList(List<MailClient> mailClientList) {
    this.mailClientList = mailClientList;
  }

  public MailContent getMailContent() {
    return mailContent;
  }

  public void setMailContent(MailContent mailContent) {
    this.mailContent = mailContent;
  }

}
