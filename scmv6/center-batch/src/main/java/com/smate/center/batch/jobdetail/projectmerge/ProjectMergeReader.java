package com.smate.center.batch.jobdetail.projectmerge;

import java.util.Date;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.beans.factory.annotation.Autowired;

import com.smate.center.batch.connector.model.job.BatchJobs;
import com.smate.center.batch.connector.service.job.BatchJobsService;
import com.smate.center.batch.constant.BatchConfConstant;
import com.smate.center.batch.model.sns.prj.OpenProject;
import com.smate.center.batch.service.BatchTaskPool;
import com.smate.center.batch.service.projectmerge.OpenProjectService;
import com.smate.core.base.utils.exception.BatchTaskException;
import com.smate.core.base.utils.json.JacksonUtils;

public class ProjectMergeReader implements ItemReader<OpenProject> {

  private JobExecution jobExecution;

  @Autowired
  OpenProjectService openProjectService;

  @Autowired
  BatchJobsService taskService;

  @Autowired
  BatchTaskPool batchTaskPool;

  protected Logger logger = LoggerFactory.getLogger(getClass());

  @BeforeStep
  public void beforeStep(StepExecution stepExecution) {

    jobExecution = stepExecution.getJobExecution();
  }

  @Override
  public OpenProject read()
      throws BatchTaskException, UnexpectedInputException, ParseException, NonTransientResourceException {

    Date startTime = new Date();
    Thread current = Thread.currentThread();
    String jobContent = jobExecution.getJobParameters().getString(BatchConfConstant.JOB_CONTENT);
    Long jobId = jobExecution.getJobParameters().getLong(BatchConfConstant.JOB_ID);
    Long jobInstanceId = jobExecution.getJobInstance().getId();
    Long threadId = current.getId();

    Map jobContentMap = JacksonUtils.jsonToMap(jobContent);
    // msg_id直接读出来为Integer，转换为String，然后再转换为Long
    Long id = Long.parseLong(String.valueOf(jobContentMap.get("msg_id")));

    if (id == null || id == 0L) {
      String error = "未获取到V_OPEN_PROJECT表中id, jobId: " + jobId;
      logger.error(error);
      // 设置错误状态3
      batchTaskPool.remove(id);
      taskService.updateEndThreadOfJobError(jobId, threadId, error);
      return null;
    }

    try {
      taskService.updateStartThreadOfJob(jobId, threadId, jobInstanceId, startTime);
      OpenProject openProject = openProjectService.queryOpenProject(id);

      if (openProject == null) {
        logger.error("未获取到项目合并任务ProjectMergeJob相关数据, jobId: " + jobId);
        BatchJobs jobDup = taskService.getJobWithStatus1ByJobId(jobId);
        if (jobDup != null) {
          String errorMsg = "V_BATCH_JOBS表中插入重复任务，请检查！jobId：" + jobId;
          taskService.updateEndThreadOfJobError(jobId, threadId, errorMsg);
        }
        batchTaskPool.remove(id);
        return null;
      }
      return openProject;
    } catch (Exception e) {
      // 更新出错状态
      logger.error("ProjectMerge任务运行错误：", e);
      batchTaskPool.remove(id);
      taskService.updateEndThreadOfJobError(jobId, threadId, e.getMessage());
      return null;
    }
  }

}
