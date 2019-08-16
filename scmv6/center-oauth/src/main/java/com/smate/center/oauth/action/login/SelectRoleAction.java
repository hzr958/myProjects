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
 * 帐号密码验证 权限
 * 
 * @author tsz
 *
 */

@Results({@Result(name = "reload", location = "index", type = "redirect"),
    @Result(name = "error", location = "/index.jsp"),
    @Result(name = "redirectUrl", location = "${forwardUrl}", type = "redirect")})
public class SelectRoleAction extends ActionSupport implements ModelDriven<OauthLoginForm>, Preparable {

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
   * 角色 选择
   * 
   * @return
   */
  @Actions({@Action("/oauth/selectRole")})
  public String selectRole() {
    // 选择角色 后 判断 该用户是否真的是这个角色，并加载改用户的权限
    try {

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
