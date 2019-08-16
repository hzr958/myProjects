package com.smate.center.task.service.rol.quartz;

import com.smate.center.task.exception.ServiceException;

public interface KpiRefreshPubService {

  void addPubRefresh(Long pubId, boolean isDel) throws ServiceException;

}
