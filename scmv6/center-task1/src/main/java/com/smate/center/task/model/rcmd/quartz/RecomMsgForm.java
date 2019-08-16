package com.smate.center.task.model.rcmd.quartz;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "test_V_Recom_msg_form")
public class RecomMsgForm implements Serializable {
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
  // 数量
  @Column(name = "count")
  private Long count;
  // 类型 2=成果认领、3=全文认领
  @Column(name = "type")
  private Long type;
  // 资源Id
  @Column(name = "res_id")
  private Long resId;
  // 时间
  @Column(name = "recom_Date")
  private Date recomDate;
  /**
   * 1=已经跑过；0=未跑任务；2=异常
   */
  @Column(name = "task_Status")
  private Integer taskStatus;

  public RecomMsgForm() {
    super();
  }

  public RecomMsgForm(Long id, Long senderId, Long receiverId, Long count, Long type, Long resId, Integer taskStatus) {
    super();
    this.id = id;
    this.senderId = senderId;
    this.receiverId = receiverId;
    this.count = count;
    this.type = type;
    this.resId = resId;
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

  public Long getCount() {
    return count;
  }

  public void setCount(Long count) {
    this.count = count;
  }

  public Long getType() {
    return type;
  }

  public void setType(Long type) {
    this.type = type;
  }

  public Long getResId() {
    return resId;
  }

  public void setResId(Long resId) {
    this.resId = resId;
  }

  public Integer getTaskStatus() {
    return taskStatus;
  }

  public void setTaskStatus(Integer taskStatus) {
    this.taskStatus = taskStatus;
  }

}
