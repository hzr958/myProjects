package com.smate.center.batch.model.sns.pub;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * 个人信息各节点同步表.
 * 
 * 
 */
@Entity
@Table(name = "PERSON_SYNC")
public class SyncPerson implements Serializable {
  private static final long serialVersionUID = -8211517470770614831L;
  private Long psnId;
  private Long psnNode;
  private String psnName;
  private String firstLetter;
  private String psnFirstName;
  private String psnLastName;
  private String psnOtherName;
  private String psnTel;
  private String psnMobile;
  private String psnQQ;
  private String psnMsn;
  private String psnEmail;
  private String psnHeadUrl;
  private String psnTitle;
  private String psnInsName;
  private String des3PsnId;
  private String psnInsIdList;
  private String psnInsNameList;
  private Long regionId;
  private Long psnPrjNum;
  private Long psnPubNum;
  private Long psnISI;
  private Long hindex;
  private String psnDiscipline;
  private String psnSkype;

  public SyncPerson() {
    super();
  }

  public SyncPerson(Long psnId, String psnInsIdList, String psnInsNameList, Long regionId) {
    super();
    this.psnId = psnId;
    this.psnInsIdList = psnInsIdList;
    this.psnInsNameList = psnInsNameList;
    this.regionId = regionId;
  }

  @Id
  @Column(name = "SYNC_PSN_ID")
  public Long getPsnId() {
    return psnId;
  }

  public void setPsnId(Long psnId) {
    this.psnId = psnId;
  }

  @Column(name = "PSN_NODE")
  public Long getPsnNode() {
    return psnNode;
  }

  public void setPsnNode(Long psnNode) {
    this.psnNode = psnNode;
  }

  @Column(name = "PSN_NAME")
  public String getPsnName() {
    return psnName;
  }

  public void setPsnName(String psnName) {
    this.psnName = psnName;
  }

  @Column(name = "PSN_NAME_FIRST_LETTER")
  public String getFirstLetter() {
    return firstLetter;
  }

  public void setFirstLetter(String firstLetter) {
    this.firstLetter = firstLetter;
  }

  @Column(name = "PSN_FIRST_NAME")
  public String getPsnFirstName() {
    return psnFirstName;
  }

  public void setPsnFirstName(String psnFirstName) {
    this.psnFirstName = psnFirstName;
  }

  @Column(name = "PSN_LAST_NAME")
  public String getPsnLastName() {
    return psnLastName;
  }

  public void setPsnLastName(String psnLastName) {
    this.psnLastName = psnLastName;
  }

  @Column(name = "PSN_TEL")
  public String getPsnTel() {
    return psnTel;
  }

  public void setPsnTel(String psnTel) {
    this.psnTel = psnTel;
  }

  @Column(name = "PSN_MOBILE")
  public String getPsnMobile() {
    return psnMobile;
  }

  public void setPsnMobile(String psnMobile) {
    this.psnMobile = psnMobile;
  }

  @Column(name = "PSN_QQ")
  public String getPsnQQ() {
    return psnQQ;
  }

  public void setPsnQQ(String psnQQ) {
    this.psnQQ = psnQQ;
  }

  @Column(name = "PSN_MSN")
  public String getPsnMsn() {
    return psnMsn;
  }

  public void setPsnMsn(String psnMsn) {
    this.psnMsn = psnMsn;
  }

  @Column(name = "PSN_EMAIL")
  public String getPsnEmail() {
    return psnEmail;
  }

  public void setPsnEmail(String psnEmail) {
    this.psnEmail = psnEmail;
  }

  @Column(name = "PSN_SKYPE")
  public String getPsnSkype() {
    return psnSkype;
  }

  public void setPsnSkype(String psnSkype) {
    this.psnSkype = psnSkype;
  }

  @Column(name = "PSN_HEAD_URL")
  public String getPsnHeadUrl() {
    return psnHeadUrl;
  }

  public void setPsnHeadUrl(String psnHeadUrl) {
    this.psnHeadUrl = psnHeadUrl;
  }

  @Column(name = "PSN_TITOLO")
  public String getPsnTitle() {
    return psnTitle;
  }

  public void setPsnTitle(String psnTitle) {
    this.psnTitle = psnTitle;
  }

  @Column(name = "PSN_OTHER_NAME")
  public String getPsnOtherName() {
    return psnOtherName;
  }

  public void setPsnOtherName(String psnOtherName) {
    this.psnOtherName = psnOtherName;
  }

  @Column(name = "PSN_INS_NAME")
  public String getPsnInsName() {
    return psnInsName;
  }

  public void setPsnInsName(String psnInsName) {
    this.psnInsName = psnInsName;
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

  @Column(name = "PSN_INS_ID_LIST")
  public String getPsnInsIdList() {
    return psnInsIdList;
  }

  public void setPsnInsIdList(String psnInsIdList) {
    this.psnInsIdList = psnInsIdList;
  }

  @Column(name = "PSN_INS_NAME_LIST")
  public String getPsnInsNameList() {
    return psnInsNameList;
  }

  public void setPsnInsNameList(String psnInsNameList) {
    this.psnInsNameList = psnInsNameList;
  }

  @Column(name = "PSN_REGION_ID")
  public Long getRegionId() {
    return regionId;
  }

  public void setRegionId(Long regionId) {
    this.regionId = regionId;
  }

  @Column(name = "PSN_PRJ_NUM")
  public Long getPsnPrjNum() {
    return psnPrjNum;
  }

  public void setPsnPrjNum(Long psnPrjNum) {
    this.psnPrjNum = psnPrjNum;
  }

  @Column(name = "PSN_PUB_NUM")
  public Long getPsnPubNum() {
    return psnPubNum;
  }

  public void setPsnPubNum(Long psnPubNum) {
    this.psnPubNum = psnPubNum;
  }

  @Column(name = "PSN_ISI")
  public Long getPsnISI() {
    return psnISI;
  }

  public void setPsnISI(Long psnISI) {
    this.psnISI = psnISI;
  }

  @Column(name = "PSN_H_INDEX")
  public Long getHindex() {
    return hindex;
  }

  public void setHindex(Long hindex) {
    this.hindex = hindex;
  }

  @Column(name = "PSN_DISCIPLINE")
  public String getPsnDiscipline() {
    return psnDiscipline;
  }

  public void setPsnDiscipline(String psnDiscipline) {
    this.psnDiscipline = psnDiscipline;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

}
