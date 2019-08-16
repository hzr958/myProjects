package com.smate.center.batch.service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.smate.center.batch.connector.model.job.BatchJobsForTaskPool;

@Service("batchTaskPool")
public class BatchTaskPool {

  private final Logger logger = LoggerFactory.getLogger(getClass());

  private Map<Long, BatchJobsForTaskPool> taskPool = new ConcurrentHashMap<Long, BatchJobsForTaskPool>();

  public Map<Long, BatchJobsForTaskPool> getTaskPool() {
    return taskPool;
  }

  public void setTaskPool(Map<Long, BatchJobsForTaskPool> taskPool) {
    this.taskPool = taskPool;
  }

  public void remove(Long jobId) {

    BatchJobsForTaskPool batchJobsForTaskPool = taskPool.get(jobId);
    // 已经完成任务
    if (batchJobsForTaskPool != null) {
      batchJobsForTaskPool.setTaskPoolStatus(1);
    }
    logger.info("batchTaskPool 已经移除任务， jobId =" + jobId);
  }

  // 为正在运行的任务设置状态2
  public void running(Long jobId) {

    BatchJobsForTaskPool batchJobsForTaskPool = taskPool.get(jobId);
    Assert.notNull(batchJobsForTaskPool, "batchTaskPool 未获取到需要执行的任务请检查，jobId= " + jobId);
    batchJobsForTaskPool.setTaskPoolStatus(2);
  }

  public void clear() {
    taskPool.clear();
  }

}

