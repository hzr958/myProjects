package com.smate.core.web.sns.action.shorturl;

import java.util.Date;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.opensymphony.xwork2.ActionSupport;
import com.smate.core.base.utils.json.JacksonUtils;
import com.smate.core.base.utils.mobile.SmateMobileUtils;
import com.smate.core.base.utils.model.shorturl.OpenShortUrlUseLog;
import com.smate.core.base.utils.security.SecurityUtils;
import com.smate.core.base.utils.struts2.Struts2Utils;
import com.smate.core.web.sns.shorturl.service.ShortUrlService;
import com.smate.core.web.sns.shorturl.service.ShortUrlUseLogService;

/**
 * 
 * 
 * @author tsz
 *
 */

@Results({@Result(name = "overdue", location = "/common/overdue.jsp"),

    @Result(name = "G", params = {"actionName", "main", "namespace", "/groupweb/grpinfo/outside", "method", "mainGrp"},
        type = "chain"),
    @Result(name = "P",
        params = {"actionName", "homepage", "namespace", "/psnweb/outside", "method", "psnOutsideHomepage"},
        type = "chain"),
    @Result(name = "PM", params = {"actionName", "outhome", "namespace", "/psnweb/mobile", "method", "outhomepage"},
        type = "chain"),
    @Result(name = "F", params = {"actionName", "filedownload1", "namespace", "/fileweb", "method", "fileDownload1"},
        type = "chain"),
    @Result(name = "A",
        params = {"actionName", "details", "namespace", "/pubweb/outside", "method", "showPubDetailsOutside"},
        type = "chain"),
    @Result(name = "AI", params = {"actionName", "show", "namespace", "/pubweb/details", "method", "showPubDetails"},
        type = "chain"),
    @Result(name = "AT",
        params = {"actionName", "viewSimple", "namespace", "/pubweb/publication", "method", "viewSimple"},
        type = "chain"),
    @Result(name = "B",
        params = {"actionName", "details", "namespace", "/pubweb/outside", "method", "showPubDetailsOutside"},
        type = "chain"),
    @Result(name = "BI", params = {"actionName", "show", "namespace", "/pubweb/details", "method", "showPubDetails"},
        type = "chain"),
    @Result(name = "S",
        params = {"actionName", "pdwhdetails", "namespace", "/pubweb/outside", "method", "showOutsidePdwhPubDetails"},
        type = "chain"),
    @Result(name = "SI",
        params = {"actionName", "showpdwh", "namespace", "/pubweb/details", "method", "showPdwhPubDetails"},
        type = "chain"),
    @Result(name = "ST",
        params = {"actionName", "viewPdwhSimple", "namespace", "/pubweb/publication", "method", "viewPdwhSimple"},
        type = "chain")})
public class ShortUrlAction extends ActionSupport {

  private static final long serialVersionUID = 6670502031787644806L;
  private Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private ShortUrlService shortUrlService;
  @Autowired
  private ShortUrlUseLogService shortUrlUseLogService;
  @Value("${domainscm}")
  private String domainscm;
  @Value("${domainMobile}")
  private String domainMobile;

  private String realUrlParamet;


  /**
   * 群组成果站外 短地址 ,
   */
  @Action("/B/*")
  public String grpPubOutside() {
    try {
      String shortUrl = getShortUrl();
      // 掉接口 取参数 参数返回为空 就为 连接失效
      String shortUrlParamet = shortUrlService.getRealUrlParamet(shortUrl);
      if (shortUrlParamet == null) {
        return "overdue";
      }
      this.setRealUrlParamet(shortUrlParamet);
      // 记录使用日志
      useLog(shortUrl);
      if (SecurityUtils.getCurrentUserId() != null && SecurityUtils.getCurrentUserId() > 0L) {
        Struts2Utils.getResponse().sendRedirect(domainscm + "/BI/" + shortUrl);
        return null;
      }
    } catch (Exception e) {
      logger.error("个人成果站外 短地址 出错", e);
    }
    return "B";
  }

  /**
   * 群组成果站内短地址
   */
  @Action("/BI/*")
  public String grpPubInside() {
    try {
      String shortUrl = getShortUrl();
      // 掉接口 取参数 参数返回为空 就为 连接失效
      String shortUrlParamet = shortUrlService.getRealUrlParamet(shortUrl);
      if (shortUrlParamet == null) {
        return "overdue";
      }
      this.setRealUrlParamet(shortUrlParamet);
      // 记录使用日志
      useLog(shortUrl);
      if (SecurityUtils.getCurrentUserId() == null || SecurityUtils.getCurrentUserId() == 0L) {
        Struts2Utils.getResponse().sendRedirect(domainscm + "/B/" + shortUrl);
        return null;
      }
    } catch (Exception e) {
      logger.error("个人成果站外 短地址 出错", e);
    }
    return "BI";
  }

  /**
   * 个人成果站外 短地址 ,
   */
  @Action("/A/*")
  public String psnPubOutside() {
    try {
      String shortUrl = getShortUrl();
      // 掉接口 取参数 参数返回为空 就为 连接失效
      String shortUrlParamet = shortUrlService.getRealUrlParamet(shortUrl);
      if (shortUrlParamet == null) {
        return "overdue";
      }
      this.setRealUrlParamet(shortUrlParamet);
      // 记录使用日志
      useLog(shortUrl);
      if (SecurityUtils.getCurrentUserId() != null && SecurityUtils.getCurrentUserId() > 0L) {
        Struts2Utils.getResponse().sendRedirect(domainscm + "/AI/" + shortUrl);
        return null;
      }
    } catch (Exception e) {
      logger.error("个人成果站外 短地址 出错", e);
    }
    return "A";
  }

  /**
   * 个人成果站内短地址
   */
  @Action("/AI/*")
  public String psnPubInside() {
    try {
      String shortUrl = getShortUrl();
      // 掉接口 取参数 参数返回为空 就为 连接失效
      String shortUrlParamet = shortUrlService.getRealUrlParamet(shortUrl);
      if (shortUrlParamet == null) {
        return "overdue";
      }
      this.setRealUrlParamet(shortUrlParamet);
      // 记录使用日志
      useLog(shortUrl);
      if (SecurityUtils.getCurrentUserId() == null || SecurityUtils.getCurrentUserId() == 0L) {
        Struts2Utils.getResponse().sendRedirect(domainscm + "/A/" + shortUrl);
        return null;
      }
    } catch (Exception e) {
      logger.error("个人成果站外 短地址 出错", e);
    }
    return "AI";
  }

  /**
   * 个人成果站内短地址(主要是给业务系统 简单版用)
   */
  @Action("/AT/*")
  public String psnPubForSimple() {
    try {
      String shortUrl = getShortUrl();
      // 如果是AT 类型的短地址 后面的 标识最少是32位才能用
      if (shortUrl.length() >= 32) {
        // 掉接口 取参数 参数返回为空 就为 连接失效
        String shortUrlParamet = shortUrlService.getRealUrlParamet(shortUrl);
        if (shortUrlParamet == null) {
          return "overdue";
        }
        this.setRealUrlParamet(shortUrlParamet);
        // 记录使用日志
        useLog(shortUrl);
      } else {
        return "overdue";
      }
    } catch (Exception e) {
      logger.error("个人成果站外 短地址 出错", e);
    }
    return "AT";
  }

  /**
   * 基准库成果站外 短地址 ,
   */
  @Action("/S/*")
  public String fundPubOutside() {
    try {
      String shortUrl = getShortUrl();
      // 掉接口 取参数 参数返回为空 就为 连接失效
      String shortUrlParamet = shortUrlService.getRealUrlParamet(shortUrl);
      if (shortUrlParamet == null) {
        return "overdue";
      }
      this.setRealUrlParamet(shortUrlParamet);
      // 记录使用日志
      useLog(shortUrl);
      if (SecurityUtils.getCurrentUserId() != null && SecurityUtils.getCurrentUserId() > 0L) {
        Struts2Utils.getResponse().sendRedirect(domainscm + "/SI/" + shortUrl);
        return null;
      }
    } catch (Exception e) {
      logger.error("基金成果站外 短地址 出错", e);
    }
    return "S";
  }

  /**
   * 基准库成果站内短地址
   */
  @Action("/SI/*")
  public String fundPubInside() {
    try {
      String shortUrl = getShortUrl();
      // 掉接口 取参数 参数返回为空 就为 连接失效
      String shortUrlParamet = shortUrlService.getRealUrlParamet(shortUrl);
      if (shortUrlParamet == null) {
        return "overdue";
      }
      this.setRealUrlParamet(shortUrlParamet);
      // 记录使用日志
      useLog(shortUrl);
      if (SecurityUtils.getCurrentUserId() == null || SecurityUtils.getCurrentUserId() == 0L) {
        Struts2Utils.getResponse().sendRedirect(domainscm + "/S/" + shortUrl);
        return null;
      }
    } catch (Exception e) {
      logger.error("基金成果站外 短地址 出错", e);
    }
    return "SI";
  }

  /**
   * 基准库成果站内短地址 (主要提供业务系统的简单页面用)
   */
  @Action("/ST/*")
  public String pdwhPubForSimple() {
    try {
      String shortUrl = getShortUrl();
      // 掉接口 取参数 参数返回为空 就为 连接失效
      String shortUrlParamet = shortUrlService.getRealUrlParamet(shortUrl);
      if (shortUrlParamet == null) {
        return "overdue";
      }
      this.setRealUrlParamet(shortUrlParamet);
      // 记录使用日志
      useLog(shortUrl);

    } catch (Exception e) {
      logger.error("基准库成果站外 短地址 出错", e);
    }
    return "ST";
  }

  /**
   * 群组站外 短地址 ,
   */
  @Action("/G/*")
  public String grpOutside() {
    try {
      String shortUrl = getShortUrl();
      // 掉接口 取参数 参数返回为空 就为 连接失效
      String shortUrlParamet = shortUrlService.getRealUrlParamet(shortUrl);
      if (shortUrlParamet == null) {
        return "overdue";
      }
      // 是否是移动端请求
      String userAgent = Struts2Utils.getRequest().getHeader("User-Agent");
      boolean isMobileRequest = SmateMobileUtils.isMobileBrowser(userAgent);
      if (isMobileRequest) {
        String des3GrpId = (String) JacksonUtils.jsonToMap2(shortUrlParamet).get("des3GrpId");
        Struts2Utils.getResponse().sendRedirect(domainMobile + "/grp/outside/main?des3GrpId=" + des3GrpId);
        return null;
      }
      this.setRealUrlParamet(shortUrlParamet);
      // 记录使用日志
      useLog(shortUrl);
    } catch (Exception e) {
      logger.error("群组站外 短地址出错", e);
    }
    return "G";
  }

  /**
   * 人员站外 短地址
   */
  @Action("/P/*")
  public String psnOutide() {
    try {
      String shortUrl = getShortUrl();
      String userAgent = Struts2Utils.getRequest().getHeader("User-Agent");
      // 是否是移动端请求
      boolean isMobileRequest = SmateMobileUtils.isMobileBrowser(userAgent);
      if (isMobileRequest) {
        Struts2Utils.getResponse().sendRedirect(domainMobile + "/PM/" + shortUrl);
        return null;
      }
      String shortUrlParamet = shortUrlService.getRealUrlParamet(shortUrl);
      if (shortUrlParamet == null) {
        return "overdue";
      }
      this.setRealUrlParamet(shortUrlParamet);
      // 记录使用日志
      useLog(shortUrl);
    } catch (Exception e) {
      logger.error("人员站外短地址出错", e);
    }
    return "P";
  }

  /**
   * 移动端人员站外 短地址
   */
  @Action("/PM/*")
  public String psnMobileOutside() {
    try {
      String shortUrl = getShortUrl();
      String userAgent = Struts2Utils.getRequest().getHeader("User-Agent");
      // 是否是移动端请求
      boolean isMobileRequest = SmateMobileUtils.isMobileBrowser(userAgent);
      if (!isMobileRequest) {
        Struts2Utils.getResponse().sendRedirect(domainscm + "/P/" + shortUrl);
        return null;
      }
      String shortUrlParamet = shortUrlService.getRealUrlParamet(shortUrl);
      if (shortUrlParamet == null) {
        return "overdue";
      }
      this.setRealUrlParamet(shortUrlParamet);
      // 记录使用日志
      useLog(shortUrl);
    } catch (Exception e) {
      logger.error("移动端人员站外短地址出错", e);
    }
    return "PM";
  }

  /**
   * 文件下载 短地址
   */
  @Action("/F/*")
  public String fileDownload() {
    String shortUrl = getShortUrl();
    String shortUrlParamet = shortUrlService.getRealUrlParamet(shortUrl);
    if (shortUrlParamet == null) {
      return "overdue";
    }
    this.setRealUrlParamet(shortUrlParamet);
    // 记录使用日志
    useLog(shortUrl);
    return "F";
  }

  /**
   * 获取短地址方法
   * 
   * @return
   */
  private String getShortUrl() {
    String uri = Struts2Utils.getRequest().getRequestURI();
    String str = uri.substring(uri.lastIndexOf("/") + 1);
    if (str.lastIndexOf("?") > 0) {
      return str.substring(0, uri.lastIndexOf("?"));
    }
    return str;
  }

  /**
   * 设置短地址映射的真实地址的参数
   * 
   * @param shortUrlParamet
   */
  private void setShortUrlParamet(String shortUrlParamet) {
    Struts2Utils.getRequest().setAttribute("SHORT_URL_FORM", shortUrlParamet);
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
    log.setUseIp(Struts2Utils.getRemoteAddr());
    shortUrlUseLogService.addUseLog(log);
  }

  public String getRealUrlParamet() {
    return realUrlParamet;
  }

  public void setRealUrlParamet(String realUrlParamet) {
    this.realUrlParamet = realUrlParamet;
  }

  public String getDomainscm() {
    return domainscm;
  }

  public void setDomainscm(String domainscm) {
    this.domainscm = domainscm;
  }



}
