package com.smate.center.batch.tasklet.base;

import com.smate.center.batch.connector.service.job.BatchJobsService;
import com.smate.center.batch.constant.BatchConfConstant;
import com.smate.center.batch.service.BatchTaskPool;
import com.smate.core.base.utils.exception.BatchTaskException;
import com.smate.core.base.utils.json.JacksonUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

/**
 * Tasklet父类方法，其他task需要继承，并实现dataVerification（数据校验）,以及taskExecution（具体业务实现）；
 * 构造V_BATCH_JOBS的job_context内容时需要区分是带数据和不带数据的任务； 带数据的任务需要把初始表中数据Id：XXXXXXX构造成格式为
 * {"msg_id":XXXXXXX}的内容，存入job_context中 不带数据的任务需要构造的格式为 {"msg_id":"datafree"}
 * 
 * @author hzr
 * @version 6.0.1
 * 
 */
public abstract class BaseTasklet implements Tasklet {

  protected Logger logger = LoggerFactory.getLogger(getClass());

  private JobExecution jobExecution;

  @Autowired
  private BatchJobsService taskService;

  @Autowired
  private BatchTaskPool batchTaskPool;

  @Override
  public final RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
    Date startTime = new Date();
    Thread current = Thread.currentThread();
    jobExecution = chunkContext.getStepContext().getStepExecution().getJobExecution();
    String jobContent = jobExecution.getJobParameters().getString(BatchConfConstant.JOB_CONTENT);
    Long jobId = jobExecution.getJobParameters().getLong(BatchConfConstant.JOB_ID);
    Long jobInstanceId = jobExecution.getJobInstance().getId();
    String jobStrategy = jobExecution.getJobParameters().getString(BatchConfConstant.JOB_STRATEGY);
    Long threadId = current.getId();
    try {
      taskService.updateStartThreadOfJob(jobId, threadId, jobInstanceId, startTime);
      Map jobContentMap = JacksonUtils.jsonToMap(jobContent);
      // 判断是否为需要读取数据的任务
      String withData = String.valueOf(jobContentMap.get("msg_id"));

      if (StringUtils.isBlank(withData)) {
        String error = "未获取到V_BATCH_JOBS表,content的msg_id, jobId: " + jobId;
        logger.error(error);
        // 设置错误状态3
        taskService.updateEndThreadOfJobError(jobId, threadId, error);
        batchTaskPool.remove(jobId);
        return RepeatStatus.FINISHED;
      }

      if (!(withData.equalsIgnoreCase(BatchConfConstant.JOB_WITHOUT_DATA))) { // 如果不是datafree的任务，则需要检验表中数据是否存在
        DataVerificationStatus Status = dataVerification(withData);

        if (DataVerificationStatus.NULL.equals(Status)) {
          String error = "未获取到与业务发起相关数据表数据, jobId: " + jobId;
          logger.error(error);
          // 设置错误状态3
          taskService.updateEndThreadOfJobError(jobId, threadId, error);
          batchTaskPool.remove(jobId);
          return RepeatStatus.FINISHED;
        }

        if (DataVerificationStatus.FALSE.equals(Status)) {
          String error = "相关业务数据未通过验证, jobId: " + jobId;
          logger.error(error);
          // 设置错误状态3
          taskService.updateEndThreadOfJobError(jobId, threadId, error);
          batchTaskPool.remove(jobId);
          return RepeatStatus.FINISHED;
        }
      }

      // 具体业务执行
      ExecutorService executor = Executors.newSingleThreadExecutor();
      List<Long> threaIds = new ArrayList<Long>();
      FutureTask<String> future = new FutureTask<String>(new Callable<String>() {
        public String call() throws Exception {
          control(jobContentMap, batchTaskPool, jobId);
          // 更新状态，并把成功发送的消息和任务写入历史表
          return "sucess";
        }
      });
      executor.execute(future);
      try {
        // 设置超时时间为180s
        String result = future.get(1800000, TimeUnit.MILLISECONDS);
      } catch (TimeoutException te) {
        // 如果线程没有被阻塞，current.interrupt不会起作用，直接判断为执行超时。更新数据库状态，然后退出；
        // 然后退出
        String errorMsg = "====任务执行超时, jobStrategy: " + jobStrategy + ", jobId: " + jobId + "====";
        logger.error(errorMsg, te);
        taskService.updateEndThreadOfJobError(jobId, threadId, errorMsg);
        // 关闭发起future的线程池
        executor.shutdownNow();
        batchTaskPool.remove(jobId);
        return RepeatStatus.FINISHED;
      } catch (ExecutionException ee) {
        // 由taskExecution抛出的一般错误
        String errorMsg = "====任务执行错误, jobStrategy: " + jobStrategy + ", jobId: " + jobId + "====";
        logger.error(errorMsg, ee);
        taskService.updateEndThreadOfJobError(jobId, threadId, ee.getMessage());
        // 关闭发起future的线程池
        batchTaskPool.remove(jobId);
        return RepeatStatus.FINISHED;
      } catch (InterruptedException ie) {
        // 超时后current.interrupt中断，如果线程是被其他原因阻塞，就会抛出InterruptedException，在此处仍然是判断为超时
        String errorMsg = "====任务执行超时, jobStrategy: " + jobStrategy + "====";
        logger.error(errorMsg, ie);
        taskService.updateEndThreadOfJobError(jobId, threadId, ie.getMessage());
        // 关闭发起future的线程池
        executor.shutdown();
        batchTaskPool.remove(jobId);
        return RepeatStatus.FINISHED;
      }
      taskService.updateEndThreadOfJobSuccess(jobId, threadId);
      batchTaskPool.remove(jobId);
      executor.shutdown();
      return RepeatStatus.FINISHED;
    } catch (Exception e) {
      // 更新出错状态
      String errorMsg = "====任务执行错误, jobStrategy: " + jobStrategy + "====, jobId: " + jobId + "====";
      logger.error(errorMsg, e);
      taskService.updateEndThreadOfJobError(jobId, threadId, e.getMessage());
      batchTaskPool.remove(jobId);
      return RepeatStatus.FINISHED;
    } catch (Throwable e) {
      // 捕获更低层次错误
      String errorMsg = "====任务执行错误，捕获到Throwable错误, jobStrategy: " + jobStrategy + "====, jobId: " + jobId + "====";
      logger.error(errorMsg, e);
      taskService.updateEndThreadOfJobError(jobId, threadId, e.getMessage());
      batchTaskPool.remove(jobId);
      return RepeatStatus.FINISHED;
    }
  }

  /**
   * 任务数据校验；如果任务是datafree类型（无数据类型），则可以返回任意DataVerificationStatus状态，对执行结果无影响
   * 
   * @param String withData；从V_BATCH_JOBS的job_context字段解析出的msg_id字符串，可以是"datafree"或者是具体id
   *        "XXXXXXXX"。如果是具体Id，注意在查询时转换为Long或者Integer
   * 
   * @return DataVerificationStatus：NULL, FALSE, TRUE
   */
  public abstract DataVerificationStatus dataVerification(String withData) throws BatchTaskException;

  public void control(Map jobContentMap, BatchTaskPool batchTaskPool, Long jobId) throws Exception {
    try {
      taskExecution(jobContentMap);
    } catch (Exception e) {
      // 用于打断阻塞的线程
      batchTaskPool.remove(jobId);

      if (e.getCause() instanceof InterruptedException) {
        logger.debug("BaseTasklet中control方法捕获InterruptedException", e);
        return;
      }

      throw new Exception(e);
    }
  }

  /**
   * 具体业务实现
   * 
   * @param Map
   *        jobContentMap；从V_BATCH_JOBS的job_context字段解析出jobContentMap,包含msg_id字符串，可以是"datafree"或者是具体id
   *        "XXXXXXXX"。如果是具体Id，注意在查询时转换为Long或者Integer
   * 
   */
  public abstract void taskExecution(Map jobContentMap) throws BatchTaskException;

}
