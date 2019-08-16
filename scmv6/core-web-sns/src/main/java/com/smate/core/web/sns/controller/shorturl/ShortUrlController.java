package com.smate.core.web.sns.controller.shorturl;

import com.opensymphony.xwork2.ActionSupport;
import com.smate.core.base.utils.json.JacksonUtils;
import com.smate.core.base.utils.model.shorturl.OpenShortUrlUseLog;
import com.smate.core.base.utils.security.SecurityUtils;
import com.smate.core.base.utils.spring.SpringUtils;
import com.smate.core.base.utils.string.StringUtils;
import com.smate.core.web.sns.shorturl.service.ShortUrlService;
import com.smate.core.web.sns.shorturl.service.ShortUrlUseLogService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 短地址映射 spring mvc
 * 
 * @author tsz
 *
 */
@Controller
public class ShortUrlController extends ActionSupport {

  private static final long serialVersionUID = 6670502031787644806L;
  private Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private ShortUrlService shortUrlService;
  @Autowired
  private ShortUrlUseLogService shortUrlUseLogService;
  @Value("${domainscm}")
  private String domainscm;

  /**
   * 群组成果站外 短地址 ,
   */
  @RequestMapping("/B/*")
  public String grpPubOutside() {
    Map<String, String> parametMap = new HashMap<String, String>();
    try {
      String shortUrl = getShortUrl();
      // 掉接口 取参数 参数返回为空 就为 连接失效
      String shortUrlParamet = shortUrlService.getRealUrlParamet(shortUrl);
      if (shortUrlParamet == null) {
        return "overdue";
      }
      parametMap = JacksonUtils.json2Map(shortUrlParamet);
      // 记录使用日志
      useLog(shortUrl);
      if (SecurityUtils.getCurrentUserId() != null && SecurityUtils.getCurrentUserId() > 0L) {
        return "redirect:" + domainscm + "/BI/" + shortUrl;
      }
    } catch (Exception e) {
      logger.error("个人成果站外 短地址 出错", e);
    }
    return "forward:/pub/outside/details?des3PubId=" + parametMap.get("des3PubId") + "&des3GrpId="
        + parametMap.get("des3GrpId");
  }

  /**
   * 群组成果站内短地址
   */
  @RequestMapping("/BI/*")
  public String grpPubInside() {
    Map<String, String> parametMap = new HashMap<String, String>();
    try {
      String shortUrl = getShortUrl();
      // 掉接口 取参数 参数返回为空 就为 连接失效
      String shortUrlParamet = shortUrlService.getRealUrlParamet(shortUrl);
      if (shortUrlParamet == null) {
        return "overdue";
      }
      parametMap = JacksonUtils.json2Map(shortUrlParamet);
      // 记录使用日志
      useLog(shortUrl);
      if (SecurityUtils.getCurrentUserId() == null || SecurityUtils.getCurrentUserId() == 0L) {
        return "redirect:" + domainscm + "/B/" + shortUrl;
      }
    } catch (Exception e) {
      logger.error("个人成果站外 短地址 出错", e);
    }
    return "forward:/pub/details?des3PubId=" + parametMap.get("des3PubId") + "&des3GrpId="
        + parametMap.get("des3GrpId");
  }

  /**
   * 个人成果站外 短地址 ,
   */
  @RequestMapping("/A/*")
  public String psnPubOutside() {
    String des3relationid = SpringUtils.getRequest().getParameter("des3relationid");
    Map<String, String> parametMap = new HashMap<String, String>();
    try {
      String shortUrl = getShortUrl();
      // 掉接口 取参数 参数返回为空 就为 连接失效
      String shortUrlParamet = shortUrlService.getRealUrlParamet(shortUrl);
      if (shortUrlParamet == null) {
        return "overdue";
      }
      parametMap = JacksonUtils.json2Map(shortUrlParamet);
      // 记录使用日志
      useLog(shortUrl);
      if (SecurityUtils.getCurrentUserId() != null && SecurityUtils.getCurrentUserId() > 0L) {
        shortUrl = StringUtils.isBlank(des3relationid) ? shortUrl : shortUrl + "?des3relationid=" + des3relationid;
        return "redirect:" + domainscm + "/AI/" + shortUrl;
      }
    } catch (Exception e) {
      logger.error("个人成果站外 短地址 出错", e);
    }
    return "forward:/pub/outside/details?des3PubId=" + parametMap.get("des3PubId");
  }

  /**
   * 个人成果站内短地址
   */
  @RequestMapping("/AI/*")
  public String psnPubInside() {
    Map<String, String> parametMap = new HashMap<String, String>();
    String des3relationid = SpringUtils.getRequest().getParameter("des3relationid");
    try {
      String shortUrl = getShortUrl();
      // 掉接口 取参数 参数返回为空 就为 连接失效
      String shortUrlParamet = shortUrlService.getRealUrlParamet(shortUrl);
      if (shortUrlParamet == null) {
        return "overdue";
      }
      parametMap = JacksonUtils.json2Map(shortUrlParamet);
      // 记录使用日志
      useLog(shortUrl);
      if (SecurityUtils.getCurrentUserId() == null || SecurityUtils.getCurrentUserId() == 0L) {
        return "redirect:" + domainscm + "/A/" + shortUrl;
      }
    } catch (Exception e) {
      logger.error("个人成果站外 短地址 出错", e);
    }
    return "forward:/pub/details?des3PubId=" + parametMap.get("des3PubId") + "&des3relationid=" + des3relationid;
  }

  /**
   * 个人成果站内短地址(主要是给业务系统 简单版用)
   */
  @RequestMapping("/AT/*")
  public String psnPubForSimple() {
    Map<String, String> parametMap = new HashMap<String, String>();
    try {
      String shortUrl = getShortUrl();
      // 如果是AT 类型的短地址 后面的 标识最少是32位才能用
      if (shortUrl.length() >= 32) {
        // 掉接口 取参数 参数返回为空 就为 连接失效
        String shortUrlParamet = shortUrlService.getRealUrlParamet(shortUrl);
        if (shortUrlParamet == null) {
          return "overdue";
        }
        parametMap = JacksonUtils.json2Map(shortUrlParamet);
        // 记录使用日志
        useLog(shortUrl);
      } else {
        return "overdue";
      }
    } catch (Exception e) {
      logger.error("个人成果站外 短地址 出错", e);
    }
    return "forward:/pub/details/viewSimple?des3PubId=" + parametMap.get("des3PubId");
  }


  /**
   * 基准库成果站外 短地址 ,
   */
  @RequestMapping("/S/*")
  public String fundPubOutside() {
    Map<String, String> parametMap = new HashMap<String, String>();
    String type = getParams();
    try {
      String shortUrl = getShortUrl();
      // 掉接口 取参数 参数返回为空 就为 连接失效
      String shortUrlParamet = shortUrlService.getRealUrlParamet(shortUrl);
      if (shortUrlParamet == null) {
        return "overdue";
      }
      parametMap = JacksonUtils.json2Map(shortUrlParamet);
      // 记录使用日志
      useLog(shortUrl);
      if (SecurityUtils.getCurrentUserId() != null && SecurityUtils.getCurrentUserId() > 0L) {
        String redirectUrl = "redirect:" + domainscm + "/SI/" + shortUrl;
        if (StringUtils.isNotBlank(type)) {
          redirectUrl = redirectUrl + "?type=" + type;
        }
        return redirectUrl;
      }
    } catch (Exception e) {
      logger.error("基金成果站外 短地址 出错", e);
    }
    String forwardUrl = "forward:/pub/outside/pdwhdetails?des3PubId=" + parametMap.get("des3PubId");
    if (StringUtils.isNotBlank(type)) {
      forwardUrl = forwardUrl + "&type=" + type;
    }
    return forwardUrl;
  }

  /**
   * 基准库成果站内短地址
   */
  @RequestMapping("/SI/*")
  public String fundPubInside() {
    Map<String, String> parametMap = new HashMap<String, String>();
    String type = getParams();
    try {
      String shortUrl = getShortUrl();
      // 掉接口 取参数 参数返回为空 就为 连接失效
      String shortUrlParamet = shortUrlService.getRealUrlParamet(shortUrl);
      if (shortUrlParamet == null) {
        return "overdue";
      }
      parametMap = JacksonUtils.json2Map(shortUrlParamet);
      // 记录使用日志
      useLog(shortUrl);
      if (SecurityUtils.getCurrentUserId() == null || SecurityUtils.getCurrentUserId() == 0L) {
        String redirectUrl = "redirect:" + domainscm + "/S/" + shortUrl;
        if (StringUtils.isNotBlank(type)) {
          redirectUrl = redirectUrl + "?type=" + type;
        }
        return redirectUrl;
      }
    } catch (Exception e) {
      logger.error("基金成果站外 短地址 出错", e);
    }

    String forwardUrl = "forward:/pub/details/pdwh?des3PubId=" + parametMap.get("des3PubId");
    if (StringUtils.isNotBlank(type)) {
      forwardUrl = forwardUrl + "&type=" + type;
    }
    return forwardUrl;
  }

  /**
   * 基准库成果站内短地址 (主要提供业务系统的简单页面用)
   */
  @RequestMapping("/ST/*")
  public String pdwhPubForSimple() {
    Map<String, String> parametMap = new HashMap<String, String>();
    try {
      String shortUrl = getShortUrl();
      // 掉接口 取参数 参数返回为空 就为 连接失效
      String shortUrlParamet = shortUrlService.getRealUrlParamet(shortUrl);
      if (shortUrlParamet == null) {
        return "overdue";
      }
      parametMap = JacksonUtils.json2Map(shortUrlParamet);
      // 记录使用日志
      useLog(shortUrl);

    } catch (Exception e) {
      logger.error("基准库成果站外 短地址 出错", e);
    }
    return "forward:/pub/details/viewPdwhSimple?des3PubId=" + parametMap.get("des3PubId");
  }


  /**
   * 获取短地址方法
   * 
   * @return
   */
  private String getShortUrl() {
    String uri = SpringUtils.getRequest().getRequestURI();
    String str = uri.substring(uri.lastIndexOf("/") + 1);
    if (str.lastIndexOf("?") > 0) {
      return str.substring(0, uri.lastIndexOf("?"));
    }
    return str;
  }

  private String getParams() {
    String type = SpringUtils.getRequest().getParameter("type");
    return type;
  }

  /**
   * 处理日志
   * 
   * @param shortUrl
   */
  private void useLog(String shortUrl) {
    // 记录访问日志
    OpenShortUrlUseLog log = new OpenShortUrlUseLog();
    log.setShortUrlParamet(shortUrl);
    log.setUseDate(new Date());
    Long currentUserId = SecurityUtils.getCurrentUserId();
    if (currentUserId == null || currentUserId == 0L) {
      log.setUsePsnId(1L);
    } else {
      log.setUsePsnId(currentUserId);
    }
    log.setUseIp(SpringUtils.getRemoteAddr());
    shortUrlUseLogService.addUseLog(log);
  }

}
