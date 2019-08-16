package com.smate.web.management.model.mail;

import java.util.Date;
import java.util.List;

import com.smate.core.base.utils.model.Page;

public class MailClientForm {
  private List<MailClient> mailClientList;
  private MailClient mailClient;
  private Page<MailClient> page;
  private Long id;
  private String clientName; // 名字
  private Integer status; // 状态 0可用 1不可用
  private String priorTemplate;
  private String priorEmail;
  private String priorSenderAccount;
  private Date createDate; // 创建时间
  private Date updateDate; // 更新时间
  private String msg; // 描述
  private Integer waitTime;// 等待时间

  public List<MailClient> getMailClientList() {
    return mailClientList;
  }

  public void setMailClientList(List<MailClient> mailClientList) {
    this.mailClientList = mailClientList;
  }

  public MailClient getMailClient() {
    if (mailClient == null) {
      mailClient = new MailClient();
      mailClient.setId(id);
      mailClient.setClientName(clientName);
      mailClient.setPriorEmail(priorEmail);
      mailClient.setPriorSenderAccount(priorSenderAccount);
      mailClient.setPriorTemplate(priorTemplate);
      mailClient.setStatus(status);
      mailClient.setCreateDate(createDate);
      mailClient.setUpdateDate(updateDate);
      mailClient.setWaitTime(waitTime);
      mailClient.setMsg(msg);
    }
    return mailClient;
  }

  public void setMailClient(MailClient mailClient) {
    this.mailClient = mailClient;
  }

  public Page<MailClient> getPage() {
    return page;
  }

  public void setPage(Page<MailClient> page) {
    this.page = page;
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

  public String getPriorTemplate() {
    return priorTemplate;
  }

  public void setPriorTemplate(String priorTemplate) {
    this.priorTemplate = priorTemplate;
  }

  public String getPriorEmail() {
    return priorEmail;
  }

  public void setPriorEmail(String priorEmail) {
    this.priorEmail = priorEmail;
  }

  public String getPriorSenderAccount() {
    return priorSenderAccount;
  }

  public void setPriorSenderAccount(String priorSenderAccount) {
    this.priorSenderAccount = priorSenderAccount;
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

  public Integer getWaitTime() {
    return waitTime;
  }

  public void setWaitTime(Integer waitTime) {
    this.waitTime = waitTime;
  }
}
