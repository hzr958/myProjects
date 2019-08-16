package com.smate.web.psn.model.mailbox;



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
 * 站内通知发件箱
 * 
 * @author yangpeihai
 * 
 */
@Entity
@Table(name = "MSG_NOTICE_OUT_BOX")
public class MessageNoticeOutBox implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = 2936214847540806357L;

  private Long noticeId;
  // 收件人Id
  private Long senderId;
  // 收件人
  private String psnName;
  // 收件人fristName
  private String firstName;
  // 收件人lastName
  private String lastName;
  // 收件人头像路径
  private String psnHeadUrl;
  // 通知标题
  private String title;
  // 通知内容
  private String content;
  // 发件时间
  private Date optDate;
  // 发件人状态 1-已删除,默认为0
  private Integer status;
  // 消息类型 0或空-邀请好友成功，1-邀请好友失败，2-加入群成功，3加入群失败，4申请加入群组成功，5申请加入群组失败
  private Integer msgType;
  // 其它扩展信息，以json格式存放
  private String extOtherInfo;
  private Map<Integer, String> syncNodePsn;

  /**
   * @return the noticeId
   */
  @Id
  @Column(name = "NOTICE_ID")
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_MSG_NOTICE_OUT_BOX", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
  public Long getNoticeId() {
    return noticeId;
  }

  public void setNoticeId(Long noticeId) {
    this.noticeId = noticeId;
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

  @Column(name = "MSG_TYPE")
  public Integer getMsgType() {
    return msgType;
  }

  public void setMsgType(Integer msgType) {
    this.msgType = msgType;
  }

  @Column(name = "EXT_OTHER_INFO")
  public String getExtOtherInfo() {
    return extOtherInfo;
  }

  public void setExtOtherInfo(String extOtherInfo) {
    this.extOtherInfo = extOtherInfo;
  }

}
