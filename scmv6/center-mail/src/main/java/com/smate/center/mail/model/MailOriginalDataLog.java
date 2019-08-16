package com.smate.center.mail.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 邮件原始数据日记表
 * 
 * @author zzx
 *
 */
@Entity
@Table(name = "V_MAIL_ORIGINAL_DATA_HIS")
public class MailOriginalDataLog implements Serializable {
  private static final long serialVersionUID = 1L;
  @Id
  @Column(name = "MAIL_ID")
  private Long mailId;// 邮件Id
  @Column(name = "SENDER_PSN_ID")
  private Long senderPsnId;// 发送者psnId ，0=系统邮件
  @Column(name = "RECEIVER_PSN_ID")
  private Long receiverPsnId;// 接收者id ,0=非科研之友用户
  @Column(name = "RECEIVER")
  private String receiver;// 接收邮箱
  @Column(name = "MAIL_TEMPLATE_CODE")
  private Integer mailTemplateCode; // 模版编号
  @Column(name = "PRIOR_LEVEL")
  private String priorLevel;// 优先级别
  @Column(name = "CREATE_DATE")
  private Date createDate; // 创建时间
  @Column(name = "UPDATE_DATE")
  private Date updateDate;// 更新时间
  @Column(name = "STATUS")
  private Integer status;// 构造状态 0=待构造邮件 1=构造成功 2=构造失败 3=用户不接收此类邮件4=模版频率限制
  @Column(name = "MSG")
  private String msg;// 描述
  @Column(name = "SEND_STATUS")
  private Integer sendStatus;// 发送状态 0=待分配 1=待发送 9=调度出错 2=发送成功 3=黑名单 4=receiver不存在

  public Long getMailId() {
    return mailId;
  }

  public void setMailId(Long mailId) {
    this.mailId = mailId;
  }

  public Long getSenderPsnId() {
    return senderPsnId;
  }

  public void setSenderPsnId(Long senderPsnId) {
    this.senderPsnId = senderPsnId;
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

  public Integer getMailTemplateCode() {
    return mailTemplateCode;
  }

  public void setMailTemplateCode(Integer mailTemplateCode) {
    this.mailTemplateCode = mailTemplateCode;
  }

  public String getPriorLevel() {
    return priorLevel;
  }

  public void setPriorLevel(String priorLevel) {
    this.priorLevel = priorLevel;
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

  public Integer getStatus() {
    return status;
  }

  public void setStatus(Integer status) {
    this.status = status;
  }

  public String getMsg() {
    return msg;
  }

  public void setMsg(String msg) {
    this.msg = msg;
  }

  public Integer getSendStatus() {
    return sendStatus;
  }

  public void setSendStatus(Integer sendStatus) {
    this.sendStatus = sendStatus;
  }

}
