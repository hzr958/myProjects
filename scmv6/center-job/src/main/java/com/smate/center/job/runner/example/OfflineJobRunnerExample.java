package com.smate.center.job.runner.example;

import com.smate.center.job.business.model.file.FileDownloadRecord;
import com.smate.center.job.framework.exception.PrecheckException;
import com.smate.center.job.framework.runner.BaseOfflineJobRunner;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionException;
import org.springframework.stereotype.Component;

/**
 * 离线任务简单示例。离线任务适合跑静态的数据处理，比如数据表中的记录不会发生变化（跑任务过程中数据会修改、删除等就不适合）
 *
 * @author houchuanjie
 * @date 2018/05/04 11:43
 */
@Component("OfflineJobRunnerExample")
public class OfflineJobRunnerExample extends BaseOfflineJobRunner {

  @Override
  protected void process(Object entity, JobDataMap jobDataMap) throws JobExecutionException {
    try {
      System.out.println("处理记录：" + ((FileDownloadRecord) entity).getId());
      // 进行相对细粒度的捕获异常进行处理，尽量不要直接捕获Exception父异常
      // } catch (IOException e) {
      // } catch (IllegalArgumentException e){
    } catch (Exception e) {
      // 打印异常日志
      logger.error("处理xxx记录时出现异常！该记录id={}", e);
      // 不再进行处理，抛出异常，终止任务执行。
      throw new JobExecutionException(e);
    }
  }

  @Override
  public void preCheck(JobDataMap jobDataMap) throws PrecheckException {

  }

  @Override
  protected Class<?> getPersistentClass() {
    return FileDownloadRecord.class;
  }
}
