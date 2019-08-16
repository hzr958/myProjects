package com.smate.web.management.model.journal;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * cwli期刊关键词表，存储期刊被各文献库收录的信息.
 */
@Entity
@Table(name = "BASE_JOURNAL_KEYWORD")
public class BaseJournalKeyword implements Serializable {

  private static final long serialVersionUID = -8071690163969062894L;

  private Long id;
  // 通过该字段与base_journal主表关联
  private Long jnlId;
  // 关键词
  private String keyword;
  // 关键词格式后的
  private String keywordText;
  // 转成小写，生成hash
  private Long keywordHash;
  // 英文1，中文2
  private Integer type;
  // 频次
  private Integer frequency;

  // 来源(如isi,cnki)
  private String scources;

  @Id
  @Column(name = "JOU_KEYWORD_ID")
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_BASE_JOU_KEYWORD", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  @Column(name = "JNL_ID")
  public Long getJnlId() {
    return jnlId;
  }

  public void setJnlId(Long jnlId) {
    this.jnlId = jnlId;
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

  @Column(name = "TYPE")
  public Integer getType() {
    return type;
  }

  public void setType(Integer type) {
    this.type = type;
  }

  @Column(name = "FREQUENCY")
  public Integer getFrequency() {
    return frequency;
  }

  public void setFrequency(Integer frequency) {
    this.frequency = frequency;
  }

  @Column(name = "SCOURCES")
  public String getScources() {
    return scources;
  }

  public void setScources(String scources) {
    this.scources = scources;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

}
