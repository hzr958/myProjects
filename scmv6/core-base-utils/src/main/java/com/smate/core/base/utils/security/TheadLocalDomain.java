package com.smate.core.base.utils.security;

/**
 * 已经废弃
 * 
 * @author tsz
 *
 */
public class TheadLocalDomain {
  private static ThreadLocal<String> domain = new ThreadLocal<String>();

  @Deprecated
  public static String getDomain() {
    return domain.get();
  }

  @Deprecated
  public static void setDomain(String domainStr) {
    domain.set(domainStr);
  }

}
