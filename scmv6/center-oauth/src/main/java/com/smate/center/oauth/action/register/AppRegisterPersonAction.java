package com.smate.center.oauth.action.register;

import java.util.HashMap;
import java.util.Map;

import org.apache.struts2.convention.annotation.Action;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Preparable;
import com.smate.center.oauth.model.profile.PersonRegisterForm;
import com.smate.center.oauth.service.psnregister.AppPersonRegisterService;
import com.smate.core.base.utils.constant.IOSHttpStatus;
import com.smate.core.base.utils.struts2.Struts2Utils;

/**
 * @author LJ SCM app客户端人员注册接口 2017年6月21日
 */
public class AppRegisterPersonAction extends ActionSupport implements ModelDriven<PersonRegisterForm>, Preparable {
  private static final long serialVersionUID = 1759823946215895023L;

  private final Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private AppPersonRegisterService appPersonRegisterService;
  private PersonRegisterForm form;
  private String verifCode;
  private String password;
  private String status = IOSHttpStatus.NOT_MODIFIED;// 默认状态

  @Action("/oauth/register/ajaxCheckedAccount")
  public String checkedUserName() {
    Boolean flag = true;// true:表示账户可用
    try {
      flag = appPersonRegisterService.checkUserName(form.getEmail().toLowerCase());
      status = IOSHttpStatus.OK;
    } catch (Exception e) {
      logger.error("校验账户是否存在出错！", e);
      status = IOSHttpStatus.INTERNAL_SERVER_ERROR;
    }
    Map<String, Object> map = new HashMap<String, Object>();
    map.put("canuse", flag);

    Struts2Utils.renderJson(buildInfo(map), "encoding:UTF-8");
    return null;
  }

  /**
   * 发送验证邮件
   * 
   * @return String
   * @throws Exception
   */
  @Action("/oauth/register/sendMail")
  public String sendMail() {
    int flag = 0;// 0失败，1成功，2超过次数
    try {
      verifCode = appPersonRegisterService.sendverifiMail(form.getEmail().toLowerCase());
      status = IOSHttpStatus.OK;
      flag = 1;
    } catch (Exception e) {
      status = IOSHttpStatus.INTERNAL_SERVER_ERROR;
      logger.error("发送验证邮件出错", e);
    }
    Map<String, Object> map = new HashMap<String, Object>();
    map.put("sendstatus", flag);
    Struts2Utils.renderJson(buildInfo(map), "encoding:UTF-8");
    return null;

  }

  /**
   * 发送验证短信
   * 
   * @return String
   * @throws Exception
   */
  @Action("/oauth/register/sendMessage")
  public String sendMessage() {
    int flag = 0;// 0失败，1成功，2超过次数
    try {
      verifCode = appPersonRegisterService.sendverifiMessage(form.getMobile());
      status = IOSHttpStatus.OK;
      if ("limitcount".equals(verifCode) == false) {
        flag = 1;
      } else {
        flag = 2;
      }
    } catch (Exception e) {
      status = IOSHttpStatus.INTERNAL_SERVER_ERROR;
      logger.error("发送短信验证码出错！", e);
    }
    Map<String, Object> map = new HashMap<String, Object>();
    map.put("sendstatus", flag);
    Struts2Utils.renderJson(buildInfo(map), "encoding:UTF-8");
    return null;
  }


  @Action("/oauth/register/checkcode")
  public String checkCode() {
    int checkCode = 0;// 1表示验证成功，2表示验证码过期，0表示验证码匹配不上或者出错
    try {
      checkCode = appPersonRegisterService.checkCode(form.getEmail().toLowerCase(), verifCode);
      status = IOSHttpStatus.OK;
    } catch (Exception e) {
      logger.error("校验验证码出错", e);
      status = IOSHttpStatus.INTERNAL_SERVER_ERROR;
    }
    Map<String, Object> map = new HashMap<String, Object>();

    map.put("checkstatus", checkCode);
    Struts2Utils.renderJson(buildInfo(map), "encoding:UTF-8");
    return null;
  }



  /**
   * 保存并注册信息
   * 
   * @return
   */
  @Action("/oauth/register/saveuserinfo")
  public String saveUserInfo() {
    Map<String, Object> map = new HashMap<String, Object>();
    Long psnopenId = null;
    form.setEmailLanguageVersion(this.getLocale().toString());// 设置个人邮件接收的语言版本，默认为当前系统的语言版本
    try {
      appPersonRegisterService.checkUserInfo(form);
    } catch (Exception e) {
      logger.info("账户信息验证出错！", e);
      map.put("psnopenId", null);
      Struts2Utils.renderJson(buildInfo(map), "encoding:UTF-8");
      return null;
    }

    try {
      psnopenId = appPersonRegisterService.saveUserInfo(form);
      if (psnopenId != null) {
        status = IOSHttpStatus.OK;
      }
    } catch (Exception e) {
      logger.error("注册失败！", e);
      status = IOSHttpStatus.INTERNAL_SERVER_ERROR;
    }
    map.put("psnopenId", psnopenId);
    map.put("des3PsnId", form.getPersonDes3Id());
    Struts2Utils.renderJson(buildInfo(map), "encoding:UTF-8");
    return null;
  }


  /**
   * 构建返回给IOS端统一Json格式
   */
  public Map<String, Object> buildInfo(Object data) {
    Map<String, Object> infoData = new HashMap<String, Object>();
    Map<String, Object> returnData = new HashMap<String, Object>();
    infoData.put("commentlist", data);
    infoData.put("total", 0);
    returnData.put("status", status);
    returnData.put("results", infoData);
    return returnData;

  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public String getVerifCode() {
    return verifCode;
  }

  public void setVerifCode(String verifCode) {
    this.verifCode = verifCode;
  }

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

}
