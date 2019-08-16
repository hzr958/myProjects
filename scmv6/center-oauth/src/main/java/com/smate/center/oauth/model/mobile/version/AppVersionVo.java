package com.smate.center.oauth.model.mobile.version;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.smate.core.base.utils.model.Page;

/**
 * app版本控制Form
 * 
 * @author wsn
 * @date Jan 4, 2019
 */
public class AppVersionVo implements Serializable {

  private static final long serialVersionUID = 165447710754433467L;
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
  private List<AppVersionVo> appVersionList; // app版本信息list
  private Integer optType; // 操作类型：新增（1），删除（0）， 编辑（2）
  // private Integer pageNo; // 第几页
  // private Integer pageSize = 10;// 每页显示记录数
  // private Integer totalPage; // 一共多少页
  private Page page = new Page();
  private String domainScm; // 科研之友域名

  public AppVersionVo() {
    super();
  }

  public AppVersionVo(Long id, String appType, Integer mustUpdate, String versionName, Integer versionCode,
      String versionDesc, String downloadUrl, String versionSize, String newMd5, Date updateDate) {
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
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getAppType() {
    return appType;
  }

  public void setAppType(String appType) {
    this.appType = appType;
  }

  public Integer getMustUpdate() {
    return mustUpdate;
  }

  public void setMustUpdate(Integer mustUpdate) {
    this.mustUpdate = mustUpdate;
  }

  public String getVersionName() {
    return versionName;
  }

  public void setVersionName(String versionName) {
    this.versionName = versionName;
  }

  public Integer getVersionCode() {
    return versionCode;
  }

  public void setVersionCode(Integer versionCode) {
    this.versionCode = versionCode;
  }

  public String getVersionDesc() {
    return versionDesc;
  }

  public void setVersionDesc(String versionDesc) {
    this.versionDesc = versionDesc;
  }

  public String getDownloadUrl() {
    return downloadUrl;
  }

  public void setDownloadUrl(String downloadUrl) {
    this.downloadUrl = downloadUrl;
  }

  public String getVersionSize() {
    return versionSize;
  }

  public void setVersionSize(String versionSize) {
    this.versionSize = versionSize;
  }

  public String getNewMd5() {
    return newMd5;
  }

  public void setNewMd5(String newMd5) {
    this.newMd5 = newMd5;
  }

  public Date getUpdateDate() {
    return updateDate;
  }

  public void setUpdateDate(Date updateDate) {
    this.updateDate = updateDate;
  }

  public List<AppVersionVo> getAppVersionList() {
    return appVersionList;
  }

  public void setAppVersionList(List<AppVersionVo> appVersionList) {
    this.appVersionList = appVersionList;
  }

  public Integer getOptType() {
    return optType;
  }

  public void setOptType(Integer optType) {
    this.optType = optType;
  }

  public Page getPage() {
    return page;
  }

  public void setPage(Page page) {
    this.page = page;
  }

  public String getDomainScm() {
    return domainScm;
  }

  public void setDomainScm(String domainScm) {
    this.domainScm = domainScm;
  }



}
