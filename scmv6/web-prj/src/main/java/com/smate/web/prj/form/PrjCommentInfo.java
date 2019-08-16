package com.smate.web.prj.form;

/**
 * 构造项目评论列表的模型
 * 
 * @author YJ 创建于2018年3月21日
 */
public class PrjCommentInfo {

  private Long prjId; // 项目id
  private Long psnId; // 评论人员id
  private String des3ReplyerId;
  private String commentsContent; // 评论内容
  private String rebuildTime; // 迄今间距的时间

  private String name;// 评论人显示姓名
  private String avatars;// 评论人显示头像
  private String position;// 评论人职称
  private String insName;// 评论人机构信息
  private String department;// 评论人部门信息
  private String psnIndexUrl;// 人员短地址

  public Long getPrjId() {
    return prjId;
  }

  public void setPrjId(Long prjId) {
    this.prjId = prjId;
  }

  public Long getPsnId() {
    return psnId;
  }

  public void setPsnId(Long psnId) {
    this.psnId = psnId;
  }

  public String getCommentsContent() {
    return commentsContent;
  }

  public void setCommentsContent(String commentsContent) {
    this.commentsContent = commentsContent;
  }

  public String getRebuildTime() {
    return rebuildTime;
  }

  public void setRebuildTime(String rebuildTime) {
    this.rebuildTime = rebuildTime;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getAvatars() {
    return avatars;
  }

  public void setAvatars(String avatars) {
    this.avatars = avatars;
  }

  public String getPosition() {
    return position;
  }

  public void setPosition(String position) {
    this.position = position;
  }

  public String getInsName() {
    return insName;
  }

  public void setInsName(String insName) {
    this.insName = insName;
  }

  public String getDepartment() {
    return department;
  }

  public void setDepartment(String department) {
    this.department = department;
  }

  public String getPsnIndexUrl() {
    return psnIndexUrl;
  }

  public void setPsnIndexUrl(String psnIndexUrl) {
    this.psnIndexUrl = psnIndexUrl;
  }

  public String getDes3ReplyerId() {
    return des3ReplyerId;
  }

  public void setDes3ReplyerId(String des3ReplyerId) {
    this.des3ReplyerId = des3ReplyerId;
  }

}
