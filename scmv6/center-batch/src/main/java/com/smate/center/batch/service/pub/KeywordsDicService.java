package com.smate.center.batch.service.pub;

import java.io.Serializable;
import java.util.List;

import com.smate.center.batch.exception.pub.ServiceException;
import com.smate.center.batch.model.sns.pub.KeywordSplit;
import com.smate.center.batch.model.sns.pub.PubInfo;

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
  public List<KeywordSplit> findKeywords(String str) throws ServiceException;

  /**
   * 查找内容中的关键词，使用字符串补充关键词.
   * 
   * @param str
   * @param extKws 补充词库.
   * @return
   * @throws ServiceException
   */
  public List<KeywordSplit> findKeywords(String str, List<String> extKws) throws ServiceException;

  /**
   * 查找内容中的关键词，使用KeywordSplit补充关键词.
   * 
   * @param str
   * @param extKws 补充词库.
   * @return
   * @throws ServiceException
   */
  public List<KeywordSplit> findKeywords2(String str, List<KeywordSplit> extKws) throws ServiceException;

  /**
   * 查找成果内容中的关键词.
   * 
   * @param content
   * @return
   * @throws Exception
   */
  public List<KeywordSplit> findPubKeywords(PubInfo pubinfo) throws ServiceException;

  /**
   * 查找成果内容中的关键词.
   * 
   * @param pubinfo
   * @param wtSetting 权重配置.
   * @return
   * @throws ServiceException
   */
  public List<KeywordSplit> findPubKeywords(PubInfo pubinfo, KeywordsWeightSetProcess wtSetting)
      throws ServiceException;
}
