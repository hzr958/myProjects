package com.smate.center.task.model.sns.psn;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.apache.commons.lang3.StringUtils;

@Entity
@Table(name = "psn_tmp_zll_20170801")
public class PsnTmp implements Serializable {
  private static final long serialVersionUID = -6972337393433037888L;
  // 用户编码
  private Long personId;
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
  // 当前语言版本
  private String localLanguage;
  // 学历名称
  private String degreeName;
  // 人员的部门信息
  private String department;
  private String zhFirstName;// 中文名
  private String zhLastName;// 中文姓

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
