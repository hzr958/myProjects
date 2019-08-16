package com.smate.core.web.sys.security.authority;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.util.WebUtils;

import com.smate.core.base.psn.dao.AccountEmailCheckLogDao;
import com.smate.core.base.utils.constant.security.SecurityConstants;
import com.smate.core.base.utils.mobile.SmateMobileUtils;
import com.smate.core.base.utils.model.cas.security.SysUserLogin;
import com.smate.core.base.utils.security.Des3Utils;
import com.smate.core.base.utils.service.security.AutoLoginOauthInfoService;
import com.smate.core.base.utils.service.security.SysUserLoginService;
import com.smate.core.web.sys.security.cache.UserCacheService;

/**
 * 自动登录参数拦截(自动登录参数名字 统一 AID)
 * 
 * @author tsz
 *
 */
public class AutoLoginAuthFilter implements Filter {

  private final Logger logger = LoggerFactory.getLogger(getClass());

  private UserCacheService userCacheService;
  private AccountEmailCheckLogDao accountEmailCheckLogDao;

  private SysUserLoginService sysUserLoginService;
  private AutoLoginOauthInfoService autoLoginOauthInfoService;
  @Value("${domainscm}")
  private String domainscm;
  @Value("${domainoauth}")
  private String domainoauth;
  @Value("${domainMobile}")
  private String domainMobile;

  /**
   * Default constructor.
   */
  public AutoLoginAuthFilter() {

  }

  /**
   * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain) 1.判断有没有这个参数 2.判断参数有没有过期3.
   *      有这个参数 重定向 到 oauth系统 /oauth/autologin 4.没有这个参数或参数过期 直接 chain.doFilter
   */
  public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
      throws IOException, ServletException {

    HttpServletRequest request = (HttpServletRequest) req;
    HttpServletResponse response = (HttpServletResponse) res;

    // 判断有没有这个参数

    // 需要注意 记住登录 也是用这个实现 所以 需要判断 如果 当前没有系统 没有权限 就是缓存中取不出权限 就从cookie中取出来
    // 是否 会跟权限拦截 有冲突的地方(需要考虑)
    // cookie的 是否另外写器

    try {
      String AID = request.getParameter(SecurityConstants.AUTO_LOGIN_PARAMETER_NAME);
      if (AID != null && !"".equals(AID)) {
        // 判断参数有没有过期
        String autoinfo = autoLoginOauthInfoService.checkAutoLoginOauth(AID);
        String host = request.getParameter("HOST");
        String targetUrl = this.rebuildTargetUrl(request, host);
        String sys = this.judgeFromSys(request, host);
        if (StringUtils.isNotBlank(autoinfo)) {
          userCacheService.put(SecurityConstants.AUTO_LOGIN_INFO_CACHE, 2 * 60, AID, autoinfo);
          // =============20170714_lhd_SCM-11425_(目前只用于确认成果邮件,bpologin自动登录到首页上)===========start
          // 1.判断请求的链接中是否包含resetpwd=true,或者bpologin=true
          if (StringUtils.isNotBlank(request.getQueryString())) {
            Long psnId = null;
            String userId = autoinfo.substring(0, autoinfo.indexOf(","));
            if (NumberUtils.isNumber(userId)) {
              psnId = Long.valueOf(userId);
            }
            // 2.第1步成立,则查询sys_user_login表,判断该用户最后一次登录时间或最后一次修改密码时间是否小于6个月
            SysUserLogin sysUserLogin = sysUserLoginService.getSysUserLogin(psnId);
            Long logintime = 0l;
            Long pwdchange = 0l;
            Long newdate = new Date().getTime();
            Long time = 6 * 30 * 24 * 60 * 60 * 1000l;
            if (sysUserLogin != null) {
              if (sysUserLogin.getLastLoginTime() != null) {
                logintime = sysUserLogin.getLastLoginTime().getTime();
              }
              if (sysUserLogin.getLastPwdChanged() != null) {
                pwdchange = sysUserLogin.getLastPwdChanged().getTime();
              }
            }
            // 确认成果邮件
            if (request.getQueryString().contains("resetpwd=true")) {
              // 链接是从邮件中跳转过来的，要确认邮件
              String from = request.getParameter("from");
              String userName = sysUserLoginService.getLoginNameByPsnId(psnId);
              if ("email".equals(from)) {
                confirmPsnEmail(request, psnId);
                targetUrl = targetUrl + "&username=" + userName;
              }
              if (sysUserLogin != null) {
                if (newdate - logintime < time || newdate - pwdchange < time) {
                  // 2.第2步成立,则重定向到登录页面,登录后跳转到对应页面
                  response.sendRedirect(domainscm + "/oauth/index?service="
                      + URLEncoder.encode(Des3Utils.encodeToDes3(targetUrl), "utf-8") + "&sys=SNS");
                  return;
                } else {
                  // 3.第2步不成立,则自动登录,targetUrl后加个参数,用于js判断弹出重置密码框
                  targetUrl = targetUrl + "needresetpwd=true&username=" + userName;
                }
              } else {
                // SysUserLogin表无记录时直接弹出重置密码框
                if (targetUrl.indexOf("&username=") == -1) {
                  targetUrl = targetUrl + "needresetpwd=true&username=" + userName;
                }
              }
            }
          }
          // =============20170714_lhd_SCM-11425_(目前只用于确认成果邮件链接上)===========end
          // 手动拼接重定向页面
          response.sendRedirect(
              domainoauth + "/oauth/autologin?service=" + URLEncoder.encode(Des3Utils.encodeToDes3(targetUrl), "utf-8")
                  + "&" + SecurityConstants.AUTO_LOGIN_PARAMETER_NAME + "=" + AID + "&sys=" + sys);
          return;
        } else {
          // 将登陆标识改为false
          Cookie autoLoginCookie = WebUtils.getCookie(request, SecurityConstants.AUTO_LOGIN_PARAMETER_NAME);
          this.setCookie(response, autoLoginCookie, "false", "0");
          response.sendRedirect(targetUrl);
          return;
        }
      }
    } catch (Exception e) {
      logger.error("拦截 自动登录参数出错", e);
      throw new ServletException(e.toString());
    }
    // 没有这个参数 或参数过期 直接 chain.doFilter
    chain.doFilter(request, response);
  }

  private void confirmPsnEmail(HttpServletRequest request, Long psnId) {
    String email = request.getParameter("email");
    email = Des3Utils.decodeFromDes3(email);
    if (StringUtils.isNotBlank(email)) {
      accountEmailCheckLogDao.confirmPsnEmail(psnId, email);
    }
  }

  @Override
  public void destroy() {

  }

  @Override
  public void init(FilterConfig arg0) throws ServletException {

  }

  public AutoLoginOauthInfoService getAutoLoginOauthInfoService() {
    return autoLoginOauthInfoService;
  }

  public void setAutoLoginOauthInfoService(AutoLoginOauthInfoService autoLoginOauthInfoService) {
    this.autoLoginOauthInfoService = autoLoginOauthInfoService;
  }

  public SysUserLoginService getSysUserLoginService() {
    return sysUserLoginService;
  }

  public void setSysUserLoginService(SysUserLoginService sysUserLoginService) {
    this.sysUserLoginService = sysUserLoginService;
  }

  /**
   * 处理参数，去掉AID
   * 
   * @param request
   * @return
   */
  private String handleAIDParams(HttpServletRequest request) {
    String paramsStr = request.getQueryString();
    StringBuffer paramsResult = new StringBuffer();
    if (StringUtils.isNotBlank(paramsStr)) {
      String[] paramsArray = paramsStr.split("&");
      List<String> restParams = new ArrayList<String>();
      for (String param : paramsArray) {
        if (StringUtils.isNotBlank(param)) {
          String[] paramArray = param.split("[=]");
          if (SecurityConstants.AUTO_LOGIN_PARAMETER_NAME.equals(paramArray[0])) {
            continue;
          }
          if (SecurityConstants.AUTO_LOGIN_PARAMETER_NAME_HOST.equals(paramArray[0])) {
            continue;
          }
          restParams.add(param);
        }
      }
      // 原先的通过=号分割参数的可能会碰到加密参数值中有等号的
      if (CollectionUtils.isNotEmpty(restParams)) {
        for (int i = 0; i < restParams.size(); i++) {
          if (StringUtils.isNotBlank(restParams.get(i))) {
            paramsResult.append(restParams.get(i));
            if (i < restParams.size() - 1) {
              paramsResult.append("&");
            }
          }
        }
      }
    }
    return paramsResult.toString();
  }

  /**
   * 
   * 
   * @param response
   * @param cookie
   * @param AID
   * @param maxAge
   * @param domain
   */
  private void setCookie(HttpServletResponse response, Cookie cookie, String AID, String maxAge) {
    if (cookie == null) {
      cookie = new Cookie(SecurityConstants.AUTO_LOGIN_PARAMETER_NAME, AID);
    }
    cookie.setValue(AID);
    cookie.setMaxAge(NumberUtils.toInt(maxAge, -1));
    cookie.setDomain(".scholarmate.com");
    cookie.setPath("/");
    response.addCookie(cookie);

  }

  /**
   * 重新构建目标地址
   * 
   * @param request
   * @return
   */
  private String rebuildTargetUrl(HttpServletRequest request, String host) {
    // 有这个参数 并且没有过期 重定向 到 oauth系统 /oauth/autologin
    String targetAction = request.getServletPath();
    String targetUrl = "";
    String sys = "SNS";
    if (!"".equals(host) && StringUtils.isNotBlank(host)) {
      String sie6Url = "http://" + host;
      targetUrl = sie6Url + targetAction;
      sys = "SIE6";
    } else {
      targetUrl = domainscm + targetAction;
      if (SmateMobileUtils.isMobileBrowser(request)) {
        targetUrl = domainMobile + targetAction;
      }
    }
    // 添加微信的openId和url，url为绑定后跳转到的微信端界面
    targetUrl +=
        (StringUtils.isNotBlank(request.getParameter("wxOpenId")) ? ("?wxOpenId=" + request.getParameter("wxOpenId"))
            : "")
            + ((StringUtils.isNotBlank(request.getParameter("wxOpenId"))
                && StringUtils.isNotBlank(request.getParameter("wxUrl"))) ? ("&wxUrl=" + request.getParameter("wxUrl"))
                    : "");
    if (targetUrl.indexOf("?") > -1) {
      targetUrl += "&" + handleAIDParams(request);
    } else {
      targetUrl += "?" + handleAIDParams(request);
    }
    return targetUrl;
  }

  // 判断系统来源
  private String judgeFromSys(HttpServletRequest request, String host) {
    String sys = "SNS";
    if (!"".equals(host) && StringUtils.isNotBlank(host)) {
      sys = "SIE6";
    }
    return sys;
  }

  public void setUserCacheService(UserCacheService userCacheService) {
    this.userCacheService = userCacheService;
  }

  public void setAccountEmailCheckLogDao(AccountEmailCheckLogDao accountEmailCheckLogDao) {
    this.accountEmailCheckLogDao = accountEmailCheckLogDao;
  }
}
