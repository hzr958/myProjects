package com.smate.web.management.model.psn;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;


/**
 * 邮件日志表.
 * 
 * @author zyx
 * 
 */
@Entity
@Table(name = "MAIL_LOG")
public class MailLog implements Serializable {

  private static final long serialVersionUID = -1134936653787351245L;

  private Long id;
  // 发送地址
  private String fromAddr;
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
  // 发送状态，0等待发送(默认)，1发送成功，2发送失败, -3待审核
  private Integer status;
  // 发送次数
  private Integer count;
  // 创建时间
  private Date createDate;
  // 最后发送时间
  private Date lastSend;
  // 错误信息，如果长度超过2000，则截取
  private String errorMsg;
  // 邮件模板代码.
  private String templateCode;
  // 邮件发送优先级
  private Integer priorCode;
  // 服务器节点
  private Integer node;
  // 是否锁定(0，未锁定；1，锁定)
  private Integer isLock;
  // 被锁定的时间
  private Date lockDate;
  // 锁定该条记录的客户端唯一标识符
  private String locker;

  private String senderName;
  private String des3Id;// 加密字符串由mail_id+”|”+时间组成

  public MailLog() {
    super();
  }

  public MailLog(Long id, Integer isLock, Date lockDate, String locker) {
    super();
    this.id = id;
    this.isLock = isLock;
    this.lockDate = lockDate;
    this.locker = locker;
  }

  /**
   * 用于在获取发送邮件的必需信息时构造的对象.
   * 
   * @param id
   * @param fromAddr
   * @param toAddr
   * @param subject
   * @param template
   * @param fileNames
   * @param attachment
   * @param psnId
   */
  public MailLog(Long id, String fromAddr, String toAddr, String subject, String template, String fileNames,
      String attachment, String senderName, Long psnId) {
    this.id = id;
    this.fromAddr = fromAddr;
    this.toAddr = toAddr;
    this.subject = subject;
    this.template = template;
    this.fileNames = fileNames;
    this.attachment = attachment;
    this.psnId = psnId;
    this.senderName = senderName;
  }

  /**
   * 用于在查看邮件内容的必需信息时构造的对象.
   * 
   * @param id
   * @param fromAddr
   * @param toAddr
   * @param subject
   * @param template
   * @param fileNames
   * @param attachment
   * @param psnId
   * @param context
   */
  public MailLog(Long id, String fromAddr, String toAddr, String subject, String template, String fileNames,
      String attachment, Long psnId, String context) {
    this.id = id;
    this.fromAddr = fromAddr;
    this.toAddr = toAddr;
    this.subject = subject;
    this.template = template;
    this.fileNames = fileNames;
    this.attachment = attachment;
    this.psnId = psnId;
    this.context = context;
  }

  public MailLog(Long id, String toAddr, String template, Date lastSend, Integer status) {
    super();
    this.id = id;
    this.toAddr = toAddr;
    this.template = template;
    this.lastSend = lastSend;
    this.status = status;

  }

  @Id
  @Column(name = "MAIL_ID")
  /*
   * @SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_MAIL_LOG", allocationSize = 1)
   * 
   * @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
   */
  public Long getId() {
    return id;
  }

  @Column(name = "FROM_ADDRESS")
  public String getFromAddr() {
    return fromAddr;
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

  @Column(name = "COUNT")
  public Integer getCount() {
    return count;
  }

  @Column(name = "CREATE_DATE")
  public Date getCreateDate() {
    return createDate;
  }

  @Column(name = "LAST_SEND")
  public Date getLastSend() {
    return lastSend;
  }

  @Transient
  public String getErrorMsg() {
    return errorMsg;
  }

  @Column(name = "TEMPLATE_CODE")
  public String getTemplateCode() {
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

  @Column(name = "IS_LOCK")
  public Integer getIsLock() {
    return isLock;
  }

  @Column(name = "LOCK_DATE")
  public Date getLockDate() {
    return lockDate;
  }

  @Column(name = "LOCKER")
  public String getLocker() {
    return locker;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public void setFromAddr(String fromAddr) {
    this.fromAddr = fromAddr;
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

  public void setCount(Integer count) {
    this.count = count;
  }

  public void setCreateDate(Date createDate) {
    this.createDate = createDate;
  }

  public void setLastSend(Date lastSend) {
    this.lastSend = lastSend;
  }

  public void setErrorMsg(String errorMsg) {
    this.errorMsg = errorMsg;
  }

  public void setTemplateCode(String templateCode) {
    this.templateCode = templateCode;
  }

  public void setPriorCode(Integer priorCode) {
    this.priorCode = priorCode;
  }

  public void setNode(Integer node) {
    this.node = node;
  }

  public void setIsLock(Integer isLock) {
    this.isLock = isLock;
  }

  public void setLockDate(Date lockDate) {
    this.lockDate = lockDate;
  }

  public void setLocker(String locker) {
    this.locker = locker;
  }

  @Column(name = "SENDER_NAME")
  public String getSenderName() {
    return senderName;
  }

  public void setSenderName(String senderName) {
    this.senderName = senderName;
  }

  @Transient
  public String getDes3Id() {
    return des3Id;
  }

  public void setDes3Id(String des3Id) {
    this.des3Id = des3Id;
  }


}
