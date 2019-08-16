package com.smate.center.batch.model.sns.pub;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang3.StringUtils;

/**
 * 动态信息model.
 * 
 * @author chenxiangrong
 * 
 */
@Entity
@Table(name = "DYNAMIC_MESSAGE")
public class Dynamic implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = -8345972696495024488L;
  private Long dynId;// 动态ID
  private Long dynParentId;// 父动态ID（同步及转发时）
  private Long producer;// 动态产生者
  private Long receiver;// 动态接受者（即查看者）
  private Long objectOwner;// 内容拥有者
  private Long groupId;// 群组ID
  private int tmpId;// 模板ID
  private int dynType;// 动态类型
  private String sameFlag;// 相同标示
  private int relation;// 动态产生者与接受者的关系
  private int visible;// 动态可见性（查询用）
  private int permission;// 动态查看权限（同步用）
  private Date dynDate;// 动态产生时间
  private Date updateDate;// 动态更新时间
  private int status;// 动态状态
  private int syncFlag;// 同步标识
  private Long dcId;// 动态内容id

  private String dynContent;// 构建的动态内容

  private String dynJson;

  public Dynamic() {}

  public Dynamic(Long dynId, String dynJson) {
    this.dynId = dynId;
    this.dynJson = dynJson;
  }

  @Id
  @Column(name = "DYN_ID")
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_DYNAMIC_MESSAGE", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
  public Long getDynId() {
    return dynId;
  }

  public void setDynId(Long dynId) {
    this.dynId = dynId;
  }

  @Column(name = "DYN_PARENT_ID")
  public Long getDynParentId() {
    return dynParentId;
  }

  public void setDynParentId(Long dynParentId) {
    this.dynParentId = dynParentId;
  }

  @Column(name = "PRODUCER")
  public Long getProducer() {
    return producer;
  }

  public void setProducer(Long producer) {
    this.producer = producer;
  }

  @Column(name = "RECEIVER")
  public Long getReceiver() {
    return receiver;
  }

  public void setReceiver(Long receiver) {
    this.receiver = receiver;
  }

  @Column(name = "OBJECT_OWNER")
  public Long getObjectOwner() {
    return objectOwner;
  }

  public void setObjectOwner(Long objectOwner) {
    this.objectOwner = objectOwner;
  }

  @Column(name = "GROUP_ID")
  public Long getGroupId() {
    return groupId;
  }

  public void setGroupId(Long groupId) {
    this.groupId = groupId;
  }

  @Column(name = "TMP_ID")
  public int getTmpId() {
    return tmpId;
  }

  public void setTmpId(int tmpId) {
    this.tmpId = tmpId;
  }

  @Column(name = "DYN_TYPE")
  public int getDynType() {
    return dynType;
  }

  public void setDynType(int dynType) {
    this.dynType = dynType;
  }

  @Column(name = "SAME_FLAG")
  public String getSameFlag() {
    return sameFlag;
  }

  public void setSameFlag(String sameFlag) {
    this.sameFlag = sameFlag;
  }

  @Column(name = "RELATION")
  public int getRelation() {
    return relation;
  }

  public void setRelation(int relation) {
    this.relation = relation;
  }

  @Column(name = "VISIBLE")
  public int getVisible() {
    return visible;
  }

  public void setVisible(int visible) {
    this.visible = visible;
  }

  @Column(name = "PERMISSION")
  public int getPermission() {
    return permission;
  }

  public void setPermission(int permission) {
    this.permission = permission;
  }

  @Column(name = "DYN_DATE")
  public Date getDynDate() {
    return dynDate;
  }

  public void setDynDate(Date dynDate) {
    this.dynDate = dynDate;
  }

  @Column(name = "UPDATE_DATE")
  public Date getUpdateDate() {
    return updateDate;
  }

  public void setUpdateDate(Date updateDate) {
    this.updateDate = updateDate;
  }

  @Column(name = "STATUS")
  public int getStatus() {
    return status;
  }

  public void setStatus(int status) {
    this.status = status;
  }

  @Column(name = "SYNC_FLAG")
  public int getSyncFlag() {
    return syncFlag;
  }

  public void setSyncFlag(int syncFlag) {
    this.syncFlag = syncFlag;
  }

  @Column(name = "DC_ID")
  public Long getDcId() {
    return dcId;
  }

  public void setDcId(Long dcId) {
    this.dcId = dcId;
  }

  @Transient
  public String getDynContent() {
    return dynContent;
  }

  public void setDynContent(String dynContent) {
    this.dynContent = dynContent;
  }

  @Transient
  public String getDynJson() {
    return dynJson;
  }

  public void setDynJson(String dynJson) {
    this.dynJson = dynJson;
  }

  public String replacePubTypes(String source) {
    if (StringUtils.isBlank(source))
      return source;
    source = source.replace(".会议论文", "");
    source = source.replace(".Conference Paper", "");
    source = source.replace(".期刊论文", "");
    source = source.replace(".Journal Article", "");
    source = source.replace(".奖励", "");
    source = source.replace(".Award", "");
    source = source.replace(".书/著作", "");
    source = source.replace(".Book/Monograph", "");
    source = source.replace(".书籍章节", "");
    source = source.replace(".Book Chapter", "");
    source = source.replace(".其它", "");
    source = source.replace(".其他", "");
    source = source.replace(".Others", "");
    source = source.replace(".专利", "");
    source = source.replace(".Patent", "");
    source = source.replace(".学位论文", "");
    source = source.replace(".Thesis", "");
    return source;
  }
}
