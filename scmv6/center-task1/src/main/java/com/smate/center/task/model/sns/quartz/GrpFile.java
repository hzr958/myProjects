package com.smate.center.task.model.sns.quartz;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang.StringUtils;

import com.smate.core.base.utils.security.Des3Utils;

/**
 * 群组文件
 * 
 * @author AiJiangBin
 *
 */

@Entity
@Table(name = "V_GRP_FILE")
public class GrpFile implements Serializable {


  /**
   * 
   */
  private static final long serialVersionUID = -3628343276090399151L;

  @Id
  @SequenceGenerator(name = "SEQ_GRP_FILE_STORE", sequenceName = "SEQ_V_GRP_FILE", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_GRP_FILE_STORE")
  @Column(name = "GRP_FILE_ID")
  private Long grpFileId; // 群组文件id

  @Column(name = "GRP_ID")
  private Long grpId; // number(18) 群组id



  @Column(name = "FILE_NAME")
  private String fileName; // varchar2(300 char)文件的名称

  @Column(name = "FILE_PATH")
  private String filePath; // varchar2(300 char) 文件存储路径

  @Column(name = "FILE_TYPE")
  private String fileType; // varchar2(100 char)文件的类型

  @Column(name = "FILE_SIZE")
  private Long fileSize; // number(10) 文件大小

  @Column(name = "FILE_DESC")
  private String fileDesc; // varchar2(500 char)文件的描述

  @Column(name = "FILE_STATUS")
  private Integer fileStatus; // number(1)文件的状态 0：未删除 ， 1：已删除

  @Column(name = "UPLOAD_DATE")
  private Date uploadDate; // date 上传日期

  @Column(name = "UPDATE_DATE")
  private Date updateDate; // date 更新日期

  @Column(name = "UPLOAD_PSN_ID")
  private Long uploadPsnId; // number(18) 文件上传者的Id

  @Column(name = "ARCHIVE_FILE_ID")
  private Long archiveFileId; // number(18)附件表（archive_files表）的id

  @Column(name = "FILE_MODULE_TYPE")
  private Integer fileModuleType; // number(1) 文件所属模块[0: 群组文件;1: 作业;2: 教学课件]

  @Transient
  private String des3GrpFileId; // 加密的群组文件id

  public GrpFile() {
    super();
  }

  public GrpFile(Long grpId, Long grpFileId, String fileName, String filePath, String fileType, String fileDesc,
      Date uploadDate, Long uploadPsnId, Long archiveFileId, Integer fileModuleType) {
    super();
    this.grpId = grpId;
    this.grpFileId = grpFileId;
    this.fileName = fileName;
    this.filePath = filePath;
    this.fileType = fileType;
    this.fileDesc = fileDesc;
    this.uploadDate = uploadDate;
    this.uploadPsnId = uploadPsnId;
    this.archiveFileId = archiveFileId;
    this.fileModuleType = fileModuleType;
  }

  public GrpFile(String fileName, String filePath, String fileType, Long fileSize, String fileDesc, Integer fileStatus,
      Date uploadDate, Date updateDate, Long uploadPsnId, Long archiveFileId, Integer fileModuleType) {
    super();
    this.fileName = fileName;
    this.filePath = filePath;
    this.fileType = fileType;
    this.fileSize = fileSize;
    this.fileDesc = fileDesc;
    this.fileStatus = fileStatus;
    this.uploadDate = uploadDate;
    this.updateDate = updateDate;
    this.uploadPsnId = uploadPsnId;
    this.archiveFileId = archiveFileId;
    this.fileModuleType = fileModuleType;
  }

  public Long getGrpFileId() {
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

  @Transient
  public String getDes3GrpFileId() {
    if (StringUtils.isBlank(des3GrpFileId) && grpFileId != null) {
      des3GrpFileId = Des3Utils.encodeToDes3(grpFileId.toString());
    }
    return des3GrpFileId;
  }

  public void setDes3GrpFileId(String des3GrpFileId) {
    this.des3GrpFileId = des3GrpFileId;
  }

}
