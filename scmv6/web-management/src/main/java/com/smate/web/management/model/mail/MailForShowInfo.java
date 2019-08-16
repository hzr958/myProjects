package com.smate.web.management.model.mail;

import java.util.Date;

import org.apache.commons.lang3.StringUtils;

import com.smate.core.base.utils.security.Des3Utils;

public class MailForShowInfo {
  private Long mailId;
  private String des3MailId;
  private Long receiverPsnId;// 接收者id ,0=非科研之友用户
  private String receiver;// 接收邮箱
  private String mailTemplateName; // 模版名
  private String subject;// 主题
  private String sender; // 发送者邮箱
  private Date updateDate;// 更新时间
  private String showDateStr;
  private Integer sendStatus;// 0=待构造邮件 1=构造成功（发送状态 0=待分配 1=待发送 9=调度出错 2=发送成功
  // 3=黑名单 4=receiver不存在） 2=构造失败 3=用户不接收此类邮件
  private String statusMsg;
  private Integer templateCode;
  private Long senderPsnId;// 发件人id

  public Long getMailId() {
    return mailId;
  }

  public void setMailId(Long mailId) {
    this.mailId = mailId;
  }

  public Long getReceiverPsnId() {
    return receiverPsnId;
  }

  public void setReceiverPsnId(Long receiverPsnId) {
    this.receiverPsnId = receiverPsnId;
  }

  public String getReceiver() {
    return receiver;
  }

  public void setReceiver(String receiver) {
    this.receiver = receiver;
  }

  public String getMailTemplateName() {
    return mailTemplateName;
  }

  public void setMailTemplateName(String mailTemplateName) {
    this.mailTemplateName = mailTemplateName;
  }

  public String getSender() {
    return sender;
  }

  public void setSender(String sender) {
    this.sender = sender;
  }

  public Date getUpdateDate() {
    return updateDate;
  }

  public void setUpdateDate(Date updateDate) {
    this.updateDate = updateDate;
  }

  public Integer getSendStatus() {
    return sendStatus;
  }

  public void setSendStatus(Integer sendStatus) {
    this.sendStatus = sendStatus;
  }

  public String getSubject() {
    return subject;
  }

  public void setSubject(String subject) {
    this.subject = subject;
  }

  public String getStatusMsg() {
    return statusMsg;
  }

  public void setStatusMsg(String statusMsg) {
    this.statusMsg = statusMsg;
  }

  public String getShowDateStr() {
    return showDateStr;
  }

  public void setShowDateStr(String showDateStr) {
    this.showDateStr = showDateStr;
  }

  public String getDes3MailId() {
    if (StringUtils.isBlank(des3MailId) && mailId != null) {
      des3MailId = Des3Utils.encodeToDes3(mailId.toString());
    }
    return des3MailId;
  }

  public void setDes3MailId(String des3MailId) {
    this.des3MailId = des3MailId;
  }

  public Integer getTemplateCode() {
    return templateCode;
  }

  public void setTemplateCode(Integer templateCode) {
    this.templateCode = templateCode;
  }

  public Long getSenderPsnId() {
    return senderPsnId;
  }

  public void setSenderPsnId(Long senderPsnId) {
    this.senderPsnId = senderPsnId;
  }

}
