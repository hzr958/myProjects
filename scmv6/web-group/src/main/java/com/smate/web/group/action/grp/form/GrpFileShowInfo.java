package com.smate.web.group.action.grp.form;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.lang3.StringUtils;

import com.smate.core.base.utils.security.Des3Utils;

/**
 * 群组文件
 * 
 * @author AiJiangBin
 *
 */

public class GrpFileShowInfo {

  private Long grpFileId; // 群组文件id

  private Long grpId; // number(18) 群组id

  private String fileName; // varchar2(300 char)文件的名称

  private String filePath; // varchar2(300 char) 文件存储路径

  private String fileType; // varchar2(100 char)文件的类型

  private Long fileSize; // number(10) 文件大小

  private String fileDesc; // varchar2(500 char)文件的描述

  private Integer fileStatus; // number(1)文件的状态 0：未删除 ， 1：已删除

  private Date uploadDate; // date 上传日期

  private Date updateDate; // date 更新日期

  private Long uploadPsnId; // number(18) 文件上传者的Id

  private Long archiveFileId; // number(18)附件表（archive_files表）的id

  private Integer fileModuleType; // number(1) 文件所属模块[0: 群组文件;1: 作业;2: 教学课件]

  private String des3GrpFileId; // 加密的群组文件id

  private String downloadUrl; // 下载地址

  private String imgThumbUrl; // 图片类型文件缩略图url

  private Boolean showEdit = false;

  private Boolean showDel = false;

  private List<GrpFileLabelShowInfo> grpFileLabelShowInfoList = new ArrayList<>();// 群组标签

  private Integer shareCount;

  public Long getGrpFileId() {
    if (grpFileId == null && StringUtils.isNotBlank(des3GrpFileId)) {
      grpFileId = NumberUtils.toLong(Des3Utils.decodeFromDes3(des3GrpFileId));
    }
    return grpFileId;
  }

  public void setGrpFileId(Long grpFileId) {
    this.grpFileId = grpFileId;
  }

  public Long getGrpId() {
    return grpId;
  }

  public void setGrpId(Long grpId) {
    this.grpId = grpId;
  }

  public String getFileName() {
    return fileName;
  }

  public void setFileName(String fileName) {
    this.fileName = fileName;
  }

  public String getFilePath() {
    return filePath;
  }

  public void setFilePath(String filePath) {
    this.filePath = filePath;
  }

  public String getFileType() {
    return fileType;
  }

  public void setFileType(String fileType) {
    this.fileType = fileType;
  }

  public Long getFileSize() {
    return fileSize;
  }

  public void setFileSize(Long fileSize) {
    this.fileSize = fileSize;
  }

  public String getFileDesc() {
    return fileDesc;
  }

  public void setFileDesc(String fileDesc) {
    this.fileDesc = fileDesc;
  }

  public Integer getFileStatus() {
    return fileStatus;
  }

  public void setFileStatus(Integer fileStatus) {
    this.fileStatus = fileStatus;
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

  public Long getUploadPsnId() {
    return uploadPsnId;
  }

  public void setUploadPsnId(Long uploadPsnId) {
    this.uploadPsnId = uploadPsnId;
  }

  public Long getArchiveFileId() {
    return archiveFileId;
  }

  public void setArchiveFileId(Long archiveFileId) {
    this.archiveFileId = archiveFileId;
  }

  public Integer getFileModuleType() {
    return fileModuleType;
  }

  public void setFileModuleType(Integer fileModuleType) {
    this.fileModuleType = fileModuleType;
  }

  public String getDes3GrpFileId() {
    if (StringUtils.isBlank(des3GrpFileId) && grpFileId != null) {
      des3GrpFileId = Des3Utils.encodeToDes3(grpFileId.toString());
    }
    return des3GrpFileId;
  }

  public void setDes3GrpFileId(String des3GrpFileId) {
    this.des3GrpFileId = des3GrpFileId;
  }

  public Boolean getShowEdit() {
    return showEdit;
  }

  public void setShowEdit(Boolean showEdit) {
    this.showEdit = showEdit;
  }

  public Boolean getShowDel() {
    return showDel;
  }

  public void setShowDel(Boolean showDel) {
    this.showDel = showDel;
  }

  public String getDownloadUrl() {
    return downloadUrl;
  }

  public void setDownloadUrl(String downloadUrl) {
    this.downloadUrl = downloadUrl;
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

  public List<GrpFileLabelShowInfo> getGrpFileLabelShowInfoList() {
    return grpFileLabelShowInfoList;
  }

  public void setGrpFileLabelShowInfoList(List<GrpFileLabelShowInfo> grpFileLabelShowInfoList) {
    this.grpFileLabelShowInfoList = grpFileLabelShowInfoList;
  }

  public Integer getShareCount() {
    return shareCount;
  }

  public void setShareCount(Integer shareCount) {
    this.shareCount = shareCount;
  }



}
