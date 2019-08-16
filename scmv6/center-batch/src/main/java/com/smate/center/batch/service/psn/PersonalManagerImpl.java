package com.smate.center.batch.service.psn;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.batch.dao.sns.psn.PsnDisciplineDao;
import com.smate.center.batch.exception.pub.DaoException;
import com.smate.center.batch.exception.pub.ServiceException;

@Service("personalManager")
@Transactional(rollbackFor = Exception.class)
public class PersonalManagerImpl implements PersonalManager {

  /**
   * 
   */
  protected final Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private PsnDisciplineDao psnDisciplineDao;


  /**
   * 获取刷新用户信息完整度的数据.
   * 
   * @param psnId
   * @return
   * @throws ServiceException
   */
  @Override
  public boolean isPsnDiscExit(Long psnId) throws ServiceException {

    try {
      return psnDisciplineDao.isPsnDiscExit(psnId);
    } catch (DaoException e) {
      logger.error("获取刷新用户信息完整度的数据", e);
      throw new ServiceException(e);
    }
  }

}
