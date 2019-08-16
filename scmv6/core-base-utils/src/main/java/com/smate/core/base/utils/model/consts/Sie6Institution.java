package com.smate.core.base.utils.model.consts;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang.StringUtils;
import org.springframework.context.i18n.LocaleContextHolder;

/**
 * SIE 机构实体
 * 
 * @author hd
 *
 */
@Entity
@Table(name = "INSTITUTION")
public class Sie6Institution implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = 4325184943046989853L;
  // 主健
  private Long id;
  // 中文名
  private String zhName;
  // 英文名
  private String enName;
  // 名称缩写
  private String abbr;
  // 联系人
  private String contactPerson;
  // 联系电话
  private String tel;
  // 单位服务邮箱
  private String serverEmail;
  // 联系人邮箱
  private String contactEmail;
  // 单位网址
  private String url;
  // 单位状态， 0未开始使用,1:注册,2:审核通过,9:删除
  private Long status;
  // 单位性质单位性质 1高校，2研究中心，3资助机构，4企业，5出版社，6协会，7医院，99其他
  private Long nature;
  // 单位邮箱服务器后缀列表
  private String checkEmails;
  // 中文地址
  private String zhAddress;
  // 英文地址
  private String enAddress;
  // 邮编
  private String postcode;
  // 传真
  private String fox;
  // 统一社会信用代码
  private String uniformId1;
  // 组织机构代码
  private String uniformId2;
  // 单位服务电话
  private String serverTel;
  // 机构版开通方式，1 表单注册 2 任务创建 3 接口创建 4 机构主页
  private Integer dataFrom;
  // 单位名，根据语音选择
  private String insName;
  // 单位域名，真实数据放在ins_portal.
  private String insDomain;
  private String logo;
  // 经度
  private String longitude;
  // 纬度
  private String latitude;
  // 是否开通报表统计功能
  private int stat;
  // 是否开通报表对比功能
  private int cons;
  // 省号
  private Long provinceId;
  // 市名
  private Long cityId;

  private String mobile;

  // 审核通过时间
  private Date createDate;

  // 单位更新时间
  private Date updateDate;

  // 统计数更新时间
  private Date stUpdateDate;

  // 单位信息完整度
  private Integer integrity;

  // 单位简介
  private String briefDesc;

  // 是否用于自动提示0否1是，个人版模糊匹配用
  private Integer autoComplete = 1;

  @Id
  @Column(name = "INS_ID")
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_INSTITUTION", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  @Column(name = "ZH_NAME")
  public String getZhName() {
    return zhName;
  }

  public void setZhName(String zhName) {
    this.zhName = zhName;
  }

  @Column(name = "EN_NAME")
  public String getEnName() {
    return enName;
  }

  public void setEnName(String enName) {
    this.enName = enName;
  }

  @Column(name = "ABBR")
  public String getAbbr() {
    return abbr;
  }

  public void setAbbr(String abbr) {
    this.abbr = abbr;
  }

  @Column(name = "CONTACT_PERSON")
  public String getContactPerson() {
    return contactPerson;
  }

  public void setContactPerson(String contactPerson) {
    this.contactPerson = contactPerson;
  }

  @Column(name = "TEL")
  public String getTel() {
    return tel;
  }

  public void setTel(String tel) {
    this.tel = tel;
  }

  @Column(name = "SERVER_EMAIL") // CONTACT_EMAIL
  public String getServerEmail() {
    return serverEmail;
  }

  public void setServerEmail(String serverEmail) {
    this.serverEmail = serverEmail;
  }

  @Column(name = "CONTACT_EMAIL")
  public String getContactEmail() {
    return contactEmail;
  }

  public void setContactEmail(String contactEmail) {
    this.contactEmail = contactEmail;
  }

  @Column(name = "URL")
  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  @Column(name = "STATUS")
  public Long getStatus() {
    return status;
  }

  public void setStatus(Long status) {
    this.status = status;
  }

  @Column(name = "NATURE")
  public Long getNature() {
    return nature;
  }

  public void setNature(Long nature) {
    this.nature = nature;
  }

  @Column(name = "CHECK_EMAILS")
  public String getCheckEmails() {
    return checkEmails;
  }

  public void setCheckEmails(String checkEmails) {
    this.checkEmails = checkEmails;
  }

  @Column(name = "ZH_ADDRESS")
  public String getZhAddress() {
    return zhAddress;
  }

  public void setZhAddress(String zhAddress) {
    this.zhAddress = zhAddress;
  }

  @Column(name = "EN_ADDRESS")
  public String getEnAddress() {
    return enAddress;
  }

  public void setEnAddress(String enAddress) {
    this.enAddress = enAddress;
  }

  @Column(name = "POST_CODE")
  public String getPostcode() {
    return postcode;
  }

  public void setPostcode(String postcode) {
    this.postcode = postcode;
  }

  @Column(name = "FOX")
  public String getFox() {
    return fox;
  }

  public void setFox(String fox) {
    this.fox = fox;
  }

  @Column(name = "UNIFORM_ID1")
  public String getUniformId1() {
    return uniformId1;
  }

  public void setUniformId1(String uniformId1) {
    this.uniformId1 = uniformId1;
  }

  @Column(name = "UNIFORM_ID2")
  public String getUniformId2() {
    return uniformId2;
  }

  public void setUniformId2(String uniformId2) {
    this.uniformId2 = uniformId2;
  }

  @Column(name = "SERVER_TEL")
  public String getServerTel() {
    return serverTel;
  }

  public void setServerTel(String serverTel) {
    this.serverTel = serverTel;
  }

  public Sie6Institution() {
    super();
  }

  public Sie6Institution(Long id) {
    super();
    this.id = id;
  }

  public Sie6Institution(Long id, String zhName, String enName, String insDomain, String logo) {
    super();
    this.id = id;
    this.zhName = zhName;
    this.enName = enName;
    this.insDomain = insDomain;
    this.logo = logo;
  }

  /**
   * 目前此方法添加了国际化的支持
   * 
   * @return
   */
  @Transient
  public String getInsName() {
    String language = LocaleContextHolder.getLocale().getLanguage();
    // 如单位名称不为空，则不对单位名称进行赋值_MJG_2013-03-25_ROL-469.
    if (StringUtils.isBlank(insName)) {
      // if ("zh".equals(language)) {
      // this.insName = StringUtils.isNotBlank(zhName) ? zhName : enName;
      // } else {
      // this.insName = StringUtils.isNotBlank(enName) ? enName : zhName;
      // }
      this.insName = StringUtils.isNotBlank(zhName) ? zhName : enName;// ROL-6612
    }
    return insName;
  }

  public void setInsName(String insName) {
    this.insName = insName;
  }

  @Transient
  public String getInsDomain() {
    return insDomain;
  }

  public void setInsDomain(String insDomain) {
    this.insDomain = insDomain;
  }

  @Transient
  public String getLogo() {
    return logo;
  }

  public void setLogo(String logo) {
    this.logo = logo;
  }

  @Transient
  public String getLongitude() {
    return longitude;
  }

  public void setLongitude(String longitude) {
    this.longitude = longitude;
  }

  @Transient
  public String getLatitude() {
    return latitude;
  }

  public void setLatitude(String latitude) {
    this.latitude = latitude;
  }

  @Transient
  public int getStat() {
    return stat;
  }

  public void setStat(int stat) {
    this.stat = stat;
  }

  @Transient
  public int getCons() {
    return cons;
  }

  public void setCons(int cons) {
    this.cons = cons;
  }

  @Transient
  public Long getProvinceId() {
    return provinceId;
  }

  public void setProvinceId(Long provinceId) {
    this.provinceId = provinceId;
  }

  @Transient
  public Long getCityId() {
    return cityId;
  }

  public void setCityId(Long cityId) {
    this.cityId = cityId;
  }

  @Column(name = "DATA_FROM")
  public Integer getDataFrom() {
    return dataFrom;
  }

  public void setDataFrom(Integer dataFrom) {
    this.dataFrom = dataFrom;
  }

  @Column(name = "MOBILE")
  public String getMobile() {
    return mobile;
  }

  public void setMobile(String mobile) {
    this.mobile = mobile;
  }

  @Column(name = "CREATE_DATE")
  public Date getCreateDate() {
    return createDate;
  }

  public void setCreateDate(Date createDate) {
    this.createDate = createDate;
  }

  @Column(name = "UPDATE_DATE")
  public Date getUpdateDate() {
    return updateDate;
  }

  public void setUpdateDate(Date updateDate) {
    this.updateDate = updateDate;
  }

  @Column(name = "BRIEF_DESC")
  public String getBriefDesc() {
    return briefDesc;
  }

  public void setBriefDesc(String briefDesc) {
    this.briefDesc = briefDesc;
  }


  @Column(name = "INTEGRITY")
  public Integer getIntegrity() {
    return integrity;
  }

  public void setIntegrity(Integer integrity) {
    this.integrity = integrity;
  }

  @Column(name = "AUTO_COMPLETE")
  public Integer getAutoComplete() {
    return autoComplete;
  }

  public void setAutoComplete(Integer autoComplete) {
    this.autoComplete = autoComplete;
  }

  @Transient
  public Date getStUpdateDate() {
    return stUpdateDate;
  }

  public void setStUpdateDate(Date stUpdateDate) {
    this.stUpdateDate = stUpdateDate;
  }
}


