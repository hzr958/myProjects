package com.smate.center.oauth.action.login;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.Map;
import java.util.Objects;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Actions;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;

import com.opensymphony.xwork2.ActionSupport;
import com.smate.center.oauth.cache.OauthCacheService;
import com.smate.center.oauth.service.mobile.MobileLoginService;
import com.smate.core.base.utils.cache.CacheConst;
import com.smate.core.base.utils.common.SystemBaseContants;
import com.smate.core.base.utils.constant.security.SecurityConstants;
import com.smate.core.base.utils.mobile.SmateMobileUtils;
import com.smate.core.base.utils.security.Des3Utils;
import com.smate.core.base.utils.security.TheadLocalInsId;
import com.smate.core.base.utils.security.TheadLocalNodeId;
import com.smate.core.base.utils.security.TheadLocalPsnId;
import com.smate.core.base.utils.security.TheadLocalRoleId;
import com.smate.core.base.utils.security.TheadLocalUnitId;
import com.smate.core.base.utils.service.security.AutoLoginOauthInfoService;
import com.smate.core.base.utils.string.ServiceUtil;
import com.smate.core.base.utils.struts2.Struts2Utils;

/**
 * 帐号密码验证 权限
 * 
 * @author tsz
 *
 */

@Results({@Result(name = "reload", location = "index", type = "redirect"),
    @Result(name = "logout", location = "/index.jsp")})
public class LogoutAction extends ActionSupport {

  private static final long serialVersionUID = -4233512169345586923L;
  protected final Logger logger = LoggerFactory.getLogger(getClass());
  private String sys = "SNS"; // 来源系统 给来 退出后定位系统用
  protected static final String OAUTH_LOGIN = "OAUTH_LOGIN";
  @Autowired
  private UserDetailsService userDetailsService;
  @Autowired
  private OauthCacheService oauthCacheService;
  @Autowired
  private MobileLoginService mobileLoginService;
  @Autowired
  private AutoLoginOauthInfoService autoLoginOauthInfoService;
  @Value("${domainoauth}")
  private String domainoauth;
  @Value("${domainscm}")
  private String domainScm;
  @Value("${domainbpo}")
  private String domainBpo;
  @Value("${domainnsfc}")
  private String domainNsfc;
  @Value("${domaingxrol}")
  private String domainGxrol;
  @Value("${domainzsrol}")
  private String domainZsrol;
  @Value("${domainstdrol}")
  private String domainHnrol;
  @Value("${domainexpert}")
  private String domainexpert;
  @Value("${domainMobile}")
  private String domainMobile;
  private String targetUrl; // 登出后跳转到的url

  /**
   * 帐号密码验证
   * 
   * @return
   * @throws Exception
   */
  @Actions({@Action("/oauth/logout")})
  public String logout() throws Exception {
    String wxOpenId = "";
    String wxUnionId = "";
    try {
      HttpSession session = Struts2Utils.getSession();
      HttpServletRequest request = Struts2Utils.getRequest();
      wxOpenId = (String) session.getAttribute("wxOpenId");
      wxUnionId = (String) session.getAttribute("wxUnionId");
      String sesseionId = session.getId();
      // 退出 清理缓存
      oauthCacheService.remove(SecurityConstants.USER_DETAILS_INFO_CACHE, sesseionId);
      oauthCacheService.remove(CacheConst.NEW_USER_DATA_CACHE, sesseionId);
      // 清空线程中的信息
      clearThreadInfo();
      // 清空 cookie自动登录信息
      Cookie aidCookie = Struts2Utils.getCookie(request, SecurityConstants.AUTO_LOGIN_PARAMETER_NAME);
      // 让aid失效
      if (aidCookie != null && StringUtils.isNotBlank(aidCookie.getValue())) {
        autoLoginOauthInfoService.invalidateAid(aidCookie.getValue());
      }
      setCookie(Struts2Utils.getResponse(), aidCookie, null, "0");
      Cookie loginCookie = Struts2Utils.getCookie(request, SecurityConstants.OAUTH_LOGIN);
      setCookie(Struts2Utils.getResponse(), loginCookie, null, "0");
      request.changeSessionId();
      session.invalidate();
      SecurityContextHolder.clearContext();
    } catch (Exception e) {
      logger.error("退出科研之友异常", e);
    }
    // 重定向到系统首页
    redirectToSysIndex(wxOpenId, wxUnionId);
    return NONE;

  }

  private void setCookie(HttpServletResponse response, Cookie cookie, String oauth, String maxAge) {
    if (cookie == null) {
      cookie = new Cookie(OAUTH_LOGIN, oauth);
    }
    cookie.setValue(oauth);
    cookie.setMaxAge(NumberUtils.toInt(maxAge, -1));
    cookie.setDomain(SystemBaseContants.IRIS_SNS_DOMAIN);
    cookie.setPath("/");
    response.addCookie(cookie);
  }

  /**
   * 微信解除绑定
   */
  @Action("/oauth/mobile/unbind")
  public String unbind() throws Exception {
    String sessionId = Struts2Utils.getRequest().getSession().getId();
    Object uDetails = oauthCacheService.get(SecurityConstants.USER_DETAILS_INFO_CACHE, sessionId);
    Map<String, Object> userDetails = (Map<String, Object>) uDetails;
    Long psnId = 0L;
    if (userDetails == null) {
      Struts2Utils.getResponse().sendRedirect(domainScm + "/oauth/logout?sys=" + sys);
      return null;
    }
    if (userDetails.get("userId") != null) {
      psnId = NumberUtils.toLong(userDetails.get("userId").toString());
    }
    if (psnId != null && psnId != 0) {
      boolean weChat = SmateMobileUtils.isWeChatBrowser(Struts2Utils.getRequest());
      if (weChat == true) {
        String wxOpenId = (String) Struts2Utils.getSession().getAttribute("wxOpenId");
        String wxUnionId = (String) Struts2Utils.getSession().getAttribute("wxUnionId");
        mobileLoginService.cancelBlind(psnId, wxUnionId);
        wxUnionId = wxUnionId == null ? "" : wxUnionId;
        if (StringUtils.isNotBlank(wxUnionId)) {
          oauthCacheService.remove(SecurityConstants.USER_DETAILS_INFO_CACHE, sessionId);
          oauthCacheService.remove(CacheConst.NEW_USER_DATA_CACHE, sessionId);
          Struts2Utils.getSession().invalidate();
          SecurityContextHolder.clearContext();
          // 清空线程中的信息
          clearThreadInfo();
          // 清空 cookie自动登录信息
          Cookie cookie = Struts2Utils.getCookie(Struts2Utils.getRequest(), "AID");
          setCookie(Struts2Utils.getResponse(), cookie, null, null);
          wxOpenId = Des3Utils.encodeToDes3(wxOpenId);
          String uri = StringUtils.isNotBlank(targetUrl) ? targetUrl
              : ServiceUtil.encodeToDes3(domainMobile + "/dynweb/mobile/dynshow");
          Struts2Utils.getResponse().sendRedirect(domainMobile + "/open/wechat/bind?des3WxOpenId=" + wxOpenId
              + "&des3WxUnionId=" + URLEncoder.encode(Des3Utils.encodeToDes3(wxUnionId), "utf-8") + "&service=" + uri);
        } else {
          Struts2Utils.getResponse().sendRedirect(domainScm + "/oauth/logout?sys=" + sys);
        }
      } else {
        Struts2Utils.getResponse().sendRedirect(domainScm + "/oauth/logout?sys=" + sys);
      }
    }
    return null;
  }

  /**
   * 移动端的退出功能
   * 
   * @return
   * @throws IOException
   */
  @Action("/oauth/mobile/logout")
  public String mobileLogout() throws IOException {
    Long psnId = 0L;
    try {
      // 在微信中退出
      if (SmateMobileUtils.isWeChatBrowser(Struts2Utils.getRequest())) {
        // 获取当前人员ID
        String sessionId = Struts2Utils.getRequest().getSession().getId();
        Object uDetails = oauthCacheService.get(SecurityConstants.USER_DETAILS_INFO_CACHE, sessionId);
        if (uDetails != null) {
          psnId = NumberUtils.toLong(((Map<String, Object>) uDetails).get("userId").toString());
          // 通过人员ID找到人员openId,再找到v_wechat_relation表中的关联关系进行删除，并在v_wechat_relation_his表中添加记录
          mobileLoginService.cancelBlind(psnId, null);
        }
      }
    } catch (Exception e) {
      logger.error("移动端退出操作失败, psnId = " + psnId, e);
    }
    // 跳转退出链接
    Struts2Utils.getResponse().sendRedirect(domainScm + "/oauth/logout?sys=mobile");
    return null;
  }


  // 清空线程中的信息
  private void clearThreadInfo() {
    TheadLocalPsnId.setPsnId(0L);
    TheadLocalNodeId.setCurrentUserNodeId(0);
    TheadLocalRoleId.setRoleId(0);
    TheadLocalUnitId.setUnitId(0L);
    TheadLocalInsId.setInsId(0L);
  }


  private String redirectToSysIndex(String wxOpenId, String wxUnionId) throws IOException {
    // 根据来源系统 定位到 来源系统的登录页面，默认重定向到 科研之友 登录页面
    if (StringUtils.isNotBlank(targetUrl)) {
      Struts2Utils.getResponse()
          .sendRedirect("/scmwebsns/redirect/wait?targetUrl=" + URLEncoder.encode(targetUrl, "utf-8"));
      return null;
    }
    if ("SIE".equals(sys) && StringUtils.isNotBlank(Struts2Utils.getParameter("sysDomain"))) {
      Struts2Utils.getResponse()
          .sendRedirect("http://" + Struts2Utils.getParameter("sysDomain") + "/scmwebrol/index?sys=" + sys);
    } else if ("SIE6".equals(sys) && StringUtils.isNotBlank(Struts2Utils.getParameter("sysDomain"))
        && StringUtils.isNotBlank(Struts2Utils.getParameter("uri"))) {
      Struts2Utils.getResponse().sendRedirect(
          "http://" + Struts2Utils.getParameter("sysDomain") + Struts2Utils.getParameter("uri") + "?sys=" + sys);
    } else if ("SIE6".equals(sys) && StringUtils.isNotBlank(Struts2Utils.getParameter("sysDomain"))) {
      Struts2Utils.getResponse()
          .sendRedirect("http://" + Struts2Utils.getParameter("sysDomain") + "/insweb/index?sys=" + sys);
    } else if ("SNS".equals(sys)) {
      String serverName = Struts2Utils.getRequest().getServerName();
      Struts2Utils.getResponse().sendRedirect("https://" + serverName + "/oauth/index?sys=" + sys);
    } else if ("WCI".equals(sys) && SmateMobileUtils.isWeChatBrowser(Struts2Utils.getRequest())) {
      if (StringUtils.isNotBlank(wxUnionId)) {
        Struts2Utils.getResponse()
            .sendRedirect(domainMobile + "/open/wechat/bind?des3WxOpenId="
                + URLEncoder.encode(Objects.toString(Des3Utils.encodeToDes3(wxOpenId), ""), "utf-8") + "&des3WxUnionId="
                + URLEncoder.encode(Des3Utils.encodeToDes3(wxUnionId), "utf-8"));
      } else {
        Struts2Utils.getResponse().sendRedirect(domainoauth + "/oauth/mobile/index?sys=" + sys);
      }
    } else if ("BPO".equals(sys)) {
      Struts2Utils.getResponse().sendRedirect(domainBpo + "/scmwebbpo/index?sys=" + sys);
    } else if ("GXROL".equals(sys)) {
      Struts2Utils.getResponse().sendRedirect(domainGxrol + "/scmwebrol/?sys=" + sys);
    } else if ("ZSROL".equals(sys)) {
      Struts2Utils.getResponse().sendRedirect(domainZsrol + "/zsrol/?sys=" + sys);
    } else if ("HNROL".equals(sys)) {
      Struts2Utils.getResponse().sendRedirect(domainHnrol + "/scmstdrol/?sys=" + sys);
    } else if ("EGTEXPERT".equals(sys)) {
      if (StringUtils.isNotBlank(Struts2Utils.getParameter("sysDomain"))) {
        Struts2Utils.getResponse()
            .sendRedirect("https://" + Struts2Utils.getParameter("sysDomain") + "/egtexpertweb/index?sys=" + sys);
      } else {
        Struts2Utils.getResponse().sendRedirect(domainexpert + "/egtexpertweb/?sys=" + sys);
      }
    } else {
      if (SmateMobileUtils.isWeChatBrowser(Struts2Utils.getRequest())) {
        Struts2Utils.getResponse().sendRedirect(domainoauth + "/dynweb/mobile/dynshow?sys=" + sys);
      } else {
        Struts2Utils.getResponse().sendRedirect(domainoauth + "/oauth/mobile/index?sys=" + sys);
      }
    }
    return null;
  }

  public String getSys() {
    return sys;
  }

  public void setSys(String sys) {
    this.sys = sys;
  }

  public String getTargetUrl() {
    return targetUrl;
  }

  public void setTargetUrl(String targetUrl) {
    this.targetUrl = targetUrl;
  }

}
