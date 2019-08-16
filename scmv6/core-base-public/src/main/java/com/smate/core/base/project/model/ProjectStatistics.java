package com.smate.core.base.project.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 项目操作统计(包括赞，分享，评论，阅读次数)
 * 
 * @author Scy
 * 
 */
@Entity
@Table(name = "PROJECT_STATISTICS")
public class ProjectStatistics implements Serializable {

  private static final long serialVersionUID = -7562428035870771929L;
  public final static Integer AWARD_TYPE = 1;
  public final static Integer SHARE_TYPE = 2;
  public final static Integer COMMENT_TYPE = 3;
  public final static Integer READ_TYPE = 4;
  public final static Integer REF_TYPE = 5;

  @Id
  @Column(name = "PROJECT_ID")
  private Long projectId;

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

  @Column(name = "RELATED_PUB_COUNT")
  private Integer relatedPubCount;// 相关成果数

  public ProjectStatistics() {
    super();
  }

  public ProjectStatistics(Long projectId, Integer awardCount, Integer shareCount, Integer commentCount,
      Integer readCount, Integer refCount, Integer relatedPubCount) {
    super();
    this.projectId = projectId;
    this.awardCount = awardCount;
    this.shareCount = shareCount;
    this.commentCount = commentCount;
    this.readCount = readCount;
    this.refCount = refCount;
    this.relatedPubCount = relatedPubCount;
  }

  public Long getProjectId() {
    return projectId;
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

  public void setProjectId(Long projectId) {
    this.projectId = projectId;
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

  public Integer getRefCount() {
    return refCount;
  }

  public void setRefCount(Integer refCount) {
    this.refCount = refCount;
  }

  public Integer getRelatedPubCount() {
    return relatedPubCount;
  }

  public void setRelatedPubCount(Integer relatedPubCount) {
    this.relatedPubCount = relatedPubCount;
  }

}
