package com.smate.center.merge.model.sns.person;

import org.apache.commons.lang3.StringUtils;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

/**
 * 个人基本信息.
 * 
 * @author tsz
 * 
 */
@Entity
@Table(name = "PERSON")
public class Person implements Serializable {
  private static final long serialVersionUID = -4511234749832281474L;
  // 用户编码
  private Long personId;
  // 用户名
  private String firstName;
  // 用户姓氏
  private String lastName;
  // 用户中文名
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
  // 当前语言版本
  private String localLanguage;
  // 学历名称
  private String degreeName;
  // 人员的部门信息
  private String department;
  private String zhFirstName;// 中文名
  private String zhLastName;// 中文姓

  public Person() {
    super();
  }

  /**
   * 函数. 2018年9月11日
   */
  public Person(Long personId, String firstName, String lastName, String name, String otherName, String ename,
      String tel, String mobile, String email, String msnNo, String qqNo, String skype, String degree, Long regionId,
      String birthday, String titolo, Integer sex, String avatars, Integer complete, Date regData, String position,
      Long posId, Integer posGrades, Long insId, String insName, Integer dynEmail, String http, Integer isAdd,
      Integer isUpdated, String defaultAffiliation, String emailLanguageVersion, Integer isLogin, String localLanguage,
      String degreeName, String department, String zhFirstName, String zhLastName) {
    super();
    this.personId = personId;
    this.firstName = firstName;
    this.lastName = lastName;
    this.name = name;
    this.otherName = otherName;
    this.ename = ename;
    this.tel = tel;
    this.mobile = mobile;
    this.email = email;
    this.msnNo = msnNo;
    this.qqNo = qqNo;
    this.skype = skype;
    this.degree = degree;
    this.regionId = regionId;
    this.birthday = birthday;
    this.titolo = titolo;
    this.sex = sex;
    this.avatars = avatars;
    this.complete = complete;
    this.regData = regData;
    this.position = position;
    this.posId = posId;
    this.posGrades = posGrades;
    this.insId = insId;
    this.insName = insName;
    this.dynEmail = dynEmail;
    this.http = http;
    this.isAdd = isAdd;
    this.isUpdated = isUpdated;
    this.defaultAffiliation = defaultAffiliation;
    this.emailLanguageVersion = emailLanguageVersion;
    this.isLogin = isLogin;
    this.localLanguage = localLanguage;
    this.degreeName = degreeName;
    this.department = department;
    this.zhFirstName = zhFirstName;
    this.zhLastName = zhLastName;
  }

  @Id
  @Column(name = "PSN_ID")
  public Long getPersonId() {
    return personId;
  }

  public void setPersonId(Long personId) {
    this.personId = personId;
  }

  @Column(name = "FIRST_NAME")
  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  @Column(name = "LAST_NAME")
  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  @Column(name = "NAME")
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  @Column(name = "OTHER_NAME")
  public String getOtherName() {
    return otherName;
  }

  public void setOtherName(String otherName) {
    this.otherName = otherName;
  }

  @Column(name = "ENAME")
  public String getEname() {
    return ename;
  }

  public void setEname(String ename) {
    this.ename = ename;
  }

  @Column(name = "TEL")
  public String getTel() {
    return tel;
  }

  public void setTel(String tel) {
    this.tel = tel;
  }

  @Column(name = "MOBILE")
  public String getMobile() {
    return mobile;
  }

  public void setMobile(String mobile) {
    this.mobile = mobile;
  }

  @Column(name = "EMAIL")
  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  @Column(name = "MSN_NO")
  public String getMsnNo() {
    return msnNo;
  }

  public void setMsnNo(String msnNo) {
    this.msnNo = msnNo;
  }

  @Column(name = "DEGREE")
  public String getDegree() {
    return degree;
  }

  public void setDegree(String degree) {
    this.degree = degree;
  }

  // ljj
  @Column(name = "INS_NAME")
  public String getInsName() {
    return insName;
  }

  public void setInsName(String insName) {
    this.insName = insName;
  }

  @Column(name = "REG_DATE")
  public Date getRegData() {
    return regData;
  }

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

  /**
   * 头像.
   * 
   * @return
   */
  @Column(name = "AVATARS")
  public String getAvatars() {
    if (StringUtils.isNotBlank(avatars) && avatars.contains("/avatars/head_nan_photo.jpg")) {
      avatars = avatars.replace("/avatars/head_nan_photo.jpg", "/resmod/smate-pc/img/logo_psndefault.png");
    } else if (StringUtils.isBlank(avatars)) {
      avatars = "/resmod/smate-pc/img/logo_psndefault.png";
    }
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

  @Column(name = "DYN_EMAIL")
  public Integer getDynEmail() {
    return dynEmail;
  }

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

  @Column(name = "DEPARTMENT")
  public String getDepartment() {
    return department;
  }

  public void setDepartment(String department) {
    this.department = department;
  }

  @Column(name = "ZH_FIRSTNAME")
  public String getZhFirstName() {
    return zhFirstName;
  }

  public void setZhFirstName(String zhFirstName) {
    this.zhFirstName = zhFirstName;
  }

  @Column(name = "ZH_LASTNAME")
  public String getZhLastName() {
    return zhLastName;
  }

  public void setZhLastName(String zhLastName) {
    this.zhLastName = zhLastName;
  }
}
