package com.smate.core.base.project.model;

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
 * 项目经费支出记录表
 * 
 * @author YJ
 *
 *         2019年8月6日
 */

@Entity
@Table(name = "V_PRJ_EXPEN_RECORD")
public class ProjectExpenRecord implements Serializable {

  private static final long serialVersionUID = 4480671898294921079L;

  @Id
  @SequenceGenerator(sequenceName = "SEQ_V_PRJ_EXPEN_RECORD", name = "SEQ_STORE", allocationSize = 1)
  @GeneratedValue(generator = "SEQ_STORE", strategy = GenerationType.SEQUENCE)
  @Column(name = "ID")
  private Long id; // 记录id，逻辑主键

  @Column(name = "EXPEN_ID")
  private Long expenId; // 经费表中主键，记录经费项目id

  @Column(name = "EXPEN_AMOUNT")
  private Float expenAmount; // 支出经费

  @Column(name = "STATUS")
  private Integer status; // 0为正常，1为已删除

  @Column(name = "REMARK")
  private String remark; // 备注信息

  @Column(name = "GMT_EXPEN")
  private Date gmtExpen;// 科目支出日期

  @Column(name = "GMT_MODIFIED")
  private Date gmtModified;// 增加支出记录的操作时间，gmtModified >= gmtExpend 此笔支出为已用，反之则为预支

  public ProjectExpenRecord() {

  }

  public ProjectExpenRecord(Long expenId, Date gmtExpen, String remark, Float expenAmount, Integer status) {
    this.expenId = expenId;
    this.gmtExpen = gmtExpen;
    this.remark = remark;
    this.expenAmount = expenAmount;
    this.status = status;
    this.gmtModified = new Date();
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Long getExpenId() {
    return expenId;
  }

  public void setExpenId(Long expenId) {
    this.expenId = expenId;
  }

  public Float getExpenAmount() {
    return expenAmount;
  }

  public void setExpenAmount(Float expenAmount) {
    this.expenAmount = expenAmount;
  }

  public Date getGmtModified() {
    return gmtModified;
  }

  public void setGmtModified(Date gmtModified) {
    this.gmtModified = gmtModified;
  }

  public Date getGmtExpen() {
    return gmtExpen;
  }

  public void setGmtExpen(Date gmtExpen) {
    this.gmtExpen = gmtExpen;
  }

  public String getRemark() {
    return remark;
  }

  public void setRemark(String remark) {
    this.remark = remark;
  }

  public Integer getStatus() {
    return status;
  }

  public void setStatus(Integer status) {
    this.status = status;
  }
}
