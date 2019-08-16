package com.smate.web.psn.model.cooperation;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.smate.core.base.utils.string.ServiceUtil;

@Entity
@Table(name = "PSN_KNOW_COPARTNER")
public class PsnKnowCopartner implements Serializable {

  private static final long serialVersionUID = 380030339476389495L;
  // pk
  private Long id;
  // 个人psnId
  private Long psnId;
  // 合作者psnId
  private Long cptPsnId;
  private String des3CptPsnId;
  // 合作者姓名
  private String cptName;
  private String cptFirstName;
  private String cptLastName;
  // 合作者头像url
  private String cptHeadUrl;
  // 合作者头衔
  private String cptViewTitel;
  // 合作类型：4成果合作，5项目合作
  private String cptTypes;
  // 成果合作得分
  private Double pubScore;
  // 项目合作得分
  private Double prjScore;
  // 成果合作次数
  private Integer pubCount;
  // 成果合作次数
  private Integer prjCount;

  private Integer isFriend;

  private String cptPsnViewName;// 合作者名称(显示用)_MJG_SCM-5707.

  public PsnKnowCopartner() {
    super();
  }

  public PsnKnowCopartner(Long cptPsnId, Integer isFriend) {
    super();
    this.cptPsnId = cptPsnId;
    this.isFriend = isFriend;
  }

  public PsnKnowCopartner(Long psnId, Long cptPsnId, Integer pubCount, Integer prjCount) {
    super();
    this.psnId = psnId;
    this.cptPsnId = cptPsnId;
    this.pubCount = pubCount;
    this.prjCount = prjCount;
  }

  @Id
  @Column(name = "ID")
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_PSN_KNOW_COPARTNER", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  @Column(name = "PSN_ID")
  public Long getPsnId() {
    return psnId;
  }

  public void setPsnId(Long psnId) {
    this.psnId = psnId;
  }

  @Column(name = "COPARTNER_ID")
  public Long getCptPsnId() {
    return cptPsnId;
  }

  public void setCptPsnId(Long cptPsnId) {
    this.cptPsnId = cptPsnId;
  }

  @Transient
  public String getDes3CptPsnId() {
    if (cptPsnId != null && des3CptPsnId == null) {
      this.des3CptPsnId = ServiceUtil.encodeToDes3(this.cptPsnId.toString());
    }
    return des3CptPsnId;
  }

  public void setDes3CptPsnId(String des3CptPsnId) {
    this.des3CptPsnId = des3CptPsnId;
  }

  @Column(name = "COPARTNER_NAME")
  public String getCptName() {
    return cptName;
  }

  public void setCptName(String cptName) {
    this.cptName = cptName;
  }

  @Column(name = "COPARTNER_FIRST_NAME")
  public String getCptFirstName() {
    return cptFirstName;
  }

  public void setCptFirstName(String cptFirstName) {
    this.cptFirstName = cptFirstName;
  }

  @Column(name = "COPARTNER_LAST_NAME")
  public String getCptLastName() {
    return cptLastName;
  }

  public void setCptLastName(String cptLastName) {
    this.cptLastName = cptLastName;
  }

  @Column(name = "COPARTNER_HEAD_URL")
  public String getCptHeadUrl() {
    return cptHeadUrl;
  }

  public void setCptHeadUrl(String cptHeadUrl) {
    this.cptHeadUrl = cptHeadUrl;
  }

  @Column(name = "COPARTNER_VIEWTITLE")
  public String getCptViewTitel() {
    return cptViewTitel;
  }

  public void setCptViewTitel(String cptViewTitel) {
    this.cptViewTitel = cptViewTitel;
  }

  @Column(name = "COPARTNER_TYPES")
  public String getCptTypes() {
    return cptTypes;
  }

  public void setCptTypes(String cptTypes) {
    this.cptTypes = cptTypes;
  }

  @Column(name = "PUB_SCORE")
  public Double getPubScore() {
    return pubScore;
  }

  public void setPubScore(Double pubScore) {
    this.pubScore = pubScore;
  }

  @Column(name = "PRJ_SCORE")
  public Double getPrjScore() {
    return prjScore;
  }

  public void setPrjScore(Double prjScore) {
    this.prjScore = prjScore;
  }

  @Column(name = "IS_FRIEND")
  public Integer getIsFriend() {
    return isFriend;
  }

  public void setIsFriend(Integer isFriend) {
    this.isFriend = isFriend;
  }

  @Column(name = "PUB_COUNT")
  public Integer getPubCount() {
    return pubCount;
  }

  public void setPubCount(Integer pubCount) {
    this.pubCount = pubCount;
  }

  @Column(name = "PRJ_COUNT")
  public Integer getPrjCount() {
    return prjCount;
  }

  public void setPrjCount(Integer prjCount) {
    this.prjCount = prjCount;
  }

  @Transient
  public String getCptPsnViewName() {
    return cptPsnViewName;
  }

  public void setCptPsnViewName(String cptPsnViewName) {
    this.cptPsnViewName = cptPsnViewName;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

}
