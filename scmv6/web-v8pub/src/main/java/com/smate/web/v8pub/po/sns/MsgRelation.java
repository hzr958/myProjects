package com.smate.web.v8pub.po.sns;

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
 * 消息关系实体类
 * 
 * @author zzx
 *
 */
@Entity
@Table(name = "V_MSG_RELATION")
public class MsgRelation implements Serializable {
  private static final long serialVersionUID = 1L;
  /**
   * 主键ID
   */
  @Id
  @Column(name = "ID")
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_V_MSG_RELATION", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
  private Long id;
  /**
   * 发送者ID
   */
  @Column(name = "SENDER_ID")
  private Long senderId;
  /**
   * 接收者ID
   */
  @Column(name = "RECEIVER_ID")
  private Long receiverId;
  /**
   * 消息内容ID
   */
  @Column(name = "CONTENT_ID")
  private Long contentId;
  /**
   * 消息类型： 0=系统消息、1=好友请求、2=成果认领、3=全文认领、4=成果推荐、5=好友推荐、6=基金推荐、7=站内信、8=群组请求、9=好友动向、10=群组动向
   */
  @Column(name = "TYPE")
  private Integer type;
  /**
   * 创建时间
   */
  @Column(name = "CREATE_DATE")
  private Date createDate;
  /**
   * 消息状态：0=未读、1=已读、2=删除
   */
  @Column(name = "STATUS")
  private Integer status;
  /**
   * 消息处理状态：0=未处理、1=同意、2=拒绝/忽略
   */
  @Column(name = "DEAL_STATUS")
  private Integer dealStatus;
  /**
   * 消息处理时间
   */
  @Column(name = "DEAL_DATE")
  private Date dealDate;

  public MsgRelation() {
    super();
  }

  public MsgRelation(Long id, Long senderId, Long receiverId, Long contentId, Integer type, Date createDate,
      Integer status, Integer dealStatus, Date dealDate) {
    super();
    this.id = id;
    this.senderId = senderId;
    this.receiverId = receiverId;
    this.contentId = contentId;
    this.type = type;
    this.createDate = createDate;
    this.status = status;
    this.dealStatus = dealStatus;
    this.dealDate = dealDate;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Long getSenderId() {
    return senderId;
  }

  public void setSenderId(Long senderId) {
    this.senderId = senderId;
  }

  public Long getReceiverId() {
    return receiverId;
  }

  public void setReceiverId(Long receiverId) {
    this.receiverId = receiverId;
  }

  public Long getContentId() {
    return contentId;
  }

  public void setContentId(Long contentId) {
    this.contentId = contentId;
  }

  public Integer getType() {
    return type;
  }

  public void setType(Integer type) {
    this.type = type;
  }

  public Date getCreateDate() {
    return createDate;
  }

  public void setCreateDate(Date createDate) {
    this.createDate = createDate;
  }

  public Integer getStatus() {
    return status;
  }

  public void setStatus(Integer status) {
    this.status = status;
  }

  public Integer getDealStatus() {
    return dealStatus;
  }

  public void setDealStatus(Integer dealStatus) {
    this.dealStatus = dealStatus;
  }

  public Date getDealDate() {
    return dealDate;
  }

  public void setDealDate(Date dealDate) {
    this.dealDate = dealDate;
  }

  @Override
  public String toString() {
    return "MsgRelation [id=" + id + ", senderId=" + senderId + ", receiverId=" + receiverId + ", contentId="
        + contentId + ", type=" + type + ", createDate=" + createDate + ", status=" + status + ", dealStatus="
        + dealStatus + ", dealDate=" + dealDate + "]";
  }

}
