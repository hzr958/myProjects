package com.smate.center.oauth.action.login;

import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Random;
import java.util.regex.Pattern;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Actions;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.WebUtils;

import com.google.gson.Gson;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Preparable;
import com.smate.center.oauth.cache.OauthCacheService;
import com.smate.center.oauth.consts.OauthConsts;
import com.smate.center.oauth.service.mobile.MobileLoginService;
import com.smate.core.base.oauth.model.OauthLoginForm;
import com.smate.core.base.utils.constant.security.SecurityConstants;
import com.smate.core.base.utils.mobile.SmateMobileUtils;
import com.smate.core.base.utils.model.shorturl.OpenShortUrl;
import com.smate.core.base.utils.security.Des3Utils;
import com.smate.core.base.utils.string.ServiceUtil;
import com.smate.core.base.utils.struts2.Struts2Utils;

@Namespace("/")
@PropertySource("classspath:webToAppUrl.properties")
@Results({@Result(name = "reload", location = "index", type = "redirect"),
    @Result(name = "success", location = "/index.jsp"),
    @Result(name = "mobileIndex", location = "/WEB-INF/jsp/mobile/MobileIndex.jsp"),
    @Result(name = "redirectUrl", location = "${forwardUrl}", type = "redirect"),
    @Result(name = "SIE_login", location = "/WEB-INF/jsp/sie/SIE_index.jsp"),
    @Result(name = "SNS_login", location = "/WEB-INF/jsp/sns/V_SNS_index.jsp"),
    @Result(name = "app_download", location = "/WEB-INF/jsp/mobile/app_downloadpage.jsp")})
public class IndexAction extends ActionSupport implements ModelDriven<OauthLoginForm>, Preparable {

  private static final long serialVersionUID = -2700156214728128232L;
  private final Logger logger = LoggerFactory.getLogger(getClass());
  protected static final String OAUTH_LOGIN = "OAUTH_LOGIN";
  private Properties prop = new Properties();
  @Autowired
  private OauthCacheService oauthCacheService;

  private OauthLoginForm form;
  @Value("${domainscm}")
  private String domainscm;
  // 微信端跳转过来后的openId
  private String wxOpenId;
  // 微信端跳转过来后的URL，绑定后跳转回去用
  private String wxUrl;
  @Value("${domainMobile}")
  private String domainMobile;
  @Value("${domainMobileApp}")
  private String domainMobileApp;
  @Value("${domainrol}")
  private String domainrol;
  @Resource(name = "restTemplate")
  private RestTemplate restTemplate;
  @Autowired
  private MobileLoginService mobileLoginService;

  /**
   * 默认科研之友 登录页面
   * 
   * @return
   * @throws Exception
   */
  @Actions({@Action("/"), @Action("/oauth"), @Action("/oauth/index"), @Action("/oauth/sns/index"),
      @Action("/oauth/mobile/index")})
  public String index() {
    try {
      form.setRunEnv(System.getenv("RUN_ENV"));
      // TODO 如果sys 为sns wci 或者空 就根据浏览器信息 自动重新算sys 如果是移动端就是 wci 如果是pc 就是
      if ("SNS".equalsIgnoreCase(form.getSys()) || "WCI".equalsIgnoreCase(form.getSys())
          || StringUtils.isBlank(form.getSys())) {
        rebuildSys(form);
      }
      // 是否需要验证码
      Object errorNum = oauthCacheService.get(OauthConsts.OAUTH_LOGIN_ERROR_NUM, Struts2Utils.getSession().getId());
      if (errorNum != null && (int) errorNum >= 3) {
        form.setNeedValidateCode("1");
      }
      // 科研之友pc端首页// sns
      if ("SNS".equalsIgnoreCase(form.getSys())) {
        return dealSnsLoginIndex();
      }
      if ("WCI".equalsIgnoreCase(form.getSys())) {
        if (StringUtils.isNotBlank(form.getMsg())) {
          form.setMsg(Des3Utils.decodeFromDes3(form.getMsg()));// 提示错误信息
        }
        return dealMobileLoginIndex();
      }
      // 如果已经登录过 就直接 进入 默认首页
      Object uDetails = oauthCacheService.get(SecurityConstants.USER_DETAILS_INFO_CACHE,
          Struts2Utils.getRequest().getSession().getId());
      String defaultUrl =
          domainMobile + "/dynweb/mobile/dynshow" + (StringUtils.isNotBlank(wxOpenId) ? "?wxOpenId=" + wxOpenId : "");
      String defaultService = Des3Utils.encodeToDes3(defaultUrl);
      if (uDetails != null) {
        Struts2Utils.getResponse().sendRedirect(defaultUrl);
        return null;
      }
      // 没有目标地址 设置默认目标地址
      if (StringUtils.isBlank(form.getService())) {
        form.setService(defaultService);
      }
    } catch (Exception e) {
      logger.error("访问登录页面出错", e);
    }
    if ("SIE".equals(form.getSys())) {
      return "SIE_login";
    }
    form.setSysDomain(domainMobile);
    return "mobileIndex";
  }

  /**
   * 解析获取跳转APP的地址
   * 
   */
  @Action("/oauth/openApp")
  public void awakenApp() {
    HttpServletRequest request = Struts2Utils.getRequest();
    HttpServletResponse response = Struts2Utils.getResponse();
    String target = "dynamicpage"; // 默认跳转个人首页
    String repUrl = domainMobileApp + "oauth/download";
    String model = "";
    String serviceUrl = form.getService();
    try {
      if (serviceUrl.contains("service")) { // 未登录状态跳转,含有service参数
        serviceUrl = Des3Utils.decodeFromDes3(serviceUrl.replaceAll(".*service=", ""));
      }
      if (StringUtils.isNoneBlank(serviceUrl)) {
        String reqUri = serviceUrl.replaceAll(".*scholarmate\\.com", "");
        if (reqUri.contains("/PM")) {// 个人主页短地址
          String shortUrl;
          if (reqUri.contains("?")) {// 是否有带参数
            shortUrl = reqUri.substring(reqUri.indexOf("/PM/") + 4, reqUri.indexOf("?"));
          } else {
            shortUrl = reqUri.substring(reqUri.indexOf("/PM/") + 4);
          }
          OpenShortUrl openShortUrl = mobileLoginService.getOpenShortUrlByShortUrl(shortUrl);
          Gson gson = new Gson();
          Map<String, String> parametMap = gson.fromJson(openShortUrl.getRealUrlParamet(), Map.class);
          repUrl = repUrl + "?target=personhomepage&des3ViewPsnId="
              + parametMap.get("des3PsnId").replaceAll("des3PsnId", "des3ViewPsnId");
          response.sendRedirect(repUrl);
          return;
        } else {
          if (reqUri.contains("?")) {
            model = StringEscapeUtils.unescapeHtml4(reqUri.substring(reqUri.indexOf("?") + 1));
            target = analysisModel(reqUri.substring(0, reqUri.indexOf("?")));
          } else {
            target = analysisModel(reqUri);
          }
        }
      }
      repUrl = repUrl + "?target=" + target + (StringUtils.isNoneBlank(model) ? ("&" + model) : "");
      response.sendRedirect(repUrl);
    } catch (Exception e) {
      logger.error("获取跳转App地址出错", e);
    }
  }

  // App下载页面
  @Action("/oauth/download")
  public String openAppDownload() {
    return "app_download";
  }

  // 解析跳转App的功能页面参数
  public String analysisModel(String reqUri) throws Exception {
    HttpServletRequest request = Struts2Utils.getRequest();
    String path = "/WEB-INF/webToAppUrl.properties";
    String realPath = request.getServletContext().getRealPath(path);
    InputStreamReader in = new InputStreamReader(new FileInputStream(realPath), "utf-8");
    prop.load(in);
    String target = prop.getProperty(reqUri);
    in.close();
    return StringUtils.isNoneBlank(target) ? target : "dynamicpage";
  }

  /**
   * 重新判断来源
   * 
   * @param form2
   */
  private void rebuildSys(OauthLoginForm form) {
    if (judgmentIsMid(Struts2Utils.getRequest())
        || SmateMobileUtils.isWeChatBrowser(Struts2Utils.getRequest().getHeader("User-Agent"))) {
      form.setSys("WCI");
    } else {
      form.setSys("SNS");
    }
  }

  /**
   * 判断当前系统是不是移动端
   * 
   * @param httpServletRequest
   * @return
   */
  private boolean judgmentIsMid(HttpServletRequest httpServletRequest) {
    String ua = httpServletRequest.getHeader("User-Agent");
    String regex =
        "ios|symbianos|windows mobile|windows ce|ucweb|rv:1.2.3.4|midp|android|webos|iphone|ipad|ipod|blackberry|iemobile|opera mini";
    Pattern p = Pattern.compile(regex);
    return p.matcher(ua.toLowerCase()).find();
  }

  /**
   * sns PC端
   * 
   * @return
   * @throws Exception
   */
  public String dealSnsLoginIndex() throws Exception {
    LOG.debug("正在进入SNS首页,尚未登录...");
    String SYSID = Struts2Utils.getSession().getId();
    String service = Struts2Utils.getRequest().getParameter("service");
    // 取缓存
    Object object = oauthCacheService.get(SecurityConstants.USER_DETAILS_INFO_CACHE, SYSID);
    if (object != null) {
      if (StringUtils.isNotBlank(service)) {
        String targetUrl = ServiceUtil.decodeFromDes3(service);
        if (StringUtils.isNotBlank(targetUrl)) {
          Struts2Utils.getResponse().sendRedirect(targetUrl);
          return null;
        }
      } else {
        if (StringUtils.isNoneBlank(form.getLocale())) {
          Struts2Utils.redirect("/dynweb/main?locale=" + form.getLocale());
        } else {
          Struts2Utils.redirect("/dynweb/main");
        }
        return NONE;
      }
    }
    Cookie cookie =
        WebUtils.getCookie((HttpServletRequest) Struts2Utils.getRequest(), SecurityConstants.AUTO_LOGIN_PARAMETER_NAME);
    Cookie loginCookie = WebUtils.getCookie((HttpServletRequest) Struts2Utils.getRequest(), OAUTH_LOGIN);
    if (loginCookie != null && "true".equals(loginCookie.getValue())) {
      if (cookie != null && StringUtils.isNotBlank(cookie.getValue())) {
        if (StringUtils.isNotBlank(service)) {
          String targetUrl = ServiceUtil.decodeFromDes3(service);
          // 有些时候解密失败可能是service未加密
          if (targetUrl == null) {
            targetUrl = service;
          }
          if (StringUtils.isNotBlank(targetUrl)) {
            if (targetUrl.indexOf("?") > -1) {
              targetUrl += "&AID=" + cookie.getValue();
            } else {
              targetUrl += "?AID=" + cookie.getValue();
            }
            Struts2Utils.getResponse().sendRedirect(targetUrl);
            return null;
          }
        } else {
          Struts2Utils.redirect("/dynweb/main?AID=" + cookie.getValue() + "&locale=" + form.getLocale());
        }
      }
    }
    form.setRandomNum((new Random().nextInt(3)) + 1);
    form.setSysDomain(domainscm);
    form.setMsg(Des3Utils.decodeFromDes3(form.getMsg()));
    return "SNS_login";
  }

  public String dealMobileLoginIndex() throws Exception {
    LOG.debug("正在进入SNS首页,尚未登录...");
    String SYSID = Struts2Utils.getSession().getId();
    String service = Struts2Utils.getRequest().getParameter("service");
    // 取缓存
    Object object = oauthCacheService.get(SecurityConstants.USER_DETAILS_INFO_CACHE, SYSID);
    if (object != null) {
      if (StringUtils.isNotBlank(service)) {
        String targetUrl = ServiceUtil.decodeFromDes3(service);
        if (StringUtils.isNotBlank(targetUrl)) {
          Struts2Utils.getResponse().sendRedirect(targetUrl);
          return null;
        }
      } else {
        if (StringUtils.isNoneBlank(form.getLocale())) {
          Struts2Utils.redirect(domainMobile + "/dynweb/mobile/dynshow?locale=" + form.getLocale());
        } else {
          Struts2Utils.redirect(domainMobile + "/dynweb/mobile/dynshow");
        }
        return NONE;
      }
    }
    Cookie cookie =
        WebUtils.getCookie((HttpServletRequest) Struts2Utils.getRequest(), SecurityConstants.AUTO_LOGIN_PARAMETER_NAME);
    Cookie loginCookie = WebUtils.getCookie((HttpServletRequest) Struts2Utils.getRequest(), OAUTH_LOGIN);
    if (loginCookie != null && "true".equals(loginCookie.getValue())) {
      if (cookie != null && StringUtils.isNotBlank(cookie.getValue())) {
        if (StringUtils.isNotBlank(service)) {
          String targetUrl = ServiceUtil.decodeFromDes3(service);
          // 有些时候解密失败可能是service未加密
          if (targetUrl == null) {
            targetUrl = service;
          }
          if (StringUtils.isNotBlank(targetUrl)) {
            if (targetUrl.indexOf("?") > -1) {
              targetUrl += "&AID=" + cookie.getValue();
            } else {
              targetUrl += "?AID=" + cookie.getValue();
            }
            Struts2Utils.getResponse().sendRedirect(targetUrl);
            return null;
          }
        } else {
          Struts2Utils.redirect("/dynweb/mobile/dynshow?AID=" + cookie.getValue() + "&locale=" + form.getLocale());
        }
      }
    }
    form.setRandomNum((new Random().nextInt(3)) + 1);
    form.setSysDomain(domainMobile);
    return "mobileIndex";
  }

  /**
   * 检查是否已登录
   */
  @Action("/oauth/login/ajaxcheck")
  public void checkHasLogin() {
    Map<String, String> result = new HashMap<String, String>();
    String hasLogin = "false";
    try {
      // 取缓存
      Object object =
          oauthCacheService.get(SecurityConstants.USER_DETAILS_INFO_CACHE, Struts2Utils.getSession().getId());
      if (object != null) {
        hasLogin = "true";
      }
    } catch (Exception e) {
      logger.error("检查是否已登录异常", e);
    }
    result.put("hasLogin", hasLogin);
    Struts2Utils.renderJson(result, "encoding: UTF-8");
  }

  @Override
  public void prepare() throws Exception {
    Object object = Struts2Utils.getSession().getAttribute("form");
    if (object != null && "true".equals(Struts2Utils.getParameter("form"))) {
      form = (OauthLoginForm) object;
    } else {
      form = new OauthLoginForm();
    }
  }

  @Override
  public OauthLoginForm getModel() {
    return form;
  }

  public String getWxOpenId() {
    return wxOpenId;
  }

  public void setWxOpenId(String wxOpenId) {
    this.wxOpenId = wxOpenId;
  }

  public String getWxUrl() {
    return wxUrl;
  }

  public void setWxUrl(String wxUrl) {
    this.wxUrl = wxUrl;
  }

}
