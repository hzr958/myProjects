package com.smate.web.v8pub.vo;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * 成果附件转换对象
 * 
 * @author aijiangbin
 * @date 2018年5月31日
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class PubAccessoryVO {

  private Long pubId; // 成果id

  private String des3fileId; // 文件id

  private String fileName; // 文件名

  private String fileDesc; // 文件描述

  private Integer permission = 0; // 文件 权限 0=所有人；2=仅本人

  private Date gmtCreate; // 创建时间

  private Date gmtModified; // 更新时间


  public PubAccessoryVO() {
    super();
  }

  public Long getPubId() {
    return pubId;
  }


  public void setPubId(Long pubId) {
    this.pubId = pubId;
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


  public String getFileDesc() {
    return fileDesc;
  }


  public void setFileDesc(String fileDesc) {
    this.fileDesc = fileDesc;
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



}
