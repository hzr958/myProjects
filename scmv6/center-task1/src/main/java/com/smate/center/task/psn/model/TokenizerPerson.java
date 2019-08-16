package com.smate.center.task.psn.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang3.StringUtils;

import com.smate.core.base.utils.string.ServiceUtil;


/**
 * 个人基本信息_分词器.
 * 
 * @author tsz
 * 
 */
@Entity
@Table(name = "person_all_tmp_20160801")
public class TokenizerPerson implements Comparable<TokenizerPerson>, Serializable {

  private static final long serialVersionUID = -4941107699668257089L;
  // 用户编码
  private Long personId;
  // 加密的用户编码
  private String personDes3Id;
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
  // 邮件是否验证过
  private Boolean isEmailVerify = false;
  // 人员单位、部门、职称信息
  private String insInfo;
  // 用于建立索引检索单位
  private String zhInsName;
  private String enInsName;
  private String psnPubKeywords;

  /**
   * 
   */
  public TokenizerPerson() {

  }

  // 用户联系方式
  public TokenizerPerson(Long personId, String tel, String mobile, String msnNo, String qqNo, String email) {
    super();
    this.personId = personId;
    this.tel = tel;
    this.mobile = mobile;
    this.msnNo = msnNo;
    this.qqNo = qqNo;
    this.email = email;
  }

  // 用户联系方式
  public TokenizerPerson(String tel, String mobile, String msnNo, String qqNo, String email, String skype,
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
  public TokenizerPerson(Long personId, String firstName, String lastName, String name, String titolo, String avatars,
      String email, String insName) {
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

  public TokenizerPerson(Long personId, String firstName, String lastName, String name, String titolo, String avatars,
      String email, String insName, String defaultAffiliation) {
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
  public TokenizerPerson(Long personId, String avatars, Integer sex) {
    super();
    this.personId = personId;
    this.avatars = avatars;
    this.sex = sex;
  }

  // 用户检索
  public TokenizerPerson(Long personId, String name, String ename, Long regionId, String titolo, String avatars) {
    super();
    this.personId = personId;
    this.name = name;
    this.ename = ename;
    this.regionId = regionId;
    this.titolo = titolo;
    this.avatars = avatars;
  }

  public TokenizerPerson(Long personId, String email, String name, String lastName, String firstName,
      String emailLanguageVersion, Integer sex) {
    this.personId = personId;
    this.email = email;
    this.name = name;
    this.lastName = lastName;
    this.firstName = firstName;
    this.emailLanguageVersion = emailLanguageVersion;
    this.sex = sex;
  }

  public TokenizerPerson(Long personId, String name, String ename) {
    super();
    this.personId = personId;
    this.name = name;
    this.ename = ename;
  }

  public TokenizerPerson(Long personId, String name, String ename, Integer isLogin) {
    super();
    this.personId = personId;
    this.name = name;
    this.ename = ename;
    this.isLogin = isLogin;
  }

  public TokenizerPerson(Long personId, Integer complete, Integer isAdd, Integer isLogin) {
    super();
    this.personId = personId;
    this.complete = complete;
    this.isAdd = isAdd;
    this.isLogin = isLogin;
  }

  // 用于发送邮件 by zk
  public TokenizerPerson(String email, Long personId, String name, String lastName, String firstName, String locale) {

    super();
    this.personId = personId;
    this.name = name;
    this.lastName = lastName;
    this.firstName = firstName;
    this.emailLanguageVersion = locale;
    this.email = email;

  }

  // 用于生成人员html
  public TokenizerPerson(String name, String firstName, String lastName, String avatars, String email, String mobile,
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

  /**
   * 注册时用
   * 
   * @param name
   * @param firstName
   * @param lastName
   * @param avatars
   * @param email
   * @param mobile
   * @param tel
   * @param personId
   * @param regData
   */
  public TokenizerPerson(String name, String firstName, String lastName, String avatars, String email, String mobile,
      String tel, Long personId, Date regData) {
    this.personId = personId;
    this.name = name;
    this.firstName = firstName;
    this.lastName = lastName;
    this.avatars = avatars;
    this.email = email;
    this.mobile = mobile;
    this.tel = tel;
    this.regData = regData;
  }

  // 获取用户名
  public TokenizerPerson(Long personId, String firstName, String lastName, String name, String otherName) {
    super();
    this.personId = personId;
    this.firstName = firstName;
    this.lastName = lastName;
    this.name = name;
    this.otherName = otherName;
  }

  // 获取用户名
  public TokenizerPerson(String firstName, String lastName, String name, String otherName, String ename,
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
  public TokenizerPerson(Long personId, String position, Long posId) {
    super();
    this.personId = personId;
    this.position = position;
    this.posId = posId;
  }

  // 获取用户首要单位
  public TokenizerPerson(Long personId, Long insId, String insName) {
    super();
    this.personId = personId;
    this.insId = insId;
    this.insName = insName;
  }

  // 获取用户学位
  public TokenizerPerson(Long personId, String degree, Long insId, String degreeName) {
    super();
    this.personId = personId;
    this.degree = degree;
    this.insId = insId;
    this.degreeName = degreeName;
  }

  public TokenizerPerson(Long personId, String name, String firstName, String lastName, String avatars, Long insId,
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

  public TokenizerPerson(Long personId, String name, String firstName, String lastName, String avatars, String insName,
      String position) {
    this.personId = personId;
    this.name = name;
    this.firstName = firstName;
    this.lastName = lastName;
    this.avatars = avatars;
    this.insName = insName;
    this.position = position;
  }

  public TokenizerPerson(Long personId, String name, String firstName, String lastName, String avatars, String insName,
      String position, Long insId, Long posId) {
    this.personId = personId;
    this.name = name;
    this.firstName = firstName;
    this.lastName = lastName;
    this.avatars = avatars;
    this.insId = insId;
    this.insName = insName;
    this.posId = posId;
    this.position = position;
  }

  public TokenizerPerson(String name, String firstName, String lastName, String avatars, Long psnId) {
    this.name = name;
    this.firstName = firstName;
    this.lastName = lastName;
    this.avatars = avatars;
    this.personId = psnId;
  }

  public TokenizerPerson(String name, String firstName, String lastName, Long psnId) {
    this.name = name;
    this.firstName = firstName;
    this.lastName = lastName;
    this.personId = psnId;
  }

  public TokenizerPerson(Long personId, String email, String mobile, String tel) {
    this.personId = personId;
    this.email = email;
    this.mobile = mobile;
    this.tel = tel;
  }

  // 用户基本配置.
  public TokenizerPerson(Long personId, Integer dynEmail, String emailLanguageVersion, String localLanguage) {
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
  public TokenizerPerson(Long personId, String degree, String titolo, String position, Long posId, Integer posGrades,
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
  public TokenizerPerson(Long personId, Long regionId, String birthday, Integer sex, String avatars, Integer complete,
      Date regData, Integer isAdd, Integer isUpdated, Integer isLogin) {
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
  public TokenizerPerson(Long personId, String firstName, String lastName, String name, Integer sex, String avatars,
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
  public TokenizerPerson(Long personId, String firstName, String lastName, String name, String ename, Integer sex,
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

  public TokenizerPerson(String name, String firstName, String lastName, String avatars, String email, String mobile,
      String tel, Long personId, String position, String insName) {
    super();
    this.name = name;
    this.firstName = firstName;
    this.lastName = lastName;
    this.avatars = avatars;
    this.email = email;
    this.mobile = mobile;
    this.tel = mobile;
    this.personId = personId;
    this.position = position;
    this.insName = insName;
  }

  /**
   * 用于sns菜单
   * 
   * @param name
   * @param firstName
   * @param lastName
   * @param personId
   * @param localLanguage
   */
  public TokenizerPerson(String name, String firstName, String lastName, Long personId, String localLanguage) {
    super();
    this.name = name;
    this.firstName = firstName;
    this.lastName = lastName;
    this.personId = personId;
    this.localLanguage = localLanguage;
  }

  /**
   * 用于sns菜单
   * 
   * @param name
   * @param firstName
   * @param lastName
   * @param personId
   * @param localLanguage
   */
  public TokenizerPerson(String name, String firstName, String lastName, Long personId, String localLanguage,
      String avatars) {
    super();
    this.name = name;
    this.firstName = firstName;
    this.lastName = lastName;
    this.personId = personId;
    this.localLanguage = localLanguage;
    this.avatars = avatars;
  }

  /**
   * @return personId
   */
  @Id
  @Column(name = "PSN_ID")
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
  @Column(name = "FIRST_NAME")
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
  @Column(name = "LAST_NAME")
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
  @Column(name = "NAME")
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
  @Column(name = "OTHER_NAME")
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

  @Column(name = "ENAME")
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
  @Column(name = "TEL")
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
  @Column(name = "MOBILE")
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
  @Column(name = "EMAIL")
  public String getEmail() {
    return email;
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
  @Column(name = "MSN_NO")
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
  @Column(name = "DEGREE")
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
  @Column(name = "INS_NAME")
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
  @Column(name = "REG_DATE")
  public Date getRegData() {
    return regData;
  }

  /**
   * @param regData
   */
  public void setRegData(Date regData) {
    this.regData = regData;
  }

  @Column(name = "QQ_NO")
  public String getQqNo() {
    return qqNo;
  }

  @Column(name = "SKYPE")
  public String getSkype() {
    return skype;
  }

  @Column(name = "REGION_ID")
  public Long getRegionId() {
    return regionId;
  }

  @Column(name = "BIRTHDAY")
  public String getBirthday() {
    return birthday;
  }

  @Column(name = "TITOLO")
  public String getTitolo() {
    return titolo;
  }

  @Column(name = "SEX")
  public Integer getSex() {
    return sex;
  }

  @Column(name = "AVATARS")
  public String getAvatars() {
    return avatars;
  }


  @Column(name = "COMPLETE")
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

  @Column(name = "POSITION")
  public String getPosition() {
    return position;
  }

  @Column(name = "POS_ID")
  public Long getPosId() {
    return posId;
  }

  @Column(name = "POS_GRADES")
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

  // ljj @Transient
  @Column(name = "INS_ID")
  public Long getInsId() {
    return insId;
  }

  public void setInsId(Long insId) {
    this.insId = insId;
  }

  /**
   * @return the dynEmail
   */
  @Column(name = "DYN_EMAIL")
  public Integer getDynEmail() {
    return dynEmail;
  }

  /**
   * @param dynEmail the dynEmail to set
   */
  public void setDynEmail(Integer dynEmail) {
    this.dynEmail = dynEmail;
  }

  @Column(name = "HTTP")
  public String getHttp() {
    return http;
  }

  @Column(name = "IS_ADD")
  public Integer getIsAdd() {
    return isAdd;
  }

  @Column(name = "IS_UPDATED")
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

  @Column(name = "AFFILIATION")
  public String getDefaultAffiliation() {
    return defaultAffiliation;
  }

  public void setDefaultAffiliation(String defaultAffiliation) {
    this.defaultAffiliation = defaultAffiliation;
  }

  @Column(name = "EMAIL_LANGUAGE_VERSION")
  public String getEmailLanguageVersion() {
    return emailLanguageVersion;
  }

  public void setEmailLanguageVersion(String emailLanguageVersion) {
    this.emailLanguageVersion = emailLanguageVersion;
  }

  @Column(name = "IS_LOGIN")
  public Integer getIsLogin() {
    return isLogin;
  }

  public void setIsLogin(Integer isLogin) {
    this.isLogin = isLogin;
  }

  @Column(name = "LOCAL_LANGUAGE")
  public String getLocalLanguage() {
    return localLanguage;
  }

  public void setLocalLanguage(String localLanguage) {
    this.localLanguage = localLanguage;
  }

  @Column(name = "DEGREE_NAME")
  public String getDegreeName() {
    return degreeName;
  }

  public void setDegreeName(String degreeName) {
    this.degreeName = degreeName;
  }

  @Transient
  public String getViewTitolo() {
    return viewTitolo;
  }

  public void setViewTitolo(String viewTitolo) {
    this.viewTitolo = viewTitolo;
  }

  @Transient
  public String getShowName() {
    return showName;
  }

  public void setShowName(String showName) {
    this.showName = showName;
  }

  @Transient
  public String getBrief() {
    return brief;
  }

  public void setBrief(String brief) {
    this.brief = brief;
  }

  @Transient
  public String getViewName() {
    return viewName;
  }

  public void setViewName(String viewName) {
    this.viewName = viewName;
  }


  @Transient
  public String getDefaultAvators() {
    return defaultAvators;
  }

  public void setDefaultAvators(String defaultAvators) {
    this.defaultAvators = defaultAvators;
  }

  @Transient
  public String getPersonDes3Id() {
    if (this.personId != null && personDes3Id == null) {
      personDes3Id = ServiceUtil.encodeToDes3(this.personId.toString());
    }
    return personDes3Id;
  }

  public void setPersonDes3Id(String personDes3Id) {
    this.personDes3Id = personDes3Id;
  }

  @Transient
  public String getNewpassword() {
    return newpassword;
  }

  public void setNewpassword(String newpassword) {
    this.newpassword = newpassword;
  }

  @Transient
  public boolean getIsRegisterR() {
    return isRegisterR;
  }

  public void setIsRegisterR(boolean isRegisterR) {
    this.isRegisterR = isRegisterR;
  }

  @Transient
  public Integer getIsPrivate() {
    if (isPrivate == null)
      return 0;
    return isPrivate;
  }

  public void setIsPrivate(Integer isPrivate) {
    this.isPrivate = isPrivate;
  }

  @Transient
  public String getDes3InviteId() {
    return des3InviteId;
  }

  public void setDes3InviteId(String des3InviteId) {
    this.des3InviteId = des3InviteId;
  }

  @Transient
  public String getKey() {
    return key;
  }

  public void setKey(String key) {
    this.key = key;
  }

  @Transient
  public Boolean getIsEmailVerify() {
    return isEmailVerify;
  }

  public void setIsEmailVerify(Boolean isEmailVerify) {
    this.isEmailVerify = isEmailVerify;
  }

  @Transient
  public String getInsInfo() {
    return insInfo;
  }

  public void setInsInfo(String insInfo) {
    this.insInfo = insInfo;
  }

  /**
   * 比较人员姓名
   */
  @Override
  public int compareTo(TokenizerPerson o) {
    String thisName = StringUtils.isNotBlank(this.name) ? this.name : this.lastName;
    String oName = StringUtils.isNoneBlank(o.getName()) ? o.getName() : o.getLastName();
    // 将名字都转成大写英文字符串，如果有中文则先将中文转换成汉语拼音
    thisName = ServiceUtil.parseNameToUpperEn(thisName);
    oName = ServiceUtil.parseNameToUpperEn(oName);
    if (StringUtils.isNotBlank(thisName)) {
      return thisName.compareTo(oName);
    } else if (StringUtils.isNotBlank(oName)) {
      return -1;
    }

    return 0;
  }

  @Transient
  public String getZhInsName() {
    return zhInsName;
  }

  public void setZhInsName(String zhInsName) {
    this.zhInsName = zhInsName;
  }

  @Transient
  public String getEnInsName() {
    return enInsName;
  }

  public void setEnInsName(String enInsName) {
    this.enInsName = enInsName;
  }

  @Transient
  public String getPsnPubKeywords() {
    return psnPubKeywords;
  }

  public void setPsnPubKeywords(String psnPubKeywords) {
    this.psnPubKeywords = psnPubKeywords;
  }
}
