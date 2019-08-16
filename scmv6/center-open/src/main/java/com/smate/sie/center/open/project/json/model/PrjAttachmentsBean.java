package com.smate.sie.center.open.project.json.model;

import java.io.Serializable;

/**
 * 项目附件信息
 * 
 * @author lijianming
 *
 * @date 2019年6月10日
 */
public class PrjAttachmentsBean implements Serializable {

  private static final long serialVersionUID = -4611662689008426705L;

  private Integer seqNo;
  private Long fileId;
  private String fileName = new String();
  private String uploadTime = new String(); // 上传时间
  private String fileSize = new String(); // 文件大小
  private String fileExt = new String(); // 文件后缀

  public Integer getSeqNo() {
    return seqNo;
  }

  public void setSeqNo(Integer seqNo) {
    this.seqNo = seqNo;
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

  public String getUploadTime() {
    return uploadTime;
  }

  public void setUploadTime(String uploadTime) {
    this.uploadTime = uploadTime;
  }

  public String getFileSize() {
    return fileSize;
  }

  public void setFileSize(String fileSize) {
    this.fileSize = fileSize;
  }

  public String getFileExt() {
    return fileExt;
  }

  public void setFileExt(String fileExt) {
    this.fileExt = fileExt;
  }

  @Override
  public String toString() {
    return "PrjAttachmentsBean [seqNo=" + seqNo + ", fileId=" + fileId + ", fileName=" + fileName + ", uploadTime="
        + uploadTime + ", fileSize=" + fileSize + ", fileExt=" + fileExt + "]";
  }
}
