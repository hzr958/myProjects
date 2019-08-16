package com.smate.center.oauth.model.bind;

import java.io.Serializable;

/**
 * 微信代理转发Form
 * 
 * @author wcw
 * 
 */
public class WeChatProxyForm implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = 2634052496836345704L;
  private String from;// 请求来源
  private String code;// 开放平台code
  private String state;

  public String getFrom() {
    return from;
  }

  public void setFrom(String from) {
    this.from = from;
  }

  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public String getState() {
    return state;
  }

  public void setState(String state) {
    this.state = state;
  }

}
