package com.smate.center.batch.model.pdwh.pub;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 分配、匹配单位任务配置.
 * 
 * @author liqinghua
 * 
 */
@Entity
@Table(name = "DBCACHE_ASSIGN_TASK")
public class DbCacheAssignTask implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = 2656639783920750396L;

  private Integer id;
  private Long startId;
  private Long maxId;
  private Integer maxErrorNum;
  private Integer errorNum;
  private Integer enabled;
  private Date startTime;
  private Date endTime;

  @Id
  @Column(name = "ID")
  public Integer getId() {
    return id;
  }

  @Column(name = "START_ID")
  public Long getStartId() {
    return startId;
  }

  @Column(name = "MAX_ID")
  public Long getMaxId() {
    return maxId;
  }

  @Column(name = "MAX_ERROR_NUM")
  public Integer getMaxErrorNum() {
    return maxErrorNum;
  }

  @Column(name = "ERROR_NUM")
  public Integer getErrorNum() {
    return errorNum;
  }

  @Column(name = "ENABLED")
  public Integer getEnabled() {
    return enabled;
  }

  @Column(name = "START_TIME")
  public Date getStartTime() {
    return startTime;
  }

  @Column(name = "END_TIME")
  public Date getEndTime() {
    return endTime;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public void setStartId(Long startId) {
    this.startId = startId;
  }

  public void setMaxId(Long maxId) {
    this.maxId = maxId;
  }

  public void setMaxErrorNum(Integer maxErrorNum) {
    this.maxErrorNum = maxErrorNum;
  }

  public void setErrorNum(Integer errorNum) {
    this.errorNum = errorNum;
  }

  public void setEnabled(Integer enabled) {
    this.enabled = enabled;
  }

  public void setStartTime(Date startTime) {
    this.startTime = startTime;
  }

  public void setEndTime(Date endTime) {
    this.endTime = endTime;
  }

}
