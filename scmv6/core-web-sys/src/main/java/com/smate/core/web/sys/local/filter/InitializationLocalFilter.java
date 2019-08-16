package com.smate.core.web.sys.local.filter;

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
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.LocaleUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.util.WebUtils;

import com.smate.core.base.utils.cache.CacheService;
import com.smate.core.base.utils.locale.service.SysUserLocaleService;
import com.smate.core.base.utils.mobile.SmateMobileUtils;
import com.smate.core.base.utils.model.local.SysUserLocale;
import com.smate.core.base.utils.security.SecurityUtils;
import com.smate.core.base.utils.security.TheadLocalDomain;

/**
 * 国际化拦截器
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
  // 用户语言版本缓存
  private static final String NEW_SYS_USER_LOCALE_CACHE = "new_sys_user_locale";

  private static final Logger LOGGER = LoggerFactory.getLogger(InitializationLocalFilter.class);

  private CacheService cacheService;
  private SysUserLocaleService sysUserLocaleService;

  /**
   * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
   */
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
      throws IOException, ServletException {
    HttpServletRequest httpServletRequest = (HttpServletRequest) request;
    HttpSession session = httpServletRequest.getSession();
    // SCM-16991-WSN
    if (SmateMobileUtils.isMobileBrowser(httpServletRequest)) {
      addCookie((HttpServletResponse) response, DEFUALT_LOCALE_ATTRIBUTE, Locale.CHINA.toString(), COOKIE_SAVE_TIME);
      LocaleContextHolder.setLocale(Locale.CHINA, true);
    } else {
      WebApplicationContext ctx = WebApplicationContextUtils.getWebApplicationContext(session.getServletContext());
      sysUserLocaleService = (SysUserLocaleService) ctx.getBean("sysUserLocaleService");
      cacheService = (CacheService) ctx.getBean("userCacheService");
      // 默认取cookie
      Locale currentLocale = loadCookieLocale(request, httpServletRequest);

      Long psnId = SecurityUtils.getCurrentUserId();
      if (psnId != null && psnId != 0L) {
        // 如果是登录用户默认取数据库记录
        Locale userLocale = loadUserLocal(psnId);
        if (userLocale != null) {
          currentLocale = userLocale;
        }
      }
      // 判断切换是否切换语言版本
      Locale changeLocale = changeLocale(request, psnId);
      if (changeLocale != null) {
        currentLocale = changeLocale;
      }

      // 设置线程变量 设置cookie
      // 判断语言版本是否标准 目前只支持中文跟英文
      currentLocale = Locale.CHINA.equals(currentLocale) ? currentLocale : Locale.US;
      addCookie((HttpServletResponse) response, DEFUALT_LOCALE_ATTRIBUTE, currentLocale.toString(), COOKIE_SAVE_TIME);
      LocaleContextHolder.setLocale(currentLocale, true);
    }

    // 设置全局域名变量，让所有service层可以访问
    setDomainUrl(request);

    // pass the request along the filter chain
    chain.doFilter(request, response);

    LocaleContextHolder.resetLocaleContext();
  }

  /**
   * 设置域名线程变量 暂时的
   * 
   * @param request
   */
  @Deprecated
  private void setDomainUrl(ServletRequest request) {
    String domainUrl = null;
    if (request.getServerPort() != 80) {
      domainUrl = request.getServerName() + ":" + request.getServerPort();
    } else {
      domainUrl = request.getServerName();
    }
    TheadLocalDomain.setDomain(domainUrl);
  }

  /**
   * 切换语言版本
   * 
   * @param request
   * @param currentLocale
   * @param psnId
   */
  private Locale changeLocale(ServletRequest request, Long psnId) {
    Locale changeLocale = null;
    try {
      changeLocale = LocaleUtils.toLocale(request.getParameter(DEFUALT_LOCALE_PARAMETER));
    } catch (IllegalArgumentException e) {
      LOGGER.warn("request 参数locale格式异常 {}", request.getParameter(DEFUALT_LOCALE_PARAMETER));
    }
    if (changeLocale != null && (Locale.CHINA.equals(changeLocale) || Locale.US.equals(changeLocale))) {
      // 保存用户最新语言版本
      if (psnId != null && psnId != 0L) {
        try {
          sysUserLocaleService.saveOrUpdateSysUserLocale(psnId, changeLocale.toString());
          // 重新缓存对象
          cacheService.put(NEW_SYS_USER_LOCALE_CACHE, psnId.toString(), changeLocale.toString());
        } catch (Exception e) {
          LOGGER.error("保存用户最新语言版本出错! psnId=" + psnId, e);
        }
      }
    }
    return changeLocale;
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
   * 加载登录用户语言版本
   * 
   * @param currentLocale
   * @param psnId
   * @return
   */
  private Locale loadUserLocal(Long psnId) {
    Locale userLocale = null;
    String localStr = null;
    try {
      Object localObj = cacheService.get(NEW_SYS_USER_LOCALE_CACHE, psnId.toString());
      if (localObj == null) {
        SysUserLocale sysUserLocale = sysUserLocaleService.getSysUserLocaleByPsnId(psnId);
        // 缓存
        if (sysUserLocale != null && StringUtils.isNotBlank(sysUserLocale.getLocale())) {
          cacheService.put(NEW_SYS_USER_LOCALE_CACHE, psnId.toString(), sysUserLocale.getLocale());
          localStr = sysUserLocale.getLocale();
        }
      } else {
        localStr = localObj.toString();
      }
      if (localStr != null) {
        userLocale = LocaleUtils.toLocale(localStr);
      }
    } catch (Exception e) {
      LOGGER.error("登录用户 获取设置的语言版本出错! psnId=" + psnId, e);
    }
    return userLocale;
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
