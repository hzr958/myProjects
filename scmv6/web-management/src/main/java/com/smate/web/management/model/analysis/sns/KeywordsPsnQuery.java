package com.smate.web.management.model.analysis.sns;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * 用于通过关键词过滤用户.
 * 
 * @author lqh
 * 
 */
@Entity
@Table(name = "KEYWORDS_PSN_QUERY")
public class KeywordsPsnQuery implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = 996839724502651480L;
  private Long id;
  // 查询ID
  private Long qid;
  // 关键词
  private String keyword;
  // 关键词Txt
  private String keywordTxt;
  // 权重
  private Double weight = 1D;
  // 查询时间
  private Date qat;

  public KeywordsPsnQuery() {
    super();
  }

  public KeywordsPsnQuery(String keyword, Double weight) {
    super();
    this.keyword = keyword;
    this.keywordTxt = keyword.toLowerCase().replaceAll("\\s+", " ").trim();
    this.weight = weight;
  }

  public KeywordsPsnQuery(Long qid, String keyword, String keywordTxt, Double weight) {
    super();
    this.qid = qid;
    this.keyword = keyword;
    this.keywordTxt = keywordTxt;
    this.weight = weight;
  }

  @Id
  @Column(name = "ID")
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_KEYWORDS_PSN_QUERY", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
  public Long getId() {
    return id;
  }

  @Column(name = "QID")
  public Long getQid() {
    return qid;
  }

  @Column(name = "KEYWORD")
  public String getKeyword() {
    return keyword;
  }

  @Column(name = "WEIGHT")
  public Double getWeight() {
    return weight;
  }

  @Column(name = "QAT")
  public Date getQat() {
    return qat;
  }

  @Column(name = "KEYWORD_TXT")
  public String getKeywordTxt() {
    return keywordTxt;
  }

  public void setKeywordTxt(String keywordTxt) {
    this.keywordTxt = keywordTxt;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public void setQid(Long qid) {
    this.qid = qid;
  }

  public void setKeyword(String keyword) {
    this.keyword = keyword;
  }

  public void setWeight(Double weight) {
    this.weight = weight;
  }

  public void setQat(Date qat) {
    this.qat = qat;
  }

}
