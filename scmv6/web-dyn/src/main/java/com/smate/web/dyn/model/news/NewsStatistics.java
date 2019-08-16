package com.smate.web.dyn.model.news;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 新闻 统计记录表
 *
 * @author aijiangbin
 * @create 2019-05-15 14:21
 **/
@Entity
@Table(name = "V_NEWS_STATISTICS")
public class NewsStatistics {
  private static final long serialVersionUID = 1L;


  @Id
  @Column(name = "NEWS_ID")
  private Long newsId; // 新闻主键

  @Column(name = "AWARD_COUNT")
  private Integer awardCount = 0; // 赞统计

  @Column(name = "VIEW_COUNT")
  private Integer viewCount = 0; // 访问统计

  @Column(name = "SHARE_COUNT")
  private Integer shareCount = 0; // 分享统计


  public NewsStatistics() {}

  public Long getNewsId() {
    return newsId;
  }

  public void setNewsId(Long newsId) {
    this.newsId = newsId;
  }

  public Integer getAwardCount() {
    return awardCount;
  }

  public void setAwardCount(Integer awardCount) {
    this.awardCount = awardCount;
  }

  public Integer getViewCount() {
    return viewCount;
  }

  public void setViewCount(Integer viewCount) {
    this.viewCount = viewCount;
  }

  public Integer getShareCount() {
    return shareCount;
  }

  public void setShareCount(Integer shareCount) {
    this.shareCount = shareCount;
  }

  public NewsStatistics(Long newsId, Integer awardCount, Integer viewCount, Integer shareCount) {
    super();
    this.newsId = newsId;
    this.awardCount = awardCount;
    this.viewCount = viewCount;
    this.shareCount = shareCount;
  }
}
