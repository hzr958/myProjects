package com.smate.center.mail.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 邮件发送者 信息表
 * 
 * @author tsz
 *
 */
@Entity
@Table(name = "V_MAIL_SENDER")
public class MailSender {

  @Id
  @Column(name = "ID")
  private Long id; // 主键
  @Column(name = "ACCOUNT")
  private String account; // 账号
  @Column(name = "PASSWORD")
  private String password; // 密码
  @Column(name = "PORT")
  private int port; // 端口号
  @Column(name = "HOST")
  private String host;
  @Column(name = "PRIOR_TEMPLATE_CODE")
  private String priorTemplateCode; // 优先发送的模板 可多个 逗号分割
  @Column(name = "PRIOR_EMAIL")
  private String priorEmail; // 优先发送的邮箱 可多个 逗号 分割
  @Column(name = "PRIOR_CLIENT")
  private String priorClient; // 优先发送 的客户端
  @Column(name = "MAX_MAIL_COUNT")
  private int maxMailCount; // 最多发送限制
  @Column(name = "TODAY_MAIL_COUNT")
  private int todayMailCount; // 今天 发送统计
  @Column(name = "STATUS")
  private int status; // 状态 0为可用 1为不可用 9为超过发送限制（会自动更新）
  @Column(name = "CREATE_DATE")
  private Date createDate;// 创建时间
  @Column(name = "UPDATE_DATE")
  private Date updateDate; // 更新时间
  @Column(name = "MSG")
  private String msg; // 描述

  public MailSender() {
    super();
  }

  public MailSender(Long id, String account, String password, String host) {
    super();
    this.id = id;
    this.account = account;
    this.password = password;
    this.host = host;
  }

  public MailSender(Long id, String account, String password, String priorTemplateCode, String priorEmail,
      String priorClient, int maxMailCount, int todayMailCount, int status, Date createDate, Date updateDate,
      String msg) {
    super();
    this.id = id;
    this.account = account;
    this.password = password;
    this.priorTemplateCode = priorTemplateCode;
    this.priorEmail = priorEmail;
    this.priorClient = priorClient;
    this.maxMailCount = maxMailCount;
    this.todayMailCount = todayMailCount;
    this.status = status;
    this.createDate = createDate;
    this.updateDate = updateDate;
    this.msg = msg;
  }

  public String getHost() {
    return host;
  }

  public void setHost(String host) {
    this.host = host;
  }

  public int getPort() {
    return port;
  }

  public void setPort(int port) {
    this.port = port;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
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

  public String getPriorTemplateCode() {
    return priorTemplateCode;
  }

  public void setPriorTemplateCode(String priorTemplateCode) {
    this.priorTemplateCode = priorTemplateCode;
  }

  public String getPriorEmail() {
    return priorEmail;
  }

  public void setPriorEmail(String priorEmail) {
    this.priorEmail = priorEmail;
  }

  public String getPriorClient() {
    return priorClient;
  }

  public void setPriorClient(String priorClient) {
    this.priorClient = priorClient;
  }

  public int getMaxMailCount() {
    return maxMailCount;
  }

  public void setMaxMailCount(int maxMailCount) {
    this.maxMailCount = maxMailCount;
  }

  public int getTodayMailCount() {
    return todayMailCount;
  }

  public void setTodayMailCount(int todayMailCount) {
    this.todayMailCount = todayMailCount;
  }

  public int getStatus() {
    return status;
  }

  public void setStatus(int status) {
    this.status = status;
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

  public String getMsg() {
    return msg;
  }

  public void setMsg(String msg) {
    this.msg = msg;
  }

  @Override
  public String toString() {
    return "MailSender [id=" + id + ", account=" + account + ", password=" + password + ", priorTemplateCode="
        + priorTemplateCode + ", priorEmail=" + priorEmail + ", priorClient=" + priorClient + ", maxMailCount="
        + maxMailCount + ", todayMailCount=" + todayMailCount + ", status=" + status + ", createDate=" + createDate
        + ", updateDate=" + updateDate + ", msg=" + msg + "]";
  }

}
