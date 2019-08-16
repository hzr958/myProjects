package com.smate.center.job.runner.example;

import com.smate.center.job.framework.exception.PrecheckException;
import com.smate.center.job.framework.runner.BaseOnlineJobRunner;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionException;
import org.springframework.stereotype.Component;

/**
 * @author houchuanjie
 * @date 2018/04/27 17:41
 */
@Component("HelloJobA")
public class HelloJobA extends BaseOnlineJobRunner {

  @Override
  protected void process(JobDataMap jobDataMap) throws JobExecutionException {
    System.out.println("HelloJobA is done.");
  }

  @Override
  public void preCheck(JobDataMap jobDataMap) throws PrecheckException {

  }
}
