package com.smate.center.task.service.sns.psn;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.task.dao.sns.psn.PsnPrjDiscodeDao;
import com.smate.center.task.exception.DaoException;
import com.smate.center.task.exception.ServiceException;


/**
 * 个人申请基金代码.
 * 
 * @author liangguokeng
 * 
 */
@Service("psnPrjDiscodeService")
@Transactional(rollbackFor = Exception.class)
public class PsnPrjDiscodeServiceImpl implements PsnPrjDiscodeService {

  /**
   * 
   */
  private static final long serialVersionUID = -6111956043866608966L;
  protected final Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private PsnPrjDiscodeDao psnPrjDiscodeDao;

  @Override
  public List<String> findPsnDisCodeByPsnId(Long psnId) throws ServiceException {
    try {
      return psnPrjDiscodeDao.findPsnDiscodeByPsnId(psnId);
    } catch (DaoException e) {
      logger.error("查找个人申请代码错误", e);
      throw new ServiceException(e);
    }
  }
}
