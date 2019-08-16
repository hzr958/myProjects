package com.smate.center.oauth.model.bind;

/**
 * @description 接收qq登录的AccessToken
 * @author xiexing
 * @date 2019年2月18日
 */
public class AccessToken {
  private String accessToken;
  private String expireIn;

  public String getAccessToken() {
    return accessToken;
  }

  public void setAccessToken(String accessToken) {
    this.accessToken = accessToken;
  }

  public String getExpireIn() {
    return expireIn;
  }

  public void setExpireIn(String expireIn) {
    this.expireIn = expireIn;
  }

  public AccessToken(String accessToken, String expireIn) {
    super();
    this.accessToken = accessToken;
    this.expireIn = expireIn;
  }

  public AccessToken() {
    super();
    // TODO Auto-generated constructor stub
  }
}

