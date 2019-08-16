package com.smate.center.oauth.action.interconnection;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Preparable;
import com.smate.center.oauth.model.interconnection.AccountInterconnectionForm;
import com.smate.center.oauth.service.interconnection.AccountInterconnectionService;
import com.smate.center.oauth.service.psnregister.PersonRegisterService;
import com.smate.center.oauth.utils.EditValidateUtils;
import com.smate.core.base.utils.struts2.Struts2Utils;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.struts2.convention.annotation.Action;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.util.HashMap;
import java.util.Map;

public class AccountInterconnectionAction extends ActionSupport
    implements ModelDriven<AccountInterconnectionForm>, Preparable {
  protected Logger logger = LoggerFactory.getLogger(getClass());
  private AccountInterconnectionForm form;
  @Autowired
  private AccountInterconnectionService accountInterconnectionService;
  @Autowired
  private PersonRegisterService personRegisterService;
  @Value("${domain.open.https}")
  private String domainOpenHtts;
  @Value("${domain.open}")
  private String domainOpen;

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
   * 创建并关联账号
   * 
   * @return
   */
  @Action("/oauth/connection/ajaxcreateandrelate")
  public String createAndRelateAccount() {
    try {
      // 解决jsonp跨域请求后禁止访问的问题
      // 是https
      if (form.getIsHttps()) {
        Struts2Utils.getResponse().addHeader("Access-Control-Allow-Origin", domainOpenHtts);
      } else {
        // 不是
        Struts2Utils.getResponse().addHeader("Access-Control-Allow-Origin", domainOpen);
      }
      form.setIsRegisterR(true);
      // 对密码进行MD5加密，后面调用center-open的人员注册服务传参需要
      form.setPassword(DigestUtils.md5Hex(form.getPassword()));
      if (!validateMsg()) {
        Map<String, String> map = new HashMap<String, String>(3);
        if (StringUtils.isNotBlank(form.getFromSys())
            && accountInterconnectionService.checkSysToken(form.getFromSys())) {
          Long openId = accountInterconnectionService.doCreateAndRelateAccount(form);
          form.setOpenId(openId);
          String signature = accountInterconnectionService.generateSignature(form);
          // 返回openId
          if (openId != null && !openId.equals(0L)) {
            map.put("result", "success");
            map.put("signature", signature);
            map.put("openId", openId.toString());
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
      }
    } catch (Exception e) {
      logger.error("创建关联账号出错", e);
    }
    return null;
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

    // 验证密码不为空
    if (EditValidateUtils.hasParam(form.getPassword(), 40, null)) {
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
   * 测试ajax跨域请求
   * 
   * @return
   */
  @Action("/oauth/test/ajax")
  public String testAjax() {
    // 解决jsonp跨域请求后禁止访问的问题
    Struts2Utils.getResponse().addHeader("Access-Control-Allow-Origin", domainOpenHtts);
    Map<String, String> map = new HashMap<String, String>(2);
    map.put("result", "success");
    map.put("openId", "12312312");
    Struts2Utils.renderJson(map, "encoding:UTF-8");
    return null;
  }

}
