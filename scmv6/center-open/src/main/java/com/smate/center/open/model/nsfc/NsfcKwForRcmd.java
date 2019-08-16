package com.smate.center.open.model.nsfc;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.springframework.util.StringUtils;

/**
 * 
 * 用于关键词推荐，排序
 * 
 * 
 */
public class NsfcKwForRcmd implements Serializable, Comparable<NsfcKwForRcmd> {


  /**
   * 
   */
  private static final long serialVersionUID = -7462314163352420236L;
  private String discode;
  private String kw;
  private Long tf;
  private Long coTfSum;
  private Integer language;
  private Integer seq;

  public NsfcKwForRcmd() {
    super();
  }

  public NsfcKwForRcmd(String discode, String kw, Long tf, Long coTfSum, Integer language) {
    super();
    this.discode = discode;
    this.kw = kw;
    this.tf = tf;
    this.coTfSum = coTfSum;
    this.language = language;
  }



  public NsfcKwForRcmd(String discode, String kw, Long tf, Long coTfSum) {
    super();
    this.discode = discode;
    this.kw = kw;
    this.tf = tf;
    this.coTfSum = coTfSum;
  }

  public String getDiscode() {
    return discode;
  }

  public void setDiscode(String discode) {
    this.discode = discode;
  }


  public String getKw() {
    return kw;
  }

  public void setKw(String kw) {
    this.kw = kw;
  }


  public Long getTf() {
    return tf;
  }

  public void setTf(Long tf) {
    this.tf = tf;
  }


  public Long getCoTfSum() {
    return coTfSum;
  }

  public void setCoTfSum(Long coTfSum) {
    this.coTfSum = coTfSum;
  }


  public Integer getLanguage() {
    return language;
  }

  public void setLanguage(Integer language) {
    this.language = language;
  }


  public Integer getSeq() {
    return seq;
  }

  public void setSeq(Integer seq) {
    this.seq = seq;
  }

  @Override
  public int compareTo(NsfcKwForRcmd o) {
    if (o == null) {
      return 1;
    }
    if (this.tf + this.coTfSum > o.tf + o.coTfSum) {
      return -1;
    } else if (this.tf + this.coTfSum == o.tf + o.coTfSum) {
      if (!StringUtils.isEmpty(this.kw) && !StringUtils.isEmpty(o.kw)) {
        if (this.kw.length() > o.kw.length()) {
          return -1;
        } else if (this.kw.length() == o.kw.length()) {
          return 0;
        } else {
          return 1;
        }
      } else {
        return 0;
      }
    } else {
      return 1;
    }
  }

  @Override
  public String toString() {
    return "NsfcKwForRcmd [" + discode + "-" + kw + ", tf=" + tf + ", coTfSum=" + coTfSum + "]";
  }


}
