package com.smate.center.job.web.vo;

import com.smate.center.job.common.enums.JobWeightEnum;
import java.io.Serializable;
import java.util.Date;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * 在线任务配置页面数据模型
 */
public class OnlineJobConfigVO implements Serializable {

  private static final long serialVersionUID = 5956853069000218462L;
  /**
   * 任务id
   */
  private String id;

  /**
   * 任务名称
   */
  private String name;
  /**
   * 权重
   */
  private JobWeightEnum weight;
  /**
   * 是否可用
   */
  private boolean enable;
  /**
   * 任务优先级
   */
  private Integer priority;

  /**
   * 未完成的数量
   */
  private Long notDoneCount;
  /**
   * 执行出错的数量
   */
  private Long errCount;

  /**
   * 已完成的数量
   */
  private Long doneCount;
  /**
   * 创建时间
   */
  private Date createTime;

  /**
   * 修改时间
   */
  private Date modifiedTime;

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public boolean isEnable() {
    return enable;
  }

  public void setEnable(boolean enable) {
    this.enable = enable;
  }

  public Integer getPriority() {
    return priority;
  }

  public void setPriority(Integer priority) {
    this.priority = priority;
  }

  public Date getCreateTime() {
    return createTime;
  }

  public void setCreateTime(Date createTime) {
    this.createTime = createTime;
  }

  public Date getModifiedTime() {
    return modifiedTime;
  }

  public void setModifiedTime(Date modifiedTime) {
    this.modifiedTime = modifiedTime;
  }

  public JobWeightEnum getWeight() {
    return weight;
  }

  public void setWeight(JobWeightEnum weight) {
    this.weight = weight;
  }

  public Long getNotDoneCount() {
    return notDoneCount;
  }

  public void setNotDoneCount(Long notDoneCount) {
    this.notDoneCount = notDoneCount;
  }

  public Long getErrCount() {
    return errCount;
  }

  public void setErrCount(Long errCount) {
    this.errCount = errCount;
  }

  public Long getDoneCount() {
    return doneCount;
  }

  public void setDoneCount(Long doneCount) {
    this.doneCount = doneCount;
  }

  @Override
  public String toString() {
    return new ToStringBuilder(this)
        .append("id", id)
        .append("name", name)
        .append("enable", enable)
        .append("priority", priority)
        .append("notDoneCount", notDoneCount)
        .append("errCount", errCount)
        .append("doneCount", doneCount)
        .append("createTime", createTime)
        .append("modifiedTime", modifiedTime)
        .append("weight", weight)
        .toString();
  }
}
