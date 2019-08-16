package com.smate.center.task.service.rol.quartz;

import com.smate.center.task.exception.ServiceException;

/**
 * 用户确认成果，完善用户信息service.
 * 
 * @author liqinghua
 * 
 */
public interface PsnConfirmPmService {
  /**
   * 用户确认成果，完善PM数据.
   * 
   * @param pubId
   * @param psnId
   * @param pmId
   * @throws ServiceException
   */
  public void psnConfirmPm(Long pubId, Long psnId, Long pmId) throws ServiceException;

}
