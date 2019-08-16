package com.smate.center.batch.model.sns.relationanalysis;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "keywords_20160120")
public class KeywordsNew implements Serializable {


  /**
   * 
   */
  private static final long serialVersionUID = -7448082374971129496L;

  @Id
  @Column(name = "KEYWORD_ID")
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_KEYWORDS_20160120", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
  private Long keywordId;
  @Column(name = "KC_ID")
  private Integer kcId;
  @Column(name = "CATEGORY_ID")
  private Integer categoryId;
  @Column(name = "CATEGORY_ZH")
  private String categoryZh;
  @Column(name = "CATEGORY_EN")
  private String categoryEn;
  @Column(name = "DESCIPLINE_ID")
  private Integer desciplineId;
  @Column(name = "DESCIPLINE_ZH")
  private String desciplineZh;
  @Column(name = "DESCIPLINE_EN")
  private String desciplineEn;
  @Column(name = "KEYWORD_ZH")
  private String keywordsZh;
  @Column(name = "KEYWORD_EN")
  private String keywordsEn;

  public KeywordsNew() {
    super();
  }


  public Long getKeywordId() {
    return keywordId;
  }

  public void setKeywordId(Long keywordId) {
    this.keywordId = keywordId;
  }

  public Integer getKcId() {
    return kcId;
  }

  public void setKcId(Integer kcId) {
    this.kcId = kcId;
  }

  public Integer getCategoryId() {
    return categoryId;
  }

  public void setCategoryId(Integer categoryId) {
    this.categoryId = categoryId;
  }

  public String getCategoryZh() {
    return categoryZh;
  }

  public void setCategoryZh(String categoryZh) {
    this.categoryZh = categoryZh;
  }

  public String getCategoryEn() {
    return categoryEn;
  }

  public void setCategoryEn(String categoryEn) {
    this.categoryEn = categoryEn;
  }

  public Integer getDesciplineId() {
    return desciplineId;
  }

  public void setDesciplineId(Integer desciplineId) {
    this.desciplineId = desciplineId;
  }

  public String getDesciplineZh() {
    return desciplineZh;
  }

  public void setDesciplineZh(String desciplineZh) {
    this.desciplineZh = desciplineZh;
  }

  public String getDesciplineEn() {
    return desciplineEn;
  }

  public void setDesciplineEn(String desciplineEn) {
    this.desciplineEn = desciplineEn;
  }

  public String getKeywordsZh() {
    return keywordsZh;
  }

  public void setKeywordsZh(String keywordsZh) {
    this.keywordsZh = keywordsZh;
  }

  public String getKeywordsEn() {
    return keywordsEn;
  }

  public void setKeywordsEn(String keywordsEn) {
    this.keywordsEn = keywordsEn;
  }


}
