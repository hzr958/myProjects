package com.smate.center.oauth.action.login;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Actions;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Preparable;
import com.smate.core.base.oauth.model.OauthLoginForm;
import com.smate.core.base.utils.security.Des3Utils;

/**
 * 帐号密码验证 权限
 * 
 * @author zx
 *
 */

@Results({@Result(name = "errorLogin", location = "/WEB-INF/jsp/sns/oauthLoginView.jsp"),
    @Result(name = "account_exception", location = "/WEB-INF/jsp/error/account_exception.jsp")})

public class LoginErrorAction extends ActionSupport implements ModelDriven<OauthLoginForm>, Preparable {

  private static final long serialVersionUID = -2650130577977524139L;

  /**
   * 
   */

  private final Logger logger = LoggerFactory.getLogger(getClass());

  private OauthLoginForm form;

  /**
   * 帐号密码错误
   * 
   * @return
   * @throws Exception
   */
  @Actions({@Action("/oauth/loginerror")})
  public String loginerror() {
    form.setMsg(Des3Utils.decodeFromDes3(form.getMsg()));
    return "errorLogin";
  }


  /**
   * 帐号异常.
   * 
   * @return
   */
  @Actions({@Action("/oauth/accountexception")})
  public String error() {

    return "account_exception";
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
