package com.smate.center.open.form.grp;


import java.util.Date;


/**
 * 群组信息主表
 * 
 * @author AiJiangBin
 *
 */
public class GrpMainForm {

  private Long grpId;// 群组ID
  private String grpName;// 群组名字
  private String grpDescription;// 群组简介
  private String projectNo;// 项目批准号、编号
  private Integer projectStatus;// 项目批状态
  private Integer grpCategory;// 群组分类 10:兴趣群组 ， 11项目群组
  private String openType; // 群组开放类型 公开类型【O = 开放 ， H = 半开放 ， P=隐私】 默认 H
  private Long psnId;// 当前人psnId
  private Date date;
  private Integer firstCategoryId; // 学科 一级领域 ，常量
  private Integer secondCategoryId; // 学科 二级领域 ，，常量
  private String keywords; // 关键词 ，分号分隔
  private String nsfcCategoryId; // 学科代码


  public String getNsfcCategoryId() {
    return nsfcCategoryId;
  }

  public void setNsfcCategoryId(String nsfcCategoryId) {
    this.nsfcCategoryId = nsfcCategoryId;
  }

  public Long getGrpId() {
    return grpId;
  }

  public void setGrpId(Long grpId) {
    this.grpId = grpId;
  }

  public String getGrpName() {
    return grpName;
  }

  public void setGrpName(String grpName) {
    this.grpName = grpName;
  }

  public String getGrpDescription() {
    return grpDescription;
  }

  public void setGrpDescription(String grpDescription) {
    this.grpDescription = grpDescription;
  }

  public String getProjectNo() {
    return projectNo;
  }

  public void setProjectNo(String projectNo) {
    this.projectNo = projectNo;
  }

  public Integer getGrpCategory() {
    return grpCategory;
  }

  public void setGrpCategory(Integer grpCategory) {
    this.grpCategory = grpCategory;
  }

  public String getOpenType() {
    return openType;
  }

  public void setOpenType(String openType) {
    this.openType = openType;
  }

  public Long getPsnId() {
    return psnId;
  }

  public void setPsnId(Long psnId) {
    this.psnId = psnId;
  }

  public Date getDate() {
    return date;
  }

  public void setDate(Date date) {
    this.date = date;
  }

  public Integer getFirstCategoryId() {
    return firstCategoryId;
  }

  public void setFirstCategoryId(Integer firstCategoryId) {
    this.firstCategoryId = firstCategoryId;
  }

  public Integer getSecondCategoryId() {
    return secondCategoryId;
  }

  public void setSecondCategoryId(Integer secondCategoryId) {
    this.secondCategoryId = secondCategoryId;
  }

  public String getKeywords() {
    return keywords;
  }

  public void setKeywords(String keywords) {
    this.keywords = keywords;
  }

  public Integer getProjectStatus() {
    return projectStatus;
  }

  public void setProjectStatus(Integer projectStatus) {
    this.projectStatus = projectStatus;
  }

}
