package com.smate.web.management.service.analysis;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.smate.web.management.model.analysis.sns.KeywordsPsnQuery;

/**
 * 用于通过关键词过滤用户，注意，该service仅供远程或者不带事务的service使用.
 * 
 * @author lqh
 * 
 */

public interface KeywordsPsnQueryService extends Serializable {

  /**
   * 通过关键词列表查询相关人员.
   * 
   * @param kwList
   * @return
   * @throws ServiceException
   */
  public List<Long> queryKeywordsPsn(List<KeywordsPsnQuery> kwList, int size) throws Exception;

  /**
   * 通过关键词查询id查询相关人员及关键词相同总数.
   * 
   * @param queryId
   * @param size
   * @return
   * @throws ServiceException
   */
  public Map<Long, Long> queryKeyPsnAndKeyCount(List<KeywordsPsnQuery> kwList, int size) throws Exception;
}
