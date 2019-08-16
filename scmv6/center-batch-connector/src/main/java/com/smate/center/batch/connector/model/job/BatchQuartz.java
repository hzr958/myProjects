package com.smate.center.batch.connector.model.job;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * SpringBatch 定时任务列表--记录需要定时检查，向v_batch_jobs插入数据的任务
 * 
 * 
 */
@Entity
@Table(name = "V_BATCH_QUARTZ")
public class BatchQuartz implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = -3878414533496992697L;

  @Id
  @Column(name = "QUARTZ_ID")
  private Long quartzId;

  @Column(name = "STRATEGY")
  private String strategy; // 任务策略，8位标记Job的来源，一般来说，需要与v_batch_jobs策略，8位标记Job的来源中的Strategy对应

  @Column(name = "TASK_STATUS")
  private Integer taskStatus;// 1任务启用，0任务停用

  @Column(name = "START_TIME")
  private Date startTime;// 任务开始时间，初始值为创建时间

  @Column(name = "END_TIME")
  private Date endTime;// 任务结束时间，初始值为创建时间

  @Column(name = "PERIOD")
  private String period;// 任务执行周期，当前时间与上一次执行时间（END_TIME）之差大于period，该任务就应该执行了

  @Column(name = "EXECUTION_STATUS")
  private Integer executionStatus;// 1执行中，2执行成功，3执行失败

  @Column(name = "EXECUTION_MSG")
  private String executionMsg;// 主要记录任务错误信息，或则任务执行相关信息

  @Column(name = "EXECUTE_COUNT")
  private Integer executeCount;// 主要记录任务错误信息，或则任务执行相关信息

  @Column(name = "REMARK")
  private String remark;// 任务简介

  public BatchQuartz() {
    super();
  }

  public Long getQuartzId() {
    return quartzId;
  }

  public void setQuartzId(Long quartzId) {
    this.quartzId = quartzId;
  }

  public String getStrategy() {
    return strategy;
  }

  public void setStrategy(String strategy) {
    this.strategy = strategy;
  }

  public Integer getTaskStatus() {
    return taskStatus;
  }

  public void setTaskStatus(Integer taskStatus) {
    this.taskStatus = taskStatus;
  }

  public Date getStartTime() {
    return startTime;
  }

  public void setStartTime(Date startTime) {
    this.startTime = startTime;
  }

  public Date getEndTime() {
    return endTime;
  }

  public void setEndTime(Date endTime) {
    this.endTime = endTime;
  }

  public String getPeriod() {
    return period;
  }

  public void setPeriod(String period) {
    this.period = period;
  }

  public Integer getExecutionStatus() {
    return executionStatus;
  }

  public void setExecutionStatus(Integer executionStatus) {
    this.executionStatus = executionStatus;
  }

  public String getExecutionMsg() {
    return executionMsg;
  }

  public void setExecutionMsg(String executionMsg) {
    this.executionMsg = executionMsg;
  }

  public Integer getExecuteCount() {
    return executeCount;
  }

  public void setExecuteCount(Integer executeCount) {
    this.executeCount = executeCount;
  }

  public String getRemark() {
    return remark;
  }

  public void setRemark(String remark) {
    this.remark = remark;
  }



}
