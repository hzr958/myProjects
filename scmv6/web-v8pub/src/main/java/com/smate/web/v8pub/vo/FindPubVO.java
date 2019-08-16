package com.smate.web.v8pub.vo;

import java.util.List;
import java.util.Map;

import com.smate.core.base.pub.vo.PubInfo;
import com.smate.core.base.utils.model.Page;

public class FindPubVO {
  private Page<PubInfo> page = new Page<>();
  private String des3AreaIds;// 多个加密的科技领域id，用逗号分隔
  private String searchArea;// 多个科技领域，用逗号分隔
  private String searchKey;// 查找字符串
  /**
   * 发表年份 多个逗号隔离 例如： 2015,2014
   */
  private String publishYear = "";
  /**
   * 收录列表，多个逗号隔离 例如：ei,sci
   */
  private String includeType = "";
  /**
   * 排序的字段 citations:引用次数 title：标题 publishDate：发表日期 gmtModified ： 更新时间 下面是群组排序说明： createDate ： 创建时间
   * 默认排序 publishDate ： 最新发表 citations ： 最多引用排序
   */
  private String searchPubType;// 成果类型
  private String searchLanguage;// 查询的语言，默认中文
  private String orderBy = "DEFAULT";

  List<Map<String, Object>> scienceAreas;// 所有的科技领域
  private int currentYear; // 当前年份

  public Page<PubInfo> getPage() {
    return page;
  }

  public void setPage(Page<PubInfo> page) {
    this.page = page;
  }

  public String getDes3AreaIds() {
    return des3AreaIds;
  }

  public void setDes3AreaIds(String des3AreaIds) {
    this.des3AreaIds = des3AreaIds;
  }

  public String getSearchArea() {
    return searchArea;
  }

  public void setSearchArea(String searchArea) {
    this.searchArea = searchArea;
  }

  public String getSearchKey() {
    return searchKey;
  }

  public void setSearchKey(String searchKey) {
    this.searchKey = searchKey;
  }

  public String getPublishYear() {
    return publishYear;
  }

  public void setPublishYear(String publishYear) {
    this.publishYear = publishYear;
  }

  public String getIncludeType() {
    return includeType;
  }

  public void setIncludeType(String includeType) {
    this.includeType = includeType;
  }

  public String getSearchPubType() {
    return searchPubType;
  }

  public void setSearchPubType(String searchPubType) {
    this.searchPubType = searchPubType;
  }

  public String getSearchLanguage() {
    return searchLanguage;
  }

  public void setSearchLanguage(String searchLanguage) {
    this.searchLanguage = searchLanguage;
  }

  public String getOrderBy() {
    return orderBy;
  }

  public void setOrderBy(String orderBy) {
    this.orderBy = orderBy;
  }

  public List<Map<String, Object>> getScienceAreas() {
    return scienceAreas;
  }

  public void setScienceAreas(List<Map<String, Object>> scienceAreas) {
    this.scienceAreas = scienceAreas;
  }

  public int getCurrentYear() {
    return currentYear;
  }

  public void setCurrentYear(int currentYear) {
    this.currentYear = currentYear;
  }



}
