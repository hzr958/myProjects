package com.smate.center.open.model.nsfc.continueproject;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * 延续项目报告
 * 
 * @author tsz
 *
 */

@Entity
@Table(name = "NSFC_CON_PRJ_RPT")
public class ContinueProjectReport implements Serializable {

  private static final long serialVersionUID = 4087789214691335664L;

  // 主键
  @Id
  @Column(name = "ID")
  @SequenceGenerator(sequenceName = "SEQ_NSFC_CON_PRJ_RPT", name = "SEQ_STORE", allocationSize = 1)
  @GeneratedValue(generator = "SEQ_STORE", strategy = GenerationType.SEQUENCE)
  private Long id;

  @Column(name = "NSFC_PRJ_CODE")
  private Long nsfcPrjCode;

  // isis系统中结题或者进展报告的ID
  @Column(name = "NSFC_RPT_ID")
  private Long nsfcRptId;

  // 报告年度
  @Column(name = "RPT_YEAR")
  private Integer rptYear;

  // 报告类型，结题报告1。进展报告4
  @Column(name = "RPT_TYPE")
  private Integer rptType;

  // 报告状态 0：允许修改成果信息 1：不允许修改成果信息
  @Column(name = "STATUS")
  private Integer status;

  public ContinueProjectReport() {
    super();
  }

  public ContinueProjectReport(Long nsfcRptId, Integer rptYear, Integer rptType, Integer status, Long nsfcPrjCode) {
    super();
    this.nsfcRptId = nsfcRptId;
    this.rptYear = rptYear;
    this.rptType = rptType;
    this.status = status;
    this.nsfcPrjCode = nsfcPrjCode;
  }

  public Long getNsfcRptId() {
    return nsfcRptId;
  }

  public Integer getRptYear() {
    return rptYear;
  }

  public Integer getRptType() {
    return rptType;
  }

  public Integer getStatus() {
    return status;
  }

  public void setNsfcRptId(Long nsfcRptId) {
    this.nsfcRptId = nsfcRptId;
  }

  public void setRptYear(Integer rptYear) {
    this.rptYear = rptYear;
  }

  public void setRptType(Integer rptType) {
    this.rptType = rptType;
  }

  public void setStatus(Integer status) {
    this.status = status;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Long getNsfcPrjCode() {
    return nsfcPrjCode;
  }

  public void setNsfcPrjCode(Long nsfcPrjCode) {
    this.nsfcPrjCode = nsfcPrjCode;
  }

}
