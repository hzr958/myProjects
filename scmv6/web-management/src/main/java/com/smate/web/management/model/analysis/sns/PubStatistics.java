package com.smate.web.management.model.analysis.sns;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 成果操作统计信息
 * 
 * @author tsz
 * 
 */
@Entity
@Table(name = "PUBLICATION_STATISTICS")
public class PubStatistics implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = 702026465070328357L;
  public final static Integer AWARD_TYPE = 1; // 赞统计数
  public final static Integer SHARE_TYPE = 2; // 分享统计数
  public final static Integer COMMENT_TYPE = 3; // 评论统计数
  public final static Integer READ_TYPE = 4; // 阅读统计数
  public final static Integer REF_TYPE = 5; // 引用统计数

  @Id
  @Column(name = "PUB_ID")
  private Long pubId;

  @Column(name = "AWARD_COUNT")
  private Integer awardCount;

  @Column(name = "SHARE_COUNT")
  private Integer shareCount;

  @Column(name = "COMMENT_COUNT")
  private Integer commentCount;

  @Column(name = "READ_COUNT")
  private Integer readCount;

  @Column(name = "REF_COUNT")
  private Integer refCount;

  public PubStatistics() {
    super();
  }

  public PubStatistics(Long pubId, Integer awardCount, Integer shareCount, Integer commentCount, Integer readCount,
      Integer refCount) {
    super();
    this.pubId = pubId;
    this.awardCount = awardCount;
    this.shareCount = shareCount;
    this.commentCount = commentCount;
    this.readCount = readCount;
    this.refCount = refCount;
  }

  public Long getPubId() {
    return pubId;
  }

  public Integer getAwardCount() {
    return awardCount;
  }

  public Integer getShareCount() {
    return shareCount;
  }

  public Integer getCommentCount() {
    return commentCount;
  }

  public Integer getReadCount() {
    return readCount;
  }

  public Integer getRefCount() {
    return refCount;
  }

  public void setPubId(Long pubId) {
    this.pubId = pubId;
  }

  public void setAwardCount(Integer awardCount) {
    this.awardCount = awardCount;
  }

  public void setShareCount(Integer shareCount) {
    this.shareCount = shareCount;
  }

  public void setCommentCount(Integer commentCount) {
    this.commentCount = commentCount;
  }

  public void setReadCount(Integer readCount) {
    this.readCount = readCount;
  }

  public void setRefCount(Integer refCount) {
    this.refCount = refCount;
  }

}
