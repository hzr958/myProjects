package com.smate.center.task.service.solrindex;

import java.util.Date;
import java.util.Map;

import org.apache.solr.client.solrj.SolrServerException;

public interface SolrIndexDifService {

  public void runIndex();

  public void addPsnIndex(Long psnId);

  public void calculateTF();

  public void deletePrivateUser();

  public void getFulltextPath();

  public Map<String, Object> getRcmdPatents(Integer pageCount, Integer size, String queryKwString, String categoryStr)
      throws SolrServerException;

  public Map<String, Object> getRequestRcmdFromPatent(Integer pageCount, Integer size, String queryKwString,
      String categoryStr) throws SolrServerException;

  /**
   * 基金推荐solr
   * 
   * @param updateDate TODO
   * @param Integer pageCount, Integer size, String regionEn, String regionZh, String categoryEn,
   *        String cateogryZh, Integer qualification, Integer timeGap(1:一周以内；2:1个月以内；3:3个月以内)
   * 
   * @return
   * @throws SolrServerException
   */
  public Map<String, Object> getFundRecommend(Integer pageCount, Integer size, String regionEn, String regionZh,
      String categoryEn, String cateogryZh, Integer qualification, Integer timeZone, Integer fundInsTypeEnterprise,
      Integer fundInsTypeResearchIns, Date updateDate) throws SolrServerException;

}
