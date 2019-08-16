package com.smate.web.group.form;

import com.smate.core.base.utils.model.Page;
import com.smate.core.base.utils.number.NumberUtils;
import com.smate.core.base.utils.security.Des3Utils;

public class GroupDataForm {
  private Integer disciplineCategory;// 群组科技领域
  private Integer pageNo;
  private String searchKey;
  private String des3PsnId;
  private Long psnId;
  private Page page = new Page();
  private Integer searchByRole = 1;// 1=所有我的群组， 2=我管理的群组，3=我是普通成员的群组，4=待批准的群组
  private Integer grpCategory;// 群组分类 10:兴趣群组 ， 11项目群组
  private Integer rcmdStatus = 0; // 0：推荐 1：申请 9 忽略
  private Long grpId;// 群组ID
  private String des3GrpId;
  private Integer isApplyJoinGrp;// 是否是申请加入群组 1=申请加入群组，0=取消加群组

  // 查询条件
  private Integer showPrjPub = 0; // 显示项目成果 1 是显示
  private Integer showRefPub = 0; // 显示 文献成果 1显示
  private String publishYear; // 发表年份 如果是多年用逗号隔开
  private String pubType; // 成果类型 如果是多个就用逗号隔开 用常量数字
  private String includeType;// 收录类别
  // 排序
  private String orderBy = "createDate"; // createDate-最新添加,publishYear-最近发表,citedTimes-引用次数

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

  public String getDes3PsnId() {
    return des3PsnId;
  }

  public void setDes3PsnId(String des3PsnId) {
    this.des3PsnId = des3PsnId;
  }

  public Long getPsnId() {
    if (NumberUtils.isNullOrZero(psnId) && des3PsnId != null) {
      psnId = NumberUtils.parseLong(Des3Utils.decodeFromDes3(des3PsnId));
    }
    return psnId;
  }

  public void setPsnId(Long psnId) {
    this.psnId = psnId;
  }

  public Page getPage() {
    return page;
  }

  public void setPage(Page page) {
    this.page = page;
  }

  public Integer getPageNo() {
    return pageNo;
  }

  public void setPageNo(Integer pageNo) {
    this.pageNo = pageNo;
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
    if (NumberUtils.isNullOrZero(grpId) && des3GrpId != null) {
      grpId = NumberUtils.parseLong(Des3Utils.decodeFromDes3(des3GrpId));
    }
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

  public Integer getShowPrjPub() {
    return showPrjPub;
  }

  public void setShowPrjPub(Integer showPrjPub) {
    this.showPrjPub = showPrjPub;
  }

  public Integer getShowRefPub() {
    return showRefPub;
  }

  public void setShowRefPub(Integer showRefPub) {
    this.showRefPub = showRefPub;
  }

  public String getPublishYear() {
    return publishYear;
  }

  public void setPublishYear(String publishYear) {
    this.publishYear = publishYear;
  }

  public String getPubType() {
    return pubType;
  }

  public void setPubType(String pubType) {
    this.pubType = pubType;
  }

  public String getIncludeType() {
    return includeType;
  }

  public void setIncludeType(String includeType) {
    this.includeType = includeType;
  }

  public String getOrderBy() {
    return orderBy;
  }

  public void setOrderBy(String orderBy) {
    this.orderBy = orderBy;
  }


}
