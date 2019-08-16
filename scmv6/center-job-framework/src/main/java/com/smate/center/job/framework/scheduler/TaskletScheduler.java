package com.smate.center.job.framework.scheduler;

import com.smate.center.job.framework.factory.SpringQuartzJobFactory;
import com.smate.center.job.framework.po.JobServerNodePO;
import com.smate.center.job.framework.runner.JobRunnable;
import com.smate.center.job.framework.support.JobProgressObservable;
import com.smate.center.job.framework.util.SpringUtil;
import com.smate.core.base.utils.string.StringUtils;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor.CallerRunsPolicy;
import org.quartz.JobDataMap;
import org.quartz.JobKey;
import org.quartz.ScheduleBuilder;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.UnableToInterruptJobException;
import org.quartz.impl.JobDetailImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

/**
 * 任务调度器
 *
 * @author houchuanjie
 * @date 2018/04/17 17:44
 */
public class TaskletScheduler {

  private static final String DEFAULT_THREAD_NAME_PREFIX = "taskletExecutor_%{}%_Thread-";
  private Logger logger = LoggerFactory.getLogger(getClass());
  // 任务调度器
  private Scheduler scheduler;
  // 任务线程池
  private ThreadPoolTaskExecutor threadPoolTaskExecutor;
  private ThreadPoolExecutor threadPoolExecutor;
  private CallerRunsPolicy callerRunsPolicy;
  private ScheduleBuilder<?> scheduleBuilder;
  private TriggerBuilder<Trigger> triggerBuilder;

  public TaskletScheduler(JobServerNodePO serverNode) {
    init(serverNode);
  }

  /**
   * 获取线程池中剩余可用线程数，由于线程池中的线程和任务执行状态在计算时会动态变化，所以这个结果是一个近似值
   *
   * @return 线程池中剩余可用线程数，近似值
   */
  public int getAvailableThreadSize() {
    return (int) (threadPoolTaskExecutor.getMaxPoolSize() - threadPoolTaskExecutor
        .getActiveCount());
  }

  /**
   * 提交任务，计划执行。
   * <p>
   * {@code group + name} 来确定任务的key，用于停止任务
   * </p>
   *
   * @param jobKey 标识该任务的key
   * @param jobClass 任务执行类的类型
   * @return 是否提交成功
   */
  public boolean submit(JobKey jobKey, Class<? extends JobRunnable> jobClass,
      JobProgressObservable observable) {
    logger.debug("创建任务：{}", jobKey);
    JobDataMap jobDataMap = new JobDataMap();
    jobDataMap.put(JobRunnable.OBSERVABLE_KEY, observable);
    return createJob(jobKey, jobClass, jobDataMap);
  }

  /**
   * 创建任务
   */
  @SuppressWarnings("unchecked")
  private boolean createJob(JobKey jobKey, Class<? extends JobRunnable> jobClass,
      JobDataMap jobDataMap) {
    try {
      JobDetailImpl jobDetailImpl = new JobDetailImpl();
      jobDetailImpl.setJobClass(jobClass);
      jobDetailImpl.setKey(jobKey);
      jobDetailImpl.setJobDataMap(jobDataMap);
      Trigger trigger = triggerBuilder.withIdentity(jobKey.getName(), jobKey.getGroup())
          .withSchedule(scheduleBuilder).build();
      scheduler.scheduleJob(jobDetailImpl, trigger);
      return true;
    } catch (SchedulerException e) {
      logger.error("将任务添加到任务执行调度器时出错！", e);
    }
    return false;
  }

  public void init(JobServerNodePO serverNode) {
    if (threadPoolTaskExecutor == null && scheduler == null) {
      threadPoolTaskExecutor = initExecutor(serverNode.getCorePoolSize(),
          serverNode.getMaxPoolSize(), serverNode.getKeepAliveSeconds(),
          10, getThreadNamePrefix(serverNode.getName()));
      threadPoolTaskExecutor.initialize();
      this.threadPoolExecutor = threadPoolTaskExecutor.getThreadPoolExecutor();
      initScheduler(threadPoolTaskExecutor);
    }
    if (scheduleBuilder == null) {
      scheduleBuilder = SimpleScheduleBuilder.simpleSchedule().withIntervalInMilliseconds(1)
          .withRepeatCount(0);
    }
    if (triggerBuilder == null) {
      triggerBuilder = TriggerBuilder.newTrigger();
    }
  }

  /**
   * 初始化Quartz调度器
   *
   * @param executor
   */
  private void initScheduler(ThreadPoolTaskExecutor executor) {
    SchedulerFactoryBean schedulerFactoryBean = SpringUtil.createBean(SchedulerFactoryBean.class);
    schedulerFactoryBean.setTaskExecutor(executor);
    schedulerFactoryBean.setJobFactory(SpringUtil.createBean(SpringQuartzJobFactory.class));
    schedulerFactoryBean.start();
    scheduler = schedulerFactoryBean.getScheduler();
  }

  /**
   * 初始化线程池
   *
   * @param corePoolSize 核心线程数
   * @param maxPoolSize 最大线程数
   * @param keepAliveSeconds 线程活跃时间
   * @param queueCapacity 队列大小
   * @param threadNamePrefix 线程名称前缀
   */
  private ThreadPoolTaskExecutor initExecutor(int corePoolSize, int maxPoolSize,
      int keepAliveSeconds, int queueCapacity, String threadNamePrefix) {
    ThreadPoolTaskExecutor threadPoolTaskExecutor = new ThreadPoolTaskExecutor();
    // 设置核心线程数
    threadPoolTaskExecutor.setCorePoolSize(corePoolSize);
    // 设置最大线程数
    threadPoolTaskExecutor.setMaxPoolSize(maxPoolSize);
    // 设置线程最大空闲时间
    threadPoolTaskExecutor.setKeepAliveSeconds(keepAliveSeconds);
    // 设置队列大小
    threadPoolTaskExecutor.setQueueCapacity(queueCapacity);
    // 设置线程名称前缀
    threadPoolTaskExecutor.setThreadNamePrefix(threadNamePrefix);
    callerRunsPolicy = new CallerRunsPolicy();
    threadPoolTaskExecutor.setRejectedExecutionHandler(callerRunsPolicy);
    return threadPoolTaskExecutor;
  }

  private String getThreadNamePrefix(String serverName) {
    return StringUtils.replace(DEFAULT_THREAD_NAME_PREFIX, "%{}%", serverName);
  }

  /**
   * 打断任务执行（乐观行为，仅仅是通知Quartz去打断而已，是否真的会终止执行，取决于子任务的InterruptableJob接口实现情况）
   *
   * @param key
   */
  public void interrupt(JobKey key) {
    try {
      scheduler.interrupt(key);
    } catch (UnableToInterruptJobException e) {
      logger.error("停止子任务失败！jobKey='{}'", key, e);
    }
  }
}
