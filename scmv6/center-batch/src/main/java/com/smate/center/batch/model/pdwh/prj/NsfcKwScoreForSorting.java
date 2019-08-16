package com.smate.center.batch.model.pdwh.prj;

import java.io.Serializable;


public class NsfcKwScoreForSorting implements Serializable, Comparable<NsfcKwScoreForSorting> {


  /**
   * 
   */
  private static final long serialVersionUID = -3335290547265611310L;
  private String nsfcApplicationCode;
  private String keywords;
  private Long tf;
  private Long cotf;
  private Integer length = 0;
  private Double isiTf;
  private Integer prjkwScore;

  public NsfcKwScoreForSorting() {
    super();
  }


  public NsfcKwScoreForSorting(String keywords, Integer length, Integer prjkwScore) {
    super();
    this.keywords = keywords;
    this.length = length;
    this.prjkwScore = prjkwScore;
  }



  public String getNsfcApplicationCode() {
    return nsfcApplicationCode;
  }

  public void setNsfcApplicationCode(String nsfcApplicationCode) {
    this.nsfcApplicationCode = nsfcApplicationCode;
  }

  public String getKeywords() {
    return keywords;
  }

  public void setKeywords(String keywords) {
    this.keywords = keywords;
  }

  public Long getTf() {
    return tf;
  }

  public void setTf(Long tf) {
    this.tf = tf;
  }

  public Long getCotf() {
    return cotf;
  }

  public void setCotf(Long cotf) {
    this.cotf = cotf;
  }

  public Integer getLength() {
    return length;
  }

  public void setLength(Integer length) {
    this.length = length;
  }

  public Double getIsiTf() {
    return isiTf;
  }

  public void setIsiTf(Double isiTf) {
    this.isiTf = isiTf;
  }

  public Integer getPrjkwScore() {
    return prjkwScore;
  }

  public void setPrjkwScore(Integer prjkwScore) {
    this.prjkwScore = prjkwScore;
  }

  @Override
  public String toString() {
    return this.nsfcApplicationCode + " " + this.keywords + " " + this.tf + " " + this.cotf;
  }

  @Override
  public int compareTo(NsfcKwScoreForSorting o) {
    if (o == null) {
      return -1;
    }
    if (o.prjkwScore - this.prjkwScore > 0) {
      return 1;
    } else if (o.prjkwScore - this.prjkwScore == 0) {
      if (o.length - this.length > 0) {
        return 1;
      } else if (o.length - this.length == 0) {
        return 0;
      } else {
        return -1;
      }
    } else {
      return -1;
    }
  }
}
