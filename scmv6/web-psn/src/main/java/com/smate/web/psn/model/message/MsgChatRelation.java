package com.smate.web.psn.model.message;

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
 * 站内信聊天关系实体类
 * 
 * @author zzx
 *
 */
@Entity
@Table(name = "V_MSG_CHAT_RELATION")
public class MsgChatRelation implements Serializable {
  private static final long serialVersionUID = 1L;
  /**
   * ID
   */
  @Id
  @Column(name = "ID")
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_V_MSG_CHAT_RELATION", allocationSize = 1)
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
   * 聊天状态 ：0=正常 、1=删除（删除状态再次发送消息时会重新设置为0）
   */
  @Column(name = "STATUS")
  private Integer status;
  /**
   * 更新时间
   */
  @Column(name = "UPDATE_DATE")
  private Date updateDate;
  /**
   * 最新会话消息内容
   */
  @Column(name = "CONTENT_NEWEST")
  private String contentNewest;

  public String getContentNewest() {
    return contentNewest;
  }

  public void setContentNewest(String contentNewest) {
    this.contentNewest = contentNewest;
  }

  public MsgChatRelation(Long id, Long senderId, Long receiverId, Integer status, Date updateDate,
      String contentNewest) {
    super();
    this.id = id;
    this.senderId = senderId;
    this.receiverId = receiverId;
    this.status = status;
    this.updateDate = updateDate;
    this.contentNewest = contentNewest;
  }

  public Date getUpdateDate() {
    return updateDate;
  }

  public void setUpdateDate(Date updateDate) {
    this.updateDate = updateDate;
  }

  public MsgChatRelation() {
    super();
  }

  public MsgChatRelation(Long id, Long senderId, Long receiverId, Integer status, Date updateDate) {
    super();
    this.id = id;
    this.senderId = senderId;
    this.receiverId = receiverId;
    this.status = status;
    this.updateDate = updateDate;
  }

  public MsgChatRelation(Long id, Long senderId, Long receiverId, Integer status) {
    super();
    this.id = id;
    this.senderId = senderId;
    this.receiverId = receiverId;
    this.status = status;
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

  public Integer getStatus() {
    return status;
  }

  public void setStatus(Integer status) {
    this.status = status;
  }

  @Override
  public String toString() {
    return "MsgChatRelation [senderId=" + senderId + ", receiverId=" + receiverId + ", status=" + status + "]";
  }

}
