package com.smate.center.oauth.service.security;

import org.springframework.dao.DataAccessException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 * 获取UserDetails的服务
 *
 * @author wsn
 *
 */
public interface OauthUserDetailsService extends UserDetailsService {

  /**
   * 根据来源系统获取UserDetails
   * 
   * @param userName
   * @param sys
   * @param url
   * @return
   * @throws UsernameNotFoundException
   * @throws DataAccessException
   */
  public UserDetails loadUserFromSys(String userName, String sys, String url)
      throws UsernameNotFoundException, DataAccessException;

  /**
   * 根据系统标识和域名获取insId
   * 
   * @return
   * @throws DataAccessException
   */
  public Long findInsIdBySysAndDomain(String sys, String domain) throws DataAccessException;
}
