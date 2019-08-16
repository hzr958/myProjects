package com.smate.sie.center.task.service;

import com.smate.core.base.exception.ServiceException;
import com.smate.sie.center.task.model.ImportThirdUnits;

/**
 * 单位部门服务接口.
 * 
 * @author xys
 *
 */
public interface Sie6InsUnitService {

  /**
   * 刷新单位部门信息.
   * 
   * @param importThirdUnits
   * @throws ServiceException
   */
  public void refreshInsUnit(ImportThirdUnits importThirdUnits) throws ServiceException;
}
