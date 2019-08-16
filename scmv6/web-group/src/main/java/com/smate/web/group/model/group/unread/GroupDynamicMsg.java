package com.smate.web.group.model.group.unread;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 群组动态信息表 群组动态主表
 * 
 * @author ZZX
 *
 */
@Entity
@Table(name = "V_GROUP_DYNAMIC_MSG")
public class GroupDynamicMsg {
  @Id
  @Column(name = "DYN_ID")
  private Long dynId;// 动态id
  @Column(name = "GROUP_ID")
  private Long groupId;// 群组id
  @Column(name = "PRODUCER")
  private Long producer;// 创建人ID
  @Column(name = "DYN_TYPE")
  private String dynType;// 动态类型
  @Column(name = "DYN_TMP")
  private String dynTmp;// 动态模版
  @Column(name = "CREATE_DATE")
  private Date createDate;// 创建时间
  @Column(name = "UPDATE_DATE")
  private Date updateDate;// 最后更新时间
  @Column(name = "STATUS")
  private Integer status;// 状态 正常0， 删除99
  @Column(name = "REL_DEAL_STATUS")
  private Integer relDealStatus;// 关系处理状态 0 未处理 1 已经处理
  @Column(name = "RES_ID")
  private Long resId;// 资源Id
  @Column(name = "RES_TYPE")
  private String resType;// 资源类型
  @Column(name = "SAME_FLAG")
  private Long sameFlag;// 动态来源（父级动态id）
  @Column(name = "EXTEND")
  private String extend;// 预留字段

  public Long getDynId() {
    return dynId;
  }

  public void setDynId(Long dynId) {
    this.dynId = dynId;
  }

  public Long getGroupId() {
    return groupId;
  }

  public void setGroupId(Long groupId) {
    this.groupId = groupId;
  }

  public Long getProducer() {
    return producer;
  }

  public void setProducer(Long producer) {
    this.producer = producer;
  }

  public String getDynType() {
    return dynType;
  }

  public void setDynType(String dynType) {
    this.dynType = dynType;
  }

  public String getDynTmp() {
    return dynTmp;
  }

  public void setDynTmp(String dynTmp) {
    this.dynTmp = dynTmp;
  }

  public Date getCreateDate() {
    return createDate;
  }

  public void setCreateDate(Date createDate) {
    this.createDate = createDate;
  }

  public Date getUpdateDate() {
    return updateDate;
  }

  public void setUpdateDate(Date updateDate) {
    this.updateDate = updateDate;
  }

  public Integer getStatus() {
    return status;
  }

  public void setStatus(Integer status) {
    this.status = status;
  }

  public Integer getRelDealStatus() {
    return relDealStatus;
  }

  public void setRelDealStatus(Integer relDealStatus) {
    this.relDealStatus = relDealStatus;
  }

  public Long getResId() {
    return resId;
  }

  public void setResId(Long resId) {
    this.resId = resId;
  }

  public String getResType() {
    return resType;
  }

  public void setResType(String resType) {
    this.resType = resType;
  }

  public Long getSameFlag() {
    return sameFlag;
  }

  public void setSameFlag(Long sameFlag) {
    this.sameFlag = sameFlag;
  }

  public String getExtend() {
    return extend;
  }

  public void setExtend(String extend) {
    this.extend = extend;
  }

}
