/**
 * 
 */
package com.smate.center.batch.model.mail;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * 站内邀请发件箱
 * 
 * @author oyh
 * 
 */
@Entity
@Table(name = "INVITE_MAILBOX")
public class InviteMailBox implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = -8171824659370159181L;
  private Long mailId;
  private Long senderId;
  private String psnName;
  private String firstName;
  private String lastName;
  private String psnHeadUrl;
  private String title;
  private String enTitle;
  private String content;
  private Date optDate;
  private Integer status;
  private Integer inviteType;
  // 发送邀请的人的基本信息
  private String senderInfo;
  // 扩展字段,保存一些其他信息，以json格式保存
  private String extOtherInfo;
  private String zhReceiver;
  private String enReceiver;

  private Map<Integer, String> syncNodePsn;
  // 因需求更改所增加的新增的字段invitePsnId 保存加入群组请求的id
  private Long invitePsnId;

  public InviteMailBox() {
    super();
  }

  public InviteMailBox(Long senderId, String title, String enTitle, String content, Date optDate, Integer status) {
    super();
    this.senderId = senderId;
    this.title = title;
    this.enTitle = enTitle;
    this.content = content;
    this.optDate = optDate;
    this.status = status;

  }

  @Id
  @Column(name = "MAIL_ID")
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_INVITE_MAILBOX", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
  public Long getMailId() {
    return mailId;
  }

  public void setMailId(Long mailId) {
    this.mailId = mailId;
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

  @Column(name = "OPT_DATE")
  public Date getOptDate() {
    return optDate;
  }

  public void setOptDate(Date optDate) {
    this.optDate = optDate;
  }

  @Column(name = "STATUS")
  public Integer getStatus() {
    return status;
  }

  public void setStatus(Integer status) {
    this.status = status;
  }

  @Column(name = "INVITE_TYPE")
  public Integer getInviteType() {
    return inviteType;
  }

  public void setInviteType(Integer inviteType) {
    this.inviteType = inviteType;
  }

  @Column(name = "SENDER_ID")
  public Long getSenderId() {
    return senderId;
  }

  public void setSenderId(Long senderId) {
    this.senderId = senderId;
  }

  @Column(name = "PSN_NAME")
  public String getPsnName() {
    return psnName;
  }

  public void setPsnName(String psnName) {
    this.psnName = psnName;
  }

  @Column(name = "FIRST_NAME")
  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  @Column(name = "LAST_NAME")
  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  @Column(name = "PSN_HEAD_URL")
  public String getPsnHeadUrl() {
    return psnHeadUrl;
  }

  public void setPsnHeadUrl(String psnHeadUrl) {
    this.psnHeadUrl = psnHeadUrl;
  }

  @Column(name = "SENDER_INFO")
  public String getSenderInfo() {
    return senderInfo;
  }

  public void setSenderInfo(String senderInfo) {
    this.senderInfo = senderInfo;
  }

  @Column(name = "EXT_OTHER_INFO")
  public String getExtOtherInfo() {
    return extOtherInfo;
  }

  public void setExtOtherInfo(String extOtherInfo) {
    this.extOtherInfo = extOtherInfo;
  }

  @Column(name = "ZH_RECEIVER")
  public String getZhReceiver() {
    return zhReceiver;
  }

  public void setZhReceiver(String zhReceiver) {
    this.zhReceiver = zhReceiver;
  }

  @Column(name = "EN_RECEIVER")
  public String getEnReceiver() {
    return enReceiver;
  }

  public void setEnReceiver(String enReceiver) {
    this.enReceiver = enReceiver;
  }

  @Transient
  public Map<Integer, String> getSyncNodePsn() {
    return syncNodePsn;
  }

  public void setSyncNodePsn(Map<Integer, String> syncNodePsn) {
    this.syncNodePsn = syncNodePsn;
  }

  @Transient
  public Long getInvitePsnId() {
    return invitePsnId;
  }

  public void setInvitePsnId(Long invitePsnId) {
    this.invitePsnId = invitePsnId;
  }

}
