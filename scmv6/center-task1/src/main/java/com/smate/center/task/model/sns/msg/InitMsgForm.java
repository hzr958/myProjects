package com.smate.center.task.model.sns.msg;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 初始化 msg form
 * 
 * @author cht
 *
 */
@Entity
@Table(name = "test_V_MSG_RELATION_content")
public class InitMsgForm implements Serializable {
  private static final long serialVersionUID = 1L;
  /**
   * 主键ID
   */
  @Id
  @Column(name = "ID")
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
   * ： 0=系统消息、1=请求添加好友消息、2=成果认领、3=全文认领、4=成果推荐、5=好友推荐、 6=基金推荐、7=站内信、8=请求加入群组消息、9=邀请加入群组消息、10=群组动向 ,
   * 11=请求全文消息
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
  /**
   * 消息内容，存放json、 页面显示需要的字段 （人员信息除外）
   */
  @Column(name = "CONTENT")
  private String content;
  /**
   * 1=已经跑过
   */
  @Column(name = "task_Status")
  private Integer taskStatus;

  public Integer getTaskStatus() {
    return taskStatus;
  }

  public void setTaskStatus(Integer taskStatus) {
    this.taskStatus = taskStatus;
  }

  public InitMsgForm() {
    super();
  }

  public InitMsgForm(Long id, Long senderId, Long receiverId, Long contentId, Integer type, Date createDate,
      Integer status, Integer dealStatus, Date dealDate, String content, Integer taskStatus) {
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
    this.content = content;
    this.taskStatus = taskStatus;
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

  public Long getContentId() {
    return contentId;
  }

  public void setContentId(Long contentId) {
    this.contentId = contentId;
  }

  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content = content;
  }

}
