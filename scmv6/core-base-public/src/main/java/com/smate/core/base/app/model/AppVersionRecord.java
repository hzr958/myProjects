package com.smate.core.base.app.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * app版本信息记录表
 * 
 * @author LIJUN
 * @date 2018年4月3日
 */
@Entity
@Table(name = "APP_VERSION_RECORD")
public class AppVersionRecord {
  @Id
  @Column(name = "ID")
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_APP_VERSION_RECORD", allocationSize = 1)
  @GeneratedValue(generator = "SEQ_STORE", strategy = GenerationType.SEQUENCE)
  private Long id;
  @Column(name = "VERSION_CODE")
  private String versionCode;// 版本号,app界面显示eg:v1.0.1
  @Column(name = "VERSION_NUMBER")
  private Long versionNumber;// 版本编号，内部使用 新版本编号要大于旧版本
  @Column(name = "APP_TYPE")
  private Integer appType;// app 类型 1:ios, 2: android
  @Column(name = "VERSION_DESCRIPTION")
  private String versionDescription;// 版本描述，显示版本信息 eg: 1.新增了功能; 2.修复了bug
  // (需要用;分隔开)
  @Column(name = "STATUS")
  private Integer status;// 状态，默认0表示可用
  @Column(name = "UPDATE_TIME")
  private Date updateTime;// 更新时间

  public AppVersionRecord(Long id, String versionCode, Long versionNumber, Integer appType, String versionDescription,
      Integer status, Date updateTime) {
    super();
    this.id = id;
    this.versionCode = versionCode;
    this.versionNumber = versionNumber;
    this.appType = appType;
    this.versionDescription = versionDescription;
    this.status = status;
    this.updateTime = updateTime;
  }

  public AppVersionRecord() {
    super();
    // TODO Auto-generated constructor stub
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getVersionCode() {
    return versionCode;
  }

  public void setVersionCode(String versionCode) {
    this.versionCode = versionCode;
  }

  public Long getVersionNumber() {
    return versionNumber;
  }

  public void setVersionNumber(Long versionNumber) {
    this.versionNumber = versionNumber;
  }

  public Integer getAppType() {
    return appType;
  }

  public void setAppType(Integer appType) {
    this.appType = appType;
  }

  public String getVersionDescription() {
    return versionDescription;
  }

  public void setVersionDescription(String versionDescription) {
    this.versionDescription = versionDescription;
  }

  public Integer getStatus() {
    return status;
  }

  public void setStatus(Integer status) {
    this.status = status;
  }

  public Date getUpdateTime() {
    return updateTime;
  }

  public void setUpdateTime(Date updateTime) {
    this.updateTime = updateTime;
  }

}
