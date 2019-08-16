package com.smate.center.mail.connector.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 邮件模版记录表
 * 
 * @author zzx
 *
 */
@Entity
@Table(name = "V_MAIL_TEMPLATE")
public class MailTemplate implements Serializable {
  private static final long serialVersionUID = 8933255924929871541L;
  @Id
  @Column(name = "TEMPLATE_CODE")
  private Integer templateCode;// 模版标识
  @Column(name = "TEMPLATE_NAME")
  private String templateName;// 模版名字
  @Column(name = "MAIL_TYPE_ID")
  private Long mailTypeId;// 模版类型Id
  @Column(name = "SUBJECT_ZH")
  private String subject_zh;// 中文主题，如果有参数写成占位表达式
  @Column(name = "SUBJECT_EN")
  private String subject_en;// 英文主题，如果有参数写成占位表达式
  @Column(name = "STATUS")
  private Integer status;// 状态 0=可用 1=不可用
  @Column(name = "CREATE_DATE")
  private Date createDate;// 创建时间
  @Column(name = "UPDATE_DATE")
  private Date updateDate;// 更新时间
  @Column(name = "MSG")
  private String msg;// 描述
  @Column(name = "LIMIT_STATUS")
  private Integer limitStatus;// 状态 0=无限制 1=每天对一个邮箱发一封 2=每周对一个邮箱发一封
  // 3=每月对一个邮箱发一封
  @Column(name = "PRIOR_LEVEL")
  private String priorLevel;// 优先级别
  @Column(name = "TEMPLATE_LANGUAGE")
  private Integer templateLanguage;// 模板语言：0.中英文模板都有，1.只有中文模板

  public MailTemplate(Integer templateCode, String templateName, Long mailTypeId, String subject_zh, String subject_en,
      Integer status, Date createDate, Date updateDate, String msg, Integer limitStatus, String priorLevel) {
    super();
    this.templateCode = templateCode;
    this.templateName = templateName;
    this.mailTypeId = mailTypeId;
    this.subject_zh = subject_zh;
    this.subject_en = subject_en;
    this.status = status;
    this.createDate = createDate;
    this.updateDate = updateDate;
    this.msg = msg;
    this.limitStatus = limitStatus;
    this.priorLevel = priorLevel;
  }

  public Integer getLimitStatus() {
    return limitStatus;
  }

  public void setLimitStatus(Integer limitStatus) {
    this.limitStatus = limitStatus;
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

  public static long getSerialversionuid() {
    return serialVersionUID;
  }

  public MailTemplate() {
    super();
  }

  public Long getMailTypeId() {
    return mailTypeId;
  }

  public void setMailTypeId(Long mailTypeId) {
    this.mailTypeId = mailTypeId;
  }

  public String getPriorLevel() {
    return priorLevel;
  }

  public void setPriorLevel(String priorLevel) {
    this.priorLevel = priorLevel;
  }

  public Integer getTemplateLanguage() {
    return templateLanguage;
  }

  public void setTemplateLanguage(Integer templateLanguage) {
    this.templateLanguage = templateLanguage;
  }

}
