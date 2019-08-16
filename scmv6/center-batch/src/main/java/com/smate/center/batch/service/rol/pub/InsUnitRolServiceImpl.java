package com.smate.center.batch.service.rol.pub;

import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.batch.dao.rol.pub.InsUnitDao;
import com.smate.center.batch.exception.pub.DaoException;
import com.smate.center.batch.exception.pub.ServiceException;
import com.smate.center.batch.model.rol.pub.InsUnit;
import com.smate.core.base.utils.security.TheadLocalInsId;

/**
 * 部门管理.
 * 
 * @author liqinghua
 * 
 */
@Service("insUnitRolService")
@Transactional(rollbackFor = Exception.class)
public class InsUnitRolServiceImpl implements InsUnitRolService {

  private final Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private InsUnitDao insUnitDao;

  @Override
  public String getInsUnitName(Long unitId) throws ServiceException {
    Long insId = TheadLocalInsId.getInsId();
    Locale locale = LocaleContextHolder.getLocale();
    try {
      return insUnitDao.getUnitName(unitId, insId, locale);
    } catch (DaoException e) {
      logger.error("获取部门名出错,单位Id:" + insId, e);
      return "";
    }
  }

  /**
   * 获取单个部门实体对象.
   * 
   * @param id
   * @return
   * @throws ServiceException
   */
  @Override
  public InsUnit getInsUnitRolById(Long id) throws ServiceException {
    try {
      return insUnitDao.getInsUnitRolById(id);
    } catch (DaoException e) {
      logger.error("getInsUnitRolById  获取单个部门实体对象id:" + id, e);
      return null;
    }
  }
}
