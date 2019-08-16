package com.smate.center.job.framework.dto;

import com.smate.center.job.common.enums.JobTypeEnum;
import com.smate.center.job.common.enums.JobWeightEnum;
import com.smate.core.base.utils.constant.DBSessionEnum;

/**
 * 离线任务数据传输模型
 *
 * @author houchuanjie
 * @date 2018年4月24日 下午4:49:11
 */
public class OfflineJobDTO extends BaseJobDTO {

  private static final long serialVersionUID = -2731330293073060370L;
  private boolean enable;
  private JobWeightEnum weight;
  private Long begin;
  private Long end;
  private DBSessionEnum dbSessionEnum;
  private String tableName;
  private Double percent;
  private Long count;
  private Integer threadCount;
  private String uniqueKey;
  private String filter;

  public OfflineJobDTO() {
    this.setJobType(JobTypeEnum.OFFLINE);
  }

  /**
   * @return begin
   */
  public Long getBegin() {
    return this.begin;
  }

  /**
   * @param begin 要设置的 begin
   */
  public void setBegin(Long begin) {
    this.begin = begin;
  }

  public boolean isEnable() {
    return enable;
  }

  public void setEnable(boolean enable) {
    this.enable = enable;
  }

  /**
   * @return end
   */
  public Long getEnd() {
    return this.end;
  }

  /**
   * @param end 要设置的 end
   */
  public void setEnd(Long end) {
    this.end = end;
  }

  public JobWeightEnum getWeight() {
    return weight;
  }

  public void setWeight(JobWeightEnum weight) {
    this.weight = weight;
  }

  /**
   * @return dbSessionEnum
   */
  public DBSessionEnum getDbSessionEnum() {
    return this.dbSessionEnum;
  }

  /**
   * @param dbSessionEnum 要设置的 dbSessionEnum
   */
  public void setDbSessionEnum(DBSessionEnum dbSessionEnum) {
    this.dbSessionEnum = dbSessionEnum;
  }

  /**
   * @return tableName
   */
  public String getTableName() {
    return this.tableName;
  }

  /**
   * @param tableName 要设置的 tableName
   */
  public void setTableName(String tableName) {
    this.tableName = tableName;
  }

  /**
   * @return percent
   */
  public Double getPercent() {
    return this.percent;
  }

  /**
   * @param percent 要设置的 percent
   */
  public void setPercent(Double percent) {
    this.percent = percent;
  }

  /**
   * @return count
   */
  public Long getCount() {
    return this.count;
  }

  /**
   * @param count 要设置的 count
   */
  public void setCount(Long count) {
    this.count = count;
  }

  /**
   * @return threadCount
   */
  public Integer getThreadCount() {
    return this.threadCount;
  }

  /**
   * @param threadCount 要设置的 threadCount
   */
  public void setThreadCount(Integer threadCount) {
    this.threadCount = threadCount;
  }

  /**
   * @return uniqueKey
   */
  public String getUniqueKey() {
    return this.uniqueKey;
  }

  /**
   * @param uniqueKey 要设置的 uniqueKey
   */
  public void setUniqueKey(String uniqueKey) {
    this.uniqueKey = uniqueKey;
  }

  public String getFilter() {
    return filter;
  }

  public void setFilter(String filter) {
    this.filter = filter;
  }

  @Override
  public String toString() {
    return "OfflineJobDTO{" +
        "id='" + id + '\'' +
        ", jobName='" + jobName + '\'' +
        ", priority=" + priority +
        ", status=" + status +
        ", jobType=" + jobType +
        ", dataMap='" + dataMap + '\'' +
        ", errMsg='" + errMsg + '\'' +
        ", startTime=" + startTime +
        ", elapsedTime=" + elapsedTime +
        ", gmtCreate=" + gmtCreate +
        ", gmtModified=" + gmtModified +
        ", enable=" + enable +
        ", weight=" + weight +
        ", begin=" + begin +
        ", end=" + end +
        ", dbSessionEnum=" + dbSessionEnum +
        ", tableName='" + tableName + '\'' +
        ", percent=" + percent +
        ", count=" + count +
        ", threadCount=" + threadCount +
        ", uniqueKey='" + uniqueKey + '\'' +
        ", filter='" + filter + '\'' +
        '}';
  }
}
