package com.smate.center.task.service.rol.quartz;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.task.dao.rol.quartz.InstitutionRolDao;
import com.smate.center.task.exception.ServiceException;
import com.smate.center.task.model.rol.quartz.InstitutionRol;

/**
 * 
 * @author zjh
 * 
 */
@Service("institutionRolService")
@Transactional(rollbackFor = Exception.class)
public class InstitutionRolServiceImpl implements InstitutionRolService {
  Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private InstitutionRolDao institutionRolDao;

  /*
   * (non-Javadoc)
   * 
   * @see com.iris.scm.scmweb.service.profile.InstitutionManager#getInstitution (java.lang.Long)
   */
  @Override
  public InstitutionRol getInstitution(Long insId) throws ServiceException {
    try {

      return institutionRolDao.findById(insId);

    } catch (Exception e) {
      logger.error("getInstitution取单位列表出错:", e);
      throw new ServiceException(e);
    }
  }

}
