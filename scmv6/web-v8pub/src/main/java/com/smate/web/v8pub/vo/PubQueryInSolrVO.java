package com.smate.web.v8pub.vo;

import com.smate.core.base.pub.vo.PubInfo;
import com.smate.core.base.utils.model.Page;

public class PubQueryInSolrVO {
  private Page<PubInfo> page;

  public String orderBy = "";
  private String searchString;// 检索字符串
  // 默认赋值false，不然如果为null,赋值给boolean时会报错
  private String searchArea;// 查询的科技领域
  private String searchPsnKey;// 查询的关键字
  private String defultKeyJson;// 默认查询的关键字
  private Integer searchPubYear;// 查询的出版年份
  private String searchPubType;// 查询的成果类型
  private Integer SearchPubTypeId;
  private Integer SearchPatTypeId;
  private Integer searchPatYear;// 查询的出版年份

  private String suggestPsnName;
  private String suggestPsnId;
  private String suggestInsName;
  private String suggestType;
  private String suggestKw;
  private String suggestInsId;


  /**
   * 收录列表，多个逗号隔离 例如：ei,sci
   */
  public String includeType = "";
  /**
   * 发表年份 多个逗号隔离 例如： 2015,2014
   */
  public String publishYear = "";

  public String getIncludeType() {
    return includeType;
  }

  public void setIncludeType(String includeType) {
    this.includeType = includeType;
  }

  public String getPublishYear() {
    return publishYear;
  }

  public void setPublishYear(String publishYear) {
    this.publishYear = publishYear;
  }

  private Boolean isDoi = false;

  public Page<PubInfo> getPage() {
    return page;
  }

  public void setPage(Page<PubInfo> page) {
    this.page = page;
  }

  public String getSearchPsnKey() {
    return searchPsnKey;
  }

  public void setSearchPsnKey(String searchPsnKey) {
    this.searchPsnKey = searchPsnKey;
  }

  public String getOrderBy() {
    return orderBy;
  }

  public void setOrderBy(String orderBy) {
    this.orderBy = orderBy;
  }

  public String getSearchString() {
    return searchString;
  }

  public void setSearchString(String searchString) {
    this.searchString = searchString;
  }

  public Boolean getIsDoi() {
    return isDoi;
  }

  public void setIsDoi(Boolean isDoi) {
    this.isDoi = isDoi;
  }

  public String getSearchArea() {
    return searchArea;
  }

  public void setSearchArea(String searchArea) {
    this.searchArea = searchArea;
  }

  public Integer getSearchPubYear() {
    return searchPubYear;
  }

  public void setSearchPubYear(Integer searchPubYear) {
    this.searchPubYear = searchPubYear;
  }

  public String getSearchPubType() {
    return searchPubType;
  }

  public void setSearchPubType(String searchPubType) {
    this.searchPubType = searchPubType;
  }

  public Integer getSearchPatTypeId() {
    return SearchPatTypeId;
  }

  public void setSearchPatTypeId(Integer searchPatTypeId) {
    SearchPatTypeId = searchPatTypeId;
  }

  public Integer getSearchPubTypeId() {
    return SearchPubTypeId;
  }

  public void setSearchPubTypeId(Integer searchPubTypeId) {
    SearchPubTypeId = searchPubTypeId;
  }

  public Integer getSearchPatYear() {
    return searchPatYear;
  }

  public void setSearchPatYear(Integer searchPatYear) {
    this.searchPatYear = searchPatYear;
  }

  public String getDefultKeyJson() {
    return defultKeyJson;
  }

  public void setDefultKeyJson(String defultKeyJson) {
    this.defultKeyJson = defultKeyJson;
  }

  public String getSuggestPsnName() {
    return suggestPsnName;
  }

  public void setSuggestPsnName(String suggestPsnName) {
    this.suggestPsnName = suggestPsnName;
  }

  public String getSuggestPsnId() {
    return suggestPsnId;
  }

  public void setSuggestPsnId(String suggestPsnId) {
    this.suggestPsnId = suggestPsnId;
  }

  public String getSuggestInsName() {
    return suggestInsName;
  }

  public void setSuggestInsName(String suggestInsName) {
    this.suggestInsName = suggestInsName;
  }

  public String getSuggestType() {
    return suggestType;
  }

  public void setSuggestType(String suggestType) {
    this.suggestType = suggestType;
  }

  public String getSuggestKw() {
    return suggestKw;
  }

  public void setSuggestKw(String suggestKw) {
    this.suggestKw = suggestKw;
  }

  public String getSuggestInsId() {
    return suggestInsId;
  }

  public void setSuggestInsId(String suggestInsId) {
    this.suggestInsId = suggestInsId;
  }


}
