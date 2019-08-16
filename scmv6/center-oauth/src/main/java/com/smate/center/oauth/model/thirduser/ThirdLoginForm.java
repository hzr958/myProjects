package com.smate.center.oauth.model.thirduser;

import java.io.Serializable;

/**
 * 第三方登录Form
 * 
 * @author wsn
 * @date Mar 28, 2019
 */
public class ThirdLoginForm implements Serializable {

  private String weiboNickName; // 用户昵称
  private String unionId; // 第三方账号唯一标识
  private String forwardUrl;
  private String code; // 第三方账号登录回调传的参数
  private String accessToken; // 第三方接口调用token
  private String error_code; // 微博登录错误码
  private String loginType; // 1：qq, 2:微博, 3:微信
  private String AID; // 自动登录串

  public String getWeiboNickName() {
    return weiboNickName;
  }

  public void setWeiboNickName(String weiboNickName) {
    this.weiboNickName = weiboNickName;
  }

  public String getUnionId() {
    return unionId;
  }

  public void setUnionId(String unionId) {
    this.unionId = unionId;
  }

  public String getForwardUrl() {
    return forwardUrl;
  }

  public void setForwardUrl(String forwardUrl) {
    this.forwardUrl = forwardUrl;
  }

  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public String getAccessToken() {
    return accessToken;
  }

  public void setAccessToken(String accessToken) {
    this.accessToken = accessToken;
  }

  public String getError_code() {
    return error_code;
  }

  public void setError_code(String error_code) {
    this.error_code = error_code;
  }

  public String getLoginType() {
    return loginType;
  }

  public void setLoginType(String loginType) {
    this.loginType = loginType;
  }

  public String getAID() {
    return AID;
  }

  public void setAID(String aID) {
    AID = aID;
  }

}
