package com.smate.center.oauth.action.login;

import java.io.IOException;
import java.io.Serializable;
import java.util.Map;
import java.util.Objects;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Actions;
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
import com.smate.center.oauth.model.bind.ThirdBindForm;
import com.smate.center.oauth.model.consts.OpenConsts;
import com.smate.center.oauth.model.consts.SmateLoginConsts;
import com.smate.center.oauth.service.bind.WeChatBindService;
import com.smate.center.oauth.service.login.OauthLoginService;
import com.smate.center.oauth.service.login.OauthService;
import com.smate.center.oauth.service.security.OauthUserDetailsService;
import com.smate.core.base.oauth.model.OauthLoginForm;
import com.smate.core.base.utils.cache.CacheConst;
import com.smate.core.base.utils.constant.security.SecurityConstants;
import com.smate.core.base.utils.mobile.SmateMobileUtils;
import com.smate.core.base.utils.model.wechat.WeChatRelation;
import com.smate.core.base.utils.number.NumberUtils;
import com.smate.core.base.utils.rsacode.PublicKeyMap;
import com.smate.core.base.utils.rsacode.RSAUtils;
import com.smate.core.base.utils.security.Des3Utils;
import com.smate.core.base.utils.service.security.AutoLoginOauthInfoService;
import com.smate.core.base.utils.string.StringUtils;
import com.smate.core.base.utils.struts2.Struts2Utils;
import com.smate.core.base.utils.wechat.OAuth2Service;

/**
 * 自动登录 验证接口(只能给业务系统拦截器请求)
 * 
 * @author tsz
 *
 */

@Results({@Result(name = "reload", location = "index", type = "redirect"),
    @Result(name = "error", location = "/WEB-INF/jsp/sns/V_SNS_index.jsp"),
    @Result(name = "redirectUrl", location = "${forwardUrl}", type = "redirect")})
public class AutoLoginAction extends ActionSupport implements ModelDriven<OauthLoginForm>, Preparable {

  /**
   * 
   */
  private static final long serialVersionUID = -4233512169345586923L;
  public static final String SPRING_SECURITY_CONTEXT_KEY = "SPRING_SECURITY_CONTEXT";
  protected final Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private OauthCacheService oauthCacheService;
  @Autowired
  private OAuth2Service oAuth2Service;
  @Autowired
  private AutoLoginOauthInfoService autoLoginOauthInfoService;
  @Autowired
  private OauthUserDetailsService userDetailsService;
  private OauthLoginForm form;
  @Autowired
  private OauthLoginService oauthLoginService;
  @Value("${domainoauth}")
  private String domainoauth;
  @Autowired
  private OauthService oauthService;
  @Autowired
  private WeChatBindService weChatBindService;

  /**
   * 自动登录 权限加密 验证
   * 
   * @return
   * @throws Exception
   * @throws Exception
   */
  @Actions({@Action("/oauth/autologin")})
  public String Autologin() {
    try {
      // 1.判断 加密id 是否正确或过期。
      String AID = Struts2Utils.getParameter(SecurityConstants.AUTO_LOGIN_PARAMETER_NAME);
      if (AID != null && !"".equals(AID)) {
        // 判断参数有没有过期
        Object object = oauthCacheService.get(SecurityConstants.AUTO_LOGIN_INFO_CACHE, AID);
        if (object != null && !"".equals(object.toString())) {
          String[] temp = object.toString().split(",");
          String psnId = temp[0];
          String token = temp[1];
          String targetUrl = oauthLoginService.oauthRebuildTargetUrl(form.getService());
          UserDetails uDetails =
              userDetailsService.loadUserFromSys(psnId, form.getSys(), Des3Utils.decodeFromDes3(form.getService()));
          String sesseionId = Struts2Utils.getSession().getId();
          Map<String, Object> detailsMap =
              oauthService.buildUserDetailsMap(sesseionId, targetUrl, form.getSys(), (UserDetails) uDetails);
          detailsMap.remove("userId");
          detailsMap.put("userId", psnId);
          oauthCacheService.put(SecurityConstants.USER_DETAILS_INFO_CACHE, SecurityConstants.USER_DETAILS_CACHE_TIME,
              sesseionId, (Serializable) detailsMap);
          // 更新AutoLoginOauthInfo表的登录次数 使用时间
          oauthLoginService.updateAutoLoinUserTime(AID);
          // 移除自动登录前，用户登入的用户信息SCM-14525
          oauthCacheService.remove(CacheConst.NEW_USER_DATA_CACHE, Struts2Utils.getSession().getId());
          oauthLoginService.saveAutoLoginLog(NumberUtils.toLong(psnId), Struts2Utils.getRemoteAddr(), targetUrl, 10);
          // 设置参数到cookie
          this.buildNewCookies(NumberUtils.toLong(psnId));
          Struts2Utils.getResponse().sendRedirect(targetUrl);
          return null;
        }
        // 第三方系统跳转登录，只一次有效
        if ("1".equals(Struts2Utils.getParameter("threesys"))) {
          oauthCacheService.remove(SecurityConstants.AUTO_LOGIN_INFO_CACHE, AID);
        }
      }
      // 不正确 重向到登录页面
      Struts2Utils.getResponse().sendRedirect(domainoauth + "/oauth/mobile/index?service=" + form.getService());
    } catch (Exception e) {
      logger.error("自动登录出错了", e);
      form.setSysDomain(domainoauth);
      return "error";
    }
    return null;
  }

  /**
   * 获取系数和指数
   * 
   * @return
   * @throws Exception
   */
  @Action("/oauth/rsa")
  public String keyPair() {
    try {
      PublicKeyMap publicKeyMap = RSAUtils.getPublicKeyMap();
      /* RSAPrivateKey rSAPrivateKey =RSAUtils.getDefaultPrivateKey(); */
      /* PrivateKeyMap privateKeyMap = RSAUtils.getPrivateKeyMap(); */
      if (publicKeyMap != null) {
        Struts2Utils.renderJson(publicKeyMap, "encoding:utf-8");
      }
    } catch (Exception e) {
      logger.error("RSA加密出错", e);
    }
    return null;
  }



  /**
   * 通过AID绑定微信
   * 
   * @throws IOException
   */
  @Action("/oauth/wx/bind")
  public String autoBindWechat() throws IOException {
    ThirdBindForm bindForm = new ThirdBindForm();
    try {
      String AID = form.getAID();
      String wxCode = form.getCode();
      if (SmateMobileUtils.isWeChatBrowser(Struts2Utils.getRequest()) && StringUtils.isNotBlank(AID)
          && StringUtils.isNotBlank(wxCode) && form.getBindType() != null) {
        // 解析AID获取人员openID并判断人员账号是否已绑定过微信
        boolean psnHasBinded = psnHasBindWx(AID, bindForm);
        // 通过wxCode获取微信unionId，并判断微信是否已绑定过科研之友账号
        boolean wxHasBinded = !psnHasBinded ? wxHasBindSmatePsn(wxCode, bindForm) : false;
        // 都没绑定过的执行绑定操作
        if (!psnHasBinded && !wxHasBinded && StringUtils.isNotBlank(bindForm.getUnionid())
            && NumberUtils.isNotNullOrZero(bindForm.getScmOpenId())) {
          bindForm.setBindType(form.getBindType());
          weChatBindService.persistenceWeChatRelation(bindForm);
        }
      }
    } catch (Exception e) {
      logger.error("自动绑定微信异常, psnOpenId={}, wxUnionId={}, AID={}", bindForm.getScmOpenId(), bindForm.getUnionid(),
          form.getAID(), e);
    }
    Struts2Utils.getResponse().sendRedirect(StringUtils.defaultIfBlank(Des3Utils.decodeFromDes3(form.getForwardUrl()),
        "/dynweb/mobile/dynshow?AID=" + form.getAID()));
    return null;
  }


  /**
   * 人员是否已绑定过微信
   * 
   * @param AID
   * @param bindForm
   * @return
   */
  private boolean psnHasBindWx(String AID, ThirdBindForm bindForm) {
    String loginInfo = autoLoginOauthInfoService.checkAutoLoginOauth(AID);
    if (StringUtils.isNotBlank(loginInfo)) {
      String psnIdStr = loginInfo.split(",")[0];
      Long psnId = NumberUtils.isCreatable(psnIdStr) ? NumberUtils.toLong(psnIdStr, 0L) : 0L;
      if (NumberUtils.isNotNullOrZero(psnId)) {
        bindForm.setScmOpenId(weChatBindService.findPsnOpenIdInUserUnion(psnId));
        // 校验人员ID对应的账号是否绑定过微信
        WeChatRelation relation = weChatBindService.findWeChatRelationByPsnId(psnId);
        if (relation == null) {
          return false;
        }
      }
    }
    return true;
  }

  /**
   * 
   * @param wxCode
   * @param bindForm
   * @return
   * @throws Exception
   */
  private boolean wxHasBindSmatePsn(String wxCode, ThirdBindForm bindForm) throws Exception {
    // 通过code获取wxOpenId
    String weChatOpenId = oAuth2Service.getWeChatOpenId(wxCode);
    // 通过公众号access_token和wxOpenId获取用户信息，获取unionId
    String weChatToken = oAuth2Service.getWeChatToken();
    logger.warn("获取微信openId为：" + weChatOpenId + ", accessToken为：" + weChatToken);
    Map<String, Object> weChatInfo = oAuth2Service.getWeChatInfo(weChatToken, weChatOpenId);
    if (weChatInfo != null && weChatInfo.get("unionid") != null) {
      bindForm.setWechatName(Objects.toString(weChatInfo.get("nickname"), ""));
      bindForm.setUnionid(Objects.toString(weChatInfo.get("unionid"), ""));
      bindForm.setOpenid(weChatOpenId);
    }
    // 校验微信是否已绑定过账号
    if (StringUtils.isNotBlank(bindForm.getUnionid())) {
      return weChatBindService.wxHasBinded(bindForm.getUnionid());
    }
    return true;
  }

  /**
   * 带AID参数的链接自动登录时添加参数到cookie
   */
  public void buildNewCookies(Long psnId) {
    // 记住登录功能不应该影响正常登录，获取AID出错的话捕获错误，继续正常登录
    try {
      HttpServletResponse response = Struts2Utils.getResponse();
      if (form.getRememberMe()) {
        Long openId = oauthLoginService.getOpenId(OpenConsts.SMATE_TOKEN, psnId, OpenConsts.OPENID_CREATE_TYPE_7);
        String AID = null;
        if (openId != null && openId != 0L) {
          AID = oauthLoginService.getAutoLoginAID(openId, SmateLoginConsts.SNS_REMEMBER_ME);
        }
        if (StringUtils.isNotBlank(AID)) {
          this.setCookie(response, new Cookie(SecurityConstants.AUTO_LOGIN_PARAMETER_NAME, AID), AID,
              SecurityConstants.REMEMBER_ME_TIME);
        }
      }
      // 添加已登录标识，自动登录用
      this.setCookie(response, new Cookie(SecurityConstants.OAUTH_LOGIN, "true"), "true",
          SecurityConstants.REMEMBER_ME_TIME);
      // 添加系统标识
      this.setCookie(response, new Cookie("SYS", form.getSys()), form.getSys(), SecurityConstants.REMEMBER_ME_TIME);
    } catch (Exception e) {
      logger.error("自动登录时获取AID或将参数放入cookie中出错， psnId=" + psnId, e);
    }
  }

  @Override
  public void prepare() throws Exception {
    if (form == null) {
      form = new OauthLoginForm();
    }
  }

  @Override
  public OauthLoginForm getModel() {
    return form;
  }

  public OauthLoginForm getForm() {
    return form;
  }

  public void setForm(OauthLoginForm form) {
    this.form = form;
  }

  /**
   * 设置参数到cookie中
   * 
   * @param response
   * @param cookie
   * @param paramVal
   * @param maxAge
   * @param domain
   */
  private void setCookie(HttpServletResponse response, Cookie cookie, String paramVal, int maxAge) {
    if (cookie != null) {
      // cookie.setSecure(true);
      cookie.setHttpOnly(true);
      cookie.setValue(paramVal);
      cookie.setMaxAge(maxAge);
      cookie.setDomain(".scholarmate.com");
      cookie.setPath("/");
      response.addCookie(cookie);
    }
  }

}
