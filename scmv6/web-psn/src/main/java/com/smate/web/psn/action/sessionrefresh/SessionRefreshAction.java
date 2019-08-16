package com.smate.web.psn.action.sessionrefresh;

import java.io.Serializable;

import org.apache.struts2.convention.annotation.Action;
import org.springframework.beans.factory.annotation.Autowired;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.Preparable;
import com.smate.core.base.utils.constant.security.SecurityConstants;
import com.smate.core.base.utils.struts2.Struts2Utils;
import com.smate.web.psn.cache.PsnCacheService;

/**
 * 前端页面定时发送请求，重新缓存权限信息 延长权限缓存时间且保持session有效时间
 *
 * @author wsn
 * @createTime 2017年6月2日 下午4:19:47
 *
 */
public class SessionRefreshAction extends ActionSupport implements Preparable {

  @Autowired
  private PsnCacheService psnCacheService;

  @Override
  public void prepare() throws Exception {
    // TODO Auto-generated method stub
  }

  @Action("/psnweb/session/ajaxrefresh")
  public String refreshSession() {
    String sessionId = Struts2Utils.getRequest().getSession().getId();
    Object uDetails = psnCacheService.get(SecurityConstants.USER_DETAILS_INFO_CACHE, sessionId);
    String resultJson = "{\"result\":\"success\"}";
    if (uDetails != null) {
      psnCacheService.remove(SecurityConstants.USER_DETAILS_INFO_CACHE, sessionId);
      psnCacheService.put(SecurityConstants.USER_DETAILS_INFO_CACHE, SecurityConstants.USER_DETAILS_CACHE_TIME,
          sessionId, (Serializable) uDetails);
    } else {
      resultJson = "{\"result\":\"cacheNull\"}";
    }
    Struts2Utils.renderJson(resultJson, "encoding:UTF-8");
    return null;
  }

}
