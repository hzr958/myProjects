package com.smate.web.management.model.analysis.sns;

import java.io.Serializable;

/**
 * 基金合作者推荐：相关度实体类.
 * 
 * @author zhuangyanming
 * 
 */
public class RelevanceScore implements Serializable {


  /**
   * 
   */
  private static final long serialVersionUID = -2557918506557765410L;
  // 相同关键词加分
  private Integer kwScore = 0;
  // 相同院系加分
  private Integer deptScore = 0;
  // 相同发表期刊加分
  private Integer jnlScore = 0;
  // 相同所教课程加分
  private Integer taughtScore = 0;

  public Integer getKwScore() {
    return kwScore;
  }

  public Integer getDeptScore() {
    return deptScore;
  }

  public Integer getJnlScore() {
    return jnlScore;
  }

  public Integer getTaughtScore() {
    return taughtScore;
  }

  public void setKwScore(Integer kwScore) {
    this.kwScore = kwScore;
  }

  public void setDeptScore(Integer deptScore) {
    this.deptScore = deptScore;
  }

  public void setJnlScore(Integer jnlScore) {
    this.jnlScore = jnlScore;
  }

  public void setTaughtScore(Integer taughtScore) {
    this.taughtScore = taughtScore;
  }

  // 相关度总分
  public Integer getTotalScore() {

    return this.kwScore + this.deptScore + this.jnlScore + this.taughtScore;
  }

  // 相关度分数为0,终止推荐
  public Boolean isBreak() {
    return getTotalScore() == 0;
  }
}
