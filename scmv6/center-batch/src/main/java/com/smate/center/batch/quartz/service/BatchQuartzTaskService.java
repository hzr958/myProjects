package com.smate.center.batch.quartz.service;

import com.smate.center.batch.connector.model.job.BatchQuartz;
import com.smate.core.base.utils.exception.BatchTaskException;

public interface BatchQuartzTaskService {

  public void taskExecute(BatchQuartz task) throws BatchTaskException;

}
