package com.smate.sie.core.base.utils.model.statistics;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 
 * 人员统计日志表
 * 
 * @author hd
 *
 */
@Entity
@Table(name = "ST_PSN_REFRESH")
public class SiePsnRefresh implements Serializable {

  private static final long serialVersionUID = 1881535774254297893L;
  private Long psnId;
  // 优先级
  private Integer priorCode;
  // 处理状态，0未处理(默认),1已处理,99失败
  private Integer status;


  public SiePsnRefresh() {
    super();
  }

  public SiePsnRefresh(Long psnId) {
    super();
    this.psnId = psnId;
  }

  public SiePsnRefresh(Long psnId, Integer priorCode, Integer status) {
    super();
    this.psnId = psnId;
    this.priorCode = priorCode;
    this.status = status;
  }

  @Id
  @Column(name = "PSN_ID")
  public Long getPsnId() {
    return psnId;
  }

  public void setPsnId(Long psnId) {
    this.psnId = psnId;
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
