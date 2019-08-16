package com.smate.center.task.service.sns.quartz;


public interface TaskMarkerService {

  public Integer getApplicationQuartzSettingValue(String name);

  public void closeQuartzApplication(String name);
}
