package com.smate.center.batch.jobdetail.basejob;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.core.job.flow.FlowExecutionStatus;
import org.springframework.batch.core.job.flow.JobExecutionDecider;

import com.smate.center.batch.constant.BatchConfConstant;

/**
 * BatchJobs分配者，决定读取的信息应该用那种任务来执行
 * 
 * @author hzr
 * @version 6.0.1
 * 
 */
public class JobDecider implements JobExecutionDecider {

  protected Logger logger = LoggerFactory.getLogger(getClass());

  @Override
  public FlowExecutionStatus decide(JobExecution jobExecution, StepExecution stepExecution) {

    String strategy = stepExecution.getJobExecution().getJobParameters().getString(BatchConfConstant.JOB_STRATEGY);
    Long jobInstanceId = stepExecution.getJobExecution().getJobInstance().getInstanceId();

    if (StringUtils.isNotBlank(strategy)) {

      return new FlowExecutionStatus(strategy);
    } else {
      logger.error("SpringBatch 获取任务名为空！JobInstanceId = ", jobInstanceId);
      return new FlowExecutionStatus("null");
    }
  }



}
