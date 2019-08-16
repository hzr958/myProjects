package com.smate.core.base.utils.model.impfilelog;

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
 * 导入文件日志
 * 
 * @author jszhou
 */
@Entity
@Table(name = "IMPORT_FILE_LOG")
public class ImportFileLog implements Serializable {

  private static final long serialVersionUID = 6120192624170982047L;

  @Id
  @Column(name = "ID")
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_IMPORT_FILE_LOG", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
  private Long id;

  // 保存路径
  @Column(name = "SAVE_PATH")
  private String savePath;

  // 文件名
  @Column(name = "FILE_NAME")
  private String fileName;

  // 操作ID
  @Column(name = "PSN_ID")
  private Long psnId;

  // 单位ID
  @Column(name = "INS_ID")
  private Long insId;

  // 上传时间
  @Column(name = "UPLOAD_DATE")
  private Date uploadDate;

  // 导入是否成功 （0：不成功 1：成功）
  @Column(name = "STATUS")
  private Integer status;

  // 上传类型 详情请看UploadTypeContant.java
  @Column(name = "UPLOAD_TYPE")
  private String uploadType;

  @Column(name = "MSG")
  private String msg;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getSavePath() {
    return savePath;
  }

  public void setSavePath(String savePath) {
    this.savePath = savePath;
  }

  public String getFileName() {
    return fileName;
  }

  public void setFileName(String fileName) {
    this.fileName = fileName;
  }

  public Long getPsnId() {
    return psnId;
  }

  public void setPsnId(Long psnId) {
    this.psnId = psnId;
  }

  public Long getInsId() {
    return insId;
  }

  public void setInsId(Long insId) {
    this.insId = insId;
  }

  public Date getUploadDate() {
    return uploadDate;
  }

  public void setUploadDate(Date uploadDate) {
    this.uploadDate = uploadDate;
  }

  public Integer getStatus() {
    return status;
  }

  public void setStatus(Integer status) {
    this.status = status;
  }

  public String getUploadType() {
    return uploadType;
  }

  public void setUploadType(String uploadType) {
    this.uploadType = uploadType;
  }

  public String getMsg() {
    return msg;
  }

  public void setMsg(String msg) {
    this.msg = msg;
  }
}
