package com.smate.center.task.service.sns.quartz;

import java.util.List;

import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.task.dao.sns.quartz.KeywordsCommendTaskDao;
import com.smate.center.task.dao.sns.quartz.PsnKwEptRefreshDao;
import com.smate.center.task.exception.ServiceException;

@Service("psnKwEptSevice")
@Transactional(rollbackFor = Exception.class)
public class PsnKwEptSeviceImpl implements PsnKwEptSevice {
  Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private KeywordsCommendTaskDao keywordsCommendTaskDao;
  @Autowired
  private PsnKwEptRefreshDao psnKwEptRefreshDao;

  @Override
  public int getKeyWordsCommendFlag() throws ServiceException {
    return keywordsCommendTaskDao.getKeyWordsCommendFlag();
  }


  @Override
  public List<Long> getRefreshPsn(Long StartId) throws ServiceException {
    try {
      return psnKwEptRefreshDao.getRefreshPsn(StartId);
    } catch (Exception e) {
      logger.error("获取psn_kw_ept_refresh人员关键词刷新纪录表中的数据时出错", e);
      throw new ServiceException(e);
    }
  }

  @Override
  public void updateFlag() throws ServiceException {
    keywordsCommendTaskDao.updateFlag();
  }

}
