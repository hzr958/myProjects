package com.smate.center.batch.model.sns.pub;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 奖励类别自动提示.
 * 
 * @author liqinghua
 * 
 */
@Entity
@Table(name = "APP_QUARTZ_SETTING")
public class ApplicationQuartzSetting implements Serializable {


  /**
   * 
   */
  private static final long serialVersionUID = 5740722841266015215L;
  // 主键
  private Long id;

  private String taskName;

  private Integer value;

  @Id
  @Column(name = "APP_ID")
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  @Column(name = "TASK_NAME")
  public String getTaskName() {
    return taskName;
  }

  public void setTaskName(String taskName) {
    this.taskName = taskName;
  }

  @Column(name = "VALUE")
  public Integer getValue() {
    return value;
  }

  public void setValue(Integer value) {
    this.value = value;
  }

}
