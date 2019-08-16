package com.smate.web.dyn.model.pub;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * 成果指派记录表
 * 
 * @author zll
 *
 */
@Entity
@Table(name = "PUB_ASSIGN_LOG")
public class PubAssignLog implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = 1932374798518152520L;
  private Long id;
  private Long pdwhPubId; // 基准库成果id
  private Long psnId; // 人员id
  private Integer confirmResult = 0; // 未认领 0 ；已认领 1 ，拒绝：2
  private Integer isAutoConfirm = 0; // 是否自动确认
  private Integer emailMatch = 0;// 邮件匹配
  private Integer fullNameMatch = 0;// 全名匹配
  private Integer InitNameMatch = 0;// 简称匹配
  private Integer insMatch = 0;// 单位匹配
  private Integer friendMatch = 0;// 好友匹配
  private Integer keywordsMatch = 0;// 关键词匹配
  private Long score = 0L;// 总分
  private Date createDate;
  private Integer isSendMail = 0;// 是否已发送认领邮件
  private Date sendMailDate;// 发送邮件时间
  private Date confirmDate;// 认领时间
  private Date updateDate;// 记录更新时间
  private Long snsPubId; // 认领后对应个人库成果id
  private Integer status = 0;// 基准库成果状态：0.正常，1.已删除

  public PubAssignLog() {
    super();
  }

  @Id
  @Column(name = "ID")
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_PUB_ASSIGN_LOG", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  @Column(name = "PUB_ID")
  public Long getPdwhPubId() {
    return pdwhPubId;
  }

  public void setPdwhPubId(Long pdwhPubId) {
    this.pdwhPubId = pdwhPubId;
  }

  @Column(name = "PSN_ID")
  public Long getPsnId() {
    return psnId;
  }

  public void setPsnId(Long psnId) {
    this.psnId = psnId;
  }

  @Column(name = "CONFIRM_RESULT")
  public Integer getConfirmResult() {
    return confirmResult;
  }

  public void setConfirmResult(Integer confirmResult) {
    this.confirmResult = confirmResult;
  }

  @Column(name = "IS_AUTO_CONFIRM")
  public Integer getIsAutoConfirm() {
    return isAutoConfirm;
  }

  public void setIsAutoConfirm(Integer isAutoConfirm) {
    this.isAutoConfirm = isAutoConfirm;
  }

  @Column(name = "EMAIL_MATCH")
  public Integer getEmailMatch() {
    return emailMatch;
  }

  public void setEmailMatch(Integer emailMatch) {
    this.emailMatch = emailMatch;
  }

  @Column(name = "FULLNAME_MATCH")
  public Integer getFullNameMatch() {
    return fullNameMatch;
  }

  public void setFullNameMatch(Integer fullNameMatch) {
    this.fullNameMatch = fullNameMatch;
  }

  @Column(name = "INITNAME_MATCH")
  public Integer getInitNameMatch() {
    return InitNameMatch;
  }

  public void setInitNameMatch(Integer initNameMatch) {
    InitNameMatch = initNameMatch;
  }

  @Column(name = "INS_MATCH")
  public Integer getInsMatch() {
    return insMatch;
  }

  public void setInsMatch(Integer insMatch) {
    this.insMatch = insMatch;
  }

  @Column(name = "FRIENDS_MATCH")
  public Integer getFriendMatch() {
    return friendMatch;
  }

  public void setFriendMatch(Integer friendMatch) {
    this.friendMatch = friendMatch;
  }

  @Column(name = "KEYWORDS_MATCH")
  public Integer getKeywordsMatch() {
    return keywordsMatch;
  }

  public void setKeywordsMatch(Integer keywordsMatch) {
    this.keywordsMatch = keywordsMatch;
  }

  @Column(name = "SCORE")
  public Long getScore() {
    return score;
  }

  public void setScore(Long score) {
    this.score = score;
  }

  @Column(name = "CREATE_DATE")
  public Date getCreateDate() {
    return createDate;
  }

  public void setCreateDate(Date createDate) {
    this.createDate = createDate;
  }

  @Column(name = "IS_SEND_MAIL")
  public Integer getIsSendMail() {
    return isSendMail;
  }

  public void setIsSendMail(Integer isSendMail) {
    this.isSendMail = isSendMail;
  }

  @Column(name = "SEND_MAIL_DATE")
  public Date getSendMailDate() {
    return sendMailDate;
  }

  public void setSendMailDate(Date sendMailDate) {
    this.sendMailDate = sendMailDate;
  }

  @Column(name = "CONFIRM_DATE")
  public Date getConfirmDate() {
    return confirmDate;
  }

  public void setConfirmDate(Date confirmDate) {
    this.confirmDate = confirmDate;
  }

  @Column(name = "UPDATE_DATE")
  public Date getUpdateDate() {
    return updateDate;
  }

  public void setUpdateDate(Date updateDate) {
    this.updateDate = updateDate;
  }

  @Column(name = "SNS_PUB_ID")
  public Long getSnsPubId() {
    return snsPubId;
  }

  public void setSnsPubId(Long snsPubId) {
    this.snsPubId = snsPubId;
  }

  @Column(name = "STATUS")
  public Integer getStatus() {
    return status;
  }

  public void setStatus(Integer status) {
    this.status = status;
  }

}
