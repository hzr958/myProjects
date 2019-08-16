package com.smate.center.task.model.pdwh.quartz;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * 成果关键词
 * 
 * @author warrior
 * 
 */
@Entity
@Table(name = "PUBLICATION_ALL_KEYWORDS")
public class PubAllkeyword implements Serializable {

  private static final long serialVersionUID = -4337550153249795544L;
  @Id
  @Column(name = "ID")
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_PUB_ALL_KEY", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
  private Long id;
  // 关键词
  @Column(name = "KEYWORD")
  private String keyword;
  // 关键词Hash值
  @Column(name = "KEYWORD_HASH")
  private Long keywordHash;
  // 关键词类别 0：中文 1：英文
  @Column(name = "TYPE")
  private Integer type;
  @Column(name = "PUB_ALL_ID")
  private Long pubAllId;
  @Column(name = "PUB_YEAR")
  private Integer pubYear;

  public PubAllkeyword() {
    super();
  }

  public PubAllkeyword(Long pubAllId, String keyword) {
    super();
    this.pubAllId = pubAllId;
    this.keyword = keyword;
  }

  public PubAllkeyword(Long pubAllId, String keyword, Long keywordHash, Integer type, Integer pubYear) {
    super();
    this.keyword = keyword;
    this.keywordHash = keywordHash;
    this.type = type;
    this.pubAllId = pubAllId;
    this.pubYear = pubYear;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getKeyword() {
    return keyword;
  }

  public void setKeyword(String keyword) {
    this.keyword = keyword;
  }

  public Long getKeywordHash() {
    return keywordHash;
  }

  public void setKeywordHash(Long keywordHash) {
    this.keywordHash = keywordHash;
  }

  public Integer getType() {
    return type;
  }

  public void setType(Integer type) {
    this.type = type;
  }

  public Long getPubAllId() {
    return pubAllId;
  }

  public void setPubAllId(Long pubAllId) {
    this.pubAllId = pubAllId;
  }

  public Integer getPubYear() {
    return pubYear;
  }

  public void setPubYear(Integer pubYear) {
    this.pubYear = pubYear;
  }

}
