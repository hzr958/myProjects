package com.smate.center.job.framework.runner;

import com.smate.center.job.framework.dto.TaskletDTO;
import com.smate.center.job.framework.exception.PrecheckException;
import org.quartz.InterruptableJob;
import org.quartz.Job;
import org.quartz.JobDataMap;

/**
 * 任务执行类接口
 *
 * @author houchuanjie
 * @date 2018/04/08 17:11
 */
public interface JobRunnable extends Job, InterruptableJob {

  String OBSERVABLE_KEY = "observable";
  /**
   * 预检查，检查不通过抛出PrecheckException（封装异常提示信息）即可
   */
  void preCheck(JobDataMap jobDataMap) throws PrecheckException;

  boolean validate(TaskletDTO taskletDTO, JobDataMap jobDataMap);
}
