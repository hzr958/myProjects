package com.smate.core.base.utils.security;

/**
 * 存储当前操作的roleId信息.
 * 
 */
public class TheadLocalRoleId {
  private static ThreadLocal<Integer> roleId = new ThreadLocal<Integer>();

  public static Integer getRoleId() {
    return roleId.get();
  }

  public static void setRoleId(Integer rId) {
    roleId.set(rId);
  }

}
