package com.smate.web.dyn.model.dynamic;

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
 * 资源分享详情model.
 * 
 * @author zk
 * 
 */
@Entity
@Table(name = "DYNAMIC_SHARE_RES")
public class DynamicShareRes implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = -466384657784234492L;
  private Long shareId;
  private int resType;
  private Long resId;
  private int resNode;
  private Long shareTimes;
  private Date updateDate;

  public DynamicShareRes() {}

  public DynamicShareRes(Long shareId, Long shareTimes) {
    this.shareId = shareId;
    this.shareTimes = shareTimes;
  }

  @Id
  @Column(name = "SHARE_ID")
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_DYNAMIC_SHARE_RES", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
  public Long getShareId() {
    return shareId;
  }

  public void setShareId(Long shareId) {
    this.shareId = shareId;
  }

  @Column(name = "RES_TYPE")
  public int getResType() {
    return resType;
  }

  public void setResType(int resType) {
    this.resType = resType;
  }

  @Column(name = "RES_ID")
  public Long getResId() {
    return resId;
  }

  public void setResId(Long resId) {
    this.resId = resId;
  }

  @Column(name = "RES_NODE")
  public int getResNode() {
    return resNode;
  }

  public void setResNode(int resNode) {
    this.resNode = resNode;
  }

  @Column(name = "SHARE_TIMES")
  public Long getShareTimes() {
    return shareTimes;
  }

  public void setShareTimes(Long shareTimes) {
    this.shareTimes = shareTimes;
  }

  @Column(name = "UPDATE_DATE")
  public Date getUpdateDate() {
    return updateDate;
  }

  public void setUpdateDate(Date updateDate) {
    this.updateDate = updateDate;
  }
}
