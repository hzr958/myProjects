package com.smate.center.batch.model.solr;

import java.io.Serializable;

import org.apache.commons.lang.StringUtils;

public class QueryFields implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = -7762095870982999769L;

  // 语言设置
  public static final String LANGUAGE_ALL = "ALL";
  public static final String LANGUAGE_ZH = "ZH_CN";
  public static final String LANGUAGE_EN = "EN";

  // 按字段排序
  public static final String ORDER_YEAR = "YEAR";
  public static final String ORDER_DEFAULT = "DEFAULT";

  // 用于异步加载，查询第一次，第二次
  public static final Integer SEQ_1 = 1;
  public static final Integer SEQ_2 = 2;

  // 检索框内容
  private String searchString;
  private Integer pubYear;
  private Integer pubTypeId;
  private String language;
  private int searchType;// 1-论文；2-专利；
  private String orderBy;
  // 查询顺序，用于异步加载
  private Integer seqQuery;

  public String getSearchString() {
    return searchString;
  }

  public void setSearchString(String searchString) {
    this.searchString = searchString;
  }

  public Integer getPubYear() {
    return pubYear;
  }

  public void setPubYear(Integer pubYear) {
    this.pubYear = pubYear;
  }

  public Integer getPubTypeId() {
    return pubTypeId;
  }

  public void setPubTypeId(Integer pubTypeId) {
    this.pubTypeId = pubTypeId;
  }

  public String getLanguage() {
    return language;
  }

  public void setLanguage(String language) {
    if (StringUtils.isEmpty(language)) {
      this.language = LANGUAGE_ALL;
    } else {
      this.language = language;
    }
  }

  public int getSearchType() {
    return searchType;
  }

  public void setSearchType(int searchType) {
    this.searchType = searchType;
  }

  public String getOrderBy() {
    return orderBy;
  }

  public void setOrderBy(String orderBy) {
    if (StringUtils.isEmpty(orderBy)) {
      this.orderBy = ORDER_DEFAULT;
    } else {
      this.orderBy = orderBy;
    }
  }

  public Integer getSeqQuery() {
    return seqQuery;
  }

  public void setSeqQuery(Integer seqQuery) {
    if (seqQuery == null) {
      this.seqQuery = SEQ_1;
    } else {
      this.seqQuery = seqQuery;
    }
  }

}
