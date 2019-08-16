package com.smate.sie.core.base.utils.pub.dom;

import java.io.Serializable;

/**
 * 成果全文信息（目前没用到）
 * 
 * @author sjzhou
 *
 */
public class PubFullTextBean implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = -7256499127711319586L;

  private String fulltextUrl = new String();
  private Long fileId;
  private String fileName = new String();
  private String fileExt = new String(); // 文件后缀
  private String uploadTime = new String(); // 上传时间

  public String getFulltextUrl() {
    return fulltextUrl;
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

  public void setFulltextUrl(String fulltextUrl) {}

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

  @Override
  public String toString() {
    return "PubFullTextBean [fulltextUrl=" + fulltextUrl + ", fileId=" + fileId + ", fileName=" + fileName
        + ", fileExt=" + fileExt + ", uploadTime=" + uploadTime + "]";
  }

}
