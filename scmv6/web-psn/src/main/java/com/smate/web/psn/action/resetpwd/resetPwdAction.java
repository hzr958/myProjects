package com.smate.web.psn.action.resetpwd;

import java.util.HashMap;
import java.util.Map;

import org.apache.struts2.convention.annotation.Action;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.opensymphony.xwork2.ActionSupport;
import com.smate.core.base.utils.model.cas.security.User;
import com.smate.core.base.utils.security.SecurityUtils;
import com.smate.core.base.utils.service.security.SysUserLoginService;
import com.smate.core.base.utils.struts2.Struts2Utils;
import com.smate.web.psn.service.desdepassword.DesDePasswordService;
import com.smate.web.psn.service.user.UserService;
import com.smate.web.psn.utils.EditValidateUtils;

/**
 * 重置密码action
 * 
 * @author lhd
 *
 */
public class resetPwdAction extends ActionSupport {

  private static final long serialVersionUID = -305982959733919464L;
  protected final Logger logger = LoggerFactory.getLogger(getClass());

  private String newPwd;// 新密码
  private String confirmPwd;// 确认密码
  @Autowired
  private UserService userService;
  @Autowired
  private DesDePasswordService desDePasswordService;
  @Autowired
  private SysUserLoginService sysUserLoginService;

  /**
   * 重置密码
   * 
   * @author lhd
   * @return
   */
  @Action("/psnweb/personal/ajaxresetpwd")
  public String resetPwd() {
    try {
      Map<String, String> json = new HashMap<String, String>();
      Long psnId = SecurityUtils.getCurrentUserId();
      User user = userService.findUserById(psnId);
      if (user != null) {
        String username = user.getLoginName();
        newPwd = desDePasswordService.DesDePassword(username, newPwd);
        confirmPwd = desDePasswordService.DesDePassword(username, confirmPwd);
        String validateSaveData = validatePwd();
        if (validateSaveData != null) {
          Struts2Utils.renderJson("{result:'error',msg:'" + validateSaveData + "'}", "encoding:UTF-8");
          return null;
        }
        if (userService.resetPassword(newPwd, psnId)) {
          json.put("result", "success");
          Struts2Utils.renderJson(json, "encoding:UTF-8");
        } else {
          json.put("result", "error");
          // json.put("msg", getText("changepassowd.msg.errorNewPassword"));
          Struts2Utils.renderJson(json, "encoding:UTF-8");
        }
      }
    } catch (Exception e) {
      logger.error("重置密码出错,psnId= " + SecurityUtils.getCurrentUserId(), e);
    }
    return null;
  }

  /**
   * 校验.
   * 
   * @author lhd
   * @return
   * @throws Exception
   */
  public String validatePwd() throws Exception {
    StringBuilder sb = new StringBuilder();
    // 新密码的最大长度控制为40个字符
    if (EditValidateUtils.hasParam(this.newPwd, 40, null)) {
      sb.append(getText("changepassowd.msg.errorNewPassword")).append("，");
    } else if (!this.newPwd.equals(this.confirmPwd)) {
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
   * 更新用户最后登录时间
   * 
   * @author lhd
   * @return
   */
  @Action("/psnweb/personal/ajaxupdatelogintime")
  public String updateLastLoginTime() {
    Map<String, Object> map = new HashMap<String, Object>();
    try {
      Long psnId = SecurityUtils.getCurrentUserId();
      if (sysUserLoginService.updateLoginTime(psnId)) {
        map.put("result", "success");
      }
      Struts2Utils.renderJson(map, "encoding:UTF-8");
    } catch (Exception e) {
      logger.error("重置密码框-更新最后登录时间-出错,psnId= " + SecurityUtils.getCurrentUserId(), e);
    }
    return null;
  }


  public String getNewPwd() {
    return newPwd;
  }

  public void setNewPwd(String newPwd) {
    this.newPwd = newPwd;
  }

  public String getConfirmPwd() {
    return confirmPwd;
  }

  public void setConfirmPwd(String confirmPwd) {
    this.confirmPwd = confirmPwd;
  }



}
