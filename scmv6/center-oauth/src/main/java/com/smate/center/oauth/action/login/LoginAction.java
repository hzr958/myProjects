package com.smate.center.oauth.action.login;

import java.io.IOException;
import java.io.Serializable;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Preparable;
import com.smate.center.oauth.cache.OauthCacheService;
import com.smate.center.oauth.model.consts.OpenConsts;
import com.smate.center.oauth.model.consts.SmateLoginConsts;
import com.smate.center.oauth.service.login.OauthLoginService;
import com.smate.center.oauth.service.login.OauthService;
import com.smate.center.oauth.service.security.OauthUserDetailsService;
import com.smate.core.base.oauth.model.OauthLoginForm;
import com.smate.core.base.utils.app.AppActionUtils;
import com.smate.core.base.utils.cache.CacheService;
import com.smate.core.base.utils.common.URIencodeUtils;
import com.smate.core.base.utils.constant.IOSHttpStatus;
import com.smate.core.base.utils.constant.security.SecurityConstants;
import com.smate.core.base.utils.security.Des3Utils;
import com.smate.core.base.utils.struts2.Struts2Utils;

/**
 * 帐号密码验证 权限
 * 
 * @author tsz
 *
 */
@Results({@Result(name = "reload", location = "index", type = "redirect"),
    @Result(name = "error", location = "/index.jsp"), @Result(name = "errorLogin", location = "/common/500.jsp"),
    @Result(name = "redirectUrl", location = "${forwardUrl}", type = "redirect"), @Result(name = "toAppIndex",
        params = {"actionName", "index", "namespace", "/oauth/app", "method", "appIndex"}, type = "chain")})
public class LoginAction extends ActionSupport implements ModelDriven<OauthLoginForm>, Preparable {

  /**
   * 
   */
  private static final long serialVersionUID = -4233512169345586923L;
  private final Logger logger = LoggerFactory.getLogger(getClass());
  public static final String SPRING_SECURITY_CONTEXT_KEY = "SPRING_SECURITY_CONTEXT";
  @Autowired
  private OauthLoginService oauthLoginService;

  @Autowired
  private OauthCacheService oauthCacheService;
  @Autowired
  private CacheService cacheService;
  @Autowired
  private OauthService oauthService;
  @Autowired
  private OauthUserDetailsService userDetailsService;

  private OauthLoginForm form;
  @Value("${domainoauth}")
  private String domainoauth;
  @Value("${domainscm}")
  private String domainScm;
  @Value("${domainbpo}")
  private String domainBpo;
  @Value("${domainnsfc}")
  private String domainNsfc;
  @Value("${domaingxrol}")
  private String domainGxrol;
  @Value("${domainzsrol}")
  private String domainZsrol;
  @Value("${domainstdrol}")
  private String domainHnrol;
  @Value("${domainexpert}")
  private String domainexpert;
  @Value("${domainMobile}")
  private String domainMobile;

  private String wxOpenId;
  private Long appflag;
  private String status = IOSHttpStatus.NOT_MODIFIED;// 默认状态

  /**
   * 帐号密码验证
   * 
   * @return
   * @throws Exception
   */
  @Action("/oauth/login")
  public String login() {
    // 1.验证帐号密码 2.如果错误记录错误次数 3.错误3次或以上需要显示验证码4.验证通过后 构造权限信息并记录缓存 5重定向到目标页面
    try {
      oauthLoginService.oauthLogin(form);
      // 登录失败后根据来源系统 进入对应的页面.
      if (form.getLoginStatus() == 0) {
        return loginErrorRedirect(form);
      }
      // 登陆成功处理
      return loginSuccessRedirect(form);
    } catch (Exception e) {
      // 需要 重新生成 sessionId, 防止session固话
      Struts2Utils.getRequest().changeSessionId();
      logger.error("帐号密码验证出错", e);
      return "errorLogin";
    }
  }


  /**
   * 登录失败后根据来源系统 进入对应的页面.
   *
   * @param form
   * @return
   * @throws IOException
   */
  protected String loginErrorRedirect(OauthLoginForm form) throws IOException {
    // 不同的系统标志对应的要跳转的链接
    Map<String, String> sysPageMap = new HashMap<String, String>();
    // 微信
    sysPageMap.put("WCI",
        domainMobile + "/oauth/mobile/index?form=true"
            + (StringUtils.isNotBlank(wxOpenId) ? "&" + "wxOpenId=" + wxOpenId : "") + "&loginStatus="
            + form.getLoginStatus() + "&needValidateCode=" + form.getNeedValidateCode() + "&msg="
            + Des3Utils.encodeToDes3(form.getMsg()) + "&mobileCodeLogin=" + form.getMobileCodeLogin());
    // 科研之友个人版
    sysPageMap.put("SNS",
        domainScm + "/oauth/index?sys=SNS&service=" + URLEncoder.encode(form.getService(), "utf-8")
            + "&needValidateCode=" + form.getNeedValidateCode() + "&msg=" + Des3Utils.encodeToDes3(form.getMsg())
            + "&errMsgPosition=" + form.getErrMsgPosition());
    // 科研之友机构版旧版
    sysPageMap.put("SIE", "https://" + form.getSysDomain() + "/scmwebrol/index?form=true&status=error");
    // 科研之友机构版新版
    sysPageMap.put("SIE6", "http://" + form.getSysDomain() + "/insweb/index?form=true&status=error");
    // bpo系统
    sysPageMap.put("BPO", domainBpo + "/scmwebbpo/index?form=true");
    // 广西科研在线
    sysPageMap.put("GXROL", domainGxrol + "/scmwebrol/?form=true&status=error");
    // 中山科研在线
    sysPageMap.put("ZSROL", domainZsrol + "/zsrol/?form=true&status=error");
    // 海南科研在线
    sysPageMap.put("HNROL", domainHnrol + "/scmstdrol/?form=true&status=error");
    // 报表系统
    sysPageMap.put("EGTEXPERT",
        domainScm + "/oauth/loginerror?sys=EGTEXPERT&service=" + URLEncoder.encode(form.getService(), "utf-8"));
    // 科研验证
    sysPageMap.put("VALIDATA", "http://" + form.getSysDomain() + "/validate/index?form=true&status=error");
    form.setForwardUrl(domainScm + "/oauth/sns/index?form=true&sys=SNS");
    if (sysPageMap.containsKey(form.getSys())) {
      form.setForwardUrl(sysPageMap.get(form.getSys()));
    } else if ("APP".equals(form.getSys())) {
      form.setForwardUrl("");
      Map<String, Object> map = new HashMap<String, Object>();
      map.put("loginstatus", form.getLoginStatus());
      map.put("msg", form.getMsg());
      status = IOSHttpStatus.OK;
      Struts2Utils.renderJson(buildInfo(map), "encoding:UTF-8");
    }
    if (StringUtils.isNotBlank(form.getForwardUrl())) {
      Struts2Utils.getResponse().sendRedirect(form.getForwardUrl());
    }
    return null;
  }



  /**
   * 登录成功后跳转处理
   *
   * @param form
   * @return
   * @throws Exception
   */
  protected String loginSuccessRedirect(OauthLoginForm form) throws Exception {
    // app客户端登陆成功处理
    if ("APP".equals(form.getSys())) {
      this.setAppflag(form.getPsnId());
      return "toAppIndex";
    } else {
      // web端登录成功处理
      return webLoginSuccessRedirect(form);
    }
  }



  /**
   * web系统登录成功后处理
   * 
   * @param form
   * @return
   */
  protected String webLoginSuccessRedirect(OauthLoginForm form) {
    try {
      // 需要 重新生成 sessionId, 防止session固话
      Struts2Utils.getRequest().changeSessionId();
      // 默认跳转到动态首页，不然为空时会跳转一次登录首页
      if (StringUtils.isBlank(form.getService())) {
        if ("WCI".equals(form.getSys())) {
          form.setService(Des3Utils.encodeToDes3(domainMobile + "/dynweb/mobile/dynshow"));
        }
        if ("SNS".equals(form.getSys())) {
          form.setService(Des3Utils.encodeToDes3(domainScm + "/dynweb/main"));
        }
      }
      String targetUrl = oauthLoginService.oauthRebuildTargetUrl(form.getService());
      oauthCacheService.remove(SecurityConstants.USER_DETAILS_INFO_CACHE, form.getUserName());
      String result = this.rebuildPsnOauthInfo(targetUrl);
      if ("success".equals(result)) {
        Struts2Utils.getResponse().sendRedirect(targetUrl);
      }
    } catch (Exception e) {
      logger.error("重写sessionId出错", e);
      return "errorLogin";
    }
    return null;
  }


  /**
   * 重新构建人员权限信息
   *
   * @param targetUrl
   * @return
   */
  private String rebuildPsnOauthInfo(String targetUrl) {
    String result = "success";
    try {
      // 拼接 psn_id,ins_id,role_id
      UserDetails uDetails = userDetailsService.loadUserFromSys(form.getPsnId().toString(), form.getSys(), targetUrl);
      String sesseionId = Struts2Utils.getSession().getId();
      Map<String, Object> Details = oauthService.buildUserDetailsMap(sesseionId, targetUrl, form.getSys(), uDetails);
      Details.remove("userId");
      Details.put("userId", form.getPsnId());
      oauthCacheService.put(SecurityConstants.USER_DETAILS_INFO_CACHE, SecurityConstants.USER_DETAILS_CACHE_TIME,
          sesseionId, (Serializable) Details);
      // 当用户选择记住登录时将AID写入cookie中
      HttpServletResponse response = Struts2Utils.getResponse();
      // 记住登录功能不应该影响正常登录，获取AID出错的话捕获错误，继续正常登录
      try {
        if (form.getRememberMe()) {
          Long openId = oauthLoginService.getOpenId("00000000", form.getPsnId(), OpenConsts.OPENID_CREATE_TYPE_7);
          String AID = null;
          if (openId != null && openId != 0L) {
            AID = oauthLoginService.getAutoLoginAID(openId, "SNSRememberMe");
          }
          if (StringUtils.isNotBlank(AID)) {
            this.setCookie(response, new Cookie(SecurityConstants.AUTO_LOGIN_PARAMETER_NAME, AID), AID,
                SecurityConstants.REMEMBER_ME_TIME);
          }
        }
      } catch (Exception e) {
        logger.error("登录获取AID出错， psnId=" + form.getPsnId(), e);
      }
      // 添加已登录标识，自动登录用
      this.setCookie(response, new Cookie(SecurityConstants.OAUTH_LOGIN, "true"), "true",
          SecurityConstants.REMEMBER_ME_TIME);
      // 添加系统标识
      this.setCookie(response, new Cookie("SYS", form.getSys()), form.getSys(), SecurityConstants.REMEMBER_ME_TIME);
    } catch (Exception e) {
      logger.error("重新构建用户权限信息并放到缓存中出错了， psnId = " + form.getPsnId(), e);
      result = "serviceError";
    }
    return result;
  }


  /**
   * 检查登录账号和验证码
   *
   * @return
   * @throws Exception
   */
  @Action("/oauth/ajaxcheckmobilecode")
  public String ajaxCheckMobileCode() {
    Map<String, String> result = new HashMap<String, String>();
    try {
      boolean validParam = oauthLoginService.checkMobileCodeLogin(form);
      if (validParam) {
        result.put("result", "success");
      } else {
        result.put("result", "error");
        result.put("errorMsg", form.getMsg());
        result.put("needValidateCode", form.getNeedValidateCode());
      }
    } catch (Exception e) {
      logger.error("确认验证码异常", e);
      result.put("result", "error");
      result.put("errorMsg", "网络异常，请稍后再试");
    }
    if (StringUtils.isNotBlank(form.getSysDomain())) {
      String origin = "http://" + form.getSysDomain();
      if (form.getService().contains("https")) {
        origin = "https://" + form.getSysDomain();
      }
      Struts2Utils.getResponse().addHeader("Access-Control-Allow-Origin", origin);
    }
    Struts2Utils.renderJson(result);
    return null;
  }

  /**
   * 构建返回给IOS端统一Json格式
   */
  public Map<String, Object> buildInfo(Object data) {
    Map<String, Object> infoData = new HashMap<String, Object>();
    Map<String, Object> returnData = new HashMap<String, Object>();
    infoData.put("commentlist", data);
    infoData.put("total", 0);
    returnData.put("status", status);
    returnData.put("results", infoData);
    return returnData;
  }



  /**
   * ajax方式登录
   * 
   * @return
   */
  @Action("/oauth/login/ajaxlogin")
  public String loginBoxLoginToScm() {
    Map<String, String> result = new HashMap<String, String>();
    try {
      // 校验账号密码
      oauthLoginService.oauthLogin(form);
      // 登录是否有问题
      if (StringUtils.isNotBlank(form.getMsg())) {
        result.put("result", form.getMsg());
      } else {
        // 登录成功更新会话ID
        Struts2Utils.getRequest().changeSessionId();
        result.put("result", rebuildPsnOauthInfo(null));
        result.put("service", Des3Utils.decodeFromDes3(form.getService()));
      }
      result.put("needValidateCode", form.getNeedValidateCode());
    } catch (Exception e) {
      logger.error("ajax登录操作出错了", e);
      result.put("result", "serviceError");
    }
    Struts2Utils.renderJson(result, "encoding:utf-8");
    return null;
  }

  /**
   * ajax方式登录
   * 
   * @return
   */
  @Action("/oauth/login/ajaxsielogin")
  public String loginBoxLoginToSie() {
    Map<String, String> result = new HashMap<String, String>();
    try {
      String origin = "http://" + form.getSysDomain();
      String str = Struts2Utils.getHttpReferer();
      if (StringUtils.isNotBlank(str)) {
        URL url = new URL(str);
        origin = url.getProtocol() + "://" + form.getSysDomain();

      }
      Struts2Utils.getResponse().addHeader("Access-Control-Allow-Origin", origin);
      Struts2Utils.getResponse().addHeader("Access-Control-Allow-Credentials", "true");
      // 校验账号密码
      oauthLoginService.checkOauthLogin(form);
      // 登录是否有问题
      if (StringUtils.isNotBlank(form.getMsg())) {
        result.put("result", form.getMsg());
      } else {
        result.put("result", "success");
      }
    } catch (Exception e) {
      logger.error("ajax登录操作出错了", e);
      result.put("result", "serviceError");
    }
    Struts2Utils.renderJson(result, "encoding:utf-8");

    return null;
  }

  /**
   * 加密url
   * 
   * @return
   */
  @Action("/oauth/url/ajaxencode")
  public String ajaxEncodeUrl() {
    Map<String, String> result = new HashMap<String, String>();
    try {
      if (StringUtils.isNotBlank(form.getService())) {
        result.put("des3Url", Des3Utils.encodeToDes3(URIencodeUtils.decodeURIComponent(form.getService())));
      }
      result.put("result", "success");
    } catch (Exception e) {
      logger.error("加密url出错， url = " + form.getService(), e);
      result.put("result", "error");
    }
    result.put("url", form.getService());
    Struts2Utils.renderJson(result, "encoding:utf-8");
    return null;
  }


  /**
   * android登录
   * 
   * @return
   */
  @Action("/oauth/app/login")
  public String appLoginToScm() {
    Map<String, String> result = new HashMap<String, String>();
    String status = SUCCESS;
    try {
      // TODO 校验验证码
      // 校验账号密码
      oauthLoginService.oauthLogin(form);
      // 登录是否有问题
      if (StringUtils.isBlank(form.getMsg())) {
        Long openId =
            oauthLoginService.getOpenId(OpenConsts.SMATE_TOKEN, form.getPsnId(), OpenConsts.OPENID_CREATE_TYPE_7);
        String aid = "";
        if (openId != null && openId != 0L) {
          aid = oauthLoginService.getAutoLoginAID(openId, SmateLoginConsts.SNS_REMEMBER_ME);
        }
        if (StringUtils.isNotBlank(aid)) {
          result.put("AID", aid);
          result.put("des3PsnId", Des3Utils.encodeToDes3(form.getPsnId().toString()));
        }
      }
    } catch (Exception e) {
      logger.error("ajax登录操作出错了", e);
      form.setMsg("登录操作出错了");
      status = ERROR;
    }
    Struts2Utils.renderJson(AppActionUtils.buildReturnInfo(result, 0, status, form.getMsg()), "encoding: utf-8");
    return null;
  }

  @Override
  public void prepare() throws Exception {
    form = new OauthLoginForm();
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

  /**
   * 记住登录，设置AID到cookie中
   * 
   * @param response
   * @param cookie
   * @param AID
   * @param maxAge
   */
  private void setCookie(HttpServletResponse response, Cookie cookie, String AID, int maxAge) {
    if (cookie == null) {
      cookie = new Cookie(SecurityConstants.AUTO_LOGIN_PARAMETER_NAME, AID);
    }
    cookie.setSecure(true);
    cookie.setHttpOnly(true);
    cookie.setValue(AID);
    cookie.setMaxAge(maxAge);
    cookie.setDomain(".scholarmate.com");
    cookie.setPath("/");
    response.addCookie(cookie);

  }

  public Long getAppflag() {
    return appflag;
  }

  public void setAppflag(Long appflag) {
    this.appflag = appflag;
  }

}
