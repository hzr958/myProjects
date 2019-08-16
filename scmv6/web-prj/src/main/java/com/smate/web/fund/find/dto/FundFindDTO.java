package com.smate.web.fund.find.dto;

import java.io.Serializable;

/**
 * 基金发现DTO
 * 
 */

public class FundFindDTO implements Serializable {
  private Integer pageNum; // 页数
  private Integer pageSize; // 每页显示记录数
  private Integer totalCount; // 查询到的结果总数
  private Integer totalPages;
  private String des3FundAgencyIds;

  public Integer getPageNum() {
    return pageNum;
  }

  public void setPageNum(Integer pageNum) {
    this.pageNum = pageNum;
  }

  public Integer getPageSize() {
    return pageSize;
  }

  public void setPageSize(Integer pageSize) {
    this.pageSize = pageSize;
  }

  public Integer getTotalCount() {
    return totalCount;
  }

  public void setTotalCount(Integer totalCount) {
    this.totalCount = totalCount;
  }

  public String getDes3FundAgencyIds() {
    return des3FundAgencyIds;
  }

  public void setDes3FundAgencyIds(String des3FundAgencyIds) {
    this.des3FundAgencyIds = des3FundAgencyIds;
  }

  public Integer getTotalPages() {
    return totalPages;
  }

  public void setTotalPages(Integer totalPages) {
    this.totalPages = totalPages;
  }



}
