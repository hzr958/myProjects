package com.smate.center.batch.model.sns.pub;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * 根据sns成果推荐出来的相关文献记录
 * 
 * @author lichangwen
 * 
 */
@Entity
@Table(name = "PUB_RELATED")
public class PubRelated implements Serializable {


  /**
   * 
   */
  private static final long serialVersionUID = 171352226677262586L;
  // 主键
  @Id
  @Column(name = "ID")
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_PUB_RELATED", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
  private Long id;
  @Column(name = "PUB_ID")
  private Long pubId;
  @Column(name = "PUBALL_ID")
  private Long pubAllId;
  @Column(name = "SCORE")
  private Double score;
  @Column(name = "LANGUAGE")
  private int language;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Long getPubId() {
    return pubId;
  }

  public void setPubId(Long pubId) {
    this.pubId = pubId;
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

  public int getLanguage() {
    return language;
  }

  public void setLanguage(int language) {
    this.language = language;
  }

}
