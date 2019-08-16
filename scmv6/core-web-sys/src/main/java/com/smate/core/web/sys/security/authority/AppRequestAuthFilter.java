package com.smate.core.web.sys.security.authority;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URLEncoder;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.smate.core.base.app.model.AppAuthToken;
import com.smate.core.base.app.service.AppAuthTokenService;
import com.smate.core.base.utils.app.AppActionUtils;
import com.smate.core.base.utils.constant.IOSHttpStatus;
import com.smate.core.base.utils.security.Des3Utils;
import com.smate.core.base.utils.security.TheadLocalPsnId;

/**
 * APP请求权限处理过滤器
 * 
 * @author LJ
 *
 *         2017年10月26日
 */

@Component("appRequestAuthFilter")
public class AppRequestAuthFilter implements Filter {
  private final Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private AppAuthTokenService appAuthTokenService;

  @Override
  public void init(FilterConfig filterConfig) throws ServletException {

  }

  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
      throws IOException, ServletException {
    HttpServletResponse httpResponse = (HttpServletResponse) response;
    HttpServletRequest httpRequest = (HttpServletRequest) request;
    // web-mobile项目中的请求是/data/开头的，其他的项目里面的请求是/app/开头的
    if (httpRequest.getRequestURI().contains("/data/") && !httpRequest.getRequestURI().contains("data/outside")
        || httpRequest.getRequestURI().contains("/app/")) {
      logger.info("正在进入app用户权限Filter");
      String method = httpRequest.getMethod();
      if ("POST".equals(method)) {
        boolean dealPostMethod = this.dealPostMethod(httpResponse, httpRequest);
        if (dealPostMethod == false) {
          return;
        }
      } else if ("GET".equals(method)) {
        boolean dealGetMethod = this.dealGetMethod(httpResponse, httpRequest);
        if (dealGetMethod == false) {
          return;
        }
      } else {
        AppActionUtils.renderAPPReturnJsonWithResponse(httpResponse, "unsupport method", 0, IOSHttpStatus.BAD_REQUEST);
        return;
      }
    }
    chain.doFilter(request, response);
  }

  /**
   * 处理post请求，token放置在header中
   * 
   * @param httpResponse
   * @param httpRequest
   */
  public boolean dealPostMethod(HttpServletResponse httpResponse, HttpServletRequest httpRequest) {
    try {
      String token = httpRequest.getHeader("token");
      String id = Des3Utils.decodeFromDes3(token).split(":")[1];
      Long psnId = NumberUtils.toLong(id);
      AppAuthToken tokeninfo = appAuthTokenService.getToken(psnId);
      if (tokeninfo != null) {
        if (!tokeninfo.getToken().equals(token)) {
          logger.info("在app用户权限,token已过期");
          AppActionUtils.renderAPPReturnJsonWithResponse(httpResponse, "invalid url", 0, IOSHttpStatus.URL_OUTOFTIME);
          return false;
        }
        TheadLocalPsnId.setPsnId(psnId);
      } else {
        throw new Exception();
      }
    } catch (Exception e) {
      TheadLocalPsnId.setPsnId(0L);
      logger.error("在app用户权限,获取app用户psnId出错", e);
      AppActionUtils.renderAPPReturnJsonWithResponse(httpResponse, "permission denied", 0, IOSHttpStatus.FORBIDDEN);
      return false;
    }
    return true;
  }

  /**
   * 处理get请求方式
   * 
   * @param httpResponse
   * @param httpRequest
   */
  public boolean dealGetMethod(HttpServletResponse httpResponse, HttpServletRequest httpRequest) {
    try {
      String urltoken = null;
      String token = null;
      urltoken = httpRequest.getHeader("token");// 先从头部取
      if (urltoken == null) {
        // 从get url参数取，需要转码
        urltoken = httpRequest.getParameter("token");
        if (urltoken == null) {
          logger.info("在app用户权限,缺失token");
          AppActionUtils.renderAPPReturnJsonWithResponse(httpResponse, "invalid url", 0, IOSHttpStatus.URL_OUTOFTIME);
          return false;
        }
        token = URLEncoder.encode(urltoken, "UTF-8");
      } else {
        token = urltoken;
      }
      String id = null;
      String tokenStr = Des3Utils.decodeFromDes3(token);
      if (tokenStr != null && tokenStr.split(":").length > 1) {
        id = Des3Utils.decodeFromDes3(token).split(":")[1];
      }
      Long psnId = NumberUtils.toLong(id);
      AppAuthToken tokeninfo = appAuthTokenService.getToken(psnId);
      if (tokeninfo != null) {
        if (!tokeninfo.getToken().equals(token) && !tokeninfo.getToken().equals(urltoken)) {
          logger.info("在app用户权限,token已过期");
          AppActionUtils.renderAPPReturnJsonWithResponse(httpResponse, "invalid url", 0, IOSHttpStatus.URL_OUTOFTIME);
          return false;
        }
        TheadLocalPsnId.setPsnId(psnId);
      } else {
        throw new Exception();
      }
    } catch (Exception e) {
      TheadLocalPsnId.setPsnId(0L);
      logger.error("在app用户权限,获取app用户psnId出错", e);
      AppActionUtils.renderAPPReturnJsonWithResponse(httpResponse, "permission denied", 0, IOSHttpStatus.FORBIDDEN);
      return false;
    }
    return true;
  }

  /**
   * post取参数,getInputStream()只能读取一次，会导致后面读取不到
   * 
   * @param httpRequest
   * @return
   * @throws Exception
   */
  public String getPostToken(HttpServletRequest httpRequest) throws Exception {
    ServletInputStream inputStream = httpRequest.getInputStream();
    InputStreamReader reader = new InputStreamReader(inputStream, "UTF-8");
    StringBuffer buffer = new StringBuffer();
    char[] buf = new char[64];
    int count = 0;
    try {
      while ((count = reader.read(buf)) != -1)
        buffer.append(buf, 0, count);
    } finally {
      reader.close();
    }
    String result = buffer.toString();
    return null;

  }

  @Override
  public void destroy() {

  }

}
