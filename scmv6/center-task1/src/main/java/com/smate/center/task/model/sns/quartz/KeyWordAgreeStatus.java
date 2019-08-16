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
@Table(name = "KEYWORD_AGREE_STATUS")
public class KeyWordAgreeStatus {
  private Long id; // 主键 与scienceArea_identification ID一致
  private Long psnId; // 人员Id
  private Integer kwId;// 科技领域id，对应PSN_DISCIPLINE_KEY主键
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

  @Column(name = "KW_ID")
  public Integer getKwId() {
    return kwId;
  }

  public void setKwId(Integer kwId) {
    this.kwId = kwId;
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

  public KeyWordAgreeStatus() {
    super();
  }

  public KeyWordAgreeStatus(Long id, Long psnId, Integer kwId, Long operatePsnId, Date operateDate, int status) {
    super();
    this.id = id;
    this.psnId = psnId;
    this.kwId = kwId;
    this.operatePsnId = operatePsnId;
    this.operateDate = operateDate;
    this.status = status;
  }

}
