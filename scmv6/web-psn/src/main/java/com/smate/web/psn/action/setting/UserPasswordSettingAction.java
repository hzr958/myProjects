package com.smate.web.psn.action.setting;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.util.WebUtils;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Preparable;
import com.smate.core.base.utils.common.SystemBaseContants;
import com.smate.core.base.utils.model.cas.security.User;
import com.smate.core.base.utils.security.SecurityUtils;
import com.smate.core.base.utils.service.msg.MobileMessageForm;
import com.smate.core.base.utils.service.msg.MobileMessageWwxyService;
import com.smate.core.base.utils.service.msg.MobileMessageWwxyServiceImpl;
import com.smate.core.base.utils.struts2.Struts2Utils;
import com.smate.web.psn.cache.PsnCacheService;
import com.smate.web.psn.form.PersonEmailInfo;
import com.smate.web.psn.form.PsnSettingForm;
import com.smate.web.psn.model.psninfo.PersonEmailRegister;
import com.smate.web.psn.service.email.AccountEmailCheckLogService;
import com.smate.web.psn.service.profile.PersonEmailManager;
import com.smate.web.psn.service.profile.PersonalManager;
import com.smate.web.psn.service.setting.ChangePasswordService;
import com.smate.web.psn.service.setting.RememberPwdContants;
import com.smate.web.psn.service.thrid.account.ThirdAccountRelationService;
import com.smate.web.psn.service.user.UserService;
import com.smate.web.psn.utils.EditValidateUtils;

/**
 * 账号密码修改
 * 
 * @author tsz
 */

@Results({@Result(name = "change_password", location = "/WEB-INF/jsp/psnsetting/password/change_password.jsp"),
    @Result(name = "psn_email_list", location = "/WEB-INF/jsp/psnsetting/password/psn_email_list.jsp"),
    @Result(name = "account_email_list", location = "/WEB-INF/jsp/psnsetting/password/account_email_list.jsp")})
public class UserPasswordSettingAction extends ActionSupport implements ModelDriven<PsnSettingForm>, Preparable {

  /**
   * 
   */
  private static final long serialVersionUID = -7496914505522794654L;

  protected final Logger logger = LoggerFactory.getLogger(getClass());

  private PsnSettingForm form;
  @Autowired
  private ChangePasswordService changePasswordService;
  @Autowired
  private UserService userService;
  @Autowired
  private PersonEmailManager personEmailManager;
  @Autowired
  private PersonalManager personalManager;
  @Autowired
  private ThirdAccountRelationService thirdAccountRelationService;

  @Value("${domainscm}")
  private String domainscm;

  @Resource
  private AccountEmailCheckLogService accountEmailCheckLogService;
  @Resource
  private MobileMessageWwxyService mobileMessageWwxyService;
  @Resource
  private PsnCacheService cacheService;
  @Autowired
  private MobileMessageWwxyService messageWwxyService;

  @Action("/psnweb/psnsetting/ajaxchangepassword")
  public String changepassword() throws Exception {

    logger.debug("进入密码修改页面");
    // 获取登录帐号
    User user = changePasswordService.get(SecurityUtils.getCurrentUserId());
    form.setUsername(user.getLoginName());
    thirdAccountRelationService.getThirdAccountBindInfo(form);
    form.setIpCheck(messageWwxyService.ipCheck());
    return "change_password";
  }

  /**
   * 进入修改帐号密码.
   * 
   * @return
   * @throws Exception
   */
  @Action("/psnweb/psnsetting/ajaxsavepwd")
  public String ajaxSavePwd() throws Exception {
    Long psnId = SecurityUtils.getCurrentUserId();
    User user = changePasswordService.get(psnId);
    form.setUsername(user.getLoginName());

    String validateSaveData = validateSavePwd();
    if (validateSaveData != null) {
      Struts2Utils.renderJson("{result:'error',msg:'" + validateSaveData + "'}", "encoding:UTF-8");
      return null;
    }
    Integer res = userService.changePassword(form.getOldpassword(), this.form.getNewpassword(), psnId);
    if (res == 1) {
      Map<String, String> json = new HashMap<String, String>(1);
      json.put("result", "success");
      json.put("msg", getText("changepassowd.msg.success"));
      Struts2Utils.renderJson(json, "encoding:UTF-8");
    } else {
      Map<String, String> json = new HashMap<String, String>(2);
      json.put("result", "error");
      json.put("msg", getText("changepassowd.msg.errorOldPassword"));
      Struts2Utils.renderJson(json, "encoding:UTF-8");
    }
    return null;
  }

  /**
   * 校验.
   * 
   * @return
   * @throws Exception
   */
  public String validateSavePwd() throws Exception {

    StringBuilder sb = new StringBuilder();

    if (EditValidateUtils.hasParam(form.getOldpassword(), 40, null)) {
      sb.append(getText("changepassowd.msg.errorOldPassword")).append("，");
    }
    // 修改新密码的最大长度控制为40个字符(原为16个字符)，与数据库及页面限制保持一致_MaoJianGuo_2012-11-09_SCM-1221
    if (EditValidateUtils.hasParam(form.getNewpassword(), 40, null)) {
      sb.append(getText("changepassowd.msg.errorNewPassword")).append("，");
    } else if (!form.getNewpassword().equals(form.getRenewpassword())) {
      sb.append(getText("changepassowd.msg.errorConfirmPassword")).append("，");
    }

    if (sb.length() > 0) {
      sb.deleteCharAt(sb.length() - 1);
      sb.append("。");
      return sb.toString();
    }
    return null;
  }

  /**
   * 添加邮件地址.
   * 
   * @return
   * @throws Exception
   */
  @SuppressWarnings({"rawtypes", "unchecked"})
  @Action("/psnweb/psnsetting/ajaxaddemail")
  public String ajaxAdd() throws Exception {
    String email = form.getEmail();
    if (email == null || !email.matches(EditValidateUtils.MAIL_COAD)) {
      Struts2Utils.renderJson("{result:'error',code:'email_error'}", "encoding:UTF-8");
      return null;
    }

    Map jsonRes = new HashMap();
    Long result = personEmailManager.addEmail(email, SecurityUtils.getCurrentUserId(), 0L, 0L);
    if (result == -1) {
      jsonRes.put("result", "error");
      jsonRes.put("code", "email_exit");
      Struts2Utils.renderJson(jsonRes, "encoding:UTF-8");
      return null;
    } else if (result == -2) {
      jsonRes.put("result", "error");
      jsonRes.put("code", "email_use_other");
      Struts2Utils.renderJson(jsonRes, "encoding:UTF-8");
      return null;
    }

    jsonRes.put("result", "success");
    jsonRes.put("code", "result");
    Struts2Utils.renderJson(jsonRes, "encoding:UTF-8");

    return null;
  }

  /**
   * 删除邮件地址.
   * 
   * @return
   * @throws Exception
   */
  @SuppressWarnings({"rawtypes", "unchecked"})
  @Action("/psnweb/psnsetting/ajaxdeleteemail")
  public String ajaxDelete() throws Exception {

    Long mailId = form.getMailId();
    int result = personEmailManager.delete(mailId, SecurityUtils.getCurrentUserId());

    Map jsonRes = new HashMap();

    if (result == 1) {
      jsonRes.put("result", "success");
    } else {
      jsonRes.put("result", "error");
    }
    Struts2Utils.renderJson(jsonRes, "encoding:UTF-8");
    return null;
  }

  /**
   * 设置登录帐号，一定要已经验证的，所有就不要发送验证码邮件 如果该帐号 被其他人设置为登录帐号或者是首要帐号，则设置不成功
   * 
   * @return
   * @throws Exception
   */
  @SuppressWarnings({"unchecked", "rawtypes"})
  @Action("/psnweb/psnsetting/ajaxfirstemail")
  public String ajaxFirstMail() throws Exception {

    Long mailId = form.getMailId();
    // 1成功设置邮件为首要邮件/登录帐号；2成果设置邮件为首要邮件，但是登录名已被其他用户占用;0更新失败 ; -1邮箱未验证;-2邮箱被占用
    int result = personEmailManager.updateFirstEmail(mailId, true);
    // data.result == 1
    // 如果登录邮件换了 删除cookie中的帐号密码信息。 tsz_2014.11.07
    if (result == 1) {
      // 更新sie的人员信息
      personalManager.updateSIEPersonInfo(form.getPsnId());
      // 如果更换了登录帐号 删除记住的帐号
      Cookie cookie = WebUtils.getCookie(Struts2Utils.getRequest(), "LashLoginUser");
      if (cookie != null) {
        setCookie(Struts2Utils.getResponse(), cookie, "false", "0");
      }
      Cookie cookie1 = WebUtils.getCookie((HttpServletRequest) Struts2Utils.getRequest(), RememberPwdContants.LOGINFO);
      if (cookie1 != null && cookie1.getValue() != null) {
        setCookie(Struts2Utils.getResponse(), cookie1, "false", "0");
      }
    }
    Map jsonRes = new HashMap();
    jsonRes.put("result", result);
    Struts2Utils.renderJson(jsonRes, "encoding:UTF-8");
    return null;
  }

  /**
   * 进入用户邮件列表. 弹框的邮件列表
   * 
   * @return
   * @throws Exception
   */
  @Action("/psnweb/psnsetting/ajaxloademail")
  public String load() throws Exception {
    // 获取用户邮件列表
    List<PersonEmailRegister> personEmails = personEmailManager.findPersonEmailList();
    form.setPersonEmailRegister(personEmails);
    return "psn_email_list";
  }

  /**
   * 帐号、邮箱列表 页面展示的列表
   * 
   * @return
   * @throws Exception
   */
  @Action("/psnweb/psnsetting/ajaxaccountemaillist")
  public String accountEmailList() throws Exception {
    // 获取用户邮件列表
    List<PersonEmailInfo> personEmails = personEmailManager.findPersonEmailInfoList();
    form.setPersonEmailInfoList(personEmails);
    return "account_email_list";
  }

  /**
   * 进入用户邮件列表.
   * 
   * @return
   * @throws Exception
   */
  @Action("/psnweb/psnsetting/ajaxsendconfirmemail")
  public String ajaxSendConfirmEmail() {
    Map jsonRes = new HashMap();
    try {
      if (form.getMailId() != null) {
        personEmailManager.sendConfirmEmail(form.getMailId());
        jsonRes.put("result", "success");
      } else {
        jsonRes.put("result", "error");
      }
      Struts2Utils.renderJson(jsonRes, "encoding:UTF-8");
      return null;
    } catch (Exception e) {
      jsonRes.put("result", "error");
    }
    Struts2Utils.renderJson(jsonRes, "encoding:UTF-8");
    return null;

  }

  /**
   * 验证输入的密码是否正确
   *
   * @return
   * @throws Exception
   */
  @Action("/psnweb/psnsetting/ajaxvalidateuser")
  public String ajaxvalidateuser() {
    Map jsonRes = new HashMap();
    try {
      if (form.getPsnId() != null && form.getPsnId() != 0L && StringUtils.isNotBlank(form.getOldpassword())) {
        Boolean result = userService.validdateUserPassword(form);
        if (result) {
          jsonRes.put("result", "success");
        } else {
          jsonRes.put("result", "error");
        }
      } else {
        jsonRes.put("result", "error");
      }
      Struts2Utils.renderJson(jsonRes, "encoding:UTF-8");
      return null;
    } catch (Exception e) {
      jsonRes.put("result", "error");
    }
    Struts2Utils.renderJson(jsonRes, "encoding:UTF-8");
    return null;

  }

  /**
   * 验证邮箱是否被占用
   *
   * @return
   * @throws Exception
   */
  @Action("/psnweb/psnsetting/ajaxvalidateemailhasused")
  public String ajaxvalidateemailhasused() {
    Map<String, String> jsonRes = new HashMap<String, String>();
    try {
      Long mailId = form.getMailId();
      Long psnId = SecurityUtils.getCurrentUserId();
      if (psnId != null && psnId != 0L) {
        // 在判断该邮箱是否被其他人设置为首要邮件或者登录帐号
        Boolean used = personEmailManager.validateEmailHasUsed(psnId, mailId);
        if (used) {
          jsonRes.put("result", "hasUsed");
        } else {
          jsonRes.put("result", "success");
        }
      } else {
        jsonRes.put("result", "error");
      }
    } catch (Exception e) {
      jsonRes.put("result", "error");
      logger.error("验证邮箱是否被占用出错", e);
    }
    Struts2Utils.renderJson(jsonRes, "encoding:UTF-8");
    return null;

  }

  /**
   * 取消绑定手机号
   *
   * @return
   * @throws Exception
   */
  @Action("/psnweb/psnsetting/ajaxunbindmobilenum")
  public String ajaxunbindmobilenum() {
    Map jsonRes = new HashMap();
    try {
      if (form.getPsnId() != null && form.getPsnId() != 0L) {
        userService.updateUser(form);
        jsonRes.put("result", "success");
      } else {
        jsonRes.put("result", "error");
      }
      Struts2Utils.renderJson(jsonRes, "encoding:UTF-8");
      return null;
    } catch (Exception e) {
      jsonRes.put("result", "error");
    }
    Struts2Utils.renderJson(jsonRes, "encoding:UTF-8");
    return null;

  }

  private void setCookie(HttpServletResponse response, Cookie cookie, String casAuth, String maxAge) {

    cookie.setValue(casAuth);
    cookie.setMaxAge(NumberUtils.toInt(maxAge, -1));
    cookie.setDomain(SystemBaseContants.IRIS_SNS_DOMAIN);
    cookie.setPath("/");

    response.addCookie(cookie);

  }

  /**
   * 发送手机验证码
   * 
   * @return
   * @throws Exception
   */
  @Action("/psnweb/psnsetting/ajaxsendmobilecode")
  public String ajaxSendMobileCode() throws Exception {
    Boolean isMobile = com.smate.core.base.utils.string.StringUtils.isMobileNumber(form.getMobileNumber());
    Map resMap = new HashMap();
    if (isMobile) {
      User user = userService.findUserByMobile(form.getMobileNumber());
      if (user == null) {
        String code = RandomStringUtils.randomNumeric(6);
        MobileMessageForm messsage = new MobileMessageForm();
        messsage.setDestId(form.getMobileNumber());
        // 注册验证码
        messsage.setPsnId(SecurityUtils.getCurrentUserId());
        messsage.setSmsType(MobileMessageWwxyServiceImpl.REG_TYPE);
        messsage.setContent(MobileMessageWwxyServiceImpl.buildRegMessage(code));
        cacheService.put(MobileMessageWwxyService.CACHE_NAME, MobileMessageWwxyService.EXPIRE_DATE,
            form.getMobileNumber(), code);
        mobileMessageWwxyService.initSendMessage(messsage);
        resMap.put("result", "success");
      } else {
        resMap.put("result", "exist");
      }
    } else {
      resMap.put("result", "error");
    }

    Struts2Utils.renderJson(resMap, "encoding:UTF-8");
    return null;
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
