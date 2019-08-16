package com.smate.center.batch.connector.model.job;

import java.io.Serializable;


public class BatchJobsForTaskPool implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = -3452316977586407198L;

  private BatchJobs batchJobs;
  private Integer taskPoolStatus;

  public BatchJobsForTaskPool() {
    super();
  }

  public BatchJobsForTaskPool(BatchJobs batchJobs, Integer taskPoolStatus) {
    super();
    this.batchJobs = batchJobs;
    this.taskPoolStatus = taskPoolStatus;
  }

  public BatchJobs getBatchJobs() {
    return batchJobs;
  }

  public void setBatchJobs(BatchJobs batchJobs) {
    this.batchJobs = batchJobs;
  }

  public Integer getTaskPoolStatus() {
    return taskPoolStatus;
  }

  public void setTaskPoolStatus(Integer taskPoolStatus) {
    this.taskPoolStatus = taskPoolStatus;
  }



}
