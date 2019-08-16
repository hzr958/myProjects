package com.smate.web.v8pub.service.pubquery;

import java.util.ArrayList;
import java.util.HashMap;
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

import com.smate.core.base.pub.enums.PubDbEnum;
import com.smate.core.base.pub.vo.PubInfo;
import com.smate.core.base.solr.model.QueryFields;
import com.smate.core.base.solr.service.SolrIndexSerivceImpl;
import com.smate.core.base.solr.service.SolrIndexService;
import com.smate.core.base.utils.json.JacksonUtils;
import com.smate.core.base.utils.security.Des3Utils;
import com.smate.web.v8pub.service.query.PubQueryDTO;
import com.smate.web.v8pub.vo.PubListResult;

public class PubRecommendQueryInSolrServiceImpl extends AbstractPubQueryService {
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
      Map<String, Object> rsMap = new HashMap<String, Object>();
      rsMap = solrIndexService.getRecommendPubs(pubQueryDTO.getPageNo(), pubQueryDTO.getPageSize(), queryFields);
      /*
       * if ("0".equals(rsMap.get(SolrIndexSerivceImpl.RESULT_NUMFOUND)) &&
       * (!StringUtils.isBlank(pubQueryDTO.getSearchPsnKey()))) { queryFields.setSearchPsnKey(null); rsMap
       * = solrIndexService.getRecommendPubs(pubQueryDTO.getPageNo(), pubQueryDTO.getPageSize(),
       * queryFields); }
       */
      pubQueryDTO.setPubInfoMap(rsMap);
    } catch (SolrServerException e) {
      logger.error("从solr获取推荐成果出错 ： psnId:" + pubQueryDTO.getSearchPsnId(), e);
      e.printStackTrace();
    }
  }

  private QueryFields buildQueryFields(PubQueryDTO pubQueryDTO) {
    QueryFields queryFields = new QueryFields();
    queryFields.setScienceAreaIds(pubQueryDTO.getSearchArea());
    queryFields.setPubTypeIdStr(pubQueryDTO.getSearchPubType());
    queryFields.setTimeGap(pubQueryDTO.getSearchPubYear());
    queryFields.setSearchPsnKey(pubQueryDTO.getSearchPsnKey());
    queryFields.setOrderBy(pubQueryDTO.getOrderBy());
    queryFields.setExcludedPubIds(pubQueryDTO.getPubIds());
    return queryFields;
  }

  @Override
  public PubListResult assembleData(PubQueryDTO pubQueryDTO) {
    String count = (String) pubQueryDTO.getPubInfoMap().get(SolrIndexSerivceImpl.RESULT_NUMFOUND);
    String items = (String) pubQueryDTO.getPubInfoMap().get(SolrIndexSerivceImpl.RESULT_ITEMS);
    if (StringUtils.isNotBlank(count)) {
      long num = NumberUtils.toLong(count);
      long totalPages = num / pubQueryDTO.getPageSize() + 1;
      pubQueryDTO.setTotalCount(num);
      pubQueryDTO.setTotalPages(totalPages);
    }
    // 构建论文信息
    PubListResult listResult = new PubListResult();
    List<Map<String, Object>> listItems = JacksonUtils.jsonToList(items);
    if (CollectionUtils.isNotEmpty(listItems)) {
      List<PubInfo> infoList = new ArrayList<PubInfo>();
      for (Map<String, Object> item : listItems) {
        PubInfo pubInfo = new PubInfo();
        // 页面基本展示信息
        Long pubId = NumberUtils.toLong(ObjectUtils.toString(item.get("pubId")));
        if (pubIsDelete(pubId)) {
          solrIndexService.deletePaperInsolr(pubId);
          int delCount = NumberUtils.toInt(count);
          count = String.valueOf(--delCount >= 0 ? delCount : 0);
          continue;
        }
        Integer pubDbId = NumberUtils.toInt(ObjectUtils.toString(item.get("pubDbId"), "0"));
        pubInfo.setPubDb(PubDbEnum.PDWH);
        pubInfo.setDbid(pubDbId);
        pubInfo.setPubId(pubId);
        pubInfo.setDes3PubId(Des3Utils.encodeToDes3(pubId.toString()));
        pubInfo.setTitle(ObjectUtils.toString(item.get("pubTitle")));
        pubInfo.setPublishYear(NumberUtils.toInt(Objects.toString(item.get("pubYear"))));
        pubInfo.setBriefDesc(ObjectUtils.toString(item.get("pubBrief")));
        pubInfo.setAuthorNames(ObjectUtils.toString(item.get("authors")));
        pubInfo.setCitations(NumberUtils.toInt(ObjectUtils.toString(item.get("pubCitations"))));
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
