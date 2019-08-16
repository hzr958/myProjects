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
@Table(name = "V_PSN_KEYWORD_SUBSET_COTF")
public class PsnKeywordsSubsetsCotf implements Serializable, Comparable<PsnKeywordsSubsetsCotf> {

  /**
   * 
   */
  private static final long serialVersionUID = 4667652110804627197L;

  private Long id;
  private Integer language;
  private String content;
  private String contentMd5Code;
  private Integer size;
  private String discode;
  private Integer origin; // 关键词子集来源 1.nsfc项目; 2.pdwh基准库成果; 3pdwh基准库专利
  private Integer counts;

  public PsnKeywordsSubsetsCotf() {
    super();
  }

  public PsnKeywordsSubsetsCotf(Long id, Integer size, String discode, Integer counts) {
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

  @Column(name = "CONTENT_MD5_CODE")
  public String getContentMd5Code() {
    return contentMd5Code;
  }

  public void setContentMd5Code(String contentMd5Code) {
    this.contentMd5Code = contentMd5Code;
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

  @Column(name = "COUNTS")
  public Integer getCounts() {
    return counts;
  }

  public void setCounts(Integer counts) {
    this.counts = counts;
  }

  @Override
  public int compareTo(PsnKeywordsSubsetsCotf o) {
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
