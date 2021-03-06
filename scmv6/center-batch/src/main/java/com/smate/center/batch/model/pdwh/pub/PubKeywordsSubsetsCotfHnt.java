package com.smate.center.batch.model.pdwh.pub;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * @author Administrator
 *
 */
@Entity
@Table(name = "V_KEYWORD_SUBSET_COTF_HNT")
public class PubKeywordsSubsetsCotfHnt implements Serializable, Comparable<PubKeywordsSubsetsCotfHnt> {

  /**
   * 
   */
  private static final long serialVersionUID = 4667652110804627197L;

  private Long id;
  private Integer language;
  private String content;
  private Long contentHashValue;
  private Integer size;
  private String discode;
  private Integer origin; // 关键词子集来源 1.nsfc项目; 2.pdwh基准库成果; 3pdwh基准库专利
  private Integer counts;

  public PubKeywordsSubsetsCotfHnt() {
    super();
  }

  public PubKeywordsSubsetsCotfHnt(Long id, Integer size, String discode, Integer counts) {
    super();
    this.id = id;
    this.size = size;
    this.discode = discode;
    this.counts = counts;
  }

  @Id
  @Column(name = "ID")
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_V_KEYWORD_SUBSET", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  @Column(name = "LANGUAGE")
  public Integer getLanguage() {
    return language;
  }

  public void setLanguage(Integer language) {
    this.language = language;
  }

  @Column(name = "CONTENT")
  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content = content;
  }

  @Column(name = "CONTENT_HASHVALUE")
  public Long getContentHashValue() {
    return contentHashValue;
  }

  public void setContentHashValue(Long contentHashValue) {
    this.contentHashValue = contentHashValue;
  }

  @Column(name = "CONTENT_SIZE")
  public Integer getSize() {
    return size;
  }

  public void setSize(Integer size) {
    this.size = size;
  }

  @Column(name = "DISCODE")
  public String getDiscode() {
    return discode;
  }

  public void setDiscode(String discode) {
    this.discode = discode;
  }

  @Column(name = "ORIGIN")
  public Integer getOrigin() {
    return origin;
  }

  public void setOrigin(Integer origin) {
    this.origin = origin;
  }

  @Column(name = "CO_TF")
  public Integer getCounts() {
    return counts;
  }

  public void setCounts(Integer counts) {
    this.counts = counts;
  }

  @Override
  public int compareTo(PubKeywordsSubsetsCotfHnt o) {
    if (o == null || o.size == null) {
      return 1;
    }
    if (this.size > o.size) {
      return 1;
    } else if (this.size < o.size) {
      return -1;
    } else if (this.size == o.size) {
      if (this.counts > o.counts) {
        return 1;
      } else {
        return -1;
      }
    }
    return 0;
  }

}
