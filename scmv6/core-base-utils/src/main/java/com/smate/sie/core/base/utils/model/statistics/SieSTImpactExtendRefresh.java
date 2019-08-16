package com.smate.sie.core.base.utils.model.statistics;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Table(name = "ST_IMPACT_EXTEND_REFRESH")
public class SieSTImpactExtendRefresh implements Serializable {

  private static final long serialVersionUID = -137043867666959018L;
  private Long insId;
  private Integer status;// 处理状态，0未处理(默认),1已处理,99失败
  private Date lastRunTime;
  private Date lastStartTime;
  private Date lastEndTime;
  private Long lastUseTime;

  public SieSTImpactExtendRefresh() {
    super();
  }



  public SieSTImpactExtendRefresh(Long insId, Integer status) {
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


  @Column(name = "LAST_START_TIME")
  public Date getLastStartTime() {
    return lastStartTime;
  }



  public void setLastStartTime(Date lastStartTime) {
    this.lastStartTime = lastStartTime;
  }


  @Column(name = "LAST_END_TIME")
  public Date getLastEndTime() {
    return lastEndTime;
  }



  public void setLastEndTime(Date lastEndTime) {
    this.lastEndTime = lastEndTime;
  }



  @Column(name = "LAST_USE_TIME")
  public Long getLastUseTime() {
    return lastUseTime;
  }



  public void setLastUseTime(Long lastUseTime) {
    this.lastUseTime = lastUseTime;
  }

}
