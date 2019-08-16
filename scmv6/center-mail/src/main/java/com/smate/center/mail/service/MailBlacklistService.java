package com.smate.center.mail.service;

/**
 * 黑名单服务
 * 
 * @author tsz
 *
 */
public interface MailBlacklistService {

  /**
   * 是否存在于黑名单
   * 
   * @param emal
   * @return
   */
  public boolean isExistsBlacklist(String email);

  /**
   * 是否存在于黑名单 (检查是否有域名 黑名单)
   * 
   * @param emal
   * @return
   */
  public boolean isExistsBlacklistHost(String host);
}
