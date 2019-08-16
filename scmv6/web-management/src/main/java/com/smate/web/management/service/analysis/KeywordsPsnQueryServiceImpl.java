package com.smate.web.management.service.analysis;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.smate.web.management.model.analysis.sns.KeywordsPsnQuery;


/**
 * 用于通过关键词过滤用户，注意，该service仅供远程或者不带事务的service使用.
 * 
 * @author lqh
 * 
 */
// 注意不能带事务，否则会出问题.
@Service("keywordsPsnQueryService")
public class KeywordsPsnQueryServiceImpl implements KeywordsPsnQueryService {

  /**
   * 
   */
  private static final long serialVersionUID = -8804463738409695890L;
  @Autowired
  private KeywordsPsnQueryDoService keywordsPsnQueryDoService;

  @Override
  public List<Long> queryKeywordsPsn(List<KeywordsPsnQuery> kwList, int size) throws Exception {

    Long queryId = keywordsPsnQueryDoService.saveKeywordsQueryInfo(kwList);
    List<Long> psnIdList = keywordsPsnQueryDoService.queryKeywordsPsn(queryId, size);
    return psnIdList;
  }

  @Override
  public Map<Long, Long> queryKeyPsnAndKeyCount(List<KeywordsPsnQuery> kwList, int size) throws Exception {
    Long queryId = keywordsPsnQueryDoService.saveKeywordsQueryInfo(kwList);
    return keywordsPsnQueryDoService.queryKeyPsnAndKeyCount(queryId, size);
  }

}
