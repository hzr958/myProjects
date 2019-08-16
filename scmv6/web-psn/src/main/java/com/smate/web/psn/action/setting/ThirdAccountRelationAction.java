package com.smate.web.psn.action.setting;

import java.util.HashMap;
import java.util.Map;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Preparable;
import com.qq.connect.oauth.Oauth;
import com.smate.core.base.utils.security.SecurityUtils;
import com.smate.core.base.utils.struts2.Struts2Utils;
import com.smate.web.psn.cache.PsnCacheService;
import com.smate.web.psn.form.PsnSettingForm;
import com.smate.web.psn.service.thrid.account.ThirdAccountRelationService;


/**
 * 第三方帐号关联绑定action 例如：QQ/微信
 * 
 * @author aijiangbin
 *
 */
@Results({@Result(name = "change_password", location = "/WEB-INF/jsp/psnsetting/password/change_password.jsp"),})
public class ThirdAccountRelationAction extends ActionSupport implements ModelDriven<PsnSettingForm>, Preparable {

  /**
   * 
   */
  private static final long serialVersionUID = -7496914505522794654L;

  private static final String QQ_LOGIN_CACHE = "qq_login_cache";
  private static final String QQ_LOGIN_PARAMS = "qqLoginParams";
  protected final Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private PsnCacheService psnCacheService;
  @Autowired
  private ThirdAccountRelationService thirdAccountRelationService;

  private PsnSettingForm form;

  @Value("${domainscm}")
  private String domainscm;



  /**
   * QQ 登录关联
   * 
   * @return
   * @throws Exception
   */
  @Action("/psnweb/psnsetting/qqloginrelation")
  public String qqLoginRelation() throws Exception {
    removeQQLoginParamsCache();
    Struts2Utils.getRequest().getSession().setAttribute("currentUserId", SecurityUtils.getCurrentUserId());
    Struts2Utils.getResponse().sendRedirect(new Oauth().getAuthorizeURL(Struts2Utils.getRequest()));
    return null;
  }

  /**
   * 取消qq绑定
   * 
   * @return
   * @throws Exception
   */
  @Action("/psnweb/psnsetting/ajaxcancelqqbind")
  public String cancelQQBind() {
    Map map = new HashMap();
    try {
      thirdAccountRelationService.cancelQQBind(form);
      map.put("result", "success");
    } catch (Exception e) {
      logger.error("取消qq绑定异常，psnId=" + form.getPsnId(), e);
      map.put("result", "fail");
    } ;
    Struts2Utils.renderJson(map, "encoding:UTF-8");
    return null;
  }

  /**
   * 取消微信绑定
   * 
   * @return
   */
  @Action("/psnweb/psnsetting/ajaxcancelwxbind")
  public String cancelWXBind() {
    Map map = new HashMap();
    try {
      thirdAccountRelationService.cancelWCBind(form);
      map.put("result", "success");
    } catch (Exception e) {
      logger.error("取消qq绑定异常，psnId=" + form.getPsnId(), e);
      map.put("result", "fail");
    } ;
    Struts2Utils.renderJson(map, "encoding:UTF-8");
    return null;
  }



  private void removeQQLoginParamsCache() {
    psnCacheService.remove(QQ_LOGIN_CACHE, QQ_LOGIN_PARAMS + Struts2Utils.getSession().getId());
  }

  @Override
  public void prepare() throws Exception {
    if (form == null) {
      form = new PsnSettingForm();
    }

  }

  @Override
  public PsnSettingForm getModel() {
    return form;
  }

}
