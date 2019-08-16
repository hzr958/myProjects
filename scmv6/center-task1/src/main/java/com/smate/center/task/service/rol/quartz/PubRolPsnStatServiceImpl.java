package com.smate.center.task.service.rol.quartz;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.task.dao.rol.quartz.PubPsnRolDao;
import com.smate.center.task.dao.rol.quartz.PubRolPsnStatDao;
import com.smate.center.task.exception.ServiceException;

/**
 * 成果指派统计数.
 * 
 * @author liqinghua
 * 
 */

@Service("pubRolPsnStatService")
@Transactional(rollbackFor = Exception.class)
public class PubRolPsnStatServiceImpl implements PubRolPsnStatService {

  /**
   * 
   */
  private static final long serialVersionUID = 1549984685827169654L;
  private Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private PubRolPsnStatDao pubRolPsnStatDao;
  @Autowired
  private PubPsnRolDao pubPsnRolDao;

  @Override
  public void refreshPubRolPsnStat(Long insId) throws ServiceException {
    try {
      pubRolPsnStatDao.refreshPsnStatByIns(insId);
    } catch (Exception e) {
      logger.error("更新单位成果成果指派统计数", e);
      throw new ServiceException(e);
    }

  }

  @Override
  public void refreshPubRolPsnStat(Long insId, Long psnId) {
    try {
      pubRolPsnStatDao.refreshPsnStatByPsn(insId, psnId);
    } catch (Exception e) {
      logger.error("更新单位人员成果指派统计数", e);
      throw new ServiceException(e);
    }
  }

  @Override
  public void refreshPubRolPsnStat(Long insId, List<Long> psnIds) throws ServiceException {
    try {
      for (Long psnId : psnIds)
        pubRolPsnStatDao.refreshPsnStatByPsn(insId, psnId);
    } catch (Exception e) {
      logger.error("更新单位人员成果指派统计数", e);
      throw new ServiceException(e);
    }
  }

  @Override
  public void refreshPubPsnStat(Long insId, Long pubId) throws ServiceException {
    try {
      List<Long> psnIds = pubPsnRolDao.getPubPsnIds(pubId, insId);
      if (psnIds == null || psnIds.size() == 0) {
        return;
      }
      for (Long psnId : psnIds) {
        pubRolPsnStatDao.refreshPsnStatByPsn(insId, psnId);
      }
    } catch (Exception e) {
      logger.error("更新成果人员指派统计数.", e);
      throw new ServiceException(e);
    }
  }

}
