package com.smate.center.batch.model.pdwh.pub;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "KEYWORDS_HIGH_NEW_TECH")
public class KeywordsHighAndNewTech implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = 8320443598759740715L;
  private Long id;
  private String contentOriginal;
  private String content;
  private Integer language;
  private Long hashValue;
  private String category;
  private String parentCategory;
  private Integer length;

  public KeywordsHighAndNewTech() {
    super();
  }

  public KeywordsHighAndNewTech(String contentOriginal, String content, Integer language, Long hashValue,
      String category, String parentCategory, Integer length) {
    super();
    this.contentOriginal = contentOriginal;
    this.content = content;
    this.language = language;
    this.hashValue = hashValue;
    this.category = category;
    this.parentCategory = parentCategory;
    this.length = length;
  }

  @Id
  @Column(name = "ID")
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_V_KEYWORD_SUBSET", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  @Column(name = "CONTENT_ORIGINAL")
  public String getContentOriginal() {
    return contentOriginal;
  }

  public void setContentOriginal(String contentOriginal) {
    this.contentOriginal = contentOriginal;
  }

  @Column(name = "CONTENT")
  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content = content;
  }

  @Column(name = "LANGUAGE")
  public Integer getLanguage() {
    return language;
  }

  public void setLanguage(Integer language) {
    this.language = language;
  }

  @Column(name = "HASHVALUE")
  public Long getHashValue() {
    return hashValue;
  }

  public void setHashValue(Long hashValue) {
    this.hashValue = hashValue;
  }

  @Column(name = "CATEGORY")
  public String getCategory() {
    return category;
  }

  public void setCategory(String category) {
    this.category = category;
  }

  @Column(name = "PARENT_CATEGORY")
  public String getParentCategory() {
    return parentCategory;
  }

  public void setParentCategory(String parentCategory) {
    this.parentCategory = parentCategory;
  }

  @Column(name = "LENGTH")
  public Integer getLength() {
    return length;
  }

  public void setLength(Integer length) {
    this.length = length;
  }

}
