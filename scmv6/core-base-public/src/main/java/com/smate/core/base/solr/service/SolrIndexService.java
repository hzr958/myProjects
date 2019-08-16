package com.smate.core.base.solr.service;

import java.util.List;
import java.util.Map;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;

import com.smate.core.base.solr.model.QueryFields;

public interface SolrIndexService {
  /**
   * demo人员检索
   * 
   * @param Integer pageCount, Integer size, QueryFields queryFields
   * @return Map<String, Object>
   * @throws SolrServerException
   */
  public Map<String, Object> queryPersonsDemo(Integer pageCount, Integer size, QueryFields query)
      throws SolrServerException;

  /**
   * 人员检索
   * 
   * @param Integer pageCount, Integer size, QueryFields queryFields
   * @return Map<String, Object>
   * @throws SolrServerException
   */
  public Map<String, Object> queryPersons(Integer pageCount, Integer size, QueryFields query)
      throws SolrServerException;

  /**
   * 专利检索
   * 
   * @param Integer pageCount, Integer size, QueryFields queryFields
   * @return Map<String, Object>
   * @throws SolrServerException
   */
  public Map<String, Object> queryPatents(Integer pageCount, Integer size, QueryFields query)
      throws SolrServerException;

  /**
   * 成果检索
   * 
   * @param Integer pageCount, Integer size, QueryFields queryFields
   * @return Map<String, Object>
   * @throws SolrServerException
   */
  public Map<String, Object> queryPubs(Integer pageCount, Integer size, QueryFields query) throws SolrServerException;

  /**
   * app 论文发现
   * 
   * @param pageCount
   * @param size
   * @param queryFields
   * @return
   * @throws SolrServerException
   */
  public Map<String, Object> mobileFindPubs(Integer pageCount, Integer size, QueryFields queryFields)
      throws SolrServerException;

  /**
   * 成果检索返回成果数
   * 
   * @param String kw
   * 
   * @return Long
   * @throws SolrServerException
   */
  public Long queryPubCount(String kw) throws SolrServerException;

  /**
   * 成果检索按年份返回成果数
   * 
   * @param String kw
   * 
   * @return Long
   * @throws SolrServerException
   */
  public Long queryPubCount(String kw, Integer year) throws SolrServerException;

  /**
   * 创新城为需求书推荐专利
   * 
   * @param Integer pageCount, Integer size, String queryKwString, String categoryStr
   * 
   * @return Map<String, Object>
   * @throws SolrServerException
   */
  public Map<String, Object> getRcmdPatents(Integer pageCount, Integer size, String queryKwString, String categoryStr)
      throws SolrServerException;

  /**
   * 创新城为专利推荐需求书
   * 
   * @param Integer pageCount, Integer size, String queryKwString, String categoryStr
   * 
   * @return Map<String, Object>
   * @throws SolrServerException
   */
  public Map<String, Object> getRequestRcmdFromPatent(Integer pageCount, Integer size, String queryKwString,
      String categoryStr) throws SolrServerException;

  /**
   * 推荐可能认识的人
   * 
   * @param Integer pageCount, Integer size, QueryFields queryFields
   * @return Map<String, Object>
   * @throws SolrServerException
   */
  public Map<String, Object> queryPersonsYouMayKnow(Integer pageCount, Integer size, QueryFields queryFields)
      throws SolrServerException;

  /**
   * 查找不需要显示添加好友按钮的人员ID
   * 
   * @param searchString
   * @return Map<String, Object>
   * @throws SolrServerException
   */
  public Map<String, Object> getSearchSuggestion(String searchString) throws SolrServerException;

  /**
   * 基金推荐solr
   * 
   * @param Integer pageCount, Integer size, String regionEn, String regionZh, String categoryEn,
   *        String cateogryZh, Integer qualification, Integer timeGap(1:一周以内；2:1个月以内；3:3个月以内) Integer
   *        sortOrder
   * @return Map<String, Object>
   * @throws SolrServerException
   */
  public Map<String, Object> getFundRecommend(Integer pageCount, Integer size, String regionEn, String regionZh,
      String categoryEn, String cateogryZh, Integer qualification, Integer timeZone, Integer fundInsTypeEnterprise,
      Integer fundInsTypeResearchIns, Integer sortOrder) throws SolrServerException;

  /**
   * 基金发现solr
   */
  public Map<String, Object> getFundFind(Integer pageCount, Integer size, String searchKey, String regionEn,
      String regionZh, String categoryEn, String categoryZh, Integer fundInsTypeEnterprise,
      Integer fundInsTypeResearchIns, Integer sortOrder) throws SolrServerException;

  /**
   * 基金推荐solr按资助机构获取
   *
   * @param Integer pageCount, Integer size, String regionEn, String regionZh, String categoryEn,
   *        String cateogryZh, Integer qualification, Integer timeGap(1:一周以内；2:1个月以内；3:3个月以内) Integer
   *        sortOrder
   * @return Map<String, Object>
   * @throws SolrServerException
   */
  public Map<String, Object> getRecommendFundRecommend(Integer pageCount, Integer size, String agencyIds, String areaId,
      Integer qualification, Integer timeZone, Integer fundInsTypeEnterprise, Integer fundInsTypeResearchIns,
      Integer sortOrder, List<Long> excludeFundIds) throws SolrServerException;

  /**
   * 成果标题匹配solr
   *
   * @param pubTitle pubType
   * @return Map<String, Object>
   * @throws SolrServerException
   */
  public Map<String, Object> getInfoForPubInput(String pubTitle, Integer pubType) throws SolrServerException;

  public Map<String, Object> getPsnRecommendForFollow(String psnInsName, String psnScienceArea, String psnKeywords,
      Integer size, List<Long> exludedPsnIds, Long currentUserId) throws SolrServerException;

  public Long queryPubCountByKeywords(String keywords, Integer year) throws SolrServerException;

  public Long queryPrjCountByKeywords(String keywords, Integer year) throws SolrServerException;

  public Long queryPatCountByKeywords(String keywords, Integer year) throws SolrServerException;

  public Long queryCiteCountByKeywords(String keywords, Integer year) throws SolrServerException;

  public Long queryTechCountByKeywords(String keywords, Integer year) throws SolrServerException;

  public Map<String, Object> queryProject(Integer pageCount, Integer size, QueryFields queryFields)
      throws SolrServerException;

  public Map<String, Long> queryPubCountByRegionId(String keywords, Long regionId) throws SolrServerException;

  public Map<String, Long> queryPatCountByRegionId(String keywords, Long regionId) throws SolrServerException;

  public Map<String, Long> queryPrjCountByRegionId(String keywords, Long regionId) throws SolrServerException;

  public Map<String, Long> queryTechCountByRegionId(String keywords, Long regionId) throws SolrServerException;

  public Long queryCountPub(String keywords, Integer year, long provinceId) throws SolrServerException;

  public Long queryCountPat(String keywords, Integer year, long provinceId) throws SolrServerException;

  public Long queryCountPrj(String keywords, Integer year, long provinceId) throws SolrServerException;

  public Long queryCountTech(String keywords, Integer year, long provinceId) throws SolrServerException;

  public QueryResponse query(SolrQuery query, List<String> fqList) throws SolrServerException;

  /**
   * 论文--》发现成果
   * 
   * @param pageCount
   * @param size
   * @param query
   * @return
   * @throws SolrServerException
   */
  public Map<String, Object> findPubs(Integer pageCount, Integer size, QueryFields query) throws SolrServerException;

  public Map<String, Object> getRecommendPubs(Integer pageNo, Integer pageSize, QueryFields queryFields)
      throws SolrServerException;

  public List<Map<String, Object>> searchPubByAuthorName(String psnName, int page) throws SolrServerException;

  public List<Map<String, Object>> searchPatentByAuthorName(String psnName, int page) throws SolrServerException;

  public Map<String, Object> queryRelatedKws(String kw, String discode, String queryType) throws SolrServerException;

  public Long queryKwTf(String kw, String discode, String queryType) throws SolrServerException;

  public Map<String, Object> queryRelatedKwsByKwHash(String kw, String discode, String queryType)
      throws SolrServerException;

  /**
   * 通过成果关键词检索人员
   *
   * @param pageCount
   * @param size
   * @param query
   * @return
   * @throws SolrServerException
   */
  public Map<String, Object> queryPersonsByPsnKw(Integer pageCount, Integer size, QueryFields query)
      throws SolrServerException;

  public Map<String, Object> queryPubsNew(Integer pageCount, Integer size, QueryFields queryFields)
      throws SolrServerException;

  public Map<String, Object> queryPubsNewIfNull(Integer pageCount, Integer size, QueryFields queryFields)
      throws SolrServerException;

  public void deletePatInsolr(Long pubId);

  public void deletePaperInsolr(Long pubId);

  public Map<String, Object> getPdwhSearchSuggestStr(String searchKey, Integer type) throws SolrServerException;

  Map<String, Object> getPdwhInsSearchSuggestStr(String searchKey, Integer type) throws SolrServerException;

  Map<String, Object> getPdwhPsnSearchSuggestStr(String searchKey, Integer type) throws SolrServerException;

  /**
   * 获取基金详情
   * 
   * @param fundId
   * @return
   * @throws SolrServerException
   */
  public Map<String, Object> queryFundDetail(Long fundId) throws SolrServerException;

  /**
   * 获取基准库论文接口专用
   * 
   * @param pageNo
   * @param pageSize
   * @param queryFields
   * @return
   */
  public Map<String, Object> restfulQueryPubs(Integer pageNo, Integer pageSize, QueryFields queryFields)
      throws SolrServerException;
}
