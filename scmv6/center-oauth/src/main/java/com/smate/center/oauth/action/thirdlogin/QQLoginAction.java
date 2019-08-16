package com.smate.center.oauth.action.thirdlogin;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.qq.connect.api.OpenID;
import com.qq.connect.oauth.Oauth;
import com.smate.center.oauth.cache.OauthCacheService;
import com.smate.center.oauth.model.bind.AccessToken;
import com.smate.center.oauth.model.thirduser.SysThirdUser;
import com.smate.center.oauth.service.thirdlogin.ThirdLoginService;
import com.smate.center.oauth.utils.PropertiesUtils;
import com.smate.center.oauth.utils.StringToMapUtils;
import com.smate.core.base.utils.cache.CacheService;
import com.smate.core.base.utils.string.ServiceUtil;
import com.smate.core.base.utils.struts2.Struts2Utils;
import com.smate.core.base.utils.url.HttpRequestUtils;
import com.smate.core.base.utils.wechat.MessageUtil;

/**
 * QQ第三方登录
 * 
 * @author Scy
 */
@Results({@Result(name = "scm_user_check", location = "/WEB-INF/jsp/sns/scm_user_check.jsp"),
    @Result(name = "loginQQ", location = "/WEB-INF/jsp/sns/scm_tempelete_login.jsp"),
    @Result(name = "sieLoginQQ", location = "/WEB-INF/jsp/sns/sie_tempelete_login.jsp"),
    @Result(name = "insLoginQQ", location = "/WEB-INF/jsp/sns/ins_tempelete_login.jsp"),
    @Result(name = "valiLoginQQ", location = "/WEB-INF/jsp/sns/vali_tempelete_login.jsp"),
    @Result(name = "bindQQ", location = "/WEB-INF/jsp/sns/scm_tempelete_bind.jsp"),
    @Result(name = "error", location = "/common/500.jsp")})
public class QQLoginAction extends ThirdLoginAction {

  /**
   * 
   */
  private static final long serialVersionUID = -6162769736054668307L;

  protected Logger logger = LoggerFactory.getLogger(getClass());

  // openId,loginType 加密
  private String des3OpenId;
  private String loginType = "1";
  private String userCheck;
  private String qqName;
  private String AID;
  private String host;
  private String targetUrl;
  private String code;
  @Value("${domainscm}")
  private String domainscm;
  @Value("${domainrol}")
  private String domainrol;
  @Value("${domainvalidate}")
  private String domainvalidate;
  @Autowired
  private OauthCacheService oauthCacheService;
  @Autowired
  private ThirdLoginService thirdLoginService;

  private static final String QQ_LOGIN_CACHE = "qq_login_cache";
  private static final String QQ_LOGIN_PARAMS = "qqLoginParams";
  private static final String oauth_consumer_key = "101190413";

  private String typeString = "QQ";
  private Boolean bindQQ = false;
  private String parentWindowUrl; // 父窗口的url

  /**
   * QQ登录
   * 
   * @return
   * @throws Exception
   */
  @Action("/oauth/qq/login")
  public String qqLogin() throws Exception {
    // scm-6660 进入设置邮箱账号页面之前清空QQ登录的缓存
    removeQQLoginParamsCache();
    // sie特殊处理。
    if (StringUtils.isNotBlank(host)) {
      Cookie cookie1 = new Cookie("host", URLEncoder.encode(host, "utf-8"));
      removeCookie(Struts2Utils.getResponse(), cookie1);
      this.setCookie(Struts2Utils.getResponse(), cookie1, 60 * 30);
    }
    Struts2Utils.getResponse().sendRedirect((new Oauth()).getAuthorizeURL(Struts2Utils.getRequest()));
    return null;
  }

  /**
   * QQ登录成功回调地址
   * 
   * @return
   * @throws Exception
   */
  @Action("/oauth/qq/loginafter")
  public String qqLoginAfter() {
    try {
      super.removeThirdLoginParamsCache();
      Map<String, String> thirdLoginParams = super.getThirdLoginParamsCache();
      if (thirdLoginParams != null) {
        if (StringUtils.isNotEmpty(thirdLoginParams.get("userName"))) {
          thirdLoginParams.remove("userName");
        }
        if (StringUtils.isNotBlank(thirdLoginParams.get("des3ThirdId"))) {
          thirdLoginParams.remove("des3ThirdId");
        }
        if (!thirdLoginParams.isEmpty()) {
          super.putThirdLoginParamsCache((HashMap<String, String>) thirdLoginParams);
        }
      }
      // 获取系统域名 主要区分个人版QQ登录，机构产品首页QQ登录，机构单位首页QQ登录
      Cookie[] cookies = Struts2Utils.getRequest().getCookies();
      String host = "";
      for (Cookie cookie : cookies) {
        if (cookie.getName().equals("host")) {
          String hosts = cookie.getValue();
          host = hosts.split(",")[0];
        }
      }
      /**
       * 通过官方文档进行获取,防止用户登录又立马退出再立马登录获取不到access_token
       */
      AccessToken accessTokenObj = new AccessToken();
      String getAccessTokenParams = "grant_type=" + PropertiesUtils.map.get("grantType") + "&" + "client_id="
          + PropertiesUtils.map.get("app_ID") + "&" + "client_secret=" + PropertiesUtils.map.get("app_KEY") + "&"
          + "code=" + code + "&redirect_uri=" + domainscm + PropertiesUtils.map.get("redirectUri");
      String results =
          HttpRequestUtils.sendGet(String.valueOf(PropertiesUtils.map.get("accessTokenURL")), getAccessTokenParams);
      Map<String, String> map = StringToMapUtils.toMap(results);
      if (StringToMapUtils.isNotEmpty(map)) {
        accessTokenObj.setAccessToken(map.get("access_token"));
        accessTokenObj.setExpireIn(map.get("expires_in"));
      }

      // 发现使用 getAccessTokenByRequest方法不会返回错误信息, 上述代码可以查看返回信息
      // AccessToken accessTokenObj = (new Oauth()).getAccessTokenByRequest(Struts2Utils.getRequest());

      String accessToken = null, openID = null;

      // 在切换中英文时，要从缓存中获取qq登录后返回的openID和accessToken，如果缓存为空，则通过accessTokenObj获取
      Map<String, String> qqParams = (Map<String, String>) getQQLoginParamsCache();
      if (qqParams != null) {
        if (StringUtils.isNotBlank(qqParams.get("accessToken")) && StringUtils.isNotBlank(qqParams.get("openID"))) {
          accessToken = qqParams.get("accessToken");
          openID = qqParams.get("openID");
        } else {
          if (accessTokenObj.getAccessToken().equals("")) {
            logger.error("---------QQ登录回调，获取不到参数，请检查配置！------------");
            return null;
          }
          accessToken = accessTokenObj.getAccessToken();

          // 利用获取到的accessToken 去获取当前用的openid -------- start
          OpenID openIDObj = new OpenID(accessToken);
          openID = openIDObj.getUserOpenID();

          if (StringUtils.isBlank(openID)) {
            logger.error("---------QQ登录回调，获取不到openID，请检查配置！------------");
            return null;
          }
        }

      } else {
        if (accessTokenObj.getAccessToken().equals("")) {
          logger.error("---------QQ登录回调，获取不到参数，请检查配置！------------");
          return null;
        }
        accessToken = accessTokenObj.getAccessToken();

        // 利用获取到的accessToken 去获取当前用的openid -------- start
        OpenID openIDObj = new OpenID(accessToken);
        openID = openIDObj.getUserOpenID();

        if (StringUtils.isBlank(openID)) {
          logger.error("---------QQ登录回调，获取不到openID，请检查配置！------------");
          return null;
        }
      }

      String qqUnionId =
          HttpRequestUtils.sendGet("https://graph.qq.com/oauth2.0/me", "access_token=" + accessToken + "&unionid=1");
      qqUnionId = qqUnionId.substring(qqUnionId.indexOf("UID"), qqUnionId.lastIndexOf('"'));

      // 获取 用户名
      String qqUserName = this.getQQName(accessToken, openID);
      this.qqName = qqUserName;
      Long currentUserId = (Long) Struts2Utils.getRequest().getSession().getAttribute("currentUserId");
      if (this.bindQQ && currentUserId != null && currentUserId != 0L) {
        bindQQ(openID, qqUserName, currentUserId, qqUnionId);
        return null;
      }
      // 标记 qq登录 记录用户名 給来登录后显示用
      Cookie cookie1 = new Cookie("qq_user_name", URLEncoder.encode(qqUserName, "utf-8"));
      this.setCookie(Struts2Utils.getResponse(), cookie1, 60 * 60 * 24);

      // 用qq的openId判断是否有关联关系
      // Long psnId = thirdLoginService.findPsnId(SysThirdUser.TYPE_QQ,
      // openID);
      // qqUnionId = qqUnionId.substring(qqUnionId.indexOf("UID"),
      // qqUnionId.lastIndexOf('"'));
      Long psnId = thirdLoginService.findUnionId(SysThirdUser.TYPE_QQ, qqUnionId);

      if (psnId != null) {
        // 更新昵称
        thirdLoginService.updateNickname(SysThirdUser.TYPE_QQ, openID, qqUserName);
        this.login(SysThirdUser.TYPE_QQ, openID, psnId, host);
        return null;
      }

      // 帐号关联或者注册新帐号
      String des3ThirdId = ServiceUtil.encodeToDes3(SysThirdUser.TYPE_QQ + "|" + openID);
      String des3QQUnionId = ServiceUtil.encodeToDes3(SysThirdUser.TYPE_QQ + "|" + qqUnionId);
      Struts2Utils.getRequest().setAttribute("des3ThirdId", des3ThirdId);
      Map<String, String> qqLoginParams = new HashMap<String, String>();
      // 将获取到的accessToken和openID放入缓存中
      qqLoginParams.put("accessToken", accessToken);
      qqLoginParams.put("openID", openID);
      qqLoginParams.put("qqUnionId", qqUnionId);
      putQQLoginParamsCache((HashMap<String, String>) qqLoginParams);
      // 因为后面的完善基本资料页面切换中英文时需要参数des3ThirdId，所以将des3ThirdId放入缓存
      if (thirdLoginParams != null) {
        thirdLoginParams.put("des3ThirdId", des3ThirdId);
        thirdLoginParams.put("des3QQUnionId", des3QQUnionId);
        super.putThirdLoginParamsCache((HashMap<String, String>) thirdLoginParams);
      } else {
        Map<String, String> params = new HashMap<String, String>();
        params.put("des3ThirdId", des3ThirdId);
        params.put("des3QQUnionId", des3QQUnionId);
        params.put("accessToken", accessToken);
        super.putThirdLoginParamsCache((HashMap<String, String>) params);

      }
      Struts2Utils.getRequest().setAttribute("host", host);
      Cookie cookie2 = new Cookie("host", URLEncoder.encode(host, "utf-8"));
      this.removeCookie(Struts2Utils.getResponse(), cookie2);
    } catch (Exception e) {
      logger.error("---------QQ登录成功回调地址出错！------------", e);
    }
    return "scm_user_check";
  }

  /**
   * 绑定QQ功能
   * 
   * @param openID
   * @param qqUserName
   * @param currentUserId
   * @throws IOException
   */
  private void bindQQ(String openID, String qqUserName, Long currentUserId, String qqUnionId) throws IOException {
    // 绑定qq跳转过来的页面
    logger.info("---------进入QQ绑定逻辑！------------");
    Struts2Utils.getRequest().getSession().removeAttribute("currentUserId");
    // 先判断QQ是否被绑定，如果绑定就提示该QQ号被占用
    Long psnId = thirdLoginService.findUnionId(SysThirdUser.TYPE_QQ, qqUnionId);
    String queryStr = "";
    if (psnId == null || psnId == 0L || psnId.longValue() == currentUserId.longValue()) {
      // 在判断当前用户是否已经被，绑定了
      SysThirdUser thirdUser = thirdLoginService.findSysThirdUser(SysThirdUser.TYPE_QQ, currentUserId);
      if (thirdUser == null) {
        thirdLoginService.saveThirdLogin(currentUserId, SysThirdUser.TYPE_QQ, openID, qqUserName, qqUnionId);
        queryStr = "QQ";
        // Struts2Utils.getResponse().sendRedirect("/psnweb/psnsetting/main?model=password&occupy=QQ");
      } else {
        queryStr = "repeatBind";
        // Struts2Utils.getResponse().sendRedirect("/psnweb/psnsetting/main?model=password&occupy=repeatBind");
      }

    } else {
      queryStr = "qqOccupy";
      // Struts2Utils.getResponse().sendRedirect("/psnweb/psnsetting/main?model=password&occupy=qqOccupy");
    }
    String requestUrl = "/oauth/bindQQ?occupy=" + queryStr;
    Struts2Utils.getResponse().sendRedirect(requestUrl);
  }

  // 第三方登录的时候跳转到一个中间页面
  @Action("/oauth/loginQQ")
  public String loginQQ() {
    // sie，根据获取首页域名判断是产品首页QQ登录还是单位首页QQ登录，因为他们登录后跳转的页面不一致
    if (StringUtils.isNotBlank(host)) {
      if (host.contains(domainrol)) {
        return "sieLoginQQ";
      } else if (domainvalidate.contains(host)) {
        return "valiLoginQQ";
      } else {
        if (host.contains("http")) {
          try {
            targetUrl = URLEncoder.encode(host, "UTF-8");
          } catch (UnsupportedEncodingException e) {
            logger.error("qq登录获取超时前的URL报错");
          }
          host = host.substring(host.indexOf(":") + 3, host.lastIndexOf("com") + 3);
        }
        return "insLoginQQ";
      }
    }
    return "loginQQ";
  }

  @Action("/oauth/bindQQ")
  public String TemplateBindQQ() {
    String occupy = Struts2Utils.getRequest().getParameter("occupy");
    Struts2Utils.getRequest().setAttribute("occupy", occupy);
    return "bindQQ";
  }

  /**
   * 模拟请求得到 用户基本信息
   * 
   * @param accessToken
   * @param openId
   * @return wcw
   */
  private String getQQName(String accessToken, String openId) {
    try {
      // 拼装 url
      StringBuilder url = new StringBuilder();
      url.append("https://graph.qq.com/user/get_user_info?");
      url.append("access_token=" + URLEncoder.encode(accessToken, "UTF-8"));
      url.append("&oauth_consumer_key=" + URLEncoder.encode(oauth_consumer_key, "UTF-8"));
      url.append("&openid=" + URLEncoder.encode(openId, "UTF-8"));
      Map map = MessageUtil.httpRequest(url.toString(), "GET", null);
      if ("0".equals(map.get("ret").toString())) {
        return map.get("nickname").toString();
      }
    } catch (Exception e) {
      logger.error("获取 qq登录人员基本信息失败");
    }
    return null;
  }

  // ------------填充unionID begin

  /**
   * 模拟请求得到 用户UnionID
   * 
   * @param accessToken
   * @param openId
   * @return wcw
   */
  public String getQQUnion_id(String openId) {

    String result = HttpRequestUtils.sendGet("https://graph.qq.com/oauth2.0/get_unionid",
        "openid=" + openId + "&client_id=101190413");

    return result.substring(result.indexOf("UID"), result.lastIndexOf('"'));
  }

  // 填充sys_third_user 表 union_id
  @Action("/oauth/fillUnionID")
  public String fillUNID() {
    List<String> thirdIds = thirdLoginService.findByType(SysThirdUser.TYPE_QQ);
    if (CollectionUtils.isNotEmpty(thirdIds)) {
      for (String thirdId : thirdIds) {
        thirdLoginService.updateUnionID(SysThirdUser.TYPE_QQ, thirdId, getQQUnion_id(thirdId));
      }
    }
    return null;
  }

  // ------------填充unionID end

  @Action("/oauth/error")
  public String showErrorPage() {
    return "error";
  }

  /**
   * 存cookie
   */
  private void setCookie(HttpServletResponse response, Cookie cookie, int date) throws Exception {
    /*
     * response.setHeader("SET-COOKIE", cookie.getName()+ "=" + cookie.getValue() +
     * ";;Path=/;Domain=scholarmate.com;Max-Age=" + date );
     */
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
   * 模拟登录
   * 
   * @param type
   * @param thirdId
   * @throws Exception
   */
  private void login(Integer type, String thirdId, Long psnId, String host) throws Exception {
    String des3ThirdId = ServiceUtil.encodeToDes3(thirdId + "|" + type);
    String queryStr = Struts2Utils.getRequest().getQueryString();
    if (StringUtils.isBlank(queryStr)) {
      queryStr = "des3ThirdId=" + des3ThirdId;
    } else {
      queryStr = queryStr + "&des3ThirdId=" + des3ThirdId + "&des3PsnId=" + ServiceUtil.encodeToDes3(psnId.toString());
    }
    if (StringUtils.isNotBlank(host) && !"".equals(host)) {
      queryStr = queryStr + "&host=" + host;
    }
    String requestUrl = "/oauth/thirdlogin/login?" + queryStr;
    Struts2Utils.getResponse().sendRedirect(requestUrl);

  }

  private void putQQLoginParamsCache(HashMap<String, String> qqLoginParams) {
    oauthCacheService.put(QQLoginAction.QQ_LOGIN_CACHE, CacheService.EXP_HOUR_1,
        QQLoginAction.QQ_LOGIN_PARAMS + Struts2Utils.getSession().getId(), qqLoginParams);
  }

  private void removeQQLoginParamsCache() {
    oauthCacheService.remove(QQLoginAction.QQ_LOGIN_CACHE,
        QQLoginAction.QQ_LOGIN_PARAMS + Struts2Utils.getSession().getId());
  }

  private Map<String, String> getQQLoginParamsCache() {
    return (Map<String, String>) oauthCacheService.get(QQLoginAction.QQ_LOGIN_CACHE,
        QQLoginAction.QQ_LOGIN_PARAMS + Struts2Utils.getSession().getId());
  }

  public String getUserCheck() {
    return userCheck;
  }

  public void setUserCheck(String userCheck) {
    this.userCheck = userCheck;
  }

  public String getQqName() {
    return qqName;
  }

  public void setQqName(String qqName) {
    this.qqName = qqName;
  }

  public String getLoginType() {
    return loginType;
  }

  public void setLoginType(String loginType) {
    this.loginType = loginType;
  }

  public String getTypeString() {
    return typeString;
  }

  public void setTypeString(String typeString) {
    this.typeString = typeString;
  }

  public String getAID() {
    return AID;
  }

  public void setAID(String aID) {
    AID = aID;
  }

  public Boolean getBindQQ() {
    return bindQQ;
  }

  public void setBindQQ(Boolean bindQQ) {
    this.bindQQ = bindQQ;
  }

  public String getParentWindowUrl() {
    return parentWindowUrl;
  }

  public void setParentWindowUrl(String parentWindowUrl) {
    this.parentWindowUrl = parentWindowUrl;
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

  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

}
