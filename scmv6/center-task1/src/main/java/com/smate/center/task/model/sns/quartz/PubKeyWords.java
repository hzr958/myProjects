package com.smate.center.task.model.sns.quartz;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "PUB_KEYWORDS")
public class PubKeyWords implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = -3966319298666469137L;
  private Long id;
  // 成果ID
  private Long pubId;
  // 成果所有人
  private Long psnId;
  // 关键词
  private String keyword;
  // 关键词文本
  private String keywordTxt;

  private String zhKw;
  // 中文关键词文本
  private String zhKwTxt;
  // 中文关键词长度
  private Integer zhKwLen = 0;
  // 翻译的英文关键词，成果有英文，则不翻译
  private String enKw;
  // 英文关键词文本
  private String enKwTxt;
  // 英文关键词长度
  private Integer enKwLen = 0;
  // 成果发表年份
  private Integer publishYear;
  // 作者序号
  private Integer auSeq;
  // 是否通讯作者
  private Integer auPos;
  // 是否核心期刊或ISI期刊
  private Integer hxj;
  // ISI/CNKI核心期刊+第123/通信作者=1，单非=0.618，双非=0.618×0.618
  private Double weight = 0D;

  public PubKeyWords() {
    super();
  }

  public PubKeyWords(Long id, Long pubId) {
    super();
    this.id = id;
    this.pubId = pubId;
  }

  public PubKeyWords(Long pubId, Long psnId, String keyword, String keywordTxt) {
    super();
    this.pubId = pubId;
    this.psnId = psnId;
    this.keyword = keyword;
    this.keywordTxt = keywordTxt;
  }

  public PubKeyWords(Long id, Long pubId, Long psnId, String keyword, String keywordTxt) {
    super();
    this.id = id;
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

  @Transient
  public String getZhKw() {
    return zhKw;
  }

  public void setZhKw(String zhKw) {
    this.zhKw = zhKw;
  }

  @Transient
  public String getZhKwTxt() {
    return zhKwTxt;
  }

  public void setZhKwTxt(String zhKwTxt) {
    this.zhKwTxt = zhKwTxt;
  }

  @Transient
  public Integer getZhKwLen() {
    return zhKwLen;
  }

  public void setZhKwLen(Integer zhKwLen) {
    this.zhKwLen = zhKwLen;
  }

  @Transient
  public String getEnKw() {
    return enKw;
  }

  public void setEnKw(String enKw) {
    this.enKw = enKw;
  }

  @Transient
  public String getEnKwTxt() {
    return enKwTxt;
  }

  public void setEnKwTxt(String enKwTxt) {
    this.enKwTxt = enKwTxt;
  }

  @Transient
  public Integer getEnKwLen() {
    return enKwLen;
  }

  public void setEnKwLen(Integer enKwLen) {
    this.enKwLen = enKwLen;
  }

  @Transient
  public Integer getPublishYear() {
    return publishYear;
  }

  public void setPublishYear(Integer publishYear) {
    this.publishYear = publishYear;
  }

  @Transient
  public Integer getAuSeq() {
    return auSeq;
  }

  public void setAuSeq(Integer auSeq) {
    this.auSeq = auSeq;
  }

  @Transient
  public Integer getAuPos() {
    return auPos;
  }

  public void setAuPos(Integer auPos) {
    this.auPos = auPos;
  }

  @Transient
  public Integer getHxj() {
    return hxj;
  }

  public void setHxj(Integer hxj) {
    this.hxj = hxj;
  }

  @Transient
  public Double getWeight() {
    return weight;
  }

  public void setWeight(Double weight) {
    this.weight = weight;
  }

}
