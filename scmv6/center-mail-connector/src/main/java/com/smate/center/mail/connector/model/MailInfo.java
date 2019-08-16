package com.smate.center.mail.connector.model;

/**
 * 发送邮件封装类
 * 
 * @author Administrator
 *
 */
public class MailInfo {

  private Long mailId;
  private String receiver; // 接收者邮箱
  private String subject; // 邮件主题
  private String account; // 账号
  private String password; // 密码
  private String senderName; // 发送者名字
  private int port; // 端口号
  private String host;
  private String content;

  public Long getMailId() {
    return mailId;
  }

  public void setMailId(Long mailId) {
    this.mailId = mailId;
  }

  public String getReceiver() {
    return receiver;
  }

  public void setReceiver(String receiver) {
    this.receiver = receiver;
  }

  public String getSubject() {
    return subject;
  }

  public void setSubject(String subject) {
    this.subject = subject;
  }

  public String getAccount() {
    return account;
  }

  public void setAccount(String account) {
    this.account = account;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public int getPort() {
    return port;
  }

  public void setPort(int port) {
    this.port = port;
  }

  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content = content;
  }

  public String getHost() {
    return host;
  }

  public void setHost(String host) {
    this.host = host;
  }

  public String getSenderName() {
    return senderName;
  }

  public void setSenderName(String senderName) {
    this.senderName = senderName;
  }

  @Override
  public String toString() {
    return "MailInfo [mailId=" + mailId + ", receiver=" + receiver + ", subject=" + subject + ", account=" + account
        + ", password=" + password + ", senderName=" + senderName + ", port=" + port + ", host=" + host + "]";
  }

}
