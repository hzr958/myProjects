package com.smate.center.batch.jobdetail.wechatmsgpsn;

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
import com.smate.center.batch.model.sns.wechat.WeChatPreProcessPsn;
import com.smate.center.batch.service.BatchTaskPool;
import com.smate.center.batch.service.WeChatMsgPsnService;
import com.smate.core.base.utils.exception.BatchTaskException;
import com.smate.core.base.utils.json.JacksonUtils;

/**
 * 从V_BATCH_PRE_WECHAT_PSN读取已经去重的需要发送的信息，并更新相应状态
 * 
 * @author hzr
 * @version 6.0.1
 * 
 */
public class WeChatMsgPsnReader implements ItemReader<WeChatPreProcessPsn> {

  private final Logger logger = LoggerFactory.getLogger(getClass());

  private JobExecution jobExecution;

  @Autowired
  BatchJobsService taskService;

  @Autowired
  WeChatMsgPsnService weChatMsgPsnService;

  @Autowired
  private BatchTaskPool batchTaskPool;

  @BeforeStep
  public void beforeStep(StepExecution stepExecution) {
    jobExecution = stepExecution.getJobExecution();
  }

  @Override
  public WeChatPreProcessPsn read()
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
      String error = "未获取到微信个人信息预处理表V_BATCH_PRE_WECHAT_PSN中id, jobId: " + jobId;
      logger.error(error);
      weChatMsgPsnService.updateStatusError(id);
      // 设置错误状态3
      taskService.updateEndThreadOfJobError(jobId, threadId, error);
      batchTaskPool.remove(jobId);
      return null;
    }

    try {
      taskService.updateStartThreadOfJob(jobId, threadId, jobInstanceId, startTime);
      WeChatPreProcessPsn msg = new WeChatPreProcessPsn();
      msg = weChatMsgPsnService.getUnProcessedWeChatPreProcessPsnById(id);
      WeChatPreProcessPsn msgCorr = weChatMsgPsnService.getWeChatPreProcessPsnById(id);

      // 判断列表中对应job为空是真正为空；为空，则抛出为找到对应信息异常
      if (msgCorr == null) {
        throw new BatchTaskException("未获取到微信个人信息预处理表V_BATCH_PRE_WECHAT_PSN对应消息, jobId: " + jobId);
      }

      // 从列表中取完值更改状态为1后，返回null值退出task
      if (msg == null && msgCorr != null) {
        // 再查一遍job表，防止JOB表中有多个对应到V_BATCH_PRE_WECHAT_PSN同一msgid，但是状态status仍然为1的job，导致BatchJob整个任务调度阻塞
        // 这种情况是不应该发生的，因为按照设计要求，V_BATCH_JOBS表中一条记录，只能对应V_BATCH_PRE_WECHAT_PSN等数据表中唯一一条记录；而且执行完的任务，要么执行成功被
        // 删除，要么执行错误，设置状态为3
        BatchJobs jobDup = taskService.getJobWithStatus1ByJobId(jobId);
        if (jobDup != null) {
          String errorMsg = "V_BATCH_JOBS表中插入重复任务，请检查！jobId：" + jobId;
          taskService.updateEndThreadOfJobError(jobId, threadId, errorMsg);
          batchTaskPool.remove(jobId);
        }
        return null;
      }

      return msg;
    } catch (Exception e) {
      // 更新出错状态
      logger.error("WeChatMsgPsnTask发送信息错误：", e);
      taskService.updateEndThreadOfJobError(jobId, threadId, e.getMessage());
      weChatMsgPsnService.updateStatusError(id);
      return null;
    }
  }

}
