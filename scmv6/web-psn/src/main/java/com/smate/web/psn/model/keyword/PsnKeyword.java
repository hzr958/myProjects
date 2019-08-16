package com.smate.web.psn.model.keyword;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * 人员关键词
 * 
 * @author tsz
 * 
 */

@Entity
@Table(name = "PSN_KEYWORDS")
public class PsnKeyword implements Serializable {

  private static final long serialVersionUID = -1446461663810053578L;

  private Long id;
  // 人员ID
  private Long psn_id;
  // 关键词
  private String keyword;
  // 关键词转化为小写
  private String keyword_text;
  // 关键词Hash值
  private Long keyword_hash;
  // 转成小写，生成反序hash
  private Long keywordRhash;
  // 出现频率
  private Long frequency;

  @Id
  @Column(name = "ID")
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_PSN_KEYWORDS", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  @Column(name = "PSN_ID")
  public Long getPsn_id() {
    return psn_id;
  }

  public void setPsn_id(Long psnId) {
    psn_id = psnId;
  }

  @Column(name = "KEYWORD")
  public String getKeyword() {
    return keyword;
  }

  public void setKeyword(String keyword) {
    this.keyword = keyword;
  }

  @Column(name = "KEYWORD_TEXT")
  public String getKeyword_text() {
    return keyword_text;
  }

  public void setKeyword_text(String keywordText) {
    keyword_text = keywordText;
  }

  @Column(name = "KEYWORD_HASH")
  public Long getKeyword_hash() {
    return keyword_hash;
  }

  public void setKeyword_hash(Long keywordHash) {
    keyword_hash = keywordHash;
  }

  @Column(name = "FREQUENCY")
  public Long getFrequency() {
    return frequency;
  }

  public void setFrequency(Long frequency) {
    this.frequency = frequency;
  }

  @Column(name = "KEYWORD_RHASH")
  public Long getKeywordRhash() {
    return keywordRhash;
  }

  public void setKeywordRhash(Long keywordRhash) {
    this.keywordRhash = keywordRhash;
  }

}
