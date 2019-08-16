package com.smate.sie.center.task.service;

import java.util.List;

import com.smate.core.base.exception.ServiceException;
import com.smate.sie.center.task.model.ImportThirdUnits;

/**
 * 第三方部门信息接口.
 * 
 * @author xys
 *
 */
public interface ImportThirdUnitsService {

  /**
   * 获取待导入数据.
   * 
   * @param maxSize
   * @return
   * @throws ServiceException
   */
  public List<ImportThirdUnits> getThirdUnitsNeedImport(int maxSize) throws ServiceException;

  /**
   * 保存第三方部门信息.
   * 
   * @param importThirdUnits
   * @throws ServiceException
   */
  public void saveImportThirdUnits(ImportThirdUnits importThirdUnits) throws ServiceException;
}
