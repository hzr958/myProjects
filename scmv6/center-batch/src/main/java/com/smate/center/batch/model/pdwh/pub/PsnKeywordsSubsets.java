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
@Table(name = "V_PSN_KEYWORD_SUBSET")
public class PsnKeywordsSubsets implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = -7178277428471984295L;

  private Long id;
  private Long psnId;
  private Integer language;
  private String content;
  private Long contentHashValue;
  private Integer size;
  private Integer origin; // 关键词子集来源 1.nsfc项目; 2.个人成果; 3个人专利
  private Long pubId;

  public PsnKeywordsSubsets() {
    super();
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

  @Column(name = "PSN_ID")
  public Long getPsnId() {
    return psnId;
  }

  public void setPsnId(Long psnId) {
    this.psnId = psnId;
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

  @Column(name = "ORIGIN")
  public Integer getOrigin() {
    return origin;
  }

  public void setOrigin(Integer origin) {
    this.origin = origin;
  }

  @Column(name = "PUB_ID")
  public Long getPubId() {
    return pubId;
  }

  public void setPubId(Long pubId) {
    this.pubId = pubId;
  }

}
