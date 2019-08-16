/**
 * 
 */
package com.smate.center.batch.model.mail;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

/**
 * 站内请求
 * 
 * @author oyh
 * 
 */
@Entity
@Table(name = "REQ_MAILBOX")
public class PsnReqMailBox implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = 4129755677768583634L;
  // 邮件Id
  private Long mailId;
  // 发件人
  private Long senderId;
  private String psnName;
  private String firstName;
  private String lastName;
  private String psnHeadUrl;
  // 邮件标题 // 邮件标题
  private String title;

  // 发件时间
  private Date optDate;
  // 发件人状态
  private Integer status;
  // 同步人员节点
  private Map<Integer, String> syncNodePsn;

  private List<ReqInbox> inboxs = new ArrayList<ReqInbox>();

  public PsnReqMailBox() {
    super();
    // TODO Auto-generated constructor stub
  }

  /**
   * @return the mailId
   */
  @Id
  @Column(name = "MAIL_ID")
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_REQ_MAILBOX", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
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
   * @return the inboxs
   */
  @OneToMany(fetch = FetchType.EAGER)
  @JoinColumn(name = "MAIL_ID", insertable = false, updatable = false)
  @Fetch(FetchMode.SUBSELECT)
  @OrderBy("id ASC")
  public List<ReqInbox> getInboxs() {
    return inboxs;
  }

  /**
   * @param inboxs the inboxs to set
   */
  public void setInboxs(List<ReqInbox> inboxs) {
    this.inboxs = inboxs;
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

}
