package com.smate.center.batch.jobdetail.projectmerge;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;

import com.smate.center.batch.connector.service.job.BatchJobsService;
import com.smate.center.batch.constant.BatchConfConstant;
import com.smate.center.batch.model.sns.prj.OpenProject;
import com.smate.center.batch.model.sns.wechat.WeChatPreProcessPsn;
import com.smate.center.batch.service.BatchTaskPool;
import com.smate.center.batch.service.WeChatMsgPsnService;
import com.smate.center.batch.service.projectmerge.OpenProjectProcessorService;
import com.smate.core.base.utils.constant.wechat.WeChatConstant;
import com.smate.core.base.utils.exception.BatchTaskException;

public class ProjectMergeWriter implements ItemWriter<OpenProject> {

  private final Logger logger = LoggerFactory.getLogger(getClass());

  private JobExecution jobExecution;

  @Autowired
  BatchJobsService taskService;

  @Autowired
  WeChatMsgPsnService weChatMsgPsnService;

  @Autowired
  OpenProjectProcessorService openProjectProcessorService;

  @Autowired
  private BatchTaskPool batchTaskPool;

  @BeforeStep
  public void beforeStep(StepExecution stepExecution) {

    jobExecution = stepExecution.getJobExecution();
  }


  @Override
  public void write(List<? extends OpenProject> items) throws BatchTaskException {
    Thread current = Thread.currentThread();
    Long jobId = jobExecution.getJobParameters().getLong(BatchConfConstant.JOB_ID);
    Long threadId = current.getId();
    OpenProject item = items.get(0);
    String errorMsg = "";
    Integer result;
    try {
      result = openProjectProcessorService.run(item);
    } catch (Exception e) {
      result = BatchConfConstant.JOB_ERROR;
      errorMsg = e.getMessage();
    }

    if (result == BatchConfConstant.JOB_SUCCESS) {
      // 更新状态，并把成功发送的消息和任务写入历史表
      batchTaskPool.remove(jobId);
      taskService.updateEndThreadOfJobSuccess(jobId, threadId);
    } else {
      batchTaskPool.remove(jobId);
      taskService.updateEndThreadOfJobError(jobId, threadId, errorMsg);
      logger.error("ProjectMerge任务发生错误, jobId = " + jobId + "; errorMsg= " + errorMsg);
    }
  }
}
