package com.smate.sie.center.task.service;

import com.smate.core.base.exception.ServiceException;
import com.smate.sie.center.task.model.ImportThirdUnits;

/**
 * 第三方部门信息处理历史记录接口.
 * 
 * @author xys
 *
 */
public interface ImportThirdUnitsHistoryService {

  /**
   * 保存第三方部门信息处理历史记录.
   * 
   * @param importThirdUnits
   * @throws ServiceException
   */
  public void saveImportThirdUnitsHistory(ImportThirdUnits importThirdUnits) throws ServiceException;
}
