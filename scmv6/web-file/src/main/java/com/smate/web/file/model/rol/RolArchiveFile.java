package com.smate.web.file.model.rol;

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

import com.smate.core.base.utils.string.ServiceUtil;

/**
 * 附件信息.
 * 
 * @author zk
 * 
 */
@Entity
@Table(name = "ARCHIVE_FILES")
public class RolArchiveFile implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = 6224462364391862499L;
  /**
   * 
   */

  private Long fileId;
  // 加密ID
  private String des3FileId;
  private String fileType;
  private Long createPsnId;
  private Date createTime;
  private String fileDesc;
  private String fileName;
  private String filePath;
  private Integer nodeId;
  private Long insId;
  private String fileUUID;

  /**
   * @return
   */
  @Id
  @Column(name = "FILE_ID")
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_ARCHIVE_FILES", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
  public Long getFileId() {
    return fileId;
  }

  public void setFileId(Long fileId) {
    this.fileId = fileId;
  }

  @Column(name = "FILE_TYPE")
  public String getFileType() {
    return fileType;
  }

  public void setFileType(String fileType) {
    this.fileType = fileType;
  }

  @Column(name = "CREATE_PSN_ID")
  public Long getCreatePsnId() {
    return createPsnId;
  }

  public void setCreatePsnId(Long createPsnId) {
    this.createPsnId = createPsnId;
  }

  @Column(name = "CREATE_TIME")
  public Date getCreateTime() {
    return createTime;
  }

  public void setCreateTime(Date date) {
    this.createTime = date;
  }

  @Column(name = "FILE_DESC")
  public String getFileDesc() {
    return fileDesc;
  }

  public void setFileDesc(String fileDesc) {
    this.fileDesc = fileDesc;
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

  @Column(name = "FILE_NODE_ID")
  public Integer getNodeId() {
    return nodeId;
  }

  @Column(name = "FILE_INS_ID")
  public Long getInsId() {
    return insId;
  }

  public void setNodeId(Integer nodeId) {
    this.nodeId = nodeId;
  }

  @Column(name = "FILE_UUID")
  public String getFileUUID() {
    return fileUUID;
  }

  public void setFileUUID(String fileUUID) {
    this.fileUUID = fileUUID;
  }

  public void setInsId(Long insId) {
    this.insId = insId;
  }

  @Transient
  public String getDes3FileId() {

    if (this.fileId != null && des3FileId == null) {
      des3FileId = ServiceUtil.encodeToDes3(this.fileId.toString());
    }
    return des3FileId;
  }

  public void setDes3FileId(String des3FileId) {
    this.des3FileId = des3FileId;
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
    RolArchiveFile other = (RolArchiveFile) obj;
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

}
