package com.smate.web.dyn.form.dynamic;

public class ShareOperateForm {
  private String des3ResId;// 分享的资源id
  private Long resId;
  private Integer resType;// 分享的类型1、2个人成果，4项目，11基金，22、24基准库成果，25资助机构
  private Integer platform; // 分享平台 ； 微信；微博；等 见：SharePlatformEnum
  private String searchKey;
  private String des3PsnId;
  private String shareText; // 分享留言

  public Integer getPlatform() {
    return platform;
  }

  public void setPlatform(Integer platform) {
    this.platform = platform;
  }

  public String getDes3ResId() {
    return des3ResId;
  }

  public void setDes3ResId(String des3ResId) {
    this.des3ResId = des3ResId;
  }

  public Integer getResType() {
    return resType;
  }

  public void setResType(Integer resType) {
    this.resType = resType;
  }

  public Long getResId() {
    return resId;
  }

  public void setResId(Long resId) {
    this.resId = resId;
  }

  public String getSearchKey() {
    return searchKey;
  }

  public void setSearchKey(String searchKey) {
    this.searchKey = searchKey;
  }

  public String getDes3PsnId() {
    return des3PsnId;
  }

  public void setDes3PsnId(String des3PsnId) {
    this.des3PsnId = des3PsnId;
  }

  public String getShareText() {
    return shareText;
  }

  public void setShareText(String shareText) {
    this.shareText = shareText;
  }



}
