package com.smate.web.prj.dto;

import java.io.Serializable;

public class ProjectExpenAccessoryDTO implements Serializable {

  private static final long serialVersionUID = 9104741814091889458L;

  private Long id;// 经费附件表主键

  private Long expenRecordId; // 经费表中主键，记录经费项目id

  private String des3FileId; // 支出经费

  private String downloadUrl; // 文件下载链接

  private String fileName; // 文件名

  public Long getExpenRecordId() {
    return expenRecordId;
  }

  public void setExpenRecordId(Long expenRecordId) {
    this.expenRecordId = expenRecordId;
  }

  public String getFileName() {
    return fileName;
  }

  public void setFileName(String fileName) {
    this.fileName = fileName;
  }

  public String getDes3FileId() {
    return des3FileId;
  }

  public void setDes3FileId(String des3FileId) {
    this.des3FileId = des3FileId;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getDownloadUrl() {
    return downloadUrl;
  }

  public void setDownloadUrl(String downloadUrl) {
    this.downloadUrl = downloadUrl;
  }
}
