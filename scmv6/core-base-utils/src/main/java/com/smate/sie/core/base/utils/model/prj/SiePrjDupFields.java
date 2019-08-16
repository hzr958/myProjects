package com.smate.sie.core.base.utils.model.prj;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 字段查重表
 * 
 * @author 叶星源
 * 
 */

@Entity
@Table(name = "PRJ_DUP_FIELDS")
public class SiePrjDupFields {

  // 0:删除 1：未删除 4：单位未确认的
  public final static Integer DELETE_STATUS = 0;
  public final static Integer NORMAL_STATUS = 1;
  public final static Integer INS_NOT_CONFIRM_STATUS = 4;
  // 项目ID
  @Id
  @Column(name = "PRJ_ID")
  private Long prjId;
  // 所在单位
  @Column(name = "OWNER_INS_ID")
  private Long ownerInsId;
  // "资助机构定义的项目编号
  @Column(name = "PRJ_EXTER_NO")
  private String externalNo;
  // 中文标题hash_code，查重时使用
  @Column(name = "ZH_TITLE_HASH")
  private Integer zhTitleHash;
  // 英文标题hash_code，查重时使用
  @Column(name = "EN_TITLE_HASH")
  private Integer enTitleHash;
  // 项目来源NAME
  @Column(name = "PRJ_FROM_NAME")
  private String prjFromName;
  // ZH_TITLE+PRJ_EXTER_NO字符串hash
  @Column(name = "ZTEFINGER_PRINT")
  private Integer ztefigerPrint;
  // ZH_TITLE+PRJ_FROM_NAME字符串hash
  @Column(name = "ZTFFINGER_PRINT")
  private Integer zeffigerPrint;
  // EN_TITLE + PRJ_EXTER_NO的字符串hash
  @Column(name = "ETEFINGER_PRINT")
  private Integer eteFigerPrint;
  // EN_TITLE + PRJ_FROM_NAME的字符串hash
  @Column(name = "ETFFINGER_PRINT")
  private Integer etfFigerPrint;

  public Long getPrjId() {
    return prjId;
  }

  public void setPrjId(Long prjId) {
    this.prjId = prjId;
  }

  public String getExternalNo() {
    return externalNo;
  }

  public void setExternalNo(String externalNo) {
    this.externalNo = externalNo;
  }

  public Integer getZhTitleHash() {
    return zhTitleHash;
  }

  public void setZhTitleHash(Integer zhTitleHash) {
    this.zhTitleHash = zhTitleHash;
  }

  public Integer getEnTitleHash() {
    return enTitleHash;
  }

  public void setEnTitleHash(Integer enTitleHash) {
    this.enTitleHash = enTitleHash;
  }

  public String getPrjFromName() {
    return prjFromName;
  }

  public void setPrjFromName(String prjFromName) {
    this.prjFromName = prjFromName;
  }

  public Long getOwnerInsId() {
    return ownerInsId;
  }

  public void setOwnerInsId(Long ownerInsId) {
    this.ownerInsId = ownerInsId;
  }

  public Integer getZtefigerPrint() {
    return ztefigerPrint;
  }

  public void setZtefigerPrint(Integer ztefigerPrint) {
    this.ztefigerPrint = ztefigerPrint;
  }

  public Integer getZeffigerPrint() {
    return zeffigerPrint;
  }

  public void setZeffigerPrint(Integer zeffigerPrint) {
    this.zeffigerPrint = zeffigerPrint;
  }

  public Integer getEteFigerPrint() {
    return eteFigerPrint;
  }

  public void setEteFigerPrint(Integer eteFigerPrint) {
    this.eteFigerPrint = eteFigerPrint;
  }

  public Integer getEtfFigerPrint() {
    return etfFigerPrint;
  }

  public void setEtfFigerPrint(Integer etfFigerPrint) {
    this.etfFigerPrint = etfFigerPrint;
  }

}
