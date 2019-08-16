package com.smate.center.batch.model.dynamic;

import java.io.Serializable;
import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * 新增成员
 * 
 * @author hzr
 * 
 * @since 6.0.1
 * @version 6.0.1
 * 
 */
@Document(collection = "InspgDynamic")
public class InspgDynamicMemberAdd implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = 6274939681738236894L;
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
  private Long memberId; // 成员Id
  private String memberName; // 成员姓名
  private String memberDesp; // 成员描述
  private String memberAvatar; // 成员头像
  private String memberHomePage; // 成员主页地址

  @Override
  public String toString() {
    return "inspgDynamicMemberAdd [uid=" + uid + ", hasComment=" + hasComment + ", commentParentId=" + commentParentId
        + ", commentType=" + commentType + ", hasLike=" + hasLike + ", likeParentId=" + likeParentId + ", likeType="
        + likeType + ", inspgId=" + inspgId + ", createTime=" + createTime + ", memberId=" + memberId + ", memberName="
        + memberName + ", memberDesp=" + memberDesp + ", memberAvatar=" + memberAvatar + ", memberHomePage="
        + memberHomePage + "]";
  }

  public InspgDynamicMemberAdd() {
    super();
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

  public Long getMemberId() {
    return memberId;
  }

  public void setMemberId(Long memberId) {
    this.memberId = memberId;
  }

  public String getMemberName() {
    return memberName;
  }

  public void setMemberName(String memberName) {
    this.memberName = memberName;
  }

  public String getMemberDesp() {
    return memberDesp;
  }

  public void setMemberDesp(String memberDesp) {
    this.memberDesp = memberDesp;
  }

  public String getMemberAvatar() {
    return memberAvatar;
  }

  public void setMemberAvatar(String memberAvatar) {
    this.memberAvatar = memberAvatar;
  }

  public String getMemberHomePage() {
    return memberHomePage;
  }

  public void setMemberHomePage(String memberHomePage) {
    this.memberHomePage = memberHomePage;
  }

  public Long getDynId() {
    return dynId;
  }

  public void setDynId(Long dynId) {
    this.dynId = dynId;
  }


}
