package com.smate.center.oauth.action.register;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Actions;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Preparable;
import com.smate.center.oauth.cache.OauthCacheService;
import com.smate.center.oauth.model.profile.PersonRegisterForm;
import com.smate.center.oauth.service.psnregister.PersonRegisterService;
import com.smate.center.oauth.utils.EditValidateUtils;
import com.smate.core.base.utils.mobile.SmateMobileUtils;
import com.smate.core.base.utils.number.NumberUtils;
import com.smate.core.base.utils.security.Des3Utils;
import com.smate.core.base.utils.string.StringUtils;
import com.smate.core.base.utils.struts2.Struts2Utils;
import com.smate.core.base.utils.wechat.MessageUtil;

;

/**
 * 人员注册Action
 * 
 * @author Administrator
 *
 */
@Results({@Result(name = "save_error", location = "/WEB-INF/jsp/mobile/MobileRegister.jsp"),
    @Result(name = "register", location = "/WEB-INF/jsp/mobile/MobileRegister.jsp")})
public class RegisterPersonAction extends ActionSupport implements ModelDriven<PersonRegisterForm>, Preparable {

  protected static final long serialVersionUID = -734066021998629664L;
  private final Logger logger = LoggerFactory.getLogger(getClass());

  private PersonRegisterForm form;
  // 是否自动登录
  private boolean autoLogin;

  @Autowired
  private PersonRegisterService personRegisterService;
  @Value("${domainscm}")
  private String domainscm;
  @Value("${domainoauth}")
  private String domainoauth;
  @Value("${domainMobile}")
  private String domainMobile;
  @Value("${wechat.appid}")
  private String appId;
  // 从微信端跳转过来后的微信openId
  private String wxOpenId;
  // 从微信端跳转过来后的微信unionId
  private String des3UnionId;
  // 微信端点击的URL，绑定后要跳转回去的地方
  private String wxUrl;
  private String service;
  @Autowired
  private OauthCacheService oauthCacheService;

  @Override
  public void prepare() throws Exception {

    if (form == null) {
      form = new PersonRegisterForm();
    }

  }

  @Override
  public PersonRegisterForm getModel() {

    return form;
  }

  @Actions(@Action("/oauth/mobile/register"))
  public String wechatRegister() throws Exception {

    return "register";
  }

  /**
   * ajax邮件验证.
   * 
   * @return String
   */
  @Action("/oauth/register/ajaxCheckedUserName")
  public String ajaxCheckedUserName() {
    try {
      String email = Struts2Utils.getParameter("email");
      if (StringUtils.isNotBlank(email)) {
        email = email.trim();
      }
      if (emailIdIsNull(email)) {
        Struts2Utils.renderText("true");
      } else {
        Struts2Utils.renderText("false");
      }
    } catch (Exception e) {
      logger.error("ajax验证邮件时获取邮件出错", e);
      Struts2Utils.renderText("false");
    }
    return null;
  }

  /**
   * 新用户注册
   * 
   * @return
   */
  @Action("/oauth/register/save")
  public String registerSave() {
    try {
      logger.info("wxOpenId={}, wxUrl={}", Des3Utils.decodeFromDes3(wxOpenId), Des3Utils.decodeFromDes3(wxUrl));
      form.setMobileReg("1");// 设置手机注册
      dealregisterName(form);// 处理姓名信息
      if (!validateMsg()) {
        form = this.assemPersonInfo();// 封装注册人员的信息.
        Long psnOpenId = personRegisterService.registerPerson(form);// 注册保存人员信息.
        if (NumberUtils.isNotNullOrZero(psnOpenId)) {// 注册成功.
          String AID = personRegisterService.getAutoLoginAID(psnOpenId, "WCR");// 获取AID
          logger.info("获取到的AID={}", AID);
          String targetUrl = buildRedirectUrl(AID, new StringBuffer());
          Struts2Utils.getResponse().sendRedirect(targetUrl);
          return null;
        } else {
          addActionMessage("邮箱注册失败");
        }
      }
      Struts2Utils.getRequest().setAttribute("person", form);
    } catch (Exception e) {
      logger.error("邮箱注册出错", e);
    }
    return "save_error";
  }



  private String buildRedirectUrl(String AID, StringBuffer targetUrl) throws UnsupportedEncodingException {
    if (StringUtils.isNotBlank(AID)) {// 正常获取到AID后
      if (form.getAutoLogin()) {// 需要自动登录，自动跳转人员信息完善页面
        targetUrl.append(domainMobile + "/psnweb/mobile/improveInfo?sysType=" + form.getSysType());
        if (StringUtils.isNotBlank(form.getService())) {
          service = StringUtils.defaultIfBlank(Des3Utils.decodeFromDes3(form.getService()), form.getService());
          String reqUrl = service.replaceAll(".*scholarmate.com(.*)", "$1");
          targetUrl.append("&reqUrl=" + URLEncoder.encode(reqUrl, "utf-8"));
        }
      }
      if (SmateMobileUtils.isWeChatBrowser(Struts2Utils.getRequest())) {// 微信端构建链接获取微信code进行自动绑定微信和注册的账号
        String url = targetUrl.toString().indexOf("?") > -1 ? targetUrl.append("&AID=" + AID).toString()
            : targetUrl.append("?AID=" + AID).toString();
        return MessageUtil.getOAuth2Url(appId,
            URLEncoder.encode(
                domainMobile + "/oauth/wx/bind?bindType=0&AID=" + AID + "&forwardUrl=" + Des3Utils.encodeToDes3(url),
                "utf-8"),
            null, null);
      }
      targetUrl.append("&AID=" + AID);
    } else {// 没有正常获取AID就跳转登录页面
      targetUrl.append("/oauth/mobile/index&wxUrl=" + wxUrl);
    }
    return targetUrl.toString();
  }



  /**
   * 校验注册填写的信息
   * 
   * @return
   */
  public boolean validateMsg() {
    boolean flag = false;

    // 验证邮箱不为空
    String msg = getText("注册信息验证");
    if (EditValidateUtils.hasParam(form.getEmail(), 50, EditValidateUtils.MAIL_COAD)) {
      addActionMessage("邮箱" + msg);
      flag = true;
    }
    // 验证邮箱未被注册过
    try {
      boolean emailNotUsed = personRegisterService.findIsEmail(form.getEmail());
      if (!emailNotUsed) {
        addActionMessage("邮箱已被注册");
        flag = true;
      }
    } catch (Exception e) {
      logger.error("校验EMAIL错误", e);
      addActionMessage("邮箱已被注册");
      flag = true;
    }
    // 验证名字不为空
    if (EditValidateUtils.hasParam(form.getName(), 61, null)) {
      addActionMessage("姓名" + msg);
      flag = true;
    }

    // 验证密码不为空
    if (EditValidateUtils.hasParam(form.getNewpassword(), 40, null)) {
      addActionMessage("密码" + msg);
      flag = true;
    }

    return flag;
  }

  /**
   * 邮件验证.
   * 
   * @param email
   * @return Long
   */
  public boolean emailIdIsNull(String email) {
    try {
      return personRegisterService.findIsEmail(email);
    } catch (Exception e) {
      logger.error("验证邮件时获取邮件出错", e);
      return false;
    }
  }

  /**
   * 封装注册人员的信息.
   * 
   * @return
   */
  private PersonRegisterForm assemPersonInfo() {
    HttpServletRequest request = Struts2Utils.getRequest();

    String degree = request.getParameter("degree");
    if (StringUtils.isNotBlank(degree)) {
      String[] d = degree.split(",");
      form.setDegree(d[0]);
      form.setDegreeName(d[1]);
    }
    form.setIsRegisterR(true);
    // 对密码进行MD5加密，后面调用center-open的人员注册服务传参需要
    form.setNewpassword(DigestUtils.md5Hex(form.getNewpassword()));

    form.setEmail(form.getEmail().toLowerCase());
    form.setEmailLanguageVersion(this.getLocale().toString());// 设置个人邮件接收的语言版本，默认为当前系统的语言版本
    return form;
  }

  /**
   * 姓名分开填写 需在在这里拼接处理
   * 
   * @param form
   * @return
   */
  private PersonRegisterForm dealregisterName(PersonRegisterForm form) {
    rebuildName(form);
    if (StringUtils.isNotEmpty(form.getZhlastName()) || StringUtils.isNotEmpty(form.getZhfirstName())) {
      form.setName(form.getZhlastName() + form.getZhfirstName());
      if (StringUtils.isNotEmpty(form.getLastName()) || StringUtils.isNotEmpty(form.getFirstName())) {
        form.setEname(form.getFirstName() + " " + form.getLastName());
      } else {
        form.setFirstName(form.getZhfirstName());
        form.setLastName(form.getZhlastName());
        form.setEname(form.getZhlastName() + form.getZhfirstName());
      }
    } else {
      if (StringUtils.isNotEmpty(form.getLastName()) || StringUtils.isNotEmpty(form.getFirstName())) {
        form.setZhfirstName(form.getFirstName());
        form.setZhlastName(form.getLastName());
        form.setName(form.getFirstName() + " " + form.getLastName());
        form.setEname(form.getFirstName() + " " + form.getLastName());
      }
    }
    return form;
  }

  /**
   * 转换名称中出现的允许使用的特殊字符,避免长度校验出错
   * 
   * @param form
   */
  public void rebuildName(PersonRegisterForm form) {
    form.setFirstName(form.getFirstName() == null ? "" : form.getFirstName().replaceAll("&middot;", "·"));
    form.setLastName(form.getLastName() == null ? "" : form.getLastName().replaceAll("&middot;", "·"));
    form.setZhfirstName(form.getZhfirstName() == null ? "" : form.getZhfirstName().replaceAll("&middot;", "·"));
    form.setZhlastName(form.getZhlastName() == null ? "" : form.getZhlastName().replaceAll("&middot;", "·"));
  }

  public boolean getAutoLogin() {
    return autoLogin;
  }

  public void setAutoLogin(boolean autoLogin) {
    this.autoLogin = autoLogin;
  }

  public String getWxOpenId() {
    return wxOpenId;
  }

  public void setWxOpenId(String wxOpenId) {
    this.wxOpenId = wxOpenId;
  }

  public String getWxUrl() {
    return wxUrl;
  }

  public void setWxUrl(String wxUrl) {
    this.wxUrl = wxUrl;
  }

  public String getDes3UnionId() {
    return des3UnionId;
  }

  public void setDes3UnionId(String des3UnionId) {
    this.des3UnionId = des3UnionId;
  }

  public String getService() {
    return service;
  }

  public void setService(String service) {
    this.service = service;
  }

}
