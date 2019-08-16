package com.smate.center.task.model.sns.quartz;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "KEYWORDS_COMMEND")
public class KeywordsCommend implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = 352418273284871194L;
  private Long id;
  private String zhkw;
  private String zhkwTxt;
  private Long zhkwHash;
  private Long zhkwRhash;
  private String enkw;
  private String enkwTxt;
  private Long enkwHash;
  private Long enkwRhash;
  // 出现频率
  private Long frequency;
  // 趋势，09年-10年增长率
  private Float trend;
  // zh,en
  private String kwLang;

  public KeywordsCommend() {
    super();
  }

  public KeywordsCommend(Long id, String zhkw, String enkw, String kwLang) {
    super();
    this.id = id;
    this.zhkw = zhkw;
    this.enkw = enkw;
    this.kwLang = kwLang;
  }

  @Id
  @Column(name = "ID")
  public Long getId() {
    return id;
  }

  @Column(name = "ZHKW")
  public String getZhkw() {
    return zhkw;
  }

  @Column(name = "ZHKW_TXT")
  public String getZhkwTxt() {
    return zhkwTxt;
  }

  @Column(name = "ZHKW_HASH")
  public Long getZhkwHash() {
    return zhkwHash;
  }

  @Column(name = "ZHKW_RHASH")
  public Long getZhkwRhash() {
    return zhkwRhash;
  }

  @Column(name = "ENKW")
  public String getEnkw() {
    return enkw;
  }

  @Column(name = "ENKW_TXT")
  public String getEnkwTxt() {
    return enkwTxt;
  }

  @Column(name = "ENKW_HASH")
  public Long getEnkwHash() {
    return enkwHash;
  }

  @Column(name = "FREQUENCY")
  public Long getFrequency() {
    return frequency;
  }

  @Column(name = "TREND")
  public Float getTrend() {
    return trend;
  }

  @Column(name = "ENKW_RHASH")
  public Long getEnkwRhash() {
    return enkwRhash;
  }

  @Transient
  public String getKwLang() {
    return kwLang;
  }

  public void setKwLang(String kwLang) {
    this.kwLang = kwLang;
  }

  public void setEnkwRhash(Long enkwRhash) {
    this.enkwRhash = enkwRhash;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public void setZhkw(String zhkw) {
    this.zhkw = zhkw;
  }

  public void setZhkwTxt(String zhkwTxt) {
    this.zhkwTxt = zhkwTxt;
  }

  public void setZhkwHash(Long zhkwHash) {
    this.zhkwHash = zhkwHash;
  }

  public void setZhkwRhash(Long zhkwRhash) {
    this.zhkwRhash = zhkwRhash;
  }

  public void setEnkw(String enkw) {
    this.enkw = enkw;
  }

  public void setEnkwTxt(String enkwTxt) {
    this.enkwTxt = enkwTxt;
  }

  public void setEnkwHash(Long enkwHash) {
    this.enkwHash = enkwHash;
  }

  public void setFrequency(Long frequency) {
    this.frequency = frequency;
  }

  public void setTrend(Float trend) {
    this.trend = trend;
  }
}
