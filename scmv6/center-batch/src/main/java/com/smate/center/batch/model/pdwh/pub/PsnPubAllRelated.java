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
 * 推荐出来的文献记录
 * 
 * @author warrior
 * 
 */
@Entity
@Table(name = "PSN_PUBALL_RELATED")
public class PsnPubAllRelated implements Serializable {

  private static final long serialVersionUID = 1527339935789670989L;
  // 主键
  @Id
  @Column(name = "ID")
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_PSN_PUBALL", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
  private Long Id;
  @Column(name = "PSN_ID")
  private Long psnId;
  @Column(name = "PUBALL_ID")
  private Long pubAllId;
  @Column(name = "SCORE")
  private Double score;
  @Column(name = "KEYWORDS")
  private String keywords;

  public Long getId() {
    return Id;
  }

  public void setId(Long id) {
    Id = id;
  }

  public Long getPsnId() {
    return psnId;
  }

  public void setPsnId(Long psnId) {
    this.psnId = psnId;
  }

  public Long getPubAllId() {
    return pubAllId;
  }

  public void setPubAllId(Long pubAllId) {
    this.pubAllId = pubAllId;
  }

  public Double getScore() {
    return score;
  }

  public void setScore(Double score) {
    this.score = score;
  }

  public String getKeywords() {
    return keywords;
  }

  public void setKeywords(String keywords) {
    this.keywords = keywords;
  }

}
