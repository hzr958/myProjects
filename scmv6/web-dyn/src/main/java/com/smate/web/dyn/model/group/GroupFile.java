package com.smate.web.dyn.model.group;

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

import com.smate.core.base.utils.common.HtmlUtils;
import com.smate.core.base.utils.security.SecurityUtils;
import com.smate.core.base.utils.string.ServiceUtil;

/**
 * 群组文件.
 * 
 * @author zk
 * 
 */
@Entity
@Table(name = "GROUP_FILES")
public class GroupFile implements Serializable {

  private static final long serialVersionUID = -5725966204389517562L;
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
  private int isMe = 0;// 标识是不是我的文件.
  // 加密文件ID
  private String des3FileId;
  // 截取转义文件描述
  private String subFileDesc;
  // 转义文件描述
  private String escapeFileDesc;
  private Long archiveFileId;
  // 文件所属模块[0: 群组文件;1: 作业;2: 教学课件]
  private int fileModuleType;

  private String psnName;// 文件上传人员

  public GroupFile() {
    super();
  }

  public GroupFile(Long groupFileId, String fileName, Long fileSize, Date uploadTime, Long psnId, Long fileId,
      String filePath, String fileDesc, String fileType) {
    super();
    this.groupFileId = groupFileId;
    this.fileName = fileName;
    this.fileSize = fileSize;
    this.uploadTime = uploadTime;
    this.psnId = psnId;
    this.fileId = fileId;
    this.filePath = filePath;
    this.fileDesc = fileDesc;
    this.fileType = fileType;
  }


  public GroupFile(Long groupFileId, Long fileId, int fileStatus, Long psnId) {
    super();
    this.groupFileId = groupFileId;
    this.fileId = fileId;
    this.fileStatus = fileStatus;
    this.psnId = psnId;
  }

  @Id
  @Column(name = "GROUP_FILES_ID")
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_GROUP_FILES", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
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


  @Transient
  public int getIsMe() {
    if (psnId.equals(SecurityUtils.getCurrentUserId()))
      isMe = 1;
    else
      isMe = 0;
    return isMe;
  }

  @Transient
  public String getDes3FileId() {
    if (this.fileId != null && des3FileId == null) {
      des3FileId = ServiceUtil.encodeToDes3(this.fileId.toString());
    }
    return des3FileId;
  }

  public void setDes3FileId(String des3FileId) {
    if (this.fileId == null && StringUtils.isNotBlank(des3FileId)) {
      this.fileId = Long.valueOf(ServiceUtil.decodeFromDes3(des3FileId));
    }
    this.des3FileId = des3FileId;
  }

  @Transient
  public String getSubFileDesc() {
    if (this.fileDesc != null) {
      if (this.fileDesc.length() > 32)
        subFileDesc = this.fileDesc.substring(0, 32) + "...";
      else
        subFileDesc = this.fileDesc;
    }
    return HtmlUtils.toHtml(subFileDesc);
  }

  @Transient
  public String getEscapeFileDesc() {
    if (this.fileDesc != null)
      escapeFileDesc = HtmlUtils.toHtml(this.fileDesc);
    return escapeFileDesc;
  }

  @Transient
  public String getPsnName() {
    return psnName;
  }

  public void setPsnName(String psnName) {
    this.psnName = psnName;
  }

}
