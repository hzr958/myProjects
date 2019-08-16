package com.smate.web.v8pub.vo;

import com.smate.core.base.pub.vo.PubInfo;
import com.smate.core.base.utils.model.Page;

public class CollectPubVO {
  public String orderBy = "";
  /**
   * 收录列表，多个逗号隔离 例如：ei,sci
   */
  public String includeType = "";
  /**
   * 成果类型 多个类型逗号隔离 例如： 4,3,5
   */
  public String pubType = "";
  /**
   * 发表年份 多个逗号隔离 例如： 2015,2014
   */
  public Page<PubInfo> page = new Page<>();
  public String publishYear = "";
  public String searchKey; // 检索字符

  public String getOrderBy() {
    return orderBy;
  }

  public void setOrderBy(String orderBy) {
    this.orderBy = orderBy;
  }

  public String getIncludeType() {
    return includeType;
  }

  public void setIncludeType(String includeType) {
    this.includeType = includeType;
  }

  public String getPubType() {
    return pubType;
  }

  public void setPubType(String pubType) {
    this.pubType = pubType;
  }

  public String getPublishYear() {
    return publishYear;
  }

  public void setPublishYear(String publishYear) {
    this.publishYear = publishYear;
  }

  public Page<PubInfo> getPage() {
    return page;
  }

  public void setPage(Page<PubInfo> page) {
    this.page = page;
  }

  public String getSearchKey() {
    return searchKey;
  }

  public void setSearchKey(String searchKey) {
    this.searchKey = searchKey;
  }



}
