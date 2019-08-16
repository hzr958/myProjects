package com.smate.center.batch.model.pdwh.pub.cnki;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * cnki关键词.
 * 
 * @author liqinghua
 * 
 */
@Entity
@Table(name = "CNKI_PUB_KEYWORDS")
public class CnkiPubKeywords implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = 6024529921730675727L;

  private Long id;
  private Long pubId;
  private String keywords;
  // 英文1，中文2
  private Integer type;

  public CnkiPubKeywords() {
    super();
  }

  public CnkiPubKeywords(Long pubId, String keywords, Integer type) {
    super();
    this.pubId = pubId;
    this.keywords = keywords;
    this.type = type;
  }

  @Id
  @Column(name = "ID")
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_CNKI_PUB_KEYWORDS", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
  public Long getId() {
    return id;
  }

  @Column(name = "PUB_ID")
  public Long getPubId() {
    return pubId;
  }

  @Column(name = "KEYWORDS")
  public String getKeywords() {
    return keywords;
  }

  @Column(name = "TYPE")
  public Integer getType() {
    return type;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public void setPubId(Long pubId) {
    this.pubId = pubId;
  }

  public void setKeywords(String keywords) {
    this.keywords = keywords;
  }

  public void setType(Integer type) {
    this.type = type;
  }

}
