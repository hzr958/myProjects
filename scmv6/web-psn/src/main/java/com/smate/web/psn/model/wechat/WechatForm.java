package com.smate.web.psn.model.wechat;

import java.io.Serializable;

/**
 * 微信相关业务form
 * 
 * @author wsn
 * @date 2018年10月10日
 */
public class WechatForm implements Serializable {

  private static final long serialVersionUID = -6633616299364497654L;

  private String currentUrl; // 当前页面的url

  public String getCurrentUrl() {
    return currentUrl;
  }

  public void setCurrentUrl(String currentUrl) {
    this.currentUrl = currentUrl;
  }

}
