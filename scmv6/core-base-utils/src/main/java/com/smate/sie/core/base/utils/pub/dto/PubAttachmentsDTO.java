package com.smate.sie.core.base.utils.pub.dto;

import java.io.Serializable;

/**
 * 附件信息
 * 
 * @author ZSJ
 *
 * @date 2019年2月11日
 */
public class PubAttachmentsDTO implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = -6760165699942057033L;

  private Integer seqNo;
  private Long fileId;
  private String fileName;
  private String uploadTime; // 上传时间
  private String fileSize; // 文件大小
  private String fileExt; // 文件后缀

  public Integer getSeqNo() {
    return seqNo;
  }

  public Long getFileId() {
    return fileId;
  }

  public String getFileName() {
    return fileName;
  }

  public String getUploadTime() {
    return uploadTime;
  }

  public String getFileSize() {
    return fileSize;
  }

  public void setSeqNo(Integer seqNo) {
    this.seqNo = seqNo;
  }

  public void setFileId(Long fileId) {
    this.fileId = fileId;
  }

  public void setFileName(String fileName) {
    this.fileName = fileName;
  }

  public void setUploadTime(String uploadTime) {
    this.uploadTime = uploadTime;
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
    return "PubAttachmentsBean [seqNo=" + seqNo + ", fileId=" + fileId + ", fileName=" + fileName + ", uploadTime="
        + uploadTime + ", fileSize=" + fileSize + "]";
  }

}
