package com.smate.core.base.utils.spring;

import org.apache.commons.lang.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * spring request工具类
 * 
 * @author tsz
 *
 * @date 2018年8月10日
 */

public class SpringUtils {

  /**
   * 取得HttpRequest的真实远程ip 兼容反向代理 因反向代理，会导致getRequest().getRemoteAddr()的ip为代理服务器地址
   * 
   */
  public static String getRemoteAddr() {
    String ip = getRequest().getHeader("X-Forwarded-For");
    if (StringUtils.isNotEmpty(ip) && !"unKnown".equalsIgnoreCase(ip)) {
      // 多次反向代理后会有多个ip值，第一个ip才是真实ip
      int index = ip.indexOf(",");
      if (index != -1) {
        return ip.substring(0, index);
      } else {
        return ip;
      }
    }
    ip = getRequest().getHeader("X-Real-IP");
    if (StringUtils.isNotEmpty(ip) && !"unKnown".equalsIgnoreCase(ip)) {
      return ip;
    }
    return getRequest().getRemoteAddr();

  }

  /**
   * 获取 request
   * 
   * @return
   */
  public static HttpServletRequest getRequest() {
    HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
    return request;
  }

  /**
   * 获取 response
   * 
   * @return
   */
  public static HttpServletResponse getResponse() {
    HttpServletResponse response =
        ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
    return response;
  }

  public static String getServerName() {
    return getRequest().getServerName();
  }
}
