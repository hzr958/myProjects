package com.smate.center.task.model.sns.quartz;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 成果操作统计信息
 * 
 * @author zk
 * 
 */
@Entity
@Table(name = "PUBLICATION_STATISTICS")
public class PublicationStatistics implements Serializable {

  private static final long serialVersionUID = 4602149204828469596L;
  public final static Integer AWARD_TYPE = 1;
  public final static Integer SHARE_TYPE = 2;
  public final static Integer COMMENT_TYPE = 3;
  public final static Integer READ_TYPE = 4;
  public final static Integer REF_TYPE = 5;

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

  public PublicationStatistics() {
    super();
  }

  public PublicationStatistics(Long pubId) {
    super();
    this.pubId = pubId;
  }

  public PublicationStatistics(Long pubId, Integer awardCount, Integer shareCount, Integer commentCount,
      Integer readCount, Integer refCount) {
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
