package com.smate.center.batch.service.rol.pub;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.batch.exception.pub.ServiceException;
import com.smate.center.batch.service.pub.EntityManagerImpl;
import com.smate.core.base.utils.dao.security.InsPortalDao;
import com.smate.core.base.utils.data.HibernateDao;
import com.smate.core.base.utils.model.InsPortal;

/**
 * @author lqh
 * 
 */
@Service("insPortalManager")
@Transactional(rollbackFor = Exception.class)
public class InsPortalManagerImpl extends EntityManagerImpl<InsPortal, Long> implements InsPortalManager {

  @Autowired
  private InsPortalDao insPortalDao;


  @Override
  protected HibernateDao<InsPortal, Long> getEntityDao() {
    return insPortalDao;
  }

  @Override
  public Integer getInsNodeId(Long insId) throws ServiceException {
    try {

      return this.insPortalDao.getInsNode(insId);
    } catch (Exception e) {
      logger.error("获取单位节点错误 : ", e);
      throw new ServiceException(e);
    }
  }

  @Override
  public InsPortal getInsPortalByInsId(Long insId) throws ServiceException {

    return insPortalDao.get(insId);
  }
}
