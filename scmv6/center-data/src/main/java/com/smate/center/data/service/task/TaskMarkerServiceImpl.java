package com.smate.center.data.service.task;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.data.dao.quartz.ApplicationQuartzSettingDao;

@Service("taskMarkerService")
@Transactional(rollbackFor = Exception.class)
public class TaskMarkerServiceImpl implements TaskMarkerService {

  @Autowired
  private ApplicationQuartzSettingDao applicationQuartzSettingDao;

  @Override
  public Integer getApplicationQuartzSettingValue(String name) {
    return applicationQuartzSettingDao.getAppValue(name);
  }

  @Override
  public void closeQuartzApplication(String name) {
    applicationQuartzSettingDao.closeApplication(name);
  }

}
