package com.smate.web.group.model.grp.job;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * task任务记录信息表
 * 
 * @author LJ
 *
 *         2017年8月18日
 */
@Entity
@Table(name = "TMP_TASK_INFO_RECORD")
public class TmpTaskInfoRecord {

  private Long jobId;// 任务编号
  private int jobType;// 任务类型 ，不同的task任务需要定义不同的类型编号

  private Long handleId;// 需要处理的ID（pubid等等）
  private Long relativeId;// 需要处理的Id的关联ID（用于有些记录需要联合Id）

  private int status = 0;// 任务状态

  private Date handletime;// 处理时间

  private String errMsg;// 错误信息

  @Id
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_TMP_TASK_INFO_RECORD", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
  @Column(name = "JOB_ID")
  public Long getJobId() {
    return jobId;
  }

  public void setJobId(Long jobId) {
    this.jobId = jobId;
  }

  @Column(name = "JOB_TYPE")
  public int getJobType() {
    return jobType;
  }

  public void setJobType(int jobType) {
    this.jobType = jobType;
  }

  @Column(name = "HANDLE_ID")
  public Long getHandleId() {
    return handleId;
  }

  public void setHandleId(Long handleId) {
    this.handleId = handleId;
  }

  @Column(name = "STATUS")
  public int getStatus() {
    return status;
  }

  public void setStatus(int status) {
    this.status = status;
  }

  @Column(name = "HANDLE_TIME")
  public Date getHandletime() {
    return handletime;
  }

  public void setHandletime(Date handletime) {
    this.handletime = handletime;
  }

  @Column(name = "ERRMSG")
  public String getErrMsg() {
    return errMsg;
  }

  public void setErrMsg(String errMsg) {
    this.errMsg = errMsg;
  }

  @Column(name = "RELATIVE_ID")
  public Long getRelativeId() {
    return relativeId;
  }

  public void setRelativeId(Long relativeId) {
    this.relativeId = relativeId;
  }

  public TmpTaskInfoRecord(Long jobId, int jobType, Long handleId, int status, Date handletime, String errMsg) {
    super();
    this.jobId = jobId;
    this.jobType = jobType;
    this.handleId = handleId;
    this.status = status;
    this.handletime = handletime;
    this.errMsg = errMsg;
  }

  public TmpTaskInfoRecord(Long jobId, int jobType, Long handleId, Long relativeId, int status, Date handletime,
      String errMsg) {
    super();
    this.jobId = jobId;
    this.jobType = jobType;
    this.handleId = handleId;
    this.relativeId = relativeId;
    this.status = status;
    this.handletime = handletime;
    this.errMsg = errMsg;
  }

  public TmpTaskInfoRecord(Long handleId, int jobType, Date handletime) {
    super();
    this.jobType = jobType;
    this.handleId = handleId;
    this.handletime = handletime;
  }

  public TmpTaskInfoRecord() {
    super();
  }

}
