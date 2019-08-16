package com.smate.sie.center.task.model;

import java.io.Serializable;

/**
 * 专用于单位同步.
 * 
 * @author hd
 *
 */
public class SieInsInfo implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = 8012649935710300803L;

  private Long orgCode;
  private String zhName;
  private String enName;
  private String prov;
  private String city;
  private String token;
  private String contactName;
  private String contactEmail;
  private String contactTel;
  private Long synFlag;
  private String synMsg;
  private String address;
  private String url;
  private Long cyId;
  private Long prvId;
  private Long countryId;
  private Long disId;
  private Long insId;
  private Long nature;
  private Long status = 3L;
  private Long psnId;
  private String firstName;
  private String lastName;
  private String zhFirstName;
  private String zhLastName;
  private String psnEname;
  private Long regionId;
  private String switchLang = "1";
  private Integer version = 0;
  private String defaultLang = "zh_CN";
  private String SnsAvatar;
  private String RolAvatar;
  private Long sid;
  private String position;
  private Long posId;
  private String posGrades;
  private String tel;
  private String unitName;
  private Integer sex;
  private String mobile;
  private Long psnRegionId;
  private String uniformId1;
  private String PsnTitolo;

  public String getPsnTitolo() {
    return PsnTitolo;
  }

  public void setPsnTitolo(String psnTitolo) {
    PsnTitolo = psnTitolo;
  }

  public SieInsInfo() {
    super();
  }

  public SieInsInfo(ImportInsDataInfo tmp) {
    super();
    this.orgCode = tmp.getId().getOrgCode();
    this.zhName = tmp.getZhName();
    // this.enName = tmp.getEnName();
    this.prov = tmp.getProv();
    this.city = tmp.getCity();
    this.token = tmp.getId().getToken();
    this.contactName = tmp.getContactName();
    this.contactEmail = tmp.getContactEmail();
    this.contactTel = tmp.getContactTel();
    this.synFlag = tmp.getSynFlag();
    this.synMsg = tmp.getSynMsg();
    this.address = tmp.getAddress();
    this.url = tmp.getUrl();
    this.uniformId1 = tmp.getUniformId1();
  }

  public Long getOrgCode() {
    return orgCode;
  }

  public void setOrgCode(Long orgCode) {
    this.orgCode = orgCode;
  }

  public String getZhName() {
    return zhName;
  }

  public void setZhName(String zhName) {
    this.zhName = zhName;
  }

  public String getEnName() {
    return enName;
  }

  public void setEnName(String enName) {
    this.enName = enName;
  }

  public Long getNature() {
    return nature;
  }

  public void setNature(Long nature) {
    this.nature = nature;
  }

  public Long getStatus() {
    return status;
  }

  public void setStatus(Long status) {
    this.status = status;
  }

  public String getProv() {
    return prov;
  }

  public void setProv(String prov) {
    this.prov = prov;
  }

  public String getCity() {
    return city;
  }

  public void setCity(String city) {
    this.city = city;
  }

  public String getToken() {
    return token;
  }

  public void setToken(String token) {
    this.token = token;
  }

  public String getContactName() {
    return contactName;
  }

  public void setContactName(String contactName) {
    this.contactName = contactName;
  }

  public String getContactEmail() {
    return contactEmail;
  }

  public void setContactEmail(String contactEmail) {
    this.contactEmail = contactEmail;
  }

  public String getContactTel() {
    return contactTel;
  }

  public void setContactTel(String contactTel) {
    this.contactTel = contactTel;
  }

  public Long getSynFlag() {
    return synFlag;
  }

  public void setSynFlag(Long synFlag) {
    this.synFlag = synFlag;
  }

  public String getSynMsg() {
    return synMsg;
  }

  public void setSynMsg(String synMsg) {
    this.synMsg = synMsg;
  }

  public String getAddress() {
    return address;
  }

  public void setAddress(String address) {
    this.address = address;
  }

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  public Long getCyId() {
    return cyId;
  }

  public void setCyId(Long cyId) {
    this.cyId = cyId;
  }

  public Long getPrvId() {
    return prvId;
  }

  public void setPrvId(Long prvId) {
    this.prvId = prvId;
  }

  public Long getInsId() {
    return insId;
  }

  public void setInsId(Long insId) {
    this.insId = insId;
  }

  public Long getPsnId() {
    return psnId;
  }

  public void setPsnId(Long psnId) {
    this.psnId = psnId;
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

  public String getPsnEname() {
    return psnEname;
  }

  public void setPsnEname(String psnEname) {
    this.psnEname = psnEname;
  }

  public Long getDisId() {
    return disId;
  }

  public void setDisId(Long disId) {
    this.disId = disId;
  }

  public Long getRegionId() {
    return regionId;
  }

  public void setRegionId(Long regionId) {
    this.regionId = regionId;
  }

  public String getSwitchLang() {
    return switchLang;
  }

  public void setSwitchLang(String switchLang) {
    this.switchLang = switchLang;
  }

  public Integer getVersion() {
    return version;
  }

  public void setVersion(Integer version) {
    this.version = version;
  }

  public String getDefaultLang() {
    return defaultLang;
  }

  public void setDefaultLang(String defaultLang) {
    this.defaultLang = defaultLang;
  }

  public String getSnsAvatar() {
    return SnsAvatar;
  }

  public void setSnsAvatar(String snsAvatar) {
    SnsAvatar = snsAvatar;
  }

  public String getRolAvatar() {
    return RolAvatar;
  }

  public void setRolAvatar(String rolAvatar) {
    RolAvatar = rolAvatar;
  }

  public String getZhFirstName() {
    return zhFirstName;
  }

  public void setZhFirstName(String zhFirstName) {
    this.zhFirstName = zhFirstName;
  }

  public String getZhLastName() {
    return zhLastName;
  }

  public void setZhLastName(String zhLastName) {
    this.zhLastName = zhLastName;
  }

  public Long getSid() {
    return sid;
  }

  public void setSid(Long sid) {
    this.sid = sid;
  }

  public Long getCountryId() {
    return countryId;
  }

  public void setCountryId(Long countryId) {
    this.countryId = countryId;
  }

  public String getPosition() {
    return position;
  }

  public void setPosition(String position) {
    this.position = position;
  }

  public Long getPosId() {
    return posId;
  }

  public void setPosId(Long posId) {
    this.posId = posId;
  }

  public String getPosGrades() {
    return posGrades;
  }

  public void setPosGrades(String posGrades) {
    this.posGrades = posGrades;
  }

  public String getTel() {
    return tel;
  }

  public void setTel(String tel) {
    this.tel = tel;
  }

  public String getUnitName() {
    return unitName;
  }

  public void setUnitName(String unitName) {
    this.unitName = unitName;
  }

  public Integer getSex() {
    return sex;
  }

  public void setSex(Integer sex) {
    this.sex = sex;
  }

  public String getMobile() {
    return mobile;
  }

  public void setMobile(String mobile) {
    this.mobile = mobile;
  }

  public Long getPsnRegionId() {
    return psnRegionId;
  }

  public void setPsnRegionId(Long psnRegionId) {
    this.psnRegionId = psnRegionId;
  }

  public String getUniformId1() {
    return uniformId1;
  }

  public void setUniformId1(String uniformId1) {
    this.uniformId1 = uniformId1;
  }

}
