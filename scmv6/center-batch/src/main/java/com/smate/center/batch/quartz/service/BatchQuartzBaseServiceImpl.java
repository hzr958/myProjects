package com.smate.center.batch.quartz.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.batch.connector.dao.job.BatchQuartzDao;
import com.smate.center.batch.connector.model.job.BatchQuartz;
import com.smate.center.batch.service.WeChatMsgMapBuildService;
import com.smate.core.base.utils.exception.BatchTaskException;

@Transactional(rollbackFor = Exception.class)
public class BatchQuartzBaseServiceImpl implements BatchQuartzBaseService {

  private Map<String, BatchQuartzTaskService> quartzTaskDispatchers = null;

  @Autowired
  private BatchQuartzDao batchQuartzDao;

  @Override
  public Integer getTaskStatus(Long taskId) {
    Integer status = batchQuartzDao.getTaskStatus(taskId);
    return status;
  }

  @Override
  public List<BatchQuartz> getTasksByStatus(Integer taskStatus) {
    List<BatchQuartz> taskList = batchQuartzDao.getTasksByStatus(taskStatus);
    return taskList;
  }

  @Override
  public void updateJobError(Long jobId, String errorMsg, Date startTime) throws BatchTaskException {
    BatchQuartz job = new BatchQuartz();
    job = batchQuartzDao.getTaskById(jobId);

    if (StringUtils.isBlank(errorMsg)) {
      errorMsg = "Unknown Error!";
    }

    if (errorMsg.length() > 300) {
      errorMsg = errorMsg.substring(0, 300);
    }

    job.setExecutionMsg(errorMsg);
    job.setExecutionStatus(3);
    job.setStartTime(startTime);
    job.setEndTime(new Date());
    batchQuartzDao.save(job);
  }

  @Override
  public void updateJobSuccess(Long jobId, Date startTime) throws BatchTaskException {
    BatchQuartz job = batchQuartzDao.getTaskById(jobId);
    job.setExecutionStatus(2);
    job.setExecutionMsg("sucess");
    job.setStartTime(startTime);
    job.setEndTime(new Date());
    batchQuartzDao.save(job);
  }

  @Override
  public void saveQuartzJob(BatchQuartz task) {
    if (task != null) {
      batchQuartzDao.save(task);
    }
  }

  @Override
  public void execute(BatchQuartz task) throws BatchTaskException {
    if (task != null) {
      String strategy = task.getStrategy();
      Long id = task.getQuartzId();

      if (StringUtils.isBlank(strategy)) {
        throw new BatchTaskException("BatchQuartzTask中的Strategy为空，taskId = " + id + "!");
      }

      BatchQuartzTaskService batchQuartzTaskService = quartzTaskDispatchers.get(strategy);

      if (batchQuartzTaskService == null) {
        throw new BatchTaskException(
            "Map quartzTaskDispatchers中找不到对应任务，请检查xml配置，taskId = " + id + " ; strategy = " + strategy + " !");
      }

      batchQuartzTaskService.taskExecute(task);
    }
  }

  public Map<String, BatchQuartzTaskService> getQuartzTaskDispatchers() {
    return quartzTaskDispatchers;
  }

  public void setQuartzTaskDispatchers(Map<String, BatchQuartzTaskService> quartzTaskDispatchers) {
    this.quartzTaskDispatchers = quartzTaskDispatchers;
  }

}
