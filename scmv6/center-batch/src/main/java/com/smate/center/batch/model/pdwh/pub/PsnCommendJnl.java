package com.smate.center.batch.model.pdwh.pub;

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
@Table(name = "PSN_APP_JNL")
public class PsnCommendJnl implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = -4009175218073997927L;
  private Long id;
  private Long psnId;
  private Long jnlId;
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
  // 推荐度
  private Integer degrees;

  // 期刊质量
  private String qa;
  private String difficulty;
  private String cycle;
  private Integer jctPsnCount;
  /** 收录. */
  private String dbCodes;
  /** 影响因子. */
  private Double impactFactors;
  /** 影响因子年份. */
  private String ifYear;

  private String viewCodes;
  private Integer intQuality;
  private double doubleQuality;
  private Integer intDifficulty;
  private double doubleDifficulty;

  @Id
  @Column(name = "ID")
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_PSN_APP_JNL", allocationSize = 1)
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

  @Column(name = "DEGREES")
  public Integer getDegrees() {
    return degrees;
  }

  public void setDegrees(Integer degrees) {
    this.degrees = degrees;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

  @Column(name = "JNL_ID")
  public Long getJnlId() {
    return jnlId;
  }

  public void setJnlId(Long jnlId) {
    this.jnlId = jnlId;
  }

  @Transient
  public String getQa() {
    return qa;
  }

  public void setQa(String qa) {
    this.qa = qa;
  }

  @Transient
  public String getDifficulty() {
    return difficulty;
  }

  public void setDifficulty(String difficulty) {
    this.difficulty = difficulty;
  }

  @Transient
  public String getCycle() {
    return cycle;
  }

  public void setCycle(String cycle) {
    this.cycle = cycle;
  }

  @Transient
  public Integer getJctPsnCount() {
    return jctPsnCount;
  }

  public void setJctPsnCount(Integer jctPsnCount) {
    this.jctPsnCount = jctPsnCount;
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

  @Transient
  public Integer getIntDifficulty() {
    if (difficulty != null && difficulty.indexOf(".") > -1) {
      this.intDifficulty = Integer.parseInt(difficulty.substring(0, difficulty.indexOf(".")));
    } else if (difficulty != null) {
      this.intDifficulty = Integer.parseInt(difficulty);
    }
    return intDifficulty;
  }

  public void setIntDifficulty(Integer intDifficulty) {
    this.intDifficulty = intDifficulty;
  }

  @Transient
  public double getDoubleDifficulty() {
    if (difficulty != null && difficulty.indexOf(".") > -1) {
      this.doubleDifficulty = Double.parseDouble(difficulty.substring(difficulty.indexOf("."), difficulty.length()));
    } else {
      this.doubleDifficulty = 0.0;
    }
    return doubleDifficulty;
  }

  public void setDoubleDifficulty(double doubleDifficulty) {
    this.doubleDifficulty = doubleDifficulty;
  }

  @Transient
  public Integer getIntQuality() {
    if (qa != null && qa.indexOf(".") > -1) {
      this.intQuality = Integer.parseInt(qa.substring(0, qa.indexOf(".")));
    } else if (qa != null) {
      this.intQuality = Integer.parseInt(qa);
    } else {
      this.intQuality = 0;
    }
    return intQuality;
  }

  public void setIntQuality(Integer intQuality) {
    this.intQuality = intQuality;
  }

  @Transient
  public double getDoubleQuality() {
    if (qa != null && qa.indexOf(".") > -1) {
      this.doubleQuality = Double.parseDouble(qa.substring(qa.indexOf("."), qa.length()));
    } else {
      this.doubleQuality = 0.0;
    }
    return doubleQuality;
  }

  public void setDoubleQuality(double doubleQuality) {
    this.doubleQuality = doubleQuality;
  }
}
