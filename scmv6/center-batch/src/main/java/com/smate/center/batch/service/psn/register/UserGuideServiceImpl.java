package com.smate.center.batch.service.psn.register;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.batch.dao.sns.psn.register.UserGuideConfigDao;
import com.smate.center.batch.exception.pub.DaoException;
import com.smate.center.batch.exception.pub.ServiceException;
import com.smate.center.batch.model.psn.register.UserGuideConfig;

/**
 * 新手指南业务层
 * 
 * @author liangguokeng
 * 
 */

@Service("userGuideService")
@Transactional(rollbackFor = Exception.class)
public class UserGuideServiceImpl implements UserGuideService {

  /**
   * 
   */
  private static final long serialVersionUID = 1595554056162672476L;

  protected final Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private UserGuideConfigDao userGuideConfigDao;

  @Override
  public UserGuideConfig findUserGuideConfig(Long psnId) throws ServiceException {
    try {
      UserGuideConfig config = userGuideConfigDao.findUserGuideConfig(psnId);
      if (config == null) {
        config = new UserGuideConfig(psnId, 1L, 0L);
        userGuideConfigDao.save(config);
      }
      return config;
    } catch (DaoException e) {
      logger.error("查找新手指南配置信息出错psnId=" + psnId, e);
      throw new ServiceException("查找新手指南配置信息出错psnId=" + psnId, e);
    }
  }

  @Override
  public void saveUserGuideConfig(UserGuideConfig config) throws ServiceException {
    try {
      userGuideConfigDao.getSession().saveOrUpdate(config);
    } catch (Exception e) {
      logger.error("保存新手指南配置信息出错", e);
      throw new ServiceException("查找新手指南配置信息出错", e);
    }
  }

}
