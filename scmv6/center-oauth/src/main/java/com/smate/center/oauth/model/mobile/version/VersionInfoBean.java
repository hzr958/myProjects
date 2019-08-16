package com.smate.center.oauth.model.mobile.version;

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
 * 移动端版本信息实体类
 */
@Entity
@Table(name = "V_MOBILE_APP_VERSION")
public class VersionInfoBean implements Serializable {

  private static final long serialVersionUID = 1L;
  private Long id;
  private String appType; // app类型，安卓APP/苹果APP
  private Integer mustUpdate; // 是否需要强制更新， 1（需要），0（不用）
  private String versionName;// app版本号，如 1.0.1
  private Integer versionCode;// app版本code，方便APP端判断是否需要更新，安卓的APP中build.gradle中有个versionCode属性
  private String versionDesc;// 版本更新说明
  private String downloadUrl;// 新版本APP下载地址
  private String versionSize;// 版本更新包的大小，如 9.5 MB
  private String newMd5; // app文件签名的MD5值
  private Date updateDate; // 更新时间
  private Integer status; // 是否已删除，1（未删除）， 0（已删除）

  public VersionInfoBean() {
    super();
  }

  public VersionInfoBean(Long id, String appType, Integer mustUpdate, String versionName, Integer versionCode,
      String versionDesc, String downloadUrl, String versionSize, String newMd5, Date updateDate, Integer status) {
    super();
    this.id = id;
    this.appType = appType;
    this.mustUpdate = mustUpdate;
    this.versionName = versionName;
    this.versionCode = versionCode;
    this.versionDesc = versionDesc;
    this.downloadUrl = downloadUrl;
    this.versionSize = versionSize;
    this.newMd5 = newMd5;
    this.updateDate = updateDate;
    this.status = status;
  }

  public VersionInfoBean(String appType, Integer mustUpdate, String versionName, Integer versionCode,
      String versionDesc, String downloadUrl, String versionSize, String newMd5, Date updateDate, Integer status) {
    super();
    this.appType = appType;
    this.mustUpdate = mustUpdate;
    this.versionName = versionName;
    this.versionCode = versionCode;
    this.versionDesc = versionDesc;
    this.downloadUrl = downloadUrl;
    this.versionSize = versionSize;
    this.newMd5 = newMd5;
    this.updateDate = updateDate;
    this.status = status;
  }

  @Column(name = "VERSION_SIZE")
  public String getVersionSize() {
    return versionSize;
  }

  public void setVersionSize(String versionSize) {
    this.versionSize = versionSize;
  }

  @Column(name = "VERSION_NAME")
  public String getVersionName() {
    return versionName;
  }

  public void setVersionName(String versionName) {
    this.versionName = versionName;
  }

  @Column(name = "VERSION_CODE")
  public Integer getVersionCode() {
    return versionCode;
  }

  public void setVersionCode(Integer versionCode) {
    this.versionCode = versionCode;
  }

  @Column(name = "VERSION_DESC")
  public String getVersionDesc() {
    return versionDesc;
  }

  public void setVersionDesc(String versionDesc) {
    this.versionDesc = versionDesc;
  }

  @Column(name = "DOWNLOAD_URL")
  public String getDownloadUrl() {
    return downloadUrl;
  }

  public void setDownloadUrl(String downloadUrl) {
    this.downloadUrl = downloadUrl;
  }


  @Id
  @Column(name = "ID")
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "MOBILE_APP_VERSION_SEQ", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  @Column(name = "APP_TYPE")
  public String getAppType() {
    return appType;
  }

  public void setAppType(String appType) {
    this.appType = appType;
  }

  @Column(name = "MUST_UPDATE")
  public Integer getMustUpdate() {
    return mustUpdate;
  }

  public void setMustUpdate(Integer mustUpdate) {
    this.mustUpdate = mustUpdate;
  }

  @Column(name = "NEW_MD5")
  public String getNewMd5() {
    return newMd5;
  }

  public void setNewMd5(String newMd5) {
    this.newMd5 = newMd5;
  }

  @Column(name = "UPDATE_DATE")
  public Date getUpdateDate() {
    return updateDate;
  }

  public void setUpdateDate(Date updateDate) {
    this.updateDate = updateDate;
  }

  @Column(name = "STATUS")
  public Integer getStatus() {
    return status;
  }

  public void setStatus(Integer status) {
    this.status = status;
  }


}
