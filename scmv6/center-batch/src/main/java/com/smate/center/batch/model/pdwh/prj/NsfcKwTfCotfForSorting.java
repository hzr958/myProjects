package com.smate.center.batch.model.pdwh.prj;

import java.io.Serializable;


public class NsfcKwTfCotfForSorting implements Serializable, Comparable<NsfcKwTfCotfForSorting> {

  /**
   * 
   */
  private static final long serialVersionUID = 4749518005499339497L;

  private String nsfcApplicationCode;
  private String keywords;
  private Long tf;
  private Long cotf;
  private Integer length = 0;
  private Double isiTf;
  private Double realTf;
  private Long bigTf;

  public NsfcKwTfCotfForSorting() {
    super();
  }

  public NsfcKwTfCotfForSorting(String keywords, Integer length) {
    super();
    this.keywords = keywords;
    this.length = length;
  }

  public NsfcKwTfCotfForSorting(String keywords, Integer length, Double isiTf) {
    super();
    this.keywords = keywords;
    this.length = length;
    this.isiTf = isiTf;
  }

  public NsfcKwTfCotfForSorting(String nsfcApplicationCode, String keywords, Long tf, Long cotf) {
    super();
    this.nsfcApplicationCode = nsfcApplicationCode;
    this.keywords = keywords;
    this.tf = tf;
    this.cotf = cotf;
  }


  public NsfcKwTfCotfForSorting(String keywords, Integer length, Double realTf, Double isiTf) {
    super();
    this.keywords = keywords;
    this.length = length;
    this.isiTf = isiTf;
    this.realTf = realTf;
  }

  public NsfcKwTfCotfForSorting(String keywords, Integer length, Long bigTf) {
    super();
    this.keywords = keywords;
    this.length = length;
    this.bigTf = bigTf;
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

  public Double getRealTf() {
    return realTf;
  }

  public void setRealTf(Double realTf) {
    this.realTf = realTf;
  }

  public Long getBigTf() {
    return bigTf;
  }

  public void setBigTf(Long bigTf) {
    this.bigTf = bigTf;
  }

  @Override
  public int compareTo(NsfcKwTfCotfForSorting o) {
    if (o == null) {
      return -1;
    }

    /*
     * if ((o.cotf + o.tf) - (this.cotf + this.tf) > 0L) { return 1; } else { return -1; }
     */
    if (o.isiTf - this.isiTf > 0) {
      return 1;
    } else if (o.isiTf - this.isiTf == 0) {
      if (o.realTf != null && this.realTf != 0) {
        if (o.realTf - this.realTf > 0) {
          return 1;
        } else if (o.realTf - this.realTf == 0) {
          if (o.length - this.length > 0) {
            return 1;
          } else {
            return -1;
          }
        } else {
          return -1;
        }
      } else {
        if (o.length - this.length > 0) {
          return 1;
        } else {
          return -1;
        }
      }
    } else {
      return -1;
    }
  }

  @Override
  public String toString() {
    return this.nsfcApplicationCode + " " + this.keywords + " " + this.tf + " " + this.cotf;
  }
}
