package com.smate.center.batch.service.rol.pub;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.smate.center.batch.dao.rol.pub.PubRolPsnStatDao;
import com.smate.center.batch.exception.pub.ServiceException;
import com.smate.center.batch.model.rol.pub.PubRolPsnStat;
import com.smate.core.base.utils.model.Page;

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
  private static final long serialVersionUID = 1966053924385124399L;
  protected final Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private PubRolPsnStatDao pubRolPsnStatDao;

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
  public void refreshPubRolPsnStat(Long insId, Long psnId) throws ServiceException {

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
    // TODO Auto-generated method stub

  }

  @Override
  public Page<PubRolPsnStat> queryAssignMailList(Page<PubRolPsnStat> page, String unitStr, String simpleSearchContent,
      Long unitId, String psnName, String email) throws ServiceException {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public void sendAssignMail(Map<String, String> confMap, List<Long> psnIds) throws ServiceException {
    // TODO Auto-generated method stub

  }

  @Override
  public Map<String, Long> countPsnByUnitId(Long insId, Integer cyFlag, Long unitId) throws ServiceException {
    // TODO Auto-generated method stub
    return null;
  }



}
