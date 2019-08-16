package com.smate.center.batch.jobdetail.wechatmsgpsn;

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
import com.smate.center.batch.model.sns.wechat.WeChatPreProcessPsn;
import com.smate.center.batch.service.BatchTaskPool;
import com.smate.center.batch.service.WeChatMsgPsnService;
import com.smate.core.base.utils.constant.wechat.WeChatConstant;
import com.smate.core.base.utils.exception.BatchTaskException;

/**
 * 根据reader读取到的信息，发送微信
 * 
 * @author hzr
 * @version 6.0.1
 * 
 */
public class WeChatMsgPsnWriter implements ItemWriter<WeChatPreProcessPsn> {

  private final Logger logger = LoggerFactory.getLogger(getClass());

  private JobExecution jobExecution;

  @Autowired
  private BatchJobsService taskService;

  @Autowired
  private WeChatMsgPsnService weChatMsgPsnService;

  @Autowired
  private BatchTaskPool batchTaskPool;

  @BeforeStep
  public void beforeStep(StepExecution stepExecution) {
    jobExecution = stepExecution.getJobExecution();
  }

  @Override
  public void write(List<? extends WeChatPreProcessPsn> items) throws BatchTaskException {
    Thread current = Thread.currentThread();
    Long jobId = jobExecution.getJobParameters().getLong(BatchConfConstant.JOB_ID);
    Long threadId = current.getId();
    WeChatPreProcessPsn msg = items.get(0);
    Long msgId = msg.getId();
    try {
      // 发送消息,一个jason格式的内容，具体见xys邮件;一个access
      // token，找tsz；组成一个map。map的键值在WeChatConstant.java中查找
      // 一个smateopenid可能对应多个微信Openid，需要发送多条微信消息
      List<Map> msgSentFeedbackMaps = weChatMsgPsnService.sendMsgPsn(msg);
      // 处理微信的返回值
      for (Map msgSentFeedback : msgSentFeedbackMaps) {
        int errorCode = (int) msgSentFeedback.get(WeChatConstant.ERRCODE_KEY);
        String errorMsg = msgSentFeedback.get(WeChatConstant.ERRMSG_KEY).toString();

        if (errorCode == WeChatConstant.ERRCODE_0 && errorMsg.equals(WeChatConstant.ERRMSG_OK)) {
          // 更新状态，并把成功发送的消息和任务写入历史表
          taskService.updateEndThreadOfJobSuccess(jobId, threadId);
          weChatMsgPsnService.updateStatusSuccess(msgId);
          continue;

        } else {
          String eMsg =
              "WeChatMsgPsnTask发送信息微信返回错误, msgId = " + msgId + "; jobId = " + jobId + "; errorMsg= " + errorMsg;
          logger.error(eMsg);
          taskService.updateEndThreadOfJobError(jobId, threadId, eMsg);
          weChatMsgPsnService.updateStatusError(msgId);
        }
      }
      batchTaskPool.remove(jobId);
    } catch (Exception e) {
      // 更新出错状态
      logger.error("WeChatMsgPsnTask发送信息错误：", e);
      taskService.updateEndThreadOfJobError(jobId, threadId, e.getMessage());
      weChatMsgPsnService.updateStatusError(msgId);
      batchTaskPool.remove(jobId);
    }
  }

}
