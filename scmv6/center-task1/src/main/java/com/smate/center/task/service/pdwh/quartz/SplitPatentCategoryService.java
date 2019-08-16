package com.smate.center.task.service.pdwh.quartz;

import java.util.List;

import com.smate.center.task.model.pdwh.quartz.TmpTaskInfoRecord;

public interface SplitPatentCategoryService {

  public List<TmpTaskInfoRecord> batchGetJobList(Integer size) throws Exception;

  public void startSplitInfo(TmpTaskInfoRecord job) throws Exception;

  public void updateTaskStatus(Long jobId, int i, String string);

}
