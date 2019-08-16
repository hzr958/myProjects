package com.smate.web.psn.service.desdepassword;

/**
 * 密码加密客户端加密,服务端解密 接口 供密码传输用
 * 
 * @author lhd
 *
 */
public interface DesDePasswordService {

  /**
   * 缓存名称
   */
  // String CACHE_NAME = "pwdDesKey";

  /**
   * 密码解密
   * 
   */
  String DesDePassword(String username, String desData);
}
