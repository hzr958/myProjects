package com.smate.web.mobile.psn.vo;


public class PsnOperateVO {
  private String insName;// 机构名
  private String department;// 部门
  private String position;// 职位
  private Integer fromYear;// 开始年份
  private Integer fromMonth;// 开始月份
  private Integer toYear;// 终止年份
  private Integer toMonth;// 终止月份
  private String description;// 描述
  private String type;// 类型 "add"为新增 其他为编辑
  private Integer isActive = 0;// 终止日期是否为至今 1为至今
  private Long workId;// 工作经历id

  private Long eduId;// 教育经历id
  private String study;// 主修
  private String degreeName;// 学位

  private String psnBriefDesc;// 个人简介

  private String titolo;// 头衔
  private Integer regionId;// 所在地区id
  private String firstName;
  private String lastName;
  private String zhFirstName;
  private String zhLastName;
  private String name;
  private String otherName;
  private Integer isHomepageEdit;
  private String searchKey;
  private String nowTime;
  private String reqFriendIds; // 加密的请求的加好友的ID

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

  public String getPosition() {
    return position;
  }

  public void setPosition(String position) {
    this.position = position;
  }

  public Integer getFromYear() {
    return fromYear;
  }

  public void setFromYear(Integer fromYear) {
    this.fromYear = fromYear;
  }

  public Integer getFromMonth() {
    return fromMonth;
  }

  public void setFromMonth(Integer fromMonth) {
    this.fromMonth = fromMonth;
  }

  public Integer getToYear() {
    return toYear;
  }

  public void setToYear(Integer toYear) {
    this.toYear = toYear;
  }

  public Integer getToMonth() {
    return toMonth;
  }

  public void setToMonth(Integer toMonth) {
    this.toMonth = toMonth;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }


  public Integer getIsActive() {
    return isActive;
  }

  public void setIsActive(Integer isActive) {
    this.isActive = isActive;
  }

  public String getStudy() {
    return study;
  }

  public void setStudy(String study) {
    this.study = study;
  }

  public String getDegreeName() {
    return degreeName;
  }

  public void setDegreeName(String degreeName) {
    this.degreeName = degreeName;
  }

  public String getPsnBriefDesc() {
    return psnBriefDesc;
  }

  public void setPsnBriefDesc(String psnBriefDesc) {
    this.psnBriefDesc = psnBriefDesc;
  }

  public String getTitolo() {
    return titolo;
  }

  public void setTitolo(String titolo) {
    this.titolo = titolo;
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

  public String getZhFirstName() {
    return zhFirstName;
  }

  public void setZhFirstName(String zhFirstName) {
    this.zhFirstName = zhFirstName;
  }

  public String getZhLastName() {
    return zhLastName;
  }

  public void setZhLastName(String zhLastName) {
    this.zhLastName = zhLastName;
  }

  public String getOtherName() {
    return otherName;
  }

  public void setOtherName(String otherName) {
    this.otherName = otherName;
  }

  public Long getWorkId() {
    return workId;
  }

  public void setWorkId(Long workId) {
    this.workId = workId;
  }

  public Long getEduId() {
    return eduId;
  }

  public void setEduId(Long eduId) {
    this.eduId = eduId;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public void setRegionId(Integer regionId) {
    this.regionId = regionId;
  }

  public Integer getRegionId() {
    return regionId;
  }

  public Integer getIsHomepageEdit() {
    return isHomepageEdit;
  }

  public void setIsHomepageEdit(Integer isHomepageEdit) {
    this.isHomepageEdit = isHomepageEdit;
  }

  public String getSearchKey() {
    return searchKey;
  }

  public void setSearchKey(String searchKey) {
    this.searchKey = searchKey;
  }

  public String getNowTime() {
    return nowTime;
  }

  public void setNowTime(String nowTime) {
    this.nowTime = nowTime;
  }

  public String getReqFriendIds() {
    return reqFriendIds;
  }

  public void setReqFriendIds(String reqFriendIds) {
    this.reqFriendIds = reqFriendIds;
  }



}
