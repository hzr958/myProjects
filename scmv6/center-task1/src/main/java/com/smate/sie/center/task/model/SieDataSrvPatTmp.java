package com.smate.sie.center.task.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@SuppressWarnings("serial")
@Entity
@Table(name = "DATA_SRV_PAT_TMP")
public class SieDataSrvPatTmp implements Serializable {

  private Long patId;// pat_id
  private String zhTitle;// zh_title
  private String patentType;// patent_type
  private String legalStatus;// legal_status
  private String remark;
  private String ipcNo;// ipc_no
  private String patentee;
  private String inventor;
  private String startYear;// start_year
  private String startMonth;// start_month
  private String startDay;// start_day
  private String endYear;// end_year
  private String endMonth;// end_month
  private String endDay;// end_day
  private Integer applyYear;// apply_year
  private Integer applyMonth;// apply_month
  private Integer applyDay;// apply_day
  private String patentno;// patent_no
  private String patentOpenNo;// patent_open_no
  private String http;

  @Id
  @Column(name = "PAT_ID")
  public Long getPatId() {
    return patId;
  }

  @Column(name = "ZH_TITLE")
  public String getZhTitle() {
    return zhTitle;
  }

  @Column(name = "PATENT_TYPE")
  public String getPatentType() {
    return patentType;
  }

  @Column(name = "LEGAL_STATUS")
  public String getLegalStatus() {
    return legalStatus;
  }

  @Column(name = "REMARK")
  public String getRemark() {
    return remark;
  }

  @Column(name = "IPC_NO")
  public String getIpcNo() {
    return ipcNo;
  }

  @Column(name = "PATENTEE")
  public String getPatentee() {
    return patentee;
  }

  @Column(name = "INVENTOR")
  public String getInventor() {
    return inventor;
  }

  @Column(name = "START_YEAR")
  public String getStartYear() {
    return startYear;
  }

  @Column(name = "START_MONTH")
  public String getStartMonth() {
    return startMonth;
  }

  @Column(name = "START_DAY")
  public String getStartDay() {
    return startDay;
  }

  @Column(name = "END_YEAR")
  public String getEndYear() {
    return endYear;
  }

  @Column(name = "END_MONTH")
  public String getEndMonth() {
    return endMonth;
  }

  @Column(name = "END_DAY")
  public String getEndDay() {
    return endDay;
  }

  @Column(name = "APPLY_YEAR")
  public Integer getApplyYear() {
    return applyYear;
  }

  @Column(name = "APPLY_MONTH")
  public Integer getApplyMonth() {
    return applyMonth;
  }

  @Column(name = "APPLY_DAY")
  public Integer getApplyDay() {
    return applyDay;
  }

  @Column(name = "PATENT_NO")
  public String getPatentno() {
    return patentno;
  }

  @Column(name = "PATENT_OPEN_NO")
  public String getPatentOpenNo() {
    return patentOpenNo;
  }

  @Column(name = "")
  public String getHttp() {
    return http;
  }

  public void setPatId(Long patId) {
    this.patId = patId;
  }

  public void setZhTitle(String zhTitle) {
    this.zhTitle = zhTitle;
  }

  public void setPatentType(String patentType) {
    this.patentType = patentType;
  }

  public void setLegalStatus(String legalStatus) {
    this.legalStatus = legalStatus;
  }

  public void setRemark(String remark) {
    this.remark = remark;
  }

  public void setIpcNo(String ipcNo) {
    this.ipcNo = ipcNo;
  }

  public void setPatentee(String patentee) {
    this.patentee = patentee;
  }

  public void setInventor(String inventor) {
    this.inventor = inventor;
  }

  public void setStartYear(String startYear) {
    this.startYear = startYear;
  }

  public void setStartMonth(String startMonth) {
    this.startMonth = startMonth;
  }

  public void setStartDay(String startDay) {
    this.startDay = startDay;
  }

  public void setEndYear(String endYear) {
    this.endYear = endYear;
  }

  public void setEndMonth(String endMonth) {
    this.endMonth = endMonth;
  }

  public void setEndDay(String endDay) {
    this.endDay = endDay;
  }

  public void setApplyYear(Integer applyYear) {
    this.applyYear = applyYear;
  }

  public void setApplyMonth(Integer applyMonth) {
    this.applyMonth = applyMonth;
  }

  public void setApplyDay(Integer applyDay) {
    this.applyDay = applyDay;
  }

  public void setPatentno(String patentno) {
    this.patentno = patentno;
  }

  public void setPatentOpenNo(String patentOpenNo) {
    this.patentOpenNo = patentOpenNo;
  }

  public void setHttp(String http) {
    this.http = http;
  }

  public SieDataSrvPatTmp(Long patId, String zhTitle, String patentType, String legalStatus, String remark,
      String ipcNo, String patentee, String inventor, String startYear, String startMonth, String startDay,
      String endYear, String endMonth, String endDay, Integer applyYear, Integer applyMonth, Integer applyDay,
      String patentno, String patentOpenNo, String http) {
    super();
    this.patId = patId;
    this.zhTitle = zhTitle;
    this.patentType = patentType;
    this.legalStatus = legalStatus;
    this.remark = remark;
    this.ipcNo = ipcNo;
    this.patentee = patentee;
    this.inventor = inventor;
    this.startYear = startYear;
    this.startMonth = startMonth;
    this.startDay = startDay;
    this.endYear = endYear;
    this.endMonth = endMonth;
    this.endDay = endDay;
    this.applyYear = applyYear;
    this.applyMonth = applyMonth;
    this.applyDay = applyDay;
    this.patentno = patentno;
    this.patentOpenNo = patentOpenNo;
    this.http = http;
  }

  public SieDataSrvPatTmp() {
    super();
  }
}
