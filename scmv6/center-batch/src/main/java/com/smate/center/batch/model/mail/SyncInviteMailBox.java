/**
 * 
 */
package com.smate.center.batch.model.mail;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * 站内消息发件箱
 * 
 * @author oyh
 * 
 */
@Entity
@Table(name = "INVITE_MAILBOX")
public class SyncInviteMailBox implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = 7890126107307711380L;
  private Long mailId;
  private String title;
  private String enTitle;
  private Long senderId;
  private String psnName;
  private String firstName;
  private String lastName;
  private String psnHeadUrl;
  private String content;
  private Date optDate;
  private Integer status;
  private Map<Integer, String> syncNodePsn;

  private Integer inviteType;
  // 扩展字段,保存一些其他信息，以json格式保存
  private String ExtOtherInfo;
  private String zhReceiver;
  private String enReceiver;

  public SyncInviteMailBox() {
    super();
    // TODO Auto-generated constructor stub
  }

  public SyncInviteMailBox(String title, String enTitle, String content, Date optDate, Integer status) {
    super();

    this.title = title;
    this.content = content;
    this.optDate = optDate;
    this.status = status;
  }

  /**
   * @return the mailId
   */
  @Id
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
   * @return the title
   */
  @Transient
  public String getTitle() {
    return title;
  }

  /**
   * @param title the title to set
   */
  public void setTitle(String title) {
    this.title = title;
  }

  @Transient
  public String getEnTitle() {
    return enTitle;
  }

  public void setEnTitle(String enTitle) {
    this.enTitle = enTitle;
  }

  /**
   * @return the content
   */
  @Transient
  public String getContent() {
    return content;
  }

  /**
   * @param content the content to set
   */
  public void setContent(String content) {
    this.content = content;
  }

  /**
   * @return the optDate
   */
  @Column(name = "OPT_DATE")
  public Date getOptDate() {
    return optDate;
  }

  /**
   * @param optDate the optDate to set
   */
  public void setOptDate(Date optDate) {
    this.optDate = optDate;
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
   * @return the syncNodePsn
   */
  @Transient
  public Map<Integer, String> getSyncNodePsn() {
    return syncNodePsn;
  }

  /**
   * @param syncNodePsn the syncNodePsn to set
   */
  public void setSyncNodePsn(Map<Integer, String> syncNodePsn) {
    this.syncNodePsn = syncNodePsn;
  }

  /**
   * @return the senderId
   */
  @Column(name = "SENDER_ID")
  public Long getSenderId() {
    return senderId;
  }

  /**
   * @param senderId the senderId to set
   */
  public void setSenderId(Long senderId) {
    this.senderId = senderId;
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
  public String getPsnHeadUrl() {
    return psnHeadUrl;
  }

  /**
   * @param psnHeadUrl the psnHeadUrl to set
   */
  @Column(name = "PSN_HEAD_URL")
  public void setPsnHeadUrl(String psnHeadUrl) {
    this.psnHeadUrl = psnHeadUrl;
  }

  /**
   * @return the inviteType
   */
  @Column(name = "INVITE_TYPE")
  public Integer getInviteType() {
    return inviteType;
  }

  /**
   * @param inviteType the inviteType to set
   */
  public void setInviteType(Integer inviteType) {
    this.inviteType = inviteType;
  }

  @Transient
  public String getExtOtherInfo() {
    return ExtOtherInfo;
  }

  public void setExtOtherInfo(String extOtherInfo) {
    ExtOtherInfo = extOtherInfo;
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

}
