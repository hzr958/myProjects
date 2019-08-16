package com.smate.center.batch.service.group;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.batch.dao.sns.pub.TaskInfoDao;
import com.smate.center.batch.model.sns.psn.FriendGroupOperateTaskInfo;

/**
 * 任务信息处理实现类
 * 
 * @author zzx
 *
 */
@Service(value = "taskInfoService")
@Transactional(rollbackFor = Exception.class)
public class TaskInfoServiceImpl implements TaskInfoService {
  @Autowired
  private TaskInfoDao taskInfoDao;

  @Override
  public boolean isDataExist(FriendGroupOperateTaskInfo taskInfo) throws Exception {
    return taskInfoDao.isDataExist(taskInfo);
  }

  @Override
  public void addTaskInfo(FriendGroupOperateTaskInfo taskInfo) throws Exception {
    taskInfoDao.save(taskInfo);
  }

}
