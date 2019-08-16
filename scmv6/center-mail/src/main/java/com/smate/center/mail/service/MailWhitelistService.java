package com.smate.center.mail.service;

/**
 * 白名单服务
 * 
 * @author tsz
 *
 */
public interface MailWhitelistService {

  /**
   * 是否存在陈名单里面
   * 
   * @param email
   * @return
   */
  public boolean isExistsWhitelist(String email);
}
