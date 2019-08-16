package com.smate.web.dyn.form.dynamic.group;

import org.apache.commons.lang3.StringUtils;

import com.smate.core.base.utils.security.Des3Utils;
import com.smate.core.base.utils.security.SecurityUtils;

/**
 * 
 * @author tsz
 *
 */
public class GroupDynOptForm {

  private Long dynId;// 动态Id
  private String des3DynId;// 加密动态id
  private Long psnId; // 赞人员id
  private Boolean hasAward = false; // 是否点赞 ，默认没有
  private Boolean isComment = false; // true 评论 ，false 删除评论
  private Long commentId; // 评论id 为了标识删除评论
  private String commentContent; // 评论内容
  private Boolean isShare = false; // true 分享 false 取消分享
  private Long shareId; // 分享id 为了标识删除分享内容
  private String shareContent; // 分享内容
  private Long resId; // 资源id
  private String resType; // 资源类型 pub/file
  private String tempType; // 动态模板 publishDyn 发布动态/add 添加成果和文件 /share 只是添加内容
  private Long commentResId; // 评论资源id
  private String des3PsnId; // 加密的人员ID
  private Integer commentCount = 0; // 评论数



  public Long getPsnId() {
    if (psnId == null || psnId == 0L) {
      this.psnId = SecurityUtils.getCurrentUserId();
    }
    return psnId;
  }

  public void setPsnId(Long psnId) {
    this.psnId = psnId;
  }

  public Long getDynId() {
    if (dynId == null && StringUtils.isNotBlank(des3DynId)) {
      String des3Str = Des3Utils.decodeFromDes3(des3DynId);
      if (des3Str == null) {
        return dynId;
      } else {
        return Long.valueOf(des3Str);
      }
    }
    return dynId;
  }

  public void setDynId(Long dynId) {
    this.dynId = dynId;
  }

  public Boolean getHasAward() {
    return hasAward;
  }

  public void setHasAward(Boolean hasAward) {
    this.hasAward = hasAward;
  }

  public Boolean getIsComment() {
    return isComment;
  }

  public void setIsComment(Boolean isComment) {
    this.isComment = isComment;
  }

  public Long getCommentId() {
    return commentId;
  }

  public void setCommentId(Long commentId) {
    this.commentId = commentId;
  }

  public String getCommentContent() {
    return commentContent;
  }

  public void setCommentContent(String commentContent) {
    this.commentContent = commentContent;
  }

  public Long getShareId() {
    return shareId;
  }

  public void setShareId(Long shareId) {
    this.shareId = shareId;
  }

  public String getShareContent() {
    return shareContent;
  }

  public void setShareContent(String shareContent) {
    this.shareContent = shareContent;
  }

  public Long getResId() {
    return resId;
  }

  public void setResId(Long resId) {
    this.resId = resId;
  }

  public String getResType() {
    return resType;
  }

  public void setResType(String resType) {
    this.resType = resType;
  }

  public String getTempType() {
    return tempType;
  }

  public void setTempType(String tempType) {
    this.tempType = tempType;
  }

  public Boolean getIsShare() {
    return isShare;
  }

  public void setIsShare(Boolean isShare) {
    this.isShare = isShare;
  }

  public String getDes3DynId() {
    return des3DynId;
  }

  public void setDes3DynId(String des3DynId) {
    this.des3DynId = des3DynId;
  }

  public Long getCommentResId() {
    return commentResId;
  }

  public void setCommentResId(Long commentResId) {
    this.commentResId = commentResId;
  }

  public String getDes3PsnId() {
    return des3PsnId;
  }

  public void setDes3PsnId(String des3PsnId) {
    this.des3PsnId = des3PsnId;
  }

  public Integer getCommentCount() {
    return commentCount;
  }

  public void setCommentCount(Integer commentCount) {
    this.commentCount = commentCount;
  }



}
