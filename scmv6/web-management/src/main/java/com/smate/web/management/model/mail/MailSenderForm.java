package com.smate.web.management.model.mail;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.smate.core.base.utils.model.Page;

public class MailSenderForm {
  private List<MailSender> mailSenderList;
  private MailSender mailSender;
  private Page<MailSender> page;
  private Long id;
  private String account;
  private int port; // 端口号
  private String host;
  private String priorTemplateCode; // 优先发送的模板 可多个 逗号分割
  private String priorEmail; // 优先发送的邮箱 可多个 逗号 分割
  private String priorClient; // 优先发送 的客户端
  private int maxMailCount; // 最多发送限制
  private int todayMailCount; // 今天 发送统计
  private Integer status; // 状态 0为可用 1为不可用 9为超过发送限制（会自动更新）
  private Date createDate;// 创建时间
  private Date updateDate; // 更新时间
  private String msg; // 描述
  private Map<String, String> resultMap;

  public List<MailSender> getMailSenderList() {
    return mailSenderList;
  }

  public void setMailSenderList(List<MailSender> mailSenderList) {
    this.mailSenderList = mailSenderList;
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

  public int getPort() {
    return port;
  }

  public void setPort(int port) {
    this.port = port;
  }

  public String getHost() {
    return host;
  }

  public void setHost(String host) {
    this.host = host;
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

  public MailSender getMailSender() {
    if (mailSender == null) {
      mailSender = new MailSender();
      mailSender.setAccount(account);
      mailSender.setId(id);
      mailSender.setPort(port);
      mailSender.setHost(host);
      mailSender.setPriorClient(priorClient);
      mailSender.setPriorEmail(priorEmail);
      mailSender.setPriorTemplateCode(priorTemplateCode);
      mailSender.setStatus(status);
      mailSender.setMsg(msg);
      mailSender.setCreateDate(createDate);
      mailSender.setUpdateDate(updateDate);
      mailSender.setMaxMailCount(maxMailCount);
    }
    return mailSender;
  }

  public void setMailSender(MailSender mailSender) {
    this.mailSender = mailSender;
  }

  public Page<MailSender> getPage() {
    return page;
  }

  public void setPage(Page<MailSender> page) {
    this.page = page;
  }

  public Map<String, String> getResultMap() {
    return resultMap;
  }

  public void setResultMap(Map<String, String> resultMap) {
    this.resultMap = resultMap;
  }

}
