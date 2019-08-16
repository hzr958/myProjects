package com.smate.center.oauth.service.login;

import java.util.Map;

import org.springframework.security.core.userdetails.UserDetails;

import com.smate.center.oauth.exception.OauthException;

/**
 * oauth服务类
 *
 * @author wsn
 *
 */
public interface OauthService {

  /**
   * 缓存用户权限信息
   * 
   * @param sessionId
   * @param targetUrl
   * @param sys
   * @param userDetails
   * @throws OauthException
   */
  public Map<String, Object> buildUserDetailsMap(String sessionId, String targetUrl, String sys,
      UserDetails userDetails) throws OauthException;

  /**
   * 从正常的url中截取出域名
   * 
   * @param url
   * @return
   * @throws OauthException
   */
  public String getDomain(String url) throws OauthException;
}
