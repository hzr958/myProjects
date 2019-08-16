package com.smate.center.merge.model.sns.task;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * 帐号合并备份记录表
 * 
 * @author tsz
 *
 */
@Entity
@Table(name = "V_ACCOUNTS_MERGE_DATA")
public class AccountsMergeData {

  @Id
  @Column(name = "ID")
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "V_SEQ_ACCOUNTS_MERGE_DATA", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
  private Long id;
  // 合并人主键
  @Column(name = "SAVE_PSN_ID")
  private Long savePsnId;
  // 被合并人主键
  @Column(name = "DEL_PSN_ID")
  private Long delPsnId;
  // 错误信息
  @Column(name = "DESC_MSG")
  private String descMsg;
  // 合并日期
  @Column(name = "CREATE_DATE")
  private Date createDate;
  // 合并类型 1=合并，0=删除
  @Column(name = "OP_TYPE")
  private Long opType;
  // 备份数据
  @Column(name = "BAK_DATA")
  private String bakData;
  // 状态
  @Column(name = "STATUS")
  private Long status;

  public AccountsMergeData() {
    super();
  }

  public AccountsMergeData(Long id, Long savePsnId, Long delPsnId, String descMsg, Date createDate, Long opType,
      String bakData, Long status) {
    super();
    this.id = id;
    this.savePsnId = savePsnId;
    this.delPsnId = delPsnId;
    this.descMsg = descMsg;
    this.createDate = createDate;
    this.opType = opType;
    this.bakData = bakData;
    this.status = status;
  }

  public AccountsMergeData(Long savePsnId, Long delPsnId, String descMsg, Date createDate, Long opType, String bakData,
      Long status) {
    super();
    this.savePsnId = savePsnId;
    this.delPsnId = delPsnId;
    this.descMsg = descMsg;
    this.createDate = createDate;
    this.opType = opType;
    this.bakData = bakData;
    this.status = status;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Long getSavePsnId() {
    return savePsnId;
  }

  public void setSavePsnId(Long savePsnId) {
    this.savePsnId = savePsnId;
  }

  public Long getDelPsnId() {
    return delPsnId;
  }

  public void setDelPsnId(Long delPsnId) {
    this.delPsnId = delPsnId;
  }

  public String getDescMsg() {
    return descMsg;
  }

  public void setDescMsg(String descMsg) {
    this.descMsg = descMsg;
  }

  public Date getCreateDate() {
    return createDate;
  }

  public void setCreateDate(Date createDate) {
    this.createDate = createDate;
  }

  public Long getOpType() {
    return opType;
  }

  public void setOpType(Long opType) {
    this.opType = opType;
  }

  public String getBakData() {
    return bakData;
  }

  public void setBakData(String bakData) {
    this.bakData = bakData;
  }

  public Long getStatus() {
    return status;
  }

  public void setStatus(Long status) {
    this.status = status;
  }

  @Override
  public String toString() {
    return "AccountsMergeData [id=" + id + ", savePsnId=" + savePsnId + ", delPsnId=" + delPsnId + ", descMsg="
        + descMsg + ", createDate=" + createDate + ", opType=" + opType + ", bakData=" + bakData + ", status=" + status
        + "]";
  }

}
