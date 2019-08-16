package com.smate.sie.center.task.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 专利查重数据.
 * 
 * @author jszhou
 */
@Entity
@Table(name = "PAT_DUP_FIELDS")
public class SiePatDupFields implements Serializable {

  // 1:删除 0:未删除
  public final static Integer DELETE_STATUS = 1;
  public final static Integer NORMAL_STATUS = 0;
  public final static Integer INS_NOT_CONFIRM_STATUS = 4;
  /**
   * 
   */
  private static final long serialVersionUID = -8075498400960538309L;

  // 专利ID
  private Long patId;
  // 单位或人员ID
  private Long ownerId;
  // 成果对应的外部数据库 refrence to const_ref_db
  private Integer sourceDbId;
  // 中文标题hash_code，查重时使用，统一调用PublicationHash.titleCode(title)取得hash_code
  private Long zhTitleHash;
  // 英文标题hash_code，查重时使用 统一调用PublicationHash.titleCode(title) 取得hash_code
  private Long enTitleHash;
  // 成果所属 期// 专利号
  private String patentNo;
  // 专利号HASH
  private Long patentNoHash;
  // 状态 0:删除 1：未删除 4：单位未确认的
  private Integer status = 1;

  private Integer pubYear;

  public SiePatDupFields() {
    super();
  }

  public SiePatDupFields(Long patId, Integer sourceDbId) {
    super();
    this.patId = patId;
    this.sourceDbId = sourceDbId;
  }

  @Id
  @Column(name = "PAT_ID")
  public Long getPatId() {
    return patId;
  }

  @Column(name = "OWNER_ID")
  public Long getOwnerId() {
    return ownerId;
  }

  @Column(name = "SOURCE_DB_ID")
  public Integer getSourceDbId() {
    return sourceDbId;
  }

  @Column(name = "ZH_TITLE_HASH")
  public Long getZhTitleHash() {
    return zhTitleHash;
  }

  @Column(name = "EN_TITLE_HASH")
  public Long getEnTitleHash() {
    return enTitleHash;
  }

  @Column(name = "STATUS")
  public Integer getStatus() {
    return status;
  }

  @Column(name = "PATENT_NO")
  public String getPatentNo() {
    return patentNo;
  }

  @Column(name = "PATENT_NO_HASH")
  public Long getPatentNoHash() {
    return patentNoHash;
  }

  public void setStatus(Integer status) {
    this.status = status;
  }

  public void setPatId(Long patId) {
    this.patId = patId;
  }

  public void setOwnerId(Long ownerId) {
    this.ownerId = ownerId;
  }

  public void setSourceDbId(Integer sourceDbId) {
    this.sourceDbId = sourceDbId;
  }

  public void setZhTitleHash(Long zhTitleHash) {
    this.zhTitleHash = zhTitleHash;
  }

  public void setEnTitleHash(Long enTitleHash) {
    this.enTitleHash = enTitleHash;
  }

  public void setPatentNo(String patentNo) {
    this.patentNo = patentNo;
  }

  public void setPatentNoHash(Long patentNoHash) {
    this.patentNoHash = patentNoHash;
  }

  @Column(name = "PUB_YEAR")
  public Integer getPubYear() {
    return pubYear;
  }

  public void setPubYear(Integer pubYear) {
    this.pubYear = pubYear;
  }

}
