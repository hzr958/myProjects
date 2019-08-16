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
import javax.persistence.Transient;

import com.smate.core.base.utils.string.ServiceUtil;

/**
 * 人员评论/回复记录model.
 * 
 * @author zk
 * 
 */
@Entity
@Table(name = "DYNAMIC_REPLY_PSN")
public class DynamicReplyPsn implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = -9050477823281271626L;
  private Long recordId;
  private Long replyId;
  private Long replyerId;
  private String syncFlag;
  private String replyerName;
  private String replyerEnName;
  private String replyerAvatar;
  private String replyContent;
  private Date replyDate;
  private int status;
  private String des3ReplyerId;
  // 被回复者
  private Long receiverId;
  private String receiverName;
  private String receiverEnName;
  private String des3ReceiverId;

  private Long replyAddResId; // 评论时添加的资源id 成果id
  private String platform; // pc端，移动端(mobile)

  @Id
  @Column(name = "RECORD_ID")
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_DYNAMIC_REPLY_PSN", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
  public Long getRecordId() {
    return recordId;
  }

  public void setRecordId(Long recordId) {
    this.recordId = recordId;
  }

  @Column(name = "REPLY_ID")
  public Long getReplyId() {
    return replyId;
  }

  public void setReplyId(Long replyId) {
    this.replyId = replyId;
  }

  @Column(name = "REPLYER_ID")
  public Long getReplyerId() {
    return replyerId;
  }

  public void setReplyerId(Long replyerId) {
    this.replyerId = replyerId;
  }

  @Column(name = "SYNC_FLAG")
  public String getSyncFlag() {
    return syncFlag;
  }

  public void setSyncFlag(String syncFlag) {
    this.syncFlag = syncFlag;
  }

  @Column(name = "REPLYER_NAME")
  public String getReplyerName() {
    return replyerName;
  }

  public void setReplyerName(String replyerName) {
    this.replyerName = replyerName;
  }

  @Column(name = "REPLYER_ENNAME")
  public String getReplyerEnName() {
    return replyerEnName;
  }

  public void setReplyerEnName(String replyerEnName) {
    this.replyerEnName = replyerEnName;
  }

  @Column(name = "REPLYER_AVATAR")
  public String getReplyerAvatar() {
    return replyerAvatar;
  }

  public void setReplyerAvatar(String replyerAvatar) {
    this.replyerAvatar = replyerAvatar;
  }

  @Column(name = "REPLY_CONTENT")
  public String getReplyContent() {
    return replyContent;
  }

  public void setReplyContent(String replyContent) {
    this.replyContent = replyContent;
  }

  @Column(name = "REPLY_DATE")
  public Date getReplyDate() {
    return replyDate;
  }

  public void setReplyDate(Date replyDate) {
    this.replyDate = replyDate;
  }

  @Column(name = "STATUS")
  public int getStatus() {
    return status;
  }

  public void setStatus(int status) {
    this.status = status;
  }

  @Transient
  public String getDes3ReplyerId() {
    return ServiceUtil.encodeToDes3(replyerId + "");
  }

  public void setDes3ReplyerId(String des3ReplyerId) {
    this.des3ReplyerId = des3ReplyerId;
  }

  @Column(name = "RECEIVER_ID")
  public Long getReceiverId() {
    return receiverId;
  }

  public void setReceiverId(Long receiverId) {
    this.receiverId = receiverId;
  }

  @Column(name = "RECEIVER_NAME")
  public String getReceiverName() {
    return receiverName;
  }

  public void setReceiverName(String receiverName) {
    this.receiverName = receiverName;
  }

  @Column(name = "RECEIVER_ENNAME")
  public String getReceiverEnName() {
    return receiverEnName;
  }

  public void setReceiverEnName(String receiverEnName) {
    this.receiverEnName = receiverEnName;
  }

  @Column(name = "REPLY_ADD_RES_ID")
  public Long getReplyAddResId() {
    return replyAddResId;
  }

  public void setReplyAddResId(Long replyAddResId) {
    this.replyAddResId = replyAddResId;
  }

  @Transient
  public String getDes3ReceiverId() {
    if (receiverId != null && receiverId.longValue() > 0) {
      this.des3ReceiverId = ServiceUtil.encodeToDes3(receiverId + "");
    } else {
      this.des3ReceiverId = "";
    }
    return des3ReceiverId;
  }

  public void setDes3ReceiverId(String des3ReceiverId) {
    this.des3ReceiverId = des3ReceiverId;
  }

  public DynamicReplyPsn() {}

  public DynamicReplyPsn(Long recordId, Long replyId, Long replyerId, String syncFlag, String replyerName,
      String replyerEnName, String replyerAvatar, String replyContent, Date replyDate, Long replyAddResId) {
    this.recordId = recordId;
    this.replyId = replyId;
    this.replyerId = replyerId;
    this.syncFlag = syncFlag;
    this.replyerName = replyerName;
    this.replyerEnName = replyerEnName;
    this.replyerAvatar = replyerAvatar;
    this.replyContent = replyContent;
    this.replyDate = replyDate;
    this.replyAddResId = replyAddResId;
  }

  public DynamicReplyPsn(Long recordId, Long replyId, Long replyerId, String syncFlag, String replyerName,
      String replyerEnName, String replyerAvatar, String replyContent, Date replyDate) {
    this.recordId = recordId;
    this.replyId = replyId;
    this.replyerId = replyerId;
    this.syncFlag = syncFlag;
    this.replyerName = replyerName;
    this.replyerEnName = replyerEnName;
    this.replyerAvatar = replyerAvatar;
    this.replyContent = replyContent;
    this.replyDate = replyDate;
  }

  @Column(name = "PLATFORM")
  public String getPlatform() {
    return platform;
  }

  public void setPlatform(String platform) {
    this.platform = platform;
  }

}
