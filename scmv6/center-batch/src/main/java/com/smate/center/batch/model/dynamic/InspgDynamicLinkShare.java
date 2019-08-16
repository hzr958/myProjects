package com.smate.center.batch.model.dynamic;

import java.io.Serializable;
import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * 发表新鲜事private 链接
 * 
 * @author hzr
 * 
 * @since 6.0.1
 * @version 6.0.1
 * 
 */
@Document(collection = "InspgDynamic")
public class InspgDynamicLinkShare implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = 3769401663040767211L;
  @Id
  private String uid; // 主键,mongodb内置ObjectId,无需赋值；
  @Indexed
  private Long dynId; // 动态id 与 InspgDynamicPsn\n或InspgDynamicInspg中的dynId对应
  private Integer hasComment; // 是否有评论模块，0无1有
  private Long commentParentId; // 评论所属id, 对应动态id
  private Integer commentType; // 评论类型
  private Integer hasLike; // 是否有赞模块,0无1有
  private Long likeParentId; // 赞所属id, 对应动态id
  private Integer likeType; // 赞类型
  private Long inspgId; // 机构Id
  private Long createTime; // 创建日期

  public InspgDynamicLinkShare() {
    super();
  }

  @Override
  public String toString() {
    return "inspgDynamicLinkShare [uid=" + uid + ", inspgId=" + inspgId + ", createTime=" + createTime + "]";
  }

  public String getUid() {
    return uid;
  }

  public void setUid(String uid) {
    this.uid = uid;
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

  public Long getDynId() {
    return dynId;
  }

  public void setDynId(Long dynId) {
    this.dynId = dynId;
  }



}
