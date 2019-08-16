package com.smate.center.open.model.publication;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * 成果seq实体表
 * 
 * @author Administrator
 *
 */
@Entity
@Table(name = "PUB_SEO_KEYWORD")
public class PubSeoKeyword implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = -1104305258338365920L;
  private Long id = null;
  private Long pubId = null;
  private String seo = null;
  private String seoTxt = null;
  private Integer scource = new Integer(0);
  private Integer locale = new Integer(0);


  public PubSeoKeyword() {
    super();
  }

  public PubSeoKeyword(Long pubId, String seo, String seoTxt, Integer scource, Integer locale) {
    super();
    this.pubId = pubId;
    this.seo = seo;
    this.seoTxt = seoTxt;
    this.scource = scource;
    this.locale = locale;
  }

  @Id
  @Column(name = "ID")
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_PUB_SEO_KEYWORD", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  @Column(name = "PUB_ID")
  public Long getPubId() {
    return pubId;
  }

  public void setPubId(Long pubId) {
    this.pubId = pubId;
  }

  @Column(name = "SEO")
  public String getSeo() {
    return seo;
  }

  public void setSeo(String seo) {
    this.seo = seo;
  }

  @Column(name = "SEO_TXT")
  public String getSeoTxt() {
    return seoTxt;
  }

  public void setSeoTxt(String seoTxt) {
    this.seoTxt = seoTxt;
  }

  @Column(name = "SOURCE")
  public Integer getScource() {
    return scource;
  }

  public void setScource(Integer scource) {
    this.scource = scource;
  }

  @Column(name = "LOCALE")
  public Integer getLocale() {
    return locale;
  }

  public void setLocale(Integer locale) {
    this.locale = locale;
  }
}
