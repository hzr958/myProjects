package com.smate.web.v8pub.service.pubquery;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.solr.client.solrj.SolrServerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.smate.core.base.pub.vo.PubInfo;
import com.smate.core.base.solr.model.QueryFields;
import com.smate.core.base.solr.service.SolrIndexSerivceImpl;
import com.smate.core.base.solr.service.SolrIndexService;
import com.smate.core.base.utils.json.JacksonUtils;
import com.smate.core.base.utils.security.Des3Utils;
import com.smate.web.v8pub.service.query.PubQueryDTO;
import com.smate.web.v8pub.vo.PubListResult;

public class PubFindQueryInSolrServiceImpl extends AbstractPubQueryService {

  public static Long[] pubType = new Long[] {4L, 3L, 8L, 7L};
  protected Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private SolrIndexService solrIndexService;

  @Override
  public Map<String, Object> checkData(PubQueryDTO pubQueryDTO) {
    return null;
  }

  @Override
  public void queryPubs(PubQueryDTO pubQueryDTO) {
    QueryFields queryFields = this.buildQueryFields(pubQueryDTO);
    try {
      Map<String, Object> rsMap =
          solrIndexService.mobileFindPubs(pubQueryDTO.getPageNo(), pubQueryDTO.getPageSize(), queryFields);
      pubQueryDTO.setPubInfoMap(rsMap);
    } catch (SolrServerException e) {
      logger.error("从solr获取推荐成果出错 ： psnId:" + pubQueryDTO.getSearchPsnId(), e);
      e.printStackTrace();
    }
  }

  private QueryFields buildQueryFields(PubQueryDTO pubQueryDTO) {
    QueryFields queryFields = new QueryFields();
    // 检索字符串
    queryFields.setSearchString(pubQueryDTO.getSearchString());
    // 科技领域
    queryFields.setScienceAreaIds(pubQueryDTO.getSearchArea());
    // 成果类型
    queryFields.setPubTypeIdStr(pubQueryDTO.getSearchPubType());
    if (!"0".equals(pubQueryDTO.getPublishYear())) {// 年份
      queryFields.setPubYearStr(pubQueryDTO.getPublishYear());
    }
    // 收录情况
    queryFields.setPubDBIds(pubQueryDTO.getIncludeType());
    // 查询语言
    if ("zh_CN".equals(pubQueryDTO.getSearchLanguage())) {
      queryFields.setLanguage(queryFields.LANGUAGE_ZH);
    } else if ("en_US".equals(pubQueryDTO.getSearchLanguage())) {
      queryFields.setLanguage(queryFields.LANGUAGE_EN);
    } else {
      queryFields.setLanguage(queryFields.LANGUAGE_ALL);
    }
    // 排序
    queryFields.setOrderBy(pubQueryDTO.getOrderBy());
    return queryFields;
  }

  @Override
  public PubListResult assembleData(PubQueryDTO pubQueryDTO) {
    PubListResult listResult = new PubListResult();
    String count = (String) pubQueryDTO.getPubInfoMap().get(SolrIndexSerivceImpl.RESULT_NUMFOUND);
    String items = (String) pubQueryDTO.getPubInfoMap().get(SolrIndexSerivceImpl.RESULT_ITEMS);
    String highlight = (String) pubQueryDTO.getPubInfoMap().get(SolrIndexSerivceImpl.RESULT_HIGHLIGHT);
    if (StringUtils.isNotBlank(count)) {
      long num = NumberUtils.toLong(count);
      long totalPages = num / pubQueryDTO.getPageSize() + 1;
      pubQueryDTO.setTotalCount(num);
      pubQueryDTO.setTotalPages(totalPages);
    }
    // 构建论文信息
    List<Map<String, Object>> listItems = JacksonUtils.jsonToList(items);
    if (CollectionUtils.isNotEmpty(listItems)) {
      List<PubInfo> infoList = new ArrayList<PubInfo>();
      for (Map<String, Object> item : listItems) {
        PubInfo pubInfo = new PubInfo();
        Long pubId = NumberUtils.toLong(ObjectUtils.toString(item.get("pubId")));
        if (pubIsDelete(pubId)) {
          solrIndexService.deletePaperInsolr(pubId);
          continue;
        }
        pubInfo.setPubId(pubId);
        pubInfo.setDes3PubId(Des3Utils.encodeToDes3(pubId.toString()));
        pubInfo.setTitle(ObjectUtils.toString(item.get("pubTitle")));
        pubInfo.setPublishYear(NumberUtils.toInt(Objects.toString(item.get("pubYear"))));
        pubInfo.setBriefDesc(ObjectUtils.toString(item.get("pubBrief")));
        pubInfo.setAuthorNames(ObjectUtils.toString(item.get("authors")));
        pubInfo.setCitations(NumberUtils.toInt(ObjectUtils.toString(item.get("pubCitations"))));
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

}
