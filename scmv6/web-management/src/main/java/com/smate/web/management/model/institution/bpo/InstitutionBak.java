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

/**
 * 单位管理修改备份表
 * 
 * @author zjh
 *
 */
@Entity
@Table(name = "INSTITUTION_EDIT_HISTORY")
public class InstitutionBak implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = -5267109051213525768L;

  private Long id;
  private Long insId;
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
  // 单位性质单位性质1: college; 2: research center; 3: funding agency;99: others
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
  // 是否允许单位在下拉框显示 0-否 1-是
  private int enabled;
  // 单位传真路径
  private String faxAttachmentPath;
  // 备份时间
  private Date backupDate;
  // 操作人
  private Long optPsnId;

  public InstitutionBak() {
    super();
  }

  @Id
  @Column(name = "ID")
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_INSTITUTION_EDIT_HISTORY", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  @Column(name = "INS_ID")
  public Long getInsId() {
    return insId;
  }

  public void setInsId(Long insId) {
    this.insId = insId;
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

  @Column(name = "ABBR")
  public String getAbbreviation() {
    return abbreviation;
  }

  public void setAbbreviation(String abbreviation) {
    this.abbreviation = abbreviation;
  }

  @Column(name = "CONTACT_PERSON")
  public String getContactPerson() {
    return contactPerson;
  }

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

  @Column(name = "TEL")
  public String getTel() {
    return tel;
  }

  public void setTel(String tel) {
    this.tel = tel;
  }

  @Column(name = "URL")
  public String getUrl() {
    return url;
  }

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

  @Column(name = "ENABLED")
  public int getEnabled() {
    return enabled;
  }

  public void setEnabled(int enabled) {
    this.enabled = enabled;
  }

  @Column(name = "ISIS_ORG_CODE")
  public Integer getIsisOrgCode() {
    return isisOrgCode;
  }

  @Column(name = "FAX_ATTACHMENT_PATH")
  public String getFaxAttachmentPath() {
    return faxAttachmentPath;
  }

  public void setFaxAttachmentPath(String faxAttachmentPath) {
    this.faxAttachmentPath = faxAttachmentPath;
  }

  public void setIsisOrgCode(Integer isisOrgCode) {
    this.isisOrgCode = isisOrgCode;
  }

  @Column(name = "BACKUP_DATE")
  public Date getBackupDate() {
    return backupDate;
  }

  public void setBackupDate(Date backupDate) {
    this.backupDate = backupDate;
  }

  @Column(name = "OPT_PSN_ID")
  public Long getOptPsnId() {
    return optPsnId;
  }

  public void setOptPsnId(Long optPsnId) {
    this.optPsnId = optPsnId;
  }
}
