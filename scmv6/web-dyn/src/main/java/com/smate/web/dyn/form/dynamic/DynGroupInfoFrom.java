package com.smate.web.dyn.form.dynamic;

import java.util.List;

import com.smate.web.dyn.model.grp.GrpStatistics;


public class DynGroupInfoFrom {

  private Long grpId; // 群组id
  private String des3GrpId; // 群组id
  private Long grpNo; // 群主编码
  private String grpName; // 群组名字
  private String grpDescription;// 群组简介
  private Integer grpCategory; // 群组分类 10:兴趣群组 ， 11项目群组
  private String grpAuatars;// 群组头像地址
  private String status;// 群组状态 【01 = 正常 ， 99 = 删除】
  private Integer role;// 当前人与群组的权限关系 ， 1群组拥有者 ， 2群组管理成员 ， 3群组普通成员， 4，待批准
  private GrpStatistics grpStatistic;
  private Long grpProjectPubSum; // 项目群组成果数量
  private Long grpProjectRefSum; // 项目群组文献数量
  private Long grpCourseFileSum; // 课程群组课件数量
  private Long grpWorkFileSum;// 课程群组作业数量
  private List<String> grpKeywordList; // 群组关键词
  private String firstDisciplinetName;
  private String secondDisciplinetName;
  private String keywords;

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

  public Long getGrpNo() {
    return grpNo;
  }

  public void setGrpNo(Long grpNo) {
    this.grpNo = grpNo;
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

  public Integer getGrpCategory() {
    return grpCategory;
  }

  public void setGrpCategory(Integer grpCategory) {
    this.grpCategory = grpCategory;
  }

  public String getGrpAuatars() {
    return grpAuatars;
  }

  public void setGrpAuatars(String grpAuatars) {
    this.grpAuatars = grpAuatars;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public Integer getRole() {
    return role;
  }

  public void setRole(Integer role) {
    this.role = role;
  }

  public Long getGrpCourseFileSum() {
    return grpCourseFileSum;
  }

  public void setGrpCourseFileSum(Long grpCourseFileSum) {
    this.grpCourseFileSum = grpCourseFileSum;
  }

  public Long getGrpWorkFileSum() {
    return grpWorkFileSum;
  }

  public void setGrpWorkFileSum(Long grpWorkFileSum) {
    this.grpWorkFileSum = grpWorkFileSum;
  }

  public List<String> getGrpKeywordList() {
    return grpKeywordList;
  }

  public void setGrpKeywordList(List<String> grpKeywordList) {
    this.grpKeywordList = grpKeywordList;
  }

  public String getFirstDisciplinetName() {
    return firstDisciplinetName;
  }

  public void setFirstDisciplinetName(String firstDisciplinetName) {
    this.firstDisciplinetName = firstDisciplinetName;
  }

  public String getSecondDisciplinetName() {
    return secondDisciplinetName;
  }

  public void setSecondDisciplinetName(String secondDisciplinetName) {
    this.secondDisciplinetName = secondDisciplinetName;
  }

  public String getKeywords() {
    return keywords;
  }

  public void setKeywords(String keywords) {
    this.keywords = keywords;
  }

  public GrpStatistics getGrpStatistic() {
    return grpStatistic;
  }

  public void setGrpStatistic(GrpStatistics grpStatistic) {
    this.grpStatistic = grpStatistic;
  }

  public Long getGrpProjectPubSum() {
    return grpProjectPubSum;
  }

  public void setGrpProjectPubSum(Long grpProjectPubSum) {
    this.grpProjectPubSum = grpProjectPubSum;
  }

  public Long getGrpProjectRefSum() {
    return grpProjectRefSum;
  }

  public void setGrpProjectRefSum(Long grpProjectRefSum) {
    this.grpProjectRefSum = grpProjectRefSum;
  }


}
