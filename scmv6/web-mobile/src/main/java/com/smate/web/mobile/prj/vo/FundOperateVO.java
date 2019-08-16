package com.smate.web.mobile.prj.vo;

import com.smate.core.base.utils.model.Page;

public class FundOperateVO {
  private String searchKey;
  private String regionAgency;
  private Integer pageNo = 1;
  private String des3FundAgencyId;
  private Page page;
  private String flag = "list";// 条件筛选标志位，用于检索框的数据回显
  private Integer optType;
  private String searchseniority;
  private String scienceCodesSelect;
  private String pageNum;
  private String searchAreaCodes;
  private String searchRegionCodes;
  private Integer totalCount;
  private Integer totalPages;
  private String des3FundAgencyIds;

  public String getSearchKey() {
    return searchKey;
  }

  public void setSearchKey(String searchKey) {
    this.searchKey = searchKey;
  }

  public String getRegionAgency() {
    return regionAgency;
  }

  public void setRegionAgency(String regionAgency) {
    this.regionAgency = regionAgency;
  }

  public Integer getPageNo() {
    return pageNo;
  }

  public void setPageNo(Integer pageNo) {
    this.pageNo = pageNo;
  }

  public String getDes3FundAgencyId() {
    return des3FundAgencyId;
  }

  public void setDes3FundAgencyId(String des3FundAgencyId) {
    this.des3FundAgencyId = des3FundAgencyId;
  }

  public Page getPage() {
    return page;
  }

  public void setPage(Page page) {
    this.page = page;
  }

  public String getFlag() {
    return flag;
  }

  public void setFlag(String flag) {
    this.flag = flag;
  }

  public Integer getOptType() {
    return optType;
  }

  public void setOptType(Integer optType) {
    this.optType = optType;
  }


  public String getSearchseniority() {
    return searchseniority;
  }

  public void setSearchseniority(String searchseniority) {
    this.searchseniority = searchseniority;
  }

  public String getScienceCodesSelect() {
    return scienceCodesSelect;
  }

  public void setScienceCodesSelect(String scienceCodesSelect) {
    this.scienceCodesSelect = scienceCodesSelect;
  }

  public String getPageNum() {
    return pageNum;
  }

  public void setPageNum(String pageNum) {
    this.pageNum = pageNum;
  }

  public String getSearchAreaCodes() {
    return searchAreaCodes;
  }

  public void setSearchAreaCodes(String searchAreaCodes) {
    this.searchAreaCodes = searchAreaCodes;
  }

  public String getSearchRegionCodes() {
    return searchRegionCodes;
  }

  public void setSearchRegionCodes(String searchRegionCodes) {
    this.searchRegionCodes = searchRegionCodes;
  }

  public Integer getTotalCount() {
    return totalCount;
  }

  public void setTotalCount(Integer totalCount) {
    this.totalCount = totalCount;
  }

  public Integer getTotalPages() {
    return totalPages;
  }

  public void setTotalPages(Integer totalPages) {
    this.totalPages = totalPages;
  }

  public String getDes3FundAgencyIds() {
    return des3FundAgencyIds;
  }

  public void setDes3FundAgencyIds(String des3FundAgencyIds) {
    this.des3FundAgencyIds = des3FundAgencyIds;
  }


}
