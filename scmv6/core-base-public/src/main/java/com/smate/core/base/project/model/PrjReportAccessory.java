package com.smate.core.base.project.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * 项目报告附件表
 * 
 * @author yhx
 * @date 2019年8月6日
 *
 */
@Entity
@Table(name = "V_PRJ_REPORT_ACCESSORY")
public class PrjReportAccessory {
  @Id
  @SequenceGenerator(name = "V_SEQ_PRJ_REPORT_ACCESSORY", sequenceName = "V_SEQ_PRJ_REPORT_ACCESSORY",
      allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "V_SEQ_PRJ_REPORT_ACCESSORY")
  @Column(name = "ID")
  private Long id; // 主键id

  @Column(name = "report_ID")
  private Long reportId;// 报告ID

  @Column(name = "file_ID")
  private Long fileId;// 文件ID

  @Column(name = "file_name")
  private String fileName;// 文件名

  @Column(name = "gmt_modified")
  private Date gmtModified; // 修改时间

  @Column(name = "GMT_CREATE")
  private Date gmtCreate; // 创建日期

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Long getReportId() {
    return reportId;
  }

  public void setReportId(Long reportId) {
    this.reportId = reportId;
  }

  public Long getFileId() {
    return fileId;
  }

  public void setFileId(Long fileId) {
    this.fileId = fileId;
  }

  public String getFileName() {
    return fileName;
  }

  public void setFileName(String fileName) {
    this.fileName = fileName;
  }

  public Date getGmtModified() {
    return gmtModified;
  }

  public void setGmtModified(Date gmtModified) {
    this.gmtModified = gmtModified;
  }

  public Date getGmtCreate() {
    return gmtCreate;
  }

  public void setGmtCreate(Date gmtCreate) {
    this.gmtCreate = gmtCreate;
  }



}
