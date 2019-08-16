package com.smate.center.oauth.model.interconnection;

import java.io.Serializable;

public class AccountInterconnectionForm implements Serializable {

  private static final long serialVersionUID = 1L;

  private String fromSys;// 第三方系统名称
  private PsnInfo psnInfo;// 快速登录显示的用户信息
  private String email;// 邮箱
  private String password;// 密码
  private String rePassword;// 确认密码
  private String userName;// 用户名称
  private String eName;// 英文名字
  private String firstName;// 英文名
  private String lastName;// 英文姓
  private String zhfirstName;// 中文名
  private String zhlastName;// 中文姓
  private String insName; // 机构名称
  private String unitName; // 单位名称
  private Long openId;// 人员openId
  private String sex;// 性别
  private String birthday;// 生日
  private String degree;// 学位
  private boolean effectiveUrl = false;// 请求地址是否有效
  private String token;
  private String validateCode;// 验证码
  private boolean needValidateCode;// 是否需要验证
  private Integer loginStatus;// 登录状态
  private String msg;// 登录信息
  private String position; // 职称
  private Integer insId; // 机构ID
  private Integer unitId; // 单位ID
  // 标示是否注册个人账号.
  private boolean isRegisterR = false;
  // 部门，同unit
  private String department;
  private boolean isHttps = true; // 是否是https

  public String getFromSys() {
    return fromSys;
  }

  public void setFromSys(String fromSys) {
    this.fromSys = fromSys;
  }

  public PsnInfo getPsnInfo() {
    return psnInfo;
  }

  public void setPsnInfo(PsnInfo psnInfo) {
    this.psnInfo = psnInfo;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public String getRePassword() {
    return rePassword;
  }

  public void setRePassword(String rePassword) {
    this.rePassword = rePassword;
  }

  public String getUserName() {
    return userName;
  }

  public void setUserName(String userName) {
    this.userName = userName;
  }

  public String getUnitName() {
    return unitName;
  }

  public void setUnitName(String unitName) {
    this.unitName = unitName;
  }

  public Long getOpenId() {
    return openId;
  }

  public void setOpenId(Long openId) {
    this.openId = openId;
  }

  public String getSex() {
    return sex;
  }

  public void setSex(String sex) {
    this.sex = sex;
  }

  public boolean isEffectiveUrl() {
    return effectiveUrl;
  }

  public void setEffectiveUrl(boolean effectiveUrl) {
    this.effectiveUrl = effectiveUrl;
  }

  public String getToken() {
    return token;
  }

  public void setToken(String token) {
    this.token = token;
  }

  public String getValidateCode() {
    return validateCode;
  }

  public void setValidateCode(String validateCode) {
    this.validateCode = validateCode;
  }

  public boolean isNeedValidateCode() {
    return needValidateCode;
  }

  public void setNeedValidateCode(boolean needValidateCode) {
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

  public String getInsName() {
    return insName;
  }

  public void setInsName(String insName) {
    this.insName = insName;
  }

  public String getPosition() {
    return position;
  }

  public void setPosition(String position) {
    this.position = position;
  }

  public Integer getInsId() {
    return insId;
  }

  public void setInsId(Integer insId) {
    this.insId = insId;
  }

  public Integer getUnitId() {
    return unitId;
  }

  public void setUnitId(Integer unitId) {
    this.unitId = unitId;
  }

  public boolean getIsRegisterR() {
    return isRegisterR;
  }

  public void setIsRegisterR(boolean isRegisterR) {
    this.isRegisterR = isRegisterR;
  }

  public String getDepartment() {
    return department;
  }

  public void setDepartment(String department) {
    this.department = department;
  }

  public String getBirthday() {
    return birthday;
  }

  public void setBirthday(String birthday) {
    this.birthday = birthday;
  }

  public String getDegree() {
    return degree;
  }

  public void setDegree(String degree) {
    this.degree = degree;
  }

  public boolean getIsHttps() {
    return isHttps;
  }

  public void setIsHttps(boolean isHttps) {
    this.isHttps = isHttps;
  }

  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public String geteName() {
    return eName;
  }

  public void seteName(String eName) {
    this.eName = eName;
  }

  public String getZhfirstName() {
    return zhfirstName;
  }

  public void setZhfirstName(String zhfirstName) {
    this.zhfirstName = zhfirstName;
  }

  public String getZhlastName() {
    return zhlastName;
  }

  public void setZhlastName(String zhlastName) {
    this.zhlastName = zhlastName;
  }

}
