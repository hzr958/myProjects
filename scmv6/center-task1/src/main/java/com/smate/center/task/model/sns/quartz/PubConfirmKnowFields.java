package com.smate.center.task.model.sns.quartz;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 成果确认数据冗余表--只有部分字段，用于好友推荐的成果合作者.
 * 
 * @author Administrator
 *
 */
@Entity
@Table(name = "PUB_CONFIRM_KNOW_FIELDS")
public class PubConfirmKnowFields implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = -5917465605804683397L;

  // 主键
  private Long dtId;
  // 人员id
  private Long psnId;
  // 成果编号
  private Long rolRubId;
  // 成果类型 const_pub_type
  private Integer pubType;
  // 中文标题hash_code
  private Integer zhTitleHash;
  // 英文标题hash_code
  private Integer enTitleHash;

  public PubConfirmKnowFields() {
    super();
  }

  public PubConfirmKnowFields(Long dtId, Long psnId, Long rolRubId, Integer pubType, Integer zhTitleHash,
      Integer enTitleHash) {
    super();
    this.dtId = dtId;
    this.psnId = psnId;
    this.rolRubId = rolRubId;
    this.pubType = pubType;
    this.zhTitleHash = zhTitleHash;
    this.enTitleHash = enTitleHash;
  }

  @Id
  @Column(name = "DT_ID")
  public Long getDtId() {
    return dtId;
  }

  @Column(name = "PSN_ID")
  public Long getPsnId() {
    return psnId;
  }

  @Column(name = "ROL_PUB_ID")
  public Long getRolRubId() {
    return rolRubId;
  }

  @Column(name = "PUB_TYPE")
  public Integer getPubType() {
    return pubType;
  }

  @Column(name = "ZH_TITLE_HASH")
  public Integer getZhTitleHash() {
    return zhTitleHash;
  }

  @Column(name = "EN_TITLE_HASH")
  public Integer getEnTitleHash() {
    return enTitleHash;
  }

  public void setDtId(Long dtId) {
    this.dtId = dtId;
  }

  public void setPsnId(Long psnId) {
    this.psnId = psnId;
  }

  public void setRolRubId(Long rolRubId) {
    this.rolRubId = rolRubId;
  }

  public void setPubType(Integer pubType) {
    this.pubType = pubType;
  }

  public void setZhTitleHash(Integer zhTitleHash) {
    this.zhTitleHash = zhTitleHash;
  }

  public void setEnTitleHash(Integer enTitleHash) {
    this.enTitleHash = enTitleHash;
  }


}
