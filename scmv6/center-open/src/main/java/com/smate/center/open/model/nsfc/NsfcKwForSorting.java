package com.smate.center.open.model.nsfc;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * 
 * 用于补充学科关键词，排序
 * 
 * 
 */
@Entity
@Table(name = "NSFC_KW_SORTING")
public class NsfcKwForSorting implements Serializable, Comparable<NsfcKwForSorting> {


  /**
   * 
   */
  private static final long serialVersionUID = 7652658291713959585L;
  private Long id;
  private String discode;
  private String kw;
  private Integer tf;
  private Integer coTfSum;
  private Integer language;
  private Integer seq;

  public NsfcKwForSorting() {
    super();
  }

  public NsfcKwForSorting(String discode, String kw, Integer tf, Integer coTfSum, Integer language) {
    super();
    this.discode = discode;
    this.kw = kw;
    this.tf = tf;
    this.coTfSum = coTfSum;
    this.language = language;
  }

  @Id
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_V_KEYWORD_SUBSET", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
  @Column(name = "ID")
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  @Column(name = "DISCODE")
  public String getDiscode() {
    return discode;
  }

  public void setDiscode(String discode) {
    this.discode = discode;
  }

  @Column(name = "KW")
  public String getKw() {
    return kw;
  }

  public void setKw(String kw) {
    this.kw = kw;
  }

  @Column(name = "TF")
  public Integer getTf() {
    return tf;
  }

  public void setTf(Integer tf) {
    this.tf = tf;
  }

  @Column(name = "COTF_SUM")
  public Integer getCoTfSum() {
    return coTfSum;
  }

  public void setCoTfSum(Integer coTfSum) {
    this.coTfSum = coTfSum;
  }

  @Column(name = "LANGUAGE")
  public Integer getLanguage() {
    return language;
  }

  public void setLanguage(Integer language) {
    this.language = language;
  }

  @Column(name = "SEQ")
  public Integer getSeq() {
    return seq;
  }

  public void setSeq(Integer seq) {
    this.seq = seq;
  }

  @Override
  public int compareTo(NsfcKwForSorting o) {
    if (o == null) {
      return 1;
    }
    if (this.tf + this.coTfSum > o.tf + o.coTfSum) {
      return -1;
    } else if (this.tf + this.coTfSum == o.tf + o.coTfSum) {
      return 0;
    } else {
      return 1;
    }
  }

}
