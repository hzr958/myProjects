package com.smate.center.mail.model;

import java.util.List;
import java.util.Map;

import com.smate.center.mail.connector.model.MailOriginalData;
import com.smate.center.mail.connector.model.MailTemplate;
import com.smate.center.mail.connector.mongodb.model.MailContent;
import com.smate.core.base.mail.model.mongodb.MailLink;

/**
 * 构建邮件数据Info
 * 
 * @author zzx
 *
 */
public class MailDataInfo {
  private MailOriginalData mailOriginalData;
  private String mailData;
  private MailContent mailContent;
  private String emailContent;
  private MailRecord mailRecord;
  private MailTemplate mailTemplate;
  private List<MailLink> mailLinkList;
  private Map<String, Object> mailDataMap;
  private String des3MailTypeId;
  private String des3ReceiverPsnId;
  private String emailLanguageVersion = "";



  public String getMailData() {
    return mailData;
  }

  public void setMailData(String mailData) {
    this.mailData = mailData;
  }

  public MailOriginalData getMailOriginalData() {
    return mailOriginalData;
  }

  public void setMailOriginalData(MailOriginalData mailOriginalData) {
    this.mailOriginalData = mailOriginalData;
  }

  public MailContent getMailContent() {
    return mailContent;
  }

  public void setMailContent(MailContent mailContent) {
    this.mailContent = mailContent;
  }

  public String getEmailContent() {
    return emailContent;
  }

  public void setEmailContent(String emailContent) {
    this.emailContent = emailContent;
  }

  public MailRecord getMailRecord() {
    return mailRecord;
  }

  public void setMailRecord(MailRecord mailRecord) {
    this.mailRecord = mailRecord;
  }

  public List<MailLink> getMailLinkList() {
    return mailLinkList;
  }

  public void setMailLinkList(List<MailLink> mailLinkList) {
    this.mailLinkList = mailLinkList;
  }

  public Map<String, Object> getMailDataMap() {
    return mailDataMap;
  }

  public void setMailDataMap(Map<String, Object> mailDataMap) {
    this.mailDataMap = mailDataMap;
  }

  public MailTemplate getMailTemplate() {
    return mailTemplate;
  }

  public void setMailTemplate(MailTemplate mailTemplate) {
    this.mailTemplate = mailTemplate;
  }

  public String getDes3MailTypeId() {
    return des3MailTypeId;
  }

  public void setDes3MailTypeId(String des3MailTypeId) {
    this.des3MailTypeId = des3MailTypeId;
  }

  public String getDes3ReceiverPsnId() {
    return des3ReceiverPsnId;
  }

  public void setDes3ReceiverPsnId(String des3ReceiverPsnId) {
    this.des3ReceiverPsnId = des3ReceiverPsnId;
  }

  public String getEmailLanguageVersion() {
    return emailLanguageVersion;
  }

  public void setEmailLanguageVersion(String emailLanguageVersion) {
    this.emailLanguageVersion = emailLanguageVersion;
  }


}
