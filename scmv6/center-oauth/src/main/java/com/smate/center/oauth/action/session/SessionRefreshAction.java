package com.smate.center.oauth.action.session;

import java.io.Serializable;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.math.NumberUtils;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Preparable;
import com.smate.center.oauth.cache.OauthCacheService;
import com.smate.center.oauth.form.SessionRefreshForm;
import com.smate.core.base.utils.cache.CacheConst;
import com.smate.core.base.utils.common.SystemBaseContants;
import com.smate.core.base.utils.constant.security.SecurityConstants;
import com.smate.core.base.utils.security.TheadLocalInsId;
import com.smate.core.base.utils.security.TheadLocalNodeId;
import com.smate.core.base.utils.security.TheadLocalPsnId;
import com.smate.core.base.utils.security.TheadLocalRoleId;
import com.smate.core.base.utils.security.TheadLocalUnitId;
import com.smate.core.base.utils.struts2.Struts2Utils;

/**
 * 前端页面定时发送请求，重新缓存权限信息 延长权限缓存时间且保持session有效时间
 *
 * @author wsn
 * @createTime 2017年6月2日 下午4:19:47
 *
 */
@Results({@Result(name = "settings", location = "/WEB-INF/jsp/session_setting.jsp")})
public class SessionRefreshAction extends ActionSupport implements Preparable, ModelDriven<SessionRefreshForm> {

  protected final Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private OauthCacheService oauthCacheService;
  private SessionRefreshForm form;

  @Override
  public void prepare() throws Exception {
    if (form == null) {
      form = new SessionRefreshForm();
    }
  }

  @Action("/oauth/session/ajaxrefresh")
  public String refreshSession() {
    String sessionId = Struts2Utils.getRequest().getSession().getId();
    Object uDetails = oauthCacheService.get(SecurityConstants.USER_DETAILS_INFO_CACHE, sessionId);
    String resultJson = "{\"result\":\"success\"}";
    if (uDetails != null) {
      oauthCacheService.remove(SecurityConstants.USER_DETAILS_INFO_CACHE, sessionId);
      oauthCacheService.put(SecurityConstants.USER_DETAILS_INFO_CACHE, SecurityConstants.USER_DETAILS_CACHE_TIME,
          sessionId, (Serializable) uDetails);
    } else {
      resultJson = "{\"result\":\"cacheNull\"}";
    }
    Struts2Utils.renderJson(resultJson, "encoding:UTF-8");
    return null;
  }

  /**
   * 操作一些会话和权限等相关的东西，方便测试那边测试用
   * 
   * @return
   */

  @Action("/oauth/session/ajaxoperate")
  public String resetSessionInfo() {
    String resultJson = "{\"result\":\"success\"}";
    try {
      String sesseionId = Struts2Utils.getSession().getId();
      // 删除缓存的权限
      if (form.getDeleteCache() == 1) {
        oauthCacheService.remove(SecurityConstants.USER_DETAILS_INFO_CACHE, sesseionId);
        oauthCacheService.remove(CacheConst.NEW_USER_DATA_CACHE, sesseionId);
      }
      // 删除资助机构左侧基金统计数
      if (form.getDeleteFundCache() == 1) {
        Long[] regionIds = {156L, 360000L, 370000L, 410000L, 420000L, 430000L, 440000L, 450000L, 460000L, 500000L,
            510000L, 520000L, 530000L, 540000L, 610000L, 620000L, 630000L, 640000L, 650000L, 158L, 344L, 446L, 110000L,
            120000L, 130000L, 140000L, 150000L, 210000L, 220000L, 230000L, 310000L, 320000L, 330000L, 340000L, 350000L,
            156L};
        for (Long regionId : regionIds) {
          oauthCacheService.remove("fund_regionCount_cache", regionId.toString());
        }
      }
      // session会话失效
      if (form.getSessionInvalidate() == 1) {
        Struts2Utils.getSession().setMaxInactiveInterval(1);
        Struts2Utils.getSession().invalidate();
      }
      // 清空Spring Security中的权限信息
      if (form.getClearContext() == 1) {
        SecurityContextHolder.clearContext();
      }
      // 清空线程变量中信息
      if (form.getClearThreadInfo() == 1) {
        TheadLocalPsnId.setPsnId(0L);
        TheadLocalNodeId.setCurrentUserNodeId(0);
        TheadLocalRoleId.setRoleId(0);
        TheadLocalUnitId.setUnitId(0L);
        TheadLocalInsId.setInsId(0L);
      }
      // 清空cookie中的AID信息
      if (form.getDeleteCookieAID() == 1) {
        Cookie cookie = Struts2Utils.getCookie(Struts2Utils.getRequest(), SecurityConstants.AUTO_LOGIN_PARAMETER_NAME);
        setCookie(Struts2Utils.getResponse(), cookie, null, null);
      }
      // 清空cookie中的Oauth_login信息
      if (form.getDeleteCookieOauthLogin() == 1) {
        Cookie loginCookie = Struts2Utils.getCookie(Struts2Utils.getRequest(), SecurityConstants.OAUTH_LOGIN);
        setCookie(Struts2Utils.getResponse(), loginCookie, null, null);
      }

    } catch (Exception e) {
      logger.error("会话或权限相关的操作失败", e);
      resultJson = "{\"result\":\"error\"}";
    }
    Struts2Utils.renderJson(resultJson, "encoding:UTF-8");
    return null;
  }

  /**
   * 进入设置页面
   * 
   * @return
   */
  @Action("/oauth/session/setting")
  public String showSessionInfoSettings() {
    return "settings";
  }

  /**
   * 会话超时后重新构建会话权限信息
   * 
   * @return
   */
  @Action("/oauth/security/ajaxrebuild")
  public String rebuildSessionSecurity() {
    try {
      String sessionId = Struts2Utils.getRequest().getSession().getId();
      Object uDetails = oauthCacheService.get(SecurityConstants.USER_DETAILS_INFO_CACHE, sessionId);
      String resultJson = "{\"result\":\"success\"}";
      // 缓存的权限不为空，从缓存中取出对应的系统权限，放到Spring Security中---------在请求
      if (uDetails != null) {
        oauthCacheService.remove(SecurityConstants.USER_DETAILS_INFO_CACHE, sessionId);
        oauthCacheService.put(SecurityConstants.USER_DETAILS_INFO_CACHE, SecurityConstants.USER_DETAILS_CACHE_TIME,
            sessionId, (Serializable) uDetails);
      } else {
        // 判断cookie中是否有有效的AID参数，有的话，通过AID参数构建权限信息放到spring security中
      }

    } catch (Exception e) {
      logger.error("会话超时后重新构建会话权限出错", e);
    }
    return null;
  }

  @Override
  public SessionRefreshForm getModel() {
    return form;
  }

  private void setCookie(HttpServletResponse response, Cookie cookie, String oauth, String maxAge) {
    if (cookie == null) {
      cookie = new Cookie(SecurityConstants.OAUTH_LOGIN, oauth);
    }
    cookie.setValue(oauth);
    cookie.setMaxAge(NumberUtils.toInt(maxAge, -1));
    cookie.setDomain(SystemBaseContants.IRIS_SNS_DOMAIN);
    cookie.setPath("/");

    response.addCookie(cookie);

  }

}
