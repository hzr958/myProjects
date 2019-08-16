package com.smate.sie.core.base.utils.model.statistics;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 刷新社交行为付费表
 * 
 * @author hd
 *
 */
@Entity
@Table(name = "KPI_PAY_BEHAVE")
public class KpiPayBehave implements Serializable {

  private static final long serialVersionUID = 2954206877499503858L;
  @Id
  @Column(name = "INS_ID")
  private Long insId;
  @Column(name = "STATUS")
  /* 处理状态：0待处理，1已处理 */
  private Integer status;
  @Column(name = "PSN_ID")
  private Long psnId;
  /* 创建时间 */
  @Column(name = "SUBMIT_DATE")
  private Date subDate;
  /* 结束时间 */
  @Column(name = "END_DATE")
  private Date endDate;
  @Column(name = "zh_name")
  private String zhName; // 机构名称

  public Long getInsId() {
    return insId;
  }

  public void setInsId(Long insId) {
    this.insId = insId;
  }

  public Integer getStatus() {
    return status;
  }

  public void setStatus(Integer status) {
    this.status = status;
  }

  public Long getPsnId() {
    return psnId;
  }

  public void setPsnId(Long psnId) {
    this.psnId = psnId;
  }

  public Date getSubDate() {
    return subDate;
  }

  public void setSubDate(Date subDate) {
    this.subDate = subDate;
  }

  public Date getEndDate() {
    return endDate;
  }

  public void setEndDate(Date endDate) {
    this.endDate = endDate;
  }

  public String getZhName() {
    return zhName;
  }

  public void setZhName(String zhName) {
    this.zhName = zhName;
  }

}
