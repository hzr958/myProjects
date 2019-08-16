package com.smate.core.web.sys.security.xss;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;


/**
 * xssfilter 过滤器.
 * 
 * @author Administrator
 *
 * @date 2018年9月8日
 */
public class XssFilter implements Filter {


  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
      throws IOException, ServletException {
    XsslHttpServletRequestWrapper xssRequest = new XsslHttpServletRequestWrapper((HttpServletRequest) request);
    chain.doFilter(xssRequest, response);
  }

  @Override
  public void init(FilterConfig fconfig) throws ServletException {}



  @Override
  public void destroy() {

  }

}
