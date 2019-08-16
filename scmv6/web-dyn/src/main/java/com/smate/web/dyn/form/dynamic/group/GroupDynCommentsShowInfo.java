package com.smate.web.dyn.form.dynamic.group;

import java.util.Date;

import org.apache.commons.lang3.StringUtils;

import com.smate.core.base.utils.number.NumberUtils;
import com.smate.core.base.utils.security.Des3Utils;

/**
 * 群组动态评论显示类-只用于页面展示数据
 * 
 * @author zzx
 *
 */
public class GroupDynCommentsShowInfo {
  // GroupDynamicComments
  private Long id;// 主键id
  private Long dynId;// 被评论的动态id
  private Long commentPsnId;// 评论的人员id
  private String commentContent;// 评论的内容
  private Date commentDate;// 评论或者删除评论的时间
  private Integer status;// 状态0：评论 1：删除评论
  // person
  private String firstName;// 用户名
  private String lastName;// 用户姓氏
  private String name;// 用户中文明
  private String ename;// 用户英文名
  private String avatars;// 头像地址
  private Long personId;// 用户编码
  // 自定义属性
  private String commentDateForShow;
  private String des3PersonId;// 用户编码
  private Long commentResId;
  private String des3CommentResId;
  private String commentResZhTitle;
  private String commentResEnTitle;

  public String getCommentResZhTitle() {
    return commentResZhTitle;
  }

  public void setCommentResZhTitle(String commentResZhTitle) {
    this.commentResZhTitle = commentResZhTitle;
  }

  public String getCommentResEnTitle() {
    return commentResEnTitle;
  }

  public void setCommentResEnTitle(String commentResEnTitle) {
    this.commentResEnTitle = commentResEnTitle;
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

  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getEname() {
    return ename;
  }

  public void setEname(String ename) {
    this.ename = ename;
  }

  public String getAvatars() {
    return avatars;
  }

  public void setAvatars(String avatars) {
    this.avatars = avatars;
  }

  public String getCommentDateForShow() {
    return commentDateForShow;
  }

  public void setCommentDateForShow(String commentDateForShow) {
    this.commentDateForShow = commentDateForShow;
  }

  public Long getPersonId() {
    return personId;
  }

  public void setPersonId(Long personId) {
    this.personId = personId;
  }

  public String getDes3PersonId() {
    return des3PersonId;
  }

  public void setDes3PersonId(String des3PersonId) {
    this.des3PersonId = des3PersonId;
  }

  public Long getCommentResId() {
    return commentResId;
  }

  public void setCommentResId(Long commentResId) {
    this.commentResId = commentResId;
  }

  public String getDes3CommentResId() {
    if (StringUtils.isBlank(des3CommentResId) && NumberUtils.isNotNullOrZero(this.commentResId)) {
      des3CommentResId = Des3Utils.encodeToDes3(this.commentResId.toString());
    }
    return des3CommentResId;
  }

  public void setDes3CommentResId(String des3CommentResId) {
    this.des3CommentResId = des3CommentResId;
  }

}
