package com.smate.web.mobile.group.vo;

import com.smate.core.base.utils.model.Page;

public class GroupOperateVO {
  private Integer disciplineCategory;// 群组科技领域
  private String searchKey;
  private Page page = new Page();
  private Integer searchByRole = 1;// 1=所有我的群组， 2=我管理的群组，3=我是普通成员的群组，4=待批准的群组
  private Integer grpCategory;// 群组分类 10:课程群组 ， 11项目群组,12,兴趣群组
  private Integer rcmdStatus = 0; // 0：推荐 1：申请 9 忽略
  private Long grpId;// 群组ID
  private String des3GrpId;
  private Integer isApplyJoinGrp;// 是否是申请加入群组 1=申请加入群组，0=取消加群组
  private String openType;// 群组权限 O公开，H隐私
  private Integer workFileType; // 1: 作业
  private Integer courseFileType;// 2: 教学课件

  public Integer getGrpCategory() {
    return grpCategory;
  }

  public void setGrpCategory(Integer grpCategory) {
    this.grpCategory = grpCategory;
  }

  public Integer getDisciplineCategory() {
    return disciplineCategory;
  }

  public void setDisciplineCategory(Integer disciplineCategory) {
    this.disciplineCategory = disciplineCategory;
  }

  public String getSearchKey() {
    return searchKey;
  }

  public void setSearchKey(String searchKey) {
    this.searchKey = searchKey;
  }

  public Page getPage() {
    return page;
  }

  public void setPage(Page page) {
    this.page = page;
  }

  public Integer getSearchByRole() {
    return searchByRole;
  }

  public void setSearchByRole(Integer searchByRole) {
    this.searchByRole = searchByRole;
  }

  public Integer getRcmdStatus() {
    return rcmdStatus;
  }

  public void setRcmdStatus(Integer rcmdStatus) {
    this.rcmdStatus = rcmdStatus;
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

  public Integer getIsApplyJoinGrp() {
    return isApplyJoinGrp;
  }

  public void setIsApplyJoinGrp(Integer isApplyJoinGrp) {
    this.isApplyJoinGrp = isApplyJoinGrp;
  }

  public String getOpenType() {
    return openType;
  }

  public void setOpenType(String openType) {
    this.openType = openType;
  }

  public Integer getWorkFileType() {
    return workFileType;
  }

  public void setWorkFileType(Integer workFileType) {
    this.workFileType = workFileType;
  }

  public Integer getCourseFileType() {
    return courseFileType;
  }

  public void setCourseFileType(Integer courseFileType) {
    this.courseFileType = courseFileType;
  }

}
