package com.smate.center.open.form;

import java.io.Serializable;

/**
 * 登录form对象
 * 
 * @author tsz
 * 
 * @since 6.0.1
 * @version 6.0.1
 */
public class OpenLoginForm implements Serializable {

  private static final long serialVersionUID = -8834813600310306836L;
  private String token;// 第三方系统特定唯一标识
  private String userName;// 登录sns的用户名
  private String password;// 登录sns的密码
  private String validateCode;// 登录sns的校验码,登录失败3此后显示
  private String needValidateCode;// 是否需要验证码 1:need 0:notneed
  private Integer loginStatus;// 1:success 0:failed
  private String msg;// 登录信息
  private String thirdSysName;// 第三方系统的名称
  private String thirdSysNameUs;// 第三方系统的名称国际化
  private String back; // 登录验证成功后需要跳转的url 由第三方系统 访问登录页面时传入参数
  private Long openId; // 重定向链接参数 openId
  private String failedMsg;// 登录失败信息
  private String type; // 类型参数
  private Long psnId;
  private String encodeUserName; // 加密后的用户名
  private String encodePassword; // 加密后的账号
  private String zhlastName;// 姓
  private String zhfirstName; // 名
  private String locale = "zh_CN";

  public String getToken() {
    return token;
  }

  public void setToken(String token) {
    this.token = token;
  }

  public String getUserName() {
    return userName;
  }

  public void setUserName(String userName) {
    this.userName = userName;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public String getValidateCode() {
    return validateCode;
  }

  public void setValidateCode(String validateCode) {
    this.validateCode = validateCode;
  }

  public String getNeedValidateCode() {
    return needValidateCode;
  }

  public void setNeedValidateCode(String needValidateCode) {
    this.needValidateCode = needValidateCode;
  }

  public Integer getLoginStatus() {
    return loginStatus;
  }

  public void setLoginStatus(Integer loginStatus) {
    this.loginStatus = loginStatus;
  }

  public String getMsg() {
    return msg;
  }

  public void setMsg(String msg) {
    this.msg = msg;
  }

  public String getThirdSysName() {
    return thirdSysName;
  }

  public void setThirdSysName(String thirdSysName) {
    this.thirdSysName = thirdSysName;
  }

  public String getBack() {
    return back;
  }

  public void setBack(String back) {
    this.back = back;
  }

  public Long getOpenId() {
    return openId;
  }

  public void setOpenId(Long openId) {
    this.openId = openId;
  }

  public String getFailedMsg() {
    return failedMsg;
  }

  public void setFailedMsg(String failedMsg) {
    this.failedMsg = failedMsg;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public Long getPsnId() {
    return psnId;
  }

  public void setPsnId(Long psnId) {
    this.psnId = psnId;
  }

  public String getEncodeUserName() {
    return encodeUserName;
  }

  public void setEncodeUserName(String encodeUserName) {
    this.encodeUserName = encodeUserName;
  }

  public String getEncodePassword() {
    return encodePassword;
  }

  public void setEncodePassword(String encodePassword) {
    this.encodePassword = encodePassword;
  }

  public String getZhlastName() {
    return zhlastName;
  }

  public void setZhlastName(String zhlastName) {
    this.zhlastName = zhlastName;
  }

  public String getZhfirstName() {
    return zhfirstName;
  }

  public void setZhfirstName(String zhfirstName) {
    this.zhfirstName = zhfirstName;
  }

  public String getLocale() {
    return locale;
  }

  public void setLocale(String locale) {
    this.locale = locale;
  }

  public String getThirdSysNameUs() {
    return thirdSysNameUs;
  }

  public void setThirdSysNameUs(String thirdSysNameUs) {
    this.thirdSysNameUs = thirdSysNameUs;
  }

}
