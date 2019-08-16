package com.smate.core.base.utils.struts2;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.struts2.ServletActionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import com.smate.core.base.utils.json.JacksonUtils;
import com.smate.core.base.utils.model.AjaxResult;

/**
 * Struts2Utils工具类
 * 
 * @author zk
 *
 */
public class Struts2Utils {

  // header 常量定义
  private static final String ENCODING_PREFIX = "encoding";
  private static final String NOCACHE_PREFIX = "no-cache";
  private static final String ENCODING_DEFAULT = "UTF-8";
  private static final boolean NOCACHE_DEFAULT = true;

  private static Logger logger = LoggerFactory.getLogger(Struts2Utils.class);

  // 取得Request/Response/Session的简化函数 //

  /**
   * 取得HttpSession的简化方法.
   */
  public static HttpSession getSession() {
    return ServletActionContext.getRequest().getSession();
  }

  /**
   * 取得HttpRequest的简化方法.
   */
  public static HttpServletRequest getRequest() {
    return ServletActionContext.getRequest();
  }

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

    // String remoteAddIp = getRequest().getHeader("X-Real-IP");
    // if (StringUtils.isBlank(remoteAddIp)) {
    // remoteAddIp = getRequest().getRemoteAddr();
    // }
    // return remoteAddIp;
  }

  /**
   * 
   * 有时候getRequest()会报空指针异常 取得HttpRequest的真实远程ip 兼容反向代理
   * 因反向代理，会导致getRequest().getRemoteAddr()的ip为代理服务器地址
   * 
   */
  public static String getRemoteAddrByReq(HttpServletRequest req) {
    String ip = req.getHeader("X-Forwarded-For");
    if (StringUtils.isNotEmpty(ip) && !"unKnown".equalsIgnoreCase(ip)) {
      // 多次反向代理后会有多个ip值，第一个ip才是真实ip
      int index = ip.indexOf(",");
      if (index != -1) {
        return ip.substring(0, index);
      } else {
        return ip;
      }
    }
    ip = req.getHeader("X-Real-IP");
    if (StringUtils.isNotEmpty(ip) && !"unKnown".equalsIgnoreCase(ip)) {
      return ip;
    }
    return req.getRemoteAddr();
  }

  /**
   * 获取请求来源
   * 
   * @author ChuanjieHou
   * @date 2017年9月14日
   * @return
   */
  public static String getHttpReferer() {
    String reffer = getRequest().getHeader("Referer");
    return reffer;
  }

  /**
   * 取得HttpResponse的简化方法.
   */
  public static HttpServletResponse getResponse() {
    return ServletActionContext.getResponse();
  }

  /**
   * 取得Request Parameter的简化方法.
   */
  public static String getParameter(String name) {
    return getRequest().getParameter(name);
  }

  // 绕过jsp/freemaker直接输出文本的函数 //

  /**
   * 直接输出内容的简便函数.
   * 
   * eg. render("text/plain", "hello", "encoding:GBK"); render("text/plain", "hello",
   * "no-cache:false"); render("text/plain", "hello", "encoding:GBK", "no-cache:false");
   * 
   * @param headers 可变的header数组，目前接受的值为"encoding:"或"no-cache:",默认值分别为UTF-8和true.
   */
  public static void render(final String contentType, final String content, final String... headers) {
    try {
      // 分析headers参数
      String encoding = ENCODING_DEFAULT;
      boolean noCache = NOCACHE_DEFAULT;
      for (String header : headers) {
        String headerName = StringUtils.substringBefore(header, ":");
        String headerValue = StringUtils.substringAfter(header, ":");

        if (StringUtils.equalsIgnoreCase(headerName, ENCODING_PREFIX)) {
          encoding = headerValue;
        } else if (StringUtils.equalsIgnoreCase(headerName, NOCACHE_PREFIX)) {
          noCache = Boolean.parseBoolean(headerValue);
        } else
          throw new IllegalArgumentException(headerName + "不是一个合法的header类型");
      }

      HttpServletResponse response = ServletActionContext.getResponse();

      // 设置headers参数
      String fullContentType = contentType + ";charset=" + encoding;
      response.setContentType(fullContentType);
      if (noCache) {
        response.setHeader("Pragma", "No-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0);
      }

      response.getWriter().write(content);
      response.getWriter().flush();

    } catch (IOException e) {
      logger.error(e.getMessage(), e);
    }
  }

  /**
   * 直接输出内容的简便函数带HttpServletResponse.
   * 
   * eg. render("text/plain", "hello", "encoding:GBK"); render("text/plain", "hello",
   * "no-cache:false"); render("text/plain", "hello", "encoding:GBK", "no-cache:false");
   * 
   * @param headers 可变的header数组，目前接受的值为"encoding:"或"no-cache:",默认值分别为UTF-8和true.
   */
  public static void renderwithResponse(HttpServletResponse response, final String contentType, final String content,
      final String... headers) {
    try {
      // 分析headers参数
      String encoding = ENCODING_DEFAULT;
      boolean noCache = NOCACHE_DEFAULT;
      for (String header : headers) {
        String headerName = StringUtils.substringBefore(header, ":");
        String headerValue = StringUtils.substringAfter(header, ":");

        if (StringUtils.equalsIgnoreCase(headerName, ENCODING_PREFIX)) {
          encoding = headerValue;
        } else if (StringUtils.equalsIgnoreCase(headerName, NOCACHE_PREFIX)) {
          noCache = Boolean.parseBoolean(headerValue);
        } else
          throw new IllegalArgumentException(headerName + "不是一个合法的header类型");
      }

      // 设置headers参数
      String fullContentType = contentType + ";charset=" + encoding;
      response.setContentType(fullContentType);
      if (noCache) {
        response.setHeader("Pragma", "No-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0);
      }

      response.getWriter().write(content);
      response.getWriter().flush();

    } catch (IOException e) {
      logger.error(e.getMessage(), e);
    }
  }

  /**
   * 直接输出文本.
   * 
   * @see #render(String, String, String...)
   */
  public static void renderText(final String text, final String... headers) {
    render("text/plain", text, headers);
  }

  /**
   * 直接输出HTML.
   * 
   * @see #render(String, String, String...)
   */
  public static void renderHtml(final String html, final String... headers) {
    render("text/html", html, headers);
  }

  /**
   * 直接输出XML.
   * 
   * @see #render(String, String, String...)
   */
  public static void renderXml(final String xml, final String... headers) {
    render("text/xml", xml, headers);
  }

  /**
   * 直接输出JSON.
   * 
   * @param string json字符串.
   * @see #render(String, String, String...)
   */
  public static void renderJson(final String string, final String... headers) {

    render("application/json", string, headers);
  }

  /**
   * 直接输出JSON.
   * 
   * @param map Map对象,将被转化为json字符串.
   * @see #render(String, String, String...)
   */
  @SuppressWarnings("rawtypes")
  public static void renderJson(final Map map, final String... headers) {
    String jsonString = JacksonUtils.jsonObjectSerializer(map);
    renderJson(jsonString, headers);
  }

  /**
   * 直接输出JSON(去除null值为"").
   * 
   * @param map Map对象,将被转化为json字符串.
   * @see #render(String, String, String...)
   */
  @SuppressWarnings("rawtypes")
  public static void renderJsonNoNull(final Map map, final String... headers) {
    String jsonString = JacksonUtils.jsonObjectSerializerNoNull(map);
    renderJson(jsonString, headers);
  }

  /**
   * 直接输出JSON.
   * 
   * @param object Java对象,将被转化为json字符串.
   * @see #render(String, String, String...)
   */
  public static void renderJson(final Object object, final String... headers) {
    String jsonString = JacksonUtils.jsonObjectSerializer(object);
    renderJson(jsonString, headers);
  }

  /**
   * 直接输出JSON.
   * 
   * @param list Java对象list,将被转化为json字符串.
   * @see #render(String, String, String...)
   */
  public static void renderJson(final List<Object> list, final String... headers) {
    String jsonString = JacksonUtils.jsonObjectSerializer(list);
    renderJson(jsonString, headers);
  }

  /**
   * 直接输出JSON.
   * 
   * @param list Java对象list,将被转化为json字符串.
   * @see #render(String, String, String...)
   */
  @SuppressWarnings("rawtypes")
  public static void renderJsonList(final List list, final String... headers) {
    String jsonString = JacksonUtils.jsonObjectSerializer(list);
    render("application/json", jsonString, headers);
  }

  /**
   * 
   * @param request
   * @return
   */
  public static String getAppServerPath(HttpServletRequest request) {

    String scheme = request.getScheme();
    String server = request.getServerName();
    int port = request.getServerPort();
    String context = request.getContextPath();

    if (port == 80) {
      return String.format("%s://%s%s", scheme, server, context);
    }
    return String.format("%s://%s:%s%s", scheme, server, port, context);
  }

  public static ServletContext getServletContext() {
    return ServletActionContext.getServletContext();
  }

  /**
   * 获取指定名称的cookie
   * 
   * @param request
   * @param name
   * @return
   */
  public static Cookie getCookie(HttpServletRequest request, String name) {
    Assert.notNull(request, "Request must not be null");
    Cookie cookies[] = request.getCookies();
    if (cookies != null) {
      for (Cookie cookie : cookies) {
        if (name.equals(cookie.getName())) {
          return cookie;
        }
      }
    }
    return null;
  }

  /**
   * 获取指定cookie的值
   * 
   * @param request
   * @param name
   * @return
   */
  public static String getCookieVal(HttpServletRequest request, String name) {
    Assert.notNull(request, "Request must not be null");
    Cookie cookies[] = request.getCookies();
    if (cookies != null) {
      for (Cookie cookie : cookies) {
        if (name.equals(cookie.getName())) {
          return cookie.getValue();
        }
      }
    }
    return "";
  }


  /**
   * 设置cookie
   * 
   * @param response
   * @param cookie
   * @param date
   * @param domain
   * @param path
   * @throws Exception
   */
  public static void setCookie(HttpServletResponse response, Cookie cookie, String val, String maxAge, String domain,
      String path) throws Exception {
    cookie.setPath(path);
    cookie.setDomain(domain);
    cookie.setValue(val);
    cookie.setMaxAge(NumberUtils.toInt(maxAge, -1));
    response.addCookie(cookie);
  }


  /**
   * 设置cookie
   * 
   * @param response
   * @param cookie
   * @param date
   * @param domain
   * @param path
   * @throws Exception
   */
  public static void setCookie(HttpServletResponse response, Cookie cookie, String maxAge, String domain, String path)
      throws Exception {
    cookie.setPath(path);
    cookie.setDomain(domain);
    cookie.setMaxAge(NumberUtils.toInt(maxAge, -1));
    response.addCookie(cookie);
  }

  /**
   * 直接输出状态为success的ResultMap，只携带提示信息。输出结果示例：<br>
   * {status="success", msg = "请求成功！"}
   * 
   * @author ChuanjieHou
   * @date 2017年9月18日
   * @param msg 提示信息
   */
  public static void renderSuccessResult(String msg) {
    AjaxResult resultMap = new AjaxResult(true, msg);
    renderJson(resultMap);
  }

  /**
   * 直接输出状态为success的ResultMap，携带提示信息和Map结构的数据信息。输出结果示例：<br>
   * {status="success", msg="成功获取数据！", data={id="1", name="apple", price="2.8"}}
   * 
   * @author ChuanjieHou
   * @date 2017年9月18日
   * @param msg 提示信息
   * @param data 数据信息
   */
  public static void renderSuccessResult(String msg, Map<?, ?> data) {
    AjaxResult resultMap = new AjaxResult(true, msg, data);
    renderJson(resultMap);
  }

  /**
   * 直接输出状态为success的ResultMap，携带提示信息和任意类型的数据信息。输出结果示例：<br>
   * {status="success", msg="成功获取数据！", data={id=1, name="apple", price="2.8"}}
   * 
   * @author ChuanjieHou
   * @date 2017年9月18日
   * @param msg 提示信息
   * @param data 数据信息
   */
  public static void renderSuccessResult(String msg, Object data) {
    AjaxResult resultMap = new AjaxResult(true, msg, data);
    renderJson(resultMap);
  }

  /**
   * 直接输出状态为error的ResultMap，携带提示信息。输出结果示例：<br>
   * {status="error", msg="请求数据失败！"}
   * 
   * @author ChuanjieHou
   * @date 2017年9月18日
   * @param msg 提示信息
   */
  public static void renderErrorResult(String msg) {
    AjaxResult resultMap = new AjaxResult(false, msg);
    renderJson(resultMap);
  }

  public static void redirect(String url) throws IOException {
    try {
      getResponse().sendRedirect(url);
    } catch (IOException e) {
      logger.error("重定向到：{} 失败！", url, e);
      throw e;
    }
  }

  /**
   * 获取当前请求带协议的域名
   * 
   * @author xr
   * @date 2019年4月25日
   * 
   */
  public static String getRequestDomin() {
    String tempUri = Struts2Utils.getRequest().getRequestURI();
    StringBuffer tempUrl = Struts2Utils.getRequest().getRequestURL();
    String insDomin = Struts2Utils.getRequest().getRequestURL()
        .delete(tempUrl.length() - tempUri.length(), tempUrl.length()).toString();
    return insDomin;
  }


  /**
   * 优先从请求头中获取sessionId，因为有时候获取到Session是空的， 用Struts2Utils.getSession()会新建session导致sessionId会改变
   *
   * @param request
   * @return
   */
  public static String getRequestSessionId() {
    HttpServletRequest request = getRequest();
    String cookieStr = request.getHeader("Cookie");
    String sessionId = "";
    if (StringUtils.isNotBlank(cookieStr)) {
      String[] cookies = cookieStr.split(";");
      if (ArrayUtils.isNotEmpty(cookies)) {
        for (String str : cookies) {
          if (str.contains("JSESSIONID=")) {
            sessionId = str.replace("JSESSIONID=", "").trim();
            break;
          }
        }
      }
      if (StringUtils.isBlank(sessionId)) {
        sessionId = request.getSession().getId();
      }
    }
    return sessionId;
  }
}
