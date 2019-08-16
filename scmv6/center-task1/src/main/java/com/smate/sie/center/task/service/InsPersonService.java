package com.smate.sie.center.task.service;

import com.smate.core.base.exception.ServiceException;
import com.smate.sie.center.task.model.ImportThirdPsns;

/**
 * 单位人员服务接口.
 * 
 * @author xys
 *
 */
public interface InsPersonService {

  /**
   * 刷新单位人员信息.
   * 
   * @param importThirdPsns
   * @throws ServiceException
   */
  public void refreshInsPsn(ImportThirdPsns importThirdPsns) throws ServiceException;
}
