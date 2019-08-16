package com.smate.core.base.statistics.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;


@Entity
@Table(name = "V_RECOMMEND_INIT")
public class RecommendInit implements Serializable {
  private static final long serialVersionUID = -7297354885425518045L;

  @Id
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_V_RECOMMEND_INIT", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
  @Column(name = "ID")
  private Long id;
  @Column(name = "PSN_ID")
  private Long psnId;
  @Column(name = "PUB_RECOMMEND_INIT", columnDefinition = "NUMBER default 0 ", nullable = true)
  private Integer pubRecommendInit;
  @Column(name = "FUND_RECOMMEND_INIT", columnDefinition = "NUMBER default 0 ", nullable = true)
  private Integer fundRecommendInit;

  public RecommendInit(Long id, Long psnId, Integer pubRecommendInit, Integer fundRecommendInit) {
    super();
    this.id = id;
    this.psnId = psnId;
    this.pubRecommendInit = pubRecommendInit;
    this.fundRecommendInit = fundRecommendInit;
  }

  public RecommendInit() {
    super();
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Long getPsnId() {
    return psnId;
  }

  public void setPsnId(Long psnId) {
    this.psnId = psnId;
  }

  public Integer getPubRecommendInit() {
    return pubRecommendInit;
  }

  public void setPubRecommendInit(Integer pubRecommendInit) {
    this.pubRecommendInit = pubRecommendInit;
  }

  public Integer getFundRecommendInit() {
    return fundRecommendInit;
  }

  public void setFundRecommendInit(Integer fundRecommendInit) {
    this.fundRecommendInit = fundRecommendInit;
  }

}
