package com.smate.center.open.model.register;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import javax.persistence.*;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

/**
 * 个人基本信息.
 * 
 * @author tsz
 * 
 */
@Entity
@Table(name = "PERSON")
public class PersonRegister implements Serializable {
  /**
   * 
   */
  private static final long serialVersionUID = -4931887103888624540L;
  // 用户编码
  private Long personId;
  // 用户名
  private String firstName;
  // 用户名
  private String zhFirstName;
  // 用户姓氏
  private String lastName;
  private String zhLastName;
  // 用户中文
  private String name;
  // 用户英文名(firstName + " " + lastName)
  private String ename;
  // 用户别名
  private String otherName;
  // 用户电子邮箱
  private String email;
  // ****** 互联互动
  private String birthday;
  // 用户所属地区
  private Long regionId;
  // 用户注册时间
  private Date regData;
  // 头像地址
  private String avatars;
  // 注册单位
  private String insName;
  // 目前身份
  private String positionType;
  // 用户密码
  private String newpassword;
  /** 以下为单位注册所需参数. */
  // 单位(管理员)电话
  private String tel;
  // 移动电话
  private String mobile;
  // 单位地址
  private String address;
  // 单位网址
  private String http;
  // 单位中文名
  private String insCname;
  // 单位英文名
  private String insEname;
  /** 以下是R在Rol注册所需要参数. */
  // 部门名
  private String unit;
  // 部门Id
  private Long unitId;
  // 单位ID
  private Long insId;
  /** Rol人员管理-编辑-所需要参数. */
  // 用户登陆名
  private String loginName;
  // 用户职称
  private String title;
  // 头衔
  private String titolo;
  // 用户职务
  private String position;
  // 职务Id
  private Long posId;
  // 职务等级
  private Integer posGrades;
  // 工作年份
  private String fromYear;
  // 工作月份
  private String fromMonth;
  // 在读起始年份
  private String studyFromYear;
  // 在读起始月份
  private String studyFromMonth;
  // 在读结束年份
  private String studyToYear;
  // 在读结束月份
  private String studyToMonth;
  // 所在国家JSON
  private String countryJson;
  // 所有省JSON串
  private String provinceJson;
  // 省编码
  private Long provinceId;
  // 市编码
  private Long cityId;
  private Long disId;
  private Integer sex;
  private Map<Long, String> titleTechList;
  private Map<Long, String> degreeList;
  private String study;
  private String degree;
  private String colleageName;
  private Long colleageId;
  // 人员邀请Id
  private String des3InviteId;
  private String des3PsnId;
  private String des3NodeId;
  private String key;
  private String guid;
  private Date lastLogin;
  private Long pubTotal;
  private String workInsName;
  // 所在市Json串
  private String cityJson;
  /** 标示是否注册个人账号. **/
  private boolean isRegisterR = false;
  /** Rol添加人员. */
  private int isRolAddPsn;
  private String emailLanguageVersion;// 个人邮件接收的语言版本，注册时默认为系统当前的语言帮版本
  private String renewpassword;
  // 学历名称
  private String degreeName;
  // 是否需要对密码加密，针对同步注册时，传入的密码已经是加密过的情况
  private Boolean isPwdEncrypt = true;
  // 邮件是否验证过
  private Boolean isEmailVerify = false;
  // 申请人pcode
  private String pcode;
  // 申请人单位性质
  private String orgnature;
  // 是否是手机端的注册
  private String mobileReg = "0";
  // 来源于哪个系统
  private String fromSys;

  private String department;

  private Boolean mobileLogin = true ; //手机号登录注册标志

  @Transient
  public Boolean getMobileLogin() {
    return mobileLogin;
  }

  public void setMobileLogin(Boolean mobileLogin) {
    this.mobileLogin = mobileLogin;
  }

  /**
   * @return PSN_ID
   */
  @Id
  @Column(name = "PSN_ID")
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_PERSON", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
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
   * @return the avatars
   */
  @Column(name = "AVATARS")
  public String getAvatars() {
    return avatars;
  }

  /**
   * @param avatars the avatars to set
   */
  public void setAvatars(String avatars) {
    this.avatars = avatars;
  }

  /**
   * @return the studyFromYear
   */
  @Transient
  public String getStudyFromYear() {
    return studyFromYear;
  }

  /**
   * @param studyFromYear the studyFromYear to set
   */
  public void setStudyFromYear(String studyFromYear) {
    this.studyFromYear = studyFromYear;
  }

  /**
   * @return the studyFromMonth
   */
  @Transient
  public String getStudyFromMonth() {
    return studyFromMonth;
  }

  /**
   * @param studyFromMonth the studyFromMonth to set
   */
  public void setStudyFromMonth(String studyFromMonth) {
    this.studyFromMonth = studyFromMonth;
  }

  /**
   * @return the studyToMonth
   */
  @Transient
  public String getStudyToMonth() {
    return studyToMonth;
  }

  /**
   * @return the studyToYear
   */
  @Transient
  public String getStudyToYear() {
    return studyToYear;
  }

  /**
   * @param studyToYear the studyToYear to set
   */
  public void setStudyToYear(String studyToYear) {
    this.studyToYear = studyToYear;
  }

  /**
   * @param studyToMonth the studyToMonth to set
   */
  public void setStudyToMonth(String studyToMonth) {
    this.studyToMonth = studyToMonth;
  }

  /**
   * @return FIRST_NAME
   */
  @Column(name = "FIRST_NAME")
  public String getFirstName() {
    return firstName;
  }

  /**
   * @return the sex
   */
  @Column(name = "SEX")
  public Integer getSex() {
    return sex;
  }

  /**
   * @param sex the sex to set
   */
  public void setSex(Integer sex) {
    this.sex = sex;
  }

  /**
   * @param firstName
   */
  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  /**
   * @return LAST_NAME
   */
  @Column(name = "LAST_NAME")
  public String getLastName() {
    return lastName;
  }

  @Column(name = "ENAME")
  public String getEname() {
    return ename;
  }

  public void setEname(String ename) {
    this.ename = ename;
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
   * @return OTHER_NAME
   */
  @Column(name = "OTHER_NAME")
  public String getOtherName() {
    return otherName;
  }

  /**
   * @return the posId
   */
  @Column(name = "POS_ID")
  public Long getPosId() {
    return posId;
  }

  /**
   * @param posId the posId to set
   */
  public void setPosId(Long posId) {
    this.posId = posId;
  }

  /**
   * @param otherName
   */
  public void setOtherName(String otherName) {
    this.otherName = otherName;
  }

  /**
   * @return EMAIL
   */
  @Column(name = "EMAIL")
  public String getEmail() {
    return email;
  }

  /**
   * @return the degreeList
   */
  @Transient
  public Map<Long, String> getDegreeList() {
    return degreeList;
  }

  /**
   * @return the des3NodeId
   */
  @Transient
  public String getDes3NodeId() {
    return des3NodeId;
  }

  /**
   * @param des3NodeId the des3NodeId to set
   */
  public void setDes3NodeId(String des3NodeId) {
    this.des3NodeId = des3NodeId;
  }

  /**
   * @return the key
   */
  @Transient
  public String getKey() {
    return key;
  }

  /**
   * @param key the key to set
   */
  public void setKey(String key) {
    this.key = key;
  }

  /**
   * @return the colleageName
   */
  @Transient
  public String getColleageName() {
    return colleageName;
  }

  /**
   * @return the des3PsnId
   */
  @Transient
  public String getDes3PsnId() {
    return des3PsnId;
  }

  /**
   * @param des3PsnId the des3PsnId to set
   */
  public void setDes3PsnId(String des3PsnId) {
    this.des3PsnId = des3PsnId;
  }

  /**
   * @return the des3InviteId
   */
  @Transient
  public String getDes3InviteId() {
    return des3InviteId;
  }

  /**
   * @param des3InviteId the des3InviteId to set
   */
  public void setDes3InviteId(String des3InviteId) {
    this.des3InviteId = des3InviteId;
  }

  /**
   * @param colleageName the colleageName to set
   */
  public void setColleageName(String colleageName) {
    this.colleageName = colleageName;
  }

  /**
   * @return the colleageId
   */
  @Transient
  public Long getColleageId() {
    return colleageId;
  }

  /**
   * @param colleageId the colleageId to set
   */
  public void setColleageId(Long colleageId) {
    this.colleageId = colleageId;
  }

  /**
   * @param degreeList the degreeList to set
   */
  public void setDegreeList(Map<Long, String> degreeList) {
    this.degreeList = degreeList;
  }

  /**
   * @return the provinceId
   */
  @Transient
  public Long getProvinceId() {
    return provinceId;
  }

  /**
   * @param provinceId the provinceId to set
   */
  public void setProvinceId(Long provinceId) {
    this.provinceId = provinceId;
  }

  /**
   * @return the cityId
   */
  @Transient
  public Long getCityId() {
    return cityId;
  }

  /**
   * @param cityId the cityId to set
   */
  public void setCityId(Long cityId) {
    this.cityId = cityId;
  }

  @Transient
  public Long getDisId() {
    return disId;
  }

  public void setDisId(Long disId) {
    this.disId = disId;
  }

  /**
   * @param email
   */
  public void setEmail(String email) {
    this.email = email;
  }

  /**
   * @return the position
   */
  @Column(name = "POSITION")
  public String getPosition() {
    return position;
  }

  /**
   * @param position the position to set
   */
  public void setPosition(String position) {
    this.position = position;
  }

  /**
   * @return REGION_ID
   */
  @Column(name = "REGION_ID")
  public Long getRegionId() {
    return regionId;
  }

  /**
   * @return the titleTechList
   */
  @Transient
  public Map<Long, String> getTitleTechList() {
    return titleTechList;
  }

  /**
   * @param titleTechList the titleTechList to set
   */
  public void setTitleTechList(Map<Long, String> titleTechList) {
    this.titleTechList = titleTechList;
  }

  /**
   * @param regionId
   */
  public void setRegionId(Long regionId) {
    this.regionId = regionId;
  }

  public void setRegionId2(String regionId) {
    if (NumberUtils.isNumber(regionId)) {
      this.regionId = Long.parseLong(regionId);
    }
  }

  /**
   * @return REG_DATE
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

  /**
   * @return the countryJson
   */
  @Transient
  public String getCountryJson() {
    return countryJson;
  }

  /**
   * @param countryJson the countryJson to set
   */
  public void setCountryJson(String countryJson) {
    this.countryJson = countryJson;
  }

  /**
   * @return the cityJson
   */
  @Transient
  public String getCityJson() {
    return cityJson;
  }

  /**
   * @param cityJson the cityJson to set
   */
  public void setCityJson(String cityJson) {
    this.cityJson = cityJson;
  }

  /**
   * @return the positionType
   */
  @Transient
  public String getPositionType() {
    return positionType;
  }

  /**
   * @param positionType the positionType to set
   */
  public void setPositionType(String positionType) {
    this.positionType = positionType;
  }

  /**
   * @return insName
   */
  // ljj @Transient
  @Column(name = "INS_NAME")
  public String getInsName() {
    return insName;
  }

  /**
   * @param insName
   */
  public void setInsName(String insName) {
    this.insName = insName;
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
   * @return address
   */
  @Transient
  public String getAddress() {
    return address;
  }

  /**
   * @param address
   */
  public void setAddress(String address) {
    this.address = address;
  }

  /**
   * @return http
   */
  @Transient
  public String getHttp() {
    return http;
  }

  /**
   * @param http
   */
  public void setHttp(String http) {
    this.http = http;
  }

  /**
   * @return insCname
   */
  @Transient
  public String getInsCname() {
    return insCname;
  }

  /**
   * @param insCname
   */
  public void setInsCname(String insCname) {
    this.insCname = insCname;
  }

  /**
   * @return insEname
   */
  @Transient
  public String getInsEname() {
    return insEname;
  }

  /**
   * @param insEname
   */
  public void setInsEname(String insEname) {
    this.insEname = insEname;
  }

  /**
   * @return unit
   */
  @Transient
  public String getUnit() {
    return unit;
  }

  /**
   * @param unit
   */
  public void setUnit(String unit) {
    this.unit = unit;
  }

  /**
   * @return unitId
   */
  @Transient
  public Long getUnitId() {
    return unitId;
  }

  /**
   * @param unitId
   */
  public void setUnitId(Long unitId) {
    this.unitId = unitId;
  }

  /**
   * @return insId
   */
  // ljj @Transient
  @Column(name = "INS_ID")
  public Long getInsId() {
    return insId;
  }

  /**
   * @param insId
   */
  public void setInsId(Long insId) {
    this.insId = insId;
  }

  /**
   * @return loginName
   */
  @Transient
  public String getloginName() {
    return loginName;
  }

  /**
   * @param loginNames
   */
  public void setloginName(String loginNames) {
    loginName = loginNames;
  }

  /**
   * @return title
   */
  // @Column(name = "TITLE")
  @Transient
  public String getTitle() {
    return title;
  }

  /**
   * @param title
   */
  public void setTitle(String title) {
    this.title = title;
  }

  @Column(name = "TITOLO")
  public String getTitolo() {
    return titolo;
  }

  public void setTitolo(String titolo) {
    this.titolo = titolo;
  }

  /**
   * @return the fromYear
   */
  @Transient
  public String getFromYear() {
    return fromYear;
  }

  /**
   * @param fromYear the fromYear to set
   */
  public void setFromYear(String fromYear) {
    this.fromYear = fromYear;
  }

  /**
   * @return the fromMonth
   */
  @Transient
  public String getFromMonth() {
    return fromMonth;
  }

  /**
   * @param fromMonth the fromMonth to set
   */
  public void setFromMonth(String fromMonth) {
    this.fromMonth = fromMonth;
  }

  @Transient
  public String getProvinceJson() {
    return provinceJson;
  }

  public void setProvinceJson(String provinceJson) {
    this.provinceJson = provinceJson;
  }

  @Transient
  public int getIsRolAddPsn() {
    return isRolAddPsn;
  }

  public void setIsRolAddPsn(int isRolAddPsn) {
    this.isRolAddPsn = isRolAddPsn;
  }

  /**
   * @return the isRegisterR
   */
  @Transient
  public boolean getIsRegisterR() {
    return isRegisterR;
  }

  /**
   * @param isRegisterR the isRegisterR to set
   */
  public void setIsRegisterR(boolean isRegisterR) {
    this.isRegisterR = isRegisterR;
  }

  /**
   * @return the study
   */
  @Transient
  public String getStudy() {
    return study;
  }

  /**
   * @param study the study to set
   */
  public void setStudy(String study) {
    this.study = study;
  }

  /**
   * @return the degree
   */
  @Column(name = "DEGREE")
  public String getDegree() {
    return degree;
  }

  /**
   * @param degree the degree to set
   */
  public void setDegree(String degree) {
    this.degree = degree;
  }

  /**
   * @return newpassword
   */
  @Transient
  public String getNewpassword() {
    return newpassword;
  }

  /**
   * @param newpassword
   */
  public void setNewpassword(String newpassword) {
    this.newpassword = newpassword;
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

  @Override
  public String toString() {
    return "PersonRegister [personId=" + personId + ", firstName=" + firstName + ", zhFirstName=" + zhFirstName
        + ", lastName=" + lastName + ", zhLastName=" + zhLastName + ", name=" + name + ", ename=" + ename
        + ", otherName=" + otherName + ", email=" + email + ", birthday=" + birthday + ", regionId=" + regionId
        + ", regData=" + regData + ", avatars=" + avatars + ", insName=" + insName + ", positionType=" + positionType
        + ", newpassword=" + newpassword + ", tel=" + tel + ", mobile=" + mobile + ", address=" + address + ", http="
        + http + ", insCname=" + insCname + ", insEname=" + insEname + ", unit=" + unit + ", unitId=" + unitId
        + ", insId=" + insId + ", loginName=" + loginName + ", title=" + title + ", titolo=" + titolo + ", position="
        + position + ", posId=" + posId + ", posGrades=" + posGrades + ", fromYear=" + fromYear + ", fromMonth="
        + fromMonth + ", studyFromYear=" + studyFromYear + ", studyFromMonth=" + studyFromMonth + ", studyToYear="
        + studyToYear + ", studyToMonth=" + studyToMonth + ", countryJson=" + countryJson + ", provinceJson="
        + provinceJson + ", provinceId=" + provinceId + ", cityId=" + cityId + ", disId=" + disId + ", sex=" + sex
        + ", titleTechList=" + titleTechList + ", degreeList=" + degreeList + ", study=" + study + ", degree=" + degree
        + ", colleageName=" + colleageName + ", colleageId=" + colleageId + ", des3InviteId=" + des3InviteId
        + ", des3PsnId=" + des3PsnId + ", des3NodeId=" + des3NodeId + ", key=" + key + ", guid=" + guid + ", lastLogin="
        + lastLogin + ", pubTotal=" + pubTotal + ", workInsName=" + workInsName + ", cityJson=" + cityJson
        + ", isRegisterR=" + isRegisterR + ", isRolAddPsn=" + isRolAddPsn + ", emailLanguageVersion="
        + emailLanguageVersion + ", renewpassword=" + renewpassword + ", degreeName=" + degreeName + ", isPwdEncrypt="
        + isPwdEncrypt + ", isEmailVerify=" + isEmailVerify + ", pcode=" + pcode + ", orgnature=" + orgnature
        + ", mobileReg=" + mobileReg + ", fromSys=" + fromSys + ", department=" + department + "]";
  }

  @Transient
  public String getGuid() {
    return guid;
  }

  public void setGuid(String guid) {
    this.guid = guid;
  }

  /**
   * @return the lastLogin
   */
  @Transient
  public Date getLastLogin() {
    return lastLogin;
  }

  /**
   * @param lastLogin the lastLogin to set
   */
  public void setLastLogin(Date lastLogin) {
    this.lastLogin = lastLogin;
  }

  /**
   * @return the pubTotal
   */
  @Transient
  public Long getPubTotal() {
    return pubTotal;
  }

  /**
   * @param pubTotal the pubTotal to set
   */
  public void setPubTotal(Long pubTotal) {
    this.pubTotal = pubTotal;
  }

  @Column(name = "EMAIL_LANGUAGE_VERSION")
  public String getEmailLanguageVersion() {
    return emailLanguageVersion;
  }

  public void setEmailLanguageVersion(String emailLanguageVersion) {
    this.emailLanguageVersion = emailLanguageVersion;
  }

  /**
   * @return the workInsName
   */
  @Transient
  public String getWorkInsName() {
    return workInsName;
  }

  /**
   * @param workInsName the workInsName to set
   */
  public void setWorkInsName(String workInsName) {
    this.workInsName = workInsName;
  }

  @Override
  public boolean equals(Object other) {
    if ((this == other))
      return true;
    if ((other == null))
      return false;
    if (!(other instanceof PersonRegister))
      return false;
    PersonRegister otherPerson = (PersonRegister) other;

    return (this.getPersonId().longValue() == otherPerson.getPersonId());

  }

  /**
   * @return the renewpassword
   */
  @Transient
  public String getRenewpassword() {
    return renewpassword;
  }

  /**
   * @param renewpassword the renewpassword to set
   */
  public void setRenewpassword(String renewpassword) {
    this.renewpassword = renewpassword;
  }

  @Column(name = "POS_GRADES")
  public Integer getPosGrades() {
    return posGrades;
  }

  public void setPosGrades(Integer posGrades) {
    this.posGrades = posGrades;
  }

  @Column(name = "DEGREE_NAME")
  public String getDegreeName() {
    return degreeName;
  }

  public void setDegreeName(String degreeName) {
    this.degreeName = degreeName;
  }

  @Transient
  public Boolean getIsPwdEncrypt() {
    return isPwdEncrypt;
  }

  public void setIsPwdEncrypt(Boolean isPwdEncrypt) {
    this.isPwdEncrypt = isPwdEncrypt;
  }

  @Transient
  public Boolean getIsEmailVerify() {
    return isEmailVerify;
  }

  public void setIsEmailVerify(Boolean isEmailVerify) {
    this.isEmailVerify = isEmailVerify;
  }

  @Transient
  public String getPcode() {
    return pcode;
  }

  public void setPcode(String pcode) {
    this.pcode = pcode;
  }

  @Transient
  public String getOrgnature() {
    return orgnature;
  }

  public void setOrgnature(String orgnature) {
    this.orgnature = orgnature;
  }

  @Transient
  public String getMobileReg() {
    return mobileReg;
  }

  public void setMobileReg(String mobileReg) {
    this.mobileReg = mobileReg;
  }

  @Column(name = "BIRTHDAY")
  public String getBirthday() {
    return birthday;
  }

  @Column(name = "FROM_SYS")
  public String getFromSys() {
    return fromSys;
  }

  public void setFromSys(String fromSys) {
    this.fromSys = fromSys;
  }

  // 出生日期yyyy/mm/dd，也可yyyy或 yyyy/mm
  public void setBirthday(String birthday) {
    if (StringUtils.isNoneBlank(birthday)) {
      String pattem = "";
      switch (birthday.length()) {
        case 4:
          pattem = "yyyy";
          break;
        case 7:
          pattem = "yyyy/mm";
          break;
        case 10:
          pattem = "yyyy/mm/dd";
          break;
        default:
          break;
      }
      DateFormat df = new SimpleDateFormat(pattem);
      try {
        df.parse(birthday);
        this.birthday = birthday;
      } catch (ParseException e) {
      }
    }

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

  @Column(name = "DEPARTMENT")
  public String getDepartment() {
    return department;
  }

  public void setDepartment(String department) {
    this.department = department;
  }



}
