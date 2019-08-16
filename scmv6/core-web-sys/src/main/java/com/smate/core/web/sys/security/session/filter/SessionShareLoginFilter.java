package com.smate.core.web.sys.security.session.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationTrustResolver;
import org.springframework.security.authentication.AuthenticationTrustResolverImpl;
import org.springframework.security.cas.ServiceProperties;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.util.ThrowableAnalyzer;
import org.springframework.security.web.util.ThrowableCauseExtractor;
import org.springframework.web.filter.GenericFilterBean;

import com.smate.core.base.utils.security.Des3Utils;

/**
 * 新系统权限控制，用户登录重定向到旧系统
 * 
 * @author zym
 * @version 6.0.1
 * @since 6.0.1
 */
public class SessionShareLoginFilter extends GenericFilterBean {

  // ~ Instance fields
  // ================================================================================================

  private AuthenticationTrustResolver authenticationTrustResolver = new AuthenticationTrustResolverImpl();
  private ThrowableAnalyzer throwableAnalyzer = new DefaultThrowableAnalyzer();
  private ServiceProperties serviceProperties;

  @SuppressWarnings("unused")
  private String domainscm;

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
      }
    }
  }

  private void handleSpringSecurityException(HttpServletRequest request, HttpServletResponse response,
      FilterChain chain, RuntimeException exception) throws IOException, ServletException {

    if (exception instanceof AccessDeniedException) {
      if (authenticationTrustResolver.isAnonymous(SecurityContextHolder.getContext().getAuthentication())) {
        logger.debug("访问的资源缺少权限，将跳转到旧系统登录", exception);
        this.targetUrl(request, response);
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
    String service = this.serviceProperties.getService();

    String paramUrl = null;
    String uri = request.getRequestURI();


    String queryStr = StringUtils.isNotEmpty(request.getQueryString()) ? "?" + request.getQueryString() : "";
    // ajax超时输出
    String xReq = request.getHeader("x-requested-with");
    if (uri.indexOf("ajax") > -1 && StringUtils.isNotBlank(xReq) && "XMLHttpRequest".equalsIgnoreCase(xReq)) {
      response.setHeader("syncSessionTimeout", "timeout");
      String fullContentType = "application/json;charset=UTF-8";
      response.setContentType(fullContentType);
      response.setHeader("Pragma", "No-cache");
      response.setHeader("Cache-Control", "no-cache");
      response.setDateHeader("Expires", 0);
      response.getWriter().write("{\"ajaxSessionTimeOut\":\"yes\"}");
      response.getWriter().flush();
      return;
    } else {// 普通url跳转登录后，重定向到目标地址
      String realHost = request.getHeader("Host");
      // 判断 是否跳转到成果在线 tsz
      int index = realHost.indexOf(".cn");
      String v6url = null;
      if (index > -1) {
        v6url = wrapUrl("http://" + realHost.substring(0, index) + ".cn/" + uri, request.getContextPath()) + queryStr;
      } else {
        v6url = wrapUrl(domainscm + "/" + uri, request.getContextPath()) + queryStr;
      }
      String scmSession = request.getParameter("NSFCSEID");
      if (scmSession != null) {
        Cookie cookie = new Cookie("JSESSIONID", scmSession);
        // cookie.setMaxAge(Integer.MAX_VALUE);默认-1关闭浏览器关闭
        cookie.setPath("/");
        response.addCookie(cookie);
        response.sendRedirect(v6url);
        return;
      }
      // String v6url = wrapUrl(service.substring(0, StringUtils.indexOf(service, '/', 10) + 1)+uri,
      // request.getContextPath()) + queryStr;
      paramUrl = service + "?token=" + Des3Utils.encodeToDes3(v6url);
    }
    response.sendRedirect(paramUrl);
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

  public void setServiceProperties(ServiceProperties serviceProperties) {
    this.serviceProperties = serviceProperties;
  }

  public void setDomainscm(String domainscm) {
    this.domainscm = domainscm;
  }

}
