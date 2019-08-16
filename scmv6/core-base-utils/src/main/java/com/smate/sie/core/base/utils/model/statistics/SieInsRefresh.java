package com.smate.sie.core.base.utils.model.statistics;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 
 * 单位统计日志表
 * 
 * @author hd
 *
 */
@Entity
@Table(name = "ST_INS_REFRESH")
public class SieInsRefresh implements Serializable {


  /**
     * 
     */
  private static final long serialVersionUID = -6883944504352569582L;

  private Long insId;
  // 优先级
  private Integer priorCode;
  // 处理状态，0未处理(默认),1已处理,99失败
  private Integer status;


  public SieInsRefresh() {
    super();
  }

  public SieInsRefresh(Long insId) {
    super();
    this.insId = insId;
  }

  public SieInsRefresh(Long insId, Integer priorCode, Integer status) {
    super();
    this.insId = insId;
    this.priorCode = priorCode;
    this.status = status;
  }

  @Id
  @Column(name = "INS_ID")
  public Long getInsId() {
    return insId;
  }

  public void setInsId(Long insId) {
    this.insId = insId;
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
