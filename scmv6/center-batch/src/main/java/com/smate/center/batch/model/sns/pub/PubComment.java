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
import javax.persistence.Transient;

/**
 * 成果评论模块 Bean.
 * 
 * @author zk
 * 
 */
@Entity
@Table(name = "PUB_COMMENTS")
public class PubComment implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = -7795557287477737850L;

  private Long commentsId;
  private Long pubId;
  private Long psnId;
  private Integer commentsStart;
  private String commentsContent;
  private Integer isAudit;
  private Date createDate;
  private String psnName;
  private String psnAvatars;
  private String psnSiteUrl;
  private String syncFlag;
  private int deleteEnabled;
  private Long dynReplyRecordId;

  @Id
  @Column(name = "COMMENTS_ID")
  @SequenceGenerator(sequenceName = "SEQ_PUB_COMMENTS", name = "SEQ_STORE", allocationSize = 1)
  @GeneratedValue(generator = "SEQ_STORE", strategy = GenerationType.SEQUENCE)
  public Long getCommentsId() {
    return commentsId;
  }

  @Column(name = "PUB_ID")
  public Long getPubId() {
    return pubId;
  }

  @Column(name = "PSN_ID")
  public Long getPsnId() {
    return psnId;
  }

  @Column(name = "COMMENTS_START")
  public Integer getCommentsStart() {
    return commentsStart;
  }

  @Column(name = "COMMENTS_CONTENT")
  public String getCommentsContent() {
    return commentsContent;
  }

  @Column(name = "IS_AUDIT")
  public Integer getIsAudit() {
    return isAudit;
  }

  @Column(name = "CREATE_DATE")
  public Date getCreateDate() {
    return createDate;
  }

  @Column(name = "PSN_NAME")
  public String getPsnName() {
    return psnName;
  }

  @Column(name = "PSN_AVATARS")
  public String getPsnAvatars() {
    return psnAvatars;
  }

  @Column(name = "PSN_SITE_URL")
  public String getPsnSiteUrl() {
    return psnSiteUrl;
  }

  @Column(name = "SYNC_FLAG")
  public String getSyncFlag() {
    return syncFlag;
  }

  @Column(name = "DYN_REPLY_RECORD_ID")
  public Long getDynReplyRecordId() {
    return dynReplyRecordId;
  }

  public void setDynReplyRecordId(Long dynReplyRecordId) {
    this.dynReplyRecordId = dynReplyRecordId;
  }

  @Transient
  public int getDeleteEnabled() {
    return deleteEnabled;
  }

  public void setCommentsId(Long commentsId) {
    this.commentsId = commentsId;
  }

  public void setPubId(Long pubId) {
    this.pubId = pubId;
  }

  public void setPsnId(Long psnId) {
    this.psnId = psnId;
  }

  public void setCommentsStart(Integer commentsStart) {
    this.commentsStart = commentsStart;
  }

  public void setCommentsContent(String commentsContent) {
    this.commentsContent = commentsContent;
  }

  public void setIsAudit(Integer isAudit) {
    this.isAudit = isAudit;
  }

  public void setCreateDate(Date createDate) {
    this.createDate = createDate;
  }

  public void setPsnName(String psnName) {
    this.psnName = psnName;
  }

  public void setPsnAvatars(String psnAvatars) {
    this.psnAvatars = psnAvatars;
  }

  public void setPsnSiteUrl(String psnSiteUrl) {
    this.psnSiteUrl = psnSiteUrl;
  }

  public void setSyncFlag(String syncFlag) {
    this.syncFlag = syncFlag;
  }

  public void setDeleteEnabled(int deleteEnabled) {
    this.deleteEnabled = deleteEnabled;
  }

}
