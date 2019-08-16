package com.smate.core.web.sys.security.user;

import java.util.Map;

import org.springframework.dao.DataAccessException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 * 科研之友用户权限信息接口
 * 
 * @author wsn
 *
 */
public interface ScmUserDetailsService {

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

  /**
   * 构建用户信息Map
   * 
   * @param sessionId
   * @param targetUrl
   * @param sys
   * @param userDetails
   * @return
   * @throws Exception
   */
  public Map<String, Object> buildUserDetailsMap(String sessionId, String targetUrl, String sys,
      UserDetails userDetails) throws Exception;
}
