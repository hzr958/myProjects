package com.smate.web.management.model.journal;

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

/**
 * cwli期刊影响因子表，存储期刊在各年的影响因子，包括ISI,scopus等各种影响因子，可以扩展.
 */
@Entity
@Table(name = "BASE_JOURNAL_ISI_IF")
public class BaseJournalIfTo implements Serializable {
  /**
   * 
   */
  private static final long serialVersionUID = 5410539165609752165L;

  private Long jouIfId;
  private Long jnlId;

  private Long dbId;
  // 对应的影响因子值
  private String jouIf;
  // 影响因子的年份，如：2008
  private String ifYear;
  // R impact factor
  private String jouRif;
  // Immediacy Index,即时系数
  private String immediacyIndex;
  // TOTAL Number of ARTICLES总发文数
  private String articles;
  // total cites总被引数
  private String cites;
  // Cited Half Life,被引半衰期
  private String citedHalfLife;
  // citing half life,引用半衰期
  private String citingHalfLife;
  // Article Influence Score，论文影响值
  private String articleInfluenceScore;
  // Eigenfactor Score
  private String eigenfactorScore;
  // 冗余字段,期刊中文名或者其它语言
  private String titleXx;
  // 冗余字段，期刊英文名
  private String titleEn;
  // 冗余字段，期刊PISSN
  private String pissn;
  // 冗余字段，dbCoed
  private String dbCode;

  @Id
  @Column(name = "JOU_IF_ID")
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_BASE_JOU_IF", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
  public Long getJouIfId() {
    return jouIfId;
  }

  public void setJouIfId(Long jouIfId) {
    this.jouIfId = jouIfId;
  }

  @Column(name = "JNL_ID")
  public Long getJnlId() {
    return jnlId;
  }

  public void setJnlId(Long jnlId) {
    this.jnlId = jnlId;
  }

  @Column(name = "DBID")
  public Long getDbId() {
    return dbId;
  }

  public void setDbId(Long dbId) {
    this.dbId = dbId;
  }

  @Column(name = "JOU_IF")
  public String getJouIf() {
    return jouIf;
  }

  public void setJouIf(String jouIf) {
    this.jouIf = jouIf;
  }

  @Column(name = "IF_YEAR")
  public String getIfYear() {
    return ifYear;
  }

  public void setIfYear(String ifYear) {
    this.ifYear = ifYear;
  }

  @Column(name = "JOU_R_IF")
  public String getJouRif() {
    return jouRif;
  }

  public void setJouRif(String jouRif) {
    this.jouRif = jouRif;
  }

  @Column(name = "JOU_IMMEDICACY_INDEX")
  public String getImmediacyIndex() {
    return immediacyIndex;
  }

  public void setImmediacyIndex(String immediacyIndex) {
    this.immediacyIndex = immediacyIndex;
  }

  @Column(name = "JOU_ARTICLES")
  public String getArticles() {
    return articles;
  }

  public void setArticles(String articles) {
    this.articles = articles;
  }

  @Column(name = "JOU_CITES")
  public String getCites() {
    return cites;
  }

  public void setCites(String cites) {
    this.cites = cites;
  }

  @Column(name = "JOU_CITED_HALF_LIFE")
  public String getCitedHalfLife() {
    return citedHalfLife;
  }

  public void setCitedHalfLife(String citedHalfLife) {
    this.citedHalfLife = citedHalfLife;
  }

  @Column(name = "JOU_CITING_HALF_LIFE")
  public String getCitingHalfLife() {
    return citingHalfLife;
  }

  public void setCitingHalfLife(String citingHalfLife) {
    this.citingHalfLife = citingHalfLife;
  }

  @Column(name = "JOU_ARTICLE_INFLUENCE_SCORE")
  public String getArticleInfluenceScore() {
    return articleInfluenceScore;
  }

  public void setArticleInfluenceScore(String articleInfluenceScore) {
    this.articleInfluenceScore = articleInfluenceScore;
  }

  @Column(name = "JOU_EIGENFACTOR_SCORE")
  public String getEigenfactorScore() {
    return eigenfactorScore;
  }

  public void setEigenfactorScore(String eigenfactorScore) {
    this.eigenfactorScore = eigenfactorScore;
  }

  @Transient
  public String getTitleXx() {
    return titleXx;
  }

  public void setTitleXx(String titleXx) {
    this.titleXx = titleXx;
  }

  @Transient
  public String getTitleEn() {
    return titleEn;
  }

  public void setTitleEn(String titleEn) {
    this.titleEn = titleEn;
  }

  @Transient
  public String getPissn() {
    return pissn;
  }

  public void setPissn(String pissn) {
    this.pissn = pissn;
  }

  @Transient
  public String getDbCode() {
    return dbCode;
  }

  public void setDbCode(String dbCode) {
    this.dbCode = dbCode;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

}
