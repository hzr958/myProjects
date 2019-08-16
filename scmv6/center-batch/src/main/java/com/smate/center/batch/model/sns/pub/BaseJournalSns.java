package com.smate.center.batch.model.sns.pub;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author tsz sns基础期刊冗余实体类.
 */
@Entity
@Table(name = "BASE_JOURNAL")
public class BaseJournalSns implements java.io.Serializable {


  /**
   * 
   */
  private static final long serialVersionUID = -8672013740483441236L;
  // 主键
  private Long jouId;
  // 英文刊名
  private String titleEn;
  // 原始刊名
  private String titleXx;
  // Print ISSN
  private String pissn;
  // 对应的影响因子值
  private String impactFactors;

  // 小写
  private String lowerTitleEn;
  // 小写
  private String lowerTitleXx;

  public BaseJournalSns() {
    super();
  }

  public BaseJournalSns(Long jouId, String titleEn, String titleXx, String pissn, String impactFactors) {
    super();
    this.jouId = jouId;
    this.titleEn = titleEn;
    this.titleXx = titleXx;
    this.pissn = pissn;
    this.impactFactors = impactFactors;

  }

  public BaseJournalSns(Long jouId, String titleEn, String titleXx, String pissn, String impactFactors,
      String lowerTitleEn, String lowerTitleXx) {
    super();
    this.jouId = jouId;
    this.titleEn = titleEn;
    this.titleXx = titleXx;
    this.pissn = pissn;
    this.impactFactors = impactFactors;
    this.lowerTitleEn = lowerTitleEn;
    this.lowerTitleXx = lowerTitleXx;
  }

  @Id
  @Column(name = "JNL_ID")
  public Long getJouId() {
    return jouId;
  }

  public void setJouId(Long jouId) {
    this.jouId = jouId;
  }

  @Column(name = "TITLE_EN")
  public String getTitleEn() {
    return titleEn;
  }

  public void setTitleEn(String titleEn) {
    this.titleEn = titleEn;
  }

  @Column(name = "TITLE_XX")
  public String getTitleXx() {
    return titleXx;
  }

  public void setTitleXx(String titleXx) {
    this.titleXx = titleXx;
  }

  @Column(name = "PISSN")
  public String getPissn() {
    return pissn;
  }

  public void setPissn(String pissn) {
    this.pissn = pissn;
  }

  @Column(name = "impact_factors")
  public String getImpactFactors() {
    return impactFactors;
  }

  public void setImpactFactors(String impactFactors) {
    this.impactFactors = impactFactors;
  }

  @Column(name = "lower_title_en")
  public String getLowerTitleEn() {
    return lowerTitleEn;
  }

  public void setLowerTitleEn(String lowerTitleEn) {
    this.lowerTitleEn = lowerTitleEn;
  }

  @Column(name = "lower_title_xx")
  public String getLowerTitleXx() {
    return lowerTitleXx;
  }

  public void setLowerTitleXx(String lowerTitleXx) {
    this.lowerTitleXx = lowerTitleXx;
  }

}
