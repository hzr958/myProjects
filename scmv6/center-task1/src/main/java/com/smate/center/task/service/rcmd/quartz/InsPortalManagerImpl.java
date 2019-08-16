package com.smate.center.task.service.rcmd.quartz;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.task.dao.rcmd.quartz.InsPortalRcmdDao;
import com.smate.center.task.exception.ServiceException;
import com.smate.center.task.model.rcmd.quartz.InsPortalRcmd;

@Service("insPortalManager")
@Transactional(rollbackFor = Exception.class)
public class InsPortalManagerImpl implements InsPortalManager {
  @Autowired
  private InsPortalRcmdDao insportalRcmdDao;

  @Override
  public InsPortalRcmd getInsPortalByInsId(Long insId) throws ServiceException {
    return insportalRcmdDao.get(insId);
  }

}
