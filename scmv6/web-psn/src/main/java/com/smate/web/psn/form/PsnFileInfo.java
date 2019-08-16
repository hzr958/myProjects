package com.smate.web.psn.form;

import java.util.Date;

import com.smate.core.base.utils.security.Des3Utils;

public class PsnFileInfo {
  // 文件id
  private Long fileId;
  private String des3FileId;
  private String fileName;
  private String fileType;
  private String filePath;
  private Long archiveFileId;
  private String fileDesc;
  private Date uploadDate;// 上传时间.
  private Date updateDate;// 更新时间
  private String downloadUrl; // 文件下载地址
  private String imgThumbUrl; // 缩略图地址
  private String appImgThumbUrl; // app端缩略图地址
  private String appDownloadUrl;// app端下载地址
  private String fileSize;// 文件大小
  private boolean isGrpFile = false;// 是否已经是群组文件

  public String getAppDownloadUrl() {
    return appDownloadUrl;
  }

  public void setAppDownloadUrl(String appDownloadUrl) {
    this.appDownloadUrl = appDownloadUrl;
  }

  public Long getFileId() {
    return fileId;
  }

  public void setFileId(Long fileId) {
    this.fileId = fileId;
  }

  public String getDes3FileId() {
    if (fileId != null && fileId > 0L) {
      des3FileId = Des3Utils.encodeToDes3(fileId.toString());
    }
    return des3FileId;
  }

  public void setDes3FileId(String des3FileId) {
    this.des3FileId = des3FileId;
  }

  public String getFileName() {
    return fileName;
  }

  public void setFileName(String fileName) {
    this.fileName = fileName;
  }

  public String getFileType() {
    return fileType;
  }

  public void setFileType(String fileType) {
    this.fileType = fileType;
  }

  public String getFilePath() {
    return filePath;
  }

  public void setFilePath(String filePath) {
    this.filePath = filePath;
  }

  public Long getArchiveFileId() {
    return archiveFileId;
  }

  public void setArchiveFileId(Long archiveFileId) {
    this.archiveFileId = archiveFileId;
  }

  public String getFileDesc() {
    return fileDesc;
  }

  public void setFileDesc(String fileDesc) {
    this.fileDesc = fileDesc;
  }

  public Date getUploadDate() {
    return uploadDate;
  }

  public void setUploadDate(Date uploadDate) {
    this.uploadDate = uploadDate;
  }

  public Date getUpdateDate() {
    return updateDate;
  }

  public void setUpdateDate(Date updateDate) {
    this.updateDate = updateDate;
  }

  public String getDownloadUrl() {
    return downloadUrl;
  }

  public void setDownloadUrl(String downloadUrl) {
    this.downloadUrl = downloadUrl;
  }

  public String getFileSize() {
    return fileSize;
  }

  public void setFileSize(String fileSize) {
    this.fileSize = fileSize;
  }

  /**
   * @return imgThumbUrl
   */
  public String getImgThumbUrl() {
    return imgThumbUrl;
  }

  /**
   * @param imgThumbUrl 要设置的 imgThumbUrl
   */
  public void setImgThumbUrl(String imgThumbUrl) {
    this.imgThumbUrl = imgThumbUrl;
  }

  public String getAppImgThumbUrl() {
    return appImgThumbUrl;
  }

  public void setAppImgThumbUrl(String appImgThumbUrl) {
    this.appImgThumbUrl = appImgThumbUrl;
  }

  public boolean getIsGrpFile() {
    return isGrpFile;
  }

  public void setIsGrpFile(boolean isGrpFile) {
    this.isGrpFile = isGrpFile;
  }
}
