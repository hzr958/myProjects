package com.smate.center.batch.connector.model.job;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Jobs历史表，用于存储已经处理成功，或者忽略状态的任务
 * 
 * @since 6.0.1
 */
@Entity
@Table(name = "V_BATCH_JOBS_HISTORY")
public class BatchJobsHistory implements Serializable {


  /**
   * 
   */
  private static final long serialVersionUID = 935895321008904154L;

  @Id
  @Column(name = "JOB_ID")
  private Long jobId; // 主键

  @Column(name = "JOB_CONTEXT")
  private String jobContext; // 任务上下文

  @Column(name = "WEIGHT")
  private String weight; // 任务权重，高速A权重78,中速B权重15,低速C权重6,蜗速D权重1;每次查询到需要查询A、B、C和D这4种数据，数据补充规则，A不足，B补上，依次类推...，如连D都不足，则有多少取多少

  @Column(name = "STRATEGY")
  private String strategy; // 策略，8位标记Job的来源

  @Column(name = "JOB_START_TIME")
  private Date jobStartTime; // 任务开始时间，用于记录成功执行的任务

  @Column(name = "JOB_END_TIME")
  private Date jobEndTime; // 任务结束时间，用于记录成果执行的任务

  @Column(name = "START_THREAD")
  private Long startThread; // 起始处理任务的线程

  @Column(name = "END_THREAD")
  private Long endThread; // 结束时的任务线程 用于检查程序执行的线程是否与开始一致

  @Column(name = "JOB_INSTANCE_ID")
  private Long jobInstanceId;

  @Column(name = "STATUS")
  private Integer status; // 任务执行状态，0待处理(默认值)，1已标记，正在处理，2处理成功，3处理出错，4忽略（2,4状态需要此表中移除，存入历史表）

  @Column(name = "ERROR_MSG")
  private String errorMsg; // 记录出错信息，BatchTaskException

  @Column(name = "CREATE_TIME")
  private Date createTime; // 记录创建时间

  public BatchJobsHistory() {
    super();
  }

  public Long getJobId() {
    return jobId;
  }

  public void setJobId(Long jobId) {
    this.jobId = jobId;
  }

  public String getJobContext() {
    return jobContext;
  }

  public void setJobContext(String jobContext) {
    this.jobContext = jobContext;
  }

  public String getWeight() {
    return weight;
  }

  public void setWeight(String weight) {
    this.weight = weight;
  }

  public String getStrategy() {
    return strategy;
  }

  public void setStrategy(String strategy) {
    this.strategy = strategy;
  }

  public Date getJobStartTime() {
    return jobStartTime;
  }

  public void setJobStartTime(Date jobStartTime) {
    this.jobStartTime = jobStartTime;
  }

  public Date getJobEndTime() {
    return jobEndTime;
  }

  public void setJobEndTime(Date jobEndTime) {
    this.jobEndTime = jobEndTime;
  }

  public Long getStartThread() {
    return startThread;
  }

  public void setStartThread(Long startThread) {
    this.startThread = startThread;
  }

  public Long getJobInstanceId() {
    return jobInstanceId;
  }

  public void setJobInstanceId(Long jobInstanceId) {
    this.jobInstanceId = jobInstanceId;
  }

  public Integer getStatus() {
    return status;
  }

  public void setStatus(Integer status) {
    this.status = status;
  }

  public Long getEndThread() {
    return endThread;
  }

  public void setEndThread(Long endThread) {
    this.endThread = endThread;
  }

  public String getErrorMsg() {
    return errorMsg;
  }

  public void setErrorMsg(String errorMsg) {
    this.errorMsg = errorMsg;
  }

  public Date getCreateTime() {
    return createTime;
  }

  public void setCreateTime(Date createTime) {
    this.createTime = createTime;
  }



}
