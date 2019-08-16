package com.smate.center.task.model.pdwh.pub;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * 成果关键词表
 * 
 * @author zjh
 *
 */
@Entity
@Table(name = "PDWH_PUB_KEYWORDS")
public class PdwhPubKeywords implements Serializable {
  /**
   * 
   */
  private static final long serialVersionUID = 4181829242253565659L;
  private Long id;
  private Long pubId;
  private String keywords;
  private Integer language;// (1英文，2中文)
  private Integer status;// 0=未处理 ， 1=已经处理


  public PdwhPubKeywords() {
    super();
  }

  public PdwhPubKeywords(Long pubId, String keywords, int language) {
    super();
    this.pubId = pubId;
    this.keywords = keywords;
    this.language = language;
  }

  @Id
  @Column(name = "ID")
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_PDWH_PUB_KEYWORDS", allocationSize = 1)
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

  @Column(name = "KEYWORDS")
  public String getKeywords() {
    return keywords;
  }

  public void setKeywords(String keywords) {
    this.keywords = keywords;
  }

  @Column(name = "LANGUAGE")
  public Integer getLanguage() {
    return language;
  }

  public void setLanguage(Integer language) {
    this.language = language;
  }

  @Column(name = "STATUS")
  public Integer getStatus() {
    return status;
  }

  public void setStatus(Integer status) {
    this.status = status;
  }

}
