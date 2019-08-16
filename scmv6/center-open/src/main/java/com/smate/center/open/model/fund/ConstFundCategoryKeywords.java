package com.smate.center.open.model.fund;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * 基金机构类别-关键词.
 * 
 * @author lichangwen
 * 
 */
@Entity
@Table(name = "CONST_FUND_CATEGORY_KEYWORD")
public class ConstFundCategoryKeywords implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = -102946972122290014L;
  private Long id;
  // 基金类别ID，参照const_fund_category表中的ID
  private Long categoryId;
  // 基金关键词
  private String keyword;
  // 基金关键词(格式化)
  private String keywordText;
  // 基金关键词的HASH值
  private Long keywordHash;

  @Id
  @Column(name = "ID")
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_CONST_FUND_CAT_KEYOWRD", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  @Column(name = "FUND_CATEGORY_ID")
  public Long getCategoryId() {
    return categoryId;
  }

  public void setCategoryId(Long categoryId) {
    this.categoryId = categoryId;
  }

  @Column(name = "FUND_KEYWORD")
  public String getKeyword() {
    return keyword;
  }

  public void setKeyword(String keyword) {
    this.keyword = keyword;
  }

  @Column(name = "FUND_KEYWORD_TEXT")
  public String getKeywordText() {
    return keywordText;
  }

  public void setKeywordText(String keywordText) {
    this.keywordText = keywordText;
  }

  @Column(name = "FUND_KEYWORD_HASH")
  public Long getKeywordHash() {
    return keywordHash;
  }

  public void setKeywordHash(Long keywordHash) {
    this.keywordHash = keywordHash;
  }

}
