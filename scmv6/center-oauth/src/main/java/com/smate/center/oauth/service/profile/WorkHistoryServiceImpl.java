package com.smate.center.oauth.service.profile;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.oauth.exception.OauthException;
import com.smate.core.base.psn.dao.WorkHistoryDao;

/**
 * 人员工作经历服务接口实现
 * 
 * @author Administrator
 *
 */
@Service("workHistoryService")
@Transactional(rollbackFor = Exception.class)
public class WorkHistoryServiceImpl implements WorkHistoryService {

  private final Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private WorkHistoryDao workHistoryDao;

  /**
   * 判断用户是否存在教育经历.
   * 
   * @param psnId
   * @return
   * @throws ServiceException
   */
  @Override
  public boolean isWorkHistoryExit(Long psnId) throws OauthException {

    try {
      return this.workHistoryDao.isWorkHistoryExit(psnId);
    } catch (OauthException e) {
      logger.error("判断用户是否存在教育经历", e);
      throw new OauthException(e);
    }
  }
}
