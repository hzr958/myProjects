package com.smate.center.batch.model.sns.psn;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * 好友群组关系操作任务表
 * 
 * @author zzx
 *
 */
@Entity
@Table(name = "FRIEND_GROUP_OPERATE_TASK")
public class FriendGroupOperateTaskInfo implements Serializable {

  private static final long serialVersionUID = -6642711891936369993L;

  private Long id;

  private Long psnId;// 当前节点的用户ID

  private Long bizId;

  // 操作，1：添加好友；2：删除好友；3：加入群组；4：退出或删除群组
  private Integer operation;

  public FriendGroupOperateTaskInfo() {
    super();
  }

  public FriendGroupOperateTaskInfo(Long psnId, Long bizId, Integer operation) {
    super();
    this.psnId = psnId;
    this.bizId = bizId;
    this.operation = operation;
  }

  @Id
  @Column(name = "ID")
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_FRIEND_GROUP_OPERATE_TASK", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
  public Long getId() {
    return id;
  }

  @Column(name = "PSN_ID")
  public Long getPsnId() {
    return psnId;
  }

  @Column(name = "BIZ_ID")
  public Long getBizId() {
    return bizId;
  }

  @Column(name = "OPERATION")
  public Integer getOperation() {
    return operation;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public void setPsnId(Long psnId) {
    this.psnId = psnId;
  }

  public void setBizId(Long bizId) {
    this.bizId = bizId;
  }

  public void setOperation(Integer operation) {
    this.operation = operation;
  }
}
