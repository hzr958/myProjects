package com.smate.center.batch.model.pdwh.pub.cnki;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * cnki关键词拆分.
 * 
 * @author liqinghua
 * 
 */
@Entity
@Table(name = "CNKI_PUB_KEYWORDS_SPLIT")
public class CnkiPubKeywordsSplit implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = 6024529921730675727L;

  private Long id;
  private Long pubId;
  private String keyword;
  private String keywordText;
  private Long keywordHash;
  // 英文1，中文2
  private Integer type;

  public CnkiPubKeywordsSplit() {
    super();
  }

  public CnkiPubKeywordsSplit(Long pubId, String keyword, String keywordText, Long keywordHash, Integer type) {
    super();
    this.pubId = pubId;
    this.keyword = keyword;
    this.keywordText = keywordText;
    this.keywordHash = keywordHash;
    this.type = type;
  }

  @Id
  @Column(name = "ID")
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_CNKI_PUB_KEYWORDS_SPLIT", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
  public Long getId() {
    return id;
  }

  @Column(name = "PUB_ID")
  public Long getPubId() {
    return pubId;
  }

  @Column(name = "KEYWORD")
  public String getKeyword() {
    return keyword;
  }

  @Column(name = "KEYWORD_TEXT")
  public String getKeywordText() {
    return keywordText;
  }

  @Column(name = "KEYWORD_HASH")
  public Long getKeywordHash() {
    return keywordHash;
  }

  @Column(name = "TYPE")
  public Integer getType() {
    return type;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public void setPubId(Long pubId) {
    this.pubId = pubId;
  }

  public void setKeyword(String keyword) {
    this.keyword = keyword;
  }

  public void setKeywordText(String keywordText) {
    this.keywordText = keywordText;
  }

  public void setKeywordHash(Long keywordHash) {
    this.keywordHash = keywordHash;
  }

  public void setType(Integer type) {
    this.type = type;
  }

}
