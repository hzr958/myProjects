package com.smate.web.management.service.analysis;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.web.management.model.analysis.sns.KeywordsPsnQuery;

/**
 * 通过关键词查找相关人员.
 * 
 * @author lqh
 * 
 */
@Service("keywordsQueryPsnService")
@Transactional(rollbackFor = Exception.class)
public class KeywordsQueryPsnServiceImpl implements KeywordsQueryPsnService {


  /**
   * 
   */
  private static final long serialVersionUID = -6194131615414340021L;
  private final Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private KeywordsPsnQueryService keywordsPsnQueryService;

  /**
   * 通过关键词查找人员ID和关键词相同总数，根据权重排序.
   * 
   * @param kwList
   * @return map-key:nodeid,value:psnids
   * @throws ServiceException
   */
  @Override
  public Map<Long, Long> queryKeyPsnAndKeyCount(List<KeywordsPsnQuery> kwList, int size) throws Exception {

    return keywordsPsnQueryService.queryKeyPsnAndKeyCount(kwList, size);
  }

}
