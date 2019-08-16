package com.smate.core.web.sys.security.authority;

import java.io.IOException;

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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.smate.core.base.utils.model.cas.security.User;
import com.smate.core.base.utils.security.SecurityUtils;
import com.smate.core.base.utils.service.security.SysUserLoginService;

/**
 * 判断帐号可用性.
 * 
 * @author tsz
 *
 */
public class AccountUsabilityCheckFilter implements Filter {

  private final Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private SysUserLoginService sysUserLoginService;
  @Value("${domainscm}")
  private String domainscm;
  @Value("${domainoauth}")
  private String domainoauth;

  /**
   * Default constructor.
   */
  public AccountUsabilityCheckFilter() {

  }

  /**
   * 判断帐号可用性.
   */
  public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
      throws IOException, ServletException {
    HttpServletRequest request = (HttpServletRequest) req;
    HttpServletResponse response = (HttpServletResponse) res;
    Long psnId = SecurityUtils.getCurrentUserId();
    if (psnId != null && !psnId.equals(0L)) {
      try {
        User user = sysUserLoginService.getUserByPsnId(psnId);
        if (user != null && !user.getEnabled()) {
          // 帐号不可用跳转 到封号页面。
          response.sendRedirect(domainoauth + "/oauth/accountexception");
          return;
        }
      } catch (Exception e) {
        logger.error("检查帐号可用性出错", e);
        return;
      }
    }
    chain.doFilter(request, response);
  }



  @Override
  public void destroy() {

  }

  @Override
  public void init(FilterConfig arg0) throws ServletException {

  }

  public SysUserLoginService getSysUserLoginService() {
    return sysUserLoginService;
  }

  public void setSysUserLoginService(SysUserLoginService sysUserLoginService) {
    this.sysUserLoginService = sysUserLoginService;
  }

}
