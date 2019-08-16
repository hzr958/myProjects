package com.smate.web.dyn.model.dynamic.group;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * 群组动态评论记录
 * 
 * @author zzx
 *
 */
@Entity
@Table(name = "V_GROUP_DYNAMIC_COMMENTS")
public class GroupDynamicComments {
  @Id
  @Column(name = "ID")
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_V_GROUP_DYNAMIC_COMMENTS", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
  private Long id;// 主键id
  @Column(name = "DYN_ID")
  private Long dynId;// 被评论的动态id
  @Column(name = "COMMENT_PSN_ID")
  private Long commentPsnId;// 评论的人员id
  @Column(name = "COMMENT_CONTENT")
  private String commentContent;// 评论的内容
  @Column(name = "COMMENT_DATE")
  private Date commentDate;// 评论或者删除评论的时间
  @Column(name = "STATUS")
  private Integer status;// 状态0：评论 1：删除评论
  @Column(name = "COMMENT_RES_ID")
  private Long commentResId;

  public Long getCommentResId() {
    return commentResId;
  }

  public void setCommentResId(Long commentResId) {
    this.commentResId = commentResId;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }


  public Long getDynId() {
    return dynId;
  }

  public void setDynId(Long dynId) {
    this.dynId = dynId;
  }


  public Long getCommentPsnId() {
    return commentPsnId;
  }

  public void setCommentPsnId(Long commentPsnId) {
    this.commentPsnId = commentPsnId;
  }


  public String getCommentContent() {
    return commentContent;
  }

  public void setCommentContent(String commentContent) {
    this.commentContent = commentContent;
  }


  public Date getCommentDate() {
    return commentDate;
  }

  public void setCommentDate(Date commentDate) {
    this.commentDate = commentDate;
  }


  public Integer getStatus() {
    return status;
  }

  public void setStatus(Integer status) {
    this.status = status;
  }

}
