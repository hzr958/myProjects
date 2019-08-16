package com.smate.center.open.service.data.pub;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.solr.client.solrj.response.FacetField;
import org.apache.solr.client.solrj.response.FacetField.Count;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.open.consts.OpenConsts;
import com.smate.center.open.consts.OpenMsgCodeConsts;
import com.smate.center.open.service.data.ThirdDataTypeBase;
import com.smate.core.base.pub.enums.PublicationTypeEnum;
import com.smate.core.base.solr.model.QueryFields;
import com.smate.core.base.solr.service.SolrIndexSerivceImpl;
import com.smate.core.base.solr.service.SolrIndexService;
import com.smate.core.base.utils.json.JacksonUtils;

/**
 * 根据地区在solr检索基准库论文
 * 
 * @author zll
 *
 */
@Transactional(rollbackFor = Exception.class)
public class SearchPdwhPaperByRegionIdServiceImpl extends ThirdDataTypeBase {
  public static Long[] pubType = new Long[] {(long) PublicationTypeEnum.CONFERENCE_PAPER,
      (long) PublicationTypeEnum.JOURNAL_ARTICLE, (long) PublicationTypeEnum.THESIS};// 成果分类参考PublicationTypeEnum中的分类
  @Value("${domainscm}")
  private String domainscm;
  @Autowired
  private SolrIndexService solrIndexService;


  @Override
  public Map<String, Object> doVerify(Map<String, Object> parameter) {
    Map<String, Object> temp = new HashMap<String, Object>();
    Map<String, Object> serviceData = super.checkDataMapParamet(parameter, temp);
    if (temp.get(OpenConsts.RESULT_STATUS) != null
        && OpenConsts.RESULT_STATUS_ERROR.equalsIgnoreCase(temp.get(OpenConsts.RESULT_STATUS).toString())) {
      // 校验公用参数
      return temp;
    }
    Object pageNo = serviceData.get("pageNo");
    Object pageSize = serviceData.get("pageSize");
    if (pageNo == null) {
      logger.error("具体服务类型参数pageNo不能为空");
      temp = super.errorMap(OpenMsgCodeConsts.SCM_272, parameter, "具体服务类型参数pageNo不能为空");
      return temp;
    } else if (!NumberUtils.isNumber(pageNo.toString())
        || NumberUtils.toInt(serviceData.get("pageNo").toString()) < 1) {
      logger.error("具体服务类型参数pageNo格式不正确");
      temp = super.errorMap(OpenMsgCodeConsts.SCM_274, parameter, "具体服务类型参数pageNo格式不正确");
      return temp;
    }
    if (pageSize == null) {
      logger.error("具体服务类型参数pageSize不能为空");
      temp = super.errorMap(OpenMsgCodeConsts.SCM_273, parameter, "具体服务类型参数pageSize不能为空");
      return temp;
    } else if (!NumberUtils.isNumber(pageSize.toString())
        || NumberUtils.toInt(serviceData.get("pageSize").toString()) < 1) {
      logger.error("具体服务类型参数pageSize格式不正确");
      temp = super.errorMap(OpenMsgCodeConsts.SCM_275, parameter, "具体服务类型参数pageSize格式不正确");
      return temp;
    }

    if (serviceData.get("regionCode") == null) {
      logger.error("根据地区检索论文，regionCode不能为空，且必须为数字");
      temp = super.errorMap(OpenMsgCodeConsts.SCM_833, parameter, "SCM_833 根据地区检索论文，regionCode不能为空，且必须为数字  ");
      return temp;
    } else if (!NumberUtils.isNumber(serviceData.get("regionCode").toString())) {
      logger.error("根据地区检索论文，regionCode不能为空，且必须为数字");
      temp = super.errorMap(OpenMsgCodeConsts.SCM_833, parameter, "SCM_833 根据地区检索论文，regionCode不能为空，且必须为数字  ");
      return temp;
    }
    parameter.putAll(serviceData);
    temp.put(OpenConsts.RESULT_STATUS, OpenConsts.RESULT_STATUS_SUCCESS);
    return temp;
  }

  @SuppressWarnings("unchecked")
  @Override
  public Map<String, Object> doHandler(Map<String, Object> parameter) {
    // 具体业务
    List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();
    QueryFields queryFields = new QueryFields();
    Integer pageNo = NumberUtils.toInt(parameter.get("pageNo").toString());
    Integer pageSize = NumberUtils.toInt(parameter.get("pageSize").toString());
    Long regionCode = NumberUtils.toLong(parameter.get("regionCode").toString());
    queryFields.setRegionCode(regionCode);
    if (parameter.get("pubType") != null && StringUtils.isNotBlank(parameter.get("pubType").toString())) {
      queryFields.setPubTypeId(Integer.valueOf(parameter.get("pubType").toString()));
    }
    if (parameter.get("pubYear") != null && StringUtils.isNotBlank(parameter.get("pubYear").toString())) {
      queryFields.setPubYear(Integer.valueOf(parameter.get("pubYear").toString()));
    }
    if (parameter.get("pubCategory") != null && StringUtils.isNotBlank(parameter.get("pubCategory").toString())) {
      queryFields.setPubSuperCatgory(Integer.valueOf(parameter.get("pubCategory").toString()));
    }
    if (parameter.get("searchKey") != null && StringUtils.isNotBlank(parameter.get("searchKey").toString())) {
      queryFields.setSearchString(parameter.get("searchKey").toString());
    }
    String[] facetField = {"pubYear", "pubTypeId", "pubSuperCategory"};
    queryFields.setFacetField(facetField);
    // 直接检索
    Map<String, Object> mapData = new HashMap<String, Object>();
    try {
      mapData = solrIndexService.restfulQueryPubs(pageNo, pageSize, queryFields);
      Map<String, Object> totalCountMap = new HashMap<String, Object>();
      totalCountMap.put("totalCount", mapData.get(SolrIndexSerivceImpl.RESULT_NUMFOUND));
      dataList.add(totalCountMap);// 论文总数
      String items = (String) mapData.get(SolrIndexSerivceImpl.RESULT_ITEMS);
      List<FacetField> facetList = (List<FacetField>) mapData.get(SolrIndexSerivceImpl.RESULT_FACET);
      boolean flag = JacksonUtils.isJsonString(items);
      if (flag) {
        Map<String, Object> facetCountData = new HashMap<String, Object>();
        if (facetList != null && facetList.size() > 0) {
          buildFacetCountData(facetList, facetCountData);
          dataList.add(facetCountData);
        }
        List<Map<String, Object>> itemsList = JacksonUtils.jsonToList(items);
        if (itemsList != null && itemsList.size() > 0) {
          for (int i = 0; i < itemsList.size(); i++) {
            Map<String, Object> pubInfoMap = new HashMap<String, Object>();
            buildPubInfoMap(itemsList.get(i), pubInfoMap);
            dataList.add(pubInfoMap);
          }
        }
      }
    } catch (Exception e) {
      logger.error("根据地区获取论文出错", e);
      return errorMap("根据地区获取论文出错", parameter, e.getMessage());
    }

    return successMap(OpenMsgCodeConsts.SCM_000, dataList);
  }

  private void buildPubInfoMap(Map<String, Object> item, Map<String, Object> pubInfoMap) {
    pubInfoMap.put("pubId", item.get("pubId"));
    pubInfoMap.put("pubTitle", item.get("pubTitle"));
    pubInfoMap.put("authors", item.get("authors"));
    pubInfoMap.put("pubBrief", item.get("pubBrief"));
    pubInfoMap.put("pubShortUrl", item.get("pubShortUrl"));
    pubInfoMap.put("downLoadCount", item.get("downLoadCount"));
    pubInfoMap.put("readCount", item.get("readCount"));
    pubInfoMap.put("fullTextImgUrl", item.get("fullTextImgUrl"));
  }

  private void buildFacetCountData(List<FacetField> facetList, Map<String, Object> countData) {
    for (FacetField f : facetList) {
      String name = f.getName();
      List<Count> countList = f.getValues();
      if (CollectionUtils.isNotEmpty(countList)) {
        switch (name) {
          case "pubYear":
            this.buildYearCountMap(f.getValues(), countData);
            break;
          case "pubTypeId":
            this.buildTypeCountMap(f.getValues(), countData);
            break;
          case "pubSuperCategory":
            this.buildSuperCategoryCountMap(f.getValues(), countData);
            break;
        }
      }
    }


  }

  private void buildYearCountMap(List<Count> countList, Map<String, Object> countData) {
    Map<Long, Long> map = new LinkedHashMap<Long, Long>();
    int curYear = Calendar.getInstance().get(Calendar.YEAR);
    for (Count count : countList) {
      if (count.getName() != null && NumberUtils.isNumber(count.getName()))
        if (NumberUtils.toInt(count.getName()) <= curYear)
          map.put(NumberUtils.toLong(count.getName()), count.getCount());
    }
    countData.put("yearCountMap", map);
  }

  private void buildSuperCategoryCountMap(List<Count> countList, Map<String, Object> countData) {
    Map<Long, Long> temp = new LinkedHashMap<Long, Long>();
    for (Count count : countList) {
      Long catId = NumberUtils.toLong(count.getName());
      temp.put(catId, count.getCount());
    }
    countData.put("categoryCountMap", temp);
  }

  private void buildTypeCountMap(List<Count> countList, Map<String, Object> countData) {
    Map<Long, Long> temp = new LinkedHashMap<Long, Long>();
    Long otherTypeNum = 0L;// 那些不在页面上存在对应分类的成果{pubTyID=1,2,10},将放在"其他"栏SCM-24123
    for (Count count : countList) {
      Long typeId = NumberUtils.toLong(count.getName());
      if (ArrayUtils.contains(pubType, typeId)) {// 是否在已存在分类中
        temp.put(typeId, count.getCount());
      } else {
        otherTypeNum += count.getCount();
      }
    }
    temp.put((long) PublicationTypeEnum.OTHERS, otherTypeNum);// 将那些不包含在页面上显示的论文作为“其他”进行显示
    for (Long num : pubType) {
      if (!temp.containsKey(num)) {// 将没有数据的那些设置为0
        temp.put(num, 0L);
      }
    }
    countData.put("TypeCountMap", temp);
  }



}
