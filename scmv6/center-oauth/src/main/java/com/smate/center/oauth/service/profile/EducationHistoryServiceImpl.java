package com.smate.center.oauth.service.profile;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.oauth.exception.OauthException;
import com.smate.core.base.psn.dao.EducationHistoryDao;

/**
 * 人员教育服务接口实现
 * 
 * @author Administrator
 *
 */
@Service("educationHistoryService")
@Transactional(rollbackFor = Exception.class)
public class EducationHistoryServiceImpl implements EducationHistoryService {

  private final Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private EducationHistoryDao educationHistoryDao;

  /**
   * 判断用户是否存在教育经历
   */
  @Override
  public boolean isEduHistoryExit(Long psnId) throws OauthException {
    try {
      return this.educationHistoryDao.isEduHistoryExit(psnId);
    } catch (OauthException e) {
      logger.error("判断用户是否存在教育经历", e);
      throw new OauthException(e);
    }
  }

}
