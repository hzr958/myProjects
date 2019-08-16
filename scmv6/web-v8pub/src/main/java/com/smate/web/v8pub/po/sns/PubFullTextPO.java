package com.smate.web.v8pub.po.sns;

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
 * 个人库成果全文表
 * 
 * @author YJ 2018年5月31日
 */

@Entity
@Table(name = "V_PUB_FULLTEXT")
public class PubFullTextPO implements Serializable {

  private static final long serialVersionUID = 8856316809928020406L;

  @Id
  @SequenceGenerator(name = "SEQ_PUB_FULLTEXT_ID", sequenceName = "SEQ_PUB_FULLTEXT_ID", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_PUB_FULLTEXT_ID")
  @Column(name = "ID")
  private Long id; // 逻辑id，主键

  @Column(name = "PUB_ID")
  private Long pubId; // 成果id

  @Column(name = "FILE_ID")
  private Long fileId; // 全文附件id，对应archive_files中的file_id

  @Column(name = "FILE_NAME")
  private String fileName; // 文件名

  @Column(name = "FILE_DESC")
  private String fileDesc; // 文件描述

  @Column(name = "THUMBNAIL_PATH")
  private String thumbnailPath; // 全文缩略图图片路径

  @Column(name = "PERMISSION", columnDefinition = "INT default 0")
  private Integer permission; // 全文下载权限，0所有人可下载，2自己可下载

  @Column(name = "GMT_CREATE")
  private Date gmtCreate; // 创建时间

  @Column(name = "GMT_MODIFIED")
  private Date gmtModified; // 更新时间

  public PubFullTextPO() {
    super();
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Long getPubId() {
    return pubId;
  }

  public void setPubId(Long pubId) {
    this.pubId = pubId;
  }

  public Long getFileId() {
    return fileId;
  }

  public void setFileId(Long fileId) {
    this.fileId = fileId;
  }

  public String getFileName() {
    return fileName;
  }

  public void setFileName(String fileName) {
    this.fileName = fileName;
  }

  public String getFileDesc() {
    return fileDesc;
  }

  public void setFileDesc(String fileDesc) {
    this.fileDesc = fileDesc;
  }

  public String getThumbnailPath() {
    return thumbnailPath;
  }

  public void setThumbnailPath(String thumbnailPath) {
    this.thumbnailPath = thumbnailPath;
  }

  public Integer getPermission() {
    return permission;
  }

  public void setPermission(Integer permission) {
    this.permission = permission;
  }

  public Date getGmtCreate() {
    return gmtCreate;
  }

  public void setGmtCreate(Date gmtCreate) {
    this.gmtCreate = gmtCreate;
  }

  public Date getGmtModified() {
    return gmtModified;
  }

  public void setGmtModified(Date gmtModified) {
    this.gmtModified = gmtModified;
  }

  @Override
  public String toString() {
    return "PubFullTextPO{" + "id='" + id + '\'' + ", pubId='" + pubId + '\'' + ", fileId='" + fileId + '\''
        + ", fileName='" + fileName + '\'' + ", fileDesc='" + fileDesc + '\'' + ", thumbnailPath='" + thumbnailPath
        + '\'' + ", permission='" + permission + '\'' + ", gmtCreate='" + gmtCreate + '\'' + ", gmtModified='"
        + gmtModified + '\'' + '}';
  }

}
