package com.smate.center.job.framework.runner;

import com.smate.center.job.common.enums.JobStatusEnum;
import com.smate.center.job.framework.dto.TaskletDTO;
import com.smate.center.job.framework.exception.PrecheckException;
import com.smate.center.job.framework.support.JobProgressObservable;
import com.smate.core.base.utils.json.JacksonUtils;
import java.io.IOException;
import org.apache.commons.lang3.StringUtils;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.UnableToInterruptJobException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

/**
 * @author houchuanjie
 * @date 2018/04/27 17:45
 */
public abstract class BaseOnlineJobRunner implements JobRunnable {

  protected Logger logger = LoggerFactory.getLogger(getClass());
  protected boolean interrupted = false;
  private JobProgressObservable observable;

  @Override
  public void execute(JobExecutionContext context) throws JobExecutionException {
    SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
    JobDataMap jobDataMap = context.getJobDetail().getJobDataMap();
    this.observable = (JobProgressObservable) jobDataMap.get(OBSERVABLE_KEY);
    TaskletDTO taskletDTO = this.observable.getTaskletDTO();
    //参数校验、检查不通过则返回
    if (!validate(taskletDTO, jobDataMap)) {
      return;
    }

    this.observable.updateStatus(JobStatusEnum.PROCESSING, "");
    logger.info("正在执行任务{}", taskletDTO.getId());

    // 执行任务
    try {
      run(jobDataMap);
      logger.info("任务[id=\"{}\", jobName=\"{}\"]执行完毕！", taskletDTO.getId(), taskletDTO
          .getJobName());
      this.observable.updateStatus(JobStatusEnum.PROCESSED, "");
    } catch (JobExecutionException e) {
      logger.error("任务执行失败！", e);
      this.observable.updateStatus(JobStatusEnum.FAILED, e.toString());
      throw e;
    } catch (Exception e) {
      logger.error("任务执行失败！", e);
      this.observable.updateStatus(JobStatusEnum.FAILED, e.toString());
    }
  }

  /**
   * 业务处理方法，在处理业务的逻辑中必须要捕获任何可能存在的异常进行处理，如果是不可控或者必须抛出的异常， 需要将其包装成{@link JobExecutionException}异常，
   * 该异常类型的构造方法{@link JobExecutionException#JobExecutionException(Throwable, boolean)}
   * 第二个布尔类型参数refireImmediately可指定是否立即重新执行一次
   *
   * @param jobDataMap 额外业务操作参数集合
   * @throws JobExecutionException
   */
  public void run(JobDataMap jobDataMap) throws JobExecutionException {
    process(jobDataMap);
  }

  /**
   * 处理数据，具体的业务逻辑实现，任务额外配置的参数在jobDataMap中
   *
   * @param jobDataMap 子任务额外配置的参数信息
   * @throws JobExecutionException
   */
  protected abstract void process(JobDataMap jobDataMap)
      throws JobExecutionException;

  /**
   * 参数校验
   *
   * @param taskletDTO
   * @param jobDataMap
   * @return
   */
  @Override
  public boolean validate(TaskletDTO taskletDTO, JobDataMap jobDataMap) {
    // 取子任务额外参数
    boolean hasOtherParams = StringUtils.isNotBlank(taskletDTO.getDataMap());
    if (hasOtherParams) {
      String errMsg = "";
      try {
        jobDataMap.putAll(JacksonUtils.json2Map(taskletDTO.getDataMap()));
        //预检查
        preCheck(jobDataMap);
        return true;
      } catch (IOException e) {
        logger.error("子任务（id='{}'）执行失败！转换子任务额外的参数Json字符串时发生错误！", taskletDTO.getId(), e);
        errMsg = "转换DATA_MAP字段时出错：" + e.getMessage();
      } catch (PrecheckException e) {
        logger.error("子任务（id='{}'）执行失败！参数预检查未通过！", taskletDTO.getId(), e);
        errMsg = "自定义预检查未通过：" + e.getMessage();
      } catch (Exception e) {
        logger.error("子任务（id='{}'）执行失败！参数预检查出错！", taskletDTO.getId(), e);
        errMsg = "预检查出错：" + e.getMessage();
      }
      this.observable.updateStatus(JobStatusEnum.FAILED, errMsg);
      return false;
    }
    return true;
  }

  @Override
  public void interrupt() throws UnableToInterruptJobException {
    // 任务被终止
    interrupted = true;
  }

  protected boolean isInterrupted() {
    return interrupted;
  }
}
