package com.smate.web.dyn.model.dynamic;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 初始化动态统计表
 * 
 * @author lhd
 *
 */
@Entity
@Table(name = "DYN_STATISTICS")
public class DynStatistics implements Serializable {

  private static final long serialVersionUID = -6667662070563438887L;

  @Id
  @Column(name = "DYN_ID")
  private Long dynId;// 动态id

  @Column(name = "AWARD_COUNT")
  private Integer awardCount;// 点赞数

  @Column(name = "COMMENT_COUNT")
  private Integer commentCount;// 评论数

  @Column(name = "SHARE_COUNT")
  private Integer shareCount;// 分享数

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

  public DynStatistics(Integer awardCount, Integer commentCount, Integer shareCount) {
    super();
    this.awardCount = awardCount;
    this.commentCount = commentCount;
    this.shareCount = shareCount;
  }

  public DynStatistics() {
    super();
  }



}
