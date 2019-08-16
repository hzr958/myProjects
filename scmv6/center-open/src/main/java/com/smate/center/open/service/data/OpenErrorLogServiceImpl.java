package com.smate.center.open.service.data;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.open.dao.login.OpenErrorLogDao;
import com.smate.center.open.model.OpenErrorLog;

@Service("openErrorLogService")
@Transactional(rollbackFor = Exception.class)
public class OpenErrorLogServiceImpl implements OpenErrorLogService {

  @Autowired
  private OpenErrorLogDao openErrorLogDao;

  @Override
  public void saveOpenErrorLog(OpenErrorLog openErrorLog) {
    openErrorLogDao.saveOpenErrorLog(openErrorLog);
  }

}
