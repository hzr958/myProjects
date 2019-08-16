package com.smate.center.batch.service.pub;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.batch.dao.sns.pub.GrantRuleDao;
import com.smate.center.batch.exception.pub.DaoException;
import com.smate.center.batch.exception.pub.ServiceException;
import com.smate.center.batch.model.sns.pub.GrantRule;

/**
 * 业务类别申请书成果填报规则
 * 
 * @author scy
 * 
 */
@Transactional(rollbackFor = Exception.class)
@Service("grantRuleService")
public class GrantRuleServiceImpl implements GrantRuleService {

  protected final Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private GrantRuleDao grantRuleDao;

  /**
   * 根据业务ID和年份查找业务规则
   * 
   * @param codeId
   * @return
   * @throws ServiceException
   */
  @Override
  public GrantRule getGrantRule(Long codeId, Integer year) throws ServiceException {
    try {
      return grantRuleDao.getGrantRule(codeId, year);
    } catch (DaoException e) {
      logger.error("根据业务ID和年份查找业务规则出现问题", e);
      throw new ServiceException(e);
    }
  }

  /**
   * 根据业务ID查找业务规则
   * 
   * @param codeId
   * @return
   * @throws ServiceException
   */
  @Override
  public GrantRule getGrantRule(Long codeId) throws ServiceException {
    try {
      return grantRuleDao.getGrantRule(codeId);
    } catch (DaoException e) {
      logger.error("根据业务ID查找业务规则出现问题", e);
      throw new ServiceException(e);
    }
  }

  @Override
  public List<GrantRule> getAll() throws ServiceException {
    try {
      return grantRuleDao.getAll();
    } catch (Exception e) {
      logger.error("查询所有业务规则出现问题", e);
      throw new ServiceException(e);
    }
  }

}
