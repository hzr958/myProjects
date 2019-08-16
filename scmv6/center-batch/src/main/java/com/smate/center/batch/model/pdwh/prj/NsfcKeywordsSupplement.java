package com.smate.center.batch.model.pdwh.prj;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/*
 * 补充整理之后的关键词
 * 
 * 
 */
@Entity
@Table(name = "NSFC_KW_SUPPLEMENT")
public class NsfcKeywordsSupplement implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = -5012838704344274697L;

  private Long Id;
  private Integer language;// 1英文，2中文
  private String nsfcCategroy;
  private String categoryLength;
  private Integer kwType; // 1自填关键词, 2关联成果关键词, 3学科主任维护关键词
  private Long kwHashValue;
  private String kwStr;

  public NsfcKeywordsSupplement() {
    super();
  }

  @Id
  @Column(name = "ID")
  @SequenceGenerator(name = "SEQ_STORE", allocationSize = 1, sequenceName = "SEQ_PDWH_PUB_ADDR_INS_RECORD")
  @GeneratedValue(generator = "SEQ_STORE", strategy = GenerationType.SEQUENCE)
  public Long getId() {
    return Id;
  }

  public void setId(Long id) {
    Id = id;
  }

  @Column(name = "LANGUAGE")
  public Integer getLanguage() {
    return language;
  }

  public void setLanguage(Integer language) {
    this.language = language;
  }

  @Column(name = "KW_TYPE")
  public Integer getKwType() {
    return kwType;
  }

  public void setKwType(Integer kwType) {
    this.kwType = kwType;
  }

  @Column(name = "KW_HASHVALUE")
  public Long getKwHashValue() {
    return kwHashValue;
  }

  public void setKwHashValue(Long kwHashValue) {
    this.kwHashValue = kwHashValue;
  }

  @Column(name = "KW_STR")
  public String getKwStr() {
    return kwStr;
  }

  public void setKwStr(String kwStr) {
    this.kwStr = kwStr;
  }

  @Column(name = "NSFC_CATEGORY")
  public String getNsfcCategroy() {
    return nsfcCategroy;
  }

  public void setNsfcCategroy(String nsfcCategroy) {
    this.nsfcCategroy = nsfcCategroy;
  }

  public String getCategoryLength() {
    return categoryLength;
  }

  public void setCategoryLength(String categoryLength) {
    this.categoryLength = categoryLength;
  }

}
