package com.smate.center.batch.model.sns.pub;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * 成果关键词拆分表.
 * 
 * @author liqinghua
 * 
 */
@Entity
@Table(name = "PUB_KEYWORDS")
public class PubKeyWords implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = -8368178313458013654L;

  private Long id;
  // 成果ID
  private Long pubId;
  // 成果所有人
  private Long psnId;
  // 关键词
  private String keyword;
  // 关键词文本
  private String keywordTxt;

  public PubKeyWords() {
    super();
  }

  public PubKeyWords(Long pubId, Long psnId, String keyword, String keywordTxt) {
    super();
    this.pubId = pubId;
    this.psnId = psnId;
    this.keyword = keyword;
    this.keywordTxt = keywordTxt;
  }

  @Id
  @Column(name = "ID")
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_PUB_KEYWORDS", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
  public Long getId() {
    return id;
  }

  @Column(name = "PUB_ID")
  public Long getPubId() {
    return pubId;
  }

  @Column(name = "PSN_ID")
  public Long getPsnId() {
    return psnId;
  }

  @Column(name = "KEYWORD")
  public String getKeyword() {
    return keyword;
  }

  @Column(name = "KEYWORD_TXT")
  public String getKeywordTxt() {
    return keywordTxt;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public void setPubId(Long pubId) {
    this.pubId = pubId;
  }

  public void setPsnId(Long psnId) {
    this.psnId = psnId;
  }

  public void setKeyword(String keyword) {
    this.keyword = keyword;
  }

  public void setKeywordTxt(String keywordTxt) {
    this.keywordTxt = keywordTxt;
  }

}
