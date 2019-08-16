package com.smate.center.batch.service;

import java.util.Date;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.smate.center.batch.constant.BatchConfConstant;

/**
 * 微信消息去重任务发起者service接口实现
 * 
 * @author hzr
 * @version 6.0.1
 * 
 */
@Service("weChatPreProcessPsnTaskLaunchService")
public class WeChatPreProcessPsnTaskLaunchServiceImpl implements WeChatPreProcessPsnTaskLaunchService {
  @Autowired
  private JobLauncher weChatPreProcessPsnTaskLaunch;

  @Autowired
  private Job weChatPreProcessPsnTask;

  @Override
  public void jobLauncher() {

    try {
      // 为jobInstance添加创建时间属性,保证每次发起新任务
      Date time = new Date();
      JobParametersBuilder builder = new JobParametersBuilder();
      JobParameters jobParameters = new JobParameters();
      builder.addDate(BatchConfConstant.JOB_CREATE_TIME, time);
      jobParameters = builder.toJobParameters();

      JobExecution result = weChatPreProcessPsnTaskLaunch.run(weChatPreProcessPsnTask, jobParameters);
      System.out.println(result.toString());
    } catch (Exception e) {
      e.printStackTrace();
    }
  }


}
