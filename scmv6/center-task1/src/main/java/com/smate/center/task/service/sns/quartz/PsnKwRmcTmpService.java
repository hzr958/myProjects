package com.smate.center.task.service.sns.quartz;

import com.smate.center.task.exception.ServiceException;

public interface PsnKwRmcTmpService {
  public void HandlePsnKwRmcTmp(Long psnId) throws ServiceException;

}
