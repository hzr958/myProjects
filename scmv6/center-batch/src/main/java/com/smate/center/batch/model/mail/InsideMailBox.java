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
 * 站内消息发件箱
 * 
 * @author oyh
 * 
 */
@Entity
@Table(name = "INSIDE_MAILBOX")
public class InsideMailBox implements Serializable {


  /**
   * 
   */
  private static final long serialVersionUID = -7895961711284544500L;
  private Long mailId;
  private Long psnId;
  private String title;
  private String enTitle;
  private String content;
  private Date sendDate;
  private Integer status;
  // 扩展字段，以json格式存储
  private String extOtherInfo;
  private Integer msgType;
  /** 消息中心查看模板. */
  private Integer tmpId;

  /** 显示标题. */
  private String viewTitle;
  /** 去除html标签的内容. */
  private String noHtmlContent;
  /** 消息接收人id（只取一个）. */
  private Long receiverId;
  /** 消息接收人姓名（只取一个）. */
  private String receiverName;
  // scm-5731
  /** 消息接受人姓名（多个） */
  private String zhReceivers;
  private String enReceivers;
  /** 消息接收人头像（只取一个）. */
  private String receiverAvatars;
  /** 消息接收人数. */
  private int receiverNum = 0;

  @Id
  @Column(name = "MAIL_ID")
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_INSIDE_MAILBOX", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
  public Long getMailId() {
    return mailId;
  }

  public void setMailId(Long mailId) {
    this.mailId = mailId;
  }

  @Column(name = "PSN_ID")
  public Long getPsnId() {
    return psnId;
  }

  public void setPsnId(Long psnId) {
    this.psnId = psnId;
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

  @Column(name = "SEND_DATE")
  public Date getSendDate() {
    return sendDate;
  }

  public void setSendDate(Date sendDate) {
    this.sendDate = sendDate;
  }

  @Column(name = "STATUS")
  public Integer getStatus() {
    return status;
  }

  public void setStatus(Integer status) {
    this.status = status;
  }

  @Column(name = "EXT_OTHER_INFO")
  public String getExtOtherInfo() {
    return extOtherInfo;
  }

  public void setExtOtherInfo(String extOtherInfo) {
    this.extOtherInfo = extOtherInfo;
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
  public Long getReceiverId() {
    return receiverId;
  }

  public void setReceiverId(Long receiverId) {
    this.receiverId = receiverId;
  }

  @Transient
  public String getReceiverName() {
    return receiverName;
  }

  public void setReceiverName(String receiverName) {
    this.receiverName = receiverName;
  }

  @Transient
  public String getReceiverAvatars() {
    return receiverAvatars;
  }

  public void setReceiverAvatars(String receiverAvatars) {
    this.receiverAvatars = receiverAvatars;
  }

  @Transient
  public int getReceiverNum() {
    return receiverNum;
  }

  public void setReceiverNum(int receiverNum) {
    this.receiverNum = receiverNum;
  }

  @Column(name = "ZH_RECEIVERS")
  public String getZhReceivers() {
    return zhReceivers;
  }

  public void setZhReceivers(String zhReceivers) {
    this.zhReceivers = zhReceivers;
  }

  @Column(name = "EN_RECEIVERS")
  public String getEnReceivers() {
    return enReceivers;
  }

  public void setEnReceivers(String enReceivers) {
    this.enReceivers = enReceivers;
  }


}
