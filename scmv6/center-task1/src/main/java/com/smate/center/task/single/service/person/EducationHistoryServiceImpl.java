package com.smate.center.task.single.service.person;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.task.exception.ServiceException;
import com.smate.core.base.psn.dao.EducationHistoryDao;
import com.smate.core.base.psn.model.EducationHistory;
import com.smate.core.base.utils.exception.SysServiceException;
import com.smate.core.base.utils.model.Page;

/**
 * @author lichangwen
 * 
 */
@Service("educationHistoryService")
@Transactional(rollbackFor = Exception.class)
public class EducationHistoryServiceImpl implements EducationHistoryService {

  Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private EducationHistoryDao educationHistoryDao;

  /**
   * 获取用户的教育经历.
   * 
   * @param psnId
   * @return
   * @throws ServiceException
   */
  @Override
  public List<EducationHistory> findByPsnId(Long psnId) throws SysServiceException {
    try {
      return educationHistoryDao.findByPsnId(psnId);
    } catch (Exception e) {
      logger.error("传入psnId,insId经人员教育经历表判断，查询人员基本信息出错", e);
      throw new SysServiceException(e);
    }
  }

  /**
   * 判断用户是否存在教育经历.
   * 
   * @param psnId
   * @return
   * @throws ServiceException
   */
  @Override
  public boolean isEduHistoryExit(Long psnId) throws ServiceException {
    try {
      return this.educationHistoryDao.isEduHistoryExit(psnId);
    } catch (Exception e) {
      logger.error("判断用户是否存在教育经历", e);
      throw new ServiceException(e);
    }
  }

  @Override
  public List<EducationHistory> findEduByAutoRecommend(List<Long> psnIds) throws ServiceException {
    return educationHistoryDao.findEduByAutoRecommend(psnIds);
  }

  @Override
  public Page<EducationHistory> findEducationHistory(Long insId, Long psnId, Page<EducationHistory> page)
      throws ServiceException {
    return educationHistoryDao.findEducationHistory(insId, psnId, page);
  }
}
