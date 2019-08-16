package com.smate.web.psn.model.friend;

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
 * 好友请求记录
 *
 * @author wsn
 * @createTime 2017年7月25日 下午4:50:29
 *
 */
@Entity
@Table(name = "FRIEND_REQ_RECORD")
public class FriendReqRecord implements Serializable {

  private static final long serialVersionUID = -9074236772434971579L;

  private Long id;// 主键
  private Long sendPsnId; // 发送者ID
  private Long receivePsnId; // 接收者ID
  private Date createTime;// 请求创建时间
  private Date dealTime; // 处理时间
  private Integer status; // 处理状态

  public FriendReqRecord(Long id, Long sendPsnId, Long receivePsnId, Date createTime, Date dealTime, Integer status) {
    super();
    this.id = id;
    this.sendPsnId = sendPsnId;
    this.receivePsnId = receivePsnId;
    this.createTime = createTime;
    this.dealTime = dealTime;
    this.status = status;
  }

  public FriendReqRecord() {
    super();
  }

  @Id
  @Column(name = "ID")
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_FRIEND_REQ_RECORD", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  @Column(name = "SEND_PSN_ID")
  public Long getSendPsnId() {
    return sendPsnId;
  }

  public void setSendPsnId(Long sendPsnId) {
    this.sendPsnId = sendPsnId;
  }

  @Column(name = "RECEIVE_PSN_ID")
  public Long getReceivePsnId() {
    return receivePsnId;
  }

  public void setReceivePsnId(Long receivePsnId) {
    this.receivePsnId = receivePsnId;
  }

  @Column(name = "CREATE_DATE")
  public Date getCreateTime() {
    return createTime;
  }

  public void setCreateTime(Date createTime) {
    this.createTime = createTime;
  }

  @Column(name = "DEAL_DATE")
  public Date getDealTime() {
    return dealTime;
  }

  public void setDealTime(Date dealTime) {
    this.dealTime = dealTime;
  }

  @Column(name = "STATUS")
  public Integer getStatus() {
    return status;
  }

  public void setStatus(Integer status) {
    this.status = status;
  }

}
