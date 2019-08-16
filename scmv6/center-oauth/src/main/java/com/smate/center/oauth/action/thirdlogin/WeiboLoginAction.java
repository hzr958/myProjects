package com.smate.center.oauth.action.thirdlogin;

import java.net.URLEncoder;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Preparable;
import com.smate.center.oauth.consts.OauthConsts;
import com.smate.center.oauth.model.consts.SmateLoginConsts;
import com.smate.center.oauth.model.thirduser.SysThirdUser;
import com.smate.center.oauth.model.thirduser.ThirdLoginForm;
import com.smate.center.oauth.service.login.OauthLoginService;
import com.smate.center.oauth.service.thirdlogin.ThirdLoginService;
import com.smate.core.base.utils.security.Des3Utils;
import com.smate.core.base.utils.string.StringUtils;
import com.smate.core.base.utils.struts2.Struts2Utils;

import weibo4j.Account;
import weibo4j.Oauth;
import weibo4j.Users;
import weibo4j.http.AccessToken;
import weibo4j.model.User;
import weibo4j.model.WeiboException;
import weibo4j.org.json.JSONException;
import weibo4j.org.json.JSONObject;

/**
 * 新浪微博登录
 * 
 * @author wsn
 * @date Mar 27, 2019
 */
@Results({@Result(name = "scm_user_check", location = "/WEB-INF/jsp/sns/scm_user_check.jsp"),
    @Result(name = "close_frame", location = "/WEB-INF/jsp/sns/scm_tempelete_login.jsp"),
    @Result(name = "redirectAction", location = "${forwardUrl}", type = "redirect")})
public class WeiboLoginAction extends ThirdLoginAction implements ModelDriven<ThirdLoginForm>, Preparable {

  private static final long serialVersionUID = -5464646876435L;
  protected Logger logger = LoggerFactory.getLogger(getClass());
  private ThirdLoginForm form;
  @Autowired
  private ThirdLoginService thirdLoginService;
  @Autowired
  private OauthLoginService oauthLoginService;
  @Value("${domainscm}")
  private String domainscm;

  // 创建应用后的appkey
  // private static final String OAUTH_CONSUMER_KEY = "4136564458";


  /**
   * 跳转到新浪微博登录授权认证界面
   * 
   * @return
   * @throws Exception
   */
  @Action("/oauth/weibo/login")
  public String weiboLogin() throws Exception {
    try {
      // 去除登录的缓存
      removeWeiboLoginParamsCache();
      // 重定向到新浪微博授权认证页面
      Struts2Utils.getResponse()
          .sendRedirect(new Oauth().authorize(OauthConsts.RESPONSE_TYPE, OauthConsts.WEIBO_LOGIN_STATE));
    } catch (Exception e) {
      logger.error("跳转到新浪微博授权认证界面出错", e);
    }
    return null;
  }

  @Action("/oauth/weibo/callback")
  public String weiboLoginAfter() {
    try {
      // 拒绝授权时跳转中间页面，关掉弹窗
      if (SmateLoginConsts.WEIBO_REJECT_LOGIN_CODE.equalsIgnoreCase(form.getError_code())) {
        return "close_frame";
      }
      // 如果是微博那边的回调而非刷新页面移除第三方登录相关的参数
      String isWeiboCall = Struts2Utils.getCookieVal(Struts2Utils.getRequest(), "isWeiboCall");
      if ("true".equalsIgnoreCase(isWeiboCall)) {
        this.deleteWeiboBindParams();
      }
      // 获取微博用户信息
      this.getWeiboUserInfo();
      // 查询该登录的微博账号是否有关联过科研之友的账号
      Long psnId = thirdLoginService.findUnionId(SysThirdUser.TYPE_WEIBO, form.getUnionId());
      // 关联过就直接获取AID自动登录
      if (psnId != null) {
        return this.getAID(psnId);
      } else {
        // 未关联过就将绑定账号所需的微博相关参数放入cookie
        this.dealWithWeiboBindParams();
      }
    } catch (Exception e) {
      logger.error("----------微博登录回调处理异常，code={}--------", form.getCode(), e);
    }
    return "scm_user_check";
  }

  @Action("/oauth/weibo/refresh")
  public String refreshWeiboLogin() {
    return "close_frame";
  }


  /**
   * 模拟登录
   * 
   * @param type
   * @param thirdId
   * @throws Exception
   */
  private String getAID(Long psnId) throws Exception {
    Long openId = oauthLoginService.getOpenId(SmateLoginConsts.SNS_DEFAULT_TOKEN, psnId, 2);
    String AID = oauthLoginService.getAutoLoginAID(openId, SmateLoginConsts.SNS_REMEMBER_ME);
    // 删除微博相关参数
    this.deleteWeiboBindParams();
    form.setAID(AID);
    return "close_frame";
  }


  /**
   * 获取微博用户信息
   * 
   * @return
   * @throws WeiboException
   * @throws JSONException
   */
  private User getWeiboUserInfo() throws WeiboException, JSONException {
    Oauth oauth2 = new Oauth();
    HttpServletRequest request = Struts2Utils.getRequest();
    String accessToken = Struts2Utils.getCookieVal(request, "accessToken");
    String des3ThirdId = Struts2Utils.getCookieVal(request, "des3ThirdId");
    // 在切换中英文时，要从缓存中获取新浪微博登录后返回的accessToken，如果缓存为空，则通过accessTokenObj获取
    if (StringUtils.isNotBlank(des3ThirdId) && StringUtils.isNotBlank(accessToken)) {
      form.setAccessToken(accessToken);
      form.setUnionId(Des3Utils.decodeFromDes3(des3ThirdId).split("|")[1]);
    } else {
      AccessToken accessTokenObj = oauth2.getAccessTokenByCode(form.getCode());
      form.setAccessToken(accessTokenObj.getAccessToken());
      logger.info("------------------------新浪微博登录回调通过code获取accessToken={}------------------------------",
          form.getAccessToken());
      Account account = new Account(form.getAccessToken());
      JSONObject uidJson = account.getUid();
      form.setUnionId(uidJson.getString("uid"));
    }
    logger.info("------------------------新浪微博登录回调获取用户UID={}------------------------------", form.getUnionId());
    Users users = new Users(form.getAccessToken());
    User weiboUser = users.showUserById(form.getUnionId());
    if (weiboUser != null) {
      form.setWeiboNickName(weiboUser.getScreenName());
    }
    return weiboUser;
  }


  /**
   * 处理绑定微博账号所需参数
   * 
   * @throws Exception
   */
  protected void dealWithWeiboBindParams() throws Exception {
    form.setLoginType(SysThirdUser.TYPE_WEIBO.toString());
    String domain = "scholarmate.com";
    String path = "/";
    HttpServletResponse response = Struts2Utils.getResponse();
    // 将微博昵称放入cookie
    Cookie nickNameCookie = new Cookie("nickName", URLEncoder.encode(form.getWeiboNickName(), "utf-8"));
    Struts2Utils.setCookie(response, nickNameCookie, "86400", domain, path);
    // 将微博uid放入cookie
    Cookie thirdIdCookie =
        new Cookie("des3ThirdId", Des3Utils.encodeToDes3(SysThirdUser.TYPE_WEIBO + "|" + form.getUnionId()));
    Struts2Utils.setCookie(response, thirdIdCookie, "86400", domain, path);
    // 将微博accessToken放cookie
    Cookie accessTokenCookie = new Cookie("thirdToken", form.getAccessToken());
    Struts2Utils.setCookie(response, accessTokenCookie, "86400", domain, path);
    // 将微博回调标记放cookie,记得点击微博登录的时候清空或改为true
    Cookie callbackCookie = new Cookie("isWeiboCall", "false");
    Struts2Utils.setCookie(response, callbackCookie, "86400", domain, path);
  }

  /**
   * 删除微博登录相关参数
   * 
   * @throws Exception
   */
  protected void deleteWeiboBindParams() throws Exception {
    String domain = "scholarmate.com";
    String path = "/";
    HttpServletResponse response = Struts2Utils.getResponse();
    HttpServletRequest request = Struts2Utils.getRequest();
    // 将微博昵称cookie删除
    Cookie nickNameCookie = Struts2Utils.getCookie(request, "nickName");
    if (nickNameCookie != null) {
      Struts2Utils.setCookie(response, nickNameCookie, null, "0", domain, path);
    }
    // 将微博uid cookie删除
    Cookie thirdIdCookie = Struts2Utils.getCookie(request, "des3ThirdId");
    if (thirdIdCookie != null) {
      Struts2Utils.setCookie(response, thirdIdCookie, null, "0", domain, path);
    }
    // 将微博accessTokencookie删除
    Cookie accessTokenCookie = Struts2Utils.getCookie(request, "thirdToken");
    if (accessTokenCookie != null) {
      Struts2Utils.setCookie(response, accessTokenCookie, null, "0", domain, path);
    }
    // 将微博回调标记cookie删除,记得点击微博登录的时候清空或改为true
    Cookie callbackCookie = Struts2Utils.getCookie(request, "isWeiboCall");
    if (callbackCookie != null) {
      Struts2Utils.setCookie(response, callbackCookie, null, "0", domain, path);
    }
  }

  @Override
  public void prepare() throws Exception {
    if (form == null) {
      form = new ThirdLoginForm();
    }
  }

  @Override
  public ThirdLoginForm getModel() {
    return form;
  }



}
