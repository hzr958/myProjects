package com.smate.center.task.model.sns.quartz;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 中文关键热词表.
 * 
 * @author liangguokeng
 * 
 */
@Entity
@Table(name = "DISC_KEYWORD_HOT")
public class DiscKeywordHot implements Serializable, Comparable<DiscKeywordHot> {

  /**
   * 
   */
  private static final long serialVersionUID = 1156273832831035339L;

  private Long id;
  // KEYWORD
  private String keyword;
  // KW_TXT
  private String kwTxt;//
  // WORD_NUM
  private Long wordNum;
  // LAST_YEAR
  private Long lastYear;
  // DISCODE
  private String disCode;
  // HOT_WT
  private double hotWt;

  public DiscKeywordHot() {
    super();
  }

  @Id
  @Column(name = "ID")
  public Long getId() {
    return id;
  }

  @Column(name = "KEYWORD")
  public String getKeyword() {
    return keyword;
  }

  @Column(name = "KW_TXT")
  public String getKwTxt() {
    return kwTxt;
  }

  @Column(name = "WORD_NUM")
  public Long getWordNum() {
    return wordNum;
  }

  @Column(name = "LAST_YEAR")
  public Long getLastYear() {
    return lastYear;
  }

  @Column(name = "DISCODE")
  public String getDisCode() {
    return disCode;
  }

  @Column(name = "HOT_WT")
  public double getHotWt() {
    return hotWt;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public void setKeyword(String keyword) {
    this.keyword = keyword;
  }

  public void setKwTxt(String kwTxt) {
    this.kwTxt = kwTxt;
  }

  public void setWordNum(Long wordNum) {
    this.wordNum = wordNum;
  }

  public void setLastYear(Long lastYear) {
    this.lastYear = lastYear;
  }

  public void setDisCode(String disCode) {
    this.disCode = disCode;
  }

  public void setHotWt(double hotWt) {
    this.hotWt = hotWt;
  }

  @Override
  public int compareTo(DiscKeywordHot o) {
    // 比hotWt
    if (o.getHotWt() > this.getHotWt()) {
      return 1;
    } else if (o.getHotWt() < this.getHotWt()) {
      return -1;
    }
    return 0;
  }

}
