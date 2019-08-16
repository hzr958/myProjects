package com.smate.core.base.utils.security;

/**
 * 存储当前操作的unitId信息.
 * 
 */
public class TheadLocalUnitId {
  private static ThreadLocal<Long> unitId = new ThreadLocal<Long>();

  public static Long getUnitId() {
    return unitId.get();
  }

  public static void setUnitId(Long uid) {
    unitId.set(uid);
  }

}
