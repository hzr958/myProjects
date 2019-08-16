package com.smate.core.base.utils.security;

/**
 * 存储当前操作的sessionid信息.
 * 
 */
public class TheadLocalSessionId {
  private static ThreadLocal<String> sessionId = new ThreadLocal<String>();

  public static String getSessionId() {
    return sessionId.get();
  }

  public static void setSessionId(String uid) {
    sessionId.set(uid);
  }

}
