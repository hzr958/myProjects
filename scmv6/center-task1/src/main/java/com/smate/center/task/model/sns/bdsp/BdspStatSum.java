package com.smate.center.task.model.sns.bdsp;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 统计分析-按总量分析、按资助类别分析、按学科领域分析
 * 
 * @author zzx
 *
 */
@Entity
@Table(name = "BDSP_STAT_SUM")
public class BdspStatSum implements Serializable {
  private static final long serialVersionUID = 1L;
  @Id
  @Column(name = "ID")
  private Long id;
  @Column(name = "INDEX_ID")
  private Long indexId;
  @Column(name = "YEAR_ID")
  private Long yearId;
  @Column(name = "GRANT_TYPE_ID")
  private Long grantTypeId;
  @Column(name = "TECHFILED_ID")
  private Long techfiledId;
  @Column(name = "RESULT")
  private Long result;
  @Column(name = "UPDATE_TIME")
  private Date updateTime;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Long getIndexId() {
    return indexId;
  }

  public void setIndexId(Long indexId) {
    this.indexId = indexId;
  }

  public Long getYearId() {
    return yearId;
  }

  public void setYearId(Long yearId) {
    this.yearId = yearId;
  }

  public Long getGrantTypeId() {
    return grantTypeId;
  }

  public void setGrantTypeId(Long grantTypeId) {
    this.grantTypeId = grantTypeId;
  }

  public Long getTechfiledId() {
    return techfiledId;
  }

  public void setTechfiledId(Long techfiledId) {
    this.techfiledId = techfiledId;
  }

  public Long getResult() {
    return result;
  }

  public void setResult(Long result) {
    this.result = result;
  }

  public Date getUpdateTime() {
    return updateTime;
  }

  public void setUpdateTime(Date updateTime) {
    this.updateTime = updateTime;
  }



}
