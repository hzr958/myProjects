package com.smate.core.web.sys.security.authority;

import java.io.IOException;
import java.io.Serializable;
import java.util.Map;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.RequestMatcher;

import com.smate.core.base.utils.cache.CacheConst;
import com.smate.core.base.utils.constant.LoginTypeConsts;
import com.smate.core.base.utils.constant.security.SecurityConstants;
import com.smate.core.base.utils.mobile.SmateMobileUtils;
import com.smate.core.base.utils.model.UserInfo;
import com.smate.core.base.utils.security.Des3Utils;
import com.smate.core.base.utils.security.OauthsAuthenticationToken;
import com.smate.core.base.utils.security.TheadLocalInsId;
import com.smate.core.base.utils.security.TheadLocalNodeId;
import com.smate.core.base.utils.security.TheadLocalPsnId;
import com.smate.core.base.utils.security.TheadLocalRoleId;
import com.smate.core.base.utils.security.TheadLocalUnitId;
import com.smate.core.base.utils.service.security.AutoLoginOauthInfoService;
import com.smate.core.base.utils.struts2.Struts2Utils;
import com.smate.core.web.sys.security.cache.UserCacheService;
import com.smate.core.web.sys.security.user.ScmUserDetailsService;

/**
 * 自定义 权限拦截
 * 
 * @author tsz
 *
 */
public class OauthsAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

  @Value("${domainscm}")
  private String domainscm;
  @Value("${domainMobile}")
  private String domainMobile;
  private AutoLoginOauthInfoService autoLoginOauthInfoService;
  private UserCacheService userCacheService; // 权限缓存 服务
  private ScmUserDetailsService scmUserDetailsService;

  protected OauthsAuthenticationFilter(RequestMatcher myequiresAuthenticationRequestMatcher) {
    super(myequiresAuthenticationRequestMatcher);
  }

  /**
   * 重写父类 拦截器 方法
   */
  @Override
  public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
      throws IOException, ServletException {
    try {
      HttpServletRequest request = (HttpServletRequest) req;
      HttpServletResponse response = (HttpServletResponse) res;
      // 拦截器 拦截条件 只有参数带了 from_oauth表示是从oauth那边跳转过来的才能 执行里面的方法
      if (!requiresAuthentication(request, response)) {
        logger.info("当前sessionId为" + request.getSession().getId());
        // 将当前请求地址放session中，弹出登录框登录时作为service参数
        String queryStr = StringUtils.isNotEmpty(request.getQueryString()) ? "?" + request.getQueryString() : "";
        request.getSession().setAttribute("reqUrl",
            Des3Utils.encodeToDes3(domainscm + request.getServletPath() + queryStr));
        // 判断 缓存 如果缓存过期，就清理权限信息
        Object object = userCacheService.get(SecurityConstants.USER_DETAILS_INFO_CACHE, request.getSession().getId());
        if (object == null
            && SecurityContextHolder.getContext().getAuthentication() instanceof OauthsAuthenticationToken) {
          SecurityContextHolder.clearContext();
        }
        // 判断当前spring Secutiry中权限和当前系统是否对应，不对应的尝试从缓存中获取
        buildNewSecurity(request, response);
        // 缓存中也没有权限时，尝试用cookie中的AID参数构建权限信息
        rebuildSecurityFromAID(request, request.getRequestURL().toString());
        chain.doFilter(request, response);
        return;
      }
      if (logger.isDebugEnabled()) {
        logger.debug("Request is to process authentication");
      }
      Authentication authResult;
      try {
        // 获取权限
        authResult = attemptAuthentication(request, response);
        if (authResult == null) {
          chain.doFilter(request, response);
          return;
        }
      } catch (InternalAuthenticationServiceException failed) {
        logger.error("An internal error occurred while trying to authenticate the user.", failed);
        unsuccessfulAuthentication(request, response, failed);
        return;
      } catch (AuthenticationException failed) {
        logger.error("出现了AuthenticationException错误", failed);
        unsuccessfulAuthentication(request, response, failed);
        return;
      }
      // Authentication success
      successfulAuthentication(request, response, authResult);
    } catch (Exception e) {
      logger.error("OauthsAuthenticationFilter报错了", e);
      throw new ServletException(e.toString());
    }
    return;
  }

  /**
   * 权限验证成功操作
   * 
   * @param request
   * @param response
   * @param authResult
   * @throws IOException
   */
  private void successfulAuthentication(HttpServletRequest request, HttpServletResponse response,
      Authentication authResult) throws IOException {
    // 记录spring 变量
    SecurityContextHolder.getContext().setAuthentication(authResult);
    logger.info("登录的用户ID--------------->" + SecurityContextHolder.getContext().getAuthentication().getName());
    // TODO 记录线程变量 像 当前登录人员id 角色id 单位id .....
    this.setThreadInfo(authResult);
    // 页面重定向
    String targetUrl = domainscm + request.getServletPath() + "?";
    // 移动端用移动端域名
    if (SmateMobileUtils.isMobileBrowser(request)) {
      targetUrl = domainMobile + request.getServletPath() + "?";
    }
    if (StringUtils.isNotBlank(request.getQueryString())) {
      targetUrl = targetUrl + handleParams(request);
    }
    if (targetUrl.indexOf("?") == -1) {
      targetUrl += "?";
    }
    // 手动拼接重定向页面
    if (targetUrl.lastIndexOf("&") == targetUrl.length() - 1 || targetUrl.lastIndexOf("?") == targetUrl.length() - 1) {
      targetUrl = targetUrl.substring(0, targetUrl.length() - 1);
    }
    response.sendRedirect(targetUrl);
  }

  /**
   * 处理参数，去掉FROM_OAUTH=true
   * 
   * @param request
   * @return
   */
  private String handleParams(HttpServletRequest request) {
    String paramsStr = request.getQueryString();
    paramsStr = paramsStr.replaceAll("[&]*[?]*" + SecurityConstants.FROM_OAUTH + "=true", "");
    return paramsStr;
  }

  /**
   * 重写 获取权限的方法
   */
  @Override
  public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
      throws AuthenticationException, IOException, ServletException {
    Object object = userCacheService.get(SecurityConstants.USER_DETAILS_INFO_CACHE, request.getSession().getId());
    if (object == null) {
      logger.warn("-------------------------从缓存中获取到的UserDetails为空-----------------" + "SessionId = "
          + request.getSession().getId());
      clearThreadInfo();
      return null;
    } else {
      Map<String, Object> detailsMap = (Map<String, Object>) object;
      // Long insId = getInsIdByDomain(request);
      Object uDetail = detailsMap.get("SNS" + "|" + 0);
      if (uDetail == null) {
        uDetail = detailsMap.get("WCI" + "|" + 0);
      }
      if (uDetail == null) {
        uDetail = detailsMap.get("SIE" + "|" + 0);
      }
      if (uDetail == null) {
        uDetail = detailsMap.get("SIE6" + "|" + 0);
      }
      if (uDetail != null) {
        UserDetails userDetails = (UserDetails) uDetail;
        OauthsAuthenticationToken oToken = new OauthsAuthenticationToken(userDetails.getAuthorities(), userDetails);
        return oToken;
      } else {
        clearThreadInfo();
        return null;
      }

    }
  }

  public void setUserCacheService(UserCacheService userCacheService) {
    this.userCacheService = userCacheService;
  }

  public void setAutoLoginOauthInfoService(AutoLoginOauthInfoService autoLoginOauthInfoService) {
    this.autoLoginOauthInfoService = autoLoginOauthInfoService;
  }

  public void setScmUserDetailsService(ScmUserDetailsService scmUserDetailsService) {
    this.scmUserDetailsService = scmUserDetailsService;
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

  /**
   * 查看当前权限信息是否对应当前系统，如果不是则尝试从缓存里面获取对应的权限
   * 
   * @param request
   * @param response
   * @throws AuthenticationException
   * @throws IOException
   * @throws ServletException
   */
  private void buildNewSecurity(HttpServletRequest request, HttpServletResponse response)
      throws AuthenticationException, IOException, ServletException {
    String sessionId = request.getSession().getId();
    if (SecurityContextHolder.getContext().getAuthentication() != null) {
      Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
      if (principal != null && principal instanceof UserInfo) {
        String uSys = ((UserInfo) principal).getSys();
        Long uInsId = ((UserInfo) principal).getInsId();
        // Long currentInsId = getInsIdByDomain(request);
        if (!("SNS".equals(uSys) || "WCI".equals(uSys)) || !compareLongValue(0L, uInsId)) {
          SecurityContextHolder.clearContext();
          Authentication auth = attemptAuthentication(request, response);
          if (auth != null) {
            SecurityContextHolder.getContext().setAuthentication(auth);
            setThreadInfo(auth);
          } else {
            SecurityContextHolder.clearContext();
          }
        }
      }
    } else {
      Authentication auth = attemptAuthentication(request, response);
      if (auth != null) {
        SecurityContextHolder.getContext().setAuthentication(auth);
        setThreadInfo(auth);
      } else {
        SecurityContextHolder.clearContext();
      }
    }
  }

  /**
   * 清空线程中信息
   */
  private void clearThreadInfo() {
    // 清空线程中的信息
    TheadLocalPsnId.setPsnId(0L);
    TheadLocalNodeId.setCurrentUserNodeId(0);
    TheadLocalRoleId.setRoleId(0);
    TheadLocalUnitId.setUnitId(0L);
    TheadLocalInsId.setInsId(null);
  }

  /**
   * 根据权限信息设置线程中的信息
   * 
   * @param authResult
   */
  private void setThreadInfo(Authentication authResult) {
    TheadLocalPsnId.setPsnId(NumberUtils.toLong(authResult.getName()));
    Object userDetail = authResult.getPrincipal();
    if (userDetail instanceof UserInfo) {
      TheadLocalNodeId.setCurrentUserNodeId(((UserInfo) userDetail).getNodeId());
      TheadLocalRoleId.setRoleId(((UserInfo) userDetail).getRoleId());
      // TheadLocalUnitId.setUnitId(userDetail.get); //设置部门ID
      TheadLocalInsId.setInsId(((UserInfo) userDetail).getInsId()); // 设置机构ID
      // TheadLocalInsId.setInsId(0L);
    }
  }

  // 如果cookie中有有效AID，则通过AID构建权限信息
  private void rebuildSecurityFromAID(HttpServletRequest request, String requestUrl) {
    try {
      if (SecurityContextHolder.getContext().getAuthentication() == null) {
        // 先看下cookie中是否有AID
        Cookie aid = Struts2Utils.getCookie(request, SecurityConstants.AUTO_LOGIN_PARAMETER_NAME);
        // 判断AID是否过期
        if (aid != null && !"".equals(aid.getValue())) {
          // 判断参数有没有过期
          String autoinfo = autoLoginOauthInfoService.checkAutoLoginOauth(aid.getValue());
          if (StringUtils.isNotBlank(autoinfo)) {
            String[] temp = autoinfo.split(",");
            String psnId = temp[0];
            String token = temp[1];
            UserDetails uDetails = scmUserDetailsService.loadUserFromSys(psnId, "SNS", requestUrl);
            String sesseionId = request.getSession().getId();
            Map<String, Object> detailsMap =
                scmUserDetailsService.buildUserDetailsMap(sesseionId, requestUrl, "SNS", uDetails);
            detailsMap.remove("userId");
            detailsMap.put("userId", psnId);
            userCacheService.put(SecurityConstants.USER_DETAILS_INFO_CACHE, SecurityConstants.USER_DETAILS_CACHE_TIME,
                sesseionId, (Serializable) detailsMap);
            // 更新AutoLoginOauthInfo表的登录次数 使用时间
            autoLoginOauthInfoService.updateAutoLoinUserTime(aid.getValue());
            // 移除自动登录前，用户登入的用户信息SCM-14525
            userCacheService.remove(CacheConst.NEW_USER_DATA_CACHE, sesseionId);
            // 记录自动登录日志
            autoLoginOauthInfoService.saveAutoLoginLogWithReq(request, NumberUtils.toLong(psnId),
                Struts2Utils.getRemoteAddrByReq(request), requestUrl, LoginTypeConsts.BUILD_SECURITY_BY_AID);
            // 将权限信息放入
            OauthsAuthenticationToken oToken = new OauthsAuthenticationToken(uDetails.getAuthorities(), uDetails);
            if (oToken != null) {
              SecurityContextHolder.getContext().setAuthentication(oToken);
              setThreadInfo(oToken);
            }
          }
          // 过期的AID就去掉，不然下次请求还需要查询判断
        }
      }
    } catch (Exception e) {
      logger.error("尝试从cookie中的AID构建权限信息出错了", e);
    }
  }

}
