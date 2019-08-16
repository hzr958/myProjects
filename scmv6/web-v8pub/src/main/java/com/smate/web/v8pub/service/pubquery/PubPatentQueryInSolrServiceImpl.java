package com.smate.web.v8pub.service.pubquery;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.TreeMap;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.FacetField;
import org.apache.solr.client.solrj.response.FacetField.Count;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.smate.core.base.pub.vo.PubInfo;
import com.smate.core.base.solr.model.QueryFields;
import com.smate.core.base.solr.service.SolrIndexSerivceImpl;
import com.smate.core.base.solr.service.SolrIndexService;
import com.smate.core.base.utils.data.XmlUtil;
import com.smate.core.base.utils.json.JacksonUtils;
import com.smate.core.base.utils.security.Des3Utils;
import com.smate.web.v8pub.dao.pdwh.PdwhPubSituationDAO;
import com.smate.web.v8pub.service.query.PubQueryDTO;
import com.smate.web.v8pub.vo.PubListResult;

public class PubPatentQueryInSolrServiceImpl extends AbstractPubQueryService {
  public static Long[] patentType = new Long[] {51L, 52L, 53L, 7L};
  protected Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private SolrIndexService solrIndexService;
  @Autowired
  private PdwhPubSituationDAO PdwhPubSituationDao;

  @Override
  public Map<String, Object> checkData(PubQueryDTO pubQueryDTO) {
    return null;
  }

  @Override
  public void queryPubs(PubQueryDTO pubQueryDTO) {
    QueryFields queryFields = this.buildQueryFields(pubQueryDTO);
    try {
      Map<String, Object> rsMap =
          solrIndexService.queryPatents(pubQueryDTO.getPageNo(), pubQueryDTO.getPageSize(), queryFields);
      pubQueryDTO.setPubInfoMap(rsMap);
    } catch (SolrServerException e) {
      logger.error("从solr获取推荐成果出错 ： psnId:" + pubQueryDTO.getSearchPsnId(), e);
      e.printStackTrace();
    }
  }

  private QueryFields buildQueryFields(PubQueryDTO pubQueryDTO) {
    QueryFields queryFields = new QueryFields();
    if (XmlUtil.containZhChar(pubQueryDTO.getSearchString())) {
      queryFields.setLanguage(queryFields.LANGUAGE_ZH);
    } else {
      queryFields.setLanguage(queryFields.LANGUAGE_EN);
    }
    queryFields.setSeqQuery(pubQueryDTO.getSeqQuery());
    queryFields.setSearchString(pubQueryDTO.getSearchString());
    // 发表年份（多个，逗号隔开或空格隔开）
    queryFields.setYears(pubQueryDTO.getPublishYear());
    queryFields.setPatYear(pubQueryDTO.getSearchPatYear());
    queryFields.setPatTypeId(pubQueryDTO.getSearchPatTypeId());
    // 专利类型（多个，逗号隔开或空格隔开）
    queryFields.setPatTypeIdStr(pubQueryDTO.getSearchPubType());
    // 排序
    queryFields.setOrderBy(pubQueryDTO.getOrderBy());
    // 收录情况
    queryFields.setPubDBIds(pubQueryDTO.getIncludeType());
    return queryFields;
  }

  @Override
  public PubListResult assembleData(PubQueryDTO pubQueryDTO) {
    PubListResult listResult = new PubListResult();
    if (PubQueryDTO.SEQ_2.equals(pubQueryDTO.getSeqQuery())) {// 包含分组
      return assembleResultData(assembleFacetData(listResult, pubQueryDTO), pubQueryDTO);
    } else {// 只要结果集
      return assembleResultData(listResult, pubQueryDTO);
    }
  }

  /**
   * 组装分组数据
   * 
   * @param listResult
   * @param pubQueryDTO
   * @return
   */
  private PubListResult assembleFacetData(PubListResult listResult, PubQueryDTO pubQueryDTO) {
    List<FacetField> facetList = (List<FacetField>) pubQueryDTO.getPubInfoMap().get(SolrIndexSerivceImpl.RESULT_FACET);
    Map<String, Object> resultData = new HashMap<String, Object>();
    if (facetList != null && facetList.size() > 0) {
      buildYearPubTypeFromPatent(facetList, resultData);
      listResult.setResultData(resultData);
    }
    return listResult;
  }

  /**
   * 组装结果数据
   * 
   * @param listResult
   * @param pubQueryDTO
   * @return
   */
  private PubListResult assembleResultData(PubListResult listResult, PubQueryDTO pubQueryDTO) {
    String count = (String) pubQueryDTO.getPubInfoMap().get(SolrIndexSerivceImpl.RESULT_NUMFOUND);
    String items = (String) pubQueryDTO.getPubInfoMap().get(SolrIndexSerivceImpl.RESULT_ITEMS);
    String highlight = (String) pubQueryDTO.getPubInfoMap().get(SolrIndexSerivceImpl.RESULT_HIGHLIGHT);
    if (StringUtils.isNotBlank(count)) {
      long num = NumberUtils.toLong(count);
      long totalPages = num / pubQueryDTO.getPageSize() + 1;
      pubQueryDTO.setTotalCount(num);
      pubQueryDTO.setTotalPages(totalPages);
    }
    boolean ishighlight = JacksonUtils.isJsonString(highlight);
    List<Map<String, Object>> listItems = JacksonUtils.jsonToList(items);
    Map<String, Map<String, List<String>>> highligh = JacksonUtils.jsonToMap(highlight);
    if (CollectionUtils.isNotEmpty(listItems) && ishighlight) {
      List<PubInfo> infoList = new ArrayList<PubInfo>();
      for (Map<String, Object> item : listItems) {
        PubInfo pubInfo = new PubInfo();
        String id = "";
        if (item.get("id") != null) {
          id = item.get("id").toString();
        }
        if (StringUtils.isNotBlank(id)) {
          buildhighlightData(highligh, pubInfo, id);
        }
        Long pubId = NumberUtils.toLong(ObjectUtils.toString(item.get("patId")));
        if (pubIsDelete(pubId)) {
          solrIndexService.deletePatInsolr(pubId);
          continue;
        }
        pubInfo.setPubId(pubId);
        pubInfo.setDes3PubId(Des3Utils.encodeToDes3(pubId.toString()));
        pubInfo.setTitle(ObjectUtils.toString(item.get("patTitle")));
        pubInfo.setAuthorNames(ObjectUtils.toString(item.get("patAuthors")));
        pubInfo.setBriefDesc(ObjectUtils.toString(item.get("patBrief")));
        pubInfo.setCitations(NumberUtils.toInt(ObjectUtils.toString(item.get("patCitations"))));
        pubInfo.setPublishYear(NumberUtils.toInt(Objects.toString(item.get("patYear"))));
        pubInfo.setDbid(NumberUtils.toInt(ObjectUtils.toString(item.get("pubDbId"))));
        // 查询统计数
        builPdwhPubStatistics(pubInfo, pubQueryDTO.getSearchPsnId());
        // 全文
        buildPdwhPubFulltext(pubInfo, pubQueryDTO.getSearchPsnId());
        // 短地址
        buildPdwhPubIndexUrl(pubInfo, pubQueryDTO.getSearchPsnId());
        infoList.add(pubInfo);
      }
      listResult.setResultList(infoList);
    }
    listResult.setTotalCount(NumberUtils.toInt(count));
    return listResult;
  }

  private void buildhighlightData(Map<String, Map<String, List<String>>> highligh, PubInfo pubInfo, String id) {
    // 高亮 patTitle enPatTitle patAuthors
    if (StringUtils.isNotBlank(id)) {
      Map<String, List<String>> highlightMap = highligh.get(id);
      if (highlightMap != null && highlightMap.size() > 0) {
        Set<String> keySet = highlightMap.keySet();
        // 论文 的高亮字段 ，zhTitle enTitle authors // doi
        for (String string : keySet) {
          StringBuffer sb = new StringBuffer();
          List<String> list = highlightMap.get(string);
          if (list == null)
            continue;
          for (String string2 : list) {
            sb.append(string2);
          }
          switch (string) {
            case "patTitle":
              pubInfo.setTitle(sb.toString());
              break;
            case "patAuthors":
              pubInfo.setAuthorNames(sb.toString());
              break;
            default:
              break;
          }
        }
      }

    }

  }

  private void buildYearPubTypeFromPatent(List<FacetField> facetList, Map<String, Object> resultData) {
    for (FacetField f : facetList) {
      Map<Long, Long> map = new TreeMap<Long, Long>(new Comparator<Long>() {
        public int compare(Long obj1, Long obj2) {
          return obj2.compareTo(obj1);
        }
      });
      String name = f.getName();
      if ("patYear".equals(name)) {
        List<Count> countList = f.getValues();
        int curYear = Calendar.getInstance().get(Calendar.YEAR);
        if (countList != null && countList.size() > 0) {
          for (int i = 0; i < countList.size(); i++) {
            if (countList.get(i).getName() != null && NumberUtils.isNumber(countList.get(i).getName()))
              if (NumberUtils.toInt(countList.get(i).getName()) <= curYear)
                map.put(NumberUtils.toLong(countList.get(i).getName()), countList.get(i).getCount());
          }
          resultData.put("pubYear", map);
        }
      } else if ("patTypeId".equals(name)) {
        List<Count> countList = f.getValues();
        if (countList != null && countList.size() > 0) {
          for (int i = 0; i < countList.size(); i++) {
            if (countList.get(i).getName() != null && NumberUtils.isNumber(countList.get(i).getName()))
              map.put(NumberUtils.toLong(countList.get(i).getName()), countList.get(i).getCount());
          }
        }
        // 临时排序变量
        Map<Long, Long> temp = new LinkedHashMap<Long, Long>();
        // SCM-23118 uat》全站检索专利》专利类型为“其他”的范围需要确认一下，详情见附件描述。
        for (Long num : patentType) {
          if (num == 7L) {
            Long otherTypeNum = 0L;
            for (Map.Entry<Long, Long> entry : map.entrySet()) {
              Long typeNum = entry.getKey();
              if (typeNum != 51L && typeNum != 52L && typeNum != 53L) {
                otherTypeNum += entry.getValue();
              }
            }
            temp.put(7L, otherTypeNum);
          } else {
            temp.put(num, map.get(num) != null ? map.get(num) : 0L);
          }
        }
        resultData.put("pubType", temp);
      } else if ("patLanguage".equals(name)) {
        List<Count> countList = f.getValues();
        Map<String, Long> temp = new LinkedHashMap<String, Long>();
        temp.put("ZH_CN", 0L);
        temp.put("EN", 0L);
        if (countList != null && countList.size() > 0) {
          for (int i = 0; i < countList.size(); i++) {
            if (countList.get(i).getName() != null)
              temp.put(countList.get(i).getName(), countList.get(i).getCount());
          }
        }
        resultData.put("languageType", temp);
      }
    }
  }

}
