package com.smate.center.task.model.pdwh.pub;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * 成果统计数表
 * 
 * @author zx
 *
 */
@Entity
@Table(name = "PDWH_PUB_STATISTICS")
public class PdwhPubStatistics implements Serializable {

  private static final long serialVersionUID = -7058114571590782644L;
  private Long pubId;// 成果ID
  private Integer dbId;// 网站ID
  private Integer awardCount;// 点赞数
  private Integer shareCount;// 分享数
  private Integer commentCount;// 评论数
  private Integer readCount;// 阅读数
  private Integer citedCount; // 引用数

  public PdwhPubStatistics() {
    super();
  }

  public PdwhPubStatistics(Long pubId, Integer awardCount, Integer shareCount, Integer commentCount,
      Integer readCount) {
    super();
    this.pubId = pubId;
    this.awardCount = awardCount;
    this.shareCount = shareCount;
    this.commentCount = commentCount;
    this.readCount = readCount;
  }

  public PdwhPubStatistics(Long pubId, Integer dbId, Integer awardCount, Integer shareCount, Integer commentCount,
      Integer readCount, Integer citedCount) {
    super();
    this.pubId = pubId;
    this.dbId = dbId;
    this.awardCount = awardCount;
    this.shareCount = shareCount;
    this.commentCount = commentCount;
    this.readCount = readCount;
    this.citedCount = citedCount;
  }

  @Id
  @Column(name = "PUB_ID")
  public Long getPubId() {
    return pubId;
  }

  public void setPubId(Long pubId) {
    this.pubId = pubId;
  }

  @Column(name = "DB_ID")
  public Integer getDbId() {
    return dbId;
  }

  public void setDbId(Integer dbId) {
    this.dbId = dbId;
  }

  @Column(name = "AWARD_COUNT")
  public Integer getAwardCount() {
    return awardCount;
  }

  public void setAwardCount(Integer awardCount) {
    this.awardCount = awardCount;
  }

  @Column(name = "SHARE_COUNT")
  public Integer getShareCount() {
    return shareCount;
  }

  public void setShareCount(Integer shareCount) {
    this.shareCount = shareCount;
  }

  @Column(name = "COMMENT_COUNT")
  public Integer getCommentCount() {
    return commentCount;
  }

  public void setCommentCount(Integer commentCount) {
    this.commentCount = commentCount;
  }

  @Column(name = "READ_COUNT")
  public Integer getReadCount() {
    return readCount;
  }

  public void setReadCount(Integer readCount) {
    this.readCount = readCount;
  }

  @Transient
  public Integer getCitedCount() {
    return citedCount;
  }

  public void setCitedCount(Integer citedCount) {
    this.citedCount = citedCount;
  }

}
