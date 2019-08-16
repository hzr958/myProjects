package com.smate.core.base.project.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * 项目报告表
 * 
 * @author yhx
 * @date 2019年8月5日
 *
 */
@Entity
@Table(name = "V_PRJ_REPORT")
public class PrjReport {
  @Id
  @SequenceGenerator(name = "V_SEQ_PRJ_REPORT", sequenceName = "V_SEQ_PRJ_REPORT", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "V_SEQ_PRJ_REPORT")
  @Column(name = "ID")
  private Long id; // 主键id

  @Column(name = "PRJ_ID")
  private Long prjId;// 项目ID

  @Column(name = "REPORT_TYPE")
  private Integer reportType; // 报告类型(1进展报告 2中期报告 3审计报告 5结题报告 6验收报告)

  @Column(name = "STATUS")
  private Integer status;// 状态(1已结束 2已填写 3前往填写 4未填写)

  @Column(name = "WARN_DATE")
  private Date warnDate; // 提交截止时间

  @Column(name = "GMT_CREATE")
  private Date gmtCreate; // 创建日期

  @Transient
  private Long fileId;
  @Transient
  private String downloadUrl;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Long getPrjId() {
    return prjId;
  }

  public void setPrjId(Long prjId) {
    this.prjId = prjId;
  }

  public Integer getReportType() {
    return reportType;
  }

  public void setReportType(Integer reportType) {
    this.reportType = reportType;
  }

  public Integer getStatus() {
    return status;
  }

  public void setStatus(Integer status) {
    this.status = status;
  }

  public Date getWarnDate() {
    return warnDate;
  }

  public void setWarnDate(Date warnDate) {
    this.warnDate = warnDate;
  }

  public Date getGmtCreate() {
    return gmtCreate;
  }

  public void setGmtCreate(Date gmtCreate) {
    this.gmtCreate = gmtCreate;
  }

  public Long getFileId() {
    return fileId;
  }

  public void setFileId(Long fileId) {
    this.fileId = fileId;
  }

  public String getDownloadUrl() {
    return downloadUrl;
  }

  public void setDownloadUrl(String downloadUrl) {
    this.downloadUrl = downloadUrl;
  }


}
