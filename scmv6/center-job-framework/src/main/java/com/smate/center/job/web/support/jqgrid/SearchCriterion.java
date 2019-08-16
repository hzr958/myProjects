package com.smate.center.job.web.support.jqgrid;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.smate.center.job.web.support.jqgrid.SearchOper.SearchOperDeserializer;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * JqGrid搜索条件页面模型类
 *
 * @author houchuanjie
 * @date 2018年3月1日 上午9:32:31
 */
public class SearchCriterion {

  /**
   * 搜索字段域
   */
  private String field;
  /**
   * 搜索逻辑操作运算
   */
  @JsonDeserialize(using = SearchOperDeserializer.class)
  private SearchOper op;
  /**
   * 搜索内容
   */
  private String data;

  public SearchCriterion() {
  }

  public SearchCriterion(String searchField, String searchString, SearchOper searchOper) {
    this.field = searchField;
    this.op = searchOper;
    this.data = searchString;
  }

  /**
   * @return field
   */
  public String getField() {
    return field;
  }

  /**
   * @param field 要设置的 field
   */
  public void setField(String field) {
    this.field = field;
  }

  public SearchOper getOp() {
    return op;
  }

  public void setOp(SearchOper op) {
    this.op = op;
  }

  /**
   * @return data
   */
  public String getData() {
    return data;
  }

  /**
   * @param data 要设置的 data
   */
  public void setData(String data) {
    this.data = data;
  }


  @Override
  public String toString() {
    return new ToStringBuilder(this)
        .append("field", field)
        .append("op", op)
        .append("data", data)
        .toString();
  }
}
