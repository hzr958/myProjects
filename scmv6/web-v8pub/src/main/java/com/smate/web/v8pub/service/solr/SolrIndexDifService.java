package com.smate.web.v8pub.service.solr;

import com.smate.web.v8pub.dom.pdwh.PubPdwhDetailDOM;

/**
 * 基准库Solr索引服务实现
 * 
 * @author YJ
 *
 */

public interface SolrIndexDifService {

  /**
   * 基准库改造，成果索引
   * 
   * @param pubIndexUrl
   * @param publishDay
   * @param publishMonth
   * @param publishYear
   * 
   * @param pdwhPubId
   * @throws Exception
   */
  void indexPublication(PubPdwhDetailDOM pubPdwhDetailDOM, String pubIndexUrl, Integer publishYear,
      Integer publishMonth, Integer publishDay) throws Exception;

  /**
   * 基准库改造，专利索引
   * 
   * @param pubIndexUrl
   * @param publishDay
   * @param publishMonth
   * @param publishYear
   * 
   * @param pdwhPubId
   * @throws Exception
   */
  void indexPatent(PubPdwhDetailDOM pubPdwhDetailDOM, String pubIndexUrl, Integer publishYear, Integer publishMonth,
      Integer publishDay) throws Exception;

  /**
   * 更新sorl
   * 
   * @param des3PdwhPubId
   * @param pubIndexUrl
   * @return
   */
  String updateSolr(Long pdwhPubId, String pubIndexUrl);
}
