package com.smate.web.management.service.analysis;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.smate.web.management.model.analysis.sns.KeywordsPsnQuery;

/**
 * 用于通过关键词过滤用户，注意，该service仅供KeywordsPsnQueryService使用.
 * 
 * @author lqh
 * 
 */
public interface KeywordsPsnQueryDoService extends Serializable {

  /**
   * 保存关键词信息到数据库.
   * 
   * @param kwList
   * @return
   * @throws ServiceException
   */
  public Long saveKeywordsQueryInfo(List<KeywordsPsnQuery> kwList) throws Exception;

  /**
   * 通过关键词查询id查询相关人员.
   * 
   * @param queryId
   * @return
   * @throws ServiceException
   */
  public List<Long> queryKeywordsPsn(Long queryId, int size) throws Exception;

  /**
   * 通过关键词查询id查询相关人员及关键词相同总数.
   * 
   * @param queryId
   * @param size
   * @return
   * @throws ServiceException
   */
  public Map<Long, Long> queryKeyPsnAndKeyCount(Long queryId, int size) throws Exception;
}
