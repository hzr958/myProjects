package com.smate.center.batch.model.dynamic;

import java.io.Serializable;
import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * 新增新闻
 * 
 * @author hzr
 * 
 * @since 6.0.1
 * @version 6.0.1
 * 
 */
@Document(collection = "InspgDynamic")
public class InspgDynamicNewsAdd implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = -588190337516663044L;
  @Id
  private String uid; // 主键,mongodb内置ObjectId,无需赋值；
  @Indexed
  private Long dynId; // 动态id 与 InspgDynamicPsn\n或InspgDynamicInspg中的dynId对应
  private Integer hasComment; // 是否有评论模块，0无1有
  private Long commentParentId; // 评论所属id,.对应newsId
  private Integer commentType; // 评论类型
  private Integer hasLike; // 是否有赞模块,0无1有
  private Long likeParentId; // 赞所属id,对应newsId
  private Integer likeType; // 赞类型
  private Long inspgId; // 机构Id
  private Long createTime; // 创建日期
  private String newsTitle; // 文件名称
  private Long newsId; // 文件Id
  private String newsUrl; // 文件链接

  public InspgDynamicNewsAdd() {
    super();
  }

  @Override
  public String toString() {
    return "inspgDynamicNewsAdd [uid=" + uid + ", hasComment=" + hasComment + ", commentParentId=" + commentParentId
        + ", commentType=" + commentType + ", hasLike=" + hasLike + ", likeParentId=" + likeParentId + ", likeType="
        + likeType + ", inspgId=" + inspgId + ", createTime=" + createTime + ", newsTitle=" + newsTitle + ", newsId="
        + newsId + ", newsUrl=" + newsUrl + "]";
  }

  public String getUid() {
    return uid;
  }

  public void setUid(String uid) {
    this.uid = uid;
  }

  public Integer getHasComment() {
    return hasComment;
  }

  public void setHasComment(Integer hasComment) {
    this.hasComment = hasComment;
  }

  public Long getCommentParentId() {
    return commentParentId;
  }

  public void setCommentParentId(Long commentParentId) {
    this.commentParentId = commentParentId;
  }

  public Integer getCommentType() {
    return commentType;
  }

  public void setCommentType(Integer commentType) {
    this.commentType = commentType;
  }

  public Integer getHasLike() {
    return hasLike;
  }

  public void setHasLike(Integer hasLike) {
    this.hasLike = hasLike;
  }

  public Long getLikeParentId() {
    return likeParentId;
  }

  public void setLikeParentId(Long likeParentId) {
    this.likeParentId = likeParentId;
  }

  public Integer getLikeType() {
    return likeType;
  }

  public void setLikeType(Integer likeType) {
    this.likeType = likeType;
  }

  public Long getInspgId() {
    return inspgId;
  }

  public void setInspgId(Long inspgId) {
    this.inspgId = inspgId;
  }

  public Long getCreateTime() {
    return createTime;
  }

  public void setCreateTime(Long createTime) {
    this.createTime = createTime;
  }

  public String getNewsTitle() {
    return newsTitle;
  }

  public void setNewsTitle(String newsTitle) {
    this.newsTitle = newsTitle;
  }

  public Long getNewsId() {
    return newsId;
  }

  public void setNewsId(Long newsId) {
    this.newsId = newsId;
  }

  public String getNewsUrl() {
    return newsUrl;
  }

  public void setNewsUrl(String newsUrl) {
    this.newsUrl = newsUrl;
  }

  public Long getDynId() {
    return dynId;
  }

  public void setDynId(Long dynId) {
    this.dynId = dynId;
  }


}
