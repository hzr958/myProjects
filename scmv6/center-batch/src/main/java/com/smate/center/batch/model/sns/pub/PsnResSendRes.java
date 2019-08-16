package com.smate.center.batch.model.sns.pub;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * 推荐资源分享主表与推荐资源的关系.
 * 
 * @author pwl
 * 
 */
@Entity
@Table(name = "PSN_RES_SEND_RES")
public class PsnResSendRes implements Serializable {
  /**
   * 
   */
  private static final long serialVersionUID = -4432136474438265325L;
  private Long id;
  /** 对应表PSN_SEND_RES的RES_SEND_ID字段. */
  private Long resSendId;
  private Long resId;
  /** 资源类型，0其他，1成果，2文献，3文件，4项目. */
  private int resType = 0;
  private int resNodeId = 1;
  /** 分享基准库成果使用. */
  private Integer dbid;
  /** 是否本人分享. */
  private Integer isOwnerShare = 0;

  private String resZhTitle;
  private String resEnTitle;

  @Id
  @Column(name = "ID")
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_PSN_RES_SEND_RES", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
  public Long getId() {
    return id;
  }

  @Column(name = "RES_SEND_ID")
  public Long getResSendId() {
    return resSendId;
  }

  @Column(name = "RES_ID")
  public Long getResId() {
    return resId;
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

  public void setDbid(Integer dbid) {
    this.dbid = dbid;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public void setResSendId(Long resSendId) {
    this.resSendId = resSendId;
  }

  public void setResId(Long resId) {
    this.resId = resId;
  }

  public void setResType(int resType) {
    this.resType = resType;
  }

  public void setResNodeId(int resNodeId) {
    this.resNodeId = resNodeId;
  }

  @Column(name = "IS_OWNER_SHARE")
  public Integer getIsOwnerShare() {
    return isOwnerShare;
  }

  public void setIsOwnerShare(Integer isOwnerShare) {
    this.isOwnerShare = isOwnerShare;
  }

  @Transient
  public String getResZhTitle() {
    return resZhTitle;
  }

  @Transient
  public String getResEnTitle() {
    return resEnTitle;
  }

  public void setResZhTitle(String resZhTitle) {
    this.resZhTitle = resZhTitle;
  }

  public void setResEnTitle(String resEnTitle) {
    this.resEnTitle = resEnTitle;
  }
}
