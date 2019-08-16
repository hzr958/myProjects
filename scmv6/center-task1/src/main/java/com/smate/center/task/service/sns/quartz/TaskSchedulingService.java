package com.smate.center.task.service.sns.quartz;

/**
 * 定时任务调度服务接口
 * 
 * @author LJ
 *
 */

public interface TaskSchedulingService {

  // 获取任务状态 1：执行 ，0：关闭
  public Integer getApplicationQuartzSettingValue(String name);

  // 关闭任务
  public void closeQuartzApplication(String name);

}
