package com.smate.center.batch.model.dynamic;

import java.io.Serializable;
import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * 发表新鲜事-文件
 * 
 * @author hzr
 * 
 * @since 6.0.1
 * @version 6.0.1
 * 
 */
@Document(collection = "InspgDynamic") // 多个inspgDynamic类型共用一个集合
public class InspgDynamicFileShare implements Serializable {


  /**
   * 
   */
  private static final long serialVersionUID = 5190837228401009506L;
  @Id
  private String uid; // 主键,mongodb内置ObjectId,无需赋值；
  @Indexed
  private Long dynId; // 动态id 与 InspgDynamicPsn\n或InspgDynamicInspg中的dynId对应
  private Integer hasComment; // 是否有评论模块，0无1有
  private Long commentParentId; // Long评论所属id, 对应fileId
  private Integer commentType; // 评论类型
  private Integer hasLike; // 是否有赞模块,0无1有
  private Long likeParentId; // 赞所属id, 对应fileId
  private Integer likeType; // 赞类型
  private Long inspgId; // 机构Id
  private Long createTime; // 创建日期
  private String fileExt; // 文件类型
  private String fileLink; // 文件链接
  private String fileTitle; // 文件名称
  private String fileImgPath; // 图片路径
  private String fileImgUrl; // 图片下载地址
  private Long fileId; // 文件Id

  @Override
  public String toString() {
    return "inspgDynamicFileShare [uid=" + uid + ", hasComment=" + hasComment + ", commentParentId=" + commentParentId
        + ", commentType=" + commentType + ", hasLike=" + hasLike + ", likeParentId=" + likeParentId + ", likeType="
        + likeType + ", inspgId=" + inspgId + ", createTime=" + createTime + ", fileExt=" + fileExt + ", fileLink="
        + fileLink + ", fileTitle=" + fileTitle + ", fileImgPath=" + fileImgPath + ", fileImgUrl=" + fileImgUrl
        + ", fileId=" + fileId + "]";
  }

  public InspgDynamicFileShare() {
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

  public String getFileExt() {
    return fileExt;
  }

  public void setFileExt(String fileExt) {
    this.fileExt = fileExt;
  }

  public String getFileLink() {
    return fileLink;
  }

  public void setFileLink(String fileLink) {
    this.fileLink = fileLink;
  }

  public String getFileTitle() {
    return fileTitle;
  }

  public void setFileTitle(String fileTitle) {
    this.fileTitle = fileTitle;
  }

  public String getFileImgPath() {
    return fileImgPath;
  }

  public void setFileImgPath(String fileImgPath) {
    this.fileImgPath = fileImgPath;
  }

  public String getFileImgUrl() {
    return fileImgUrl;
  }

  public void setFileImgUrl(String fileImgUrl) {
    this.fileImgUrl = fileImgUrl;
  }

  public Long getFileId() {
    return fileId;
  }

  public void setFileId(Long fileId) {
    this.fileId = fileId;
  }

  public Long getDynId() {
    return dynId;
  }

  public void setDynId(Long dynId) {
    this.dynId = dynId;
  }


}
