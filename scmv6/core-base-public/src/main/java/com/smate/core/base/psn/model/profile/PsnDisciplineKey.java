package com.smate.core.base.psn.model.profile;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.smate.core.base.utils.number.NumberUtils;
import com.smate.core.base.utils.security.Des3Utils;

/**
 * 个人熟悉的学科关键字.
 *
 * @author liqinghua
 *
 */
@Entity
@Table(name = "PSN_DISCIPLINE_KEY")
public class PsnDisciplineKey implements Serializable {

  /**
   *
   */
  private static final long serialVersionUID = 2467297195382733818L;

  private Long id;
  private Long keyId;
  private String keyWords;
  private Long pdId;// (废弃，改用psn_id)
  private Integer lanType; // 关键字语言类别
  private int refreshFlag;// 0:要刷新；1：已刷新
  private Long psnId;// lgk ,拆分学科领域与关键词
  private String des3PsnId;
  private Integer status;// zk,当前研究领域是有效,0无效（既删除的研究领域），１有效[兼容认同信息，用户保存的关键词记录不能直接删除，否则该关键词的认同信息就会失联]
  private Date updateDate;// 更新时间
  private boolean hasIdentified = false; // 已认同过
  private Long identificationSum; // 认同数
  private List<String> identifyAvatars; // 认同人员头像地址

  public PsnDisciplineKey() {
    super();
  }

  public PsnDisciplineKey(Long keyId, String keyWords, Long pdId, Integer lanType) {
    super();
    this.keyId = keyId;
    this.keyWords = keyWords;
    this.pdId = pdId;
    this.lanType = lanType;
  }

  public PsnDisciplineKey(Long id, String keyWords, Long psnId) {
    super();
    this.id = id;
    this.keyWords = keyWords;
    this.psnId = psnId;
  }

  public PsnDisciplineKey(String keyWords, Long psnId) {
    super();
    this.keyWords = keyWords;
    this.psnId = psnId;
  }

  public PsnDisciplineKey(String keyWords, Long psnId, Integer status) {
    super();
    this.keyWords = keyWords;
    this.psnId = psnId;
    this.status = status;
  }

  public PsnDisciplineKey(String keyWords, Long psnId, Integer status, Date updateDate) {
    super();
    this.keyWords = keyWords;
    this.psnId = psnId;
    this.status = status;
    this.updateDate = updateDate;
  }

  public PsnDisciplineKey(Long keyId, Long psnId, String keyWords, Integer status, Date updateDate) {
    super();
    this.keyId = keyId;
    this.keyWords = keyWords;
    this.psnId = psnId;
    this.status = status;
    this.updateDate = updateDate;
  }

  @Id
  @Column(name = "ID")
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_PSN_DISCIPLINE_KEY", allocationSize = 1)
  public Long getId() {
    return id;
  }

  @Column(name = "KEY_ID")
  public Long getKeyId() {
    return keyId;
  }

  @Column(name = "KEY_WORDS")
  public String getKeyWords() {
    return keyWords;
  }

  @Column(name = "PSNDIS_ID")
  public Long getPdId() {
    return pdId;
  }

  @Column(name = "LAN_TYPE")
  public Integer getLanType() {
    return lanType;
  }

  public void setLanType(Integer lanType) {
    this.lanType = lanType;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public void setKeyId(Long keyId) {
    this.keyId = keyId;
  }

  public void setKeyWords(String keyWords) {
    this.keyWords = keyWords;
  }

  public void setPdId(Long pdId) {
    this.pdId = pdId;
  }

  @Column(name = "refresh_flag")
  public int getRefreshFlag() {
    return refreshFlag;
  }

  public void setRefreshFlag(int refreshFlag) {
    this.refreshFlag = refreshFlag;
  }

  @Column(name = "PSN_ID")
  public Long getPsnId() {
    return psnId;
  }

  public void setPsnId(Long psnId) {
    this.psnId = psnId;
  }

  @Column(name = "STATUS")
  public Integer getStatus() {
    return status;
  }

  public void setStatus(Integer status) {
    this.status = status;
  }

  @Column(name = "UPDATE_DATE")
  public Date getUpdateDate() {
    return updateDate;
  }

  public void setUpdateDate(Date updateDate) {
    this.updateDate = updateDate;
  }

  @Transient
  public boolean getHasIdentified() {
    return hasIdentified;
  }

  public void setHasIdentified(boolean hasIdentified) {
    this.hasIdentified = hasIdentified;
  }

  @Transient
  public Long getIdentificationSum() {
    return identificationSum;
  }

  public void setIdentificationSum(Long identificationSum) {
    this.identificationSum = identificationSum;
  }

  @Transient
  public List<String> getIdentifyAvatars() {
    return identifyAvatars;
  }

  public void setIdentifyAvatars(List<String> identifyAvatars) {
    this.identifyAvatars = identifyAvatars;
  }

  @Transient
  public String getDes3PsnId() {
    if (!NumberUtils.isNullOrZero(psnId)) {
      des3PsnId = Des3Utils.encodeToDes3(psnId.toString());
    }
    return des3PsnId;
  }

  public void setDes3PsnId(String des3PsnId) {
    this.des3PsnId = des3PsnId;
  }
}
