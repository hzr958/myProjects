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
 * 站内消息发件箱
 * 
 * @author oyh
 * 
 */
@Entity
@Table(name = "SHARE_MAILBOX")
public class PsnShareMailBox implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = -1178496182604164334L;
  private Long mailId;
  private String title;
  private Long senderId;
  private String psnName;
  private String firstName;
  private String lastName;
  private String psnHeadUrl;
  // 发件时间
  private Date optDate;
  // 共享有效期限
  private Date deadLine;
  // 发件人状态

  private Integer status;
  private String zhReceiver;
  private String enReceiver;

  private List<ShareInbox> inboxs = new ArrayList<ShareInbox>();
  // 同步人员节点
  private Map<Integer, String> syncNodePsn;

  public PsnShareMailBox() {
    super();
  }

  @Id
  @Column(name = "MAIL_ID")
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_SHARE_MAILBOX", allocationSize = 1)
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

  @Column(name = "DEADLINE")
  public Date getDeadLine() {
    return deadLine;
  }

  public void setDeadLine(Date deadLine) {
    this.deadLine = deadLine;
  }

  @OneToMany(fetch = FetchType.EAGER)
  @JoinColumn(name = "MAIL_ID", insertable = false, updatable = false)
  @Fetch(FetchMode.SUBSELECT)
  @OrderBy("id ASC")
  public List<ShareInbox> getInboxs() {
    return inboxs;
  }

  public void setInboxs(List<ShareInbox> inboxs) {
    this.inboxs = inboxs;
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

  @Column(name = "SENDER_ID")
  public Long getSenderId() {
    return senderId;
  }

  public void setSenderId(Long senderId) {
    this.senderId = senderId;
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
}
