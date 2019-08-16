package com.smate.center.task.service.sns.quartz;

import java.util.List;

import com.smate.center.task.model.pdwh.quartz.TmpTaskInfoRecord;
import com.smate.center.task.model.sns.quartz.VKeywordsSynonym;

public interface GenerateIKAnalyerDictService {
  /**
   * 从TmpTaskInfoRecord获取需要处理的数据
   * 
   * @param size
   * @return
   * @throws Exception
   */
  public List<TmpTaskInfoRecord> batchGetJobList(Integer size) throws Exception;

  /**
   * 开始处理单个任务
   * 
   * @param job
   * @throws Exception
   */
  public void startJob(TmpTaskInfoRecord job) throws Exception;

  /**
   * 根据任务记录Id修改任务状态
   * 
   * @param jobId
   * @param status
   * @param errMsg
   */
  public void updateTaskStatus(Long jobId, int status, String errMsg);

  public List<VKeywordsSynonym> GetVKeywordsSynonymList(int index, int batchsize);

  public void writeToDicFile(String datesuffix) throws Exception;

}
