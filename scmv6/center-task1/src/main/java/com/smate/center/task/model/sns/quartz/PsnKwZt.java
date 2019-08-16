package com.smate.center.task.model.sns.quartz;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * 科研之友人员自填关键词(NSFC+SMATE自填).
 * 
 * @author liqinghua
 * 
 */
@Entity
@Table(name = "PSN_KW_ZT")
public class PsnKwZt implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = -8757117723944369512L;
  private Long id;
  // 人员ID
  private Long psnId;
  // 关键词
  private String keyword;
  // 关键词txt
  private String keywordTxt;
  // 翻译的中文关键词
  private String zhKw;
  // 中文关键词文本
  private String zhKwTxt;
  // 中文关键词长度
  private Integer zhKwLen;
  // 翻译的英文关键词
  private String enKw;
  // 英文关键词文本
  private String enKwTxt;
  // 英文关键词长度
  private Integer enKwLen;

  @Id
  @Column(name = "ID")
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_PSN_KW_ZT", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
  public Long getId() {
    return id;
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

  @Column(name = "ZH_KW")
  public String getZhKw() {
    return zhKw;
  }

  @Column(name = "ZH_KW_TXT")
  public String getZhKwTxt() {
    return zhKwTxt;
  }

  @Column(name = "ZH_KW_LEN")
  public Integer getZhKwLen() {
    return zhKwLen;
  }

  @Column(name = "EN_KW")
  public String getEnKw() {
    return enKw;
  }

  @Column(name = "EN_KW_TXT")
  public String getEnKwTxt() {
    return enKwTxt;
  }

  @Column(name = "EN_KW_LEN")
  public Integer getEnKwLen() {
    return enKwLen;
  }

  public void setId(Long id) {
    this.id = id;
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

  public void setZhKw(String zhKw) {
    this.zhKw = zhKw;
  }

  public void setZhKwTxt(String zhKwTxt) {
    this.zhKwTxt = zhKwTxt;
  }

  public void setZhKwLen(Integer zhKwLen) {
    this.zhKwLen = zhKwLen;
  }

  public void setEnKw(String enKw) {
    this.enKw = enKw;
  }

  public void setEnKwTxt(String enKwTxt) {
    this.enKwTxt = enKwTxt;
  }

  public void setEnKwLen(Integer enKwLen) {
    this.enKwLen = enKwLen;
  }
}
