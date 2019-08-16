package com.smate.center.oauth.action.wechat;

import java.util.HashMap;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Preparable;
import com.smate.center.oauth.model.bind.MidBindForm;
import com.smate.center.oauth.model.consts.MidBindConsts;
import com.smate.center.oauth.service.bind.MidBindService;
import com.smate.core.base.utils.struts2.Struts2Utils;

/**
 * 移动端绑定控制器
 * 
 * @author zzx
 *
 */
@Results({@Result(name = "privacy_terms", location = "/WEB-INF/jsp/terms/privacy_terms.jsp"),
    @Result(name = "service_terms", location = "/WEB-INF/jsp/terms/service_terms.jsp")})
public class MidBindAction extends ActionSupport implements ModelDriven<MidBindForm>, Preparable {

  private static final long serialVersionUID = 1L;
  private final Logger logger = LoggerFactory.getLogger(getClass());
  private MidBindForm form;
  @Autowired
  private MidBindService midBindService;

  /**
   * 获取服务条款静态页面
   */
  @Action("/oauth/api/showtermspage")
  public String showtermspage() {
    if ("privacy".equals(form.getTermspage())) {
      return "privacy_terms";
    }
    if ("service".equals(form.getTermspage())) {
      return "service_terms";
    }
    return null;
  }

  /**
   * QQ解除绑定接口
   */
  @Action("/oauth/api/unbindqq")
  public void unbindQQ() {
    form.setResultMap(new HashMap<String, String>());
    form.getResultMap().put(MidBindConsts.STATUS, MidBindConsts.SUCCESS);
    try {
      midBindService.unbindQQ(form);
    } catch (Exception e) {
      form.getResultMap().put(MidBindConsts.STATUS, MidBindConsts.ERROR);
      form.getResultMap().put(MidBindConsts.MSG, "QQ检查绑定接口出错");
      logger.error("QQ解除绑定接口服务出错", e);
    }
    Struts2Utils.renderJson(form.getResult(), "encoding:UTF-8");
  }

  /**
   * 微信解除绑定接口
   */
  @Action("/oauth/api/unbindwc")
  public void unbindWC() {
    form.setResultMap(new HashMap<String, String>());
    form.getResultMap().put(MidBindConsts.STATUS, MidBindConsts.SUCCESS);
    try {
      midBindService.unbindWC(form);
    } catch (Exception e) {
      form.getResultMap().put(MidBindConsts.STATUS, MidBindConsts.ERROR);
      form.getResultMap().put(MidBindConsts.MSG, "微信解除绑定接口出错");
      logger.error("微信解除绑定接口服务出错", e);
    }
    Struts2Utils.renderJson(form.getResult(), "encoding:UTF-8");
  }

  /**
   * QQ检查绑定接口
   */
  @Action("/oauth/api/checkmidbingqq")
  public void checkMidBingQQ() {
    form.setResultMap(new HashMap<String, String>());
    form.getResultMap().put(MidBindConsts.STATUS, MidBindConsts.SUCCESS);
    try {
      midBindService.checkMidBingQQ(form);
    } catch (Exception e) {
      form.getResultMap().put(MidBindConsts.STATUS, MidBindConsts.ERROR);
      form.getResultMap().put(MidBindConsts.MSG, "QQ检查绑定接口出错");
      logger.error("QQ检查绑定接口服务出错", e);
    }
    Struts2Utils.renderJson(form.getResult(), "encoding:UTF-8");
  }

  /**
   * QQ绑定登录接口
   */
  @Action("/oauth/api/qqlogin")
  public void qqLogin() {
    form.setResultMap(new HashMap<String, String>());
    form.getResultMap().put(MidBindConsts.STATUS, MidBindConsts.SUCCESS);
    try {
      midBindService.qqLogin(form);
    } catch (Exception e) {
      form.getResultMap().put(MidBindConsts.STATUS, MidBindConsts.ERROR);
      form.getResultMap().put(MidBindConsts.MSG, "QQ绑定登录接口出错");
      logger.error("QQ绑定登录接口服务出错", e);
    }
    Struts2Utils.renderJson(form.getResult(), "encoding:UTF-8");
  }

  /**
   * QQ绑定注册接口
   */
  @Action("/oauth/api/qqregistered")
  public void qqRegistered() {
    form.setResultMap(new HashMap<String, String>());
    form.getResultMap().put(MidBindConsts.STATUS, MidBindConsts.SUCCESS);
    try {
      midBindService.qqRegistered(form);
    } catch (Exception e) {
      form.getResultMap().put(MidBindConsts.STATUS, MidBindConsts.ERROR);
      form.getResultMap().put(MidBindConsts.MSG, "QQ绑定注册接口出错");
      logger.error("QQ绑定注册接口服务出错", e);
    }
    Struts2Utils.renderJson(form.getResult(), "encoding:UTF-8");
  }

  /**
   * 微信检查绑定接口
   */
  @Action("/oauth/api/checkmidbingwc")
  public void checkMidBingWC() {
    form.setResultMap(new HashMap<String, String>());
    form.getResultMap().put(MidBindConsts.STATUS, MidBindConsts.SUCCESS);
    try {
      midBindService.checkIosBingWC(form);
    } catch (Exception e) {
      form.getResultMap().put(MidBindConsts.STATUS, MidBindConsts.ERROR);
      form.getResultMap().put(MidBindConsts.MSG, "微信绑定接口服务出错");
      logger.error("微信绑定接口服务出错", e);
    }
    Struts2Utils.renderJson(form.getResult(), "encoding:UTF-8");
  }

  /**
   * 微信绑定登录
   */
  @Action("/oauth/api/wclogin")
  public void midLogin() {
    form.setResultMap(new HashMap<String, String>());
    form.getResultMap().put(MidBindConsts.STATUS, MidBindConsts.SUCCESS);
    try {
      midBindService.wcLogin(form);
    } catch (Exception e) {
      form.getResultMap().put(MidBindConsts.STATUS, MidBindConsts.ERROR);
      form.getResultMap().put(MidBindConsts.MSG, "微信绑定登录出错");
      logger.error("微信绑定登录出错", e);
    }
    Struts2Utils.renderJson(form.getResult(), "encoding:UTF-8");
  }

  /**
   * 微信绑定注册
   */
  @Action("/oauth/api/wcregistered")
  public void midRegistered() {
    form.setResultMap(new HashMap<String, String>());
    form.getResultMap().put(MidBindConsts.STATUS, MidBindConsts.SUCCESS);
    try {
      midBindService.wcRegistered(form);
    } catch (Exception e) {
      form.getResultMap().put(MidBindConsts.STATUS, MidBindConsts.ERROR);
      form.getResultMap().put(MidBindConsts.MSG, "微信绑定注册出错");
      logger.error("微信绑定注册出错", e);
    }
    Struts2Utils.renderJson(form.getResult(), "encoding:UTF-8");
  }

  @Override
  public void prepare() throws Exception {
    if (form == null) {
      form = new MidBindForm();
    }
  }

  @Override
  public MidBindForm getModel() {
    return form;
  }

}
