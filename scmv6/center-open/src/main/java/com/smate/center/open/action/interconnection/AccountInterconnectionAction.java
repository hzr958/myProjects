package com.smate.center.open.action.interconnection;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Preparable;
import com.smate.center.open.model.interconnection.AccountInterconnectionForm;
import com.smate.center.open.service.interconnection.AccountInterconnectionService;
import com.smate.core.base.utils.security.Des3Utils;
import com.smate.core.base.utils.struts2.Struts2Utils;
import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Actions;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.util.HashMap;
import java.util.Map;

/**
 * 互联互通Action
 * 
 * @author zll
 */
@Results({@Result(name = "connection", location = "/WEB-INF/jsp/interconnection/interconnection_login.jsp"),
    @Result(name = "simple_connection", location = "/WEB-INF/jsp/interconnection/interconnection_login_simple.jsp")})
public class AccountInterconnectionAction extends ActionSupport
    implements ModelDriven<AccountInterconnectionForm>, Preparable {

  private static final long serialVersionUID = 1L;
  protected Logger logger = LoggerFactory.getLogger(getClass());
  private AccountInterconnectionForm form;
  @Autowired
  private AccountInterconnectionService accountInterconnectionService;
  @Value("${domainoauth}")
  private String domainOauth;
  @Value("${http.domainoauth}")
  private String httpDomainOauth;
  @Value("${domain.open.https}")
  private String domainScm;
  @Value("${domain.open}")
  private String httpDomain;

  /**
   * 进入帐号关联页面
   * 
   * @return
   */
  @Action("/open/interconnection/show")
  public String showInterConnection() {
    try {
      if (accountInterconnectionService.checkUrl(form)) {
        form.setEffectiveUrl(true);
        if (accountInterconnectionService.checkSysToken(form.getFromSys())) {
          form.setEffectiveToken(true);
          if (StringUtils.isNotBlank(form.getEmail())) {
            form.setEmailIsExist(accountInterconnectionService.findEmailIsExist(form.getEmail()));
          }
          accountInterconnectionService.dofindMatchUser(form);
        }
      }
      if ("simple".equals(form.getType())) {
        return "simple_connection";
      } else {
        return "connection";
      }
    } catch (Exception e) {
      logger.error("进入互联互通帐号关联页面出错", e);
    }
    return null;
  }

  @Action("/open/connection/validateemail")
  public String ajaxValidateEmail() {
    try {
      Map<String, String> map = new HashMap<String, String>(2);
      if (StringUtils.isNotBlank(form.getEmail())) {
        if (accountInterconnectionService.findEmailIsExist(form.getEmail())) {
          map.put("result", "exist");
          Struts2Utils.renderJson(map, "encoding:UTF-8");
        } else {
          map.put("result", "notexist");
          Struts2Utils.renderJson(map, "encoding:UTF-8");
        }
      }
    } catch (Exception e) {
      logger.error("互联互通----验证邮箱是否已存在出错，email=" + form.getEmail(), e);
    }
    return null;
  }

  @Override
  public void prepare() throws Exception {
    if (form == null) {
      form = new AccountInterconnectionForm();
    }
  }

  @Override
  public AccountInterconnectionForm getModel() {
    return form;
  }

  /**
   * @Title: fastRelateAccount
   * @Description: 快速关联登录
   * @param @return 入参
   * @return String 返回类型
   * @author ltl
   * @throws @date 2016年8月25日 上午11:36:47
   * @version V1.0
   */
  @Action("/open/interconnection/ajaxfastrelate")
  public String fastRelateAccount() {
    Map<String, String> map = new HashMap<String, String>(2);
    try {
      Long openId = null;
      if (Des3Utils.decodeFromDes3(form.getDes3PsnId()) != null
          && accountInterconnectionService.checkSysToken(form.getToken())) {
        openId = accountInterconnectionService.doFastRelateAccount(form);
        form.setOpenId(openId);
        String signature = accountInterconnectionService.generateSignature(form);
        if (openId != null && !openId.equals(0L)) {
          map.put("result", "success");
          map.put("openId", openId.toString());
          map.put("signature", signature);
          Struts2Utils.renderJson(map, "encoding:UTF-8");
        } else {
          map.put("result", "快速登录失败");
          Struts2Utils.renderJson(map, "encoding:UTF-8");
        }
      } else {
        map.put("result", "error");
        map.put("errorMsg", "wrongToken");
        Struts2Utils.renderJson(map, "encoding:UTF-8");
      }
    } catch (Exception e) {
      logger.error("快速登录失败", e);
    }
    return null;
  }

  /**
   * @Title: relateExistAccount
   * @Description: 已有账号登录
   * @param @return 入参
   * @return String 返回类型
   * @author ltl
   * @throws @date 2016年8月25日 上午11:37:41
   * @version V1.0
   */
  @Action("/open/interconnection/ajaxrelateexist")
  public String relateExistAccount() {
    Map<String, String> map = new HashMap<String, String>(3);
    try {
      Long openId = accountInterconnectionService.doRelateExistAccount(form);
      if (openId != null && !openId.equals(0L)) {
        form.setOpenId(openId);
        String signature = accountInterconnectionService.generateSignature(form);
        if (!form.getRelationExist()) {
          map.put("result", "success");
          map.put("openId", openId.toString());
          map.put("signature", signature);
          Struts2Utils.renderJson(map, "encoding:UTF-8");
        } else {
          map.put("result", "exist");
          map.put("openId", openId.toString());
          map.put("signature", signature);
          Struts2Utils.renderJson(map, "encoding:UTF-8");
        }
      } else {
        map.put("result", form.getMsg());
        Struts2Utils.renderJson(map, "encoding:UTF-8");
      }
    } catch (Exception e) {
      logger.error("登录关联失败", e);
    }
    return null;
  }

  /**
   * 效验关联
   * 
   * @return
   */
  @Actions({@Action("/open/interconnection/ajaxcheckrelate"), @Action("/open/interconnection/checkrelate")})
  public String ajaxCheckRelate() {
    Map<String, String> map = new HashMap<String, String>(2);
    try {
      if (accountInterconnectionService.CheckRelate(form)) {
        map.put("result", "success");
      } else {
        map.put("result", "error");
      }
      Struts2Utils.renderJson(map, "encoding:UTF-8");
    } catch (Exception e) {
      logger.error("效验关联出错", e);
    }
    return null;
  }

  /**
   * 同步人员信息
   * 
   * @return
   */
  @Action("/open/interconnection/ajaxsyncpersoninfo")
  public String syncPersonInfo() {
    try {
      // 解决jsonp跨域请求后禁止访问的问题
      if (form.getIsHttps()) {
        Struts2Utils.getResponse().addHeader("Access-Control-Allow-Origin", domainScm);
      } else {
        Struts2Utils.getResponse().addHeader("Access-Control-Allow-Origin", httpDomain);
      }
      Map<String, String> map = new HashMap<String, String>(3);
      if (StringUtils.isNotBlank(form.getFromSys()) && accountInterconnectionService.checkSysToken(form.getFromSys())) {
        boolean flag = accountInterconnectionService.syncPersonInfo(form);
        if (flag) {
          map.put("result", "success");
          Struts2Utils.renderJson(map, "encoding:UTF-8");
        } else {
          map.put("result", "error");
          Struts2Utils.renderJson(map, "encoding:UTF-8");
        }
      } else {
        map.put("result", "error");
        map.put("errorMsg", "wrongToken");
        Struts2Utils.renderJson(map, "encoding:UTF-8");
      }
    } catch (Exception e) {
      logger.error("同步人员信息出错", e);
    }
    return null;
  }

  @Action("/open/interconnection/login")
  public String log() {
    return "connection";
  }



  public String getDomainScm() {
    return domainScm;
  }

  public void setDomainScm(String domainScm) {
    this.domainScm = domainScm;
  }

  public String getHttpDomain() {
    return httpDomain;
  }

  public void setHttpDomain(String httpsDomain) {
    this.httpDomain = httpsDomain;
  }

  public String getDomainOauth() {
    return domainOauth;
  }

  public void setDomainOauth(String domainOauth) {
    this.domainOauth = domainOauth;
  }

  public String getHttpDomainOauth() {
    return httpDomainOauth;
  }

  public void setHttpDomainOauth(String httpDomainOauth) {
    this.httpDomainOauth = httpDomainOauth;
  }
}
