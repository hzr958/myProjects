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
@Table(name = "INSIDE_MAILBOX")
public class SyncInsideMailBox implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = 2135235606769106121L;
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
  private Map<Integer, String> syncNodePsn;
  // 请求信息
  private String requestMessage;

  // 扩展字段，以json格式存储
  private String extOtherInfo;

  // 消息类型：1 好友评价
  private Integer msgType;
  private String zhReceiver;
  private String enReceiver;

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
   * @return the title
   */
  @Column(name = "TITLE")
  public String getTitle() {
    return title;
  }

  /**
   * @param title the title to set
   */
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

  /**
   * @return the content
   */
  @Column(name = "CONTENT")
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

  @Column(name = "REQUEST_MESSAGE")
  public String getRequestMessage() {
    return requestMessage;
  }

  public void setRequestMessage(String requestMessage) {
    this.requestMessage = requestMessage;
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
