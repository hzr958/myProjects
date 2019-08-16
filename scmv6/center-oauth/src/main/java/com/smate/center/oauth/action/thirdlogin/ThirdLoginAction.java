package com.smate.center.oauth.action.thirdlogin;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.opensymphony.xwork2.ActionSupport;
import com.smate.center.oauth.cache.OauthCacheService;
import com.smate.center.oauth.consts.OauthConsts;
import com.smate.core.base.utils.cache.CacheService;
import com.smate.core.base.utils.struts2.Struts2Utils;

/**
 * 第三方登录
 * 
 * @author Scy
 * 
 */
public class ThirdLoginAction extends ActionSupport {
  /**
   * 
   */
  private static final long serialVersionUID = -1362499290500818156L;

  private Map<String, String> thirdLoginParams = new HashMap<String, String>();
  @Autowired
  private OauthCacheService oauthCacheService;

  private static final String THIRD_LOGIN_CACHE = "third_login_cache";
  private static final String THIRD_LOGIN_PARAMS = "third_login_params";

  public Map<String, String> getThirdLoginParams() {
    return thirdLoginParams;
  }

  public void setThirdLoginParams(Map<String, String> thirdLoginParams) {
    this.thirdLoginParams = thirdLoginParams;
  }

  protected void putThirdLoginParamsCache(HashMap<String, String> thirdLoginParams) {
    oauthCacheService.put(ThirdLoginAction.THIRD_LOGIN_CACHE, CacheService.EXP_HOUR_1,
        ThirdLoginAction.THIRD_LOGIN_PARAMS + Struts2Utils.getSession().getId(), thirdLoginParams);
  }

  protected void removeThirdLoginParamsCache() {
    oauthCacheService.remove(ThirdLoginAction.THIRD_LOGIN_CACHE,
        ThirdLoginAction.THIRD_LOGIN_PARAMS + Struts2Utils.getSession().getId());
  }

  protected Map<String, String> getThirdLoginParamsCache() {
    return (Map<String, String>) oauthCacheService.get(ThirdLoginAction.THIRD_LOGIN_CACHE,
        ThirdLoginAction.THIRD_LOGIN_PARAMS + Struts2Utils.getSession().getId());
  }

  protected void putWeiboLoginParamsCache(HashMap<String, String> qqLoginParams) {
    oauthCacheService.put(OauthConsts.WEIBO_LOGIN_CACHE, oauthCacheService.EXP_HOUR_1,
        OauthConsts.WEIBO_LOGIN_PARAMS + Struts2Utils.getSession().getId(), qqLoginParams);
  }

  protected void removeWeiboLoginParamsCache() {
    oauthCacheService.remove(OauthConsts.WEIBO_LOGIN_CACHE,
        OauthConsts.WEIBO_LOGIN_PARAMS + Struts2Utils.getSession().getId());
  }

  protected Map<String, String> getWeiboLoginParamsCache() {
    return (Map<String, String>) oauthCacheService.get(OauthConsts.WEIBO_LOGIN_CACHE,
        OauthConsts.WEIBO_LOGIN_PARAMS + Struts2Utils.getSession().getId());
  }

}
