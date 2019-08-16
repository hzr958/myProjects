package com.smate.web.management.model.mongodb.mail;

import java.util.Date;

import javax.persistence.Id;

import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * 邮件发送失败、退信记录实体类
 * 
 * @author zzx
 *
 */
@Document(collection = "V_MAIL_RETURNED_RECORD")
public class MailReturnedRecord {
  @Id
  private String id;// 主键
  @Indexed
  private String address;// 接收地址
  @Indexed
  private String account;// 发送地址
  private Date sendDate;// 发送时间
  private String subject;// 主题
  private String content;// 邮件内容
  private Date createDate;// 创建时间
  private String type;// 退信类别 0异常 1自动回复

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getAddress() {
    return address;
  }

  public void setAddress(String address) {
    this.address = address;
  }

  public String getAccount() {
    return account;
  }

  public void setAccount(String account) {
    this.account = account;
  }

  public Date getSendDate() {
    return sendDate;
  }

  public void setSendDate(Date sendDate) {
    this.sendDate = sendDate;
  }

  public String getSubject() {
    return subject;
  }

  public void setSubject(String subject) {
    this.subject = subject;
  }

  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content = content;
  }

  public Date getCreateDate() {
    return createDate;
  }

  public void setCreateDate(Date createDate) {
    this.createDate = createDate;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

}
