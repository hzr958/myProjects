package com.smate.center.batch.model.psn;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * cwli推荐资源之接收的资源.
 */
@Entity
@Table(name = "PSN_RES_RECEIVE_RES")
public class PsnResReceiveRes implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = 5684455428793518773L;
  private Long id;
  private PsnResReceive resReceive;
  private Long resId;
  private int resType = 0;
  private int resNodeId = 1;
  private Integer dbid;
  private Long resRecId;
  // 旧的资源ID
  private Long oldResId;
  // 旧的资源的拥有者ID
  private Long oldOwnerPsnId;
  // 旧资源的类型
  private Integer oldResType;
  // 0：未确认，1：已确认，2：已取消
  private Integer status;

  private Long senderId;

  private Date shareDate;
  /** 是否本人分享. */
  private Integer isOwnerShare = 0;

  public PsnResReceiveRes() {}

  public PsnResReceiveRes(Long id, Long resId, int resType, int resNodeId, Integer dbid, Long resRecId, Integer status,
      Long senderId, Date shareDate) {
    this.id = id;
    this.resId = resId;
    this.resType = resType;
    this.resNodeId = resNodeId;
    this.dbid = dbid;
    this.resRecId = resRecId;
    this.status = status;
    this.senderId = senderId;
    this.shareDate = shareDate;
  }

  @Id
  @Column(name = "ID")
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_PSN_RES_RECEIVE_RES", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  @Column(name = "RES_ID")
  public Long getResId() {
    return resId;
  }

  public void setResId(Long resId) {
    this.resId = resId;
  }

  @Column(name = "OLD_RES_ID")
  public Long getOldResId() {
    return oldResId;
  }

  public void setOldResId(Long oldResId) {
    this.oldResId = oldResId;
  }

  @Column(name = "OLD_RES_TYPE")
  public Integer getOldResType() {
    return oldResType;
  }

  public void setOldResType(Integer oldResType) {
    this.oldResType = oldResType;
  }

  @Column(name = "OLD_OWNER_PSNID")
  public Long getOldOwnerPsnId() {
    return oldOwnerPsnId;
  }

  public void setOldOwnerPsnId(Long oldOwnerPsnId) {
    this.oldOwnerPsnId = oldOwnerPsnId;
  }

  @Column(name = "STATUS")
  public Integer getStatus() {
    return status;
  }

  public void setStatus(Integer status) {
    this.status = status;
  }

  @Column(name = "RES_REC_ID")
  public Long getResRecId() {
    return resRecId;
  }

  public void setResRecId(Long resRecId) {
    this.resRecId = resRecId;
  }

  @ManyToOne(cascade = {CascadeType.ALL}, fetch = FetchType.LAZY)
  @JoinColumn(name = "RES_REC_ID", insertable = false, updatable = false)
  @JsonIgnore
  public PsnResReceive getResReceive() {
    return resReceive;
  }

  public void setResReceive(PsnResReceive resReceive) {
    this.resReceive = resReceive;
  }

  @Column(name = "RES_TYPE")
  public int getResType() {
    return resType;
  }

  @Column(name = "RES_NODE_ID")
  public int getResNodeId() {
    return resNodeId;
  }

  @Column(name = "DB_ID")
  public Integer getDbid() {
    return dbid;
  }

  public void setResType(int resType) {
    this.resType = resType;
  }

  public void setResNodeId(int resNodeId) {
    this.resNodeId = resNodeId;
  }

  public void setDbid(Integer dbid) {
    this.dbid = dbid;
  }

  @Column(name = "IS_OWNER_SHARE")
  public Integer getIsOwnerShare() {
    return isOwnerShare;
  }

  public void setIsOwnerShare(Integer isOwnerShare) {
    this.isOwnerShare = isOwnerShare;
  }

  @Transient
  public Long getSenderId() {
    return senderId;
  }

  public void setSenderId(Long senderId) {
    this.senderId = senderId;
  }

  @Transient
  public Date getShareDate() {
    return shareDate;
  }

  public void setShareDate(Date shareDate) {
    this.shareDate = shareDate;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

}
