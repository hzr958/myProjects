package com.smate.center.job.framework.runner;

import com.smate.center.job.common.enums.JobStatusEnum;
import com.smate.center.job.common.exception.ServiceException;
import com.smate.center.job.framework.dto.TaskletDTO;
import com.smate.center.job.framework.exception.InterruptedJobException;
import com.smate.center.job.framework.exception.PrecheckException;
import com.smate.center.job.framework.service.OfflineJobService;
import com.smate.center.job.framework.support.JobProgressObservable;
import com.smate.center.job.framework.util.HibernateUtil;
import com.smate.core.base.utils.json.JacksonUtils;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.List;
import java.util.Objects;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.dozer.util.ReflectionUtils;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.UnableToInterruptJobException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

/**
 * 离线子任务执行者抽象基类，所有的离线子任务，都要继承此类
 *
 * @author houchuanjie
 * @date 2018/04/08 18:31
 */
public abstract class BaseOfflineJobRunner implements JobRunnable {

  //默认批量获取数据的大小
  private static final int DEFAULT_BATCH_SIZE = 100;
  // 日志
  protected Logger logger = LoggerFactory.getLogger(getClass());
  //是否被打断
  protected boolean interrupted = false;
  //子任务执行监视
  private JobProgressObservable observable;
  @Autowired
  private OfflineJobService offlineJobService;

  /**
   * 子任务执行入口
   */
  @Override
  public void execute(JobExecutionContext context) throws JobExecutionException {
    //注入依赖
    SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
    JobDataMap jobDataMap = context.getJobDetail().getJobDataMap();
    this.observable = (JobProgressObservable) jobDataMap.get(OBSERVABLE_KEY);
    TaskletDTO taskletDTO = this.observable.getTaskletDTO();
    //参数校验、检查不通过则返回
    if (!validate(taskletDTO, jobDataMap)) {
      return;
    }

    long begin = taskletDTO.getBegin();
    long end = taskletDTO.getEnd();
    long count = taskletDTO.getCount();
    try {
      this.observable.updateStatus(JobStatusEnum.PROCESSING, "");

      logger.info("正在执行子任务（id='{}'），分片起止ID区间：[{}, {}]，需要处理的记录数：{}", taskletDTO.getId(), begin, end,
          count);
      // 执行分片
      run(begin, end, count, jobDataMap);
      logger.info("子任务分片[id='{}', jobName='{}', begin={}, end={}, count={}, threadNumber={}]执行完毕！",
          taskletDTO.getId(), taskletDTO.getJobName(), begin, end, count,
          taskletDTO.getThreadNo());
      this.observable.updateStatus(JobStatusEnum.PROCESSED, "");
    } catch (JobExecutionException e) {
      logger.error("子任务分片[id='{}', jobName='{}', begin={}, end={}, count={}, threadNumber={}]执行出错！",
          taskletDTO.getId(), taskletDTO.getJobName(), begin, end, count,
          taskletDTO.getThreadNo(), e);
      this.observable.updateStatus(JobStatusEnum.FAILED, e.toString());
    } catch (InterruptedJobException e) {
      logger.warn("子任务分片[id='{}', jobName='{}', begin={}, end={}, count={}, threadNumber={}]被中断执行！",
          taskletDTO.getId(), taskletDTO.getJobName(), begin, end, count,
          taskletDTO.getThreadNo());
      this.observable.updateStatus(JobStatusEnum.FAILED, e.toString());
    } catch (Exception e) {
      logger.error("子任务分片[id='{}', jobName='{}', begin={}, end={}, count={}, threadNumber={}]执行出错！",
          taskletDTO.getId(), taskletDTO.getJobName(), begin, end, count,
          taskletDTO.getThreadNo(), e);
      this.observable.updateStatus(JobStatusEnum.FAILED, e.toString());
    }
  }

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
    if (Objects.isNull(getPersistentClass())) {
      String errMsg = MessageFormat
          .format("子任务（id='{0}'）执行失败！表对应的实体类不能为null，getPersistentClass()方法返回必须是表对应的实体类，请修复此问题！",
              taskletDTO.getId());
      logger.error(errMsg);
      this.observable.updateStatus(JobStatusEnum.FAILED, errMsg);
      return false;
    }
    return true;
  }

  /**
   * 业务处理方法，在处理业务的逻辑中必须要捕获任何可能存在的异常进行处理，如果是不可控或者必须抛出的异常， 需要将其包装成{@link JobExecutionException}异常，
   * 该异常类型的构造方法{@link JobExecutionException#JobExecutionException(Throwable, boolean)}
   * 第二个布尔类型参数refireImmediately可指定是否立即重新执行一次
   *
   * @param begin 业务表起始id
   * @param end 业务表截止id
   * @param count 总记录数
   * @param jobDataMap 额外业务操作参数集合
   */
  public void run(long begin, long end, long count, JobDataMap jobDataMap)
      throws JobExecutionException, InterruptedJobException {
    while (begin <= end) {
      List<?> list = null;
      try {
        //加载一批数据（已排序）
        list = loadData(begin, end);
      } catch (ServiceException e) {
        logger.error("批量读取数据表记录出错！", e);
        throw new JobExecutionException(e);
      }
      if (CollectionUtils.isNotEmpty(list)) {
        //遍历此批数据进行处理
        for (Object t : list) {
          try {
            if (interrupted) {
              throw new InterruptedJobException("子任务执行中被人为操作终止！");
            }
            //处理记录
            process(t, jobDataMap);
            //更新进度
            updateProgress();
          } catch (JobExecutionException e) {
            String errMsg = MessageFormat
                .format("处理第{0}条记录时出错！，entity：{1}", observable.getOffset(), t);
            logger.error(errMsg, e);
            throw new JobExecutionException(errMsg + "；" + e.getMessage(), e);
          }
        }
        //处理完成后取这批数据最后一条记录的id（最大id）
        Object lastObj = list.get(list.size() - 1);
        try {
          Long lastId = (Long) ReflectionUtils
              .invoke(HibernateUtil.findIdFieldGetter(getPersistentClass()), lastObj, null);
          begin = lastId + 1;
        } catch (Exception e) {
          String errMsg = MessageFormat
              .format("取第{0}条记录的id时出错！entity：{1}", observable.getOffset(), lastObj);
          logger.error(errMsg, e);
          throw new JobExecutionException(errMsg + "；" + e.getMessage(), e);
        }
      } else {  //取不到数据时表示已执行完毕
        break;
      }
    }
  }

  /**
   * 处理每一条数据，具体的业务逻辑实现，任务额外配置的参数在jobDataMap中
   *
   * @param entity 具体业务表数据对应的实体对象
   * @param jobDataMap 子任务额外配置的参数信息
   * @throws JobExecutionException
   */
  protected abstract void process(Object entity, JobDataMap jobDataMap)
      throws JobExecutionException;

  /**
   * 加载一批数据
   *
   * @param begin 起始id（）
   */
  protected List<?> loadData(long begin, long end) throws ServiceException {
    return offlineJobService.getJobData(begin, end, getBatchSize(), getPersistentClass(),
        observable.getTaskletDTO().getId());
  }

  /**
   * 可重写此方法，来重新定义一次批量获取数据的条数
   *
   * @return 一次批量读取数据的条数
   */
  protected int getBatchSize() {
    return DEFAULT_BATCH_SIZE;
  }

  /**
   * 返回要从数据库读取进行处理的持久化对象类型
   */
  protected abstract Class<?> getPersistentClass();

  /**
   * 更新子任务执行进度，业务处理的记录数偏移量值自增一。必须调用此方法，才能保证子任务进度正常统计。
   */
  protected void updateProgress() {
    observable.increaseOffset();
  }

  @Override
  public void interrupt() throws UnableToInterruptJobException {
    // 子任务被终止
    this.interrupted = true;
  }

  protected boolean isInterrupted() {
    return interrupted;
  }
}
