package com.smate.center.batch.joblaunch;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.smate.center.batch.connector.model.job.BatchJobs;
import com.smate.center.batch.service.BatchTaskPool;
import com.smate.center.batch.service.JobLaunchMultiThreadService;
import com.smate.center.batch.service.TaskMarkerService;
import com.smate.core.base.utils.exception.BatchTaskException;

/*
 * 
 * 用于标记batch_jobs表中的需要运行的任务，并触发多线程执行任务
 * 
 */
public class JobExecute {

  private final Logger logger = LoggerFactory.getLogger(getClass());

  private Integer counts = 0;

  @Autowired
  private TaskMarkerService taskMarkerService;

  @Autowired
  private BatchTaskPool batchTaskPool;

  @Value("${centerbatch.taskPoolSize}")
  private Integer taskPoolSize;

  @Autowired
  JobLaunchMultiThreadService jobLaunchMultiThreadService;

  public void run() throws BatchTaskException {
    logger.debug("=================CenterBatchTask开始运行，标记需要运行的任务=================");

    if (!isRun()) {
      logger.debug("=================CenterBatchTask开关关闭=================");
      return;
    } else {
      try {
        doRun();
      } catch (BatchTaskException e) {
        logger.error("BatchSingleThreadTask运行异常", e);
        throw new BatchTaskException(e);
      }
    }
  }

  public void doRun() throws BatchTaskException {
    try {
      List<BatchJobs> taskList = new ArrayList<BatchJobs>();

      int startCount = counts;
      // Long streadId = Thread.currentThread().getId();
      // 获得列表中待处理的msg数量,如果仍然有未处理完的任务，就不继续进行
      // 用来最外层控制任务的超时；此时会把所有已经分配（status=1）的任务置为超时，与每个线程内部的超时处理有区别
      if (counts > 50) {
        List<BatchJobs> jobList = taskMarkerService.getTaskListStatus1();

        if (CollectionUtils.isNotEmpty(jobList)) {
          for (Integer i = 0; i < jobList.size(); i++) {
            BatchJobs item = jobList.get(i);
            item.setStatus(3);
            item.setErrorMsg("运行超时，请检查相关代码");
            item.setJobEndTime(new Date());
            taskMarkerService.updateTaskStatus(item);
          }
          // System.out.println("执行任务超时， threadId = " + streadId +
          // "。 startCounts = " + startCount
          // + " counts计数被清零");
          // 超时时同时重置任务池map中的数据
          batchTaskPool.getTaskPool().clear();
          counts = 0;
          return;
        }
      }

      Long totalCount = taskMarkerService.getTaskMsgIdCountByStatus(1);

      if (totalCount > 0) {
        counts++;
        // int endCounts = counts;
        // System.out.println("仍有在执行的任务， threadId = " + streadId +
        // "。 startCounts = " + startCount
        // + " endCounts = " + endCounts);
        return;
      }

      // 获得列表中status为0的msg数量，如果不超过200条，就全取出放入list中
      Long count0 = taskMarkerService.getTaskMsgIdCountByStatus(0);

      if (count0 <= 0) {
        // System.out.println("表中无需要处理的数据， threadId = " + streadId +
        // "。 startCounts = " + startCount);
        logger.debug("===========Batch_Job表中无需要处理的数据!============");
        counts = 0;
        return;
      }

      // 按权重比例获取任务
      taskList = taskMarkerService.arrangeTask(taskPoolSize);

      // TODO,不要一个一个去修改
      for (Integer i = 0; i < taskList.size(); i++) {
        BatchJobs item = taskList.get(i);
        item.setStatus(1);
        taskMarkerService.updateTaskStatus(item);
      }

      jobLaunchMultiThreadService.JobLauchMultiThread();
      // System.out.println("执行任务完毕， threadId = " + streadId +
      // "。 startCounts = " + startCount + " endCounts = 0");
      counts = 0;
    } catch (Exception e) {
      logger.error("BatchTask运行异常", e);
    }

  }

  public boolean isRun() throws BatchTaskException {
    // 动态控制逻辑
    return true;
    // return false;
  }

}
