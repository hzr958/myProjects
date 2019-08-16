package com.smate.center.oauth.action.password;

import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Actions;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Preparable;
import com.smate.center.oauth.form.ForgetPwdForm;
import com.smate.center.oauth.service.password.DecryptPwdService;
import com.smate.center.oauth.service.profile.PersonProfileService;
import com.smate.core.base.utils.struts2.Struts2Utils;

/**
 * 忘记密码Action.
 * 
 * @author zzx
 * 
 */
@Results({@Result(name = "forget_success", location = "/WEB-INF/jsp/pc/pwd/forget/success.jsp"),
    @Result(name = "forget_index", location = "/WEB-INF/jsp/pc/pwd/forget/index.jsp"),
    @Result(name = "forget_failure", location = "/WEB-INF/jsp/pc/pwd/forget/failure.jsp"),
    @Result(name = "mobile_forget_index", location = "/WEB-INF/jsp/mobile/pwd/forget/index.jsp"),
    @Result(name = "mobile_forget_success", location = "/WEB-INF/jsp/mobile/pwd/forget/success.jsp"),
    @Result(name = "reset_index", location = "/WEB-INF/jsp/pc/pwd/reset/index.jsp"),
    @Result(name = "reset_invalid", location = "/WEB-INF/jsp/pc/pwd/reset/invalid.jsp"),
    @Result(name = "mobile_reset_index", location = "/WEB-INF/jsp/pc/pwd/reset/mobile/mobile_index.jsp"),
    @Result(name = "mobile_reset_invalid", location = "/WEB-INF/jsp/pc/pwd/reset/mobile/mobile_invalid.jsp"),
    @Result(name = "mobile_reset_failure", location = "/WEB-INF/jsp/pc/pwd/reset/mobile/mobile_failure.jsp"),
    @Result(name = "mobile_reset_success", location = "/WEB-INF/jsp/pc/pwd/reset/mobile/mobile_success.jsp"),
    @Result(name = "reset_failure", location = "/WEB-INF/jsp/pc/pwd/reset/failure.jsp"),
    @Result(name = "reset_success", location = "/WEB-INF/jsp/pc/pwd/reset/success.jsp")})
public class PasswordAction extends ActionSupport implements ModelDriven<ForgetPwdForm>, Preparable {

  private static final long serialVersionUID = -5703232304761259290L;
  protected final Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private PersonProfileService personProfileService;
  @Autowired
  private DecryptPwdService decryptPwdService;

  private ForgetPwdForm form;

  @Actions(value = {@Action("/oauth/pwd/forget/index")})
  public String main() {
    return "forget_index";
  }

  @Action("/oauth/mobile/pwd/forget/index")
  public String mobileMain() {
    return "mobile_forget_index";
  }

  @Action("/oauth/pwd/reset/index")
  public String resetPwd() {
    boolean flag = personProfileService.verifyResetPwdReqParam(form);
    if (flag) {
      return "reset_index"; // 返回重置密码页面
    } else {
      return "reset_invalid"; // 返回无效的重置密码链接提示页面
    }
  }

  @Action("/oauth/mobile/pwd/reset/index")
  public String mobileResetPwd() {
    boolean flag = personProfileService.verifyResetPwdReqParam(form);
    if (flag) {
      return "mobile_reset_index"; // 返回重置密码页面
    } else {
      return "mobile_reset_invalid"; // 返回无效的重置密码链接提示页面
    }
  }

  @Action("/oauth/mobile/pwd/reset/toreset")
  public String mobileToResetPwd() {
    if (StringUtils.isNotBlank(form.getEmail()) && StringUtils.isNotBlank(form.getKey())
        && StringUtils.isNoneBlank(form.getGen()) && StringUtils.isNotBlank(form.getNewpwd())) {
      boolean valid = personProfileService.verifyResetPwdReqParam(form);
      if (!valid) {
        return "mobile_reset_invalid";
      }
      String deDesPassword = decryptPwdService.DeDesPassword(form.getEmail(), form.getNewpwd());
      if (StringUtils.isBlank(deDesPassword)) {
        deDesPassword = form.getNewpwd();
      }
      boolean flag = personProfileService.updateNewPwd(form.getKey(), deDesPassword);
      if (flag) {
        return "mobile_reset_success";
      } else {
        return "mobile_reset_failure";
      }
    } else {
      return "mobile_reset_invalid";
      // return "mobile_reset_success";
      // return "mobile_reset_failure";

    }
  }

  @Action("/oauth/pwd/reset/toreset")
  public String toResetPwd() {
    if (StringUtils.isNotBlank(form.getEmail()) && StringUtils.isNotBlank(form.getKey())
        && StringUtils.isNoneBlank(form.getGen()) && StringUtils.isNotBlank(form.getNewpwd())) {
      boolean valid = personProfileService.verifyResetPwdReqParam(form);
      if (!valid) {
        return "reset_invalid";
      }
      boolean flag = personProfileService.updateNewPwd(form.getKey(), form.getNewpwd());
      if (flag) {
        return "reset_success";
      } else {
        return "reset_failure";
      }
    } else {
      return "reset_invalid";
    }
  }

  /**
   * 检验邮箱是否存在
   * 
   * @return
   * @throws Exception
   */
  @Action("/oauth/pwd/ajaxcheckedemail")
  public String ajaxCheckedEmail() throws Exception {
    String trimEmail = this.form.getEmail().trim();
    if (personProfileService.validateEmailIsExist(trimEmail)) {
      Struts2Utils.renderText("true");
    } else {
      Struts2Utils.renderText("false");
    }
    return null;
  }

  /**
   * 发送忘记密码邮件
   * 
   * @return
   * @throws Exception
   */
  @SuppressWarnings("finally")
  @Action("/oauth/pwd/forget/sendemail")
  public String sendForgetPwdMail() throws Exception {
    try {
      if (StringUtils.isNotBlank(form.getEmail())
          && Struts2Utils.getHttpReferer().contains("/oauth/pwd/forget/index")) {
        String emails = personProfileService.sendForgetPasswordMail(form.getEmail());
        form.setEmail(emails);
        return "forget_success";
      } else {
        Struts2Utils.redirect("/oauth/pwd/forget/index");
        return "";
      }
    } catch (RuntimeException e) {
      logger.error("发送忘记密码邮件失败" + e);
    }
    return "forget_failure";
  }

  @Action("/oauth/pwd/forget/failure")
  public String forgetFail() {
    return "forget_failure";
  }

  @Action("/oauth/pwd/reset/failure")
  public String resetFail() {
    return "reset_failure";
  }

  @SuppressWarnings("finally")
  @Action("/oauth/mobile/pwd/forget/sendemail")
  public String sendResetPwdEmailPc() {
    String emails = "";
    try {
      if (StringUtils.isNotBlank(form.getEmail())) {
        emails = personProfileService.sendForgetPasswordMail(form.getEmail());
        form.setEmail(emails);
        return "mobile_forget_success";
      } else {
        return "mobile_forget_index";
      }
    } catch (Exception e) {
      logger.error("发送忘记密码邮件失败 , email = " + form.getEmail(), e);
    }
    return "mobile_forget_index";
  }

  @Override
  public ForgetPwdForm getModel() {

    return form;
  }

  @Override
  public void prepare() throws Exception {
    if (form == null) {
      form = new ForgetPwdForm();
    }
  }

}
