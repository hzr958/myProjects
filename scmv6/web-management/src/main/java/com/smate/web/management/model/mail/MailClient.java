package com.smate.web.management.model.mail;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 邮件客户端登记表
 * 
 * @author tsz
 *
 */
@Entity
@Table(name = "V_MAIL_CLIENT")
public class MailClient {

  @Id
  @Column(name = "ID")
  private Long id;
  @Column(name = "CLIENT_NAME")
  private String clientName; // 名字
  @Column(name = "STATUS")
  private Integer status; // 状态 0可用 1不可用
  @Column(name = "prior_Template")
  private String priorTemplate;
  @Column(name = "prior_Email")
  private String priorEmail;
  @Column(name = "prior_Sender_Account")
  private String priorSenderAccount;
  @Column(name = "CREATE_DATE")
  private Date createDate; // 创建时间
  @Column(name = "UPDATE_DATE")
  private Date updateDate; // 更新时间
  @Column(name = "MSG")
  private String msg; // 描述
  @Column(name = "WAIT_TIME")
  private Integer waitTime;// 等待时间

  public MailClient() {
    super();
  }

  public String getPriorEmail() {
    return priorEmail;
  }

  public void setPriorEmail(String priorEmail) {
    this.priorEmail = priorEmail;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getClientName() {
    return clientName;
  }

  public void setClientName(String clientName) {
    this.clientName = clientName;
  }

  public Integer getStatus() {
    return status;
  }

  public void setStatus(Integer status) {
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

  public String getPriorTemplate() {
    return priorTemplate;
  }

  public void setPriorTemplate(String priorTemplate) {
    this.priorTemplate = priorTemplate;
  }

  public String getPriorSenderAccount() {
    return priorSenderAccount;
  }

  public void setPriorSenderAccount(String priorSenderAccount) {
    this.priorSenderAccount = priorSenderAccount;
  }

  public Integer getWaitTime() {
    return waitTime;
  }

  public void setWaitTime(Integer waitTime) {
    this.waitTime = waitTime;
  }

}
