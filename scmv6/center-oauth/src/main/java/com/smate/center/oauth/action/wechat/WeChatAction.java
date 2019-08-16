package com.smate.center.oauth.action.wechat;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Actions;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Preparable;
import com.smate.center.oauth.cache.OauthCacheService;
import com.smate.center.oauth.service.mobile.MobileLoginService;
import com.smate.core.base.oauth.model.OauthLoginForm;
import com.smate.core.base.utils.constant.security.SecurityConstants;
import com.smate.core.base.utils.json.JacksonUtils;
import com.smate.core.base.utils.security.Des3Utils;
import com.smate.core.base.utils.struts2.Struts2Utils;
import com.smate.core.base.utils.wechat.OAuth2Service;
import com.smate.core.base.utils.wechat.WeChatRelationService;

/**
 * PC微信扫码登录
 * 
 * @author wuchuanwen
 *
 */
@Results({@Result(name = "wechat_login_refresh", location = "/WEB-INF/jsp/sns/scm_tempelete_login.jsp"),
    @Result(name = "bindWX", location = "/WEB-INF/jsp/sns/scm_tempelete_bind_wx.jsp"),
    @Result(name = "wechat_sie_login_refresh", location = "/WEB-INF/jsp/sns/sie_tempelete_login.jsp"),
    @Result(name = "wechat_ins_login_refresh", location = "/WEB-INF/jsp/sns/ins_tempelete_login.jsp"),
    @Result(name = "wechat_validate_login_refresh", location = "/WEB-INF/jsp/sns/vali_tempelete_login.jsp")})
public class WeChatAction extends ActionSupport implements ModelDriven<OauthLoginForm>, Preparable {

  /**
   * 
   */
  private static final long serialVersionUID = 214099442310624776L;
  @Value("${domainscm}")
  private String domainscm;
  @Value("${proxydomainscm}")
  private String proxydomainscm;
  @Value("${wechat.open.appid}")
  private String appId;
  @Value("${wechat.appid}")
  private String appid;
  private String state;// 用于保持请求和回调的状态，授权请求后原样带回给第三方。该参数可用于防止csrf攻击
  private OauthLoginForm form;
  private String wechatName;
  private String des3ThirdId;
  private final Logger logger = LoggerFactory.getLogger(getClass());
  private final String openUrl = "https://open.weixin.qq.com";
  @Autowired
  private OauthCacheService cacheService;
  @Autowired
  private OAuth2Service oAuth2Service;
  @Autowired
  private MobileLoginService mobileLoginService;
  @Autowired
  private WeChatRelationService weChatRelationService;
  private String wechatFunction = ""; // 微信功能。bindWX=绑定微信
  private String des3PsnId;
  private String occupy = "";
  private String AID;
  private String host;
  @Value("${domainrol}")
  private String domainrol;
  @Value("${domainvalidate}")
  private String domainvalidate;
  private String targetUrl;

  /**
   * 微信开放平台登录
   */
  @Actions({@Action("/oauth/wechat/login"), @Action("/oauth/wechat/bind"),})
  public void wechatLogin() {
    try {
      state = generateState();// 存于缓存用于校验
      state = dealState(state); // 为了添加其他功能
      cacheService.remove("openWechatState", "state");
      cacheService.put("openWechatState", cacheService.EXP_MIN_5, "state", state);
      state = URLEncoder.encode(Des3Utils.encodeToDes3(state), "utf-8");
      StringBuilder reqUrl = new StringBuilder();
      // 由于开放平台返回域只有一个，不同环境需要返回不同的回调 使用代理转发
      String from = "";
      from = domainscm.contains("dev") ? "dev"
          : (domainscm.contains("alpha") ? "alpha"
              : (domainscm.contains("test") ? "test" : (domainscm.contains("uat") ? "uat" : "")));
      if (StringUtils.isNotBlank(from)) {
        // 此处的域名应是在开放平台下的回调域一致
        reqUrl.append(proxydomainscm + "/oauth/wechat/proxy");
        reqUrl.append("?from=" + from);
        reqUrl.append("&state=" + state);

      } else {
        reqUrl.append(openUrl);
        reqUrl.append("/connect/qrconnect");
        reqUrl.append("?appid=" + appId);
        reqUrl.append("&redirect_uri=" + URLEncoder.encode(domainscm + "/oauth/wechat/callback", "utf-8"));
        reqUrl.append("&response_type=code");
        reqUrl.append("&scope=snsapi_login");
        reqUrl.append("&state=" + state);
        reqUrl.append("#wechat_redirect");
      }
      if (this.wechatFunction.equalsIgnoreCase("bindWX")) {// 绑定微信功能，需要的参数
        Long currentUserId = NumberUtils.toLong(Des3Utils.decodeFromDes3(this.des3PsnId), 0L);
        if (currentUserId != 0L) {
          Struts2Utils.getSession().setAttribute("currentUserId", currentUserId);
        }
      }

      // ROL-6514 机构版支持微信登录， 机构版请求的host参数加入cookie
      if (StringUtils.isNotBlank(host)) {
        Cookie cookie1 = new Cookie("host", URLEncoder.encode(host, "utf-8"));
        removeCookie(Struts2Utils.getResponse(), cookie1);
        this.setCookie(Struts2Utils.getResponse(), cookie1, 60 * 30);
      } else {
        Cookie cookie1 = new Cookie("host", URLEncoder.encode("", "utf-8"));
        removeCookie(Struts2Utils.getResponse(), cookie1);
      }
      Struts2Utils.getResponse().sendRedirect(reqUrl.toString());
    } catch (Exception e) {
      logger.error("跳转微信登录失败 state:" + state, e);
    }
  }

  /**
   * 存cookie
   */
  private void setCookie(HttpServletResponse response, Cookie cookie, int date) throws Exception {
    cookie.setPath("/");
    cookie.setDomain("scholarmate.com");
    cookie.setMaxAge(date);
    response.addCookie(cookie);
  }

  /**
   * 清除cookie
   */
  private void removeCookie(HttpServletResponse response, Cookie cookie) throws Exception {
    cookie.setPath("/");
    cookie.setDomain("scholarmate.com");
    cookie.setMaxAge(0);
    response.addCookie(cookie);
  }

  /**
   * 微信开放平台返回数据
   */
  @Action("/oauth/wechat/callback")
  public String wechatLoginCallback() {
    String backState = Struts2Utils.getParameter("state");
    backState = Des3Utils.decodeFromDes3(backState);
    handleState(backState);// 为了跳转其他功能
    String backCode = Struts2Utils.getParameter("code");
    String cacheState = (String) cacheService.get("openWechatState", "state");
    try {
      if (StringUtils.isBlank(cacheState)) {
        // 缓存被清除，需要重新扫描登录
        Struts2Utils.getResponse().sendRedirect(domainscm + "/oauth/wechat/login");
      }
    } catch (Exception e) {
      logger.error("跳转微信登录页面失败", e);
    }
    // cacheService.remove("openWechatState", "state");
    // 微信绑定功能
    if ("bindWX".equals(this.wechatFunction)) {
      String result = dealWXBind(backState, backCode, cacheState);
      if (StringUtils.isNotBlank(result)) {
        occupy = result;
        return "bindWX";
      }
      return null;
    }
    String targetUrl = "";
    if (backState.equals(cacheState)) {
      if (StringUtils.isNotBlank(backCode)) {
        // 授权成功-检验用户是否已注册绑定
        try {
          Map<String, Object> openWeChatInfo = oAuth2Service.getOpenWeChatUnionInfo(backCode);
          String wxUnionId = openWeChatInfo.get("unionid") == null ? null : openWeChatInfo.get("unionid").toString();
          String accessToken = null, openid = null;
          if (wxUnionId != null) {
            openid = openWeChatInfo.get("openid").toString();
            accessToken = openWeChatInfo.get("access_token").toString();
          }
          this.setDes3ThirdId(Des3Utils.encodeToDes3(form.getLoginType() + "|" + wxUnionId));
          Struts2Utils.getSession().setAttribute("wxUnionId", wxUnionId);
          Map<String, Object> result = mobileLoginService.buildAIDByWxUnionId(wxUnionId);
          if (result.containsKey("msg")) {
            // msg有值，则表示用微信openId找不到对应的科研之友人员，所以重定向到绑定页面进行绑定或者创建新账号
            targetUrl = domainscm + "/oauth/wechat/pcbind";
            targetUrl += "?des3ThirdId=" + URLEncoder.encode(des3ThirdId, "utf-8");
            // 获取用户信息
            Map<String, Object> weChatInfo = oAuth2Service.getOpenWeChatInfo(accessToken, openid);
            if (weChatInfo.get("nickname") != null) {
              targetUrl += "&wechatName=" + URLEncoder.encode(weChatInfo.get("nickname").toString(), "utf-8");
            }
            // ROL-6514 机构版支持微信登录，携带host参数
            targetUrl = addParameterNameHost(targetUrl);
            Struts2Utils.getResponse().sendRedirect(targetUrl);
            return null;
          } else {
            // 通过AID自动登录到对应的页面
            if (result.containsKey("AID") && StringUtils.isNotBlank(result.get("AID").toString())) {
              String AID = result.get("AID").toString();
              // 微信里面缓存sessionId,取消关注时清空权限用
              wxUnionId = (String) Struts2Utils.getSession().getAttribute("wxUnionId");
              if (StringUtils.isNotBlank(wxUnionId)) {
                cacheService.put(SecurityConstants.CACHE_SESSIONID, SecurityConstants.USER_DETAILS_CACHE_TIME,
                    wxUnionId, Struts2Utils.getSession().getId());
              }
              // String defaultUrl = domainscm + "/dynweb/main";
              // 修改为跳转到中间页面，以便页面中弹出登录框时用微信登录
              String defaultUrl = domainscm + "/oauth/wechat/login/refresh";
              targetUrl = this.rebuildTargetUrl(Struts2Utils.getParameter("service"), defaultUrl);
              if (targetUrl.indexOf("?") > 0) {
                targetUrl += "&AID=" + AID;
              } else {
                targetUrl += "?AID=" + AID;
              }
            }
          }
          // ROL-6514 机构版支持微信登录，携带host参数
          targetUrl = addParameterNameHost(targetUrl);
          Struts2Utils.getResponse().sendRedirect(targetUrl);
          return null;
        } catch (Exception e) {
          logger.error("PC端微信扫描登录构造AID出错", e);
        }
      }
    } else {
      // state参数校验失败
      logger.error("state参数过期或者不是最新的");
    }
    return null;
  }

  // ROL-6514 机构版支持微信登录, 添加host，用于判断登录后是否跳转到机构版对应页面
  private String addParameterNameHost(String targetUrl) {
    Cookie[] cookies = Struts2Utils.getRequest().getCookies();
    String host = "";
    for (Cookie cookie : cookies) {
      if (cookie.getName().equals("host")) {
        String hosts = cookie.getValue();
        host = hosts.split(",")[0];
      }
    }
    if (targetUrl.indexOf("?") > 0) {
      targetUrl += "&host=" + host;
    } else {
      targetUrl += "?host=" + host;
    }
    return targetUrl;
  }

  @Action("/oauth/wechat/login/refresh")
  public String refreshWechatLogin() {
    // sie，根据获取首页域名判断weChat登录后，跳转目的页是产品首页，还是单位首页，亦或是科研验证在个人版的页面
    if (StringUtils.isNotBlank(host)) {
      if (host.contains(domainrol)) {
        return "wechat_sie_login_refresh";
      } else if (domainvalidate.contains(host)) {
        return "wechat_validate_login_refresh";
      } else {
        if (host.contains("http")) {
          try {
            targetUrl = URLEncoder.encode(host, "UTF-8");
          } catch (UnsupportedEncodingException e) {
            logger.error("weChat登录获取超时前的URL报错");
          }
          host = host.substring(host.indexOf(":") + 3, host.lastIndexOf("com") + 3);
        }
        return "wechat_ins_login_refresh";
      }
    }
    return "wechat_login_refresh";
  }

  /**
   * 处理微信绑定业务
   * 
   * @param backState
   * @param backCode
   * @param cacheState
   */
  private String dealWXBind(String backState, String backCode, String cacheState) {
    if (backState.equals(cacheState)) {
      Long currentUserId = (Long) Struts2Utils.getSession().getAttribute("currentUserId");
      Struts2Utils.getSession().removeAttribute("currentUserId");
      if (currentUserId == null) {
        logger.info("进入微信绑定功能,报错currentUserId" + currentUserId);
        return "";
      }
      logger.info("进入微信绑定功能currentUserId" + currentUserId);
      try {
        Map<String, Object> openWeChatInfo = oAuth2Service.getOpenWeChatUnionInfo(backCode);
        String wxUnionId = openWeChatInfo.get("unionid") == null ? null : openWeChatInfo.get("unionid").toString();
        String accessToken = null, wxOpenid = null;
        if (wxUnionId != null) {
          wxOpenid = openWeChatInfo.get("openid").toString();
          accessToken = openWeChatInfo.get("access_token").toString();
          logger.info("获取的微信unionId为：" + wxUnionId + ", 微信openId为：" + wxOpenid + ", accessToken为：" + accessToken);
          Map<String, Object> weChatInfo = oAuth2Service.getOpenWeChatInfo(accessToken, wxOpenid);
          Object nickName = weChatInfo.get("nickname");
          String result = weChatRelationService.bindWeChat(wxOpenid, wxUnionId, 1, currentUserId,
              nickName != null ? nickName.toString() : "");
          return result;
        }
      } catch (Exception e) {
        logger.error("微信绑定异常", e);
      }
    } else {
      logger.error("state参数过期或者不是最新的");
    }
    return "";
  }

  // @Action("/oauth/wechat/runOldData")
  public void runOldDate() {
    // 判断是否还存在没有union信息的数据 -------更新生产前重新跑一次
    try {
      // 获取该环境下的access_token open系统中有获取该参数的方法
      /*
       * List<String> openIdList = weChatRelationService.findWeChatNoUnionIdList(); if
       * (CollectionUtils.isNotEmpty(openIdList)) { // String access_token =
       * oAuth2Service.getWeChatToken(); String access_token =
       * "8_w0tQUVDAM1E8D2r8cLmFPsrNoccrjcZYbFmNSTItjCMM__yZjQQKkCJuCghBB2iMCWiJuc4aUTcTMThOGWDe4yzacRyUvZ6oSrQry-MCu6y5YfMvuIuOeDn8HSJIaF_y0LqYc17jCADRQyIjHHIgAGAMMN";
       * if (StringUtils.isNotBlank(access_token)) { int time = openIdList.size() / 100 +
       * (openIdList.size() % 100 > 0 ? 1 : 0); for (int i = 0; i < time; i++) { int endPos = (i + 1) *
       * 100 > openIdList.size() ? openIdList.size() : (i + 1) * 100; String userList =
       * buildUserListInfo(openIdList.subList(i * 100, endPos)); Map<String, Object> infos =
       * oAuth2Service.getWeChatInfos(access_token.toString(), userList); Object jsonList =
       * infos.get("user_info_list"); if (jsonList != null) { List<Map<String, String>> list =
       * (ArrayList<Map<String, String>>) jsonList; for (int j = 0; j < list.size(); j++) { String openId
       * = list.get(j).get("openid"); String unionId = list.get(j).get("unionid"); if
       * (StringUtils.isNotBlank(openId) && StringUtils.isNotBlank(unionId)) {
       * weChatRelationService.refreshUnionId(openId, unionId); } } } } } }
       */
      List<String> openIdList = weChatRelationService.findWeChatNoUnionIdList();
      if (CollectionUtils.isNotEmpty(openIdList)) {
        // String access_token = oAuth2Service.getWeChatToken();
        String access_token =
            "8_w0tQUVDAM1E8D2r8cLmFPsrNoccrjcZYbFmNSTItjCMM__yZjQQKkCJuCghBB2iMCWiJuc4aUTcTMThOGWDe4yzacRyUvZ6oSrQry-MCu6y5YfMvuIuOeDn8HSJIaF_y0LqYc17jCADRQyIjHHIgAGAMMN";
        if (StringUtils.isNotBlank(access_token)) {
          for (int i = 0; i < openIdList.size(); i++) {
            Map<String, Object> infos = oAuth2Service.getWeChatInfoSingle(access_token.toString(), openIdList.get(i));
            if (infos.get("unionid") != null) {
              String unionId = infos.get("unionid").toString();
              String openId = openIdList.get(i);
              if (StringUtils.isNotBlank(openId) && StringUtils.isNotBlank(unionId)) {
                weChatRelationService.refreshUnionId(openId, unionId);
              }
            }
          }
        }
      }
    } catch (Exception e) {
    }
  }

  /**
   * 拼接参数 -- 为微信老用户获取unionId使用
   * 
   * @param userList
   * @return
   */
  private String buildUserListInfo(List<String> userList) {
    // 构建json串
    List<Map<String, String>> users = new ArrayList<>();
    for (int i = 0; i < userList.size(); i++) {
      Map<String, String> user = new HashMap<>();
      user.put("openid", userList.get(i));
      user.put("lang", "zh_CN");
      users.add(user);
    }
    String jsonList = JacksonUtils.jsonListSerializer(users);
    return "{\"user_list\":" + jsonList + "}";
  }

  /**
   * 解密目标url
   * 
   * @param encodeUrl 已加密的目标页面url
   * @param defaultUrl 目标页面url为空时默认的url
   * @return
   */
  private String rebuildTargetUrl(String encodeUrl, String defaultUrl) {
    StringBuilder targetUrl = new StringBuilder();
    if (encodeUrl == null || "".equals(encodeUrl)) {
      targetUrl.append(defaultUrl);
    } else {
      targetUrl.append(Des3Utils.decodeFromDes3(encodeUrl));
    }
    // 如果解密失败，可能是目标页面未加密
    if ("null".equals(targetUrl.toString())) {
      targetUrl = new StringBuilder();
      targetUrl.append(encodeUrl);
    }
    return targetUrl.toString();
  }

  /**
   * 生成state参数 6位随机数(建议使用随机数+session)
   */
  private static String generateState() throws Exception {
    String randomNum = RandomStringUtils.random(6,
        new char[] {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't',
            'u', 'v', 'w', 'x', 'y', 'z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E',
            'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'});

    return randomNum;
  }

  /**
   * 处理 微信扫一扫的功能
   */
  private String handleState(String state) {
    if (StringUtils.isBlank(state)) {
      return state;
    }
    String[] split = state.split("\\|");
    if (split.length > 1) {
      this.wechatFunction = split[1];
    }
    return state;
  }

  private String dealState(String state) {
    if (StringUtils.isBlank(this.wechatFunction)) {
      return state;
    }
    state = state + "|" + this.wechatFunction;
    return state;
  }

  @Override
  public void prepare() throws Exception {
    form = new OauthLoginForm();
    form.setLoginType("3");
  }

  @Override
  public OauthLoginForm getModel() {
    // TODO Auto-generated method stub
    return form;
  }

  public String getWechatName() {
    return wechatName;
  }

  public void setWechatName(String wechatName) {
    this.wechatName = wechatName;
  }

  public String getDes3ThirdId() {
    return des3ThirdId;
  }

  public void setDes3ThirdId(String des3ThirdId) {
    this.des3ThirdId = des3ThirdId;
  }

  public String getWechatFunction() {
    return wechatFunction;
  }

  public void setWechatFunction(String wechatFunction) {
    this.wechatFunction = wechatFunction;
  }

  public String getDes3PsnId() {
    return des3PsnId;
  }

  public void setDes3PsnId(String des3PsnId) {
    this.des3PsnId = des3PsnId;
  }

  public String getOccupy() {
    return occupy;
  }

  public void setOccupy(String occupy) {
    this.occupy = occupy;
  }

  public String getAID() {
    return AID;
  }

  public void setAID(String aID) {
    AID = aID;
  }

  public String getHost() {
    return host;
  }

  public void setHost(String host) {
    this.host = host;
  }

  public String getTargetUrl() {
    return targetUrl;
  }

  public void setTargetUrl(String targetUrl) {
    this.targetUrl = targetUrl;
  }
}
