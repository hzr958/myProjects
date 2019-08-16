package com.smate.sie.core.base.utils.model.validate;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "KPI_PAY_VALIDATE")
public class KpiPayValidate implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = -3512415817036191775L;
  private Long insId;
  private Integer status; // 0申请 1通过
  private Long psnId;
  private Date smDate; // 付费日期
  private Date endDate;// 授权截至时间
  private Integer limitPDf; // 同一个人允许上传pdf次数
  private String permitIP;// 允许的IP,英文分号分割
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

  @Column(name = "SUBMIT_TIME")
  public Date getSmDate() {
    return smDate;
  }

  @Column(name = "END_TIME")
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

  public KpiPayValidate() {
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

  @Column(name = "LIMIT_PDF")
  public Integer getLimitPDf() {
    return limitPDf;
  }

  @Column(name = "PERMIT_IP")
  public String getPermitIP() {
    return permitIP;
  }

  public void setLimitPDf(Integer limitPDf) {
    this.limitPDf = limitPDf;
  }

  public void setPermitIP(String permitIP) {
    this.permitIP = permitIP;
  }

}
