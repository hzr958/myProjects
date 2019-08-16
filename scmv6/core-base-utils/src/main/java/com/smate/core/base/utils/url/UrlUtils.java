package com.smate.core.base.utils.url;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.ServletActionContext;

/**
 * url utils.
 * 
 * @author xys
 *
 */
public final class UrlUtils {

  /**
   * Obtains the virtual context path.
   * 
   * @return
   */
  public static String getCtxPath() {
    return getCtxPath(ServletActionContext.getRequest());
  }

  /**
   * Obtains the virtual context path.
   * 
   * @param req
   * @return
   */
  public static String getCtxPath(HttpServletRequest req) {
    return getCtxPath(req.getServletPath());
  }

  /**
   * Obtains the virtual context path.
   * 
   * @param servletPath
   * @return
   */
  public static String getCtxPath(String servletPath) {
    if (servletPath == null || servletPath.length() == 0) {
      return "";
    }
    return servletPath.substring(servletPath.indexOf("/"), servletPath.indexOf("/", servletPath.indexOf("/") + 1));
  }

  public static String getUrlDomain(String url) {
    Pattern p = Pattern.compile("(?<=http://|//.)[^.]*?//.(com|cn|net|org|biz|info|cc|tv)", Pattern.CASE_INSENSITIVE);
    Matcher matcher = p.matcher(url);
    matcher.find();
    return matcher.group();
  }
}
