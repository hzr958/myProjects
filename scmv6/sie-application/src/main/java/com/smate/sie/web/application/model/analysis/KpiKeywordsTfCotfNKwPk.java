package com.smate.sie.web.application.model.analysis;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class KpiKeywordsTfCotfNKwPk implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = 866819905210597651L;
  private String sub3DisCode;
  private String kwFirst;

  public KpiKeywordsTfCotfNKwPk() {
    super();
  }

  public KpiKeywordsTfCotfNKwPk(String sub3DisCode, String kwFirst) {
    super();
    this.sub3DisCode = sub3DisCode;
    this.kwFirst = kwFirst;
  }

  @Column(name = "SUB3_DISCODE")
  public String getSub3DisCode() {
    return sub3DisCode;
  }

  @Column(name = "KW_FIRST")
  public String getKwFirst() {
    return kwFirst;
  }

  public void setSub3DisCode(String sub3DisCode) {
    this.sub3DisCode = sub3DisCode;
  }

  public void setKwFirst(String kwFirst) {
    this.kwFirst = kwFirst;
  }
}
