package com.smate.center.merge.model.sns.group;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 群组动态操作统计 记录
 * 
 * @author zzx
 *
 */
@Entity
@Table(name = "V_GROUP_DYNAMIC_STATISTIC")
public class GroupDynamicStatistic {
  @Id
  @Column(name = "DYN_ID")
  private Long dynId;// 动态id
  @Column(name = "AWARD_COUNT")
  private Integer awardCount;// 赞次数
  @Column(name = "COMMENT_COUNT")
  private Integer commentCount;// 评论次数
  @Column(name = "SHARE_COUNT")
  private Integer shareCount;// 分享次数

  public Long getDynId() {
    return dynId;
  }

  public void setDynId(Long dynId) {
    this.dynId = dynId;
  }

  public Integer getAwardCount() {
    return awardCount;
  }

  public void setAwardCount(Integer awardCount) {
    this.awardCount = awardCount;
  }

  public Integer getCommentCount() {
    return commentCount;
  }

  public void setCommentCount(Integer commentCount) {
    this.commentCount = commentCount;
  }

  public Integer getShareCount() {
    return shareCount;
  }

  public void setShareCount(Integer shareCount) {
    this.shareCount = shareCount;
  }

}
