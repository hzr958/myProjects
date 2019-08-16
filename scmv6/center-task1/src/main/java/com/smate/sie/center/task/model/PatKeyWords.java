package com.smate.sie.center.task.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * 专利关键词拆分表.
 * 
 * @author jszhou
 */
@Entity
@Table(name = "PAT_KEYWORDS")
public class PatKeyWords implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = 563542786985405436L;
  private Long id;
  // 成果ID
  private Long patId;
  // 成果所有人
  private Long insId;
  // 关键词
  private String keyword;
  // 关键词文本
  private String keywordTxt;

  public PatKeyWords() {
    super();
  }

  public PatKeyWords(Long patId, Long insId, String keyword, String keywordTxt) {
    super();
    this.patId = patId;
    this.insId = insId;
    this.keyword = keyword;
    this.keywordTxt = keywordTxt;
  }

  @Id
  @Column(name = "ID")
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_PAT_KEYWORDS", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
  public Long getId() {
    return id;
  }

  @Column(name = "PAT_ID")
  public Long getPatId() {
    return patId;
  }

  @Column(name = "KEYWORD")
  public String getKeyword() {
    return keyword;
  }

  @Column(name = "KEYWORD_TXT")
  public String getKeywordTxt() {
    return keywordTxt;
  }

  @Column(name = "INS_ID")
  public Long getInsId() {
    return insId;
  }

  public void setInsId(Long insId) {
    this.insId = insId;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public void setPatId(Long patId) {
    this.patId = patId;
  }

  public void setKeyword(String keyword) {
    this.keyword = keyword;
  }

  public void setKeywordTxt(String keywordTxt) {
    this.keywordTxt = keywordTxt;
  }

}
