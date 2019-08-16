package com.smate.core.base.pub.model.pdwh;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * 成果关键词 词典
 * 
 * @author aijiangbin
 *
 */
@Entity
@Table(name = "PDWH_PUB_KYWORD_DIC")
public class PdwhPubKeywordDictionary implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = -8368401288357831970L;

  @Id
  @Column(name = "ID")
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "seq_pdwh_pub_kyword_dictionary", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
  private Long id;

  @Column(name = "ZH_KEYWORD")
  private String zhKeyword; // 中文关键词

  @Column(name = "EN_KEYWORD")
  private String enKeyword; // 英文关键词

  @Column(name = "UPDATE_DATE")
  private Date updateDate;

  @Column(name = "STATUS")
  private Integer status;// 默认为0 1表示已经翻译';

  @Column(name = "LANGUAGE")
  private Integer language;// 语种， 1=英文， 2=中文

  @Column(name = "ZH_KEYWORD_GG")
  private String zhKeywordGg; // 中文关键词 google翻译
  @Column(name = "ZH_KEYWORD_BD")
  private String zhKeywordBd; // 中文关键词 百度翻译
  @Column(name = "ZH_KEYWORD_TX")
  private String zhKeywordTx; // 中文关键词 腾讯翻译

  @Column(name = "EN_KEYWORD_GG")
  private String enKeywordGg; // 英文关键词 google翻译
  @Column(name = "EN_KEYWORD_BD")
  private String enKeywordBd; // 英文关键词 百度翻译
  @Column(name = "EN_KEYWORD_TX")
  private String enKeywordTx; // 英文关键词 腾讯翻译

  @Column(name = "KEYWORD_HASH_CODE")
  private String keywordHashCode; // 关键词hash值


  /*
   * @Column(name = "TEMP_STATUS") private Integer tempStatus;// 临时状态
   */


  public PdwhPubKeywordDictionary() {
    super();
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getZhKeyword() {
    return zhKeyword;
  }

  public void setZhKeyword(String zhKeyword) {
    this.zhKeyword = zhKeyword;
  }

  public String getEnKeyword() {
    return enKeyword;
  }

  public void setEnKeyword(String enKeyword) {
    this.enKeyword = enKeyword;
  }

  public Date getUpdateDate() {
    return updateDate;
  }

  public void setUpdateDate(Date updateDate) {
    this.updateDate = updateDate;
  }

  public Integer getStatus() {
    return status;
  }

  public void setStatus(Integer status) {
    this.status = status;
  }

  public Integer getLanguage() {
    return language;
  }

  public void setLanguage(Integer language) {
    this.language = language;
  }

  public String getZhKeywordGg() {
    return zhKeywordGg;
  }

  public void setZhKeywordGg(String zhKeywordGg) {
    this.zhKeywordGg = zhKeywordGg;
  }

  public String getZhKeywordBd() {
    return zhKeywordBd;
  }

  public void setZhKeywordBd(String zhKeywordBd) {
    this.zhKeywordBd = zhKeywordBd;
  }



  public String getZhKeywordTx() {
    return zhKeywordTx;
  }

  public void setZhKeywordTx(String zhKeywordTx) {
    this.zhKeywordTx = zhKeywordTx;
  }

  public String getEnKeywordGg() {
    return enKeywordGg;
  }

  public void setEnKeywordGg(String enKeywordGg) {
    this.enKeywordGg = enKeywordGg;
  }

  public String getEnKeywordBd() {
    return enKeywordBd;
  }

  public void setEnKeywordBd(String enKeywordBd) {
    this.enKeywordBd = enKeywordBd;
  }

  public String getEnKeywordTx() {
    return enKeywordTx;
  }

  public void setEnKeywordTx(String enKeywordTx) {
    this.enKeywordTx = enKeywordTx;
  }

  public String getKeywordHashCode() {
    return keywordHashCode;
  }

  public void setKeywordHashCode(String keywordHashCode) {
    this.keywordHashCode = keywordHashCode;
  }

  /*
   * public Integer getTempStatus() { return tempStatus; }
   * 
   * public void setTempStatus(Integer tempStatus) { this.tempStatus = tempStatus; }
   */



}
