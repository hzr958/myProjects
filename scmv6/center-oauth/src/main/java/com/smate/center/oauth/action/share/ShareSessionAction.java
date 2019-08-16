package com.smate.center.oauth.action.share;

import java.io.IOException;
import java.io.Serializable;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import javax.servlet.http.HttpSession;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;

import com.opensymphony.xwork2.ActionSupport;
import com.smate.center.oauth.cache.OauthCacheService;
import com.smate.center.oauth.service.login.OauthLoginService;
import com.smate.center.oauth.service.login.OauthService;
import com.smate.center.oauth.service.mobile.MobileLoginService;
import com.smate.center.oauth.service.security.OauthUserDetailsService;
import com.smate.center.oauth.service.security.UserService;
import com.smate.core.base.utils.constant.security.SecurityConstants;
import com.smate.core.base.utils.mobile.SmateMobileUtils;
import com.smate.core.base.utils.number.NumberUtils;
import com.smate.core.base.utils.security.Des3Utils;
import com.smate.core.base.utils.struts2.Struts2Utils;
import com.smate.core.base.utils.wechat.OAuth2Service;
import com.smate.core.base.utils.wechat.WeChatRelationService;

/**
 * 登陆后，重定向到新系统
 * 
 * @author zhuangyanming
 * @version 6.0.1
 * @since 6.0.1
 * 
 */
@Results({@Result(name = "shareerror", location = "/common/500.jsp")})
public class ShareSessionAction extends ActionSupport {

  /**
   * 
   */
  private static final long serialVersionUID = 3398563602974329092L;
  protected final Logger logger = LoggerFactory.getLogger(getClass());
  public static final String SPRING_SECURITY_CONTEXT_KEY = "SPRING_SECURITY_CONTEXT";
  @Autowired
  private OauthCacheService oauthCacheService;
  @Autowired
  private OauthService oauthService;
  @Autowired
  private OAuth2Service oAuth2Service;
  @Autowired
  private OauthLoginService oauthLoginService;
  @Autowired
  private WeChatRelationService weChatRelationService;

  private String sys; // 来源系统

  private String sysDomain; // 来源系统的域名

  private Long insId; // 机构ID

  private String SYSSID; // 来源系统sessionId

  private String des3PsnId; // 加密的人员ID

  private Integer roleId; // 人员角色ID

  private String service; // 目标页面 (已加密)
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
  @Autowired
  private OauthUserDetailsService userDetailsService;
  @Autowired
  private UserService userService;
  @Value("${domainrol}")
  private String domainrol;
  @Value("${domainMobile}")
  private String domainMobile;
  private String JSID;
  @Autowired
  private MobileLoginService mobileLoginService;

  @Action("/oauth/share")
  public String shareSession() throws Exception {
    try {
      // v6url新系统跳转标记
      // 构造 需要重定向目标页面，
      // 判断有没有权限
      // 如果没有权限就去登录页面 并重登录页面重定向目标页面
      String sessionId = Struts2Utils.getRequest().getSession().getId();
      Object uDetails = oauthCacheService.get(SecurityConstants.USER_DETAILS_INFO_CACHE, sessionId);
      if (uDetails == null) {
        // 判断 来源系统 看要重定向到 哪个系统的登录页面 sys (默认进入 权限系统登录页面)
        if ("SNS".equals(sys)) {
          /*
           * Struts2Utils.getResponse().sendRedirect(domainScm + "/scmwebsns/index?service=" + service);
           */
          /*
           * 来源系统是SNS的话，登录后还是进入SNS，所以这里不要进行域名重定向，只进行请求路径重定向。 这样有助于做负载均衡。
           */
          Struts2Utils.getResponse().sendRedirect("/oauth/index?sys=SNS&service=" + service);
        } else if ("SIE".equals(sys)) {
          if (StringUtils.isNotBlank(sysDomain)) {
            if (StringUtils.isNotBlank(domainrol) && domainrol.equals(sysDomain)) {
              Struts2Utils.getResponse().sendRedirect(domainrol + "/scmwebrol/commonIndex/index?service=" + service);
            } else {
              Struts2Utils.getResponse().sendRedirect(sysDomain + "/scmwebrol/index?service=" + service);
            }
          } else {
            logger.error("获取SIE系统域名为空");
          }
        } else if ("SIE6".equals(sys)) {
          Struts2Utils.getResponse().sendRedirect(sysDomain + "/insweb/index?service=" + service);
        } else if ("BPO".equals(sys)) {
          Struts2Utils.getResponse().sendRedirect(domainBpo + "/scmwebbpo/index?service=" + service);
        } else if ("GXROL".equals(sys)) {
          Struts2Utils.getResponse().sendRedirect(domainGxrol + "/scmwebrol/index?service=" + service);
        } else if ("ZSROL".equals(sys)) {
          Struts2Utils.getResponse().sendRedirect(domainZsrol + "/zsrol/?service=" + service);
        } else if ("HNROL".equals(sys)) {
          Struts2Utils.getResponse().sendRedirect(domainHnrol + "/scmstdrol/?service=" + service);
        } else if ("EGTEXPERT".equals(sys)) {
          Struts2Utils.getResponse().sendRedirect(domainexpert + "/egtexpertweb/?service=" + service);
        } else {
          Struts2Utils.getResponse()
              .sendRedirect(domainoauth + "/oauth/mobile/index?service=" + service + "&sys=" + sys);
        }
        return null;
      } else {
        Map<String, Object> userDetails = (Map<String, Object>) uDetails;
        Long userId = 0L;
        if (userDetails.get("userId") != null) {
          userId = NumberUtils.toLong(userDetails.get("userId").toString());
        }
        uDetails = userDetailsService.loadUserFromSys(
            userId + "|" + (insId == null ? 0L : insId) + "|" + (roleId == null ? "-1" : roleId), sys,
            Des3Utils.decodeFromDes3(service));
        logger.info("-----------------------------------共享获取的权限为--" + ((UserDetails) uDetails).getAuthorities());
      }

      logger.info("-----------------------------------当前系统sessionId为---" + sessionId);
      logger.debug("有人来共享session了==============================================");
      String targetUrl = oauthLoginService.oauthRebuildTargetUrl(service);
      Map<String, Object> detailsMap =
          oauthService.buildUserDetailsMap(sessionId, targetUrl, sys, (UserDetails) uDetails);
      oauthCacheService.put(SecurityConstants.USER_DETAILS_INFO_CACHE, SecurityConstants.USER_DETAILS_CACHE_TIME,
          sessionId, (Serializable) detailsMap);
      Struts2Utils.getResponse().sendRedirect(targetUrl.toString());
      logger.debug("已经重定向到目标页面了==============================================");
    } catch (Exception e) {
      logger.error("共享权限出错", e);
      return "shareerror";
    }
    return null;
  }

  /**
   * 移动端没有权限时的处理
   * 
   * @return
   */
  @Deprecated
  @Action("/oauth/mobile/share")
  public String mobileShareSession() {
    String isWeChatRequest = Struts2Utils.getParameter("isWeChatRequest");
    String AID = "";
    String targetUrl = domainMobile + "/oauth/mobile/index"; // 移动端没有权限默认跳到移动端登录页面
    String url = Struts2Utils.getRequest().getRequestURL().toString();
    try {
      // 是否是微信端请求
      if ("true".equals(isWeChatRequest)) {
        Map<String, Object> result = new HashMap<String, Object>();
        // 微信端请求用微信端code或wxopenId获取AID
        String wxOpenId = (String) Struts2Utils.getSession().getAttribute("wxOpenId");
        String wxUnionId = (String) Struts2Utils.getSession().getAttribute("wxUnionId");
        // 顺序 ： unionId - openId - code
        if (StringUtils.isNotBlank(wxUnionId)) {
          result = mobileLoginService.buildAIDByWxUnionId(wxUnionId);
        }
        // 微信unionId找不到对应的科研之友人员，用openId获取
        /*
         * if (result != null && result.containsKey("msg")) { if (StringUtils.isNotBlank(wxOpenId)) { result
         * = mobileLoginService.buildAIDByWxOpenId(wxOpenId); } }
         */
        if (StringUtils.isBlank(AID)) {
          String wxCode = Struts2Utils.getParameter("code");
          result = mobileLoginService.buildAIDByWxCode(wxCode);
        }
        if (result != null) {
          if (result.containsKey("msg")) {
            wxUnionId = (String) Struts2Utils.getSession().getAttribute("wxUnionId");
            if (StringUtils.isNotBlank(wxUnionId)) {
              // msg有值，则表示用微信openId找不到对应的科研之友人员，所以重定向到绑定页面
              targetUrl = domainMobile + "/open/wechat/bind?des3WxOpenId="
                  + URLEncoder.encode(result.get("des3WxOpenId").toString(), "utf-8");
              targetUrl += "&des3WxUnionId=" + URLEncoder.encode(Des3Utils.encodeToDes3(wxUnionId), "utf-8");
              targetUrl += "&service=" + service;
            }
          } else {
            // 通过AID自动登录到对应的页面
            if (result.containsKey("AID") && StringUtils.isNotBlank(result.get("AID").toString())) {
              AID = result.get("AID").toString();
              // 微信里面缓存sessionId,取消关注时清空权限用
              wxOpenId = (String) Struts2Utils.getSession().getAttribute("wxOpenId");
              if (StringUtils.isNotBlank(wxOpenId)) {
                oauthCacheService.put(SecurityConstants.CACHE_SESSIONID, SecurityConstants.USER_DETAILS_CACHE_TIME,
                    wxOpenId, Struts2Utils.getSession().getId());
              }
              String defaultUrl = domainMobile + "/psnweb/mobile/homepage";
              targetUrl = this.rebuildTargetUrl(Struts2Utils.getParameter("service"), defaultUrl);
              if (targetUrl.indexOf("?") > 0) {
                targetUrl += "&AID=" + AID;
              } else {
                targetUrl += "?AID=" + AID;
              }
            }
          }
        }
      } else {
        targetUrl += "?service=" + Struts2Utils.getParameter("service");
      }
      Struts2Utils.getResponse().sendRedirect(targetUrl);
      return null;
    } catch (Exception e) {
      logger.error("移动端没有权限时构造AID出错， isWeChatRequest=" + isWeChatRequest + ", targetUrl=" + targetUrl, e);
    }
    return null;
  }

  /**
   * 移动端没有权限时的处理
   * 
   * @return
   * @throws IOException
   */
  @Action("/oauth/mobile/newshare")
  public String mobileShareSessionNew() throws IOException {
    String isWeChatRequest = Struts2Utils.getParameter("isWeChatRequest");
    String targetUrl = domainMobile + "/oauth/mobile/index"; // 移动端没有权限默认跳到移动端登录页面
    String service = Struts2Utils.getParameter("service");
    try {
      // 是否是微信端请求
      if (SmateMobileUtils.isWeChatBrowser(Struts2Utils.getRequest())) {
        Map<String, Object> result = new HashMap<String, Object>();
        String wxCode = Struts2Utils.getParameter("code");
        if (StringUtils.isNotBlank(wxCode)) {
          result = mobileLoginService.buildAIDByWxCode(wxCode);
        }
        // 没有取到AID的，说明没有绑定或取的有问题，退出原先登录的账号
        targetUrl = MapUtils.isNotEmpty(result) ? rebuildTargetUrlByResult(result, targetUrl, service)
            : domainMobile + "/oauth/logout?sys=WCI";
      } else {
        targetUrl += "?service=" + service;
        logger.info("进入newshareAction，但不是微信端请求");
      }
    } catch (Exception e) {
      logger.error("移动端没有权限时构造AID出错， isWeChatRequest=" + isWeChatRequest + ", targetUrl=" + targetUrl, e);
    }
    Struts2Utils.getResponse().sendRedirect(targetUrl);
    return null;
  }

  public String getSys() {
    return sys;
  }

  public void setSys(String sys) {
    this.sys = sys;
  }

  public String getService() {
    return service;
  }

  public void setService(String service) {
    this.service = service;
  }

  public String getSysDomain() {
    return sysDomain;
  }

  public void setSysDomain(String sysDomain) {
    this.sysDomain = sysDomain;
  }

  public String getSYSSID() {
    return SYSSID;
  }

  public void setSYSSID(String sYSSID) {
    SYSSID = sYSSID;
  }

  public Long getInsId() {
    return insId;
  }

  public void setInsId(Long insId) {
    this.insId = insId;
  }

  public String getDes3PsnId() {
    return des3PsnId;
  }

  public void setDes3PsnId(String des3PsnId) {
    this.des3PsnId = des3PsnId;
  }

  /**
   * 比较两个Long类型的值是否相等
   * 
   * @param x
   * @param y
   * @return
   */
  private boolean compareLongValue(Long x, Long y) {
    boolean result = false;
    if (x == null) {
      x = 0L;
    }
    if (y == null) {
      y = 0L;
    }
    if (x.longValue() == y.longValue()) {
      result = true;
    }
    return result;
  }

  private boolean compareIntegerValue(Integer x, Integer y) {
    boolean result = false;
    if (x == null) {
      x = 0;
    }
    if (y == null) {
      y = 0;
    }
    if (x.intValue() == y.intValue()) {
      result = true;
    }
    return result;
  }

  public Integer getRoleId() {
    return roleId;
  }

  public void setRoleId(Integer roleId) {
    this.roleId = roleId;
  }

  public String getJSID() {
    return JSID;
  }

  public void setJSID(String jSID) {
    JSID = jSID;
  }

  private void cacheUserDetails(String sessionId, String targetUrl, String sys, UserDetails userDetails) {
    Object detailsObj = oauthCacheService.get(SecurityConstants.USER_DETAILS_INFO_CACHE, sessionId);
    Map<String, Object> detailsMap = null;
    if (detailsObj != null) {
      detailsMap = (Map<String, Object>) detailsObj;
    } else {
      detailsMap = new HashMap<String, Object>();
    }
    String domain = this.getDomain(targetUrl);
    if (StringUtils.isBlank(domain)) {
      logger.info("从跳转的目标页面获取域名为空，get domain is null");
    }
    Long insId = userDetailsService.findInsIdBySysAndDomain(sys, domain);
    detailsMap.remove(sys + "|" + insId);
    detailsMap.put(sys + "|" + insId, userDetails);
  }

  /**
   * 从请求的url获取域名
   * 
   * @param url
   * @return
   */
  private String getDomain(String url) {
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

  /**
   * 解密目标url
   * 
   * @param encodeUrl 已加密的目标页面url
   * @param defaultUrl 目标页面url为空时默认的url
   * @return
   */
  private String rebuildTargetUrl(String encodeUrl, String defaultUrl) {
    StringBuilder targetUrl = new StringBuilder();
    if (encodeUrl == null || "".equals(encodeUrl)) {
      targetUrl.append(defaultUrl);
    } else {
      targetUrl.append(Des3Utils.decodeFromDes3(encodeUrl));
    }
    // 如果解密失败，可能是目标页面未加密
    if ("null".equals(targetUrl.toString())) {
      targetUrl = new StringBuilder();
      targetUrl.append(encodeUrl);
    }
    return targetUrl.toString();
  }


  /**
   * 在通过微信回调后的code参数构建AID等参数后，构建跳转目标地址
   * 
   * @param result
   * @param targetUrl 下一步跳转的目标地址
   * @param service 最终要跳转的地址
   * @return
   * @throws Exception
   */
  private String rebuildTargetUrlByResult(Map<String, Object> result, String targetUrl, String service)
      throws Exception {
    HttpSession session = Struts2Utils.getSession();
    String AID = Objects.toString(result.get("AID"), "");
    String des3WxUnionId = Objects.toString(result.get("des3WxUnionId"), "");
    String des3WxOpenId = Objects.toString(result.get("des3WxOpenId"));
    if (StringUtils.isNotBlank(AID)) {
      // AID不为空则利用AID自动登录
      String defaultUrl = domainMobile + "/psnweb/mobile/homepage";
      targetUrl = this.rebuildTargetUrl(service, defaultUrl);
      targetUrl += (targetUrl.indexOf("?") > 0 ? "&AID=" : "?AID=") + AID;
      logger.info("获取到了AID,获取的加密微信unionId为：" + des3WxUnionId);
    } else if (StringUtils.isNotBlank(des3WxUnionId)) {
      // 有获取到微信unionId, 若有绑定记录，跳转登录页面，若无绑定记录，跳转绑定页面(跳解绑链接，清理缓存的权限信息)
      Long smateOpenId = weChatRelationService.findSmateOpenIdByWxUnionId(Des3Utils.decodeFromDes3(des3WxUnionId));
      if (NumberUtils.isNotNullOrZero(smateOpenId)) {
        targetUrl = domainMobile + "/oauth/logout?sys=WCI";
      } else {
        session.setAttribute("wxOpenId", Des3Utils.decodeFromDes3(des3WxOpenId));
        session.setAttribute("wxUnionId", Des3Utils.decodeFromDes3(des3WxUnionId));
        targetUrl = domainMobile + "/oauth/mobile/unbind?sys=WCI&targetUrl=" + service;
      }
      logger.info("未获取到了AID,获取的加密微信unionId为：{}, smateOpenId={}", des3WxUnionId, smateOpenId);
    }
    return targetUrl;
  }

}
