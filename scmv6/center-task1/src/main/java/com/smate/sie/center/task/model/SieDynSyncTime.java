package com.smate.sie.center.task.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 动态信息同步时间
 * 
 * @author yxy
 */
@Entity
@Table(name = "DYN_SYNC_TIME")
public class SieDynSyncTime {

  @Id
  @Column(name = "ID")
  private Long syncId;

  @Column(name = "UPDATE_TIME")
  private Date updateTime;

  public Long getSyncId() {
    return syncId;
  }

  public Date getUpdateTime() {
    return updateTime;
  }

  public void setSyncId(Long syncId) {
    this.syncId = syncId;
  }

  public void setUpdateTime(Date updateTime) {
    this.updateTime = updateTime;
  }

}
