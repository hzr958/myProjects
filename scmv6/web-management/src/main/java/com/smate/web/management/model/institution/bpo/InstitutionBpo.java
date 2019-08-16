package com.smate.web.management.model.institution.bpo;

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

import com.smate.web.management.model.institution.sns.InstitutionAdd;

/**
 * 机构类
 * 
 * @author zjh
 * 
 */

@Entity
@Table(name = "INSTITUTION")
public class InstitutionBpo implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = 4676334540779910470L;
  // 主健
  private Long id;
  private String insName;
  // 中文名
  private String zhName;
  // 英文名
  private String enName;
  // 名称缩写
  private String abbreviation;
  // 联系人
  private String contactPerson;
  // 中文地址
  private String zhAddress;
  // 英文地址
  private String enAddress;
  // 联系电话
  private String tel;
  // 单位网址
  private String url;
  // 单位状态， 0未开始使用,1:注册,2:审核通过,9:删除
  private Long status;
  // 单位性质 1: 高校; 2: 研究中心; 3: 资助机构 4:企业 ，5：出版社 ，6 协会 99: 其它 (1: college; 2:
  // research center; 3: funding agency;99: others)
  private Long nature;
  // 单位服务邮箱
  private String serverEmail;
  // 单位服务电话
  private String serverTel;
  // 地区编码，对应：const_region表主键
  private Long regionId;
  private String postcode;

  // 和ISIS系统对应的单位Code，同步数据需以此Code为准
  private Integer isisOrgCode;

  // 省号
  private Long provinceId;
  // 市名
  private Long cityId;
  // 经度
  private String longitude;
  // 纬度
  private String latitude;
  // 是否开通报表统计功能
  private int stat;
  // 是否开通报表对比功能
  private int cons;
  // 是否允许单位在下拉框显示 0-否 1-是
  private int enabled;
  // 单位传真路径
  private String faxAttachmentPath;
  // 单位来源
  private InsSource insSource;
  // 单位Logo
  private String logoPath;
  // 单位审核时间
  private Date appDate;


  @Transient
  public int getCons() {
    return cons;
  }

  public void setCons(int cons) {
    this.cons = cons;
  }

  public InstitutionBpo() {}

  public InstitutionBpo(Long id, String zhName, String enName) {
    super();
    this.id = id;
    this.zhName = zhName;
    this.enName = enName;
  }

  public InstitutionBpo(InstitutionBpo ins, InsSource insSource) {
    this.id = ins.id;
    this.zhName = ins.zhName;
    this.enName = ins.enName;
    this.abbreviation = ins.abbreviation;
    this.contactPerson = ins.contactPerson;
    this.zhAddress = ins.zhAddress;
    this.enAddress = ins.enAddress;
    this.tel = ins.tel;
    this.url = ins.url;
    this.status = ins.status;
    this.nature = ins.nature;
    this.serverEmail = ins.serverEmail;
    this.serverTel = ins.serverTel;
    this.regionId = ins.regionId;
    this.postcode = ins.postcode;
    this.isisOrgCode = ins.isisOrgCode;
    this.provinceId = ins.provinceId;
    this.cityId = ins.cityId;
    this.longitude = ins.longitude;
    this.latitude = ins.latitude;
    this.stat = ins.stat;
    this.cons = ins.cons;
    this.faxAttachmentPath = ins.faxAttachmentPath;
    this.enabled = ins.enabled;
    this.insSource = insSource;
  }

  public InstitutionAdd getInstitution2(InstitutionBpo ins) {
    InstitutionAdd institution = new InstitutionAdd();
    institution.setAbbreviation(ins.getAbbreviation());
    institution.setCityId(ins.getCityId());
    institution.setCons(ins.getCons());
    institution.setContactPerson(ins.getContactPerson());
    institution.setEnabled(ins.getEnabled());
    institution.setEnAddress(ins.getEnAddress());
    institution.setEnName(ins.getEnName());
    institution.setId(ins.getId());
    institution.setIsisOrgCode(ins.getIsisOrgCode());
    institution.setLatitude(ins.getLatitude());
    institution.setLongitude(ins.getLongitude());
    institution.setNature(ins.getNature());
    institution.setPostcode(ins.getPostcode());
    institution.setProvinceId(ins.getProvinceId());
    institution.setRegionId(ins.getRegionId());
    institution.setServerEmail(ins.getServerEmail());
    institution.setServerTel(ins.getServerTel());
    institution.setStat(ins.getStat());
    institution.setStatus(ins.getStatus());
    institution.setTel(ins.getTel());
    institution.setUrl(ins.getUrl());
    institution.setZhAddress(ins.getZhAddress());
    institution.setZhName(ins.getZhName());
    return institution;
  }

  public InstitutionBpo(InstitutionBpo ins, Date appDate) {
    this.id = ins.id;
    this.zhName = ins.zhName;
    this.enName = ins.enName;
    this.abbreviation = ins.abbreviation;
    this.contactPerson = ins.contactPerson;
    this.zhAddress = ins.zhAddress;
    this.enAddress = ins.enAddress;
    this.tel = ins.tel;
    this.url = ins.url;
    this.status = ins.status;
    this.nature = ins.nature;
    this.serverEmail = ins.serverEmail;
    this.serverTel = ins.serverTel;
    this.regionId = ins.regionId;
    this.postcode = ins.postcode;
    this.isisOrgCode = ins.isisOrgCode;
    this.provinceId = ins.provinceId;
    this.cityId = ins.cityId;
    this.longitude = ins.longitude;
    this.latitude = ins.latitude;
    this.stat = ins.stat;
    this.cons = ins.cons;
    this.faxAttachmentPath = ins.faxAttachmentPath;
    this.enabled = ins.enabled;
    this.appDate = appDate;
  }

  /**
   * @return
   */
  @Id
  @Column(name = "INS_ID")
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_INSTITUTION", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
  public Long getId() {
    return id;
  }

  /**
   * @param id
   */
  public void setId(Long id) {
    this.id = id;
  }

  @Transient
  public String getInsName() {
    return zhName;
  }

  public void setInsName(String insName) {
    this.insName = insName;
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

  /**
   * @return
   */
  @Column(name = "ABBR")
  public String getAbbreviation() {
    return abbreviation;
  }

  /**
   * @param abbreviation
   */
  public void setAbbreviation(String abbreviation) {
    this.abbreviation = abbreviation;
  }

  /**
   * @return
   */
  @Column(name = "CONTACT_PERSON")
  public String getContactPerson() {
    return contactPerson;
  }

  /**
   * @param contactPerson
   */
  public void setContactPerson(String contactPerson) {
    this.contactPerson = contactPerson;
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

  /**
   * @return
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
   * @return
   */
  @Column(name = "URL")
  public String getUrl() {
    return url;
  }

  /**
   * @param url
   */
  public void setUrl(String url) {
    this.url = url;
  }

  @Column(name = "REGION_ID")
  public Long getRegionId() {
    return regionId;
  }

  public void setRegionId(Long regionId) {
    this.regionId = regionId;
  }

  /**
   * @return
   */
  @Column(name = "STATUS")
  public Long getStatus() {
    return status;
  }

  /**
   * @param status
   */
  public void setStatus(Long status) {
    this.status = status;
  }

  /**
   * @return
   */
  @Column(name = "NATURE")
  public Long getNature() {
    return nature;
  }

  /**
   * @param nature
   */
  public void setNature(Long nature) {
    this.nature = nature;
  }

  @Column(name = "SERVER_EMAIL")
  public String getServerEmail() {
    return serverEmail;
  }

  public void setServerEmail(String serverEmail) {
    this.serverEmail = serverEmail;
  }

  @Column(name = "SERVER_TEL")
  public String getServerTel() {
    return serverTel;
  }

  public void setServerTel(String serverTel) {
    this.serverTel = serverTel;
  }

  @Column(name = "POST_CODE")
  public String getPostcode() {
    return postcode;
  }

  public void setPostcode(String postcode) {
    this.postcode = postcode;
  }

  @Transient
  public Long getProvinceId() {
    return provinceId;
  }

  @Transient
  public Long getCityId() {
    return cityId;
  }

  @Transient
  public String getLongitude() {
    return longitude;
  }

  @Transient
  public String getLatitude() {
    return latitude;
  }

  @Transient
  public int getStat() {
    return stat;
  }

  public void setProvinceId(Long provinceId) {
    this.provinceId = provinceId;
  }

  public void setCityId(Long cityId) {
    this.cityId = cityId;
  }

  public void setLongitude(String longitude) {
    this.longitude = longitude;
  }

  public void setLatitude(String latitude) {
    this.latitude = latitude;
  }

  public void setStat(int stat) {
    this.stat = stat;
  }

  /**
   * @return the enabled
   */
  @Column(name = "ENABLED")
  public int getEnabled() {
    return enabled;
  }

  /**
   * @param enabled the enabled to set
   */
  public void setEnabled(int enabled) {
    this.enabled = enabled;
  }

  @Column(name = "ISIS_ORG_CODE")
  public Integer getIsisOrgCode() {
    return isisOrgCode;
  }

  @Transient
  public String getFaxAttachmentPath() {
    return faxAttachmentPath;
  }

  public void setFaxAttachmentPath(String faxAttachmentPath) {
    this.faxAttachmentPath = faxAttachmentPath;
  }

  public void setIsisOrgCode(Integer isisOrgCode) {
    this.isisOrgCode = isisOrgCode;
  }

  @Transient
  public InsSource getInsSource() {
    return insSource;
  }

  public void setInsSource(InsSource insSource) {
    this.insSource = insSource;
  }

  @Transient
  public String getLogoPath() {
    return logoPath;
  }

  public void setLogoPath(String logoPath) {
    this.logoPath = logoPath;
  }

  @Transient
  public Date getAppDate() {
    return appDate;
  }

  public void setAppDate(Date appDate) {
    this.appDate = appDate;
  }

}
