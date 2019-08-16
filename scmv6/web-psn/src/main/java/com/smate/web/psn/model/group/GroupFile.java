package com.smate.web.psn.model.group;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 群组文件.
 * 
 * @author zk
 * 
 */
@Entity
@Table(name = "GROUP_FILES")
public class GroupFile implements Serializable {

  private static final long serialVersionUID = -6634333722297056287L;
  private Long groupFileId;// 群组文件ID
  private Long groupId;// 群组ID
  private Long fileId;// 文件ID
  private String fileName;// 文件名
  private String filePath;// 文件路径
  private String fileType;// 文件类型
  private Long fileSize;// 文件大小
  private String fileDesc;// 文件描述
  private int fileStatus;// 文件状态
  private String groupFolderIds;// 群组文件夹
  private int nodeId;// 存放节点
  private Long psnId;// 拥有者
  private Date uploadTime;// 文件上传时间.
  private String authorName;// 上传人名.
  private Long archiveFileId;
  // 文件所属模块[0: 群组文件;1: 作业;2: 教学课件]
  private int fileModuleType;

  public GroupFile() {
    super();
  }

  @Id
  @Column(name = "GROUP_FILES_ID")
  public Long getGroupFileId() {
    return groupFileId;
  }

  public void setGroupFileId(Long groupFileId) {
    this.groupFileId = groupFileId;
  }

  @Column(name = "GROUP_ID")
  public Long getGroupId() {
    return groupId;
  }

  public void setGroupId(Long groupId) {
    this.groupId = groupId;
  }

  @Column(name = "FILE_ID")
  public Long getFileId() {
    return fileId;
  }

  public void setFileId(Long fileId) {
    this.fileId = fileId;
  }

  @Column(name = "FILE_NAME")
  public String getFileName() {
    return fileName;
  }

  public void setFileName(String fileName) {
    this.fileName = fileName;
  }

  @Column(name = "FILE_PATH")
  public String getFilePath() {
    return filePath;
  }

  public void setFilePath(String filePath) {
    this.filePath = filePath;
  }

  @Column(name = "FILE_TYPE")
  public String getFileType() {
    return fileType;
  }

  public void setFileType(String fileType) {
    this.fileType = fileType;
  }

  @Column(name = "FILE_SIZE")
  public Long getFileSize() {
    return fileSize;
  }

  public void setFileSize(Long fileSize) {
    this.fileSize = fileSize;
  }

  @Column(name = "FILE_DESC")
  public String getFileDesc() {
    return fileDesc;
  }

  public void setFileDesc(String fileDesc) {
    this.fileDesc = fileDesc;
  }

  @Column(name = "FILE_STATUS")
  public int getFileStatus() {
    return fileStatus;
  }

  public void setFileStatus(int fileStatus) {
    this.fileStatus = fileStatus;
  }

  @Column(name = "GROUP_FOLDER_IDS")
  public String getGroupFolderIds() {
    return groupFolderIds;
  }

  public void setGroupFolderIds(String groupFolderIds) {
    this.groupFolderIds = groupFolderIds;
  }

  @Column(name = "NODE_ID")
  public int getNodeId() {
    return nodeId;
  }

  public void setNodeId(int nodeId) {
    this.nodeId = nodeId;
  }

  @Column(name = "OWNER_PSN_ID")
  public Long getPsnId() {
    return psnId;
  }

  public void setPsnId(Long psnId) {
    this.psnId = psnId;
  }

  @Column(name = "UPLOAD_TIME")
  public Date getUploadTime() {
    return uploadTime;
  }

  public void setUploadTime(Date uploadTime) {
    this.uploadTime = uploadTime;
  }

  @Column(name = "AUTHOR_NAMES")
  public String getAuthorName() {
    return authorName;
  }

  public void setAuthorName(String authorName) {
    this.authorName = authorName;
  }

  @Column(name = "ARCHIVE_FILE_ID")
  public Long getArchiveFileId() {
    return archiveFileId;
  }

  public void setArchiveFileId(Long archiveFileId) {
    this.archiveFileId = archiveFileId;
  }

  @Column(name = "FILE_MODULE_TYPE")
  public int getFileModuleType() {
    return fileModuleType;
  }

  public void setFileModuleType(int fileModuleType) {
    this.fileModuleType = fileModuleType;
  }

}
