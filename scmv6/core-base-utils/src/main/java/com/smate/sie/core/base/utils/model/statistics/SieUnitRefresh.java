package com.smate.sie.core.base.utils.model.statistics;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 
 * 部门统计日志表
 * 
 * @author hd
 *
 */
@Entity
@Table(name = "ST_UNIT_REFRESH")
public class SieUnitRefresh implements Serializable {


  private static final long serialVersionUID = 1963303009210383274L;
  private Long unitId;
  // 优先级
  private Integer priorCode;
  // 处理状态，0未处理(默认),1已处理,99失败
  private Integer status;


  public SieUnitRefresh() {
    super();
  }

  public SieUnitRefresh(Long unitId) {
    super();
    this.unitId = unitId;
  }

  public SieUnitRefresh(Long unitId, Integer priorCode, Integer status) {
    super();
    this.unitId = unitId;
    this.priorCode = priorCode;
    this.status = status;
  }

  @Id
  @Column(name = "UNIT_ID")
  public Long getUnitId() {
    return unitId;
  }

  public void setUnitId(Long unitId) {
    this.unitId = unitId;
  }

  @Column(name = "PRIOR_CODE")
  public Integer getPriorCode() {
    return priorCode;
  }

  public void setPriorCode(Integer priorCode) {
    this.priorCode = priorCode;
  }

  @Column(name = "STATUS")
  public Integer getStatus() {
    return status;
  }

  public void setStatus(Integer status) {
    this.status = status;
  }

}
