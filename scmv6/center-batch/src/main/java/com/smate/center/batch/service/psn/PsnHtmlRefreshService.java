package com.smate.center.batch.service.psn;

import com.smate.center.batch.exception.pub.ServiceException;


public interface PsnHtmlRefreshService {

  void updatePsnHtmlRefresh(Long psnId) throws ServiceException;
}
