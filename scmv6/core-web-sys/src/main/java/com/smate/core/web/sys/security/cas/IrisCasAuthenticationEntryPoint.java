/*
 * Copyright 2004, 2005, 2006 Acegi Technology Pty Limited
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package com.smate.core.web.sys.security.cas;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.jasig.cas.client.util.CommonUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.security.cas.ServiceProperties;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.util.Assert;

/**
 * Used by the <code>ExceptionTranslationFilter</code> to commence authentication via the JA-SIG
 * Central Authentication Service (CAS).
 * <p>
 * The user's browser will be redirected to the JA-SIG CAS enterprise-wide login page. This page is
 * specified by the <code>loginUrl</code> property. Once login is complete, the CAS login page will
 * redirect to the page indicated by the <code>service</code> property. The <code>service</code> is
 * a HTTP URL belonging to the current application. The <code>service</code> URL is monitored by the
 * {@link CasAuthenticationFilter}, which will validate the CAS login was successful.
 * 
 * @author Ben Alex
 * @author Scott Battaglia
 * 
 * 
 * @author tsz
 * 
 * @since 6.0.1
 * @version 6.0.1
 */
public class IrisCasAuthenticationEntryPoint implements AuthenticationEntryPoint, InitializingBean {
  // ~ Instance fields
  // ================================================================================================
  private ServiceProperties serviceProperties;

  private String loginUrl;
  private Map<String, String> loginUrlMap;
  // 相对路径
  private String relativelyLocaleUrl;

  private Map<String, String> filterUrlMap;

  public Map<String, String> getLoginUrlMap() {
    return loginUrlMap;
  }

  public void setLoginUrlMap(Map<String, String> loginUrlMap) {
    this.loginUrlMap = loginUrlMap;
  }

  /**
   * @return the filterUrlMap
   */
  public Map<String, String> getFilterUrlMap() {
    return filterUrlMap;
  }

  /**
   * @param filterUrlMap the filterUrlMap to set
   */
  public void setFilterUrlMap(Map<String, String> filterUrlMap) {
    this.filterUrlMap = filterUrlMap;
  }

  /**
   * Determines whether the Service URL should include the session id for the specific user. As of CAS
   * 3.0.5, the session id will automatically be stripped. However, older versions of CAS (i.e. CAS
   * 2), do not automatically strip the session identifier (this is a bug on the part of the older
   * server implementations), so an option to disable the session encoding is provided for backwards
   * compatibility.
   * 
   * By default, encoding is enabled.
   * 
   * @deprecated since 3.0.0 because CAS is currently on 3.3.5.
   */
  @Deprecated
  private boolean encodeServiceUrlWithSessionId = true;

  // ~ Methods
  // ========================================================================================================

  @Override
  public void afterPropertiesSet() throws Exception {
    Assert.hasLength(this.loginUrl, "loginUrl must be specified");
    Assert.notNull(this.serviceProperties, "serviceProperties must be specified");
  }

  @Override
  public final void commence(final HttpServletRequest servletRequest, final HttpServletResponse response,
      final AuthenticationException authenticationException) throws IOException, ServletException {
    /**
     * 以下改造代码目的为识别多域名访问验证
     */
    // HttpSession session =
    // ((HttpServletRequest)servletRequest).getSession();
    // Map portal =
    // (Map)session.getAttribute(ClientConstants.INS_PORTAL_KEY);
    String service = null;
    int serverPort = 0;
    String domain = servletRequest.getServerName();
    if (80 != servletRequest.getServerPort()) {
      serverPort = servletRequest.getServerPort();
    }

    // if(portal!=null&&portal.size()>0)
    // {
    if (null != domain && !"".equals(domain)) {
      if (serverPort != 0 && serverPort != 80) {
        service = "http://" + domain + ":" + servletRequest.getServerPort() + this.relativelyLocaleUrl;// +
        // "?tomcat=node1";
      } else {
        service = "http://" + domain + this.relativelyLocaleUrl; // +
        // "?tomcat=node1";//
        // 这里为集群注销加入标记
      }
    }
    // }
    if (service == null) {
      service = this.serviceProperties.getService();
    }

    final String urlEncodedService = CommonUtils.constructServiceUrl(null, response, service, null,
        this.serviceProperties.getArtifactParameter(), this.encodeServiceUrlWithSessionId);

    /** citeWrite超时 要定位到mininsfc 登录界面 */
    final String redirectUrl;
    boolean isOtherPage = false;
    String uri = servletRequest.getRequestURI();
    String otherLoginPage = "";
    if (MapUtils.isNotEmpty(loginUrlMap) && StringUtils.isNotBlank(uri)) {
      for (String key : loginUrlMap.keySet()) {
        if ((StringUtils.indexOf(uri, key) > -1 && StringUtils.indexOf(uri, key) + key.length() == uri.length())
            || StringUtils.indexOf(uri, key + "?") > -1
            || StringUtils.indexOf(uri.toLowerCase(), key.toLowerCase() + "%3f") > -1) {
          isOtherPage = true;
          otherLoginPage = loginUrlMap.get(key);
          break;
        }
      }
    }
    if (isOtherPage && StringUtils.isNotBlank(otherLoginPage)) {
      redirectUrl = createRedirectUrl(urlEncodedService, otherLoginPage);
    } else {
      /** end */

      String queryStr =
          StringUtils.isNotEmpty(servletRequest.getQueryString()) ? "?" + servletRequest.getQueryString() : "";
      String paramUrl = null;
      if (MapUtils.isNotEmpty(this.filterUrlMap) && StringUtils.isNotBlank(uri)) {

        String resovleUrl = uri.replace(servletRequest.getContextPath(), "");
        uri = this.filterUrlMap.get(resovleUrl) == null ? uri
            : servletRequest.getContextPath() + this.filterUrlMap.get(resovleUrl);

      }

      if (uri.indexOf("ajax") == -1) {

        paramUrl = "http://" + servletRequest.getServerName() + uri + queryStr;
      } else {
        paramUrl = "http://" + servletRequest.getServerName() + servletRequest.getContextPath() + "/";

      }
      redirectUrl = wrapperUrl(createRedirectUrl(urlEncodedService, loginUrl), paramUrl);
    }

    String xReq = servletRequest.getHeader("x-requested-with");
    if (StringUtils.isNotBlank(xReq) && "XMLHttpRequest".equalsIgnoreCase(xReq)) {

      response.setHeader("syncSessionTimeout", "timeout");
      String fullContentType = "application/json;charset=UTF-8";
      response.setContentType(fullContentType);
      response.setHeader("Pragma", "No-cache");
      response.setHeader("Cache-Control", "no-cache");
      response.setDateHeader("Expires", 0);
      response.getWriter().write("{\"ajaxSessionTimeOut\":\"yes\"}");
      response.getWriter().flush();
      return;

    }

    preCommence(servletRequest, response);

    response.sendRedirect(redirectUrl);
  }

  private String wrapperUrl(final String redirectUrl, String paramUrl) {

    try {

      if (StringUtils.isEmpty(paramUrl)) {

        return redirectUrl;
      }

      return redirectUrl + (redirectUrl.indexOf("?") != -1 ? "&" : "?") + "target_url="
          + URLEncoder.encode(paramUrl, "UTF-8");
    } catch (final UnsupportedEncodingException e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * Constructs the Url for Redirection to the CAS server. Default implementation relies on the CAS
   * client to do the bulk of the work.
   * 
   * @param serviceUrl the service url that should be included.
   * @return the redirect url. CANNOT be NULL.
   */
  protected String createRedirectUrl(final String serviceUrl, final String loginUrl) {
    return CommonUtils.constructRedirectUrl(loginUrl, this.serviceProperties.getServiceParameter(), serviceUrl,
        this.serviceProperties.isSendRenew(), false);
  }

  /**
   * Template method for you to do your own pre-processing before the redirect occurs.
   * 
   * @param request the HttpServletRequest
   * @param response the HttpServletResponse
   */
  protected void preCommence(final HttpServletRequest request, final HttpServletResponse response) {

  }

  /**
   * The enterprise-wide CAS login URL. Usually something like
   * <code>https://www.mycompany.com/cas/login</code>.
   * 
   * @return the enterprise-wide CAS login URL
   */
  public final String getLoginUrl() {
    return this.loginUrl;
  }

  public final ServiceProperties getServiceProperties() {
    return this.serviceProperties;
  }

  public final void setLoginUrl(final String loginUrl) {
    this.loginUrl = loginUrl;
  }

  public final void setServiceProperties(final ServiceProperties serviceProperties) {
    this.serviceProperties = serviceProperties;
  }

  public String getRelativelyLocaleUrl() {
    return relativelyLocaleUrl;
  }

  public void setRelativelyLocaleUrl(String relativelyLocaleUrl) {
    this.relativelyLocaleUrl = relativelyLocaleUrl;
  }

  /**
   * Sets whether to encode the service url with the session id or not.
   * 
   * @param encodeServiceUrlWithSessionId whether to encode the service url with the session id or
   *        not.
   * @deprecated since 3.0.0 because CAS is currently on 3.3.5.
   */
  @Deprecated
  public final void setEncodeServiceUrlWithSessionId(final boolean encodeServiceUrlWithSessionId) {
    this.encodeServiceUrlWithSessionId = encodeServiceUrlWithSessionId;
  }

  /**
   * Sets whether to encode the service url with the session id or not.
   * 
   * @return whether to encode the service url with the session id or not.
   * 
   * @deprecated since 3.0.0 because CAS is currently on 3.3.5.
   */
  @Deprecated
  protected boolean getEncodeServiceUrlWithSessionId() {
    return this.encodeServiceUrlWithSessionId;
  }

}
