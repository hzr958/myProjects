package com.smate.sie.center.task.service;

import com.smate.core.base.exception.ServiceException;
import com.smate.sie.center.task.model.ImportThirdPsns;

/**
 * 第三方人员信息处理历史记录接口.
 * 
 * @author xys
 *
 */
public interface ImportThirdPsnsHistoryService {

  /**
   * 保存第三方人员信息处理历史记录.
   * 
   * @param importThirdPsns
   * @throws ServiceException
   */
  public void saveImportThirdPsnsHistory(ImportThirdPsns importThirdPsns) throws ServiceException;
}
