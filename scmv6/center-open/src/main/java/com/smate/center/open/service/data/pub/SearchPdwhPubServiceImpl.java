package com.smate.center.open.service.data.pub;

import com.smate.center.open.consts.OpenConsts;
import com.smate.center.open.consts.OpenMsgCodeConsts;
import com.smate.center.open.service.data.ThirdDataTypeBase;
import com.smate.core.base.solr.model.QueryFields;
import com.smate.core.base.solr.service.SolrIndexSerivceImpl;
import com.smate.core.base.solr.service.SolrIndexService;
import com.smate.core.base.utils.data.XmlUtil;
import com.smate.core.base.utils.json.JacksonUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 通过solr检索基准库的成果
 * 
 * @author aijiangbin
 * @date 2018年8月23日
 */
@Transactional(rollbackFor = Exception.class)
public class SearchPdwhPubServiceImpl extends ThirdDataTypeBase {

  protected Logger logger = LoggerFactory.getLogger(getClass());


  @Autowired
  public SolrIndexService solrIndexService;
  @Value("${domainscm}")
  private String scmDomain;


  @Override
  public Map<String, Object> doVerify(Map<String, Object> paramet) {
    Map<String, Object> temp = new HashMap<String, Object>();
    Map<String, Object> serviceData = super.checkDataMapParamet(paramet, temp);
    if (temp.get(OpenConsts.RESULT_STATUS) != null
        && OpenConsts.RESULT_STATUS_ERROR.equalsIgnoreCase(temp.get(OpenConsts.RESULT_STATUS).toString())) {
      // 校验公用参数
      return temp;
    }
    Object pageNo = serviceData.get("pageNo");
    Object pageSize = serviceData.get("pageSize");


    if (pageNo == null) {
      logger.error("具体服务类型参数pageNo不能为空");
      temp = super.errorMap(OpenMsgCodeConsts.SCM_272, paramet, "具体服务类型参数pageNo不能为空");
      return temp;
    }
    if (pageSize == null) {
      logger.error("具体服务类型参数pageSize不能为空");
      temp = super.errorMap(OpenMsgCodeConsts.SCM_273, paramet, "具体服务类型参数pageSize不能为空");
      return temp;
    }
    if (!NumberUtils.isNumber(pageNo.toString()) || NumberUtils.toInt(serviceData.get("pageNo").toString()) < 1) {
      logger.error("具体服务类型参数pageNo格式不正确");
      temp = super.errorMap(OpenMsgCodeConsts.SCM_274, paramet, "具体服务类型参数pageNo格式不正确");
      return temp;
    }
    if (!NumberUtils.isNumber(pageSize.toString()) || NumberUtils.toInt(serviceData.get("pageSize").toString()) < 1) {
      logger.error("具体服务类型参数pageSize格式不正确");
      temp = super.errorMap(OpenMsgCodeConsts.SCM_275, paramet, "具体服务类型参数pageSize格式不正确");
      return temp;
    }



    paramet.putAll(serviceData);
    temp.put(OpenConsts.RESULT_STATUS, OpenConsts.RESULT_STATUS_SUCCESS);
    return temp;
  }

  @Override
  public Map<String, Object> doHandler(Map<String, Object> paramet) {
    // 具体业务
    List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();
    // 用于判断详情页面优先显示的内容
    String orderBy = paramet.get("orderBy") == null ? "DEFAULT" : paramet.get("orderBy").toString();
    List<Integer> pubTypeList = parsePubType(paramet);
    List<Long> excludePubId = parseExcludePubId(paramet);
    Integer pageNo = NumberUtils.toInt(paramet.get("pageNo").toString());
    Integer pageSize = NumberUtils.toInt(paramet.get("pageSize").toString());
    String searchKey = paramet.get("searchKey").toString();

    Object title = paramet.get("title");
    Object keywords = paramet.get("keywords");
    Object authors = paramet.get("authors");

    Object pubYear = paramet.get("pubYear");
    QueryFields queryFields = new QueryFields();

    queryFields.setTitle(title != null ? title.toString() : "");
    queryFields.setKeywords(keywords != null ? keywords.toString() : "");
    queryFields.setAuthors(authors != null ? authors.toString() : "");
    // 成果年份
    if (pubYear != null && NumberUtils.isCreatable(pubYear.toString())) {
      queryFields.setPubYear(Integer.parseInt(pubYear.toString()));
    }
    // 过滤成果类型
    if (pubTypeList.size() > 0) {
      queryFields.setPubTypeIdList(pubTypeList);
    }
    // 过滤成果id
    if (excludePubId.size() > 0) {
      queryFields.setExcludedPubIds(excludePubId);
    }
    queryFields.setSearchString(searchKey);
    queryFields.setOrderBy(orderBy);
    if (XmlUtil.containZhChar(searchKey)) {
      queryFields.setLanguage(queryFields.LANGUAGE_ZH);
    } else {
      queryFields.setLanguage(queryFields.LANGUAGE_EN);
    }
    // 直接检索
    Map<String, Object> mapData = new HashMap<String, Object>();
    try {
      mapData = solrIndexService.queryPubs(pageNo, pageSize, queryFields);
      String numFound = (String) mapData.get(SolrIndexSerivceImpl.RESULT_NUMFOUND);
      Map<String, Object> extraMap = new HashMap<>();
      // 设置页数
      if (NumberUtils.isCreatable(numFound)) {
        Long tatalCount = NumberUtils.toLong(numFound);
        extraMap.put("count", tatalCount);
        if (tatalCount % pageSize == 0) {
          extraMap.put("totalPages", tatalCount / pageSize);
        } else {
          extraMap.put("totalPages", tatalCount / pageSize + 1);
        }
      }
      dataList.add(extraMap);
      String items = (String) mapData.get(SolrIndexSerivceImpl.RESULT_ITEMS);
      boolean flag = JacksonUtils.isJsonString(items);
      if (flag) {
        List<Map<String, Object>> itemsList = JacksonUtils.jsonToList(items);
        if (itemsList != null && itemsList.size() > 0) {
          for (int i = 0; i < itemsList.size(); i++) {
            Map item = itemsList.get(i);
            Map<String, Object> pubInfoMap = new HashMap<String, Object>();
            setPdwhPubInfo(item, pubInfoMap);
            dealErrorPubShortUrl(pubInfoMap);
            dataList.add(pubInfoMap);
          }
        }
      }
    } catch (Exception e) {
      logger.error("检索数据异常!", e);
    }
    return successMap(OpenMsgCodeConsts.SCM_000, dataList);
  }


  /**
   * 设置基准库的成果信息 如果信息不够 ，就要从v8pub项目中查找成果，来添加额外的信息。
   * 
   * @param item
   * @param dataMap
   */
  public void setPdwhPubInfo(Map<String, Object> item, Map<String, Object> dataMap) {
    String id = "";
    if (item.get("id") != null) {
      id = item.get("id").toString();
    }
    if (item.get("pubId") != null && NumberUtils.isNumber(item.get("pubId").toString())) {
      dataMap.put("pubId", NumberUtils.toLong(item.get("pubId").toString()));
    }
    String pubTitle = "";
    String authors = "";
    String pubBrief = "";
    if (item.get("pubDbId") != null && NumberUtils.isNumber(item.get("pubDbId").toString())) {
      dataMap.put("pubDbId", NumberUtils.toInt(item.get("pubDbId").toString()));
    } else {
      dataMap.put("pubDbId", "");
    }
    if (item.get("pubTitle") != null) {
      pubTitle = item.get("pubTitle").toString();
    }
    if (item.get("authors") != null) {
      authors = item.get("authors").toString();
    }
    if (item.get("pubBrief") != null) {
      pubBrief = item.get("pubBrief").toString();
    }
    dataMap.put("title", pubTitle);
    dataMap.put("pubBrief", pubBrief);
    dataMap.put("authors", authors);
    dataMap.put("pubShortUrl", item.get("pubShortUrl") != null ? item.get("pubShortUrl") : "");
  }

  /**
   * @param paramet
   */
  private List<Integer> parsePubType(Map<String, Object> paramet) {
    List<Integer> pubTypeList = new ArrayList<Integer>();
    Object pubTypeObj = paramet.get("pubType");
    if (pubTypeObj != null && StringUtils.isNotBlank(pubTypeObj.toString())) {
      String[] split = pubTypeObj.toString().split(",");
      if (split != null && split.length > 0) {
        for (int i = 0; i < split.length; i++) {
          if (split[i] != null && NumberUtils.isNumber(split[i].trim())) {
            pubTypeList.add(Integer.parseInt(split[i].trim()));
          }
        }
      }
    }
    return pubTypeList;
  }

  /**
   * @param paramet
   */
  private List<Long> parseExcludePubId(Map<String, Object> paramet) {
    List<Long> pubTypeList = new ArrayList<Long>();
    Object pubTypeObj = paramet.get("excludePubIds");
    if (pubTypeObj != null && StringUtils.isNotBlank(pubTypeObj.toString())) {
      String[] split = pubTypeObj.toString().split(",");
      if (split != null && split.length > 0) {
        for (int i = 0; i < split.length; i++) {
          if (split[i] != null && NumberUtils.isNumber(split[i].trim())) {
            pubTypeList.add(Long.parseLong(split[i].trim()));
          }
        }
      }
    }
    return pubTypeList;
  }



  /**
   * 处理错误的 成果短地址
   * 
   * @param dataMap
   */
  private void dealErrorPubShortUrl(Map<String, Object> dataMap) {
    Object pubShortUrlObj = dataMap.get("pubShortUrl");
    if (pubShortUrlObj != null && pubShortUrlObj.toString().startsWith("null/")) {
      String replace = pubShortUrlObj.toString().replace("null/", this.scmDomain + "/");
      dataMap.put("pubShortUrl", replace);
    }
  }



}
