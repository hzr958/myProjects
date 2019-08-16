package com.smate.center.task.psn.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * 人员科技领域认同记录
 *
 * @author wsn
 * @createTime 2017年3月14日 下午6:03:49
 *
 */

@Entity
@Table(name = "SCIENCEAREA_IDENTIFICATION")
public class ScienceAreaIdentification implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = 2448490004911448385L;
  private Long id; // 主键
  private Long psnId; // 人员Id
  private Integer scienceAreaId;// 科技领域id，对应psn_science_area主键
  private Long operatePsnId; // 认同人员ID
  private Date operateDate; // 认同时间

  public ScienceAreaIdentification() {
    super();
  }

  public ScienceAreaIdentification(Long id, Long psnId, Integer scienceAreaId, Long operatePsnId, Date operateDate) {
    super();
    this.id = id;
    this.psnId = psnId;
    this.scienceAreaId = scienceAreaId;
    this.operatePsnId = operatePsnId;
    this.operateDate = operateDate;
  }

  @Id
  @Column(name = "ID")
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_SCIENCEAREA_IDENTIFICATION", allocationSize = 1)
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  @Column(name = "PSN_ID")
  public Long getPsnId() {
    return psnId;
  }

  public void setPsnId(Long psnId) {
    this.psnId = psnId;
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

}
