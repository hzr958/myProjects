package com.smate.core.base.utils.security;

/**
 * 存储当前操作的单位id信息.
 * 
 */
public class TheadLocalInsId {
  private static ThreadLocal<Long> insId = new ThreadLocal<Long>();

  public static Long getInsId() {
    return insId.get();
  }

  public static void setInsId(Long insid) {
    insId.set(insid);
  }

}
