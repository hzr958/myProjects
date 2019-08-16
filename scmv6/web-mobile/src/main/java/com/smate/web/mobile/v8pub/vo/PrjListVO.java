package com.smate.web.mobile.v8pub.vo;

import java.util.List;
import java.util.Map;

import com.smate.core.base.project.vo.ProjectInfo;
import com.smate.core.base.utils.model.Page;

public class PrjListVO {
  private List<ProjectInfo> resultList;
  private Integer totalCount = 0;
  private String addToRepresentPrjIds;// 添加代表项目的加密id，多个用逗号分隔
  private Page<ProjectInfo> page = new Page<>();
  private String searchKey;
  private String orderBy;// 排序
  private String fundingYear;// 项目年度
  private String agencyNames;// 资助机构多个用逗号分隔
  private String orderRule;// 排序规则
  private List<Map<String, Object>> agencyNameList;// 资助机构名称统计信息
  private Integer currentYear;// 当前年份
  private String searchDes3PsnId;// 查找人员的psnid

  public List<ProjectInfo> getResultList() {
    return resultList;
  }

  public void setResultList(List<ProjectInfo> resultList) {
    this.resultList = resultList;
  }

  public Integer getTotalCount() {
    return totalCount;
  }

  public void setTotalCount(Integer totalCount) {
    this.totalCount = totalCount;
  }

  public String getAddToRepresentPrjIds() {
    return addToRepresentPrjIds;
  }

  public void setAddToRepresentPrjIds(String addToRepresentPrjIds) {
    this.addToRepresentPrjIds = addToRepresentPrjIds;
  }

  public Page<ProjectInfo> getPage() {
    return page;
  }

  public void setPage(Page<ProjectInfo> page) {
    this.page = page;
  }

  public String getSearchKey() {
    return searchKey;
  }

  public void setSearchKey(String searchKey) {
    this.searchKey = searchKey;
  }

  public String getOrderBy() {
    return orderBy;
  }

  public void setOrderBy(String orderBy) {
    this.orderBy = orderBy;
  }

  public String getFundingYear() {
    return fundingYear;
  }

  public void setFundingYear(String fundingYear) {
    this.fundingYear = fundingYear;
  }

  public String getAgencyNames() {
    return agencyNames;
  }

  public void setAgencyNames(String agencyNames) {
    this.agencyNames = agencyNames;
  }

  public String getOrderRule() {
    return orderRule;
  }

  public void setOrderRule(String orderRule) {
    this.orderRule = orderRule;
  }

  public List<Map<String, Object>> getAgencyNameList() {
    return agencyNameList;
  }

  public void setAgencyNameList(List<Map<String, Object>> agencyNameList) {
    this.agencyNameList = agencyNameList;
  }

  public Integer getCurrentYear() {
    return currentYear;
  }

  public void setCurrentYear(Integer currentYear) {
    this.currentYear = currentYear;
  }

  public String getSearchDes3PsnId() {
    return searchDes3PsnId;
  }

  public void setSearchDes3PsnId(String searchDes3PsnId) {
    this.searchDes3PsnId = searchDes3PsnId;
  }

}
