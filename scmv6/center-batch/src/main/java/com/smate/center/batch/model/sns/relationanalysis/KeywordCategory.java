package com.smate.center.batch.model.sns.relationanalysis;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "keyword_category_20160120")
public class KeywordCategory implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = -8873134572335380609L;

  @Id
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
  @Column(name = "TOP10_JOURNALS_EN")
  private String topJournalsEn;
  @Column(name = "TOP10_JOURNALS_ZH")
  private String topJournalsZh;

  public KeywordCategory() {
    super();
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

  public String getTopJournalsEn() {
    return topJournalsEn;
  }

  public void setTopJournalsEn(String topJournalsEn) {
    this.topJournalsEn = topJournalsEn;
  }

  public String getTopJournalsZh() {
    return topJournalsZh;
  }

  public void setTopJournalsZh(String topJournalsZh) {
    this.topJournalsZh = topJournalsZh;
  }


}
