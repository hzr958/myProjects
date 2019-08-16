package com.smate.center.task.dao.sns.psn;

import java.io.Serializable;
import java.util.List;

import com.smate.center.task.exception.ServiceException;
import com.smate.center.task.model.fund.rcmd.ConstFundCategoryKeywords;
import com.smate.center.task.model.sns.pub.KeywordSplit;


public interface KeywordScoreService extends Serializable {


  /**
   * 个人关键词hash
   * 
   * @param psnId
   * @return
   * @throws ServiceException
   */
  List<String> getPsnKeyWordsByPsnId(Long psnId) throws ServiceException;

  /**
   * 计算个人特征关键词和基金关键词的匹配.
   * 
   * @param psnKey
   * @param fundKeyList
   * @return
   */
  int matchPsnFundKey(List<String> psnKey, List<ConstFundCategoryKeywords> fundKeyList);

  /**
   * 计算成果拆分关键词和基金关键词的匹配.
   * 
   * @param keywordsList
   * @param fundKeyList
   * @return
   */
  List<KeywordSplit> matchPubFundKey(List<KeywordSplit> keywordsList, List<ConstFundCategoryKeywords> fundKeyList);

  /**
   * 根据基金ID匹配基金专题关键词和参数关键词.
   * 
   * @param paramKeyList
   * @param fundId
   * @return
   */
  List<String> matchFundTopicKey(List<String> paramKeyList, Long fundId);

  /**
   * 匹配基金专题关键词和参数关键词.
   * 
   * @param paramKeyList
   * @param fundTopicList
   * @return
   */
  List<String> matchFundTopic(List<String> paramKeyList, List<String> fundTopicList);

  /**
   * 获取和基金关键词匹配的关键词记录.
   * 
   * @param keywordsList
   * @param fundId
   * @return
   */
  List<KeywordSplit> countPubAndFundKeyword(List<KeywordSplit> keywordsList, Long fundId);

  /**
   * 计算个人关键词与基金关键词
   * 
   * @param psnKey
   * @param categoryId
   * @return
   */

  int countPsnAndFundKeyword(List<String> psnKey, Long categoryId);
}
