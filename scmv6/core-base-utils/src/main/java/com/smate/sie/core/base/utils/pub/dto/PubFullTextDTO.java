package com.smate.sie.core.base.utils.pub.dto;

import java.io.Serializable;

/**
 * 成果全文信息（目前没用到）
 * 
 * @author ZSJ
 *
 * @date 2019年1月31日
 */
public class PubFullTextDTO implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = -7256499127711319586L;

  private String fulltextUrl;
  private Long fileId;
  private String fileName;
  private String uploadTime; // 上传时间

  public String getFulltextUrl() {
    return fulltextUrl;
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

  public void setFulltextUrl(String fulltextUrl) {}

  public void setFileId(Long fileId) {
    this.fileId = fileId;
  }

  public void setFileName(String fileName) {
    this.fileName = fileName;
  }

  public void setUploadTime(String uploadTime) {
    this.uploadTime = uploadTime;
  }

  @Override
  public String toString() {
    return "PubFullTextBean [fulltextUrl=" + fulltextUrl + ", fileId=" + fileId + ", fileName=" + fileName
        + ", uploadTime=" + uploadTime + "]";
  }

}
