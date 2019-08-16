package com.smate.center.batch.service.pub;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import com.smate.center.batch.model.sns.pub.PdwhPubKeywordsCategory;

public interface PdwhPubKeywordsService {

  List<PdwhPubKeywordsCategory> getPdwhPubKeywords(Integer size, Long startPubId, Long endPubId);

  void saveOpResult(Long pubId, int status);

  void saveOpListResult(List<Long> pubIdList, int status);

  public TreeSet<String> handlePubKeywords(String keywords);

  public StringBuilder conbinePubKeywords(Long categoryId, Set<String> keywordsSet, StringBuilder strBuilder);

  /*
   * 计算长度size为2-5的子集，kwList需要提前查重排序
   * 
   */
  public TreeSet<String> getSubsets(List<String> kwList, Integer size);

  TreeSet<String> getAllSubsets(List<String> kwList);

  public Map<String, Object> getAllSubsetsString(List<String> kwList, String discode, Long pubId);

  TreeSet<String> getSubsetsLengthTwo(List<String> kwList, String kw, List<String> relatedKws);

  TreeSet<String> getGroupKwsByCotfTwo(List<String> kwList, String kw, List<String> relatedKws, Integer counts,
      List<Object> kwsCotf);

}
