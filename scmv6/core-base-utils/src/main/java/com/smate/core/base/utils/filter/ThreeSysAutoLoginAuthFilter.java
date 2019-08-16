package com.smate.core.base.utils.filter;

import java.io.IOException;
import java.net.URLEncoder;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.smate.core.base.utils.cache.SnsCacheService;
import com.smate.core.base.utils.constant.security.SecurityConstants;
import com.smate.core.base.utils.security.Des3Utils;

/**
 * 第三方系统自动登录参数拦截(自动登录参数名字 统一 AID)
 * 
 * @author zk
 *
 */
public class ThreeSysAutoLoginAuthFilter implements Filter {

  private final Logger logger = LoggerFactory.getLogger(getClass());

  @Value("${domainscm}")
  private String domainscm;
  @Value("${domainoauth}")
  private String domainoauth;

  /**
   * Default constructor.
   */
  public ThreeSysAutoLoginAuthFilter() {

  }


  /**
   * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain) 1.判断有没有这个参数 2.判断参数有没有过期3.
   *      有这个参数 重定向 到 oauth系统 /oauth/autologin 4.没有这个参数或参数过期 直接 chain.doFilter
   */
  public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
      throws IOException, ServletException {

    HttpServletRequest request = (HttpServletRequest) req;
    HttpServletResponse response = (HttpServletResponse) res;

    // 需要注意 记住登录 也是用这个实现 所以 需要判断 如果 当前没有系统 没有权限 就是缓存中取不出权限 就从cookie中取出来
    try {
      request.getRequestURI();
      if ("1".equals(request.getParameter("threesys"))) {
        String AID = request.getParameter(SecurityConstants.AUTO_LOGIN_PARAMETER_NAME);
        if (AID != null && !"".equals(AID)) {
          // 判断参数有没有过期
          WebApplicationContext ctx =
              WebApplicationContextUtils.getWebApplicationContext(request.getSession().getServletContext());
          SnsCacheService snsCacheService = (SnsCacheService) ctx.getBean("snsCacheService");
          if (snsCacheService != null) {
            Object obj = snsCacheService.get(SecurityConstants.AUTO_LOGIN_INFO_CACHE, AID);
            if (obj != null) {

              // 有这个参数 并且没有过期 重定向 到 oauth系统 /oauth/autologin
              String targetAction = request.getServletPath();
              String targetUrl = request.getSession().getServletContext().getAttribute("domainscm") + targetAction;
              // 手动拼接重定向页面
              response.sendRedirect(request.getSession().getServletContext().getAttribute("domainoauth")
                  + "/oauth/autologin?service=" + URLEncoder.encode(Des3Utils.encodeToDes3(targetUrl), "utf-8")
                  + "&threesys=1&" + SecurityConstants.AUTO_LOGIN_PARAMETER_NAME + "=" + AID);
              return;
            }
          }
        }
      }
    } catch (Exception e) {
      logger.error("拦截 自动登录参数出错", e);
    }
    // 没有这个参数 或参数过期 直接 chain.doFilter
    chain.doFilter(request, response);
  }

  @Override
  public void destroy() {

  }

  @Override
  public void init(FilterConfig arg0) throws ServletException {

  }

}
