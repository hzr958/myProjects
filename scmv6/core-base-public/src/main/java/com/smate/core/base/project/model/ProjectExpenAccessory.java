package com.smate.core.base.project.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * 项目经费支出附件表
 * 
 * @author YJ
 *
 *         2019年8月6日
 */

@Entity
@Table(name = "V_PRJ_EXPEN_ACCESSORY")
public class ProjectExpenAccessory implements Serializable {

  private static final long serialVersionUID = 3493963174652490070L;

  @Id
  @SequenceGenerator(sequenceName = "SEQ_V_PRJ_EXPEN_ACCESSORY", name = "SEQ_STORE", allocationSize = 1)
  @GeneratedValue(generator = "SEQ_STORE", strategy = GenerationType.SEQUENCE)
  @Column(name = "ID")
  private Long id; // 记录id，逻辑主键

  @Column(name = "EXPEN_RECORD_ID")
  private Long expenRecordId; // 经费表中主键，记录经费项目id

  @Column(name = "FILE_ID")
  private Long fileId; // 支出经费

  @Column(name = "FILE_NAME")
  private String fileName; // 文件名

  @Column(name = "GMT_CREATE")
  private Date gmtCreate;// 支出时间

  @Column(name = "GMT_MODIFIED")
  private Date gmtModified;// 修改时间

  public ProjectExpenAccessory() {

  }

  public ProjectExpenAccessory(Long expenRecordId, Long fileId, String fileName) {
    this.expenRecordId = expenRecordId;
    this.fileId = fileId;
    this.fileName = fileName;
    this.gmtCreate = new Date();
    this.gmtModified = new Date();
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Long getExpenRecordId() {
    return expenRecordId;
  }

  public void setExpenRecordId(Long expenRecordId) {
    this.expenRecordId = expenRecordId;
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

  public Date getGmtCreate() {
    return gmtCreate;
  }

  public void setGmtCreate(Date gmtCreate) {
    this.gmtCreate = gmtCreate;
  }

  public Date getGmtModified() {
    return gmtModified;
  }

  public void setGmtModified(Date gmtModified) {
    this.gmtModified = gmtModified;
  }


}
