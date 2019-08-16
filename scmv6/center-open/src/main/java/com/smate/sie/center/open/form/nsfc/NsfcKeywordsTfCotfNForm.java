package com.smate.sie.center.open.form.nsfc;

import java.io.Serializable;

/**
 * 关键词与学科关系
 * 
 * @author sjzhou
 *
 */
public class NsfcKeywordsTfCotfNForm implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = 4186362506792297693L;

  private String nsfcId;
  private String disCode;
  private String sub3DisCode;
  private String kwFirst;
  private String kwSecond;
  private Integer counts;

  public String getNsfcId() {
    return nsfcId;
  }

  public String getDisCode() {
    return disCode;
  }

  public String getSub3DisCode() {
    return sub3DisCode;
  }

  public String getKwFirst() {
    return kwFirst;
  }

  public String getKwSecond() {
    return kwSecond;
  }

  public Integer getCounts() {
    return counts;
  }

  public void setNsfcId(String nsfcId) {
    this.nsfcId = nsfcId;
  }

  public void setDisCode(String disCode) {
    this.disCode = disCode;
  }

  public void setSub3DisCode(String sub3DisCode) {
    this.sub3DisCode = sub3DisCode;
  }

  public void setKwFirst(String kwFirst) {
    this.kwFirst = kwFirst;
  }

  public void setKwSecond(String kwSecond) {
    this.kwSecond = kwSecond;
  }

  public void setCounts(Integer counts) {
    this.counts = counts;
  }

}
