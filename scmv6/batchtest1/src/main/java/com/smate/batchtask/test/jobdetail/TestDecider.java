package com.smate.batchtask.test.jobdetail;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.core.job.flow.FlowExecutionStatus;
import org.springframework.batch.core.job.flow.JobExecutionDecider;


public class TestDecider implements JobExecutionDecider{

/*	private JobExecution jobExecution;
	
	@BeforeStep
	public void beforeStep(StepExecution stepExecution){
		
		jobExecution = stepExecution.getJobExecution();
	}*/
	protected Logger logger = LoggerFactory.getLogger(getClass());
	
	@Override
	public FlowExecutionStatus decide(JobExecution jobExecution, StepExecution stepExecution){
		
		String jobCategory = stepExecution.getJobExecution().getJobParameters().getString("jobCategory");//.getExecutionContext().getString("jobCategory");
		Long jobInstanceId = stepExecution.getJobExecution().getJobInstance().getInstanceId();
		
		if(StringUtils.isNotBlank(jobCategory)){
			
			return new FlowExecutionStatus(jobCategory);
		}else{
			logger.error("SpringBatch 获取任务名为空！JobInstanceId = ", jobInstanceId);
			return new FlowExecutionStatus("null");
		}
	}
	
	
	
}