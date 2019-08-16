package com.smate.center.batch.connector.factory;

import java.util.Date;

import com.smate.center.batch.connector.enums.BatchOpenCodeEnum;
import com.smate.center.batch.connector.model.job.BatchJobs;

/**
 * 直接传入Context的BatchJobs构造器
 *
 * @author LXZ
 * 
 * @since 6.0.1
 * @version 6.0.1
 */
public class BatchJobsContextFactory extends BatchJobsFactory {


  /**
   * 传入构造好的context
   */
  protected BatchJobs getBatchJob(BatchOpenCodeEnum em, String context, String Weight) {
    String strategy = em.toString();
    BatchJobs job = new BatchJobs(context, Weight, strategy);
    job.setCreateTime(new Date());
    return job;
  }
}
