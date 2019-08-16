package com.smate.center.task.model.sns.psn;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * 个人好友系统智能推荐得分表.
 * 
 * @author lichangwen
 * 
 */
@Entity
@Table(name = "PSN_FRIEND_RECOMMEND_SCORE")
public class FriendSysRecommendScore implements Serializable {

  private static final long serialVersionUID = 5692611410845139140L;
  // pk
  private Long id;
  // 个人psnId
  private Long psnId;
  // 推荐好友psnId
  private Long recommendPsnId;
  // 推荐度得分
  private Double recommendScore;
  // 推荐来源：1工作经历，2教育经历，3好友的好友，4成果合作，5项目合作，6科研领域
  private Integer recommendType;

  public FriendSysRecommendScore() {
    super();
  }

  public FriendSysRecommendScore(Long psnId, Long recommendPsnId, Double recommendScore, Integer recommendType) {
    super();
    this.psnId = psnId;
    this.recommendPsnId = recommendPsnId;
    this.recommendScore = recommendScore;
    this.recommendType = recommendType;
  }

  @Id
  @Column(name = "ID")
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_PSN_FRIEND_RECOMMEND_SCORE", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  @Column(name = "PSN_ID")
  public Long getPsnId() {
    return psnId;
  }

  public void setPsnId(Long psnId) {
    this.psnId = psnId;
  }

  @Column(name = "RECOMMEND_PSN_ID")
  public Long getRecommendPsnId() {
    return recommendPsnId;
  }

  public void setRecommendPsnId(Long recommendPsnId) {
    this.recommendPsnId = recommendPsnId;
  }

  @Column(name = "RECOMMEND_SCORE")
  public Double getRecommendScore() {
    return recommendScore;
  }

  public void setRecommendScore(Double recommendScore) {
    this.recommendScore = recommendScore;
  }

  @Column(name = "RECOMMEND_TYPE")
  public Integer getRecommendType() {
    return recommendType;
  }

  public void setRecommendType(Integer recommendType) {
    this.recommendType = recommendType;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

}
