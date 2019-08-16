package com.smate.core.base.psn.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * 个人文件库2017-07
 * 
 * @author Administrator
 *
 */
@Entity
@Table(name = "V_PSN_FILE")
public class PsnFile implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = -2538698977980699179L;

  @Id
  @Column(name = "ID")
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_V_PSN_FILE", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
  private Long id;// 文件编号.

  @Column(name = "OWNER_PSN_ID")
  private Long ownerPsnId;// 文件拥有者编号.

  @Column(name = "FILE_NAME")
  private String fileName;// 文件全名称.

  @Column(name = "FILE_TYPE")
  private String fileType;// 文件类型.

  @Column(name = "FILE_DESC")
  private String fileDesc;// 文件描述.

  @Column(name = "STATUS")
  private Integer status; // 文件状态 0==正常

  @Column(name = "UPLOAD_DATE")
  private Date uploadDate;// 上传时间.

  @Column(name = "UPDATE_DATE")
  private Date updateDate;// 更新时间

  @Column(name = "ARCHIVE_FILE_ID")
  private Long archiveFileId; // 附件id

  @Column(name = "PERMISSION")
  private Integer permission; // 权限查看权限：0：所有人可见；1：好友可见；2：仅本人可见'';';

  public PsnFile() {}

  public PsnFile(Long id, Long ownerPsnId, String fileName, String fileType, String fileDesc, Integer status,
      Date uploadDate, Date updateDate, Long archiveFileId, Integer permission) {
    super();
    this.id = id;
    this.ownerPsnId = ownerPsnId;
    this.fileName = fileName;
    this.fileType = fileType;
    this.fileDesc = fileDesc;
    this.status = status;
    this.uploadDate = uploadDate;
    this.updateDate = updateDate;
    this.archiveFileId = archiveFileId;
    this.permission = permission;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Long getOwnerPsnId() {
    return ownerPsnId;
  }

  public void setOwnerPsnId(Long ownerPsnId) {
    this.ownerPsnId = ownerPsnId;
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

  public String getFileDesc() {
    return fileDesc;
  }

  public void setFileDesc(String fileDesc) {
    this.fileDesc = fileDesc;
  }

  public Integer getStatus() {
    return status;
  }

  public void setStatus(Integer status) {
    this.status = status;
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

  public Long getArchiveFileId() {
    return archiveFileId;
  }

  public void setArchiveFileId(Long archiveFileId) {
    this.archiveFileId = archiveFileId;
  }

  public Integer getPermission() {
    return permission;
  }

  public void setPermission(Integer permission) {
    this.permission = permission;
  }

}
