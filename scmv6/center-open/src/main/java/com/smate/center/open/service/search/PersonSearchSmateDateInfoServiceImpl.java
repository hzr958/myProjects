package com.smate.center.open.service.search;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.solr.client.solrj.SolrServerException;
import org.springframework.context.i18n.LocaleContextHolder;

import com.smate.core.base.psn.dao.PsnStatisticsDao;
import com.smate.core.base.psn.model.PsnStatistics;
import com.smate.core.base.solr.model.QueryFields;
import com.smate.core.base.solr.service.SolrIndexSerivceImpl;
import com.smate.core.base.utils.json.JacksonUtils;
import com.smate.core.base.utils.string.ServiceUtil;

/**
 * 查询人员列表信息
 * 
 * @author ajb
 *
 */
public class PersonSearchSmateDateInfoServiceImpl extends BaseSearchSmateDateInfoService {

  @Resource
  private PsnStatisticsDao psnStatisticsDao;

  @Override
  public List<Map<String, Object>> getSearchData(Integer pageNo, Integer pageSize, QueryFields queryFields) {
    List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();
    Map<String, Object> extraMap = new HashMap<String, Object>();
    dataList.add(extraMap);
    checkQueryFileds(queryFields);
    if (StringUtils.isNotBlank(queryFields.getSearchString())) {
      try {
        Map<String, Object> rsMap = solrIndexService.queryPersonsDemo(pageNo, pageSize, queryFields);
        String count = (String) rsMap.get(SolrIndexSerivceImpl.RESULT_NUMFOUND);
        // 设置页数
        if (NumberUtils.isNumber(count)) {
          Long tatalCount = NumberUtils.toLong(count);
          extraMap.put("count", tatalCount);
          if (tatalCount % pageSize == 0) {
            extraMap.put("count", tatalCount / pageSize);
          } else {
            extraMap.put("totalPages", tatalCount / pageSize + 1);
          }
        }

        String items = (String) rsMap.get(SolrIndexSerivceImpl.RESULT_ITEMS);
        String highlight = (String) rsMap.get(SolrIndexSerivceImpl.RESULT_HIGHLIGHT);
        List<Map<String, Object>> listItems = JacksonUtils.jsonToList(items);
        Map<String, Map<String, List<String>>> highligh = JacksonUtils.jsonToMap(highlight);
        if (listItems != null && listItems.size() > 0) {
          for (int i = 0; i < listItems.size(); i++) {
            Map item = listItems.get(i);
            Map<String, Object> dataMap = new HashMap<String, Object>();
            setperson(item, dataMap, highligh);
            dataList.add(dataMap);
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

  @SuppressWarnings("rawtypes")
  private void setperson(Map item, Map<String, Object> dataMap, Map<String, Map<String, List<String>>> highligh) {
    // 高亮psnName enPsnName title zhInsName enInsName zhUnit enUnit
    String id = ObjectUtils.toString(item.get("id"));
    String title = "";
    String psnName = "";
    String enPsnName = "";
    String zhInsName = "";
    String enInsName = "";
    if (StringUtils.isNotBlank(id)) {
      Map<String, List<String>> highlightMap = highligh.get(id);
      if (highlightMap != null && highlightMap.size() > 0) {
        Set<String> keySet = highlightMap.keySet();
        for (String string : keySet) {
          StringBuffer sb = new StringBuffer();
          List<String> list = highlightMap.get(string);
          if (list == null)
            continue;
          for (String string2 : list) {
            sb.append(string2);
          }
          switch (string) {
            case "psnName":
              psnName = sb.toString();
              break;
            case "enPsnName":
              enPsnName = sb.toString();
              break;
            case "title":
              title = sb.toString();
              break;
            case "zhInsName":
              zhInsName = sb.toString();
              break;
            case "enInsName":
              enInsName = sb.toString();
              break;
            default:
              break;
          }
        }
      }

    }
    if (StringUtils.isBlank(psnName)) {
      psnName = ObjectUtils.toString(item.get("psnName"));
    }
    if (StringUtils.isBlank(enPsnName)) {
      enPsnName = ObjectUtils.toString(item.get("enPsnName"));
    }
    if (StringUtils.isBlank(title)) {
      title = ObjectUtils.toString(item.get("title"));
    }
    if (StringUtils.isBlank(zhInsName)) {
      zhInsName = ObjectUtils.toString(item.get("zhInsName"));
    }
    if (StringUtils.isBlank(enInsName)) {
      enInsName = ObjectUtils.toString(item.get("enInsName"));
    }
    String language = LocaleContextHolder.getLocale().toString();
    Long psnId = NumberUtils.toLong(ObjectUtils.toString(item.get("psnId")));
    PsnStatistics psnStatistics = psnStatisticsDao.get(psnId);
    if (psnStatistics != null) {
      dataMap.put("pubSum", psnStatistics.getPubSum());
      dataMap.put("citedSum", psnStatistics.getCitedSum());
      dataMap.put("hidex", psnStatistics.getHindex());
    } else {
      dataMap.put("pubSum", "");
      dataMap.put("citedSum", "");
      dataMap.put("hidex", "");
    }
    dataMap.put("psnId", psnId);
    dataMap.put("des3PsnId", ServiceUtil.encodeToDes3(psnId.toString()));
    dataMap.put("title", StringUtils.isNotBlank(title) ? title : ObjectUtils.toString(item.get("title")));
    if ("zh_CN".equals(language)) {
      dataMap.put("name", StringUtils.isNotBlank(psnName) ? psnName : enPsnName);
      dataMap.put("insName", StringUtils.isNotBlank(zhInsName) ? zhInsName : enInsName);
    } else {
      dataMap.put("name", StringUtils.isNotBlank(enPsnName) ? enPsnName : psnName);
      dataMap.put("insName", StringUtils.isNotBlank(enInsName) ? enInsName : zhInsName);
    }
  }

}
