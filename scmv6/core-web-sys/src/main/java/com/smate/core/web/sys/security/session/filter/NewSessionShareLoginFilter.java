package com.smate.core.web.sys.security.session.filter;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationTrustResolver;
import org.springframework.security.authentication.AuthenticationTrustResolverImpl;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.util.ThrowableAnalyzer;
import org.springframework.security.web.util.ThrowableCauseExtractor;
import org.springframework.web.filter.GenericFilterBean;

import com.smate.core.base.utils.mobile.SmateMobileUtils;
import com.smate.core.base.utils.string.ServiceUtil;
import com.smate.core.base.utils.wechat.MessageUtil;

/**
 * 新系统权限控制，用户登录重定向到旧系统
 * 
 * @author zym
 * @version 6.0.1
 * @since 6.0.1
 */
public class NewSessionShareLoginFilter extends GenericFilterBean {

  @Value("${domainscm}")
  private String domainscm;
  @Value("${domainoauth}")
  private String domainoauth;
  @Value("${domainMobile}")
  private String domainMobile;
  @Value("${wechat.appid}")
  private String appId;

  // ~ Instance fields
  // ================================================================================================
  private AuthenticationTrustResolver authenticationTrustResolver = new AuthenticationTrustResolverImpl();
  private ThrowableAnalyzer throwableAnalyzer = new DefaultThrowableAnalyzer();

  // ~ Methods
  // ========================================================================================================

  public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
      throws IOException, ServletException {
    HttpServletRequest request = (HttpServletRequest) req;
    HttpServletResponse response = (HttpServletResponse) res;
    try {
      chain.doFilter(request, response);
      logger.debug("Chain processed normally");
    } catch (IOException ex) {
      throw ex;
    } catch (Exception ex) {
      // Try to extract a SpringSecurityException from the stacktrace
      Throwable[] causeChain = throwableAnalyzer.determineCauseChain(ex);
      RuntimeException ase = (AuthenticationException) throwableAnalyzer
          .getFirstThrowableOfType(AuthenticationException.class, causeChain);

      if (ase == null) {
        ase =
            (AccessDeniedException) throwableAnalyzer.getFirstThrowableOfType(AccessDeniedException.class, causeChain);
      }
      if (ase != null) {
        handleSpringSecurityException(request, response, chain, ase);
      } else {
        logger.error("NewSessionShareLoginFilter捕获到异常", ex);
        throw new ServletException(ex.toString());
      }
    }
  }

  private void handleSpringSecurityException(HttpServletRequest request, HttpServletResponse response,
      FilterChain chain, RuntimeException exception) throws IOException, ServletException {
    if (exception instanceof AccessDeniedException) {
      if (authenticationTrustResolver.isAnonymous(SecurityContextHolder.getContext().getAuthentication())) {
        logger.debug("访问的资源缺少权限，将跳转到oauth系统登录", exception);
        this.targetUrl(request, response);
      } else {
        logger.error("访问的资源缺少权限，但当前用户非匿名权限，权限为" + SecurityContextHolder.getContext().getAuthentication());
      }
    }
  }

  /**
   * 访问无权限或超时，URL分析逻辑
   * 
   * @param request
   * @param response
   * @throws IOException
   * @throws ServletException
   */
  protected void targetUrl(HttpServletRequest request, HttpServletResponse response)
      throws IOException, ServletException {
    String service = domainoauth + "/oauth/share"; // 权限系统开放权限共享地址，需要写在环境变量配置文件中
    String mobileService = domainMobile + "/oauth/mobile/newshare";
    String paramUrl = null;
    String uri = request.getRequestURI();
    String queryStr = StringUtils.isNotEmpty(request.getQueryString()) ? "?" + request.getQueryString() : "";
    String xReq = request.getHeader("x-requested-with");
    if (uri.indexOf("ajax") > -1 && StringUtils.isNotBlank(xReq) && "XMLHttpRequest".equalsIgnoreCase(xReq)) {
      // ajax请求 没有权限直接返回 超时 信息
      response.setHeader("syncSessionTimeout", "timeout");
      String fullContentType = "application/json;charset=UTF-8";
      response.setContentType(fullContentType);
      response.setHeader("Pragma", "No-cache");
      response.setHeader("Cache-Control", "no-cache");
      response.setDateHeader("Expires", 0);
      response.getWriter().write("{\"ajaxSessionTimeOut\":\"yes\"}");
      response.getWriter().flush();
      return;
    } else {
      // 请求页面 没有权限 重定向到 权限系统 登录 (并携带系统标记 如：nsfc,sns,rol....记录在环境变量中)
      String sys = "SNS"; // 需改为从环境变量中取
      // 需要拼接参数信息
      String path = request.getServletPath();
      String v6url = domainscm + path + queryStr;
      String userAgent = request.getHeader("User-Agent");
      // 是否是移动端请求
      boolean isMobileRequest = SmateMobileUtils.isMobileBrowser(userAgent);
      if (isMobileRequest) {
        v6url = domainMobile + path + queryStr;
      }
      Cookie[] cookies = request.getCookies();
      if (cookies != null && cookies.length > 0) {
        for (Cookie cookie : cookies) {
          if ("SYS".equals(cookie.getName())) {
            if ("WCI".equals(cookie.getValue())) {
              sys = "WCI";
              v6url = domainMobile + path + queryStr;
            }
          }
        }
      }
      paramUrl = this.dealMobileRequest(request, response, mobileService, v6url, sys, service, appId);
      response.sendRedirect(paramUrl);
      return;
    }
  }

  protected String wrapUrl(String uri, String context) {
    if (StringUtils.isNotEmpty(uri)) {
      String name = StringUtils.defaultString(context + "/");
      String resovleUrl = uri.replace(name, "");
      return resovleUrl;
    }
    return "";
  }

  /**
   * Default implementation of <code>ThrowableAnalyzer</code> which is capable of also unwrapping
   * <code>ServletException</code>s.
   */
  private static final class DefaultThrowableAnalyzer extends ThrowableAnalyzer {
    /**
     * @see org.springframework.security.web.util.ThrowableAnalyzer#initExtractorMap()
     */
    protected void initExtractorMap() {
      super.initExtractorMap();

      registerExtractor(ServletException.class, new ThrowableCauseExtractor() {
        public Throwable extractCause(Throwable throwable) {
          ThrowableAnalyzer.verifyThrowableHierarchy(throwable, ServletException.class);
          return ((ServletException) throwable).getRootCause();
        }
      });
    }

  }

  /**
   * 处理移动端请求
   * 
   * @param request
   * @param response
   * @throws UnsupportedEncodingException
   */
  private String dealMobileRequest(HttpServletRequest request, HttpServletResponse response, String mobileService,
      String v6Url, String sys, String service, String appId) throws UnsupportedEncodingException {
    String redirectUrl = null;
    String userAgent = request.getHeader("User-Agent");
    // 是否是移动端请求
    boolean isMobileRequest = SmateMobileUtils.isMobileBrowser(userAgent);
    // 是否是微信端请求
    boolean isWeChatRequest = SmateMobileUtils.isWeChatBrowser(userAgent);
    if (isMobileRequest || isWeChatRequest) {
      redirectUrl = mobileService + "?service=" + URLEncoder.encode(ServiceUtil.encodeToDes3(v6Url), "utf-8")
          + "&isWeChatRequest=" + isWeChatRequest;
      if (isWeChatRequest) {
        redirectUrl = MessageUtil.getOAuth2Url(appId, URLEncoder.encode(redirectUrl, "utf-8"), null, null);
      }
    } else {
      redirectUrl = service + "?service=" + URLEncoder.encode(ServiceUtil.encodeToDes3(v6Url), "utf-8") + "&sys=" + sys
          + "&sysDomain=" + URLEncoder.encode(domainscm, "utf-8");
    }
    return redirectUrl;

  }
}
