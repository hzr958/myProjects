package com.smate.web.management.model.analysis.sns;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 基金、论文推荐合作者：计算可能合作者控制实体类.
 * 
 * @author zhuangyanming
 * 
 */
@Entity
@Table(name = "COOPERATOR_MAY_RUN")
public class CooperatorMayRun implements Serializable {


  /**
   * 
   */
  private static final long serialVersionUID = 1325291177094294419L;

  // 主键
  // 用户psnId
  private Long psnId;

  // 任务状态:0进入任务队列，1挂起任务进入队列，2执行成功，-1挂起任务，-2执行失败
  // 挂起任务用于中断，计算非常耗时的用户，避免影响任务运行，待其它用户数据执行完成后，再重新进入队列执行
  private Integer status = 0;

  // 任务开始执行时间
  private Date date = new Date();

  // 任务消息
  private String msg;

  public CooperatorMayRun() {

  }

  public CooperatorMayRun(Long psnId) {
    this.psnId = psnId;
  }

  @Id
  @Column(name = "PSN_ID")
  public Long getPsnId() {
    return psnId;
  }

  @Column(name = "STATUS")
  public Integer getStatus() {
    return status;
  }

  @Column(name = "RUN_MSG")
  public String getMsg() {
    return msg;
  }

  @Column(name = "RUN_DATE")
  public Date getDate() {
    return date;
  }

  public void setPsnId(Long psnId) {
    this.psnId = psnId;
  }

  public void setStatus(Integer status) {
    this.status = status;
  }

  public void setMsg(String msg) {
    this.msg = msg;
  }

  public void setDate(Date date) {
    this.date = date;
  }

}
