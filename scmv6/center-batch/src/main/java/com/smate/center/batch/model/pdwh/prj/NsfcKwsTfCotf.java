package com.smate.center.batch.model.pdwh.prj;

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
@Table(name = "NSFC_KEYWORDS_TF_COTF")
public class NsfcKwsTfCotf implements Serializable {


  /**
   * 
   */
  private static final long serialVersionUID = -3748339404728123480L;

  private Long id;
  private Integer language;
  private String kwFirst;
  private String kwSecond;
  private Integer size;
  private String discode;
  private Integer counts;


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

  @Column(name = "COUNTS")
  public Integer getCounts() {
    return counts;
  }

  public void setCounts(Integer counts) {
    this.counts = counts;
  }

  @Column(name = "KW_FIRST")
  public String getKwFirst() {
    return kwFirst;
  }

  public void setKwFirst(String kwFirst) {
    this.kwFirst = kwFirst;
  }

  @Column(name = "KW_SECOND")
  public String getKwSecond() {
    return kwSecond;
  }

  public void setKwSecond(String kwSecond) {
    this.kwSecond = kwSecond;
  }

}
