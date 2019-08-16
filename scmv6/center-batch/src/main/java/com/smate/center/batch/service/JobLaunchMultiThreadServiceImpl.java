package com.smate.center.batch.service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.smate.center.batch.connector.model.job.BatchJobs;
import com.smate.center.batch.connector.model.job.BatchJobsForTaskPool;
import com.smate.center.batch.connector.service.job.BatchJobsService;
import com.smate.center.batch.constant.BatchConfConstant;

/**
 * BatchJobs发起多线程任务service实现
 * 
 * @author hzr
 * @version 6.0.1
 * 
 */
@Service("jobLaunchMultiThreadService")
public class JobLaunchMultiThreadServiceImpl implements JobLaunchMultiThreadService {

  private final Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private JobLauncher jobLauncherMultiThread;

  @Autowired
  private TaskMarkerService taskMarkerService;

  @Autowired
  private BatchJobsService testService;

  @Autowired
  private Job jobSteps;

  @Value("${database.maxPoolSize}")
  private Integer taskSize;

  // 多线程共享的map，通过map中key保证的各线程处理完的任务从map中去掉。
  @Autowired
  private BatchTaskPool batchTaskPool;

  /****
   * 在固定10s发起任务的间隔中，能够让线程不空闲，尽可能多的处理任务；
   * 
   * 发起多线程任务说明：taskSize，是根据数据库线程池大小得到的，任务可以使用的线程数；JobExecute中的taskPoolSize，是自定义一次性分配的任务数；
   * 
   * map中存放taskPoolSize大小的待处理任务；JobLauchMultiThread开始后，每次分配不超过taskSize数量的线程，循环处理；每个线程处理完毕（包括成功或者失败）后，把对应jobid的任务从map中改变状态，继续
   * 处理剩下的任务。
   * 当前任务小于预定的tasksize时，继续分配tasksize-taskRunning，而不是和以前一样等待。由此保证运行的线程数，不会超过数据库支持的最大连接数，而造成系统锁死；
   * 同时也减少了等待时间，提高处理效率；由此循环，完成剩余任务。
   * 
   ***/
  @Override
  public void JobLauchMultiThread() {
    try {
      Map<Long, BatchJobsForTaskPool> poolMap = testService.getAllTaskMapStatus1();

      if (poolMap.isEmpty()) {
        logger.debug("========Batch_job无需要执行的任务！========");
        return;
      }

      batchTaskPool.setTaskPool(poolMap);
      Integer taskRunning = 0;
      // 只用作内部计数
      Long loopCount = 0L;

      while (this.getToRunTaskSize(batchTaskPool) != 0) {
        Thread.sleep(50);
        taskRunning = this.getRunningTaskSize(batchTaskPool);

        if (taskRunning < taskSize && taskRunning >= 0) {
          // 分配任务数量为taskSize-taskRunning
          Integer countIn = 0;
          Map<Long, BatchJobsForTaskPool> taskMap = this.getToRunTask(batchTaskPool);

          for (Entry<Long, BatchJobsForTaskPool> entry : taskMap.entrySet()) {
            if (countIn >= taskSize - taskRunning) {
              break;
            }
            launchJob(entry);
            countIn++;
          }
          loopCount++;
          continue;
        }
        loopCount++;
      }

      // 注意还需要检测map中状态是否还有状态为2,正在运行的任务,只有状态为0与状态为2的任务变为1后,才能清除
      while (this.getRunningTaskSize(batchTaskPool) > 0) {
        Thread.sleep(50);
      }

      batchTaskPool.clear();
      logger.debug("========Batch_job执行完成！========");
    } catch (Exception e) {
      logger.error("JobLaunchMultiThreadService 初始化任务错误", e);
    }
  }

  /**
   * 获取map中未处理的任务数
   * 
   */
  public Integer getToRunTaskSize(BatchTaskPool taskPool) {
    if (taskPool == null) {
      return 0;
    }

    Integer counts = 0;
    Map<Long, BatchJobsForTaskPool> poolMap = taskPool.getTaskPool();

    for (Entry<Long, BatchJobsForTaskPool> entry : poolMap.entrySet()) {
      BatchJobsForTaskPool task = entry.getValue();
      Integer status = task.getTaskPoolStatus();

      if (status == 0) {
        counts++;
      }
    }
    return counts;
  }

  /**
   * 获取map中正在处理的任务数
   * 
   */
  public Integer getRunningTaskSize(BatchTaskPool taskPool) {
    if (taskPool == null) {
      return 0;
    }

    Integer counts = 0;
    Map<Long, BatchJobsForTaskPool> poolMap = taskPool.getTaskPool();

    for (Entry<Long, BatchJobsForTaskPool> entry : poolMap.entrySet()) {
      BatchJobsForTaskPool task = entry.getValue();
      Integer status = task.getTaskPoolStatus();

      if (status == 2) {
        counts++;
      }
    }
    return counts;
  }


  /**
   * 获取map中未处理的任务
   * 
   */
  public Map<Long, BatchJobsForTaskPool> getToRunTask(BatchTaskPool taskPool) {
    if (taskPool == null) {
      return null;
    }

    Map<Long, BatchJobsForTaskPool> maps = new HashMap<Long, BatchJobsForTaskPool>();
    Map<Long, BatchJobsForTaskPool> poolMap = taskPool.getTaskPool();

    for (Entry<Long, BatchJobsForTaskPool> entry : poolMap.entrySet()) {
      BatchJobsForTaskPool task = entry.getValue();
      Integer status = task.getTaskPoolStatus();
      Long id = task.getBatchJobs().getJobId();

      if (status == 0) {
        maps.put(id, task);
      }
    }
    return maps;
  }

  /**
   * 发起任务
   * 
   */
  private void launchJob(Entry<Long, BatchJobsForTaskPool> entry) {
    BatchJobsForTaskPool task = entry.getValue();
    BatchJobs itemGet = task.getBatchJobs();
    Long jobId = itemGet.getJobId();
    try {
      batchTaskPool.running(jobId);
      String jobContent = itemGet.getJobContext();
      String strategy = itemGet.getStrategy();
      Date time = new Date();
      JobParametersBuilder builder = new JobParametersBuilder();
      JobParameters jobParameters = new JobParameters();
      builder.addDate(BatchConfConstant.JOB_CREATE_TIME, time);
      // jobContent存储传输过来的jason数据，供任务解析出需要的参数
      builder.addString(BatchConfConstant.JOB_CONTENT, jobContent);
      // jobStrategy为job的种类，供decider使用.
      builder.addString(BatchConfConstant.JOB_STRATEGY, strategy);
      builder.addLong(BatchConfConstant.JOB_ID, jobId);
      jobParameters = builder.toJobParameters();
      JobExecution result = jobLauncherMultiThread.run(jobSteps, jobParameters);
      logger.info(result.toString());
    } catch (Exception e) {
      logger.error("JobLaunchMultiThreadService 初始化任务错误, jobId = " + jobId, e);
    }
  }


}
