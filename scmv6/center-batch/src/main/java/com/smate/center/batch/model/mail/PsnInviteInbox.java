package com.smate.center.batch.model.mail;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
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
public class PsnInviteInbox implements Serializable {


  /**
   * 
   */
  private static final long serialVersionUID = -2693229308306802085L;
  // 主键
  private Long id;
  // 收件人Id
  private Long psnId;
  private String psnName;
  private String firstName;
  private String lastName;
  private String psnHeadUrl;
  // 邮件
  private PsnInviteMailBox mailBox;
  // 0 未读 1已读 2 删除

  private Integer status;
  // 邮件处理状态，0-未处理 1- 接受 2- 拒绝
  private Integer optStatus;

  public PsnInviteInbox() {
    super();
    // TODO Auto-generated constructor stub
  }

  public PsnInviteInbox(Long psnId, Integer status, Integer optStatus) {
    super();
    this.psnId = psnId;
    this.optStatus = optStatus;
    this.status = status;
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
   * @return the mailBox
   */
  @OneToOne
  @JoinColumn(name = "MAIL_ID")
  public PsnInviteMailBox getMailBox() {
    return mailBox;
  }

  /**
   * @param mailBox the mailBox to set
   */
  public void setMailBox(PsnInviteMailBox mailBox) {
    this.mailBox = mailBox;
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
