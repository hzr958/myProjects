package com.smate.web.psn.action.setting;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Preparable;
import com.smate.core.base.utils.model.cas.security.SysUserLoginLog;
import com.smate.core.base.utils.model.cas.security.User;
import com.smate.core.base.utils.model.msg.MessageLog;
import com.smate.core.base.utils.security.Des3Utils;
import com.smate.core.base.utils.security.SecurityUtils;
import com.smate.core.base.utils.service.msg.MobileMessageWwxyService;
import com.smate.core.base.utils.struts2.Struts2Utils;
import com.smate.web.psn.cache.PsnCacheService;
import com.smate.web.psn.form.AccountEmailForm;
import com.smate.web.psn.model.psninfo.PersonEmailRegister;
import com.smate.web.psn.service.email.AccountEmailCheckLogService;
import com.smate.web.psn.service.profile.PersonEmailManager;
import com.smate.web.psn.service.user.UserService;


@Results({
    @Result(name = "account_validate_success",
        location = "/WEB-INF/jsp/psnsetting/accountemail/account_validate_success.jsp"),
    @Result(name = "confirmResult", location = "/WEB-INF/jsp/psnsetting/accountemail/confirmEmailResult.jsp"),})
public class AccountEmailCheckAction extends ActionSupport implements ModelDriven<AccountEmailForm>, Preparable {

  /**
   * 
   */
  private static final long serialVersionUID = 2084616899573852407L;

  protected final Logger logger = LoggerFactory.getLogger(getClass());

  private AccountEmailForm form;

  @Resource
  private AccountEmailCheckLogService accountEmailCheckLogService;
  @Resource
  private UserService userService;
  @Autowired
  private PersonEmailManager personEmailManager;
  @Autowired
  private PsnCacheService psnCacheService;
  @Autowired
  private MobileMessageWwxyService messageWwxyService;


  /**
   * 检查是否要弹框验证账号邮箱
   * 
   * @return
   * @throws Exception
   */
  @Action("/psnweb/accountvaliate/ajaxisneed")
  public String ajaxisneed() {
    Map<String, Object> json = new HashMap<String, Object>();
    try {
      if (form.getCurrentPsnId() != null && form.getCurrentPsnId() != 0L) {
        Boolean need = accountEmailCheckLogService.needValidateAccount(form);
        json.put("result", need);
        json.put("newEmail", form.getNewEmail());
        json.put("delaySendDate", form.getDelaySendDate());
      } else {
        // 用户没有登录，不需要弹框
        json.put("result", false);
      }
    } catch (Exception e) {
      json.put("result", "failure");
      logger.error("检查是否要弹框验证账号邮箱异常！", e);
    }
    Struts2Utils.renderJson(json, "encoding:UTF-8");
    return null;
  }

  /**
   * 检查是否需要验证手机号 账号密码登录才要，验证手机号
   * 
   * @return
   * @throws Exception
   */
  @Action("/psnweb/mobilevaliate/ajaxisneed")
  public String ajaxIsNeedMobile() throws Exception {
    Long psnId = SecurityUtils.getCurrentUserId();
    Map result = new HashMap();
    User user = userService.findUserById(psnId);
    if (user != null && StringUtils.isBlank(user.getMobileNumber())) {
      SysUserLoginLog log = userService.findLastLog(psnId);
      if (log != null && log.getLoginType() == 8) {
        MessageLog messageLog = messageWwxyService.findLastestLogByTime(psnId, MobileMessageWwxyService.REG_TYPE);
        if (messageLog != null) {
          long time = new Date().getTime() / 1000 - messageLog.getSendTime().getTime() / 1000;
          if (time < 60) {
            result.put("mobileNumber", messageLog.getSmsTo());
            result.put("countdown", 60 - time);
          }
        }
        result.put("result", true);
        result.put("des3PsnId", Des3Utils.encodeToDes3(psnId.toString()));
      } else {
        result.put("result", false);
      }
    } else {
      result.put("result", false);
    }
    Struts2Utils.renderJson(result);
    return null;
  }

  /**
   * 检查是否需要验证手机号 账号密码登录才要，验证手机号
   * 
   * @return
   * @throws Exception
   */
  @Action("/psnweb/mobilevaliate/ajaxgetmobile")
  public String ajaxgetMobile() throws Exception {
    Long psnId = SecurityUtils.getCurrentUserId();
    Map result = new HashMap();
    SysUserLoginLog log = userService.findLastLog(psnId);
    if ((log != null && log.getLoginType() == 8) || form.isShowMobileBox()) {
      MessageLog messageLog = messageWwxyService.findLastestLogByTime(psnId, MobileMessageWwxyService.REG_TYPE);
      if (messageLog != null) {
        long time = new Date().getTime() / 1000 - messageLog.getSendTime().getTime() / 1000;
        if (time < 60) {
          result.put("mobileNumber", messageLog.getSmsTo());
          result.put("countdown", 60 - time);
        } else {
          result.put("mobileNumber", messageLog.getSmsTo());
          result.put("countdown", 0);
        }
      }
      result.put("result", true);
      result.put("des3PsnId", Des3Utils.encodeToDes3(psnId.toString()));
    } else {
      result.put("result", false);
    }
    Struts2Utils.renderJson(result);
    return null;
  }

  /**
   * 确定手机号
   * 
   * @return
   * @throws Exception
   */
  @Action("/psnweb/mobilevaliate/ajaxsuremobile")
  public String ajaxSureMobile() {
    Map result = new HashMap();
    try {
      result = sureMobile(form);
    } catch (Exception e) {
      logger.error("确认验证码异常", e);
      result.put("result", "error");
    }
    Struts2Utils.renderJson(result);
    return null;
  }

  private Map sureMobile(AccountEmailForm form) throws Exception {
    Map result = new HashMap();
    Long psnId = SecurityUtils.getCurrentUserId();
    User user = userService.findUserById(psnId);
    if (user == null) {
      result.put("result", "error");
    }
    Boolean isSend = messageWwxyService.isSendMessageTheDay(form.getMobileNumber(), messageWwxyService.REG_TYPE);
    if (!isSend) {
      result.put("result", "mobileError");
      return result;
    }
    Object obj = psnCacheService.get(MobileMessageWwxyService.CACHE_NAME, form.getMobileNumber());
    if (obj == null) {
      result.put("result", "codeInvalid");
      return result;
    }
    if (obj.toString().equals(form.getMobileValidateCode())) {
      user.setMobileNumber(form.getMobileNumber());
      userService.updateUser(user);
      result.put("result", "success");
    } else {
      result.put("result", "codeError");
    }
    return result;
  }

  /**
   * 邮件链接验证 0=未处理 ， 1验证成功 ， 9=验证失败 ， 2=重新发送
   * 
   * @return
   * @throws Exception
   */
  @Action("/psnweb/accountvalidate/ajaxdovalidte")
  public String ajaxdovalidtebyemail() {
    try {
      if (form.getId() != null && form.getId() != 0L) {
        Integer result = accountEmailCheckLogService.daValidateByEmail(form);
        if (result == 1) {
          form.setConfirmResult("success");
        } else if (result == 2) {
          form.setConfirmResult("invalid");
        } else {
          form.setConfirmResult("error");
        }
      }
    } catch (Exception e) {
      form.setConfirmResult("error");
      logger.error("邮件链接验证账号异常！", e);
    }
    return "confirmResult";
  }

  /**
   * 邮件 验证码 验证 0=未处理 ， 1验证成功 ， 9=验证失败 ， 2=重新发送 3= 异常
   * 
   * @return
   * @throws Exception
   */
  @Action("/psnweb/accountvalidate/ajaxdovalidteforcode")
  public String ajaxdovalidteforcode() {
    Map<String, Object> json = new HashMap<String, Object>();
    Integer result = 0;
    try {
      if (StringUtils.isNotBlank(form.getValidateCode()) && form.getCurrentPsnId() != null
          && form.getCurrentPsnId() != 0L) {
        result = accountEmailCheckLogService.daValidateByCode(form);
      } else {
        result = 0;
      }
    } catch (Exception e) {
      result = 3;
      logger.error("邮件 验证码验证账号异常！", e);
    }
    json.put("result", result);
    Struts2Utils.renderJson(json, "encoding:UTF-8");
    return null;
  }

  /**
   * 个人设置，验证码验证某个邮件 0=未处理 ， 1验证成功 ， 3=异常 ； 9=验证码错误
   * 
   * @return
   * @throws Exception
   */
  @Action("/psnweb/psnseting/ajaxdovalidateemail")
  public String ajaxdovalidateemail() {
    Map<String, Object> json = new HashMap<String, Object>();
    Integer result = 0;
    try {
      if (StringUtils.isNotBlank(form.getValidateCode()) && form.getCurrentPsnId() != null
          && form.getCurrentPsnId() != 0L && form.getMailId() != null) {
        PersonEmailRegister emailRegister = personEmailManager.findPsnEmailById(form.getMailId());
        if (emailRegister != null) {
          form.setNewEmail(emailRegister.getEmail());
          // 如果个人 设置的邮箱，是自己的登录帐号邮箱，则调用邮件验证码的处理方式
          User user = userService.findUserById(form.getCurrentPsnId());
          if (user != null && user.getLoginName().equalsIgnoreCase(form.getNewEmail())) {
            result = accountEmailCheckLogService.daValidateByCode(form);
          } else {
            result = accountEmailCheckLogService.psnSetDoValidateEmail(form);
          }
        }

      } else {
        result = 0;
      }
    } catch (Exception e) {
      result = 3;
      logger.error("邮件 验证码验证账号异常！", e);
    }
    json.put("result", result);
    Struts2Utils.renderJson(json, "encoding:UTF-8");
    return null;
  }


  /**
   * 编辑邮箱 验证 result 1 = 更新成功， 2=邮箱被占用 ， 3格式不正确 ， 4异常
   * 
   * @return
   * @throws Exception
   */
  @Action("/psnweb/accountvalidate/ajaxeditloginname")
  public String ajaxeditloginname() {
    Map<String, Object> json = new HashMap<String, Object>();
    try {
      Integer result = 0;
      if (StringUtils.isNotBlank(form.getNewEmail()) && form.getCurrentPsnId() != null
          && form.getCurrentPsnId() != 0L) {
        result = userService.editEmailLoginname(form);
        if (result == 1) {
          // 更新邮箱账号成功，重新发送发送一封邮件
          accountEmailCheckLogService.reSendAccountEmailValidateEmail(form);
        }
      }
      json.put("result", result);
    } catch (Exception e) {
      json.put("result", 4);
      logger.error("邮件 验证码验证账号异常！", e);
    }
    Struts2Utils.renderJson(json, "encoding:UTF-8");
    return null;
  }



  /**
   * 发送 账号 的确认的邮件
   * 
   * @return
   */
  @Action("/psnweb/accountvalidate/ajaxsendaccountconfirmemail")
  public String ajaxsendaccountconfirmemail() {
    Map<String, Object> json = new HashMap<String, Object>();
    try {
      Boolean result = false;
      if (StringUtils.isNotBlank(form.getNewEmail()) && form.getValidatePsnId() != null
          && form.getValidatePsnId() != 0L) {
        result = accountEmailCheckLogService.sendAccountEmailValidateEmail(form);
      }
      json.put("result", result);
    } catch (Exception e) {
      json.put("result", false);
      logger.error("邮件 验证码验证账号异常！", e);
    }
    Struts2Utils.renderJson(json, "encoding:UTF-8");
    return null;
  }

  /**
   * 再一次重新发送 账号 的确认的邮件 从登陆账号中获取邮箱
   * 
   * @return
   */
  @Action("/psnweb/accountvalidate/ajaxresendaccountconfirmemail")
  public String ajaxresendaccountconfirmemail() {
    Map<String, Object> json = new HashMap<String, Object>();
    try {
      Boolean result = false;
      if (form.getCurrentPsnId() != null && form.getCurrentPsnId() != 0L) {
        result = accountEmailCheckLogService.reSendAccountEmailValidateEmail(form);
        json.put("delaySendDate", form.getDelaySendDate());
      }
      json.put("result", result);
    } catch (Exception e) {
      json.put("result", false);
      logger.error("邮件 验证码验证账号异常！", e);
    }
    Struts2Utils.renderJson(json, "encoding:UTF-8");
    return null;
  }


  /**
   * 个人设置页面发送，验证码邮件
   * 
   * @return
   */
  @Action("/psnweb/psnset/ajaxsendvalidatememail")
  public String ajaxsendvalidatememail() {
    Map<String, Object> json = new HashMap<String, Object>();
    try {
      Boolean result = false;
      if (form.getCurrentPsnId() != null && form.getCurrentPsnId() != 0L && form.getMailId() != null) {
        PersonEmailRegister personEmail = personEmailManager.findPsnEmailById(form.getMailId());
        if (personEmail != null
            && personEmail.getPerson().getPersonId().longValue() == form.getCurrentPsnId().longValue())
          ;
        form.setValidatePsnId(form.getCurrentPsnId());
        form.setNewEmail(personEmail.getEmail());
        if (!accountEmailCheckLogService.checkAccountSendDate(form)) {
          json.put("result", "delaySend");
        } else {
          if (form.getResend()) {
            result = accountEmailCheckLogService.reSendAccountEmailValidateEmail(form);
          } else {
            result = accountEmailCheckLogService.sendAccountEmailValidateEmail(form);
          }
          json.put("result", result);
        }
      }

    } catch (Exception e) {
      json.put("result", false);
      logger.error("邮件 验证码验证账号异常！", e);
    }
    Struts2Utils.renderJson(json, "encoding:UTF-8");
    return null;
  }

  /**
   * 检查邮箱是否有效
   * 
   * @return
   */
  @Action("/psnweb/accountvalidate/ajaxcheckemailisvalidate")
  public String ajaxcheckemailisvalidate() {
    Map<String, Object> json = new HashMap<String, Object>();
    try {
      Boolean result = false;
      if (StringUtils.isNotBlank(form.getNewEmail()) && form.getCurrentPsnId() != null
          && form.getCurrentPsnId() != 0L) {
        result = accountEmailCheckLogService.checkEmailIsValidate(form);
      }
      json.put("result", result);
    } catch (Exception e) {
      json.put("result", false);
      logger.error("邮件 验证码验证账号异常！", e);
    }
    Struts2Utils.renderJson(json, "encoding:UTF-8");
    return null;
  }



  @Override
  public void prepare() throws Exception {
    if (form == null) {
      form = new AccountEmailForm();
    }

  }

  @Override
  public AccountEmailForm getModel() {
    return form;
  }



}
