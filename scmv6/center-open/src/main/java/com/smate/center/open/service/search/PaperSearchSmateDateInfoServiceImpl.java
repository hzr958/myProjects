package com.smate.center.open.service.search;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.solr.client.solrj.SolrServerException;

import com.smate.core.base.solr.model.QueryFields;
import com.smate.core.base.solr.service.SolrIndexSerivceImpl;
import com.smate.core.base.utils.json.JacksonUtils;
import com.smate.core.base.utils.string.ServiceUtil;

/**
 * 查询成果论文
 * 
 * @author ajb
 *
 */
public class PaperSearchSmateDateInfoServiceImpl extends BaseSearchSmateDateInfoService {

  @Override
  public List<Map<String, Object>> getSearchData(Integer pageNo, Integer pageSize, QueryFields queryFields) {
    List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();
    Map<String, Object> extraMap = new HashMap<String, Object>();
    dataList.add(extraMap);
    checkQueryFileds(queryFields);
    if (StringUtils.isNotBlank(queryFields.getSearchString())) {
      try {
        Map<String, Object> mapData = solrIndexService.queryPubs(pageNo, pageSize, queryFields);

        String numFound = (String) mapData.get(SolrIndexSerivceImpl.RESULT_NUMFOUND);
        // 设置页数
        if (NumberUtils.isNumber(numFound)) {
          Long tatalCount = NumberUtils.toLong(numFound);
          extraMap.put("count", tatalCount);
          if (tatalCount % pageSize == 0) {
            extraMap.put("count", tatalCount / pageSize);
          } else {
            extraMap.put("totalPages", tatalCount / pageSize + 1);
          }
        }

        String items = (String) mapData.get(SolrIndexSerivceImpl.RESULT_ITEMS);
        String highlight = (String) mapData.get(SolrIndexSerivceImpl.RESULT_HIGHLIGHT);
        boolean ishighlight = JacksonUtils.isJsonString(highlight);
        boolean flag = JacksonUtils.isJsonString(items);
        if (flag && ishighlight) {
          List<Map<String, Object>> itemsList = JacksonUtils.jsonToList(items);
          Map<String, Map<String, List<String>>> highligh = JacksonUtils.jsonToMap(highlight);
          if (itemsList != null && itemsList.size() > 0) {
            for (int i = 0; i < itemsList.size(); i++) {
              Map item = itemsList.get(i);
              Map<String, Object> dataMap = new HashMap<String, Object>();
              setPdwhPublication(item, dataMap, queryFields.getLanguage(), highligh);
              dataList.add(dataMap);
            }
          }
        }

      } catch (SolrServerException e) {
        logger.error("调用solr服务查询成果异常", e.toString());
        extraMap.put("count", 0);
        extraMap.put("totalPages", 0);
      }
    } else {
      extraMap.put("count", 0);
      extraMap.put("totalPages", 0);
    }
    return dataList;
  }

  /**
   * map对象 ， 转化成 PdwhPublication
   * 
   * @param item
   * @param pd
   */
  public void setPdwhPublication(Map<String, Object> item, Map<String, Object> dataMap, String language,
      Map<String, Map<String, List<String>>> highligh) {
    String id = "";
    if (item.get("id") != null) {
      id = item.get("id").toString();
    }
    String zhTitle = "";
    String enTitle = "";
    String authors = "";
    if (StringUtils.isNotBlank(id)) {
      Map<String, List<String>> highlightMap = highligh.get(id);
      if (highlightMap != null && highlightMap.size() > 0) {
        Set<String> keySet = highlightMap.keySet();
        // 论文 的高亮字段 ，zhTitle enTitle authors // doi zhKeywords
        // enKeywords
        for (String string : keySet) {
          StringBuffer sb = new StringBuffer();
          List<String> list = highlightMap.get(string);
          if (list == null)
            continue;
          for (String string2 : list) {
            sb.append(string2);
          }
          switch (string) {
            case "zhTitle":
              zhTitle = sb.toString();
              break;
            case "enTitle":
              enTitle = sb.toString();
              break;
            case "authors":
              authors = sb.toString();
              break;
            default:
              break;
          }
        }
      }

    }

    logger.info("查询的成果id==" + id);

    // 设置成果详情的优先显示语言，与检索语言保持一致
    dataMap.put("language", StringUtils.isEmpty(language) == true ? QueryFields.LANGUAGE_EN : language);

    if (item.get("pubId") != null && NumberUtils.isNumber(item.get("pubId").toString())) {
      dataMap.put("des3PubId", ServiceUtil.encodeToDes3(item.get("pubId").toString()));
      dataMap.put("pubId", NumberUtils.toLong(item.get("pubId").toString()));
    }
    if (item.get("pubDbId") != null && NumberUtils.isNumber(item.get("pubDbId").toString())) {
      dataMap.put("pubDbId", NumberUtils.toInt(item.get("pubDbId").toString()));
    }

    if (StringUtils.isBlank(zhTitle) && item.get("zhTitle") != null)
      zhTitle = item.get("zhTitle").toString();
    if (StringUtils.isBlank(enTitle) && item.get("enTitle") != null)
      enTitle = item.get("enTitle").toString();
    if (StringUtils.isBlank(authors) && item.get("authors") != null)
      authors = item.get("authors").toString();
    Object zhPubBrief = item.get("zhPubBrief");
    Object enPubBrief = item.get("enPubBrief");
    Object pubTypeId = item.get("pubTypeId");
    Object pubYear = item.get("pubYear");

    if (authors != null && StringUtils.isNotBlank(authors.toString())) {
      dataMap.put("authorNames", authors.toString());
    }

    if (language == null || "".equals(language) || "ZH_CN".equals(language) || "ALL".equalsIgnoreCase(language)) {
      if (zhTitle != null && StringUtils.isNotBlank(zhTitle.toString())) {
        dataMap.put("title", zhTitle.toString());
      } else {
        if (enTitle != null)
          dataMap.put("title", enTitle.toString());
      }

      if (zhPubBrief != null && StringUtils.isNoneBlank(zhPubBrief.toString())) {
        dataMap.put("briefDesc", zhPubBrief.toString());
      } else {
        if (enPubBrief != null)
          dataMap.put("briefDesc", enPubBrief.toString());
      }
    } else {

      if (enTitle != null && StringUtils.isNotBlank(enTitle.toString())) {
        dataMap.put("title", enTitle.toString());
      } else {
        if (zhTitle != null)
          dataMap.put("title", zhTitle.toString());
      }

      if (enPubBrief != null && StringUtils.isNoneBlank(enPubBrief.toString())) {
        dataMap.put("briefDesc", enPubBrief.toString());
      } else {
        if (zhPubBrief != null)
          dataMap.put("briefDesc", zhPubBrief.toString());
      }
    }
    if (pubYear != null && StringUtils.isNotBlank(pubYear.toString())) {
      dataMap.put("publishYear", NumberUtils.toLong(pubYear.toString()));
    }
    if (pubTypeId != null && StringUtils.isNotBlank(pubTypeId.toString())) {
      dataMap.put("pubType", NumberUtils.toLong(pubTypeId.toString()));
    }

  }
}
