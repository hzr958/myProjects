package com.smate.core.base.utils.struts2;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Stack;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Struts2后跳转工具类.
 * 
 * @author zk
 * @since 6.0.1
 */
public class Struts2MoveUtils {

  protected static final Logger logger = LoggerFactory.getLogger(Struts2MoveUtils.class);
  /**
   * 前一页session key.
   */
  private static String PREVIOUS_URL = "##struts2_pre_url";

  /**
   * 保持指定URL到session栈，如果为空，则默认存入当前URL.
   * 
   * @param request
   * @param ownerUrl
   */
  public static void cacheOwnerUrl(HttpServletRequest request, String ownerUrl) throws Exception {

    if (StringUtils.isBlank(ownerUrl)) {
      ownerUrl = getDefaultOwnerUrl(request);
    }
    if (ownerUrl.indexOf("?") > -1 && ownerUrl.indexOf("isBack=1") < 0) {
      ownerUrl = ownerUrl + "&isBack=1";
    } else if (ownerUrl.indexOf("isBack=1") < 0) {
      ownerUrl = ownerUrl + "?isBack=1";
    }
    HttpSession session = request.getSession();
    Stack<String> preUrls = getPreUrlStack(session);
    if (preUrls == null) {
      preUrls = new Stack<String>();
    }
    preUrls.push(ownerUrl);
    session.setAttribute(getKey(session), preUrls);
  }

  /**
   * 保持指定URL(包括当前的queryString)到session栈，如果为空，则默认存入当前URL.
   * 
   * @param request
   * @param ownerUrl
   */
  @SuppressWarnings({"rawtypes"})
  public static void cacheOwnerQueryStringUrl(HttpServletRequest request, String ownerUrl) throws Exception {
    if (StringUtils.isBlank(ownerUrl)) {
      ownerUrl = getDefaultOwnerUrl(request);

      if (ownerUrl.indexOf("?") > -1) {
        ownerUrl = ownerUrl.substring(0, ownerUrl.indexOf("?"));
      }
      String queryString = null;
      Map data = request.getParameterMap();
      Iterator itor = data.keySet().iterator();
      Map<String, String> param = new HashMap<String, String>();
      while (itor.hasNext()) {

        String key = String.valueOf(itor.next());
        logger.debug("####" + key);
        if ("forwardUrl".equals(key) || "ownerUrl".equals(key))
          continue;
        // 属性值
        String[] val = (String[]) data.get(key);
        if (val != null && val.length > 0) {
          if (StringUtils.isNotBlank(val[0]) && val[0].length() < 300 && !"undefined".equals(val[0]))
            param.put(key, java.net.URLEncoder.encode(val[0], "utf-8"));
        }
      }

      if (param.size() > 0) {

        Iterator<String> itor2 = param.keySet().iterator();
        while (itor2.hasNext()) {
          String key = String.valueOf(itor2.next());
          String value = ObjectUtils.toString(param.get(key));
          if (queryString == null) {
            queryString = key + "=" + value;
          } else {
            queryString += "&" + key + "=" + value;
          }

        }
      }

      if (queryString != null && ownerUrl.indexOf("?") == -1) {
        ownerUrl += "?" + queryString;
      } else if (queryString != null && ownerUrl.endsWith("?")) {
        ownerUrl += queryString;
      } else if (queryString != null) {
        ownerUrl += "&" + queryString;

      }
    }
    Struts2MoveUtils.cacheOwnerUrl(request, ownerUrl);
  }

  @SuppressWarnings({"rawtypes"})
  public static void cacheOwnerQueryStringUrlNsfc(HttpServletRequest request, String ownerUrl) throws Exception {
    String ownerParam = "";
    if (StringUtils.isBlank(ownerUrl)) {
      ownerUrl = getDefaultOwnerUrl(request);

      if (ownerUrl.indexOf("?") > -1) {
        ownerParam = ownerUrl.substring(ownerUrl.indexOf("?") + 1, ownerUrl.length());
        ownerUrl = ownerUrl.substring(0, ownerUrl.indexOf("?"));
      }

      String queryString = null;
      Map data = request.getParameterMap();
      Iterator itor = data.keySet().iterator();
      Map<String, String> param = new HashMap<String, String>();

      if (StringUtils.isNotBlank(ownerParam)) {
        String[] ownerParamArray = ownerParam.split("&");
        for (String paramStr : ownerParamArray) {
          if (StringUtils.isNotBlank(paramStr)) {
            String[] paramArray = paramStr.split("=");
            param.put(paramArray[0], paramArray[1]);
          }
        }
      }
      while (itor.hasNext()) {

        String key = String.valueOf(itor.next());
        logger.debug("####" + key);
        if ("forwardUrl".equals(key) || "ownerUrl".equals(key))
          continue;
        // 属性值
        String[] val = (String[]) data.get(key);
        if (val != null && val.length > 0) {
          if (StringUtils.isNotBlank(val[0]) && val[0].length() < 300 && !"undefined".equals(val[0]))
            param.put(key, java.net.URLEncoder.encode(val[0], "utf-8"));
        }
      }

      if (param.size() > 0) {

        Iterator<String> itor2 = param.keySet().iterator();
        while (itor2.hasNext()) {
          String key = String.valueOf(itor2.next());
          String value = ObjectUtils.toString(param.get(key));
          if (queryString == null) {
            queryString = key + "=" + value;
          } else {
            queryString += "&" + key + "=" + value;
          }

        }
      }

      if (queryString != null && ownerUrl.indexOf("?") == -1) {
        ownerUrl += "?" + queryString;
      } else if (queryString != null && ownerUrl.endsWith("?")) {
        ownerUrl += queryString;
      } else if (queryString != null) {
        ownerUrl += "&" + queryString;

      }
    }
    Struts2MoveUtils.cacheOwnerUrl(request, ownerUrl);
  }

  /**
   * 获取session中的前一个URL，但不删除前一个URL.
   * 
   * @param session
   * @param defaultUrl
   * 
   * @return
   */
  public static String getPreUrl(HttpSession session, String defaultUrl) throws Exception {
    Stack<String> preUrls = getPreUrlStack(session);
    if (preUrls != null && preUrls.size() > 0) {
      return preUrls.peek();
    } else {
      return defaultUrl;
    }
  }

  /**
   * 获取session中的前一个URL，且删除前一个URL.
   * 
   * @param session
   * @param defaultUrl
   * 
   * @return
   */
  public static String backPreUrl(HttpSession session, String defaultUrl) throws Exception {

    Stack<String> preUrls = getPreUrlStack(session);
    if (preUrls != null && preUrls.size() > 0) {
      String url = preUrls.pop();
      return url;// java.net.URLDecoder.decode(url, "utf-8");
    } else {
      return defaultUrl;
    }
  }

  @SuppressWarnings("unchecked")
  public static Stack<String> getPreUrlStack(HttpSession session) throws Exception {

    return (Stack<String>) session.getAttribute(getKey(session));
  }

  /**
   * 清空上一页数据.
   * 
   * @param session
   */
  public static void reomvePreUrl(HttpSession session) throws Exception {

    session.removeAttribute(getKey(session));
  }

  /**
   * 获取当前请求的前一个URL路径，如果为空，则获取当前URI.
   * 
   * @param request
   * @return
   */
  public static String getDefaultOwnerUrl(HttpServletRequest request) {

    String preUrl = request.getHeader("Referer");
    if (preUrl == null) {
      preUrl = request.getRequestURI().substring(request.getContextPath().length());
    }
    return preUrl;
  }

  /**
   * 获取缓存KEY.
   * 
   * @param session
   * @return
   */
  public static String getKey(HttpSession session) {

    return PREVIOUS_URL + session.getId();
  }
}
