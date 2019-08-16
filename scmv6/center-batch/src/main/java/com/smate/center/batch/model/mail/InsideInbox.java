/**
 * 
 */
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

import com.smate.core.base.utils.common.HtmlUtils;


/**
 * 站内消息收件箱.
 * 
 * @author oyh
 */
@Entity
@Table(name = "INSIDE_INBOX")
public class InsideInbox implements Serializable {


  /**
   * 
   */
  private static final long serialVersionUID = -9058994414351732562L;
  // 主键
  private Long id;
  // 收件人Id
  private Long psnId;
  // 发件人Id
  private Long senderId;
  // 邮件Id
  private Long mailId;

  // 邮件状态 0--未读 1—已读 2 –已删除
  private Integer status;

  private String title;
  private String enTitle;
  private String content;
  private Integer msgType;
  /** 消息中心查看模板. */
  private Integer tmpId;
  private String extOtherInfo;
  private Date recieveDate;

  /** 显示标题. */
  private String viewTitle;
  /** 去除html标签的内容. */
  private String noHtmlContent;
  /** 发送消息人名. */
  private String senderName;
  /** 发送消息人头像. */
  private String senderAvatars;

  public InsideInbox() {
    super();
    // TODO Auto-generated constructor stub
  }

  public InsideInbox(Long psnId, Long mailId, Integer status) {
    super();
    this.psnId = psnId;
    this.mailId = mailId;
    this.status = status;
  }

  /**
   * @return the id
   */
  @Id
  @Column(name = "ID")
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_INSIDE_INBOX", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
  public Long getId() {
    return id;
  }

  /**
   * @param id the id to set
   */
  public void setId(Long id) {
    this.id = id;
  }

  /**
   * @return the psnId
   */
  @Column(name = "PSN_ID")
  public Long getPsnId() {
    return psnId;
  }

  /**
   * @param psnId the psnId to set
   */
  public void setPsnId(Long psnId) {
    this.psnId = psnId;
  }

  /**
   * @return the mailId
   */
  @Column(name = "MAIL_ID")
  public Long getMailId() {
    return mailId;
  }

  /**
   * @param mailId the mailId to set
   */
  public void setMailId(Long mailId) {
    this.mailId = mailId;
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

  @Column(name = "TITLE_EN")
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

  @Column(name = "MSG_TYPE")
  public Integer getMsgType() {
    return msgType;
  }

  public void setMsgType(Integer msgType) {
    this.msgType = msgType;
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

  @Column(name = "RECIEVE_DATE")
  public Date getRecieveDate() {
    return recieveDate;
  }

  public void setRecieveDate(Date recieveDate) {
    this.recieveDate = recieveDate;
  }

  @Transient
  public String getViewTitle() {
    String locale = LocaleContextHolder.getLocale().toString();
    if ("zh_CN".equals(locale)) {
      viewTitle = StringUtils.isNotBlank(title) ? title : enTitle;
    } else {
      viewTitle = StringUtils.isNotBlank(enTitle) ? enTitle : title;
    }

    return viewTitle;
  }

  public void setViewTitle(String viewTitle) {
    this.viewTitle = viewTitle;
  }

  @Transient
  public String getNoHtmlContent() {
    noHtmlContent = this.content;
    if (noHtmlContent != null) {
      noHtmlContent = HtmlUtils.Html2Text(noHtmlContent);
    }
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
