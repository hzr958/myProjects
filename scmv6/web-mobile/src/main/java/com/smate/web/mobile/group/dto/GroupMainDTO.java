package com.smate.web.mobile.group.dto;

import java.io.Serializable;

import com.smate.core.base.utils.model.Page;

/**
 * 移动端群组首页DTO
 * 
 * @author wsn
 * @date May 10, 2019
 */
public class GroupMainDTO implements Serializable {

  private Long psnId;// 当前人psnId
  private String des3PsnId;
  private Long grpId;// 群组ID
  private String des3GrpId; // 加密的群组ID
  private Integer details; // 是否是详情页面，1是，0否
  private String des3DynId; // 加密的动态ID
  private Page page = new Page();
  private String dynText; // 发布动态时的文字内容
  private String des3PubId; // 发布动态时选中的要一起发布的成果ID
  private String des3ResId; // 资源加密ID
  private String tempType; // 动态类型
  private String resType; // 资源类型
  private String commentContent; // 评论内容
  private String stickyOpt; // 置顶操作，1：置顶群组， 0：取消置顶

  public GroupMainDTO() {
    super();
  }

  public Long getPsnId() {
    return psnId;
  }

  public void setPsnId(Long psnId) {
    this.psnId = psnId;
  }

  public String getDes3PsnId() {
    return des3PsnId;
  }

  public void setDes3PsnId(String des3PsnId) {
    this.des3PsnId = des3PsnId;
  }

  public Long getGrpId() {
    return grpId;
  }

  public void setGrpId(Long grpId) {
    this.grpId = grpId;
  }

  public String getDes3GrpId() {
    return des3GrpId;
  }

  public void setDes3GrpId(String des3GrpId) {
    this.des3GrpId = des3GrpId;
  }

  public Integer getDetails() {
    return details;
  }

  public void setDetails(Integer details) {
    this.details = details;
  }

  public String getDes3DynId() {
    return des3DynId;
  }

  public void setDes3DynId(String des3DynId) {
    this.des3DynId = des3DynId;
  }

  public Page getPage() {
    return page;
  }

  public void setPage(Page page) {
    this.page = page;
  }

  public String getDynText() {
    return dynText;
  }

  public void setDynText(String dynText) {
    this.dynText = dynText;
  }

  public String getDes3PubId() {
    return des3PubId;
  }

  public void setDes3PubId(String des3PubId) {
    this.des3PubId = des3PubId;
  }

  public String getDes3ResId() {
    return des3ResId;
  }

  public void setDes3ResId(String des3ResId) {
    this.des3ResId = des3ResId;
  }

  public String getTempType() {
    return tempType;
  }

  public void setTempType(String tempType) {
    this.tempType = tempType;
  }

  public String getResType() {
    return resType;
  }

  public void setResType(String resType) {
    this.resType = resType;
  }

  public String getCommentContent() {
    return commentContent;
  }

  public void setCommentContent(String commentContent) {
    this.commentContent = commentContent;
  }

  public String getStickyOpt() {
    return stickyOpt;
  }

  public void setStickyOpt(String stickyOpt) {
    this.stickyOpt = stickyOpt;
  }



}
