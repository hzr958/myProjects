package com.smate.web.management.model.mail;

import java.util.Date;
import java.util.List;

import com.smate.center.mail.connector.model.MailTemplate;
import com.smate.core.base.utils.model.Page;

public class MailTemplateForm {
  private List<MailTemplate> mailTemplateList;
  private MailTemplate mailTemplate;
  private Page<MailTemplate> page;
  private Integer templateCode;// 模版标识
  private String templateName;// 模版名字
  private Long mailTypeId;// 模版类型Id
  private String subject_zh;// 中文主题，如果有参数写成占位表达式
  private String subject_en;// 英文主题，如果有参数写成占位表达式
  private Integer status;// 状态 0=可用 1=不可用
  private Date createDate;// 创建时间
  private String msg;// 描述
  private String statusMsg;
  private String subject;
  private Integer limitStatus;
  private String priorLevel;// 优先级别

  public Integer getLimitStatus() {
    return limitStatus;
  }

  public void setLimitStatus(Integer limitStatus) {
    this.limitStatus = limitStatus;
  }

  public List<MailTemplate> getMailTemplateList() {
    return mailTemplateList;
  }

  public void setMailTemplateList(List<MailTemplate> mailTemplateList) {
    this.mailTemplateList = mailTemplateList;
  }

  public Page<MailTemplate> getPage() {
    return page;
  }

  public void setPage(Page<MailTemplate> page) {
    this.page = page;
  }

  public Integer getTemplateCode() {
    return templateCode;
  }

  public void setTemplateCode(Integer templateCode) {
    this.templateCode = templateCode;
  }

  public String getTemplateName() {
    return templateName;
  }

  public void setTemplateName(String templateName) {
    this.templateName = templateName;
  }

  public Long getMailTypeId() {
    return mailTypeId;
  }

  public void setMailTypeId(Long mailTypeId) {
    this.mailTypeId = mailTypeId;
  }

  public String getSubject_zh() {
    return subject_zh;
  }

  public void setSubject_zh(String subject_zh) {
    this.subject_zh = subject_zh;
  }

  public String getSubject_en() {
    return subject_en;
  }

  public void setSubject_en(String subject_en) {
    this.subject_en = subject_en;
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

  public String getMsg() {
    return msg;
  }

  public void setMsg(String msg) {
    this.msg = msg;
  }

  public MailTemplate getMailTemplate() {
    if (mailTemplate == null) {
      mailTemplate = new MailTemplate();
      mailTemplate.setTemplateCode(templateCode);
      mailTemplate.setTemplateName(templateName);
      mailTemplate.setMailTypeId(mailTypeId);
      mailTemplate.setSubject_zh(subject_zh);
      mailTemplate.setSubject_en(subject_en);
      mailTemplate.setCreateDate(createDate);
      mailTemplate.setStatus(status);
      mailTemplate.setLimitStatus(limitStatus);
      mailTemplate.setMsg(msg);
      mailTemplate.setPriorLevel(priorLevel);
    }
    return mailTemplate;
  }

  public void setMailTemplate(MailTemplate mailTemplate) {
    this.mailTemplate = mailTemplate;
  }

  public String getStatusMsg() {
    return statusMsg;
  }

  public void setStatusMsg(String statusMsg) {
    this.statusMsg = statusMsg;
  }

  public String getSubject() {
    return subject;
  }

  public void setSubject(String subject) {
    this.subject = subject;
  }

  public String getPriorLevel() {
    return priorLevel;
  }

  public void setPriorLevel(String priorLevel) {
    this.priorLevel = priorLevel;
  }

}
