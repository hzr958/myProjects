package com.smate.web.mobile.wechat.base.vo;

public class WechatBaseVO {

  private String wxOpenId;// 微信openid
  private String des3Wid;// 微信加密id
  // 页面调用微信js
  private String timestamp;// 必填，生成签名的时间戳
  private String nonceStr;// 必填，生成签名的随机串
  private String signature;// 必填，签名
  private String code;// 微信网页授权code
  private String appId; // 微信appId
  private String domain;
  private String shareUrl; // 分享url

  public String getWxOpenId() {
    return wxOpenId;
  }

  public void setWxOpenId(String wxOpenId) {
    this.wxOpenId = wxOpenId;
  }

  public String getTimestamp() {
    return timestamp;
  }

  public void setTimestamp(String timestamp) {
    this.timestamp = timestamp;
  }

  public String getNonceStr() {
    return nonceStr;
  }

  public void setNonceStr(String nonceStr) {
    this.nonceStr = nonceStr;
  }

  public String getSignature() {
    return signature;
  }

  public void setSignature(String signature) {
    this.signature = signature;
  }

  public String getAppId() {
    return appId;
  }

  public void setAppId(String appId) {
    this.appId = appId;
  }

  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public String getDes3Wid() {
    return des3Wid;
  }

  public void setDes3Wid(String des3Wid) {
    this.des3Wid = des3Wid;
  }

  public String getDomain() {
    return domain;
  }

  public void setDomain(String domain) {
    this.domain = domain;
  }

  public String getShareUrl() {
    return shareUrl;
  }

  public void setShareUrl(String shareUrl) {
    this.shareUrl = shareUrl;
  }

}
