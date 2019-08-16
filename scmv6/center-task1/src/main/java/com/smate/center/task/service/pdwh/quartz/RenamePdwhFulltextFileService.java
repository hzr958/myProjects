package com.smate.center.task.service.pdwh.quartz;

import java.util.List;

import com.smate.center.task.model.pdwh.quartz.TmpTaskInfoRecord;

public interface RenamePdwhFulltextFileService {

  List<TmpTaskInfoRecord> batchGetJobList(Integer size) throws Exception;

  void startProcessing(TmpTaskInfoRecord job) throws Exception;

  void updateTaskStatus(Long jobId, int i, String string);

}
