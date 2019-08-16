package com.smate.center.batch.service.group;

import com.smate.center.batch.exception.pub.ServiceException;
import com.smate.center.batch.model.sns.psn.FriendGroupOperateTaskInfo;

/**
 * 任务信息处理接口
 * 
 * @author zzx
 *
 */
public interface TaskInfoService {
  /**
   * 查询数据是否已存在
   * 
   * @param taskInfo
   * @return
   * @throws ServiceException
   */
  public boolean isDataExist(FriendGroupOperateTaskInfo taskInfo) throws Exception;

  /**
   * 添加任务数据
   * 
   * @param taskInfo
   * @throws ServiceException
   */
  public void addTaskInfo(FriendGroupOperateTaskInfo taskInfo) throws Exception;
}
