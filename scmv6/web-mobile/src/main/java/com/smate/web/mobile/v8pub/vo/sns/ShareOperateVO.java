package com.smate.web.mobile.v8pub.vo.sns;

public class ShareOperateVO {
  private String des3ResId;// 分享的资源id
  private Long resId;
  private Integer resType;// 分享的类型1、2个人成果，4项目，11基金，22、24基准库成果，25资助机构
  private Integer platform; // 分享平台 ； 微信；微博；等 见：SharePlatformEnum

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

}
