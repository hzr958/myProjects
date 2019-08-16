package com.smate.center.oauth.service.password;

/**
 * 密码加密客户端加密后 服务短解密 实现类 供密码传输用
 * 
 * @author tsz
 * 
 */
public interface DecryptPwdService {

  /**
   * 缓存名称
   */
  String CACHE_NAME = "pwdDesKey";

  /**
   * 密码解密
   * 
   */
  String DeDesPassword(String username, String desData);

}
