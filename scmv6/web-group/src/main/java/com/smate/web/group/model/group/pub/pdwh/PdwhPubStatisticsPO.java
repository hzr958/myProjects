package com.smate.web.group.model.group.pub.pdwh;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 基准库成果操作统计表
 * 
 * @author YJ 2018年5月31号
 */
@Entity
@Table(name = "V_PDWH_STATISTICS")
public class PdwhPubStatisticsPO implements Serializable {

  private static final long serialVersionUID = 9064204660239692298L;

  @Id
  @Column(name = "PDWH_PUB_ID")
  private Long pdwhPubId; // 基准库成果id

  @Column(name = "AWARD_COUNT", columnDefinition = "INT default 0")
  private Integer awardCount; // 赞数

  @Column(name = "SHARE_COUNT", columnDefinition = "INT default 0")
  private Integer shareCount; // 分享数

  @Column(name = "COMMENT_COUNT", columnDefinition = "INT default 0")
  private Integer commentCount; // 评论数

  @Column(name = "READ_COUNT", columnDefinition = "INT default 0")
  private Integer readCount; // 阅读数

  @Column(name = "REF_COUNT", columnDefinition = "INT default 0")
  private Integer refCount; // 引用数

  public PdwhPubStatisticsPO() {
    super();
  }

  public PdwhPubStatisticsPO(Long pdwhPubId) {
    super();
    this.pdwhPubId = pdwhPubId;
  }

  public PdwhPubStatisticsPO(Long pdwhPubId, Integer awardCount, Integer shareCount, Integer commentCount,
      Integer readCount, Integer refCount) {
    super();
    this.pdwhPubId = pdwhPubId;
    this.awardCount = awardCount;
    this.shareCount = shareCount;
    this.commentCount = commentCount;
    this.readCount = readCount;
    this.refCount = refCount;
  }

  public Long getPdwhPubId() {
    return pdwhPubId;
  }

  public void setPdwhPubId(Long pdwhPubId) {
    this.pdwhPubId = pdwhPubId;
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
    return "PdwhStatisticsPO{" + "pdwhPubId='" + pdwhPubId + '\'' + ", awardCount='" + awardCount + '\''
        + ", shareCount='" + shareCount + '\'' + ", commentCount='" + commentCount + '\'' + ", readCount='" + readCount
        + '\'' + ", refCount='" + refCount + '\'' + '}';
  }
}
