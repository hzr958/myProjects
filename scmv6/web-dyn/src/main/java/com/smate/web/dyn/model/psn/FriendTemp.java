package com.smate.web.dyn.model.psn;

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
 * 添加好友，但好友还没有批准的临时存储表.
 * 
 * @author lhd
 * 
 */
@Entity
@Table(name = "PSN_FRIEND_TEMP")
public class FriendTemp implements Serializable {

  private static final long serialVersionUID = -6472700368074305856L;
  // pk
  private Long id;
  // 个人psnId
  private Long psnId;
  // 邀请好友psnId
  private Long tempPsnId;
  // 对即将要邀请的好友分组
  private String groups;
  // 对即将要邀请的好友创建新分组
  private String newGroups;
  // 对邀请的好友发送请求消息内容
  private String reqContent;
  // 通过邮件邀请时，本地临时短消息的ID
  private Long sendReqId;
  // 通过邮件邀请时的邮件
  private String sendMail;
  private String inviteCode;
  private Date createDate;

  public FriendTemp() {
    super();
    this.createDate = new Date();
  }

  public FriendTemp(Long psnId, Long tempPsnId, String reqContent, String sendMail) {
    super();
    this.psnId = psnId;
    this.tempPsnId = tempPsnId;
    this.reqContent = reqContent;
    this.sendMail = sendMail;
    this.createDate = new Date();
  }

  public FriendTemp(Long id, Long psnId, Long tempPsnId, String sendMail) {
    super();
    this.id = id;
    this.psnId = psnId;
    this.tempPsnId = tempPsnId;
    this.sendMail = sendMail;
  }

  @Id
  @Column(name = "ID")
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_PSN_FRIEND_TEMP", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  @Column(name = "PSN_ID")
  public Long getPsnId() {
    return psnId;
  }

  public void setPsnId(Long psnId) {
    this.psnId = psnId;
  }

  @Column(name = "TEMP_PSN_ID")
  public Long getTempPsnId() {
    return tempPsnId;
  }

  public void setTempPsnId(Long tempPsnId) {
    this.tempPsnId = tempPsnId;
  }

  @Column(name = "GROUPS")
  public String getGroups() {
    return groups;
  }

  public void setGroups(String groups) {
    this.groups = groups;
  }

  @Column(name = "REQ_CONTENT")
  public String getReqContent() {
    return reqContent;
  }

  public void setReqContent(String reqContent) {
    this.reqContent = reqContent;
  }

  @Column(name = "SEND_REQ_ID")
  public Long getSendReqId() {
    return sendReqId;
  }

  public void setSendReqId(Long sendReqId) {
    this.sendReqId = sendReqId;
  }

  @Column(name = "SEND_MAIL")
  public String getSendMail() {
    return sendMail;
  }

  public void setSendMail(String sendMail) {
    this.sendMail = sendMail;
  }

  @Column(name = "GROUPS_NEW")
  public String getNewGroups() {
    return newGroups;
  }

  public void setNewGroups(String newGroups) {
    this.newGroups = newGroups;
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

  @Column(name = "CREATE_DATE")
  public Date getCreateDate() {
    return createDate;
  }

  public void setCreateDate(Date createDate) {
    this.createDate = createDate;
  }

}
