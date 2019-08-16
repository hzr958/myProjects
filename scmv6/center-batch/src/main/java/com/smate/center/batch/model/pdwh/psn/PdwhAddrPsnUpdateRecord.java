package com.smate.center.batch.model.pdwh.psn;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * 基准库成果地址作者匹配任务链，地址更新，人员信息更新任务记录表
 * 
 * @author LIJUN
 * @date 2018年3月31日
 */
@Entity
@Table(name = "PDWH_ADDR_PSN_UPDATE_RECORD")
public class PdwhAddrPsnUpdateRecord {
  @Id
  @Column(name = "ID")
  @SequenceGenerator(sequenceName = "SEQ_PDWH_ADDR_PSN_UP_RECORD", allocationSize = 1, name = "SEQ_STORE")
  @GeneratedValue(generator = "SEQ_STORE", strategy = GenerationType.SEQUENCE)
  private Long Id;// 主键
  @Column(name = "TASK_ID")
  private Long taskId;// 任务Id ,psnId,constId
  @Column(name = "TYPE")
  private Integer type;// 任务类别，1表示地址常量更新，2表示人员信息更新
  @Column(name = "STATUS")
  private Integer status;// 任务状态，0默认，1成功，2失败
  @Column(name = "UPDATE_TIME")
  private Date updateTime;// 更新时间

  public PdwhAddrPsnUpdateRecord() {
    super();
  }

  public PdwhAddrPsnUpdateRecord(Long taskId, Integer type, Integer status) {
    super();
    this.taskId = taskId;
    this.type = type;
    this.status = status;
  }

  public Long getId() {
    return Id;
  }

  public void setId(Long id) {
    Id = id;
  }

  public Long getTaskId() {
    return taskId;
  }

  public void setTaskId(Long taskId) {
    this.taskId = taskId;
  }

  public Integer getType() {
    return type;
  }

  public void setType(Integer type) {
    this.type = type;
  }

  public Integer getStatus() {
    return status;
  }

  public void setStatus(Integer status) {
    this.status = status;
  }

}
