package com.smate.center.batch.model.sns.pub;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * 群组人员的邀请临时表
 * 
 * @author oyh
 * 
 */
@Entity
@Table(name = "GROUP_INVITE")
public class GroupInvite implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = 5480650804090778480L;

  private Long inviteId;
  // 邀请人员Id
  private Long psnId;
  private Long groupId;
  // 站外邀请人邮件
  private String email;
  // 发起邀请人的psnId
  private Long sendPsnId;
  private Date createDate;
  // 邀请邮件Id
  private Long reqId;
  // 邀请码.
  private String inviteCode;

  /**
   * @return the inviteId
   */
  @Id
  @Column(name = "INVITE_ID")
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_GROUP_INVITE", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
  public Long getInviteId() {
    return inviteId;
  }

  /**
   * @param inviteId the inviteId to set
   */
  public void setInviteId(Long inviteId) {
    this.inviteId = inviteId;
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
   * @return the groupId
   */
  @Column(name = "GROUP_ID")
  public Long getGroupId() {
    return groupId;
  }

  /**
   * @param groupId the groupId to set
   */
  public void setGroupId(Long groupId) {
    this.groupId = groupId;
  }

  /**
   * @return the email
   */
  @Column(name = "EMAIL")
  public String getEmail() {
    return email;
  }

  /**
   * @param email the email to set
   */
  public void setEmail(String email) {
    this.email = email;
  }

  /**
   * @return the sendPsnId
   */
  @Column(name = "SEND_PSN_ID")
  public Long getSendPsnId() {
    return sendPsnId;
  }

  /**
   * @param sendPsnId the sendPsnId to set
   */
  public void setSendPsnId(Long sendPsnId) {
    this.sendPsnId = sendPsnId;
  }

  /**
   * @return the createDate
   */
  @Column(name = "CREATE_DATE")
  public Date getCreateDate() {
    return createDate;
  }

  /**
   * @param createDate the createDate to set
   */
  public void setCreateDate(Date createDate) {
    this.createDate = createDate;
  }

  /**
   * @return the reqId
   */
  @Column(name = "REQ_ID")
  public Long getReqId() {
    return reqId;
  }

  /**
   * @param reqId the reqId to set
   */
  public void setReqId(Long reqId) {
    this.reqId = reqId;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

  @Column(name = "INVITE_CODE")
  public String getInviteCode() {
    return inviteCode;
  }

  public void setInviteCode(String inviteCode) {
    this.inviteCode = inviteCode;
  }

}
