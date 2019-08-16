package com.smate.web.mobile.controller.web.pub;

import java.net.URLEncoder;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.Assert;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.smate.core.base.utils.mobile.SmateMobileUtils;
import com.smate.core.base.utils.wechat.WeChatUtilsService;
import com.smate.web.mobile.wechat.base.vo.WechatBaseVO;

/**
 * 微信基础的controller 主要是为了构建 微信链接的 票据
 * 
 * @author aijiangbin
 * @date 2018年9月4日
 */
public class WeChatBaseController {

  private Logger logger = LoggerFactory.getLogger(getClass());
  private WechatBaseVO wechatBaseVO = new WechatBaseVO();
  @Value("${wechat.appid}")
  private String appId;
  @Value("${domainMobile}")
  private String domain;
  @Value("${domainMobileWithoutScheme}")
  private String domainMobileWithoutScheme;

  @Autowired
  private WeChatUtilsService weChatUtilsService;

  // 处理微信js-api票据
  public void handleWxJsApiTicket(String url) throws Exception {
    Map<String, String> map = weChatUtilsService.handleWxJsApiTicket(url);
    Assert.notNull(map.get("nonceStr"));
    Assert.notNull(map.get("timestamp"));
    Assert.notNull(map.get("signature"));
    wechatBaseVO.setNonceStr(map.get("nonceStr"));
    wechatBaseVO.setTimestamp(map.get("timestamp"));
    wechatBaseVO.setSignature(map.get("signature"));
    wechatBaseVO.setAppId(this.appId);
  }

  /**
   * 是否是在微信端浏览器里面请求的
   * 
   * @return
   */
  public boolean isWechatBrowser(HttpServletRequest request) {
    String userAgent = request.getHeader("User-Agent");
    // 是否是移动端请求---PC端也有微信
    // boolean isMobileRequest =
    // SmateMobileUtils.isMobileBrowser(userAgent);
    // 是否是微信端请求
    return SmateMobileUtils.isWeChatBrowser(userAgent);
  }

  /**
   * 构建微信端数据信息
   * 
   * @param currentUrl
   * @throws Exception
   */
  public void buildWeChatData(String currentUrl, HttpServletRequest request) {
    try {
      if (isWechatBrowser(request)) {
        if (getSession().getAttribute("wxOpenId") != null) {
          wechatBaseVO.setWxOpenId(getSession().getAttribute("wxOpenId").toString());
        }
        this.handleWxJsApiTicket(this.getRequestDomain() + currentUrl + this.handleRequestParams(request));
      }
    } catch (Exception e) {
      logger.error("构建微信端数据信息出错", e);
    }
  }

  /**
   * 构建微信端数据信息
   * 
   * @param currentUrl
   * @throws Exception
   */
  public WechatBaseVO buildWeChatData(String currentUrl) {
    try {
      HttpServletRequest request = getRequest();
      if (request != null && isWechatBrowser(request)) {
        HttpSession session = request.getSession();
        if (session.getAttribute("wxOpenId") != null) {
          wechatBaseVO.setWxOpenId(session.getAttribute("wxOpenId").toString());
        }
        this.handleWxJsApiTicket(currentUrl);
      }
      wechatBaseVO.setDomain(this.domain);
    } catch (Exception e) {
      logger.error("构建微信签名信息出错", e);
    }
    return wechatBaseVO;
  }

  /**
   * 构建微信签名
   * 
   * @return
   */
  public WechatBaseVO buildWeChatData() {
    try {
      HttpServletRequest request = getRequest();
      if (request != null && isWechatBrowser(request)) {
        HttpSession session = request.getSession();
        if (session.getAttribute("wxOpenId") != null) {
          wechatBaseVO.setWxOpenId(session.getAttribute("wxOpenId").toString());
        }
        String proto = request.getHeader("X-Forwarded-Proto");
        proto = StringUtils.isNotBlank(proto) ? proto + "://" : "https://";
        this.handleWxJsApiTicket(
            proto + domainMobileWithoutScheme + request.getServletPath() + this.handleRequestParams(request));
      }
      wechatBaseVO.setDomain(this.domain);
    } catch (Exception e) {
      logger.error("构建微信签名信息出错", e);
    }
    return wechatBaseVO;
  }

  /**
   * 处理微信js-api票据所需参数
   * 
   * jsapi签名与浏览器地址内容一致即可
   * 
   * @throws Exception
   */
  public String handleRequestParams(HttpServletRequest request) throws Exception {
    Map<String, String[]> paramMap = request.getParameterMap();
    StringBuffer sb = new StringBuffer();
    sb.append("?");
    if (MapUtils.isNotEmpty(paramMap)) {
      for (String key : paramMap.keySet()) {
        String[] values = paramMap.get(key);
        sb.append(key + "=");
        // 只考虑第一个值
        if (values != null && values.length > 0) {
          sb.append(URLEncoder.encode(values[0], "utf-8"));
        }
        sb.append("&");
      }
    }
    return sb.toString().substring(0, sb.toString().length() - 1);
  }

  public static HttpSession getSession() {
    HttpSession session = null;
    try {
      session = getRequest().getSession();
    } catch (Exception e) {
    }
    return session;
  }

  public static HttpServletRequest getRequest() {
    ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
    return attrs.getRequest();
  }

  public static String getHttpReferer() {
    String reffer = getRequest().getHeader("Referer");
    return reffer;
  }

  public WeChatUtilsService getWeChatUtilsService() {
    return weChatUtilsService;
  }

  public void setWeChatUtilsService(WeChatUtilsService weChatUtilsService) {
    this.weChatUtilsService = weChatUtilsService;
  }

  public String getDomain() {
    return domain;
  }

  public void setDomain(String domain) {
    this.domain = domain;
  }

  public WechatBaseVO getWechatBaseVO() {
    return wechatBaseVO;
  }

  public void setWechatBaseVO(WechatBaseVO wechatBaseVO) {
    this.wechatBaseVO = wechatBaseVO;
  }

  /**
   * 根据request获取域名如http://test.schloarmate.com
   * 
   * @return
   */
  public String getRequestDomain() {
    StringBuffer url = getRequest().getRequestURL();
    String contextDomai = url.delete(url.length() - getRequest().getRequestURI().length(), url.length()).toString();
    return contextDomai;
  }

}
