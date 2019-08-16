package com.smate.web.management.model.analysis.sns;

import java.io.Serializable;

import com.smate.web.management.model.analysis.DegreeScore;

/**
 * 基金合作者推荐：推荐度实体类.
 * 
 * @author zhuangyanming
 * 
 */
public class RecommendScore implements Comparable<RecommendScore>, Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = 5113526866509702171L;
  // 用户psnId
  private Long psnId;
  // 合作者psnId
  private Long coPsnId;
  // 质量
  private Integer qualityScore = 0;
  // 总分
  private Double totalScore = 0.0;
  // 合作度
  private final DegreeScore degreeScore = new DegreeScore();
  // 相关度
  private final RelevanceScore relevanceScore = new RelevanceScore();

  public RecommendScore() {

  }

  public RecommendScore(Long coPsnId, Long kwScore) {
    this.coPsnId = coPsnId;
    this.relevanceScore.setKwScore(kwScore.intValue());
  }

  public Integer getQualityScore() {
    return qualityScore;
  }

  public DegreeScore getDegreeScore() {
    return degreeScore;
  }

  public RelevanceScore getRelevanceScore() {
    return relevanceScore;
  }

  public void setQualityScore(Integer qualityScore) {
    this.qualityScore = qualityScore;
  }

  // 推荐度总分：相关度*Ln(2.72+质量 +合作度)
  public Double getTotalScore() {
    totalScore = relevanceScore.getTotalScore() * Math.log(2.72 + qualityScore + degreeScore.getTotalScore());
    return totalScore;
  }

  public Long getPsnId() {
    return psnId;
  }

  public Long getCoPsnId() {
    return coPsnId;
  }

  @Override
  public int compareTo(RecommendScore o) {
    double a = this.getTotalScore();
    double b = o.getTotalScore();
    if (a > b) {
      return -1;
    } else if (a == b) {
      return 0;
    } else {
      return 1;
    }
  }

  public void setPsnId(Long psnId) {
    this.psnId = psnId;
  }

  public void setCoPsnId(Long coPsnId) {
    this.coPsnId = coPsnId;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((coPsnId == null) ? 0 : coPsnId.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    RecommendScore other = (RecommendScore) obj;
    if (coPsnId == null) {
      if (other.coPsnId != null)
        return false;
    } else if (!coPsnId.equals(other.coPsnId))
      return false;
    return true;
  }

}
