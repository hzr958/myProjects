package com.smate.center.task.dyn.model.base;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 移动端动态内容更新任务状态表
 * 
 * @author LJ
 *
 *         2017年7月21日
 */
@Entity
@Table(name = "MOBILE_DYN_CONTENT_UPDATE")
public class MobileDynContentUpdate {

  private Long dynId; // 主键
  private String dyntype; // 动态类型
  private Integer updateStatus = 0; // 更新状态。默认为0表示未更新，1表示更新成功，2表示失败
  private Date updateDate; // 更新时间
  private String updateMsg; // 更新信息

  public MobileDynContentUpdate() {
    super();
  }

  @Id
  @Column(name = "DYN_ID")
  public Long getDynId() {
    return dynId;
  }

  public void setDynId(Long dynId) {
    this.dynId = dynId;
  }

  @Column(name = "UPDATE_STATUS")
  public Integer getUpdateStatus() {
    return updateStatus;
  }

  public void setUpdateStatus(Integer updateStatus) {
    this.updateStatus = updateStatus;
  }

  @Column(name = "UPDATE_DATE")
  public Date getUpdateDate() {
    return updateDate;
  }

  public void setUpdateDate(Date updateDate) {
    this.updateDate = updateDate;
  }

  @Column(name = "DYN_TYPE")
  public String getDyntype() {
    return dyntype;
  }

  public void setDyntype(String dyntype) {
    this.dyntype = dyntype;
  }

  @Column(name = "UPDATE_MSG")
  public String getUpdateMsg() {
    return updateMsg;
  }

  public void setUpdateMsg(String updateMsg) {
    this.updateMsg = updateMsg;
  }

  public MobileDynContentUpdate(Long dynId, String dyntype, Integer updateStatus, Date updateDate, String updateMsg) {
    super();
    this.dynId = dynId;
    this.dyntype = dyntype;
    this.updateStatus = updateStatus;
    this.updateDate = updateDate;
    this.updateMsg = updateMsg;
  }

  public MobileDynContentUpdate(Long dynId, Integer updateStatus, Date updateDate, String updateMsg) {
    super();
    this.dynId = dynId;
    this.updateStatus = updateStatus;
    this.updateDate = updateDate;
    this.updateMsg = updateMsg;
  }

}
