package com.smate.center.batch.model.sns.pub;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * 学科关键字.
 * 
 * @author liqinghua
 * 
 */
@Entity
@Table(name = "CONST_DISCIPLINE_KEY")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class ConstDisciplineKey implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = -6444691320592440081L;

  private Long id;
  private String keyWords;
  private Float impactFactor;

  @Id
  @Column(name = "KEY_ID")
  public Long getId() {
    return id;
  }

  @Column(name = "KEY_WORDS")
  public String getKeyWords() {
    return keyWords;
  }

  @Column(name = "IMPACT_FACTOR")
  public Float getImpactFactor() {
    return impactFactor;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public void setKeyWords(String keyWords) {
    this.keyWords = keyWords;
  }

  public void setImpactFactor(Float impactFactor) {
    this.impactFactor = impactFactor;
  }

}
