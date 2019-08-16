package com.smate.core.base.pub.vo;

import java.io.Serializable;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.smate.core.base.utils.security.Des3Utils;

/**
 * 附件
 * 
 * @author aijiangbin
 * @date 2018年8月3日
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Accessory implements Serializable {
  private static final long serialVersionUID = 1L;
  public String des3pubId;// 成果id
  public Long pubId;// 成果id
  public Long fileId;// 文件id
  public String des3fileId;// 文件id
  public String fileName; // 文件名
  public Integer permission = 0; // 文件 权限 0=所有人；2=仅本人
  private String fileType;
  private String fileUrl;// 下载链接
  private Boolean isUpdown = false; // 是否可以下载
  private String simpleFileUrl;// 简单详情页面 下载链接

  public Integer getPermission() {
    return permission;
  }

  public void setPermission(Integer permission) {
    this.permission = permission;
  }

  public String getDes3pubId() {
    return des3pubId;
  }

  public void setDes3pubId(String des3pubId) {
    this.des3pubId = des3pubId;
  }

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

  public String getFileUrl() {
    return fileUrl;
  }

  public void setFileUrl(String fileUrl) {
    this.fileUrl = fileUrl;
  }

  public String getFileType() {
    return fileType;
  }

  public void setFileType(String fileType) {
    this.fileType = fileType;
  }

  public Boolean getIsUpdown() {
    return isUpdown;
  }

  public void setIsUpdown(Boolean isUpdown) {
    this.isUpdown = isUpdown;
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

  public Long getPubId() {
    return pubId;
  }

  public void setPubId(Long pubId) {
    this.pubId = pubId;
  }

  public String getSimpleFileUrl() {
    return simpleFileUrl;
  }

  public void setSimpleFileUrl(String simpleFileUrl) {
    this.simpleFileUrl = simpleFileUrl;
  }

}
