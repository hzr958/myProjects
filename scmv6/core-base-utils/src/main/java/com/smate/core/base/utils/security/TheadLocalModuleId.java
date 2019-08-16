package com.smate.core.base.utils.security;


/**
 * 保存当前服务器的模块Id.
 * 
 * @author liqinghua
 * 
 */
public class TheadLocalModuleId {
  private static Long mdId;

  public static Long getMdId() {
    return mdId;
  }

  public static void setMdId(Long mdId) {
    TheadLocalModuleId.mdId = mdId;
  }


}
