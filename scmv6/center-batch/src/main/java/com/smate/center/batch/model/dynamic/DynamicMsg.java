package com.smate.center.batch.model.dynamic;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * 动态信息表
 * 
 * @author zk
 *
 */
@Entity
@Table(name = "V_DYNAMIC_MSG")
public class DynamicMsg implements Serializable {

  private static final long serialVersionUID = -80594302460847805L;

  private Long dynId; // 主键
  private Long producer; // 动态创建人
  private String dynType; // 动态类型
  private Integer dynTmp; // 动态模版
  private Integer permission; // 动态权限
  private Integer delstatus; // 状态,0：正常；99：删除; 1:实时动态(任务处理完，需变更为0)
  private Integer relDealStatus;// 关系处理状态位,0:未处理,1已处理(用于后台任务处理)
  private Long sameFlag; // 是否是相同资源产生的动态表示，放置父动态id
  private Integer fromType; // 动态来源类型id,1:个人,2:群组,3:机构主页,4..(用于查询过滤)
  private Long targetId; // 群组id/机构Id,默认0（值from_type有关）
  private Date createDate; // 动态信息生成时间
  private Date updateDate; // 动态信息更新时间
  private String dynData; // 动态数据
  private Long resId; // 资源id
  private Integer resType; // 资源类型
  private String platform; // pc端，移动端(mobile)

  @Column(name = "RES_ID")
  public Long getResId() {
    return resId;
  }


  public void setResId(Long resId) {
    this.resId = resId;
  }

  @Column(name = "RES_TYPE")
  public Integer getResType() {
    return resType;
  }

  public void setResType(Integer resType) {
    this.resType = resType;
  }

  public DynamicMsg() {}

  public DynamicMsg(Long dynId, Long producer) {
    this.dynId = dynId;
    this.producer = producer;
  }

  @Id
  @Column(name = "DYN_ID")
  public Long getDynId() {
    return dynId;
  }

  public void setDynId(Long dynId) {
    this.dynId = dynId;
  }

  @Column(name = "PRODUCER")
  public Long getProducer() {
    return producer;
  }

  public void setProducer(Long producer) {
    this.producer = producer;
  }

  @Column(name = "DYN_TYPE")
  public String getDynType() {
    return dynType;
  }

  public void setDynType(String dynType) {
    this.dynType = dynType;
  }

  @Column(name = "DYN_TMP")
  public Integer getDynTmp() {
    return dynTmp;
  }

  public void setDynTmp(Integer dynTmp) {
    this.dynTmp = dynTmp;
  }

  @Column(name = "PERMISSION")
  public Integer getPermission() {
    return permission;
  }

  public void setPermission(Integer permission) {
    this.permission = permission;
  }

  @Column(name = "DEL_STATUS")
  public Integer getDelstatus() {
    return delstatus;
  }

  public void setDelstatus(Integer delstatus) {
    this.delstatus = delstatus;
  }

  @Column(name = "CREATE_DATE")
  public Date getCreateDate() {
    return createDate;
  }

  public void setCreateDate(Date createDate) {
    this.createDate = createDate;
  }

  @Column(name = "UPDATE_DATE")
  public Date getUpdateDate() {
    return updateDate;
  }

  public void setUpdateDate(Date updateDate) {
    this.updateDate = updateDate;
  }

  @Transient
  public String getDynData() {
    return dynData;
  }

  public void setDynData(String dynData) {
    this.dynData = dynData;
  }

  @Column(name = "SAME_FLAG")
  public Long getSameFlag() {
    return sameFlag;
  }

  public void setSameFlag(Long sameFlag) {
    this.sameFlag = sameFlag;
  }

  @Column(name = "FROM_TYPE")
  public Integer getFromType() {
    return fromType;
  }

  public void setFromType(Integer fromType) {
    this.fromType = fromType;
  }

  @Column(name = "TARGET_ID")
  public Long getTargetId() {
    return targetId;
  }

  @Column(name = "REL_DEAL_STATUS")
  public Integer getRelDealStatus() {
    return relDealStatus;
  }

  public void setRelDealStatus(Integer relDealStatus) {
    this.relDealStatus = relDealStatus;
  }

  public void setTargetId(Long targetId) {
    this.targetId = targetId;
  }

  @Column(name = "PLATFORM")
  public String getPlatform() {
    return platform;
  }

  public void setPlatform(String platform) {
    this.platform = platform;
  }

}
