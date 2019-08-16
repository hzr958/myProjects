package com.smate.web.psn.model.autocomplete;

import java.io.Serializable;

/**
 * 自动提示form对象，供action使用.
 * 
 * @author liqinghua
 * 
 */
public class AutoCompleteForm implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = 1828927241501827787L;
  private String query;
  private String type;

  public String getQuery() {
    return query;
  }

  public void setQuery(String query) {
    this.query = query;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }
}
