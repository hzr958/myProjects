package com.smate.center.batch.model.sns.pub;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * V2.6同步使用. NsfcPrjectReport entity.
 */
@Entity
@Table(name = "NSFC_RESCH_PRJ_REPORT")
public class SyncNsfcReschProjectReportTemp implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = 4730820426308880958L;
  // Fields
  private Long rptId;
  private Long prjId;
  private Long nsfcRptId;
  private Integer rptYear;
  private Integer rptType;
  private Integer status;
  private Date deliverDate;

  @Id
  @Column(name = "RPT_ID", unique = true, nullable = false, precision = 9, scale = 0)
  public Long getRptId() {
    return this.rptId;
  }

  public void setRptId(Long rptId) {
    this.rptId = rptId;
  }

  @Column(name = "PRJ_ID")
  public Long getPrjId() {
    return prjId;
  }

  public void setPrjId(Long prjId) {
    this.prjId = prjId;
  }

  @Column(name = "NSFC_RPT_ID", precision = 9, scale = 0)
  public Long getNsfcRptId() {
    return this.nsfcRptId;
  }

  public void setNsfcRptId(Long nsfcRptId) {
    this.nsfcRptId = nsfcRptId;
  }

  @Column(name = "RPT_YEAR", precision = 4)
  public Integer getRptYear() {
    return this.rptYear;
  }

  public void setRptYear(Integer rptYear) {
    this.rptYear = rptYear;
  }

  @Column(name = "RPT_TYPE", precision = 2)
  public Integer getRptType() {
    return this.rptType;
  }

  public void setRptType(Integer rptType) {
    this.rptType = rptType;
  }

  @Column(name = "STATUS", precision = 2)
  public Integer getStatus() {
    return this.status;
  }

  public void setStatus(Integer status) {
    this.status = status;
  }

  @Column(name = "DELIVER_DATE")
  public Date getDeliverDate() {
    return this.deliverDate;
  }

  public void setDeliverDate(Date deliverDate) {
    this.deliverDate = deliverDate;
  }

}
