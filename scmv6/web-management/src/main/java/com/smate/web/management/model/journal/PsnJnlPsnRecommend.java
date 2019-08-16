package com.smate.web.management.model.journal;

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

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * 论文->期刊推荐-给人员推荐期刊新算法.
 * 
 * @author lichangwen
 * 
 */
@Entity
@Table(name = "PSN_JNL_PSNREC_RECOMMEND")
public class PsnJnlPsnRecommend implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = -3215672429890939158L;
  private Long id;
  private Long psnId;
  private Long jnlId;
  private String des3JnlId;
  // 推荐度
  private Integer degrees;
  // 推荐得分
  private Double score;
  // 人员关键词匹配期刊关键词
  private String keywords;
  // 推荐时间
  private Date recDate;
  // --------冗余----------
  // 期刊名称
  private String jnlTitileEn;
  private String jnlTitleXx;
  private String titleView;

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

  public PsnJnlPsnRecommend() {
    super();
  }

  public PsnJnlPsnRecommend(Long psnId, Long jnlId, Integer degrees, String jnlTitileEn, String jnlTitleXx,
      String dbCodes, Double impactFactors, String ifYear, String keywords) {
    super();
    this.psnId = psnId;
    this.jnlId = jnlId;
    this.degrees = degrees;
    this.jnlTitileEn = jnlTitileEn;
    this.jnlTitleXx = jnlTitleXx;
    this.dbCodes = dbCodes;
    this.impactFactors = impactFactors;
    this.ifYear = ifYear;
    this.keywords = keywords;
  }

  @Id
  @Column(name = "ID")
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_PSN_JNL_PSN_RECOMMEND", allocationSize = 1)
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

  @Column(name = "DEGREES")
  public Integer getDegrees() {
    if (this.score != null) {
      degrees = score < 3 ? 3 : score.intValue() > 5 ? 5 : score.intValue();
    }
    return degrees;
  }

  public void setDegrees(Integer degrees) {
    this.degrees = degrees;
  }

  @Column(name = "SCORE")
  public Double getScore() {
    return score;
  }

  public void setScore(Double score) {
    this.score = score;
  }

  @Column(name = "KEYWORDS")
  public String getKeywords() {
    return keywords;
  }

  public void setKeywords(String keywords) {
    this.keywords = keywords;
  }

  @Column(name = "REC_DATE")
  public Date getRecDate() {
    return recDate;
  }

  public void setRecDate(Date recDate) {
    this.recDate = recDate;
  }

  @Transient
  public String getDes3JnlId() {
    return des3JnlId;
  }

  public void setDes3JnlId(String des3JnlId) {
    this.des3JnlId = des3JnlId;
  }

  @Transient
  public String getJnlTitileEn() {
    return jnlTitileEn;
  }

  public void setJnlTitileEn(String jnlTitileEn) {
    this.jnlTitileEn = jnlTitileEn;
  }

  @Transient
  public String getJnlTitleXx() {
    return jnlTitleXx;
  }

  public void setJnlTitleXx(String jnlTitleXx) {
    this.jnlTitleXx = jnlTitleXx;
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

  @Transient
  public String getDbCodes() {
    return dbCodes;
  }

  public void setDbCodes(String dbCodes) {
    this.dbCodes = dbCodes;
  }

  @Transient
  public String getViewCodes() {
    if (StringUtils.isNotBlank(dbCodes)) {
      if (dbCodes.endsWith(",")) {
        viewCodes = dbCodes.substring(1, dbCodes.length() - 1).trim();
      } else {
        viewCodes = dbCodes.substring(1, dbCodes.length()).trim();
      }
    }
    return viewCodes;
  }

  public void setViewCodes(String viewCodes) {
    this.viewCodes = viewCodes;
  }

  @Transient
  public Double getImpactFactors() {
    return impactFactors;
  }

  public void setImpactFactors(Double impactFactors) {
    this.impactFactors = impactFactors;
  }

  @Transient
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

  @Transient
  public String getTitleView() {
    return titleView;
  }

  public void setTitleView(String titleView) {
    this.titleView = titleView;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((jnlId == null) ? 0 : jnlId.hashCode());
    result = prime * result + ((psnId == null) ? 0 : psnId.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    PsnJnlPsnRecommend other = (PsnJnlPsnRecommend) obj;
    if (jnlId == null) {
      if (other.jnlId != null)
        return false;
    } else if (!jnlId.equals(other.jnlId))
      return false;
    if (psnId == null) {
      if (other.psnId != null)
        return false;
    } else if (!psnId.equals(other.psnId))
      return false;
    return true;
  }

}
