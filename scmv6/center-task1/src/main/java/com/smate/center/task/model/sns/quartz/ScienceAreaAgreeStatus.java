package com.smate.center.task.model.sns.quartz;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 认同研究领域生成邮件状态信息表
 * 
 * @author LJ
 *
 *         2017年7月6日
 */

@Entity
@Table(name = "SCIENCEAREA_AGREE_STATUS")
public class ScienceAreaAgreeStatus {
  private Long id; // 主键 与scienceArea_identification ID一致
  private Long psnId; // 人员Id
  private Integer scienceAreaId;// 科技领域id，对应psn_science_area主键
  private Long operatePsnId; // 认同人员ID
  private Date operateDate; // 认同时间
  private int status;// 0表示未生成，1表示失败，2表示成功

  @Id
  @Column(name = "ID")
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public void setPsnId(Long psnId) {
    this.psnId = psnId;
  }

  @Column(name = "PSN_ID")
  public Long getPsnId() {
    return psnId;
  }

  @Column(name = "STATUS")
  public int getStatus() {
    return status;
  }

  public void setStatus(int status) {
    this.status = status;
  }

  @Column(name = "SCIENCE_AREA_ID")
  public Integer getScienceAreaId() {
    return scienceAreaId;
  }

  public void setScienceAreaId(Integer scienceAreaId) {
    this.scienceAreaId = scienceAreaId;
  }

  @Column(name = "FRIEND_ID")
  public Long getOperatePsnId() {
    return operatePsnId;
  }

  public void setOperatePsnId(Long operatePsnId) {
    this.operatePsnId = operatePsnId;
  }

  @Column(name = "OP_DATE")
  public Date getOperateDate() {
    return operateDate;
  }

  public void setOperateDate(Date operateDate) {
    this.operateDate = operateDate;
  }


  public ScienceAreaAgreeStatus() {
    super();
  }

  public ScienceAreaAgreeStatus(Long id, Long psnId, Integer scienceAreaId, Long operatePsnId, Date operateDate,
      int status) {
    super();
    this.id = id;
    this.psnId = psnId;
    this.scienceAreaId = scienceAreaId;
    this.operatePsnId = operatePsnId;
    this.operateDate = operateDate;
    this.status = status;
  }


}
