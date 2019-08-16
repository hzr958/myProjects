package com.smate.center.oauth.service.login;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.oauth.cache.OauthCacheService;
import com.smate.center.oauth.exception.OauthException;
import com.smate.center.oauth.service.security.OauthUserDetailsService;
import com.smate.core.base.utils.constant.security.SecurityConstants;

/**
 * Oauth服务实现类
 *
 * @author wsn
 *
 */
@Service("oauthService")
@Transactional(rollbackFor = Exception.class)
public class OauthServiceImpl implements OauthService {

  protected final Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private OauthCacheService oauthCacheService;
  @Autowired
  private OauthUserDetailsService userDetailsService;

  @Override
  public Map<String, Object> buildUserDetailsMap(String sessionId, String targetUrl, String sys,
      UserDetails userDetails) throws OauthException {
    Object detailsObj = oauthCacheService.get(SecurityConstants.USER_DETAILS_INFO_CACHE, sessionId);
    Map<String, Object> detailsMap = null;
    if (detailsObj != null) {
      detailsMap = (Map<String, Object>) detailsObj;
    } else {
      detailsMap = new HashMap<String, Object>();
    }
    String domain = this.getDomain(targetUrl);
    if (StringUtils.isBlank(domain)) {
      logger.info("从跳转的目标页面获取域名为空，get domain from targetUrl is null");
    }
    Long insId = userDetailsService.findInsIdBySysAndDomain(sys, domain);
    detailsMap.remove(sys + "|" + insId);
    detailsMap.put(sys + "|" + insId, userDetails);
    return detailsMap;
  }

  @Override
  public String getDomain(String url) throws OauthException {
    if (StringUtils.isNotBlank(url)) {
      if (url.indexOf("http://") > -1) {
        url = url.replace("http://", "");
      }
      if (url.indexOf("https://") > -1) {
        url = url.replace("https://", "");
      }
      if (url.indexOf("/") > -1) {
        url = url.substring(0, url.indexOf("/"));
      }
    }
    return url;
  }

}
