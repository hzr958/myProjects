package com.smate.center.task.service.rol.quartz;

import com.smate.center.task.exception.ServiceException;

public interface PubInsSyncRolService {
  /**
   * 更新同步记录的提交状态，过滤条件.
   * 
   * @param snsPubId
   * @param insId
   * @param flag
   * @throws ServiceException
   */
  void updateSnsPubSubmittedFlag(Long snsPubId, Long insId, boolean flag) throws ServiceException;

}
