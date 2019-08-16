package com.smate.sie.core.base.utils.pub.dom;

import java.io.Serializable;

/**
 * 附件信息
 * 
 * @author sjzhou
 *
 */
public class PubAttachmentsBean implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = -6760165699942057033L;

  private Integer seqNo;
  private Long fileId;
  private String fileName = new String();
  private String fileExt = new String(); // 文件后缀
  private String uploadTime = new String(); // 上传时间
  private String fileSize = new String(); // 文件大小

  public Integer getSeqNo() {
    return seqNo;
  }

  public Long getFileId() {
    return fileId;
  }

  public String getFileName() {
    return fileName;
  }

  public String getFileExt() {
    return fileExt;
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

  public void setFileExt(String fileExt) {
    this.fileExt = fileExt;
  }

  public void setUploadTime(String uploadTime) {
    this.uploadTime = uploadTime;
  }

  public void setFileSize(String fileSize) {
    this.fileSize = fileSize;
  }

  @Override
  public String toString() {
    return "PubAttachmentsBean [seqNo=" + seqNo + ", fileId=" + fileId + ", fileName=" + fileName + ", fileExt="
        + fileExt + ", uploadTime=" + uploadTime + ", fileSize=" + fileSize + "]";
  }

}
