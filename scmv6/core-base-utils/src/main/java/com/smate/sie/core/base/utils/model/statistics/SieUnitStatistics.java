package com.smate.sie.core.base.utils.model.statistics;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 
 * @author yxs
 * @descript 部门统计表
 */
@Entity
@Table(name = "ST_UNIT")
public class SieUnitStatistics implements Serializable {

  private static final long serialVersionUID = 2647130016916205320L;
  @Id
  @Column(name = "UNIT_ID")
  private Long unitId;
  @Column(name = "PRJ_SUM") // 项目数
  private Integer prjSum = 0;//
  @Column(name = "PUB_SUM") // 成果数
  private Integer pubSum = 0;
  @Column(name = "PT_SUM") // 专利数
  private Integer ptSum = 0;
  @Column(name = "PD_SUM") // 产品数
  private Integer pdSum = 0;
  @Column(name = "PSN_SUM") // 人员数
  private Integer psnSum = 0;
  @Column(name = "UNIT_ADMIN_SUM") // 部门管理员数
  private Integer unitAdminSum = 0;
  @Column(name = "UPDATE_DATE")
  private Date updateDate;

  public SieUnitStatistics() {
    super();
  }

  public SieUnitStatistics(Long unitId, Integer prjSum, Integer pubSum, Integer ptSum, Integer pdSum, Integer psnSum,
      Integer unitAdminSum, Date updateDate) {
    super();
    this.unitId = unitId;
    this.prjSum = prjSum;
    this.pubSum = pubSum;
    this.ptSum = ptSum;
    this.pdSum = pdSum;
    this.psnSum = psnSum;
    this.unitAdminSum = unitAdminSum;
    this.updateDate = updateDate;
  }

  public SieUnitStatistics(Long unitId, Integer prjSum, Integer pubSum, Integer ptSum, Integer psnSum,
      Integer unitAdminSum, Date updateDate) {
    super();
    this.unitId = unitId;
    this.prjSum = prjSum;
    this.pubSum = pubSum;
    this.ptSum = ptSum;
    this.psnSum = psnSum;
    this.unitAdminSum = unitAdminSum;
    this.updateDate = updateDate;
  }

  public Long getUnitId() {
    return unitId;
  }

  public void setUnitId(Long unitId) {
    this.unitId = unitId;
  }

  public Integer getPrjSum() {
    return prjSum;
  }

  public void setPrjSum(Integer prjSum) {
    this.prjSum = prjSum;
  }

  public Integer getPubSum() {
    return pubSum;
  }

  public void setPubSum(Integer pubSum) {
    this.pubSum = pubSum;
  }

  public Integer getPtSum() {
    return ptSum;
  }

  public void setPtSum(Integer ptSum) {
    this.ptSum = ptSum;
  }

  public Integer getPdSum() {
    return pdSum;
  }

  public void setPdSum(Integer pdSum) {
    this.pdSum = pdSum;
  }

  public Integer getPsnSum() {
    return psnSum;
  }

  public void setPsnSum(Integer psnSum) {
    this.psnSum = psnSum;
  }

  public Integer getUnitAdminSum() {
    return unitAdminSum;
  }

  public void setUnitAdminSum(Integer unitAdminSum) {
    this.unitAdminSum = unitAdminSum;
  }


  public Date getUpdateDate() {
    return updateDate;
  }

  public void setUpdateDate(Date updateDate) {
    this.updateDate = updateDate;
  }

}
