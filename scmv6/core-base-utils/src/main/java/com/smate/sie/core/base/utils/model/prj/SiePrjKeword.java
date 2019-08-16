package com.smate.sie.core.base.utils.model.prj;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * 
 * @author yxs
 * @descript 项目关键词
 */
@Entity
@Table(name = "PRJ_KEYWORDS")
public class SiePrjKeword {
  @Id
  @Column(name = "ID")
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_PRJ_KEYWORDS", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
  private Long id;
  @Column(name = "PRJ_ID")
  private Long prjId;
  @Column(name = "INS_ID")
  private Long insId;
  @Column(name = "KEYWORD")
  private String keyword;
  @Column(name = "KEYWORD_TXT")
  private String keywordText;

  public SiePrjKeword() {
    super();
    // TODO Auto-generated constructor stub
  }

  public SiePrjKeword(Long prjId, Long insId, String keyword, String keywordText) {
    super();
    this.prjId = prjId;
    this.insId = insId;
    this.keyword = keyword;
    this.keywordText = keywordText;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Long getPrjId() {
    return prjId;
  }

  public void setPrjId(Long prjId) {
    this.prjId = prjId;
  }

  public Long getInsId() {
    return insId;
  }

  public void setInsId(Long insId) {
    this.insId = insId;
  }

  public String getKeyword() {
    return keyword;
  }

  public void setKeyword(String keyword) {
    this.keyword = keyword;
  }

  public String getKeywordText() {
    return keywordText;
  }

  public void setKeywordText(String keywordText) {
    this.keywordText = keywordText;
  }

}
