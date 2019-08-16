package com.smate.web.v8pub.dto;

import java.io.Serializable;

import org.codehaus.plexus.util.StringUtils;

import com.smate.core.base.utils.security.Des3Utils;

public class PubFulltextDTO implements Serializable {

  private static final long serialVersionUID = 3498374207104764006L;

  private String des3fileId;

  private Long fileId;

  private String fileName;

  private Integer permission;// 全文下载权限，0所有人可下载,2自己可下载

  private String srcFulltextUrl; // 来源全文路径

  private String thumbnailPath; // 全文缩略图图片路径

  private String gmtCreate; // 创建时间

  private String gmtModified; // 更新时间

  private Integer srcDbId;

  public String getDes3fileId() {
    return des3fileId;
  }

  public void setDes3fileId(String des3fileId) {
    this.des3fileId = des3fileId;
  }

  public String getFileName() {
    return fileName;
  }

  public void setFileName(String fileName) {
    this.fileName = fileName;
  }

  public Integer getPermission() {
    return permission;
  }

  public void setPermission(Integer permission) {
    this.permission = permission;
  }

  public Long getFileId() {
    if (fileId == null && StringUtils.isNotBlank(des3fileId)) {
      String fileIdStr = Des3Utils.decodeFromDes3(des3fileId);
      if (fileIdStr == null) {
        return fileId;
      } else {
        return Long.valueOf(fileIdStr);
      }
    }
    return fileId;
  }

  public void setFileId(Long fileId) {
    this.fileId = fileId;
  }

  public String getSrcFulltextUrl() {
    return srcFulltextUrl;
  }

  public void setSrcFulltextUrl(String srcFulltextUrl) {
    this.srcFulltextUrl = srcFulltextUrl;
  }

  public String getThumbnailPath() {
    return thumbnailPath;
  }

  public void setThumbnailPath(String thumbnailPath) {
    this.thumbnailPath = thumbnailPath;
  }

  public String getGmtCreate() {
    return gmtCreate;
  }

  public void setGmtCreate(String gmtCreate) {
    this.gmtCreate = gmtCreate;
  }

  public String getGmtModified() {
    return gmtModified;
  }

  public void setGmtModified(String gmtModified) {
    this.gmtModified = gmtModified;
  }

  public Integer getSrcDbId() {
    return srcDbId;
  }

  public void setSrcDbId(Integer srcDbId) {
    this.srcDbId = srcDbId;
  }

}
