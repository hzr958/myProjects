package com.smate.center.task.model.sns.quartz;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 人员成果关键词详情表.
 * 
 * @author liqinghua
 * 
 */
@Entity
@Table(name = "PSN_KW_PUB")
public class PsnKwPub implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = 4825743086289120431L;
  private Long id;
  // 成果ID
  private Long pubId;
  // 成果所有人
  private Long psnId;
  // 关键词
  private String keyword;
  // 关键词文本
  private String keywordTxt;
  // 成果类型
  private Integer pubType;
  // 成果发表年份
  private Integer publishYear;
  // 作者序号
  private Integer auSeq;
  // 是否通讯作者
  private Integer auPos;
  // 是否核心期刊或ISI期刊
  private Integer hxj;
  // 翻译的中文关键词，成果有中文，则不翻译
  private String zhKw;
  // 中文关键词文本
  private String zhKwTxt;
  // 中文关键词长度
  private Integer zhKwLen;
  // 翻译的英文关键词，成果有英文，则不翻译
  private String enKw;
  // 英文关键词文本
  private String enKwTxt;
  // 英文关键词长度
  private Integer enKwLen;
  // ISI/CNKI核心期刊+第123/通信作者=1，单非=0.618，双非=0.618×0.618
  private Double weight = 0D;

  private Integer sourceType;

  public PsnKwPub() {
    super();
  }

  public PsnKwPub(Long id, Long pubId) {
    super();
    this.id = id;
    this.pubId = pubId;
  }

  @Id
  @Column(name = "ID")
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

  @Column(name = "PUB_TYPE")
  public Integer getPubType() {
    return pubType;
  }

  @Column(name = "PUBLISH_YEAR")
  public Integer getPublishYear() {
    return publishYear;
  }

  public void setPublishYear(Integer publishYear) {
    this.publishYear = publishYear;
  }

  @Column(name = "AU_SEQ")
  public Integer getAuSeq() {
    return auSeq;
  }

  @Column(name = "AU_POS")
  public Integer getAuPos() {
    return auPos;
  }

  @Column(name = "HXJ")
  public Integer getHxj() {
    return hxj;
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

  @Column(name = "WEIGHT")
  public Double getWeight() {
    return weight;
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

  public void setPubType(Integer pubType) {
    this.pubType = pubType;
  }

  public void setAuSeq(Integer auSeq) {
    this.auSeq = auSeq;
  }

  public void setAuPos(Integer auPos) {
    this.auPos = auPos;
  }

  public void setHxj(Integer hxj) {
    this.hxj = hxj;
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

  public void setWeight(Double weight) {
    this.weight = weight;
  }

  @Column(name = "SOURCE_TYPE")
  public Integer getSourceType() {
    return sourceType;
  }

  public void setSourceType(Integer sourceType) {
    this.sourceType = sourceType;
  }
}
