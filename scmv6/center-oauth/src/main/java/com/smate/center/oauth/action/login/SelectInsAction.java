package com.smate.center.oauth.action.login;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Actions;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Preparable;
import com.smate.center.oauth.cache.OauthCacheService;
import com.smate.center.oauth.service.login.OauthLoginService;
import com.smate.core.base.oauth.model.OauthLoginForm;

/**
 * 选择单位
 * 
 * @author tsz
 *
 */

@Results({@Result(name = "reload", location = "index", type = "redirect"),
    @Result(name = "error", location = "/index.jsp"),
    @Result(name = "redirectUrl", location = "${forwardUrl}", type = "redirect")})
public class SelectInsAction extends ActionSupport implements ModelDriven<OauthLoginForm>, Preparable {

  /**
   * 
   */
  private static final long serialVersionUID = -4233512169345586923L;

  @Autowired
  private OauthLoginService oauthLoginService;

  @Autowired
  private OauthCacheService oauthCacheService;

  @Autowired
  private UserDetailsService userDetailsService;

  private OauthLoginForm form;

  /**
   * 单位 选择
   * 
   * @return
   */
  @Actions({@Action("/oauth/selectIns")})
  public String selectIns() {
    // 选择角色 后 判断 该用户是否真的是这个角色，并加载改用户的权限
    try {
      // 判断 信息是否

    } catch (Exception e) {
      // TODO 返回异常信息
    }
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

}
