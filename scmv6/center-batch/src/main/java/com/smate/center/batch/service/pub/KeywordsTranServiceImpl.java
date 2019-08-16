package com.smate.center.batch.service.pub;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.batch.dao.sns.pub.KeywordsEnTranZhDao;
import com.smate.center.batch.dao.sns.pub.KeywordsZhTranEnDao;
import com.smate.center.batch.exception.pub.ServiceException;
import com.smate.center.batch.model.sns.pub.KeywordsEnTranZh;
import com.smate.center.batch.model.sns.pub.KeywordsZhTranEn;

/**
 * 关键词翻译服务.
 * 
 * @author lqh
 * 
 */
@Service("keywordsTranService")
@Transactional(rollbackFor = Exception.class)
public class KeywordsTranServiceImpl implements KeywordsTranService {

  /**
   * 
   */
  private static final long serialVersionUID = 7497694965140888943L;
  private final Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private KeywordsEnTranZhDao keywordsEnTranZhDao;
  @Autowired
  private KeywordsZhTranEnDao keywordsZhTranEnDao;

  @Override
  public KeywordsEnTranZh findEnTranZhKw(String enKw) throws ServiceException {
    try {
      return keywordsEnTranZhDao.findEnTranZhKw(enKw);
    } catch (Exception e) {
      logger.error("查找英文翻译中文的关键词.enKw=" + enKw, e);
      throw new ServiceException("查找英文翻译中文的关键词.enKw=" + enKw, e);
    }
  }

  @Override
  public KeywordsZhTranEn findZhTranEnKw(String zhKw) throws ServiceException {
    try {
      return keywordsZhTranEnDao.findZhTranEnKw(zhKw);
    } catch (Exception e) {
      logger.error("查找中文翻译英文的关键词.zhKw=" + zhKw, e);
      throw new ServiceException("查找中文翻译英文的关键词.zhKw=" + zhKw, e);
    }
  }

}
