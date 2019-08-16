package com.smate.center.task.service.sns.psn;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.task.dao.sns.psn.PsnPrjGrantDao;
import com.smate.center.task.exception.DaoException;
import com.smate.center.task.exception.ServiceException;
import com.smate.center.task.model.sns.psn.PsnPrjGrant;


/**
 * 
 * 
 * @author liangguokeng
 * 
 */
@Service("psnPrjGrantService")
@Transactional(rollbackFor = Exception.class)
public class PsnPrjGrantServiceImpl implements PsnPrjGrantService {
  /**
   * 
   */
  private static final long serialVersionUID = 7609087859629988610L;
  private final Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private PsnPrjGrantDao psnPrjGrantDao;

  @Override
  public List<PsnPrjGrant> findPsnPrjGrant(Long psnId, int prjRole) throws ServiceException {
    try {
      return psnPrjGrantDao.findPsnPrjGrant(psnId, prjRole);
    } catch (DaoException e) {
      logger.error("查找个人获得基金列表出错", e);
      throw new ServiceException(e);
    }
  }
}
