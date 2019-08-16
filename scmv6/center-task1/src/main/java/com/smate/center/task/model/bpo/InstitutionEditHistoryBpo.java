package com.smate.center.task.model.bpo;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.smate.core.base.utils.security.SecurityUtils;

/**
 * 单位备份表
 * 
 * @author hd
 * 
 */
@Entity
@Table(name = "INSTITUTION_EDIT_HISTORY")
public class InstitutionEditHistoryBpo implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = -1821225572291875531L;
  // 主健
  private Long id;
  // 单位id
  private Long insId;
  // 中文名
  private String zhName;
  // 英文名
  private String enName;
  // 名称缩写
  private String abbreviation;
  // 联系人
  private String contactPerson;
  // 联系电话
  private String tel;
  // 单位服务邮箱
  private String serverEmail;
  // 单位网址
  private String url;


  // 单位状态， 0未开始使用,1:注册,2:审核通过,9:删除
  private Long status;
  // 单位性质单位性质1: college; 2: research center; 3: funding agency;99: others
  private Long nature;
  private String checkEmails;
  // 中文地址
  private String zhAddress;
  // 英文地址
  private String enAddress;
  // 单位服务电话
  private String serverTel;
  // 邮编
  private String postcode;
  // 和ISIS系统对应的单位Code，同步数据需以此Code为准
  private Integer isisOrgCode;
  // 组织结构代码, 用于验证单位用户注册或者其他功能
  private String checkCode;
  // 单位用户注册时是否检查组织验证代码0/1
  private Integer isCheckCode;
  private Integer enable;
  private String faxAttachPath;

  private Date backupDate;
  private Long optPsnId;



  public InstitutionEditHistoryBpo() {
    super();
  }

  public InstitutionEditHistoryBpo(InstitutionBpo ins) {
    super();
    this.insId = ins.getId();
    this.zhName = ins.getZhName();
    this.enName = ins.getEnName();
    this.abbreviation = ins.getAbbreviation();
    this.contactPerson = ins.getContactPerson();
    this.tel = ins.getTel();
    this.serverEmail = ins.getServerEmail();
    this.url = ins.getUrl();
    this.status = ins.getStatus();
    this.nature = ins.getNature();
    this.checkEmails = ins.getCheckEmails();
    this.zhAddress = ins.getZhAddress();
    this.enAddress = ins.getEnAddress();
    this.serverTel = ins.getServerTel();
    this.postcode = ins.getPostcode();
    this.isisOrgCode = ins.getIsisOrgCode();
    this.checkCode = ins.getCheckCode();
    this.isCheckCode = ins.getIsCheckCode();
    this.enable = ins.getEnable();
    this.backupDate = new Date();
    this.optPsnId = SecurityUtils.getCurrentUserId();
  }


  @Id
  @Column(name = "Id")
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_INSTITUTION_EDIT_HISTORY", allocationSize = 1)
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

  @Column(name = "CHECK_EMAILS")
  public String getCheckEmails() {
    return checkEmails;
  }

  public void setCheckEmails(String checkEmails) {
    this.checkEmails = checkEmails;
  }

  /**
   * @param url
   */
  public void setUrl(String url) {
    this.url = url;
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

  @Column(name = "CHECK_CODE")
  public String getCheckCode() {
    return checkCode;
  }

  @Column(name = "IS_CHECKCODE")
  public Integer getIsCheckCode() {
    return isCheckCode;
  }

  @Column(name = "ISIS_ORG_CODE")
  public Integer getIsisOrgCode() {
    return isisOrgCode;
  }

  public void setCheckCode(String checkCode) {
    this.checkCode = checkCode;
  }

  public void setIsCheckCode(Integer isCheckCode) {
    this.isCheckCode = isCheckCode;
  }

  public void setIsisOrgCode(Integer isisOrgCode) {
    this.isisOrgCode = isisOrgCode;
  }

  public void setPostcode(String postcode) {
    this.postcode = postcode;
  }


  @Column(name = "INS_ID")
  public Long getInsId() {
    return insId;
  }


  public void setInsId(Long insId) {
    this.insId = insId;
  }

  @Column(name = "ENABLED")
  public Integer getEnable() {
    return enable;
  }


  public void setEnable(Integer enable) {
    this.enable = enable;
  }

  @Column(name = "FAX_ATTACHMENT_PATH")
  public String getFaxAttachPath() {
    return faxAttachPath;
  }


  public void setFaxAttachPath(String faxAttachPath) {
    this.faxAttachPath = faxAttachPath;
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
