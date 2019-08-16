package com.smate.center.oauth.action.wechat;

import java.net.URLDecoder;
import java.util.Map;
import java.util.Objects;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Preparable;
import com.smate.center.oauth.action.thirdlogin.ThirdLoginAction;
import com.smate.center.oauth.model.bind.ThirdBindForm;
import com.smate.center.oauth.model.consts.SmateLoginConsts;
import com.smate.center.oauth.model.thirduser.SysThirdUser;
import com.smate.center.oauth.service.bind.WeChatBindService;
import com.smate.center.oauth.service.login.OauthLoginService;
import com.smate.center.oauth.service.security.UserService;
import com.smate.center.oauth.service.thirdlogin.ThirdLoginService;
import com.smate.core.base.utils.common.CommonUtils;
import com.smate.core.base.utils.model.cas.security.User;
import com.smate.core.base.utils.model.wechat.WeChatRelation;
import com.smate.core.base.utils.security.Des3Utils;
import com.smate.core.base.utils.struts2.Struts2Utils;

/**
 * 第三方账号绑定
 * 
 * @author wuchuanwen
 */
@Results({@Result(name = "scm_user_check", location = "/WEB-INF/jsp/sns/scm_user_check.jsp")})
public class ThirdBindAction extends ThirdLoginAction implements ModelDriven<ThirdBindForm>, Preparable {
  private static final long serialVersionUID = 214099442310624776L;
  @Value("${domainscm}")
  private String domainscm;
  @Value("${wechat.open.appid}")
  private String appId;
  @Value("${wechat.appid}")
  private String appid;
  // 登录类型
  private String loginType;
  // 系统的域名
  private String host;
  private ThirdBindForm form;
  private boolean passwordError = true;// 校验账号信息错误就为false
  private boolean accountHasBind = false;// 校验账号信息, 账号是否被绑定
  private final Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private UserService userService;
  @Autowired
  private WeChatBindService weChatBindService;
  @Autowired
  private ThirdLoginService thirdLoginService;
  @Autowired
  private OauthLoginService oauthLoginService;

  /**
   * 微信绑定
   */
  @Action("/oauth/wechat/pcbind")
  public String wechatBind() {
    loginType = "3";
    return "scm_user_check";
  }

  /**
   * 第三方登录关联
   */
  @Action("/oauth/thirdlogin/scmloginconnect")
  public String wechatConnect() {
    try {
      this.setHost(Struts2Utils.getRequest().getParameter("host"));
      String userName = form.getUserName(), password = form.getPasswords();
      if (StringUtils.isNotBlank(userName) && StringUtils.isNotBlank(password)) {
        User user = userService.getUser(userName, DigestUtils.md5Hex(password.trim()));
        if (user == null) {
          passwordError = false;
          return "scm_user_check";
        }
        // 验证成功
        form.setPsnId(user.getId());
        if (StringUtils.isNotBlank(loginType)) {
          if (SysThirdUser.TYPE_QQ.toString().equals(loginType)) {
            // 判断当前人是否已经被绑定了
            SysThirdUser thirdUser = thirdLoginService.findSysThirdUser(SysThirdUser.TYPE_QQ, form.getPsnId());
            if (thirdUser != null) {
              accountHasBind = true;
              return "scm_user_check";
            }
            // QQ关联
            connectQQ(form);
          } else if (SysThirdUser.TYPE_WECHAT.toString().equals(loginType)) {
            WeChatRelation weChatRelation = weChatBindService.findWeChatRelationByPsnId(form.getPsnId());
            if (weChatRelation != null) {
              accountHasBind = true;
              return "scm_user_check";
            }
            // 微信关联
            connectWechat(form);
          } else if (SysThirdUser.TYPE_WEIBO.toString().equals(loginType)) {
            SysThirdUser thirdUser = thirdLoginService.findSysThirdUser(SysThirdUser.TYPE_WEIBO, form.getPsnId());
            if (thirdUser != null) {
              accountHasBind = true;
              return "scm_user_check";
            }
            // 微博关联
            connectWeibo(form);
          }
        }
      }
    } catch (Exception e) {
      logger.error("第三方账号登录绑定操作异常，psnId={}, bindType={}", form.getPsnId(), form.getBindType(), e);
    }
    return "scm_user_check";
  }

  /**
   * 第三方注册关联
   */
  @Action("/oauth/thirdlogin/scmregist")
  public String registConnect() {
    if (StringUtils.isNotBlank(form.getDes3PsnId())) {
      String psnIdStr = Des3Utils.decodeFromDes3(form.getDes3PsnId());
      if (StringUtils.isNotBlank(psnIdStr)) {
        form.setPsnId(Long.parseLong(psnIdStr));
      }
    } else if (StringUtils.isNotBlank(Objects.toString(Struts2Utils.getSession().getAttribute("des3PsnId"), ""))) {
      // 赋值人员ID给form
      form.setDes3PsnId(Objects.toString(Struts2Utils.getSession().getAttribute("des3PsnId"), ""));
    }
    try {
      String des3ThirdId = StringUtils.isNotBlank(form.getDes3ThirdId()) ? form.getDes3ThirdId()
          : Struts2Utils.getCookieVal(Struts2Utils.getRequest(), "des3ThirdId");
      if (StringUtils.isNotBlank(des3ThirdId)) {
        String thirdIdStr = Des3Utils.decodeFromDes3(des3ThirdId);
        String[] strArray = StringUtils.stripAll(StringUtils.split(thirdIdStr, "|"));
        if (strArray.length > 1) {
          form.setUnionid(strArray[1]);
        } else {
          form.setMsg("没有获取到微信信息");
          return "scm_user_check";
        }
        this.setLoginType(strArray[0]);
        if (SysThirdUser.TYPE_QQ.toString().equals(loginType)) {
          // QQ注册关联
          connectQQ(form);
        } else if (SysThirdUser.TYPE_WECHAT.toString().equals(loginType)) {
          // 微信注册关联
          connectWechat(form);
        } else if (SysThirdUser.TYPE_WEIBO.toString().equals(loginType)) {
          // 新浪微博注册关联
          connectWeibo(form);
        }
      }
    } catch (Exception e) {
      logger.error("绑定失败,psnId=" + form.getPsnId(), e);
    }
    return null;
  }

  /**
   * 微信账号关联
   *
   * @param form
   * @return
   */
  private String connectWechat(ThirdBindForm form) {
    form.setBindType(1);
    form.setOpenid("0");
    if (StringUtils.isNotBlank(form.getDes3ThirdId())) {
      String thirdIdStr = Des3Utils.decodeFromDes3(form.getDes3ThirdId());
      String[] strArray = StringUtils.stripAll(StringUtils.split(thirdIdStr, "|"));
      if (strArray.length > 1) {
        form.setUnionid(strArray[1]);
      } else {
        form.setMsg("没有获取到微信信息");
        return "scm_user_check";
      }
    } else if (StringUtils.isNotBlank(form.getDes3Unionid())) {
      form.setUnionid(Des3Utils.decodeFromDes3(form.getDes3Unionid()));
    } else {
      form.setMsg("没有获取到微信信息");
      return "scm_user_check";
    }
    try {
      weChatBindService.pcBindUser(form);
    } catch (Exception e) {
      logger.error("绑定失败,psnId=" + form.getPsnId(), e);
    }
    return null;
  }

  /**
   * QQ关联
   *
   * @param form
   * @return
   */
  private String connectQQ(ThirdBindForm form) {
    Map<String, String> thirdLoginParamsCache = super.getThirdLoginParamsCache();
    String qqUnionIdStr = Des3Utils.decodeFromDes3(thirdLoginParamsCache.get("des3QQUnionId"));
    if (StringUtils.isNotEmpty(qqUnionIdStr) && StringUtils.isNotBlank(form.getDes3ThirdId())) {
      String[] strArray = StringUtils.stripAll(StringUtils.split(qqUnionIdStr, "|"));
      if (strArray.length > 1) {
        // 设置qqUnionID
        form.setQqUnionId(strArray[1]);
        // 设置thirdID
        String thirdIdStr = Des3Utils.decodeFromDes3(form.getDes3ThirdId());
        String[] thirdIdStrArray = StringUtils.stripAll(StringUtils.split(thirdIdStr, "|"));
        if (strArray.length > 1) {
          form.setOpenid(thirdIdStrArray[1]);
        }
      } else {
        form.setMsg("没有获取到QQ第三方qqUnionID信息");
        return "scm_user_check";
      }
    } else {
      form.setMsg("没有获取到第三方信息");
      return "scm_user_check";
    }

    /*
     * if (StringUtils.isNotBlank(form.getDes3ThirdId())) { String thirdIdStr =
     * Des3Utils.decodeFromDes3(form.getDes3ThirdId()); String[] strArray =
     * StringUtils.stripAll(StringUtils.split(thirdIdStr, "|")); if (strArray.length > 1) {
     * form.setOpenid(strArray[1]); } else { form.setMsg("没有获取到第三方信息"); return "scm_user_check"; } }
     * else { form.setMsg("没有获取到第三方信息"); return "scm_user_check"; }
     */

    try {
      thirdLoginService.saveThirdLogin2(form.getPsnId(), Integer.valueOf(loginType), form.getOpenid(),
          form.getQqUnionId(), form.getQqName());
      return this.login();
    } catch (Exception e) {
      logger.error("绑定失败,psnId=" + form.getPsnId(), e);
    }
    return null;
  }



  /**
   * 微博账号关联
   *
   * @param form
   * @return
   * @throws Exception
   */
  private String connectWeibo(ThirdBindForm form) throws Exception {
    form.setBindType(2);
    form.setOpenid("0");
    String des3ThirdId = StringUtils.isNotBlank(form.getDes3ThirdId()) ? form.getDes3ThirdId()
        : Struts2Utils.getCookieVal(Struts2Utils.getRequest(), "des3ThirdId");
    if (StringUtils.isNotBlank(des3ThirdId)) {
      form.setDes3ThirdId(des3ThirdId);
      String thirdIdStr = Des3Utils.decodeFromDes3(des3ThirdId);
      String[] strArray = StringUtils.stripAll(StringUtils.split(thirdIdStr, "|"));
      if (strArray.length > 1) {
        form.setWeiboUid(strArray[1]);
        String weiboNickName = StringUtils.isNotBlank(form.getWeiboNickname()) ? form.getWeiboNickname()
            : URLDecoder.decode(Objects.toString(Struts2Utils.getCookieVal(Struts2Utils.getRequest(), "nickName"), ""),
                "utf-8");
        thirdLoginService.saveThirdLogin2(form.getPsnId(), Integer.valueOf(loginType), form.getOpenid(), strArray[1],
            weiboNickName);
        return this.login();
      }
    }
    if (StringUtils.isBlank(form.getWeiboUid())) {
      form.setMsg("没有获取到微博uid信息");
      return "scm_user_check";
    }
    return null;
  }

  /**
   * 模拟登录
   *
   * @throws Exception
   */
  @Action("/oauth/thirdlogin/login")
  public String login() throws Exception {
    if (StringUtils.isBlank(form.getDes3ThirdId())) {
      logger.error("---------模拟登录，des3ThirdId为空------------");
      return null;
    }
    // 初次关联时，即是要注册科研之友账号时获取到的host.
    if (StringUtils.isBlank(host)) {
      host = Struts2Utils.getParameter("host");
    }
    host = StringUtils.defaultString(host, "");
    // 获取openId和AID
    Long openId = oauthLoginService.getOpenId(SmateLoginConsts.SNS_DEFAULT_TOKEN, form.getPsnId(), 2);
    String AID = oauthLoginService.getAutoLoginAID(openId, SmateLoginConsts.SNS_REMEMBER_ME);
    if (CommonUtils.compareIntegerValue(SysThirdUser.TYPE_WEIBO, form.getBindType())) {
      this.deleteWeiboBindParams();
      Struts2Utils.getResponse().sendRedirect("/oauth/weibo/refresh?AID=" + AID);
    } else {
      Struts2Utils.getResponse().sendRedirect("/oauth/loginQQ" + "?AID=" + AID + "&host=" + host);
    }
    return null;
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
      form = new ThirdBindForm();
    }
  }

  @Override
  public ThirdBindForm getModel() {
    return form;
  }

  public boolean isPasswordError() {
    return passwordError;
  }

  public void setPasswordError(boolean passwordError) {
    this.passwordError = passwordError;
  }

  public String getLoginType() {
    return loginType;
  }

  public void setLoginType(String loginType) {
    this.loginType = loginType;
  }

  public String getHost() {
    return host;
  }

  public void setHost(String host) {
    this.host = host;
  }

  public boolean isAccountHasBind() {
    return accountHasBind;
  }

  public void setAccountHasBind(boolean accountHasBind) {
    this.accountHasBind = accountHasBind;
  }
}
