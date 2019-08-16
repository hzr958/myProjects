package com.smate.web.psn.model.setting;

import java.io.Serializable;

import org.apache.commons.lang.StringUtils;

import com.smate.core.base.utils.model.security.Person;


/**
 * 人员信息同步消息.
 * 
 * @author liqinghua
 * 
 */
public class SnsPersonSyncMessage implements Serializable {



  /**
   * 
   */
  private static final long serialVersionUID = 5413337903189650545L;
  // 用户编码
  private Long psnId;
  // 用户名
  private String firstName;
  // 用户姓氏
  private String lastName;
  // 用户中文明
  private String zhName;
  // 用户另用名
  private String otherName;
  // 用户英文名
  private String enName;
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
  // Skype
  private String skype;
  // 头衔
  private String titolo;
  // 头像地址
  private String avatars;
  // 职务
  private String position;
  private Long posId;
  private Integer posGrades;
  private String insName;
  private String name;
  private Integer sex;
  // 是否是要隐藏的用户
  private Integer isPrivate;
  // zh,en
  private String lang;
  private Integer nodeId;
  // lcw人员工作单位教育经历，国家地区
  private Boolean isSyncIns;
  private Long regionId;
  private String insIdList;
  private String insNameList;

  private String unitName;

  public SnsPersonSyncMessage() {
    super();
  }

  public SnsPersonSyncMessage(Person person, String lang, Integer nodeId, Integer fromNodeId, Integer sex) {
    // super(fromNodeId);
    this.psnId = person.getPersonId();
    this.firstName = person.getFirstName();
    this.lastName = person.getLastName();
    this.zhName = person.getName();
    this.enName = person.getEname();
    this.otherName = person.getOtherName();
    this.tel = person.getTel();
    this.mobile = person.getMobile();
    this.email = person.getEmail();
    this.msnNo = person.getMsnNo();
    this.qqNo = person.getQqNo();
    this.skype = person.getSkype();
    this.titolo = person.getTitolo();
    this.avatars = person.getAvatars();
    this.posGrades = person.getPosGrades();
    this.posId = person.getPosId();
    this.position = person.getPosition();
    this.insName = person.getInsName();
    this.name = person.getName();
    // this.isPrivate = person.getIsPrivate();
    this.lang = lang;
    this.nodeId = nodeId;
    this.sex = sex;
    // this.isSyncIns = person.getIsSyncIns();
    // this.regionId = person.getRegionId();
    // this.insIdList = person.getInsIdList();
    // this.insNameList = person.getInsNameList();
    // this.unitName = person.getDepartment();
  }

  public Long getPsnId() {
    return psnId;
  }

  public String getFirstName() {
    return firstName;
  }

  public Integer getNodeId() {
    return nodeId;
  }

  public void setNodeId(Integer nodeId) {
    this.nodeId = nodeId;
  }

  public String getLastName() {
    return lastName;
  }

  public String getZhName() {
    return zhName;
  }

  public String getOtherName() {
    return otherName;
  }

  public Integer getSex() {
    return sex;
  }

  public void setSex(Integer sex) {
    this.sex = sex;
  }

  public String getEnName() {
    return enName;
  }

  public String getTel() {
    return tel;
  }

  public String getMobile() {
    return mobile;
  }

  public String getEmail() {
    return email;
  }

  public String getMsnNo() {
    return msnNo;
  }

  public String getQqNo() {
    return qqNo;
  }

  public String getSkype() {
    return skype;
  }

  public String getTitolo() {
    return titolo;
  }

  public String getAvatars() {
    return avatars;
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

  public void setPsnId(Long psnId) {
    this.psnId = psnId;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public void setZhName(String zhName) {
    this.zhName = zhName;
  }

  public void setOtherName(String otherName) {
    this.otherName = otherName;
  }

  public void setEnName(String enName) {
    this.enName = enName;
  }

  public void setTel(String tel) {
    this.tel = tel;
  }

  public void setMobile(String mobile) {
    this.mobile = mobile;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public void setMsnNo(String msnNo) {
    this.msnNo = msnNo;
  }

  public void setQqNo(String qqNo) {
    this.qqNo = qqNo;
  }

  public void setSkype(String skype) {
    this.skype = skype;
  }

  public void setTitolo(String titolo) {
    this.titolo = titolo;
  }

  public void setAvatars(String avatars) {
    this.avatars = avatars;
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

  public String getInsName() {
    return insName;
  }

  public String getName() {
    return name;
  }

  public void setInsName(String insName) {
    this.insName = insName;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getLang() {
    return lang;
  }

  public void setLang(String lang) {
    this.lang = lang;
  }

  public String getNameByLang() {
    // if (Locale.US.getLanguage().equalsIgnoreCase(lang)) {
    // return StringUtils.isBlank(enName) ? zhName : enName;
    // } else {
    // return StringUtils.isBlank(zhName) ? enName : zhName;
    // }
    return StringUtils.isBlank(zhName) ? enName : zhName;
  }

  public Integer getIsPrivate() {
    return isPrivate;
  }

  public void setIsPrivate(Integer isPrivate) {
    this.isPrivate = isPrivate;
  }

  public Long getRegionId() {
    return regionId;
  }

  public void setRegionId(Long regionId) {
    this.regionId = regionId;
  }

  public String getInsIdList() {
    return insIdList;
  }

  public void setInsIdList(String insIdList) {
    this.insIdList = insIdList;
  }

  public String getInsNameList() {
    return insNameList;
  }

  public void setInsNameList(String insNameList) {
    this.insNameList = insNameList;
  }

  public Boolean getIsSyncIns() {
    return isSyncIns == null ? false : isSyncIns;
  }

  public void setIsSyncIns(Boolean isSyncIns) {
    this.isSyncIns = isSyncIns;
  }

  public String getUnitName() {
    return unitName;
  }

  public void setUnitName(String unitName) {
    this.unitName = unitName;
  }

}
