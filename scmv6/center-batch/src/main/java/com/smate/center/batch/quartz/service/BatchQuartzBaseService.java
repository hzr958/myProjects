package com.smate.center.batch.quartz.service;

import java.util.Date;
import java.util.List;

import com.smate.center.batch.connector.model.job.BatchQuartz;
import com.smate.core.base.utils.exception.BatchTaskException;


public interface BatchQuartzBaseService {

  public Integer getTaskStatus(Long taskId);

  public List<BatchQuartz> getTasksByStatus(Integer taskStatus);

  public void updateJobError(Long jobId, String errorMsg, Date startTime) throws BatchTaskException;

  public void updateJobSuccess(Long jobId, Date startTime) throws BatchTaskException;

  public void saveQuartzJob(BatchQuartz task);

  public void execute(BatchQuartz task) throws BatchTaskException;
}
