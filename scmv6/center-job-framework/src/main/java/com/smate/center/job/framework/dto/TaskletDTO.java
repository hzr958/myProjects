package com.smate.center.job.framework.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.smate.center.job.common.enums.JobStatusEnum;
import com.smate.center.job.common.enums.JobStatusEnum.JobStatusEnumDeserializer;
import com.smate.center.job.common.enums.JobTypeEnum;
import com.smate.center.job.common.enums.JobTypeEnum.JobTypeEnumDeserializer;
import java.io.Serializable;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 小任务信息类，数据传输模型，任务执行时使用的模型
 *
 * @author houchuanjie
 * @date 2018/04/08 18:13
 */
public class TaskletDTO implements Serializable {

  private static final long serialVersionUID = 4091454950194556663L;
  /**
   * 任务ID
   */
  private String id;
  /**
   * 任务bean名称
   */
  private String jobName;
  /**
   * 任务状态
   */
  private JobStatusEnum status;
  /**
   * 任务优先级
   */
  private Integer priority;
  /**
   * 任务参数
   */
  private String dataMap;
  /**
   * 任务错误信息
   */
  private String errMsg;

  /**
   * 任务类型
   */
  private JobTypeEnum jobType;

  /**
   * 任务分片执行记录数偏移量，当且仅当{@code jobType}值为{@link JobTypeEnum#OFFLINE}时有效
   */
  private AtomicLong offset = new AtomicLong(0);

  /**
   * 任务分片需要执行的总记录数
   */
  private Long count;

  /**
   * 任务分片号
   */
  private Integer threadNo;

  /**
   * 任务分片需要处理的记录的起始id
   */
  private Long begin;

  /**
   * 任务分片需要处理的记录的截止id
   */
  private Long end;

  /**
   * 执行百分比
   */
  private Double percent;

  /**
   * 时间戳
   */
  private Long timestamp;

  /**
   * 被指派的服务器节点名称
   */
  private String assignedServerName;

  public TaskletDTO(final OfflineJobDTO jobInfo, long count, long begin, long end, int threadNo,
      long timestamp) {
    copyProperties(jobInfo);
    this.setOffset(0L);
    this.setPercent(0.000);
    this.setCount(count);
    this.setBegin(begin);
    this.setEnd(end);
    this.setTimestamp(timestamp);
    this.setThreadNo(threadNo);
    this.setJobType(JobTypeEnum.OFFLINE);
  }

  public TaskletDTO(final OnlineJobDTO jobInfo, long timestamp) {
    copyProperties(jobInfo);
    this.setTimestamp(timestamp);
    this.setJobType(JobTypeEnum.ONLINE);
  }

  public TaskletDTO() {
  }

  /**
   * 复制相同属性值
   *
   * @param jobInfo
   */
  private void copyProperties(OfflineJobDTO jobInfo) {
    this.setId(jobInfo.getId());
    this.setJobName(jobInfo.getJobName());
    this.setStatus(jobInfo.getStatus());
    this.setDataMap(jobInfo.getDataMap());
    this.setPriority(jobInfo.getPriority());
  }

  /**
   * 复制相同属性值
   *
   * @param jobInfo
   */
  private void copyProperties(OnlineJobDTO jobInfo) {
    this.setId(jobInfo.getId());
    this.setJobName(jobInfo.getJobName());
    this.setStatus(jobInfo.getStatus());
    this.setDataMap(jobInfo.getDataMap());
    this.setPriority(jobInfo.getPriority());
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getJobName() {
    return jobName;
  }

  public void setJobName(String jobName) {
    this.jobName = jobName;
  }

  /**
   * @return status
   */
  public JobStatusEnum getStatus() {
    return status;
  }

  /**
   * @param status 要设置的 status
   */
  @JsonDeserialize(using = JobStatusEnumDeserializer.class)
  public void setStatus(JobStatusEnum status) {
    this.status = status;
  }

  public Integer getPriority() {
    return priority;
  }

  public void setPriority(Integer priority) {
    this.priority = priority;
  }

  public JobTypeEnum getJobType() {
    return jobType;
  }

  @JsonDeserialize(using = JobTypeEnumDeserializer.class)
  public void setJobType(JobTypeEnum jobType) {
    this.jobType = jobType;
  }

  public Long getOffset() {
    return offset.get();
  }

  public void setOffset(Long offset) {
    this.offset.set(offset);
  }

  public Long getCount() {
    return count;
  }

  public void setCount(Long count) {
    this.count = count;
  }

  public Integer getThreadNo() {
    return threadNo;
  }

  public void setThreadNo(Integer threadNo) {
    this.threadNo = threadNo;
  }

  public Long getBegin() {
    return begin;
  }

  public void setBegin(Long begin) {
    this.begin = begin;
  }

  public Long getEnd() {
    return end;
  }

  public void setEnd(Long end) {
    this.end = end;
  }

  public Long getTimestamp() {
    return timestamp;
  }

  public void setTimestamp(Long timestamp) {
    this.timestamp = timestamp;
  }

  /**
   * @return percent
   */
  public Double getPercent() {
    return percent;
  }

  /**
   * @param percent 要设置的 percent
   */
  public void setPercent(Double percent) {
    this.percent = percent;
  }

  /**
   * @return dataMap
   */
  public String getDataMap() {
    return dataMap;
  }

  /**
   * @param dataMap 要设置的 dataMap
   */
  public void setDataMap(String dataMap) {
    this.dataMap = dataMap;
  }

  /**
   * @return errMsg
   */
  public String getErrMsg() {
    return errMsg;
  }

  /**
   * @param errMsg 要设置的 errMsg
   */
  public void setErrMsg(String errMsg) {
    this.errMsg = errMsg;
  }

  /**
   * 获取该子任务被指派的服务器节点名称
   *
   * @return
   */
  public String getAssignedServerName() {
    return assignedServerName;
  }

  /**
   * 设置该子任务被指派的服务器节点名称
   *
   * @return
   */
  public void setAssignedServerName(String assignedServerName) {
    this.assignedServerName = assignedServerName;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    TaskletDTO that = (TaskletDTO) o;
    return Objects.equals(id, that.id) && Objects.equals(jobName, that.jobName);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, jobName);
  }

  @Override
  public String toString() {
    return "TaskletDTO{" +
        "id='" + id + '\'' +
        ", jobName='" + jobName + '\'' +
        ", status=" + status +
        ", priority=" + priority +
        ", dataMap='" + dataMap + '\'' +
        ", errMsg='" + errMsg + '\'' +
        ", jobType=" + jobType +
        ", offset=" + offset +
        ", count=" + count +
        ", threadNo=" + threadNo +
        ", begin=" + begin +
        ", end=" + end +
        ", percent=" + percent +
        ", timestamp=" + timestamp +
        ", assignedServerName='" + assignedServerName + '\'' +
        '}';
  }
}
