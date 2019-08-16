package com.smate.center.batch.model.pdwh.pubimport;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * 成果关键词拆分表
 * 
 * @author zjh
 *
 */
@Entity
@Table(name = "PDWH_PUB_KEYWORDS_SPLIT")
public class PdwhPubKeywordsSplit implements Serializable {
  /**
   * 
   */
  private static final long serialVersionUID = 4176428875211121038L;
  private Long id;
  private Long pubId;
  private String keyword;
  private String keywordText;
  private Long keywordHash;
  private Integer language;// (1英文，2中文)

  public PdwhPubKeywordsSplit() {
    super();
  }

  public PdwhPubKeywordsSplit(Long pubId, String keyword, String keywordText, Long keywordHash, Integer language) {
    super();
    this.pubId = pubId;
    this.keyword = keyword;
    this.keywordText = keywordText;
    this.keywordHash = keywordHash;
    this.language = language;
  }

  @Id
  @Column(name = "Id")
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_PUB_KEYWORDS_SPLIT", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  @Column(name = "PUB_ID")
  public Long getPubId() {
    return pubId;
  }

  public void setPubId(Long pubId) {
    this.pubId = pubId;
  }

  @Column(name = "KEYWORD")
  public String getKeyword() {
    return keyword;
  }

  public void setKeyword(String keyword) {
    this.keyword = keyword;
  }

  @Column(name = "KEYWORD_TEXT")
  public String getKeywordText() {
    return keywordText;
  }

  public void setKeywordText(String keywordText) {
    this.keywordText = keywordText;
  }

  @Column(name = "KEYWORD_HASH")
  public Long getKeywordHash() {
    return keywordHash;
  }

  public void setKeywordHash(Long keywordHash) {
    this.keywordHash = keywordHash;
  }

  @Column(name = "LANGUAGE")
  public Integer getLanguage() {
    return language;
  }

  public void setLanguage(Integer language) {
    this.language = language;
  }

}
