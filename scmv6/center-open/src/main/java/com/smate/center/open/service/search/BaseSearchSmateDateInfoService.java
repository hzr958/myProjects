package com.smate.center.open.service.search;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringEscapeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.smate.core.base.solr.model.QueryFields;
import com.smate.core.base.solr.service.SolrIndexService;
import com.smate.core.base.utils.data.XmlUtil;
import com.smate.core.base.utils.filter.FilterAllSpecialCharacter;
import com.smate.core.base.utils.string.JnlFormateUtils;

public class BaseSearchSmateDateInfoService implements SearchSmateDateInfoService {

  protected Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  public SolrIndexService solrIndexService;

  /**
   * 查询数据前检查查询字段
   * 
   * @param queryFields
   */
  public void checkQueryFileds(QueryFields queryFields) {
    queryFields.setSeqQuery(queryFields.SEQ_1);
    String searchString = StringEscapeUtils.unescapeHtml4(queryFields.getSearchString());
    Boolean isDoi = JnlFormateUtils.isDoi(searchString);
    // 特殊字符处理
    if (isDoi) {
      queryFields.setIsDoi(true);
      queryFields.setSearchString(FilterAllSpecialCharacter.StringFilter(searchString));
    } else {
      queryFields.setIsDoi(false);
      // 只有这五种特殊字符 , exit
      if (FilterAllSpecialCharacter.isExcludedChars(searchString)) {
        queryFields.setSearchString("");
      } else {
        queryFields.setSearchString(FilterAllSpecialCharacter.stringFilterExcludeFiveChar(searchString));
        // 用于判断详情页面优先显示的内容
        if (XmlUtil.containZhChar(searchString)) {
          queryFields.setLanguage(queryFields.LANGUAGE_ZH);
        } else {
          queryFields.setLanguage(queryFields.LANGUAGE_EN);
        }
      }
    }
  }

  @Override
  public List<Map<String, Object>> getSearchData(Integer pageNo, Integer pageSize, QueryFields queryFields) {
    // TODO Auto-generated method stub
    return null;
  }

}
