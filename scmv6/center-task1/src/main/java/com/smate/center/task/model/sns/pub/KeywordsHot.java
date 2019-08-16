package com.smate.center.task.model.sns.pub;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * 关键词热词.
 * 
 * @author liqinghua
 * 
 */
@Entity
@Table(name = "KEYWORDS_HOT")
public class KeywordsHot implements Serializable, Comparable<KeywordsHot> {

  /**
   * 
   */
  private static final long serialVersionUID = -7568359451853665000L;

  private Long id;
  private String keywords;
  private String kwTxt;
  private Integer tf;
  private String ekeywords;
  private String ekwTxt;
  private Integer etf;
  private List<KeywordsHot> relKeywords;
  private Long relHotKey;
  private Double weight;

  public KeywordsHot() {
    super();
  }

  public KeywordsHot(Long id, String keywords, String ekeywords) {
    this.id = id;
    this.keywords = keywords;
    this.ekeywords = ekeywords;
  }

  public KeywordsHot(Long id, String keywords, String kwTxt, Integer tf, String ekeywords, String ekwTxt, Integer etf,
      Long relHotKey) {
    super();
    this.id = id;
    this.keywords = keywords;
    this.kwTxt = kwTxt;
    this.tf = tf;
    this.ekeywords = ekeywords;
    this.ekwTxt = ekwTxt;
    this.etf = etf;
    this.relHotKey = relHotKey;
  }

  @Id
  @Column(name = "ID")
  public Long getId() {
    return id;
  }

  @Column(name = "KEYWORD")
  public String getKeywords() {
    return keywords;
  }

  @Column(name = "KW_TXT")
  public String getKwTxt() {
    return kwTxt;
  }

  @Column(name = "TF")
  public Integer getTf() {
    return tf;
  }

  @Column(name = "EN_KW")
  public String getEkeywords() {
    return ekeywords;
  }

  @Column(name = "EN_KWTXT")
  public String getEkwTxt() {
    return ekwTxt;
  }

  @Column(name = "ENKW_TF")
  public Integer getEtf() {
    return etf;
  }

  @Transient
  public List<KeywordsHot> getRelKeywords() {
    return relKeywords;
  }

  @Transient
  public Long getRelHotKey() {
    return relHotKey;
  }

  @Transient
  public Double getWeight() {
    return weight;
  }

  public void setWeight(Double weight) {
    this.weight = weight;
  }

  public void setRelKeywords(List<KeywordsHot> relKeywords) {
    this.relKeywords = relKeywords;
  }

  public void setRelHotKey(Long relHotKey) {
    this.relHotKey = relHotKey;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public void setKeywords(String keywords) {
    this.keywords = keywords;
  }

  public void setKwTxt(String kwTxt) {
    this.kwTxt = kwTxt;
  }

  public void setTf(Integer tf) {
    this.tf = tf;
  }

  public void setEkeywords(String ekeywords) {
    this.ekeywords = ekeywords;
  }

  public void setEkwTxt(String ekwTxt) {
    this.ekwTxt = ekwTxt;
  }

  public void setEtf(Integer etf) {
    this.etf = etf;
  }

  @Override
  public int compareTo(KeywordsHot o) {
    // 比权重
    if (o.getWeight() > this.getWeight()) {
      return 1;
    } else if (o.getWeight() < this.getWeight()) {
      return -1;
    }
    return 0;
  }
}
