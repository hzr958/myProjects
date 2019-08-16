package com.smate.web.management.service.analysis;

import java.io.Serializable;
import java.util.List;

import com.smate.web.management.model.analysis.KeywordSplit;
import com.smate.web.management.model.analysis.PubInfo;

/**
 * 使用字典拆分字符串服务.
 * 
 * @author lqh
 * 
 */
public interface KeywordsDicService extends Serializable {

  /**
   * 查找内容中的关键词.
   * 
   * @param content
   * @return
   * @throws Exception
   */
  public List<KeywordSplit> findKeywords(String str) throws Exception;

  /**
   * 查找内容中的关键词，使用字符串补充关键词.
   * 
   * @param str
   * @param extKws 补充词库.
   * @return
   * @throws ServiceException
   */
  public List<KeywordSplit> findKeywords(String str, List<String> extKws) throws Exception;

  /**
   * 查找内容中的关键词，使用KeywordSplit补充关键词.
   * 
   * @param str
   * @param extKws 补充词库.
   * @return
   * @throws ServiceException
   */
  public List<KeywordSplit> findKeywords2(String str, List<KeywordSplit> extKws) throws Exception;

  /**
   * 查找成果内容中的关键词.
   * 
   * @param content
   * @return
   * @throws Exception
   */
  public List<KeywordSplit> findPubKeywords(PubInfo pubinfo) throws Exception;

  /**
   * 查找成果内容中的关键词.
   * 
   * @param pubinfo
   * @param wtSetting 权重配置.
   * @return
   * @throws ServiceException
   */
  public List<KeywordSplit> findPubKeywords(PubInfo pubinfo, KeywordsWeightSetProcess wtSetting) throws Exception;

}
