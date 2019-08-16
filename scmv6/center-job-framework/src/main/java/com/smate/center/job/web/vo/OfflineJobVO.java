package com.smate.center.job.web.vo;

import com.smate.center.job.common.enums.JobStatusEnum;
import com.smate.center.job.common.enums.JobWeightEnum;
import com.smate.core.base.utils.constant.DBSessionEnum;
import java.util.Date;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * 离线任务对象页面数据模型
 */
public class OfflineJobVO extends BaseJobVO {

  private static final long serialVersionUID = 6672822113644281706L;
  private boolean enable; //是否禁用
  private Long begin; // 起始id，包含
  private Long end; // 截止id，不包含
  private DBSessionEnum db; // 数据源枚举
  private String table; // 表名
  private Double percent; // 执行进度百分比
  private Long count; // 总记录数
  private Integer threads; // 线程数
  private String uniKey; // 唯一键
  private JobWeightEnum weight; // 权重
  private Date startTime; //执行开始时间
  private Long elapsed;   //执行耗时，ms
  private String filter;  //过滤条件
  private String op;  //操作（重新执行、停止、启动）

  public boolean isEnable() {
    return enable;
  }

  public void setEnable(boolean enable) {
    this.enable = enable;
  }

  public JobWeightEnum getWeight() {
    return weight;
  }

  public void setWeight(JobWeightEnum weight) {
    this.weight = weight;
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

  /**
   * @return db
   */
  public DBSessionEnum getDb() {
    return this.db;
  }

  /**
   * @param db 要设置的 db
   */
  public void setDb(DBSessionEnum db) {
    this.db = db;
  }

  /**
   * @return table
   */
  public String getTable() {
    return this.table;
  }

  /**
   * @param table 要设置的 table
   */
  public void setTable(String table) {
    this.table = table;
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
   * @return threads
   */
  public Integer getThreads() {
    return this.threads;
  }

  /**
   * @param threads 要设置的 threads
   */
  public void setThreads(Integer threads) {
    this.threads = threads;
  }

  /**
   * @return uniKey
   */
  public String getUniKey() {
    return this.uniKey;
  }

  /**
   * @param uniKey 要设置的 uniKey
   */
  public void setUniKey(String uniKey) {
    this.uniKey = uniKey;
  }

  public Date getStartTime() {
    return startTime;
  }

  public void setStartTime(Date startTime) {
    this.startTime = startTime;
  }

  public Long getElapsed() {
    return elapsed;
  }

  public void setElapsed(Long elapsed) {
    this.elapsed = elapsed;
  }

  public String getFilter() {
    return filter;
  }

  public void setFilter(String filter) {
    this.filter = filter;
  }

  public String getOp() {
    if (enable) {
      if (status.in(JobStatusEnum.FAILED, JobStatusEnum.PROCESSED)) {
        op = "repeat";
      } else {
        op = "stop";
      }
    } else {
      op = "run";
    }
    return op;
  }

  @Override
  public String toString() {
    return new ToStringBuilder(this).append("id", id).append("name", name)
        .append("priority", priority).append("status", status).append("paramsMap", paramsMap)
        .append("errMsg", errMsg).append("createTime", createTime)
        .append("modifiedTime", modifiedTime).append("enable", enable).append("begin", begin)
        .append("end", end).append("db", db).append("table", table).append("percent", percent)
        .append("count", count).append("threads", threads).append("uniKey", uniKey)
        .append("weight", weight).append("startTime", startTime).append("elapsed", elapsed)
        .append("filter", filter).append("errMsg", errMsg).toString();
  }
}
