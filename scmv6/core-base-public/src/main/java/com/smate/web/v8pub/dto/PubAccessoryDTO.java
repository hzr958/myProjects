package com.smate.web.v8pub.dto;

import java.io.Serializable;

/**
 * 成果附件表
 * 
 * @author aijiangbin
 * @String 2018年5月31日
 */

public class PubAccessoryDTO implements Serializable {

  private static final long serialVersionUID = 2190446272925143774L;

  private Long id; // 主键

  private Long pubId; // 成果id

  private Long fileId; // 文件id

  private String fileName; // 文件名

  private Integer permission = 0; // 文件 权限 0=所有人；2=仅本人

  private String gmtCreate; // 创建时间

  private String gmtModified; // 更新时间


  public PubAccessoryDTO() {
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

  public Integer getPermission() {
    return permission;
  }

  public void setPermission(Integer permission) {
    this.permission = permission;
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

}
