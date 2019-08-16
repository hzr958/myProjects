package com.smate.sie.core.base.utils.model.statistics;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 单位信息统计表
 * 
 * @author hd
 *
 */
@Entity
@Table(name = "ST_INS")
public class SieInsStatistics implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = -7860319578516323627L;

  private Long insId;
  // 项目数
  private Integer prjSum = 0;
  // 成果数
  private Integer pubSum = 0;
  // 专利数
  private Integer ptSum = 0;
  // 产品数
  private Integer pdSum = 0;
  // 人员数
  private Integer psnSum = 0;
  // 人员数
  private Integer unitNum = 0;
  // 单位管理员数
  private Integer adminSum = 0;
  // 部门管理员数
  private Integer unitAdminSum = 0;
  // 更新时间
  private Date updateDate;

  public SieInsStatistics() {
    super();
  }



  public SieInsStatistics(Long insId, Integer prjSum, Integer pubSum, Integer ptSum, Integer pdSum, Integer psnSum,
      Integer unitNum, Integer adminSum, Integer unitAdminSum, Date updateDate) {
    super();
    this.insId = insId;
    this.prjSum = prjSum;
    this.pubSum = pubSum;
    this.ptSum = ptSum;
    this.pdSum = pdSum;
    this.psnSum = psnSum;
    this.unitNum = unitNum;
    this.adminSum = adminSum;
    this.unitAdminSum = unitAdminSum;
    this.updateDate = updateDate;
  }



  public SieInsStatistics(Long insId, Integer prjSum, Integer pubSum, Integer ptSum, Integer psnSum, Integer unitNum,
      Integer adminSum, Integer unitAdminSum, Date updateDate) {
    super();
    this.insId = insId;
    this.prjSum = prjSum;
    this.pubSum = pubSum;
    this.ptSum = ptSum;
    this.psnSum = psnSum;
    this.unitNum = unitNum;
    this.adminSum = adminSum;
    this.unitAdminSum = unitAdminSum;
    this.updateDate = updateDate;
  }

  public SieInsStatistics(Long insId, Integer pubSum) {
    super();
    this.insId = insId;
    this.pubSum = pubSum;
  }

  @Id
  @Column(name = "INS_ID")
  public Long getInsId() {
    return insId;
  }

  public void setInsId(Long insId) {
    this.insId = insId;
  }

  @Column(name = "PRJ_SUM")
  public Integer getPrjSum() {
    return prjSum;
  }

  public void setPrjSum(Integer prjSum) {
    this.prjSum = prjSum;
  }

  @Column(name = "PUB_SUM")
  public Integer getPubSum() {
    return pubSum;
  }

  public void setPubSum(Integer pubSum) {
    this.pubSum = pubSum;
  }

  @Column(name = "PT_SUM")
  public Integer getPtSum() {
    return ptSum;
  }

  public void setPtSum(Integer ptSum) {
    this.ptSum = ptSum;
  }


  @Column(name = "PD_SUM")
  public Integer getPdSum() {
    return pdSum;
  }



  public void setPdSum(Integer pdSum) {
    this.pdSum = pdSum;
  }



  @Column(name = "PSN_SUM")
  public Integer getPsnSum() {
    return psnSum;
  }

  public void setPsnSum(Integer psnSum) {
    this.psnSum = psnSum;
  }

  @Column(name = "UNIT_SUM")
  public Integer getUnitNum() {
    return unitNum;
  }

  public void setUnitNum(Integer unitNum) {
    this.unitNum = unitNum;
  }

  @Column(name = "ADMIN_SUM")
  public Integer getAdminSum() {
    return adminSum;
  }

  public void setAdminSum(Integer adminSum) {
    this.adminSum = adminSum;
  }

  @Column(name = "UNIT_ADMIN_SUM")
  public Integer getUnitAdminSum() {
    return unitAdminSum;
  }

  public void setUnitAdminSum(Integer unitAdminSum) {
    this.unitAdminSum = unitAdminSum;
  }


  @Column(name = "UPDATE_DATE")
  public Date getUpdateDate() {
    return updateDate;
  }

  public void setUpdateDate(Date updateDate) {
    this.updateDate = updateDate;
  }

}
