package com.smate.web.management.model.analysis;

import java.io.Serializable;

/**
 * 基金合作者推荐：合作度实体类.
 * 
 * @author zhuangyanming
 * 
 */
public class DegreeScore implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = -1569086517006373964L;
  // 好友加分
  private Integer friendScore = 0;
  // 合作者加分
  private Integer cooperatorScore = 0;

  public Integer getFriendScore() {
    return friendScore;
  }

  public Integer getCooperatorScore() {
    return cooperatorScore;
  }

  public void setFriendScore(Integer friendScore) {
    this.friendScore = friendScore;
  }

  public void setCooperatorScore(Integer cooperatorScore) {
    this.cooperatorScore = cooperatorScore;
  }

  // 合作度总分
  public Integer getTotalScore() {

    return friendScore + cooperatorScore > 0 ? 1 : 0;
  }
}
