package com.smate.core.base.utils.mobile;

import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;

/**
 * 移动端工具类
 *
 * @author wsn
 * @createTime 2017年6月6日 下午4:48:26
 *
 */
public class SmateMobileUtils {

  /**
   * 是否是移动端(包括PC微信端)
   * 
   * @param requestHeader 值是request.getHeader("user-agent")
   * @return
   */
  public static boolean isMobileBrowser(String requestHeader) {
    boolean isMobile = false;
    if (StringUtils.isNotBlank(requestHeader)) {
      requestHeader = requestHeader.toLowerCase();
    } else {
      return isMobile;
    }
    // 排除 苹果桌面系统
    if ((!requestHeader.contains("Windows NT") && !requestHeader.contains("Macintosh"))
        || (requestHeader.contains("Windows NT") && requestHeader.contains("compatible; MSIE 9.0;"))) {
      String regex =
          "ios|symbianos|windows mobile|windows ce|ucweb|rv:1.2.3.4|midp|android|webos|iphone|ipad|ipod|blackberry|iemobile|opera mini|windows phone|mqqbrowser";
      Pattern p = Pattern.compile(regex);
      isMobile = p.matcher(requestHeader.toLowerCase()).find();
    }
    return isMobile || isWeChatBrowser(requestHeader);
  }

  /**
   * 是否是微信端浏览器
   * 
   * @param requestHeader 值是request.getHeader("user-agent")
   * @return
   */
  public static boolean isWeChatBrowser(String requestHeader) {
    boolean isWeChatBrowser = false;
    if (StringUtils.isNotBlank(requestHeader)) {
      requestHeader = requestHeader.toLowerCase();
    } else {
      return isWeChatBrowser;
    }
    // 是微信浏览器
    if (requestHeader.indexOf("micromessenger") > 0) {
      isWeChatBrowser = true;
    }
    return isWeChatBrowser;
  }

  /**
   * 是否是IOS系统的
   * 
   * @param requestHeader
   * @return
   */
  public static boolean isIphone(String requestHeader) {
    boolean isIphone = false;
    if (StringUtils.isNotBlank(requestHeader)) {
      requestHeader = requestHeader.toLowerCase();
    } else {
      return isIphone;
    }
    String[] deviceArray = new String[] {"iphone", "ipod", "ipad"};
    if (!requestHeader.contains("Windows NT")
        || (requestHeader.contains("Windows NT") && requestHeader.contains("compatible; MSIE 9.0;"))) {
      // 排除 苹果桌面系统
      if (!requestHeader.contains("Windows NT") && !requestHeader.contains("Macintosh")) {
        for (String item : deviceArray) {
          if (requestHeader.contains(item)) {
            isIphone = true;
            break;
          }
        }
      }
    }
    return isIphone;
  }

  public static boolean isAndroid(String requestHeader) {
    boolean isAndroid = false;
    if (StringUtils.isNotBlank(requestHeader)) {
      requestHeader = requestHeader.toLowerCase();
    } else {
      return isAndroid;
    }
    // 是android端
    if (requestHeader.indexOf("android") > 0 || requestHeader.indexOf("adr") > 0) {
      isAndroid = true;
    }
    return isAndroid;
  }

  /**
   * 是否是移动端请求（包括PC微信端）
   * 
   * @param request
   * @return
   */
  public static boolean isMobileBrowser(HttpServletRequest request) {
    return isMobileBrowser(request.getHeader("user-agent")) || isWeChatBrowser(request);
  }

  /**
   * 是否是微信端请求
   * 
   * @param request
   * @return
   */
  public static boolean isWeChatBrowser(HttpServletRequest request) {
    return isWeChatBrowser(request.getHeader("user-agent"));
  }
}
