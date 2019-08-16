package com.smate.core.base.utils.security;


/**
 * 保存当前服务器的Web服务Id.
 * 
 * @author liqinghua
 * 
 */
public class TheadLocalWebServerId {
  private static Long wsId;

  public static Long getWsId() {
    return wsId;
  }

  public static void setWsId(Long wsId) {
    TheadLocalWebServerId.wsId = wsId;
  }


}
