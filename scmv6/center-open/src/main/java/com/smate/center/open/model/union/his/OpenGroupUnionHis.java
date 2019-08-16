package com.smate.center.open.model.union.his;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 互联互通 关联群组 历史表
 */
@Entity
@Table(name = "v_open_group_union_HIS")
public class OpenGroupUnionHis {


  @Id
  @Column(name = "ID")
  private Long id;
  @Column(name = "GROUP_CODE")
  private String groupCode;

  @Column(name = "OWNER_PSN_ID")
  private Long ownerPsnId;

  @Column(name = "OWNER_OPEN_ID")
  private Long ownerOpenId;

  @Column(name = "GROUP_ID")
  private Long groupId;

  @Column(name = "TOKEN")
  private String token;

  @Column(name = "CREATETIME")
  private Date createTime;
  @Column(name = "del_time")
  private Date delTime; // 移动到历史表 时间
  @Column(name = "del_type")
  private String delType; // 什么情况下移动到历史表的 //什么情况下移动到历史表的 1合并， 2群组删除

  @Column(name = "DEAL_DATE")
  private Date dealDate; // 处理时间

  @Column(name = "status")
  private Integer status; // 0 未处理 ,1已经处理

  public Integer getStatus() {
    return status;
  }

  public void setStatus(Integer status) {
    this.status = status;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getGroupCode() {
    return groupCode;
  }

  public void setGroupCode(String groupCode) {
    this.groupCode = groupCode;
  }

  public Long getOwnerPsnId() {
    return ownerPsnId;
  }

  public void setOwnerPsnId(Long ownerPsnId) {
    this.ownerPsnId = ownerPsnId;
  }

  public Long getGroupId() {
    return groupId;
  }

  public void setGroupId(Long groupId) {
    this.groupId = groupId;
  }

  public String getToken() {
    return token;
  }

  public void setToken(String token) {
    this.token = token;
  }

  public Date getCreateTime() {
    return createTime;
  }

  public void setCreateTime(Date createTime) {
    this.createTime = createTime;
  }

  public Long getOwnerOpenId() {
    return ownerOpenId;
  }

  public void setOwnerOpenId(Long ownerOpenId) {
    this.ownerOpenId = ownerOpenId;
  }

  public Date getDelTime() {
    return delTime;
  }

  public void setDelTime(Date delTime) {
    this.delTime = delTime;
  }

  public String getDelType() {
    return delType;
  }

  public void setDelType(String delType) {
    this.delType = delType;
  }

  public Date getDealDate() {
    return dealDate;
  }

  public void setDealDate(Date dealDate) {
    this.dealDate = dealDate;
  }

}
