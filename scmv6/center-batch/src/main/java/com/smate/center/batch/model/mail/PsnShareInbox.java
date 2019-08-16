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


@Entity
@Table(name = "SHARE_INBOX")
public class PsnShareInbox implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = -7271568582160554157L;
  // 主键
  private Long id;
  // 收件人Id
  private Long psnId;
  private String psnName;
  private String firstName;
  private String lastName;
  private String psnHeadUrl;
  // 邮件
  private PsnShareMailBox mailBox;

  // 邮件处理状态，0--未读 1—已读 2 –已删除

  private Integer status;

  public PsnShareInbox() {
    super();
    // TODO Auto-generated constructor stub
  }

  public PsnShareInbox(Long psnId, Integer status) {
    super();
    this.psnId = psnId;

    this.status = status;
  }

  /**
   * @return the id
   */
  @Id
  @Column(name = "ID")
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_SHARE_INBOX", allocationSize = 1)
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
   * @return the mailBox
   */
  @OneToOne
  @JoinColumn(name = "MAIL_ID")
  public PsnShareMailBox getMailBox() {
    return mailBox;
  }

  /**
   * @param mailBox the mailBox to set
   */
  public void setMailBox(PsnShareMailBox mailBox) {
    this.mailBox = mailBox;
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
