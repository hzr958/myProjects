package com.smate.center.task.service.pdwh.quartz;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.task.dao.pdwh.quartz.PubAllkeywordDupDao;
import com.smate.center.task.exception.DaoException;
import com.smate.center.task.exception.ServiceException;
import com.smate.center.task.model.pdwh.quartz.PubAllkeywordDup;

/**
 * 关键词词频统计
 * 
 * @author warrior
 * 
 */
@Service("pubAllkeywordDupService")
@Transactional(rollbackFor = Exception.class)
public class PubAllkeywordDupServiceImpl implements PubAllkeywordDupService {

  protected final Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private PubAllkeywordDupDao pubAllkeywordDupDao;

  @Override
  public void updateTf(Long keywordHash) throws ServiceException {
    PubAllkeywordDup pubAllkeywordDup;
    try {
      pubAllkeywordDup = this.pubAllkeywordDupDao.findKeywordTf(keywordHash);
      if (pubAllkeywordDup == null) {
        pubAllkeywordDup = new PubAllkeywordDup();
        pubAllkeywordDup.setKeywordHash(keywordHash);
        pubAllkeywordDup.setCount(1l);
      } else {
        pubAllkeywordDup.setCount(pubAllkeywordDup.getCount() + 1l);
      }
      this.pubAllkeywordDupDao.save(pubAllkeywordDup);
    } catch (DaoException e) {
      logger.error("更新关键词频数出现异常，keywordHash=" + keywordHash, e);
      throw new ServiceException(e);
    }

  }
}
