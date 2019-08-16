package com.smate.web.psn.model.file;

import java.io.Serializable;

/**
 * 分享记录显示INFO类
 * 
 * @author cht
 *
 */
public class PsnFileShareRecordInfo implements Serializable {
  private static final long serialVersionUID = 1L;
  // 接收人
  private Long receiverId;
  private String receiverAvatars;
  private String receiverZhName;
  private String receiverEnName;
  private String receiverShortUrl;
  // 文件id
  private Long fileId;
  private String des3FileId;
  private String fileName;
  private String fileType;
  private String filePath;
  private String archiveFileId;
  private String fileDesc;
  private PsnFileShareRecord psnFileShareRecord;


  public PsnFileShareRecord getPsnFileShareRecord() {
    return psnFileShareRecord;
  }

  public void setPsnFileShareRecord(PsnFileShareRecord psnFileShareRecord) {
    this.psnFileShareRecord = psnFileShareRecord;
  }

  public Long getReceiverId() {
    return receiverId;
  }

  public void setReceiverId(Long receiverId) {
    this.receiverId = receiverId;
  }

  public String getReceiverAvatars() {
    return receiverAvatars;
  }

  public void setReceiverAvatars(String receiverAvatars) {
    this.receiverAvatars = receiverAvatars;
  }

  public String getReceiverZhName() {
    return receiverZhName;
  }

  public void setReceiverZhName(String receiverZhName) {
    this.receiverZhName = receiverZhName;
  }

  public String getReceiverEnName() {
    return receiverEnName;
  }

  public void setReceiverEnName(String receiverEnName) {
    this.receiverEnName = receiverEnName;
  }

  public String getReceiverShortUrl() {
    return receiverShortUrl;
  }

  public void setReceiverShortUrl(String receiverShortUrl) {
    this.receiverShortUrl = receiverShortUrl;
  }

  public Long getFileId() {
    return fileId;
  }

  public void setFileId(Long fileId) {
    this.fileId = fileId;
  }

  public String getDes3FileId() {
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

  public String getArchiveFileId() {
    return archiveFileId;
  }

  public void setArchiveFileId(String archiveFileId) {
    this.archiveFileId = archiveFileId;
  }

  public static long getSerialversionuid() {
    return serialVersionUID;
  }

  public String getFileDesc() {
    return fileDesc;
  }

  public void setFileDesc(String fileDesc) {
    this.fileDesc = fileDesc;
  }

}
