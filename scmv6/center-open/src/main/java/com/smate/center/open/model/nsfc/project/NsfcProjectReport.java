package com.smate.center.open.model.nsfc.project;



import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.codehaus.jackson.annotate.JsonIgnore;

/**
 * NsfcPrjectReport entity.
 */
@Entity
@Table(name = "NSFC_PRJ_REPORT")
public class NsfcProjectReport implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = -2464687166492255146L;
  // Fields
  private Long rptId;
  private NsfcProject nsfcProject;
  private Long nsfcRptId;
  private Integer rptYear;
  private Integer rptType;
  private Integer status;
  private Date deliverDate;

  // Constructors

  /** default constructor. */
  public NsfcProjectReport() {}

  /** minimal constructor. */
  public NsfcProjectReport(Long rptId) {
    this.rptId = rptId;
  }

  /** full constructor. */
  public NsfcProjectReport(Long rptId, NsfcProject nsfcProject, Long nsfcRptId, Integer rptYear, Integer rptType,
      Integer status, Date deliverDate) {
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
  @ManyToOne
  // (cascade = { CascadeType.PERSIST, CascadeType.MERGE }, fetch =
  // FetchType.LAZY)
  // insertable = true, updatable = false
  @JoinColumn(name = "PRJ_ID", referencedColumnName = "PRJ_ID")
  public NsfcProject getNsfcProject() {
    return this.nsfcProject;
  }

  public void setNsfcProject(NsfcProject nsfcProject) {
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

}
