package com.smate.center.batch.service.pub;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.solr.client.solrj.SolrServer;

import com.smate.center.batch.model.pdwh.pub.KeywordsHighAndNewTech;
import com.smate.center.batch.model.pdwh.pub.PubKeywordsSubsetsHntTmp;
import com.smate.center.batch.model.sns.pub.ProjectDataFiveYear;

public interface ProjectDataFiveYearService {

  public String findPatenXml(Long pubId);

  public void updatePatentLog(Long resId, Integer resStatus, String resMsg);

  public List<Object> findPatentList(Integer size, Long startPubId, Long endPubId);

  List<ProjectDataFiveYear> getProjectDataList(Integer size, Long i);

  Set<String> handlePubKeywords(String keywords);

  StringBuilder conbinePubKeywords(String applicationCode, Set<String> keywordsSet, StringBuilder strBuilder);

  Map<Integer, Map<Integer, String>> getPubKwSubsets(String kws);

  void saveAllSubsets(Map<Integer, Map<Integer, String>> mp, String disCode, String id);

  Long getTotalCounts();

  void classifyByKeywords(Map<Integer, Map<Integer, String>> mp, String approveCode, Integer language);

  void saveKeywordsHighAndNewTech(KeywordsHighAndNewTech keywordsHighAndNewTech);

  List<String> getKwStrsByCategory(String category);

  Map<Integer, Map<Integer, String>> getKwSubsetsFromPubContent(String kws);

  void saveAllSubsetsHnt(Map<Integer, Map<Integer, String>> mp, String disCode, String id);

  void saveRs(PubKeywordsSubsetsHntTmp pt);

  List<PubKeywordsSubsetsHntTmp> getCategory(Integer status);

  void classifyByKeywordsHnt(Map<Integer, Map<Integer, String>> mp, String approveCode, Integer language);

  List<String> getKwStrsByCategoryByLanguage(String category, Integer language);

  List<String> getKwStrs();

  List<String> getNsfcKwByLanguage(Integer language);

  List<String> getPubContent(Long psnId);

  void saveAllSubsetsPsn(Map<Integer, Map<Integer, String>> mp, Long psnId, Integer language);

  Map<Long, String> getPubEnContentMap(Long psnId);

  void saveAllSubsetsPsn(Map<Integer, Map<Integer, String>> mp, Long psnId, Long pubId, Integer origin);

  Map<Long, String> getPubZhContentMap(Long psnId);

  List<Object> findPubList(Integer size, Long startPubId, Long endPubId);

  void updatePubLog(Long resId, Integer resStatus, String resMsg);

  void pubClassifyByKeywords(Map<Integer, Map<Integer, String>> mp, String approveCode, Integer language);

  List<String> getNsfcKwStrsDiscipline();

  List<ProjectDataFiveYear> getProjectDataList(Integer size, Long id, String category);

  Map<Integer, Map<Integer, String>> getKwSubsetsFromPubContentNsfc(String kws);

  void saveAllSubsetsHntNsfc(Map<Integer, Map<Integer, String>> mp, String disCode, String id);

  String[] getAllNsfcKws(String kws);

  void saveAllNsfcKw(String[] kws, String disCode, String id);

  List<BigDecimal> getTohandlePdwhPubJournal();

  void updateTohandlePdwhPubJournal(Integer status, Long pubId);

  void updateTohandlePdwhPubJournalJnlId(Long jnlId, Long pubId);

  String getXmlStr(Long pubId);

  List<String> getNsfcKwStrsDiscipline(Integer language);

  Integer saveKwStrToSolr(SolrServer server, String content, String disCode, String id) throws Exception;

}
