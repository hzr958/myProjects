package com.smate.center.batch.model.mail.emailsrv;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * 邮件数据备份表
 * 
 * @author zk
 * 
 */

@Entity
@Table(name = "MAIL_LOG_HIS")
public class MailLogHis implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = 5187061067868322011L;
  private Long id;
  // 收件地址
  private String toAddr;
  // 邮件标题
  private String subject;
  // 模板文件名
  private String template;
  // 邮件参数，JSON格式
  private String context;
  // 附件名称，使用(\n)分隔
  private String fileNames;
  // 附件地址，使用(\n)逗号分隔
  private String attachment;
  // 发送人员ID
  private Long psnId;
  // 发送状态，0等待发送(系统邮件)，1发送成功(系统邮件)，5初始状态(推广邮件审核状态),6可发送状态(推广邮件),7发送成功(推广邮件)
  private Integer status;
  // 创建时间
  private Date createDate;
  // 模板编号,对应maildispath.mail_template_info.temp_code
  private Integer templateCode;
  // 邮件发送优先级
  private Integer priorCode;
  // 服务器节点
  private Integer node;
  private String senderName;

  public MailLogHis() {
    super();
  }

  @Id
  @Column(name = "MAIL_ID")
  public Long getId() {
    return id;
  }

  @Column(name = "TO_ADDRESS")
  public String getToAddr() {
    return toAddr;
  }

  @Column(name = "SUBJECT")
  public String getSubject() {
    return subject;
  }

  @Column(name = "TEMPLATE")
  public String getTemplate() {
    return template;
  }

  @Transient
  public String getContext() {
    return context;
  }

  @Column(name = "FILENAMES")
  public String getFileNames() {
    return fileNames;
  }

  @Column(name = "ATTACHMENTS")
  public String getAttachment() {
    return attachment;
  }

  @Column(name = "PERSON_ID")
  public Long getPsnId() {
    return psnId;
  }

  @Column(name = "STATUS")
  public Integer getStatus() {
    return status;
  }

  @Column(name = "CREATE_DATE")
  public Date getCreateDate() {
    return createDate;
  }

  @Column(name = "TEMPLATE_CODE")
  public Integer getTemplateCode() {
    return templateCode;
  }

  @Column(name = "PRIOR_CODE")
  public Integer getPriorCode() {
    return priorCode;
  }

  @Column(name = "NODE")
  public Integer getNode() {
    return node;
  }

  @Column(name = "sender_name")
  public String getSenderName() {
    return senderName;
  }

  public void setSenderName(String senderName) {
    this.senderName = senderName;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public void setToAddr(String toAddr) {
    this.toAddr = toAddr;
  }

  public void setSubject(String subject) {
    this.subject = subject;
  }

  public void setTemplate(String template) {
    this.template = template;
  }

  public void setContext(String context) {
    this.context = context;
  }

  public void setFileNames(String fileNames) {
    this.fileNames = fileNames;
  }

  public void setAttachment(String attachment) {
    this.attachment = attachment;
  }

  public void setPsnId(Long psnId) {
    this.psnId = psnId;
  }

  public void setStatus(Integer status) {
    this.status = status;
  }

  public void setCreateDate(Date createDate) {
    this.createDate = createDate;
  }

  public void setTemplateCode(Integer templateCode) {
    this.templateCode = templateCode;
  }

  public void setPriorCode(Integer priorCode) {
    this.priorCode = priorCode;
  }

  public void setNode(Integer node) {
    this.node = node;
  }

}
