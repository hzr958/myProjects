package com.smate.sie.core.base.utils.model.validate;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "KPI_PAY_PROJECT")
public class KpiPayProject implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = 6219562900998904508L;
  private Long insId;
  private Integer status; // 0申请 1通过
  private Long psnId;
  private Date smDate; // 付费日期
  private Date endDate;// 授权截至时间
  private String zhName; // 机构名称

  @Id
  @Column(name = "INS_ID")
  public Long getInsId() {
    return insId;
  }

  @Column(name = "STATUS")
  public Integer getStatus() {
    return status;
  }

  @Column(name = "PSN_ID")
  public Long getPsnId() {
    return psnId;
  }

  @Column(name = "SUBMIT_DATE")
  public Date getSmDate() {
    return smDate;
  }

  @Column(name = "END_DATE")
  public Date getEndDate() {
    return endDate;
  }

  @Column(name = "zh_name")
  public String getZhName() {
    return zhName;
  }

  public void setZhName(String zhName) {
    this.zhName = zhName;
  }

  public KpiPayProject() {
    super();
  }

  public void setInsId(Long insId) {
    this.insId = insId;
  }

  public void setStatus(Integer status) {
    this.status = status;
  }

  public void setPsnId(Long psnId) {
    this.psnId = psnId;
  }

  public void setSmDate(Date smDate) {
    this.smDate = smDate;
  }

  public void setEndDate(Date endDate) {
    this.endDate = endDate;
  }

}
