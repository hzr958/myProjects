package com.smate.center.batch.service.rol.pub;

import com.smate.center.batch.exception.pub.ServiceException;

public interface SiePsnHtmlService {

  public void saveToRefreshTask(Long psnId) throws ServiceException;
}
