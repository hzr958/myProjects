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
 * 基金委学科主任维护关键词表
 * 
 **/
@Entity
@Table(name = "NSFC_KEYWORDS_DISCIPLINE")
public class NsfcKwDiscipline implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = 4749518005499339497L;

  private Long id;
  private String nsfcApplicationCode;
  private String keywords;

  public NsfcKwDiscipline() {
    super();
  }

  @Id
  @Column(name = "ID")
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  @Column(name = "NSFC_APPLICATION_CODE")
  public String getNsfcApplicationCode() {
    return nsfcApplicationCode;
  }

  public void setNsfcApplicationCode(String nsfcApplicationCode) {
    this.nsfcApplicationCode = nsfcApplicationCode;
  }

  @Column(name = "KEYWORD")
  public String getKeywords() {
    return keywords;
  }

  public void setKeywords(String keywords) {
    this.keywords = keywords;
  }

}
