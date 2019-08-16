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

package com.smate.core.web.sys.security.authority;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

import com.smate.core.base.utils.service.security.ResourceDetailService;

/**
 * Default implementation of <tt>FilterInvocationDefinitionSource</tt>.
 * <p>
 * Stores an ordered map of compiled URL paths to <tt>ConfigAttribute</tt> lists and provides URL
 * matching against the items stored in this map using the configured <tt>UrlMatcher</tt>.
 * <p>
 * The order of the URL paths in the map is very important. The system will identify the
 * <b>first</b> matching path for a given HTTP URL. It will not proceed to evaluate later paths if a
 * match has already been found. Accordingly, the most specific matches should be registered first,
 * with the most general matches registered last.
 * <p>
 * If URL paths are registered for a particular HTTP method using, then the method-specific matches
 * will take precedence over any URLs which are registered without an HTTP method.
 * 
 * @author Ben Alex
 * @author Luke Taylor
 */
public class IrisFilterInvocationSecurityMetadataSource
    implements FilterInvocationSecurityMetadataSource, InitializingBean {

  private ResourceDetailService resourceDetailService;

  public void setResourceDetailService(ResourceDetailService resourceDetailService) {
    this.resourceDetailService = resourceDetailService;
  }

  private static final Set<String> HTTP_METHODS =
      new HashSet<String>(Arrays.asList("DELETE", "GET", "HEAD", "OPTIONS", "POST", "PUT", "TRACE"));

  protected final Log logger = LogFactory.getLog(getClass());

  // private Map<Object, List<ConfigAttribute>> requestMap = new
  // LinkedHashMap<Object, List<ConfigAttribute>>();
  /**
   * Stores request maps keyed by specific HTTP methods. A null key matches any method
   */
  private Map<String, Map<Object, Collection<ConfigAttribute>>> httpMethodMap =
      new HashMap<String, Map<Object, Collection<ConfigAttribute>>>();

  /* private UrlMatcher urlMatcher; */
  /* private RequestMatcher requestMatcher; */

  private boolean stripQueryStringFromUrls;

  // ~ Constructors
  // ===================================================================================================

  /**
   * Builds the internal request map from the supplied map. The key elements should be of type
   * {@link RequestKey}, which contains a URL path and an optional HTTP method (may be null). The path
   * stored in the key will depend on the type of the supplied UrlMatcher.
   * 
   * @param urlMatcher typically an ant or regular expression matcher.
   * @param requestMap order-preserving map of request definitions to attribute lists
   */
  public IrisFilterInvocationSecurityMetadataSource() {

  }

  // ~ Methods
  // ========================================================================================================

  /**
   * Adds a URL,attribute-list pair to the request map, first allowing the <tt>UrlMatcher</tt> to
   * process the pattern if required, using its <tt>compile</tt> method. The returned object will be
   * used as the key to the request map and will be passed back to the <tt>UrlMatcher</tt> when
   * iterating through the map to find a match for a particular URL.
   */
  private void addSecureUrl(String pattern, String method, Collection<ConfigAttribute> attrs) {
    Map<Object, Collection<ConfigAttribute>> mapToUse = getRequestMapForHttpMethod(method);

    mapToUse.put(pattern, attrs);

    if (logger.isDebugEnabled()) {
      logger.debug("Added URL pattern: " + pattern + "; attributes: " + attrs
          + (method == null ? "" : " for HTTP method '" + method + "'"));
    }
  }

  /**
   * Return the HTTP method specific request map, creating it if it doesn't already exist.
   * 
   * @param method GET, POST etc
   * @return map of URL patterns to <tt>ConfigAttribute</tt>s for this method.
   */
  private Map<Object, Collection<ConfigAttribute>> getRequestMapForHttpMethod(String method) {
    if (method != null && !HTTP_METHODS.contains(method)) {
      throw new IllegalArgumentException("Unrecognised HTTP method: '" + method + "'");
    }

    Map<Object, Collection<ConfigAttribute>> methodRequestMap = httpMethodMap.get(method);

    if (methodRequestMap == null) {
      methodRequestMap = new LinkedHashMap<Object, Collection<ConfigAttribute>>();
      httpMethodMap.put(method, methodRequestMap);
    }

    return methodRequestMap;
  }

  public Collection<ConfigAttribute> getAllConfigAttributes() {
    Set<ConfigAttribute> allAttributes = new HashSet<ConfigAttribute>();

    for (Map.Entry<String, Map<Object, Collection<ConfigAttribute>>> entry : httpMethodMap.entrySet()) {
      for (Collection<ConfigAttribute> attrs : entry.getValue().values()) {
        allAttributes.addAll(attrs);
      }
    }

    return allAttributes;
  }

  public Collection<ConfigAttribute> getAttributes(Object object) {
    if ((object == null) || !this.supports(object.getClass())) {
      throw new IllegalArgumentException("Object must be a FilterInvocation");
    }
    FilterInvocation filterInvocation = (FilterInvocation) object;
    String method = filterInvocation.getHttpRequest().getMethod();

    return lookupAttributes(filterInvocation.getHttpRequest(), method);
  }

  /**
   * Performs the actual lookup of the relevant <tt>ConfigAttribute</tt>s for the given
   * <code>FilterInvocation</code>.
   * <p>
   * By default, iterates through the stored URL map and calls the
   * {@link UrlMatcher#pathMatchesUrl(Object path, String url)} method until a match is found.
   * 
   * @param httpServletRequest the URI to retrieve configuration attributes for
   * @param method the HTTP method (GET, POST, DELETE...), or null for any method.
   * 
   * @return the <code>ConfigAttribute</code>s that apply to the specified
   *         <code>FilterInvocation</code> or null if no match is found
   */
  public final Collection<ConfigAttribute> lookupAttributes(HttpServletRequest httpServletRequest, String method) {

    // Obtain the map of request patterns to attributes for this method and
    // lookup the url.
    Collection<ConfigAttribute> attributes = extractMatchingAttributes(httpServletRequest, httpMethodMap.get(method));

    // If no attributes found in method-specific map, use the general one
    // stored under the null key
    if (attributes == null) {
      attributes = extractMatchingAttributes(httpServletRequest, httpMethodMap.get(null));
    }

    return attributes;
  }

  private Collection<ConfigAttribute> extractMatchingAttributes(HttpServletRequest httpServletRequest,
      Map<Object, Collection<ConfigAttribute>> map) {
    if (map == null) {
      return null;
    }

    final boolean debug = logger.isDebugEnabled();

    Collection<ConfigAttribute> ccList = null;
    for (Map.Entry<Object, Collection<ConfigAttribute>> entry : map.entrySet()) {
      Object p = entry.getKey();
      RequestMatcher requestMatcher = new AntPathRequestMatcher(p.toString());
      boolean matched = requestMatcher.matches(httpServletRequest);
      if (debug) {
        logger.debug("Candidate is: '" + httpServletRequest + "'; pattern is " + p + "; matched=" + matched);
      }
      // lqh edit 20140916，有可能多个配置匹配上了，都需要返回
      if (matched) {
        ccList = ccList == null ? new ArrayList<ConfigAttribute>() : ccList;
        ccList.addAll(entry.getValue());
      }
    }
    return ccList;
  }

  public boolean supports(Class<?> clazz) {
    return FilterInvocation.class.isAssignableFrom(clazz);
  }


  public void setStripQueryStringFromUrls(boolean stripQueryStringFromUrls) {
    this.stripQueryStringFromUrls = stripQueryStringFromUrls;
  }

  @Override
  public void afterPropertiesSet() throws Exception {

    LinkedHashMap<String, String> srcMap = null;

    try {
      srcMap = resourceDetailService.getRequestMap();
    } catch (Exception e) {
      logger.error("获得权限资源错误", e);
    }

    for (Map.Entry<String, String> entry : srcMap.entrySet()) {

      Collection<ConfigAttribute> attrs = new ArrayList<ConfigAttribute>(); // entry.getValue()
      String[] auths = StringUtils.split(entry.getValue(), ',');

      for (String auth : auths) {
        attrs.add(new SecurityConfig(auth));
      }
      addSecureUrl(entry.getKey(), null, attrs);

    }

  }
}
