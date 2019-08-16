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

/**
 * 文件主表，所以文件上传后都会在这里产生记录
 * 
 * @author tsz
 * 
 */
@Entity
@Table(name = "ARCHIVE_FILES")
public class ArchiveFile implements Serializable {

  private static final long serialVersionUID = -2221542400406014732L;

  @Id
  @Column(name = "FILE_ID")
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_ARCHIVE_FILES", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
  private Long fileId;
  @Column(name = "FILE_TYPE")
  private String fileType;
  @Column(name = "CREATE_PSN_ID")
  private Long createPsnId;
  @Column(name = "CREATE_TIME")
  private Date createTime;
  @Column(name = "FILE_NAME")
  private String fileName;
  @Column(name = "FILE_DESC")
  private String fileDesc;
  @Column(name = "FILE_PATH")
  private String filePath;
  @Column(name = "FILE_URL")
  private String fileUrl; // 文件路径
  @Column(name = "FILE_SIZE")
  private Long fileSize; // 文件大小 单位kb
  @Column(name = "STATUS", columnDefinition = "INT default 0", nullable = false)
  private Integer status = 0; // 文件状态; 0--正常, 1--删除; 默认0
  @Column(name = "FILE_UUID")
  private String fileUUID;
  @Column(name = "FILE_NODE_ID")
  private Integer nodeId;
  @Column(name = "FILE_INS_ID")
  private Long insId;
  @Column(name = "FILE_FROM")
  private String fileFrom; // 文件来源

  /**
   * @return
   */

  public Long getFileId() {
    return fileId;
  }

  public void setFileId(Long fileId) {
    this.fileId = fileId;
  }

  public String getFileType() {
    return fileType;
  }

  public void setFileType(String fileType) {
    this.fileType = fileType;
  }

  public Long getCreatePsnId() {
    return createPsnId;
  }

  public void setCreatePsnId(Long createPsnId) {
    this.createPsnId = createPsnId;
  }

  public Date getCreateTime() {
    return createTime;
  }

  public void setCreateTime(Date date) {
    this.createTime = date;
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

  public String getFileUrl() {
    return fileUrl;
  }

  public void setFileUrl(String fileUrl) {
    this.fileUrl = fileUrl;
  }

  public Long getFileSize() {
    return fileSize;
  }

  public void setFileSize(Long fileSize) {
    this.fileSize = fileSize;
  }

  public Integer getStatus() {
    return status;
  }

  public void setStatus(Integer status) {
    if (status == null || (status != 0 && status != 1)) {
      this.status = 0;
    } else {
      this.status = status;
    }
  }

  /**
   * @return fileUUID
   */
  public String getFileUUID() {
    return fileUUID;
  }

  /**
   * @param fileUUID 要设置的 fileUUID
   */
  public void setFileUUID(String fileUUID) {
    this.fileUUID = fileUUID;
  }

  /**
   * @return nodeId
   */
  public Integer getNodeId() {
    return nodeId;
  }

  /**
   * @param nodeId 要设置的 nodeId
   */
  public void setNodeId(Integer nodeId) {
    this.nodeId = nodeId;
  }

  /**
   * @return insId
   */
  public Long getInsId() {
    return insId;
  }

  /**
   * @param insId 要设置的 insId
   */
  public void setInsId(Long insId) {
    this.insId = insId;
  }

  /**
   * @return fileDesc
   */
  public String getFileDesc() {
    return fileDesc;
  }

  /**
   * @param fileDesc 要设置的 fileDesc
   */
  public void setFileDesc(String fileDesc) {
    this.fileDesc = fileDesc;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((createTime == null) ? 0 : createTime.hashCode());
    result = prime * result + ((fileName == null) ? 0 : fileName.hashCode());
    result = prime * result + ((filePath == null) ? 0 : filePath.hashCode());
    result = prime * result + ((fileType == null) ? 0 : fileType.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    ArchiveFile other = (ArchiveFile) obj;
    if (createTime == null) {
      if (other.createTime != null)
        return false;
    } else if (!createTime.equals(other.createTime))
      return false;
    if (fileName == null) {
      if (other.fileName != null)
        return false;
    } else if (!fileName.equals(other.fileName))
      return false;
    if (filePath == null) {
      if (other.filePath != null)
        return false;
    } else if (!filePath.equals(other.filePath))
      return false;
    if (fileType == null) {
      if (other.fileType != null)
        return false;
    } else if (!fileType.equals(other.fileType))
      return false;
    return true;
  }

  public String getFileFrom() {
    return fileFrom;
  }

  public void setFileFrom(String fileFrom) {
    this.fileFrom = fileFrom;
  }

}
