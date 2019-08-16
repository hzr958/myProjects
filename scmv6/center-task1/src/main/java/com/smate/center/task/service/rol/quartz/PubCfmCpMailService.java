package com.smate.center.task.service.rol.quartz;

import com.smate.center.task.exception.ServiceException;

public interface PubCfmCpMailService {
  /**
   * 标记成果认领后通知合作者标记.
   * 
   * @param psnId
   * @param pubId
   * @param insId
   * @throws ServiceException
   */
  public void markPubCfmCpMailStat(Long psnId, Long pubId, Long insId) throws ServiceException;

}
