package com.smate.center.oauth.filter;

import java.io.IOException;
import java.util.Locale;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.LocaleUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.web.util.WebUtils;

import com.smate.core.base.utils.mobile.SmateMobileUtils;

/**
 * 国际化拦截器(简化版 没有用户登录信息的国际化切换与cookie存取)
 * 
 * @author tsz
 *
 */
public class InitializationLocalFilter implements Filter {

  // 切换参数
  private static final String DEFUALT_LOCALE_PARAMETER = "locale";
  // 存取名字
  public static final String DEFUALT_LOCALE_ATTRIBUTE = "locale_request_change_attr";
  // 国际化语言保存时间
  private static final int COOKIE_SAVE_TIME = 1209600;

  private static final Logger LOGGER = LoggerFactory.getLogger(InitializationLocalFilter.class);

  /**
   * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
   */
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
      throws IOException, ServletException {
    HttpServletRequest httpServletRequest = (HttpServletRequest) request;
    // SCM-16991-WSN
    if (SmateMobileUtils.isMobileBrowser(httpServletRequest)) {
      addCookie((HttpServletResponse) response, DEFUALT_LOCALE_ATTRIBUTE, Locale.CHINA.toString(), COOKIE_SAVE_TIME);
      LocaleContextHolder.setLocale(Locale.CHINA, true);
    } else {
      // 默认取cookie
      Locale currentLocale = loadCookieLocale(request, httpServletRequest);

      // 判断切换是否切换语言版本
      Locale changeLocale = changeLocale(request);
      if (changeLocale != null) {
        currentLocale = changeLocale;
      }

      // 设置线程变量 设置cookie
      // 判断语言版本是否标准 目前只支持中文跟英文
      currentLocale = Locale.CHINA.equals(currentLocale) ? currentLocale : Locale.US;
      addCookie((HttpServletResponse) response, DEFUALT_LOCALE_ATTRIBUTE, currentLocale.toString(), COOKIE_SAVE_TIME);
      LocaleContextHolder.setLocale(currentLocale, true);
    }

    // pass the request along the filter chain
    chain.doFilter(request, response);

    LocaleContextHolder.resetLocaleContext();
  }

  /**
   * 切换语言版本
   * 
   * @param request
   * @param currentLocale
   * @param psnId
   */
  private Locale changeLocale(ServletRequest request) {
    Locale changeLocale = null;
    try {
      changeLocale = LocaleUtils.toLocale(request.getParameter(DEFUALT_LOCALE_PARAMETER));
      if (changeLocale != null && (Locale.CHINA.equals(changeLocale) || Locale.US.equals(changeLocale))) {
        return changeLocale;
      }

    } catch (IllegalArgumentException e) {
      LOGGER.warn("request 参数locale格式异常 {}", request.getParameter(DEFUALT_LOCALE_PARAMETER));
    }
    return null;
  }

  /**
   * 加载cookie语言版本
   * 
   * @param request
   * @param httpServletRequest
   * @param currentLocale
   */
  private Locale loadCookieLocale(ServletRequest request, HttpServletRequest httpServletRequest) {
    Locale cookieLocale = null;
    Cookie cookie = WebUtils.getCookie(httpServletRequest, DEFUALT_LOCALE_ATTRIBUTE);
    try {
      if (cookie != null && !cookie.getValue().isEmpty()) {
        cookieLocale = LocaleUtils.toLocale(cookie.getValue());
      } else {
        cookieLocale = request.getLocale();
      }
    } catch (IllegalArgumentException e) {
      LOGGER.warn("cookie 参数locale格式异常 {}", request.getParameter(cookie.getValue()));
    }
    return cookieLocale == null ? Locale.CHINA : cookieLocale;
  }

  /**
   * 设置cookie
   * 
   * @param response
   * @param name
   * @param value
   * @param maxAge
   */
  public static void addCookie(HttpServletResponse response, String name, String value, int maxAge) {
    Cookie cookie = new Cookie(name, value);
    cookie.setPath("/");
    if (maxAge > 0)
      cookie.setMaxAge(maxAge);
    response.addCookie(cookie);
  }

  /**
   * @see Filter#init(FilterConfig)
   */
  public void init(FilterConfig fConfig) throws ServletException {}

  /**
   * @see Filter#destroy()
   */
  public void destroy() {}

  public static void main(String[] args) {
    System.err.println(LocaleUtils.toLocale(""));
  }

}
