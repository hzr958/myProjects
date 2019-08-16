package com.smate.center.batch.model.mail;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * 请求.
 * 
 * @author chenxiangrong
 * 
 */
@Entity
@Table(name = "INVITE_INBOX")
public class InviteInbox implements Serializable {



  /**
   * 
   */
  private static final long serialVersionUID = 5197564054889214846L;
  // 主键
  private Long id;
  // 收件人Id
  private Long psnId;
  private String psnName;
  private String firstName;
  private String lastName;
  private String psnHeadUrl;
  // 邮件Id
  private Long mailId;

  // 0 未读 1已读 2 删除

  private Integer status;
  // 邮件处理状态，0-未处理 1-接受 2-拒绝 3-操作异常 4-不允许加为好友 5-已经是组成员 6-已经处理 7-系统忽略
  private Integer optStatus;

  public InviteInbox() {
    super();
    this.status = 0;
    this.optStatus = 0;
    // TODO Auto-generated constructor stub
  }

  public InviteInbox(Long psnId, Long mailId, Integer status, Integer optStatus) {
    super();
    this.psnId = psnId;
    this.mailId = mailId;
    this.status = status == null ? 0 : status;
    this.optStatus = optStatus;
  }

  /**
   * @return the id
   */
  @Id
  @Column(name = "ID")
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_INVITE_INBOX", allocationSize = 1)
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

  /**
   * @return the optStatus
   */
  @Column(name = "OPT_STATUS")
  public Integer getOptStatus() {
    return optStatus;
  }

  /**
   * @param optStatus the optStatus to set
   */
  public void setOptStatus(Integer optStatus) {
    this.optStatus = optStatus;
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

}
