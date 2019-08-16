package com.smate.web.dyn.model.reply;

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
 * 资源回复/评论model.
 * 
 * @author zk
 * 
 */
@Entity
@Table(name = "DYNAMIC_REPLY_RES")
public class DynamicReplyRes implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = 3431297213823751232L;
  private Long replyId;
  private int resType;
  private Long resId;
  private int resNode;
  private Long replyTimes;
  private Date updateDate;
  private Long psnId;
  private String platform; // pc端，移动端(mobile)

  public DynamicReplyRes() {}

  public DynamicReplyRes(Long replyId, Long replyTimes) {
    this.replyId = replyId;
    this.replyTimes = replyTimes;
  }

  public DynamicReplyRes(Long replyId, Long psnId, Long replyTimes) {
    this.replyId = replyId;
    this.psnId = psnId;
    this.replyTimes = replyTimes;
  }

  @Id
  @Column(name = "REPLY_ID")
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_DYNAMIC_REPLY_RES", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
  public Long getReplyId() {
    return replyId;
  }

  public void setReplyId(Long replyId) {
    this.replyId = replyId;
  }

  @Column(name = "RES_TYPE")
  public int getResType() {
    return resType;
  }

  public void setResType(int resType) {
    this.resType = resType;
  }

  @Column(name = "RES_ID")
  public Long getResId() {
    return resId;
  }

  public void setResId(Long resId) {
    this.resId = resId;
  }

  @Column(name = "RES_NODE")
  public int getResNode() {
    return resNode;
  }

  public void setResNode(int resNode) {
    this.resNode = resNode;
  }

  @Column(name = "REPLY_TIMES")
  public Long getReplyTimes() {
    return replyTimes;
  }

  public void setReplyTimes(Long replyTimes) {
    this.replyTimes = replyTimes;
  }

  @Column(name = "UPDATE_DATE")
  public Date getUpdateDate() {
    return updateDate;
  }

  public void setUpdateDate(Date updateDate) {
    this.updateDate = updateDate;
  }

  @Column(name = "PSN_ID")
  public Long getPsnId() {
    return psnId;
  }

  public void setPsnId(Long psnId) {
    this.psnId = psnId;
  }

  @Column(name = "PLATFORM")
  public String getPlatform() {
    return platform;
  }

  public void setPlatform(String platform) {
    this.platform = platform;
  }

}
