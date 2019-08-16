package com.smate.center.open.isis.model.data.isissns;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;


/**
 * The persistent class for the NSFC_PRJ_REPORT database table.
 * 
 * @author hp
 * @date 2015-10-27
 */
@Entity
@Table(name = "NSFC_PRJ_REPORT")
public class SnsNsfcPrjReport implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = -6357555688376125413L;

  @Id
  @Column(name = "RPT_ID")
  private long rptId;


  @Column(name = "DELIVER_DATE")
  private Date deliverDate;

  @Column(name = "NSFC_RPT_ID")
  private Long nsfcRptId;

  @Column(name = "RPT_TYPE")
  private Long rptType;

  @Column(name = "RPT_YEAR")
  private Long rptYear;
  @Column(name = "STATUS")
  private Long status;


  @Column(name = "PRJ_ID")
  private Long prjId;


  public long getRptId() {
    return this.rptId;
  }

  public void setRptId(long rptId) {
    this.rptId = rptId;
  }

  public Date getDeliverDate() {
    return this.deliverDate;
  }

  public void setDeliverDate(Date deliverDate) {
    this.deliverDate = deliverDate;
  }

  public Long getNsfcRptId() {
    return this.nsfcRptId;
  }

  public void setNsfcRptId(Long nsfcRptId) {
    this.nsfcRptId = nsfcRptId;
  }


  public Long getPrjId() {
    return prjId;
  }

  public void setPrjId(Long prjId) {
    this.prjId = prjId;
  }

  public Long getRptType() {
    return this.rptType;
  }

  public void setRptType(Long rptType) {
    this.rptType = rptType;
  }

  public Long getRptYear() {
    return this.rptYear;
  }

  public void setRptYear(Long rptYear) {
    this.rptYear = rptYear;
  }

  public Long getStatus() {
    return this.status;
  }

  public void setStatus(Long status) {
    this.status = status;
  }



}
