package com.smate.center.task.sys.quartz.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 定时器时间model
 * 
 * @author zk
 *
 */
@Entity
@Table(name = "V_QUARTZ_CRON_EXPRESSION")
public class QuartzCronExpression {

  private Long id;
  // CronTriggerBean id
  private String beanName;
  // cronExpression时间设置
  private String expression;
  // 是否允许运行
  private Integer status;

  @Id
  @Column(name = "ID")
  public Long getId() {
    return id;
  }

  @Column(name = "CRON_TRIGGER_BEAN")
  public String getBeanName() {
    return beanName;
  }

  @Column(name = "CRON_EXPRESSION")
  public String getExpression() {
    return expression;
  }

  @Column(name = "STATUS")
  public Integer getStatus() {
    return status;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public void setBeanName(String beanName) {
    this.beanName = beanName;
  }

  public void setExpression(String expression) {
    this.expression = expression;
  }

  public void setStatus(Integer status) {
    this.status = status;
  }
}
