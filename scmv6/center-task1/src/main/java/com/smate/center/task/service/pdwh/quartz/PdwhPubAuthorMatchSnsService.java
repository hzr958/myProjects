package com.smate.center.task.service.pdwh.quartz;

import java.util.List;

import com.smate.center.task.model.pdwh.quartz.TmpTaskInfoRecord;

public interface PdwhPubAuthorMatchSnsService {

  public void startMatching(TmpTaskInfoRecord info) throws Exception;

  public void updateTaskStatus(Long pdwhPubId, int status, String errMsg);

  public List<TmpTaskInfoRecord> batchGetJobList(Integer size) throws Exception;
}
