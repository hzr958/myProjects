package com.smate.center.open.service.search;

import java.util.List;
import java.util.Map;

import com.smate.core.base.solr.model.QueryFields;

/**
 * 查询科研之友信息的接口
 * 
 * @author ajb
 *
 */
public interface SearchSmateDateInfoService {

  /**
   * 得到查询的数据
   * 
   * @return
   */
  public List<Map<String, Object>> getSearchData(Integer pageNo, Integer pageSize, QueryFields queryFields);
}
