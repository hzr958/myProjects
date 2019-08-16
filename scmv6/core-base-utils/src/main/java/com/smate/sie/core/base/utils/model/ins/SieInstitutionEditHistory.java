package com.smate.sie.core.base.utils.model.ins;

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

import com.smate.core.base.utils.model.consts.Sie6Institution;
import com.smate.core.base.utils.security.SecurityUtils;

/**
 * 单位备份表
 * 
 * @author hd
 *
 */
@Entity
@Table(name = "INSTITUTION_EDIT_HISTORY")
public class SieInstitutionEditHistory implements Serializable {

  /**
     * 
     */
  private static final long serialVersionUID = -5124100640789398622L;
  private Long id;
  private Long insId;
  @SuppressWarnings("unused")
  private String insName;
  // 中文名
  private String zhName;
  // 英文名
  private String enName;
  // 名称缩写
  private String abbr;
  // 联系人
  private String contactPsn;
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
  private String checkMails;
  private String postcode;
  // 备份时间
  private Date backupDate;
  // 操作人
  private Long optPsnId;
  // 传真
  private String fox;
  // 机构开通方式
  private Integer dataFrom;
  // 是否用于自动提示0否1是，个人版模糊匹配用
  private Integer autoComplete;
  // 手机号
  private String mobile;
  // 单位简介
  private String briefDesc;
  // 单位服务邮箱
  private String contactEmail;
  // 统一社会信用代码
  private String uniformId1;
  // 统一社会信用代码
  private String uniformId2;

  public SieInstitutionEditHistory() {
    super();
  }


  public SieInstitutionEditHistory(Sie6Institution ins) {
    super();
    this.insId = ins.getId();
    this.zhName = ins.getZhName();
    this.enName = ins.getEnName();
    this.abbr = ins.getAbbr();
    this.contactPsn = ins.getContactPerson();
    this.tel = ins.getTel();
    this.serverEmail = ins.getServerEmail();
    this.url = ins.getUrl();
    this.status = ins.getStatus();
    this.nature = ins.getNature();
    this.checkMails = ins.getCheckEmails();
    this.zhAddress = ins.getZhAddress();
    this.enAddress = ins.getEnAddress();
    this.postcode = ins.getPostcode();
    this.fox = ins.getFox();
    this.uniformId1 = ins.getUniformId1();
    this.uniformId2 = ins.getUniformId2();
    this.serverTel = ins.getServerTel();
    this.dataFrom = ins.getDataFrom();
    this.mobile = ins.getMobile();
    this.briefDesc = ins.getBriefDesc();
    this.contactEmail = ins.getContactEmail();
    this.autoComplete = ins.getAutoComplete();
    this.backupDate = new Date();
    this.optPsnId = SecurityUtils.getCurrentUserId();
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
  public String getAbbr() {
    return abbr;
  }

  public void setAbbr(String abbr) {
    this.abbr = abbr;
  }

  @Column(name = "CONTACT_PERSON")
  public String getContactPsn() {
    return contactPsn;
  }

  public void setContactPsn(String contactPsn) {
    this.contactPsn = contactPsn;
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

  @Column(name = "CHECK_EMAILS")
  public String getCheckMails() {
    return checkMails;
  }

  public void setCheckMails(String checkMails) {
    this.checkMails = checkMails;
  }

  @Column(name = "FOX")
  public String getFox() {
    return fox;
  }

  public void setFox(String fox) {
    this.fox = fox;
  }

  @Column(name = "DATA_FROM")
  public Integer getDataFrom() {
    return dataFrom;
  }

  public void setDataFrom(Integer dataFrom) {
    this.dataFrom = dataFrom;
  }

  @Column(name = "AUTO_COMPLETE")
  public Integer getAutoComplete() {
    return autoComplete;
  }

  public void setAutoComplete(Integer autoComplete) {
    this.autoComplete = autoComplete;
  }

  @Column(name = "MOBILE")
  public String getMobile() {
    return mobile;
  }

  public void setMobile(String mobile) {
    this.mobile = mobile;
  }

  @Column(name = "BRIEF_DESC")
  public String getBriefDesc() {
    return briefDesc;
  }

  public void setBriefDesc(String briefDesc) {
    this.briefDesc = briefDesc;
  }

  @Column(name = "CONTACT_EMAIL")
  public String getContactEmail() {
    return contactEmail;
  }

  public void setContactEmail(String contactEmail) {
    this.contactEmail = contactEmail;
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

}
