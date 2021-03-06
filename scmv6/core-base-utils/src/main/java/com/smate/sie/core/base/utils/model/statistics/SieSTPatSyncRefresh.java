package com.smate.sie.core.base.utils.model.statistics;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "ST_PAT_SYNC_REFRESH")
public class SieSTPatSyncRefresh implements Serializable {

  private static final long serialVersionUID = -2917752518129676101L;
  private Long insId;
  private Integer status;// 处理状态，0未处理(默认),1已处理,99失败
  private Date lastRunTime;

  public SieSTPatSyncRefresh() {
    super();
  }

  public SieSTPatSyncRefresh(Long insId, Integer status) {
    super();
    this.insId = insId;
    this.status = status;
  }


  @Id
  @Column(name = "INS_ID")
  public Long getInsId() {
    return insId;
  }

  public void setInsId(Long insId) {
    this.insId = insId;
  }

  @Column(name = "STATUS")
  public Integer getStatus() {
    return status;
  }

  public void setStatus(Integer status) {
    this.status = status;
  }

  @Column(name = "LAST_RUN_TIME")
  public Date getLastRunTime() {
    return lastRunTime;
  }

  public void setLastRunTime(Date lastRunTime) {
    this.lastRunTime = lastRunTime;
  }


}
