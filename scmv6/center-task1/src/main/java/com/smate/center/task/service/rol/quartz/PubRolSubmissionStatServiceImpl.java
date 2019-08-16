package com.smate.center.task.service.rol.quartz;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.task.dao.rol.quartz.PubRolSubmissionStatDao;
import com.smate.center.task.exception.ServiceException;

@Service("pubRolSubmissionStatService")
@Transactional(rollbackFor = Exception.class)
public class PubRolSubmissionStatServiceImpl implements PubRolSubmissionStatService {
  @Autowired
  private PubRolSubmissionStatDao pubRolSubmissionStatDao;
  private Logger logger = LoggerFactory.getLogger(getClass());

  @Override
  public void refreshPubRolSubmissionStat(Long insId) throws ServiceException {

    try {
      pubRolSubmissionStatDao.refreshPubSubmission(insId);
    } catch (Exception e) {
      logger.error("更新单位成果提交统计数", e);
      throw new ServiceException(e);
    }
  }

  @Override
  public void refreshPubRolSubmissionStat(Long insId, Long psnId) throws ServiceException {
    try {
      pubRolSubmissionStatDao.refreshPubSubmission(insId, psnId);
    } catch (Exception e) {
      logger.error("更新单位人员成果统计数", e);
      throw new ServiceException(e);
    }
  }

}
