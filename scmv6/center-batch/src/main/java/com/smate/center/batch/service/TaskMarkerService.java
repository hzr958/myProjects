package com.smate.center.batch.service;

import java.util.List;

import com.smate.center.batch.connector.model.job.BatchJobs;
import com.smate.core.base.utils.exception.BatchTaskException;

/**
 * BatchJobs调度Service
 * 
 * @author hzr
 * 
 * @since 6.0.1
 * @version 6.0.1
 * @param job
 */
public interface TaskMarkerService {

  // 根据status的获取taskMsgId数
  public Long getTaskMsgIdCountByStatus(Integer status) throws BatchTaskException;


  // 获取指定数量的不同权重为的taskMsgList
  public List<BatchJobs> getTaskMsgIdListByWeightAndNum(Integer num, String weight) throws BatchTaskException;

  // 获取不同权重为的taskMsgList
  public List<BatchJobs> getTaskMsgIdListByWeight(String weight) throws BatchTaskException;

  // 获取不同权重为的taskMsgList
  public Long getTaskMsgIdListCountByWeight(String weight) throws BatchTaskException;

  // 重新分配各权重任务数
  public List<BatchJobs> arrangeTask(Integer taskSize) throws BatchTaskException;

  // 更新任务
  public void updateTaskStatus(BatchJobs itemList) throws BatchTaskException;

  // 取得表中状态为1的数据
  public List<BatchJobs> getTaskListStatus1() throws BatchTaskException;

  public Integer getApplicationQuartzSettingValue(String name);

  public void closeQuartzApplication(String name);
}
