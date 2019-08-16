package com.smate.center.batch.service.rol.pub;

import com.smate.center.batch.exception.pub.ServiceException;
import com.smate.center.batch.model.rol.pub.InsUnit;

/**
 * 部门管理.
 * 
 * @author liqinghua
 * 
 */
public interface InsUnitRolService {

  /**
   * 根据ID获取部门名.
   * 
   * @param unitId
   * @return
   * @throws ServiceException
   */
  String getInsUnitName(Long unitId) throws ServiceException;

  /**
   * 获取单个部门实体对象.
   * 
   * @param id
   * @return
   * @throws ServiceException
   */
  InsUnit getInsUnitRolById(Long id) throws ServiceException;
}
