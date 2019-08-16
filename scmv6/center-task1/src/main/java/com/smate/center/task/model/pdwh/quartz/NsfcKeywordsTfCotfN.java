package com.smate.center.task.model.pdwh.quartz;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 关键词与学科关系统计表
 * 
 * @author sjzhou
 *
 */
@Entity
@Table(name = "NSFC_KEYWORDS_TF_COTF_N")
public class NsfcKeywordsTfCotfN implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = 4186362506792297693L;

  private Long id;
  private Integer language;
  private Integer contentSize;
  private String disCode;
  private String kwFirst;
  private String kwSecond;
  private Integer counts;

  @Id
  @Column(name = "ID")
  public Long getId() {
    return id;
  }

  @Column(name = "LANGUAGE")
  public Integer getLanguage() {
    return language;
  }

  @Column(name = "CONTENT_SIZE")
  public Integer getContentSize() {
    return contentSize;
  }

  @Column(name = "DISCODE")
  public String getDisCode() {
    return disCode;
  }

  @Column(name = "KW_FIRST")
  public String getKwFirst() {
    return kwFirst;
  }

  @Column(name = "KW_SECOND")
  public String getKwSecond() {
    return kwSecond;
  }

  @Column(name = "COUNTS")
  public Integer getCounts() {
    return counts;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public void setLanguage(Integer language) {
    this.language = language;
  }

  public void setContentSize(Integer contentSize) {
    this.contentSize = contentSize;
  }

  public void setDisCode(String disCode) {
    this.disCode = disCode;
  }

  public void setKwFirst(String kwFirst) {
    this.kwFirst = kwFirst;
  }

  public void setKwSecond(String kwSecond) {
    this.kwSecond = kwSecond;
  }

  public void setCounts(Integer counts) {
    this.counts = counts;
  }

}
