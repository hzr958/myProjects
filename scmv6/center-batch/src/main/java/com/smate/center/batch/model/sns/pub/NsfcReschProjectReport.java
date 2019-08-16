package com.smate.center.batch.model.sns.pub;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;


/**
 * NsfcPrjectReport entity.
 */
@Entity
@Table(name = "NSFC_RESCH_PRJ_REPORT")
public class NsfcReschProjectReport implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = -3245155635971578541L;
  // Fields
  private Long rptId;
  private NsfcReschProject nsfcProject;
  private Long nsfcRptId;
  private Integer rptYear;
  private Integer rptType;
  private Integer status;
  private Date deliverDate;
  private Long versionId = 0L;

  // Constructors

  /** default constructor. */
  public NsfcReschProjectReport() {}

  /** minimal constructor. */
  public NsfcReschProjectReport(Long rptId) {
    this.rptId = rptId;
  }

  /** full constructor. */
  public NsfcReschProjectReport(Long rptId, NsfcReschProject nsfcProject, Long nsfcRptId, Integer rptYear,
      Integer rptType, Integer status, Date deliverDate) {
    this.rptId = rptId;
    this.nsfcProject = nsfcProject;
    this.nsfcRptId = nsfcRptId;
    this.rptYear = rptYear;
    this.rptType = rptType;
    this.status = status;
    this.deliverDate = deliverDate;
  }

  // Property accessors
  @Id
  @Column(name = "RPT_ID", unique = true, nullable = false, precision = 9, scale = 0)
  public Long getRptId() {
    return this.rptId;
  }

  public void setRptId(Long rptId) {
    this.rptId = rptId;
  }

  @JsonIgnore
  @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
  @JoinColumn(name = "PRJ_ID", referencedColumnName = "PRJ_ID", insertable = true, updatable = false)
  public NsfcReschProject getNsfcProject() {
    return this.nsfcProject;
  }

  public void setNsfcProject(NsfcReschProject nsfcProject) {
    this.nsfcProject = nsfcProject;
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

  /**
   * @return the versionId
   */
  @Column(name = "VERSION_ID")
  public Long getVersionId() {
    return versionId;
  }

  /**
   * @param versionId the versionId to set
   */
  public void setVersionId(Long versionId) {
    this.versionId = versionId;
  }

}
