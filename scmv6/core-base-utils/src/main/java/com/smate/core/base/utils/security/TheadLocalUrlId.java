package com.smate.core.base.utils.security;


/**
 * 保存当前服务器的服务url Id.
 * 
 * @author liqinghua
 * 
 */
public class TheadLocalUrlId {

  private static Long urlId;

  public static Long getUrlId() {
    return urlId;
  }

  public static void setUrlId(Long urlId) {
    TheadLocalUrlId.urlId = urlId;
  }


}
