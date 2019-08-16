package com.smate.center.task.service.sns.pub;

import com.smate.center.task.model.pdwh.quartz.TmpTaskInfoRecord;

import java.util.List;

public interface SnsDupPubGroupingService {

  public List<TmpTaskInfoRecord> batchGetJobList(Integer size) throws Exception;

  public void startProcessing(TmpTaskInfoRecord job) throws Exception;

  public void updateTaskStatus(Long id, int status, String errMsg);


}
