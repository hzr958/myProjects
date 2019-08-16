package com.smate.core.base.oauth.model;

import java.io.Serializable;

/**
 * 登录form对象
 * 
 * @author tsz
 * 
 * @since 6.0.1
 * @version 6.0.1
 */
public class OauthLoginForm implements Serializable {

  private static final long serialVersionUID = -8834813600310306836L;
  private String sys = "SNS";// 请求登录系统标记 还需要作为 需要加载 哪个库的权限用
  private String userName;// 登录sns的用户名
  private String password;// 登录sns的密码
  private String validateCode;// 登录sns的校验码,登录失败3此后显示
  private String needValidateCode;// 是否需要验证码 1:need 0:notneed
  private Integer loginStatus = 0;// 1:success 0:failed
  private String msg;// 登录信息
  private String service = ""; // 登录验证成功后需要跳转的url传入参数(已经做des3
                               // 加密),默认给"",不然为null编码的时候会报错
  private String failedMsg;// 登录失败信息
  private Integer errMsgPosition;// 登录失败提示语位置 1：账号 2：密码 3：验证码
  private Integer needSelectIns; // 是否需要选择单位 1:need 0:notneed
  private Integer needSelectRole; // 是否需要选择角色 1:need 0:notneed
  private Long psnId; // 登录用户id
  private Long insId; // 单位id
  private Long roleId; // 角色id
  private String sysDomain; // 系统的域名
  private String SYSSID; // 来共享权限的系统sessionId
  private String locale; // 语言

  private String loginType;// 登录类型 3-微信 2-微博 1-QQ
  private String forwardUrl;
  private boolean rememberMe = true;
  private Integer randomNum = 1;
  private String AID; // 自动登录加密串
  private String params; // 保存参数
  private Boolean mobileCodeLogin = false; // 手机验证码登录
  private String mobileNum; // 手机号
  private String mobileCode; // 手机验证码
  private String runEnv; // 运行环境
  private String code; // 微信端接口返回链接带的参数，获取微信openId需要用到
  private Integer bindType; // 绑定类型

  public Boolean getMobileCodeLogin() {
    return mobileCodeLogin;
  }

  public void setMobileCodeLogin(Boolean mobileCodeLogin) {
    this.mobileCodeLogin = mobileCodeLogin;
  }

  public String getMobileNum() {
    return mobileNum;
  }

  public void setMobileNum(String mobileNum) {
    this.mobileNum = mobileNum;
  }

  public String getMobileCode() {
    return mobileCode;
  }

  public void setMobileCode(String mobileCode) {
    this.mobileCode = mobileCode;
  }

  public String getSys() {
    return sys;
  }

  public void setSys(String sys) {
    this.sys = sys;
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

  public String getService() {
    return service;
  }

  public void setService(String service) {
    this.service = service;
  }

  public String getFailedMsg() {
    return failedMsg;
  }

  public void setFailedMsg(String failedMsg) {
    this.failedMsg = failedMsg;
  }

  public Integer getNeedSelectIns() {
    return needSelectIns;
  }

  public void setNeedSelectIns(Integer needSelectIns) {
    this.needSelectIns = needSelectIns;
  }

  public Integer getNeedSelectRole() {
    return needSelectRole;
  }

  public void setNeedSelectRole(Integer needSelectRole) {
    this.needSelectRole = needSelectRole;
  }

  public String getForwardUrl() {
    return forwardUrl;
  }

  public void setForwardUrl(String forwardUrl) {
    this.forwardUrl = forwardUrl;
  }

  public Long getPsnId() {
    return psnId;
  }

  public void setPsnId(Long psnId) {
    this.psnId = psnId;
  }

  public Long getRoleId() {
    return roleId;
  }

  public void setRoleId(Long roleId) {
    this.roleId = roleId;
  }

  public Long getInsId() {
    return insId;
  }

  public void setInsId(Long insId) {
    this.insId = insId;
  }

  public String getSysDomain() {
    return sysDomain;
  }

  public void setSysDomain(String sysDomain) {
    this.sysDomain = sysDomain;
  }

  public String getSYSSID() {
    return SYSSID;
  }

  public void setSYSSID(String sYSSID) {
    SYSSID = sYSSID;
  }

  public boolean getRememberMe() {
    return rememberMe;
  }

  public void setRememberMe(boolean rememberMe) {
    this.rememberMe = rememberMe;
  }

  public String getLocale() {
    return locale;
  }

  public void setLocale(String locale) {
    this.locale = locale;
  }

  public String getLoginType() {
    return loginType;
  }

  public void setLoginType(String loginType) {
    this.loginType = loginType;
  }

  public Integer getRandomNum() {
    return randomNum;
  }

  public void setRandomNum(Integer randomNum) {
    this.randomNum = randomNum;
  }

  public String getAID() {
    return AID;
  }

  public void setAID(String aID) {
    AID = aID;
  }

  public String getParams() {
    return params;
  }

  public void setParams(String params) {
    this.params = params;
  }

  public String getRunEnv() {
    return runEnv;
  }

  public void setRunEnv(String runEnv) {
    this.runEnv = runEnv;
  }

  public Integer getErrMsgPosition() {
    return errMsgPosition;
  }

  public void setErrMsgPosition(Integer errMsgPosition) {
    this.errMsgPosition = errMsgPosition;
  }

  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public Integer getBindType() {
    return bindType;
  }

  public void setBindType(Integer bindType) {
    this.bindType = bindType;
  }



}
