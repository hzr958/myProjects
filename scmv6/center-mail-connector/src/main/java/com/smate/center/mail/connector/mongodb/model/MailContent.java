package com.smate.center.mail.connector.mongodb.model;

import java.io.Serializable;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * 邮件内容
 * 
 * @author zzx
 *
 */
@Document(collection = "V_MAIL_CONTENT")
public class MailContent implements Serializable {
  private static final long serialVersionUID = 4609716080948038429L;
  @Id
  @Indexed
  private Long mailId;// 邮件Id
  private String content;// 邮件最后内容
  private String mailData;// 原始数据

  public Long getMailId() {
    return mailId;
  }

  public void setMailId(Long mailId) {
    this.mailId = mailId;
  }

  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content = content;
  }

  public String getMailData() {
    return mailData;
  }

  public void setMailData(String mailData) {
    this.mailData = mailData;
  }



}
