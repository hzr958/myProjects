package com.smate.center.task.service.rcmd.quartz;

/**
 * 动态推荐人员服务.
 * 
 * @author zjh
 *
 */
public interface DynRecomPsnTaskService {

  void saveDynRePsnTask(Long psnId, int dynReType);

}
