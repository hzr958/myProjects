package com.smate.center.oauth.action.login;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.convention.annotation.Action;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;

import com.opensymphony.xwork2.ActionSupport;
import com.smate.center.oauth.cache.OauthCacheService;
import com.smate.core.base.app.model.AppAuthToken;
import com.smate.core.base.app.service.AppAuthTokenService;
import com.smate.core.base.oauth.model.OauthLoginForm;
import com.smate.core.base.utils.app.AppActionUtils;
import com.smate.core.base.utils.cache.CacheConst;
import com.smate.core.base.utils.common.SystemBaseContants;
import com.smate.core.base.utils.constant.IOSHttpStatus;
import com.smate.core.base.utils.constant.security.SecurityConstants;
import com.smate.core.base.utils.number.NumberUtils;
import com.smate.core.base.utils.security.Des3Utils;
import com.smate.core.base.utils.security.TheadLocalInsId;
import com.smate.core.base.utils.security.TheadLocalNodeId;
import com.smate.core.base.utils.security.TheadLocalPsnId;
import com.smate.core.base.utils.security.TheadLocalRoleId;
import com.smate.core.base.utils.security.TheadLocalUnitId;
import com.smate.core.base.utils.struts2.Struts2Utils;

/**
 * APP登陆成功处理
 * 
 * @author LJ
 *
 *         2017年10月24日
 */
public class APPIndexAction extends ActionSupport {
  private static final long serialVersionUID = -8187087331091991886L;
  private OauthLoginForm form;
  protected static final String OAUTH_LOGIN = "OAUTH_LOGIN";
  @Autowired
  private OauthCacheService oauthCacheService;
  @Autowired
  private AppAuthTokenService appAuthTokenService;
  private Long appflag;
  private String status = IOSHttpStatus.NOT_MODIFIED;// 默认状态
  private final Logger logger = LoggerFactory.getLogger(getClass());
  @Value("${domainscm}")
  private String domainScm;

  @Action("/oauth/app/index")
  public String appIndex() {
    Map<String, String> map = new HashMap<String, String>();
    try {
      // 清理缓存
      String sesseionId = Struts2Utils.getSession().getId();
      oauthCacheService.remove(SecurityConstants.USER_DETAILS_INFO_CACHE, sesseionId);
      oauthCacheService.remove(CacheConst.NEW_USER_DATA_CACHE, sesseionId);
      Struts2Utils.getSession().invalidate();
      SecurityContextHolder.clearContext();
      // 清空线程中的信息
      TheadLocalPsnId.setPsnId(0L);
      TheadLocalNodeId.setCurrentUserNodeId(0);
      TheadLocalRoleId.setRoleId(0);
      TheadLocalUnitId.setUnitId(0L);
      TheadLocalInsId.setInsId(0L);
      // 清空 cookie自动登录信息，防止浏览器测试用token失效
      Cookie cookie = Struts2Utils.getCookie(Struts2Utils.getRequest(), "AID");
      setCookie(Struts2Utils.getResponse(), cookie, null, null);

      String remoteAddr = Struts2Utils.getRemoteAddr();// 远程ip
      Long psnId = appflag;
      if (psnId == null || psnId == 0L) {
        AppActionUtils.renderAPPReturnJson("bad request", 0, IOSHttpStatus.BAD_REQUEST);
        return null;
      }
      long currentTimeMillis = System.currentTimeMillis();
      String des3PsnId = Des3Utils.encodeToDes3(psnId.toString());
      String encryptstr = currentTimeMillis + ":" + psnId;
      String token = Des3Utils.encodeToDes3(encryptstr);
      if (appAuthTokenService.getToken(psnId) == null) {
        appAuthTokenService.saveToken(new AppAuthToken(psnId, new Date(), token));
      } else {
        appAuthTokenService.updateToken(psnId, token, new Date());
      }
      map.put("des3PsnId", des3PsnId);
      /*
       * oauthCacheService.put(appUserTokenCache, 60 * 60 * 24 * 90, psnId.toString(), token); String
       * serializable = (String) oauthCacheService.get(appUserTokenCache, psnId.toString());
       */
      map.put("token", token);
      status = IOSHttpStatus.OK;
    } catch (Exception e) {
      status = IOSHttpStatus.INTERNAL_SERVER_ERROR;
      logger.error("app登陆跳转出错,psnId" + form.getPsnId(), e);
    }
    AppActionUtils.renderAPPReturnJson(map, 0, status);
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

  private void setCookie(HttpServletResponse response, Cookie cookie, String oauth, String maxAge) {
    if (cookie == null) {
      cookie = new Cookie(OAUTH_LOGIN, oauth);
    }
    cookie.setValue(oauth);
    cookie.setMaxAge(NumberUtils.toInt(maxAge, -1));
    cookie.setDomain(SystemBaseContants.IRIS_SNS_DOMAIN);
    cookie.setPath("/");

    response.addCookie(cookie);

  }

  public OauthLoginForm getForm() {
    return form;
  }

  public void setForm(OauthLoginForm form) {
    this.form = form;
  }

  public Long getAppflag() {
    return appflag;
  }

  public void setAppflag(Long appflag) {
    this.appflag = appflag;
  }

}
