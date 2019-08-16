package com.smate.center.batch.model.sns.pub;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 英文翻译中文库.
 * 
 * @author liqinghua
 * 
 */
@Entity
@Table(name = "KEYWORDS_ENTRAN_ZH")
public class KeywordsEnTranZh implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = -6901391094220867657L;

  private Long id;
  // 英文关键词
  private String enKw;
  // 英文关键词文本
  private String enKwTxt;
  // 中文关键词
  private String zhKw;
  // 中文关键词文本
  private String zhKwTxt;
  private Integer tf;

  @Id
  @Column(name = "ID")
  public Long getId() {
    return id;
  }

  @Column(name = "EN_KEYWORD")
  public String getEnKw() {
    return enKw;
  }

  @Column(name = "EN_KW_TXT")
  public String getEnKwTxt() {
    return enKwTxt;
  }

  @Column(name = "ZH_KEYWORD")
  public String getZhKw() {
    return zhKw;
  }

  @Column(name = "ZH_KW_TXT")
  public String getZhKwTxt() {
    return zhKwTxt;
  }

  @Column(name = "TF")
  public Integer getTf() {
    return tf;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public void setEnKw(String enKw) {
    this.enKw = enKw;
  }

  public void setEnKwTxt(String enKwTxt) {
    this.enKwTxt = enKwTxt;
  }

  public void setZhKw(String zhKw) {
    this.zhKw = zhKw;
  }

  public void setZhKwTxt(String zhKwTxt) {
    this.zhKwTxt = zhKwTxt;
  }

  public void setTf(Integer tf) {
    this.tf = tf;
  }

}
