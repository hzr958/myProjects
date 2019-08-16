package com.smate.center.batch.connector.factory;

import java.util.Date;

import com.smate.center.batch.connector.enums.BatchOpenCodeEnum;
import com.smate.center.batch.connector.model.job.BatchJobs;
import com.smate.center.batch.connector.util.BatchJobUtil;

/**
 * 普通BatchJobs构造器
 *
 * @author LXZ
 * 
 * @since 6.0.1
 * @version 6.0.1
 */
public class BatchJobsNormalFactory extends BatchJobsFactory {


  protected BatchJobs getBatchJob(BatchOpenCodeEnum em, String objId, String Weight) {
    String context = BatchJobUtil.getContext(objId);
    String strategy = em.toString();
    BatchJobs job = new BatchJobs(context, Weight, strategy);
    job.setCreateTime(new Date());
    return job;
  }
}
