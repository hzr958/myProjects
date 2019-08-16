package com.smate.web.management.service.analysis;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.web.management.dao.analysis.sns.KeywordsPsnQueryDao;
import com.smate.web.management.model.analysis.sns.KeywordsPsnQuery;

/**
 * 用于通过关键词过滤用户，注意，该service仅供KeywordsPsnQueryService使用.
 * 
 * @author lqh
 * 
 */
@Service("keywordsPsnQueryDoService")
@Transactional(rollbackFor = Exception.class)
public class KeywordsPsnQueryDoServiceImpl implements KeywordsPsnQueryDoService {

  /**
   * 
   */
  private static final long serialVersionUID = -3469341575061825177L;
  protected final Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private KeywordsPsnQueryDao keywordsPsnQueryDao;

  @Override
  public Long saveKeywordsQueryInfo(List<KeywordsPsnQuery> kwList) throws Exception {

    try {
      Long qid = keywordsPsnQueryDao.getQueryId();
      Date now = new Date();
      for (KeywordsPsnQuery kw : kwList) {
        // Long[] hashes =
        // PubHashUtils.getKeywordUnitHash(kw.getKeyword());

        kw.setQat(now);
        if (kw.getWeight() == null) {
          kw.setWeight(1D);
        }
        kw.setQid(qid);
        keywordsPsnQueryDao.save(kw);
      }
      return qid;
    } catch (Exception e) {
      logger.error("保存关键词信息到数据库", e);
      throw new Exception("保存关键词信息到数据库", e);
    }
  }

  @Override
  public List<Long> queryKeywordsPsn(Long queryId, int size) throws Exception {

    try {
      return this.keywordsPsnQueryDao.queryKeywordsPsn(queryId, size);
    } catch (Exception e) {
      logger.error("通过关键词查询id查询相关人员", e);
      throw new Exception("通过关键词查询id查询相关人员", e);
    }
  }

  @Override
  public Map<Long, Long> queryKeyPsnAndKeyCount(Long queryId, int size) throws Exception {
    try {
      return this.keywordsPsnQueryDao.queryKeyPsnAndKeyCount(queryId, size);
    } catch (Exception e) {
      logger.error("通过关键词查询id查询相关人员及关键词相同总数", e);
      throw new Exception("通过关键词查询id查询相关人员及关键词相同总数", e);
    }
  }

}
