package com.smate.web.dyn.model.pub;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 个人库成果操作统计表
 * 
 * @author YJ 2018年5月31号
 */
@Entity
@Table(name = "V_PUB_STATISTICS")
public class PubStatisticsPO implements Serializable {

  private static final long serialVersionUID = 520924935675699660L;

  @Id
  @Column(name = "PUB_ID")
  private Long pubId; // 成果id

  @Column(name = "AWARD_COUNT", columnDefinition = "INT default 0")
  private Integer awardCount = 0; // 赞数

  @Column(name = "SHARE_COUNT", columnDefinition = "INT default 0")
  private Integer shareCount = 0; // 分享数

  @Column(name = "COMMENT_COUNT", columnDefinition = "INT default 0")
  private Integer commentCount = 0; // 评论数

  @Column(name = "READ_COUNT", columnDefinition = "INT default 0")
  private Integer readCount = 0; // 阅读数

  @Column(name = "REF_COUNT", columnDefinition = "INT default 0")
  private Integer refCount = 0; // 引用数

  public PubStatisticsPO() {
    super();
  }

  public PubStatisticsPO(Long pubId) {
    super();
    this.pubId = pubId;
  }

  public PubStatisticsPO(Long pubId, Integer awardCount, Integer shareCount, Integer commentCount, Integer readCount,
      Integer refCount) {
    super();
    this.pubId = pubId;
    this.awardCount = awardCount;
    this.shareCount = shareCount;
    this.commentCount = commentCount;
    this.readCount = readCount;
    this.refCount = refCount;
  }

  public PubStatisticsPO(Long pubId, Integer awardCount, Integer shareCount) {
    super();
    this.pubId = pubId;
    this.awardCount = awardCount;
    this.shareCount = shareCount;
  }

  public Long getPubId() {
    return pubId;
  }

  public void setPubId(Long pubId) {
    this.pubId = pubId;
  }

  public Integer getAwardCount() {
    return awardCount;
  }

  public void setAwardCount(Integer awardCount) {
    this.awardCount = awardCount;
  }

  public Integer getShareCount() {
    return shareCount;
  }

  public void setShareCount(Integer shareCount) {
    this.shareCount = shareCount;
  }

  public Integer getCommentCount() {
    return commentCount;
  }

  public void setCommentCount(Integer commentCount) {
    this.commentCount = commentCount;
  }

  public Integer getReadCount() {
    return readCount;
  }

  public void setReadCount(Integer readCount) {
    this.readCount = readCount;
  }

  public Integer getRefCount() {
    return refCount;
  }

  public void setRefCount(Integer refCount) {
    this.refCount = refCount;
  }

  @Override
  public String toString() {
    return "PubStatisticsPO{" + "pubId='" + pubId + '\'' + ", awardCount='" + awardCount + '\'' + ", shareCount='"
        + shareCount + '\'' + ", commentCount='" + commentCount + '\'' + ", readCount='" + readCount + '\''
        + ", refCount='" + refCount + '\'' + '}';
  }

}
