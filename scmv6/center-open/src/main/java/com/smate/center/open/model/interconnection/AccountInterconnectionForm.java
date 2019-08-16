package com.smate.center.open.model.interconnection;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class AccountInterconnectionForm implements Serializable {

  private static final long serialVersionUID = 1L;

  private String fromSys;// 第三方系统标识
  private PsnInfo psnInfo;// 快速登录显示的用户信息
  private List<PsnInfo> psnInfoList = new ArrayList<PsnInfo>();// 快速登录显示的用户信息
  private String des3PsnId; // 加密的psnId
  private String email;// 邮箱
  private String password;// 密码
  private String rePassword;// 确认密码
  private String userName;// 用户名称
  private String insName; // 机构名称
  private String unitName; // 单位名称
  private Long openId;// 人员openId
  private String sex;// 性别
  private boolean effectiveUrl = false;// 请求地址是否有效
  private String token; // 进账号关联页面时接收缓存的key用
  private String validateCode;// 验证码
  private boolean needValidateCode;// 是否需要验证
  private Integer loginStatus;// 登录状态
  private String msg;// 登录信息
  private String position; // 职称
  private Integer insId; // 机构ID
  private Integer unitId; // 单位ID
  private String department; // 部门
  private boolean emailIsExist; // 第三方系统用户邮箱是否已存在于系统中
  private String signature;// 关联签名
  private String country; // 国别
  private String province; // 省份
  private String city; // 市
  private String degree; // 学位
  private String disciplineCode; // 学科代码
  private String researchArea; // 研究方向（xxx,xxx,xxx的形式）
  private String birthdate; // 出生日期 （年,月,日的形式）
  private String firstName;// 姓
  private String lastName;// 名
  private String sysName;// 第三方系统名称
  private boolean effectiveToken = false; // 传过来的第三方系统标识是否有效（在V_OPEN_THIRD_REG表中是否能找到对应的记录）
  private boolean relationExist = false; // 关联关系是否已存在（v_open_user_union中是否有对应记录）
  private String crossUrl;// 业务系统的域名
  private String keyWordsZh;// 中文关键字
  private String keyWordsEn;// 英文关键字
  private boolean isHttps = true; // 是否是https协议
  private String type = ""; // 请求登录的页面类型，simple--简单登录页面(只有登录、注册框)
  private String demoOpenId = ""; // demo 传过来的openId

  private String cancleV = "yes"; // 是否显示暂不关联。默认显示

  public String getDemoOpenId() {
    return demoOpenId;
  }

  public void setDemoOpenId(String demoOpenId) {
    this.demoOpenId = demoOpenId;
  }

  public String getSignature() {
    return signature;
  }

  public void setSignature(String signature) {
    this.signature = signature;
  }

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

  public String getDes3PsnId() {
    return des3PsnId;
  }

  public void setDes3PsnId(String des3PsnId) {
    this.des3PsnId = des3PsnId;
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

  public String getDepartment() {
    return department;
  }

  public void setDepartment(String department) {
    this.department = department;
  }

  public boolean isEmailIsExist() {
    return emailIsExist;
  }

  public void setEmailIsExist(boolean emailIsExist) {
    this.emailIsExist = emailIsExist;
  }

  public String getCountry() {
    return country;
  }

  public void setCountry(String country) {
    this.country = country;
  }

  public String getProvince() {
    return province;
  }

  public void setProvince(String province) {
    this.province = province;
  }

  public String getCity() {
    return city;
  }

  public void setCity(String city) {
    this.city = city;
  }

  public String getDegree() {
    return degree;
  }

  public void setDegree(String degree) {
    this.degree = degree;
  }

  public String getDisciplineCode() {
    return disciplineCode;
  }

  public void setDisciplineCode(String disciplineCode) {
    this.disciplineCode = disciplineCode;
  }

  public String getResearchArea() {
    return researchArea;
  }

  public void setResearchArea(String researchArea) {
    this.researchArea = researchArea;
  }

  public String getBirthdate() {
    return birthdate;
  }

  public void setBirthdate(String birthdate) {
    this.birthdate = birthdate;
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

  public String getSysName() {
    return sysName;
  }

  public void setSysName(String sysName) {
    this.sysName = sysName;
  }

  public boolean getEffectiveToken() {
    return effectiveToken;
  }

  public void setEffectiveToken(boolean effectiveToken) {
    this.effectiveToken = effectiveToken;
  }

  public boolean getRelationExist() {
    return relationExist;
  }

  public void setRelationExist(boolean relationExist) {
    this.relationExist = relationExist;
  }

  public String getCrossUrl() {
    return crossUrl;
  }

  public void setCrossUrl(String crossUrl) {
    this.crossUrl = crossUrl;
  }

  public String getKeyWordsZh() {
    return keyWordsZh;
  }

  public void setKeyWordsZh(String keyWordsZh) {
    this.keyWordsZh = keyWordsZh;
  }

  public String getKeyWordsEn() {
    return keyWordsEn;
  }

  public void setKeyWordsEn(String keyWordsEn) {
    this.keyWordsEn = keyWordsEn;
  }

  public boolean getIsHttps() {
    return isHttps;
  }

  public void setIsHttps(boolean isHttps) {
    this.isHttps = isHttps;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public List<PsnInfo> getPsnInfoList() {
    return psnInfoList;
  }

  public void setPsnInfoList(List<PsnInfo> psnInfoList) {
    this.psnInfoList = psnInfoList;
  }

  public String getCancleV() {
    return cancleV;
  }

  public void setCancleV(String cancleV) {
    this.cancleV = cancleV;
  }


}
