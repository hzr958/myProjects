package com.smate.center.data.service.task;

public interface TaskMarkerService {

  public Integer getApplicationQuartzSettingValue(String name);

  public void closeQuartzApplication(String name);

}
