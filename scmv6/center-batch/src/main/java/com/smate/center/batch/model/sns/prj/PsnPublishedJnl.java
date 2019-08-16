package com.smate.center.batch.model.sns.prj;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * 应用-期刊推荐.
 * 
 * @author cwli
 * 
 */
@Entity
@Table(name = "PSN_APP_JNL_PUBLISHED")
public class PsnPublishedJnl implements Serializable {


  /**
   * 
   */
  private static final long serialVersionUID = 5347155776150257558L;
  private Long id;
  private Long psnId;
  private Long jnlId;
  // 期刊名称
  // 期刊名称
  private String jnlTitileEn;
  private String jnlTitleXx;
  // 期刊链接
  private String jnlUrl;
  // 质量指标
  private String quality;
  // 关键词
  private String keywordView;
  private String keywordAll;
  // 发表次数
  private Integer times;
  /** 收录. */
  private String dbCodes;
  /** 影响因子. */
  private Double impactFactors;
  /** 影响因子年份. */
  private String ifYear;

  private String viewCodes;

  @Id
  @Column(name = "ID")
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_PSN_APP_JNL_PUBLISHED", allocationSize = 1)
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

  @Column(name = "JNL_ID")
  public Long getJnlId() {
    return jnlId;
  }

  public void setJnlId(Long jnlId) {
    this.jnlId = jnlId;
  }

  @Column(name = "JNL_TITLE_EN")
  public String getJnlTitileEn() {
    return jnlTitileEn;
  }

  public void setJnlTitileEn(String jnlTitileEn) {
    this.jnlTitileEn = jnlTitileEn;
  }

  @Column(name = "JNL_TITLE_XX")
  public String getJnlTitleXx() {
    return jnlTitleXx;
  }

  public void setJnlTitleXx(String jnlTitleXx) {
    this.jnlTitleXx = jnlTitleXx;
  }

  @Column(name = "JNL_URL")
  public String getJnlUrl() {
    return jnlUrl;
  }

  public void setJnlUrl(String jnlUrl) {
    this.jnlUrl = jnlUrl;
  }

  @Column(name = "QUALITY")
  public String getQuality() {
    return quality;
  }

  public void setQuality(String quality) {
    this.quality = quality;
  }

  @Column(name = "KEYWORD_VIEW")
  public String getKeywordView() {
    return keywordView;
  }

  public void setKeywordView(String keywordView) {
    this.keywordView = keywordView;
  }

  @Transient
  public String getKeywordAll() {
    return keywordAll;
  }

  public void setKeywordAll(String keywordAll) {
    this.keywordAll = keywordAll;
  }

  @Column(name = "TIMES")
  public Integer getTimes() {
    return times;
  }

  public void setTimes(Integer times) {
    this.times = times;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

  @Column(name = "DBCODES")
  public String getDbCodes() {
    return dbCodes;
  }

  public void setDbCodes(String dbCodes) {
    this.dbCodes = dbCodes;
  }

  @Transient
  public String getViewCodes() {
    if (StringUtils.isNotBlank(dbCodes)) {
      viewCodes = dbCodes.substring(1, dbCodes.length() - 1).trim();
    }
    return viewCodes;
  }

  public void setViewCodes(String viewCodes) {
    this.viewCodes = viewCodes;
  }

  @Column(name = "IMPACT_FACTORS")
  public Double getImpactFactors() {
    return impactFactors;
  }

  public void setImpactFactors(Double impactFactors) {
    this.impactFactors = impactFactors;
  }

  @Column(name = "IF_YEAR")
  public String getIfYear() {
    return ifYear;
  }

  public void setIfYear(String ifYear) {
    this.ifYear = ifYear;
  }
}
