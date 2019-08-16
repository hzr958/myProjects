package com.smate.center.oauth.model.profile;

import java.io.Serializable;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;

import com.smate.core.base.utils.model.security.Person;
import com.smate.core.base.utils.string.ServiceUtil;

/**
 * 个人基本信息.
 * 
 * @author tsz
 * 
 */
public class PersonRegisterForm implements Serializable {

  private static final long serialVersionUID = -4511234749832281474L;
  // 用户编码
  private Long personId;
  // 加密的用户编码
  private String personDes3Id;
  private Long psnId;
  private String des3PsnId;
  // 用户名
  private String firstName;
  // 用户姓氏
  private String lastName;
  // 用户中文明
  private String name;
  // 用户另用名
  private String otherName;
  // 用户英文名
  private String ename;
  // 用户电话
  private String tel;
  // 用户移动电话号码
  private String mobile;
  private String zhfirstName;// 中文名
  private String zhlastName;// 中文姓
  // 用户电子邮箱
  private String email;
  // 用户msn帐号
  private String msnNo;
  // QQ号码
  private String qqNo;
  // skype账号
  private String skype;
  // 用户学位
  private String degree;
  // 所在地
  private Long regionId;
  // 出生日期yyyy/mm/dd，也可yyyy或 yyyy/mm
  private String birthday;
  // 头衔
  private String titolo;
  // 性别，1男，0女
  private Integer sex;
  // 头像地址
  private String avatars;
  // 信息完整度
  private Integer complete;
  // 用户注册时间
  private Date regData;
  // 职务
  private String position;
  private Long posId;
  private Integer posGrades;
  private Long insId;
  private String insName;
  // 是否邮件订阅关注动态
  private Integer dynEmail;
  // 个人网址
  private String http;
  // 标示是从导入用户或单位添加
  private Integer isAdd;
  // 判断是否修改个人简历，0未修改，1已修改
  private Integer isUpdated;
  // 个人教育经历默认值
  private String defaultAffiliation;
  // 个人邮件语言版本设置
  private String emailLanguageVersion;
  // 是否已登录过
  private Integer isLogin;
  // 人员推荐显示头衔：头衔->单位->国家地区
  private String viewTitolo;
  // 当前语言版本
  private String localLanguage;
  // 学历名称
  private String degreeName;
  private String showName;
  private String viewName;// 显示名称<方便整合各JSP页面和后台代码中对人员显示名称的处理代码>_MJG_SCM-4388.
  // 默认头像
  private String defaultAvators;
  // 个人简介
  private String brief;
  // 密码
  private String newpassword;
  // 标示是否注册个人账号.
  private boolean isRegisterR = false;
  // 如果该字段没有值则公开。如果有值则不公开
  private Integer isPrivate;
  // 人员邀请Id
  private String des3InviteId;
  // 邀请类型：好友邀请 or 群组邀请
  private String key;
  // 第三方判断(CXC_LOGIN:创新城)
  private String sysType;
  // 国家
  private String countryJson;
  // 目标跳转地址_2013-03-07_SCM-1894.
  private String forward;
  // 邀请码_2013-06-09_SCM-2443.
  private String invitCode;
  // SCM-11380注册后
  private String back; // 跳转url
  private String targetUrl; // 跳转url
  private String service; // 跳转url
  private String backURL;
  private String host; // sie系统首页域名，QQ登陆的时候要通过域名做判断。
  private String renewpassword;
  private String qqName; // qq昵称
  private String wechatName; // 微信昵称
  private String loginType; // 第三方登录注册时的类型
  // 国家区域id
  private String superRegionId;
  // 单位
  private String queryInstitution;
  // 排除字段
  private String excludes;
  private Person person;
  private String token;
  private String locale; // 语言
  private String aid;
  private String mobileReg = "0"; // 1=手机端注册 ；
  private Boolean needDynamicOpenId = false; // 需要返回动态openId
  private String mobileNumber = ""; // 手机号码
  private String mobileVerifyCode = ""; // 手机验证码
  private Long smsType = 1000L; // 消息类型
  private String zhfirstNameNoPhone;// 中文名
  private String zhlastNameNoPhone;// 中文姓
  private String firstNameNoPhone;
  private String lastNameNoPhone;
  private Integer tryCount = 1; // 重试的次数
  private boolean autoLogin = false; // 是否需要自动登录

  /**
   * @return
   */
  public Long getSmsType() {
    return smsType;
  }

  public void setSmsType(Long smsType) {
    this.smsType = smsType;
  }

  public String getMobileVerifyCode() {
    return mobileVerifyCode;
  }

  public void setMobileVerifyCode(String mobileVerifyCode) {
    this.mobileVerifyCode = mobileVerifyCode;
  }

  public String getMobileNumber() {
    return mobileNumber;
  }

  public void setMobileNumber(String mobileNumber) {
    this.mobileNumber = mobileNumber;
  }

  /**
   * 
   */
  public PersonRegisterForm() {

  }

  // 用户联系方式
  public PersonRegisterForm(Long personId, String tel, String mobile, String msnNo, String qqNo, String email) {
    super();
    this.personId = personId;
    this.tel = tel;
    this.mobile = mobile;
    this.msnNo = msnNo;
    this.qqNo = qqNo;
    this.email = email;
  }

  // 用户联系方式
  public PersonRegisterForm(String tel, String mobile, String msnNo, String qqNo, String email, String skype,
      String http) {
    super();
    this.tel = tel;
    this.mobile = mobile;
    this.msnNo = msnNo;
    this.qqNo = qqNo;
    this.email = email;
    this.skype = skype;
    this.http = http;
  }

  // 好友智能推荐
  public PersonRegisterForm(Long personId, String firstName, String lastName, String name, String titolo,
      String avatars, String email, String insName) {
    super();
    this.personId = personId;
    this.firstName = firstName;
    this.lastName = lastName;
    this.name = name;
    this.titolo = titolo;
    this.avatars = avatars;
    this.email = email;
    this.insName = insName;
  }

  public PersonRegisterForm(Long personId, String firstName, String lastName, String name, String titolo,
      String avatars, String email, String insName, String defaultAffiliation) {
    super();
    this.personId = personId;
    this.firstName = firstName;
    this.lastName = lastName;
    this.name = name;
    this.titolo = titolo;
    this.avatars = avatars;
    this.email = email;
    this.insName = insName;
    this.defaultAffiliation = defaultAffiliation;
  }

  // 用户头像
  public PersonRegisterForm(Long personId, String avatars, Integer sex) {
    super();
    this.personId = personId;
    this.avatars = avatars;
    this.sex = sex;
  }

  // 用户检索
  public PersonRegisterForm(Long personId, String name, String ename, Long regionId, String titolo, String avatars) {
    super();
    this.personId = personId;
    this.name = name;
    this.ename = ename;
    this.regionId = regionId;
    this.titolo = titolo;
    this.avatars = avatars;
  }

  public PersonRegisterForm(Long personId, String email, String name, String lastName, String firstName,
      String emailLanguageVersion, Integer sex) {
    this.personId = personId;
    this.email = email;
    this.name = name;
    this.lastName = lastName;
    this.firstName = firstName;
    this.emailLanguageVersion = emailLanguageVersion;
    this.sex = sex;
  }

  public PersonRegisterForm(Long personId, String name, String ename) {
    super();
    this.personId = personId;
    this.name = name;
    this.ename = ename;
  }

  public PersonRegisterForm(Long personId, String name, String ename, Integer isLogin) {
    super();
    this.personId = personId;
    this.name = name;
    this.ename = ename;
    this.isLogin = isLogin;
  }

  public PersonRegisterForm(Long personId, Integer complete, Integer isAdd, Integer isLogin) {
    super();
    this.personId = personId;
    this.complete = complete;
    this.isAdd = isAdd;
    this.isLogin = isLogin;
  }

  // 用于发送邮件 by zk
  public PersonRegisterForm(String email, Long personId, String name, String lastName, String firstName,
      String locale) {

    super();
    this.personId = personId;
    this.name = name;
    this.lastName = lastName;
    this.firstName = firstName;
    this.emailLanguageVersion = locale;
    this.email = email;

  }

  // 用于生成人员html
  public PersonRegisterForm(String name, String firstName, String lastName, String avatars, String email, String mobile,
      String tel, Long personId) {
    this.personId = personId;
    this.name = name;
    this.firstName = firstName;
    this.lastName = lastName;
    this.avatars = avatars;
    this.email = email;
    this.mobile = mobile;
    this.tel = tel;
  }

  // 获取用户名
  public PersonRegisterForm(Long personId, String firstName, String lastName, String name, String otherName) {
    super();
    this.personId = personId;
    this.firstName = firstName;
    this.lastName = lastName;
    this.name = name;
    this.otherName = otherName;
  }

  // 获取用户名
  public PersonRegisterForm(String firstName, String lastName, String name, String otherName, String ename,
      Long personId) {
    super();
    this.personId = personId;
    this.firstName = firstName;
    this.lastName = lastName;
    this.name = name;
    this.otherName = otherName;
    this.ename = ename;
  }

  // 获取用户职称
  public PersonRegisterForm(Long personId, String position, Long posId) {
    super();
    this.personId = personId;
    this.position = position;
    this.posId = posId;
  }

  // 获取用户首要单位
  public PersonRegisterForm(Long personId, Long insId, String insName) {
    super();
    this.personId = personId;
    this.insId = insId;
    this.insName = insName;
  }

  // 获取用户学位
  public PersonRegisterForm(Long personId, String degree, Long insId, String degreeName) {
    super();
    this.personId = personId;
    this.degree = degree;
    this.insId = insId;
    this.degreeName = degreeName;
  }

  public PersonRegisterForm(Long personId, String name, String firstName, String lastName, String avatars, Long insId,
      String insName, String email) {
    this.personId = personId;
    this.name = name;
    this.firstName = firstName;
    this.lastName = lastName;
    this.avatars = avatars;
    this.insId = insId;
    this.insName = insName;
    this.email = email;
  }

  public PersonRegisterForm(String name, String firstName, String lastName, String avatars, Long psnId) {
    this.name = name;
    this.firstName = firstName;
    this.lastName = lastName;
    this.avatars = avatars;
    this.personId = psnId;
  }

  public PersonRegisterForm(String name, String firstName, String lastName, Long psnId) {
    this.name = name;
    this.firstName = firstName;
    this.lastName = lastName;
    this.personId = psnId;
  }

  public PersonRegisterForm(Long personId, String email, String mobile, String tel) {
    this.personId = personId;
    this.email = email;
    this.mobile = mobile;
    this.tel = tel;
  }

  // 用户基本配置.
  public PersonRegisterForm(Long personId, Integer dynEmail, String emailLanguageVersion, String localLanguage) {
    super();
    this.personId = personId;
    this.dynEmail = dynEmail;
    this.emailLanguageVersion = emailLanguageVersion;
    this.localLanguage = localLanguage;
  }

  /**
   * 人员经验信息.
   * 
   * @param personId
   * @param degree
   * @param titolo
   * @param position
   * @param posId
   * @param posGrades
   * @param defaultAffiliation
   * @param degreeName
   */
  public PersonRegisterForm(Long personId, String degree, String titolo, String position, Long posId, Integer posGrades,
      String defaultAffiliation, String degreeName) {
    super();
    this.personId = personId;
    this.degree = degree;
    this.titolo = titolo;
    this.position = position;
    this.posId = posId;
    this.posGrades = posGrades;
    this.defaultAffiliation = defaultAffiliation;
    this.degreeName = degreeName;
  }

  /**
   * 获取人员辅助信息.
   * 
   * @param personId
   * @param regionId
   * @param birthday
   * @param sex
   * @param avatars
   * @param complete
   * @param regData
   * @param isAdd
   * @param isUpdated
   * @param isLogin
   */
  public PersonRegisterForm(Long personId, Long regionId, String birthday, Integer sex, String avatars,
      Integer complete, Date regData, Integer isAdd, Integer isUpdated, Integer isLogin) {
    super();
    this.personId = personId;
    this.regionId = regionId;
    this.birthday = birthday;
    this.sex = sex;
    this.avatars = avatars;
    this.complete = complete;
    this.regData = regData;
    this.isAdd = isAdd;
    this.isUpdated = isUpdated;
    this.isLogin = isLogin;
  }

  // 机构主页，阅览列表
  public PersonRegisterForm(Long personId, String firstName, String lastName, String name, Integer sex, String avatars,
      String defaultAffiliation) {
    super();
    this.personId = personId;
    this.firstName = firstName;
    this.lastName = lastName;
    this.name = name;
    this.sex = sex;
    this.avatars = avatars;
    this.defaultAffiliation = defaultAffiliation;
  }

  // 机构主页，阅览列表
  public PersonRegisterForm(Long personId, String firstName, String lastName, String name, String ename, Integer sex,
      String avatars, String defaultAffiliation) {
    super();
    this.personId = personId;
    this.firstName = firstName;
    this.lastName = lastName;
    this.name = name;
    this.ename = ename;
    this.sex = sex;
    this.avatars = avatars;
    this.defaultAffiliation = defaultAffiliation;
  }

  /**
   * @return personId
   */

  public Long getPersonId() {
    return personId;
  }

  /**
   * @param personId
   */
  public void setPersonId(Long personId) {
    this.personId = personId;
  }

  /**
   * @return firstName
   */
  public String getFirstName() {
    return firstName;
  }

  /**
   * @param firstName
   */
  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  /**
   * @return lastName
   */
  public String getLastName() {
    return lastName;
  }

  /**
   * @param lastName
   */
  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  /**
   * @return NAME
   */
  public String getName() {
    return name;
  }

  /**
   * @param name
   */
  public void setName(String name) {
    this.name = name;
  }

  /**
   * @return otherName
   */
  public String getOtherName() {
    return otherName;
  }

  /**
   * @param otherName
   */
  public void setOtherName(String otherName) {
    this.otherName = otherName;
  }

  /**
   * @return EN_NAME
   */

  public String getEname() {
    return ename;
  }

  /**
   * @param ename
   */
  public void setEname(String ename) {
    this.ename = ename;
  }

  /**
   * @return tel
   */
  public String getTel() {
    return tel;
  }

  /**
   * @param tel
   */
  public void setTel(String tel) {
    this.tel = tel;
  }

  /**
   * @return mobile
   */
  public String getMobile() {
    return mobile;
  }

  /**
   * @param mobile
   */
  public void setMobile(String mobile) {
    this.mobile = mobile;
  }

  /**
   * @return email
   */
  public String getEmail() {
    return StringUtils.lowerCase(email);
  }

  /**
   * @param email
   */
  public void setEmail(String email) {
    this.email = email;
  }

  /**
   * @return msnNo
   */
  public String getMsnNo() {
    return msnNo;
  }

  /**
   * @param msnNo
   */
  public void setMsnNo(String msnNo) {
    this.msnNo = msnNo;
  }

  /**
   * @return degree
   */
  public String getDegree() {
    return degree;
  }

  /**
   * @param degree
   */
  public void setDegree(String degree) {
    this.degree = degree;
  }

  // ljj
  public String getInsName() {
    return insName;
  }

  /**
   * @param insName the insName to set
   */
  public void setInsName(String insName) {
    this.insName = insName;
  }

  /**
   * @return regData
   */
  public Date getRegData() {
    return regData;
  }

  /**
   * @param regData
   */
  public void setRegData(Date regData) {
    this.regData = regData;
  }

  public String getQqNo() {
    return qqNo;
  }

  public String getSkype() {
    return skype;
  }

  public Long getRegionId() {
    return regionId;
  }

  public String getBirthday() {
    return birthday;
  }

  public String getTitolo() {
    return titolo;
  }

  public Integer getSex() {
    return sex;
  }

  public String getAvatars() {
    return avatars;
  }

  public Integer getComplete() {
    return complete;
  }

  public void setQqNo(String qqNo) {
    this.qqNo = qqNo;
  }

  public void setSkype(String skype) {
    this.skype = skype;
  }

  public void setRegionId(Long regionId) {
    this.regionId = regionId;
  }

  public void setBirthday(String birthday) {
    this.birthday = birthday;
  }

  public void setTitolo(String titolo) {
    this.titolo = titolo;
  }

  public void setSex(Integer sex) {
    this.sex = sex;
  }

  public void setAvatars(String avatars) {
    this.avatars = avatars;
  }

  public void setComplete(Integer complete) {
    this.complete = complete;
  }

  public String getPosition() {
    return position;
  }

  public Long getPosId() {
    return posId;
  }

  public Integer getPosGrades() {
    return posGrades;
  }

  public void setPosition(String position) {
    this.position = position;
  }

  public void setPosId(Long posId) {
    this.posId = posId;
  }

  public void setPosGrades(Integer posGrades) {
    this.posGrades = posGrades;
  }

  // ljj
  public Long getInsId() {
    return insId;
  }

  public void setInsId(Long insId) {
    this.insId = insId;
  }

  /**
   * @return the dynEmail
   */
  public Integer getDynEmail() {
    return dynEmail;
  }

  /**
   * @param dynEmail the dynEmail to set
   */
  public void setDynEmail(Integer dynEmail) {
    this.dynEmail = dynEmail;
  }

  public String getHttp() {
    return http;
  }

  public Integer getIsAdd() {
    return isAdd;
  }

  public Integer getIsUpdated() {
    return isUpdated;
  }

  public void setHttp(String http) {
    this.http = http;
  }

  public void setIsAdd(Integer isAdd) {
    this.isAdd = isAdd;
  }

  public void setIsUpdated(Integer isUpdated) {
    this.isUpdated = isUpdated;
  }

  public String getDefaultAffiliation() {
    return defaultAffiliation;
  }

  public void setDefaultAffiliation(String defaultAffiliation) {
    this.defaultAffiliation = defaultAffiliation;
  }

  public String getEmailLanguageVersion() {
    return emailLanguageVersion;
  }

  public void setEmailLanguageVersion(String emailLanguageVersion) {
    this.emailLanguageVersion = emailLanguageVersion;
  }

  public Integer getIsLogin() {
    return isLogin;
  }

  public void setIsLogin(Integer isLogin) {
    this.isLogin = isLogin;
  }

  public String getLocalLanguage() {
    return localLanguage;
  }

  public void setLocalLanguage(String localLanguage) {
    this.localLanguage = localLanguage;
  }

  public String getDegreeName() {
    return degreeName;
  }

  public void setDegreeName(String degreeName) {
    this.degreeName = degreeName;
  }

  public String getViewTitolo() {
    return viewTitolo;
  }

  public void setViewTitolo(String viewTitolo) {
    this.viewTitolo = viewTitolo;
  }

  public String getShowName() {
    return showName;
  }

  public void setShowName(String showName) {
    this.showName = showName;
  }

  public String getBrief() {
    return brief;
  }

  public void setBrief(String brief) {
    this.brief = brief;
  }

  public String getViewName() {
    return viewName;
  }

  public void setViewName(String viewName) {
    this.viewName = viewName;
  }

  public String getDefaultAvators() {
    return defaultAvators;
  }

  public void setDefaultAvators(String defaultAvators) {
    this.defaultAvators = defaultAvators;
  }

  public String getPersonDes3Id() {
    if (this.personId != null && personDes3Id == null) {
      personDes3Id = ServiceUtil.encodeToDes3(this.personId.toString());
    }
    return personDes3Id;
  }

  public void setPersonDes3Id(String personDes3Id) {
    this.personDes3Id = personDes3Id;
  }

  public String getNewpassword() {
    return newpassword;
  }

  public void setNewpassword(String newpassword) {
    this.newpassword = newpassword;
  }

  public boolean getIsRegisterR() {
    return isRegisterR;
  }

  public void setIsRegisterR(boolean isRegisterR) {
    this.isRegisterR = isRegisterR;
  }

  public Integer getIsPrivate() {
    if (isPrivate == null) {
      return 0;
    }
    return isPrivate;
  }

  public void setIsPrivate(Integer isPrivate) {
    this.isPrivate = isPrivate;
  }

  public String getDes3InviteId() {
    return des3InviteId;
  }

  public void setDes3InviteId(String des3InviteId) {
    this.des3InviteId = des3InviteId;
  }

  public String getKey() {
    return key;
  }

  public void setKey(String key) {
    this.key = key;
  }

  public String getSysType() {
    return sysType;
  }

  public void setSysType(String sysType) {
    this.sysType = sysType;
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

  public Long getPsnId() {
    return psnId;
  }

  public void setPsnId(Long psnId) {
    this.psnId = psnId;
  }

  public String getDes3PsnId() {
    return des3PsnId;
  }

  public void setDes3PsnId(String des3PsnId) {
    this.des3PsnId = des3PsnId;
  }

  public String getCountryJson() {
    return countryJson;
  }

  public void setCountryJson(String countryJson) {
    this.countryJson = countryJson;
  }

  public String getForward() {
    return forward;
  }

  public void setForward(String forward) {
    this.forward = forward;
  }

  public String getInvitCode() {
    return invitCode;
  }

  public void setInvitCode(String invitCode) {
    this.invitCode = invitCode;
  }

  public String getBack() {
    return back;
  }

  public void setBack(String back) {
    this.back = back;
  }

  public String getBackURL() {
    if (StringUtils.isBlank(backURL)) {
      backURL = back;
    }
    return backURL;
  }

  public void setBackURL(String backURL) {
    this.backURL = backURL;
  }

  public String getRenewpassword() {
    return renewpassword;
  }

  public void setRenewpassword(String renewpassword) {
    this.renewpassword = renewpassword;
  }

  public String getSuperRegionId() {
    return superRegionId;
  }

  public void setSuperRegionId(String superRegionId) {
    this.superRegionId = superRegionId;
  }

  public String getQueryInstitution() {
    return queryInstitution;
  }

  public void setQueryInstitution(String queryInstitution) {
    this.queryInstitution = queryInstitution;
  }

  public String getExcludes() {
    return excludes;
  }

  public void setExcludes(String excludes) {
    this.excludes = excludes;
  }

  public Person getPerson() {
    return person;
  }

  public void setPerson(Person person) {
    this.person = person;
  }

  public String getTargetUrl() {
    return targetUrl;
  }

  public void setTargetUrl(String targetUrl) {
    this.targetUrl = targetUrl;
  }

  public String getService() {
    return service;
  }

  public void setService(String service) {
    this.service = service;
  }

  public String getToken() {
    return token;
  }

  public void setToken(String token) {
    this.token = token;
  }

  public String getLocale() {
    return locale;
  }

  public void setLocale(String locale) {
    this.locale = locale;
  }

  public String getAid() {
    return aid;
  }

  public void setAid(String aid) {
    this.aid = aid;
  }

  public String getHost() {
    return host;
  }

  public void setHost(String host) {
    this.host = host;
  }

  public Boolean getNeedDynamicOpenId() {
    return needDynamicOpenId;
  }

  public void setNeedDynamicOpenId(Boolean needDynamicOpenId) {
    this.needDynamicOpenId = needDynamicOpenId;
  }

  public String getQqName() {
    return qqName;
  }

  public void setQqName(String qqName) {
    this.qqName = qqName;
  }

  public String getWechatName() {
    return wechatName;
  }

  public void setWechatName(String wechatName) {
    this.wechatName = wechatName;
  }

  public String getLoginType() {
    return loginType;
  }

  public void setLoginType(String loginType) {
    this.loginType = loginType;
  }

  public String getMobileReg() {
    return mobileReg;
  }

  public void setMobileReg(String mobileReg) {
    this.mobileReg = mobileReg;
  }

  public String getZhlastNameNoPhone() {
    return zhlastNameNoPhone;
  }

  public void setZhlastNameNoPhone(String zhlastNameNoPhone) {
    this.zhlastNameNoPhone = zhlastNameNoPhone;
  }

  public String getZhfirstNameNoPhone() {
    return zhfirstNameNoPhone;
  }

  public void setZhfirstNameNoPhone(String zhfirstNameNoPhone) {
    this.zhfirstNameNoPhone = zhfirstNameNoPhone;
  }

  public String getFirstNameNoPhone() {
    return firstNameNoPhone;
  }

  public void setFirstNameNoPhone(String firstNameNoPhone) {
    this.firstNameNoPhone = firstNameNoPhone;
  }

  public String getLastNameNoPhone() {
    return lastNameNoPhone;
  }

  public void setLastNameNoPhone(String lastNameNoPhone) {
    this.lastNameNoPhone = lastNameNoPhone;
  }

  public Integer getTryCount() {
    return tryCount;
  }

  public void setTryCount(Integer tryCount) {
    this.tryCount = tryCount;
  }

  public boolean getAutoLogin() {
    return autoLogin;
  }

  public void setAutoLogin(boolean autoLogin) {
    this.autoLogin = autoLogin;
  }

  @Override
  public String toString() {
    return "PersonRegisterForm [personId=" + personId + ", personDes3Id=" + personDes3Id + ", psnId=" + psnId
        + ", des3PsnId=" + des3PsnId + ", firstName=" + firstName + ", lastName=" + lastName + ", name=" + name
        + ", otherName=" + otherName + ", ename=" + ename + ", tel=" + tel + ", mobile=" + mobile + ", zhfirstName="
        + zhfirstName + ", zhlastName=" + zhlastName + ", email=" + email + ", msnNo=" + msnNo + ", qqNo=" + qqNo
        + ", skype=" + skype + ", degree=" + degree + ", regionId=" + regionId + ", birthday=" + birthday + ", titolo="
        + titolo + ", sex=" + sex + ", avatars=" + avatars + ", complete=" + complete + ", regData=" + regData
        + ", position=" + position + ", posId=" + posId + ", posGrades=" + posGrades + ", insId=" + insId + ", insName="
        + insName + ", dynEmail=" + dynEmail + ", http=" + http + ", isAdd=" + isAdd + ", isUpdated=" + isUpdated
        + ", defaultAffiliation=" + defaultAffiliation + ", emailLanguageVersion=" + emailLanguageVersion + ", isLogin="
        + isLogin + ", viewTitolo=" + viewTitolo + ", localLanguage=" + localLanguage + ", degreeName=" + degreeName
        + ", showName=" + showName + ", viewName=" + viewName + ", defaultAvators=" + defaultAvators + ", brief="
        + brief + ", newpassword=" + newpassword + ", isRegisterR=" + isRegisterR + ", isPrivate=" + isPrivate
        + ", des3InviteId=" + des3InviteId + ", key=" + key + ", sysType=" + sysType + ", countryJson=" + countryJson
        + ", forward=" + forward + ", invitCode=" + invitCode + ", back=" + back + ", targetUrl=" + targetUrl
        + ", service=" + service + ", backURL=" + backURL + ", host=" + host + ", renewpassword=" + renewpassword
        + ", qqName=" + qqName + ", wechatName=" + wechatName + ", loginType=" + loginType + ", superRegionId="
        + superRegionId + ", queryInstitution=" + queryInstitution + ", excludes=" + excludes + ", person=" + person
        + ", token=" + token + ", locale=" + locale + ", aid=" + aid + ", mobileReg=" + mobileReg
        + ", needDynamicOpenId=" + needDynamicOpenId + ", mobileNumber=" + mobileNumber + ", mobileVerifyCode="
        + mobileVerifyCode + ", smsType=" + smsType + ", zhfirstNameNoPhone=" + zhfirstNameNoPhone
        + ", zhlastNameNoPhone=" + zhlastNameNoPhone + ", firstNameNoPhone=" + firstNameNoPhone + ", lastNameNoPhone="
        + lastNameNoPhone + ", tryCount=" + tryCount + "]";
  }


}
