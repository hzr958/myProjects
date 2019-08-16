package com.smate.center.batch.model.mail;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang.StringUtils;
import org.springframework.context.i18n.LocaleContextHolder;

@Entity
@Table(name = "SHARE_INBOX")
public class ShareInbox implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = 8370785636344953467L;
  // 主键
  private Long id;
  // 收件人Id
  private Long psnId;
  private Long mailId;
  /** 发送人ID. */
  private Long senderId;
  private String title;
  private String enTitle;
  private String content;
  /** 关联表PSN_RES_RECEIVE_RES中的Res_REC_ID字段. */
  private Long resRecId;
  // 共享有效期限
  private Date deadLine;
  /** 分享类型：1成果，2文献，3文件. */
  private Integer shareType;
  /** 查看分享收件箱详情模板. */
  private Integer tmpId;
  /** 扩展字段，以json格式存储. */
  private String extOtherInfo;
  private Date receiveDate;
  private String psnName;
  private String firstName;
  private String lastName;
  private String psnHeadUrl;
  // 邮件Id

  // 邮件处理状态，0--未读 1—已读 2 –已删除

  private Integer status;
  /** 是否是老数据. **/
  private Integer oldData = 0;

  /** 显示标题. */
  private String viewTitle;
  /** 去除html标签的内容. */
  private String noHtmlContent;
  /** 发送消息人名. */
  private String senderName;
  /** 发送消息人头像. */
  private String senderAvatars;

  public ShareInbox() {
    super();
  }

  public ShareInbox(Long psnId, Long mailId, Integer status) {
    super();
    this.psnId = psnId;
    this.mailId = mailId;
    this.status = status;
  }

  @Id
  @Column(name = "ID")
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_SHARE_INBOX", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  @Column(name = "PSN_ID")
  public Long getPsnId() {
    return psnId;
  }

  public void setPsnId(Long psnId) {
    this.psnId = psnId;
  }

  @Column(name = "MAIL_ID")
  public Long getMailId() {
    return mailId;
  }

  public void setMailId(Long mailId) {
    this.mailId = mailId;
  }

  @Column(name = "SENDER_ID")
  public Long getSenderId() {
    return senderId;
  }

  public void setSenderId(Long senderId) {
    this.senderId = senderId;
  }

  @Column(name = "TITLE")
  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  @Column(name = "EN_TITLE")
  public String getEnTitle() {
    return enTitle;
  }

  public void setEnTitle(String enTitle) {
    this.enTitle = enTitle;
  }

  @Column(name = "CONTENT")
  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content = content;
  }

  @Column(name = "RES_REC_ID")
  public Long getResRecId() {
    return resRecId;
  }

  public void setResRecId(Long resRecId) {
    this.resRecId = resRecId;
  }

  @Column(name = "DEADLINE")
  public Date getDeadLine() {
    return deadLine;
  }

  public void setDeadLine(Date deadLine) {
    this.deadLine = deadLine;
  }

  @Column(name = "SHARE_TYPE")
  public Integer getShareType() {
    return shareType;
  }

  public void setShareType(Integer shareType) {
    this.shareType = shareType;
  }

  @Column(name = "TMP_ID")
  public Integer getTmpId() {
    return tmpId;
  }

  public void setTmpId(Integer tmpId) {
    this.tmpId = tmpId;
  }

  @Column(name = "EXT_OTHER_INFO")
  public String getExtOtherInfo() {
    return extOtherInfo;
  }

  public void setExtOtherInfo(String extOtherInfo) {
    this.extOtherInfo = extOtherInfo;
  }

  @Column(name = "RECEIVE_DATE")
  public Date getReceiveDate() {
    return receiveDate;
  }

  public void setReceiveDate(Date receiveDate) {
    this.receiveDate = receiveDate;
  }

  /**
   * @return the status
   */
  @Column(name = "STATUS")
  public Integer getStatus() {
    return status;
  }

  /**
   * @param status the status to set
   */
  public void setStatus(Integer status) {
    this.status = status;
  }

  /**
   * @return the psnName
   */
  @Column(name = "PSN_NAME")
  public String getPsnName() {
    return psnName;
  }

  /**
   * @param psnName the psnName to set
   */
  public void setPsnName(String psnName) {
    this.psnName = psnName;
  }

  /**
   * @return the firstName
   */
  @Column(name = "FIRST_NAME")
  public String getFirstName() {
    return firstName;
  }

  /**
   * @param firstName the firstName to set
   */
  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  /**
   * @return the lastName
   */
  @Column(name = "LAST_NAME")
  public String getLastName() {
    return lastName;
  }

  /**
   * @param lastName the lastName to set
   */
  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  /**
   * @return the psnHeadUrl
   */
  @Column(name = "PSN_HEAD_URL")
  public String getPsnHeadUrl() {
    return psnHeadUrl;
  }

  /**
   * @param psnHeadUrl the psnHeadUrl to set
   */
  public void setPsnHeadUrl(String psnHeadUrl) {
    this.psnHeadUrl = psnHeadUrl;
  }

  @Column(name = "OLD_DATA")
  public Integer getOldData() {
    return oldData;
  }

  public void setOldData(Integer oldData) {
    this.oldData = oldData;
  }

  @Transient
  public String getViewTitle() {
    if (StringUtils.isBlank(viewTitle)) {
      String locale = LocaleContextHolder.getLocale().toString();
      if ("zh_CN".equals(locale)) {
        viewTitle = (this.oldData == 1 ? "" : StringUtils.isNotBlank(senderName) ? senderName : "")
            + (StringUtils.isNotBlank(title) ? title : enTitle);
      } else {
        viewTitle = (this.oldData == 1 ? "" : StringUtils.isNotBlank(senderName) ? senderName : "") + " "
            + (StringUtils.isNotBlank(enTitle) ? enTitle : title);
      }
    }
    return viewTitle;
  }

  public void setViewTitle(String viewTitle) {
    this.viewTitle = viewTitle;
  }

  @Transient
  public String getNoHtmlContent() {
    return noHtmlContent;
  }

  public void setNoHtmlContent(String noHtmlContent) {
    this.noHtmlContent = noHtmlContent;
  }

  @Transient
  public String getSenderName() {
    return senderName;
  }

  public void setSenderName(String senderName) {
    this.senderName = senderName;
  }

  @Transient
  public String getSenderAvatars() {
    return senderAvatars;
  }

  public void setSenderAvatars(String senderAvatars) {
    this.senderAvatars = senderAvatars;
  }

}
