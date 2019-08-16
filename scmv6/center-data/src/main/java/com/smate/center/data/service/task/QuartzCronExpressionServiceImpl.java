package com.smate.center.data.service.task;

import java.util.List;

import javax.transaction.Transactional;

import org.quartz.Scheduler;
import org.quartz.Trigger;
import org.quartz.Trigger.TriggerState;
import org.quartz.TriggerKey;
import org.quartz.impl.StdScheduler;
import org.quartz.impl.triggers.CronTriggerImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.smate.center.data.dao.quartz.QuartzCronExpressionDao;
import com.smate.center.data.exception.TaskException;
import com.smate.center.data.model.quartz.QuartzCronExpression;

/**
 * 调度时间服务类
 * 
 * @author zk
 *
 */
@Service("quartzCronExpressionService")
@Transactional(rollbackOn = Exception.class)
public class QuartzCronExpressionServiceImpl implements QuartzCronExpressionService, BeanFactoryAware {

  private static final String SCHEDULER_FACTORY_BEAN = "schedulerFactoryBean";

  protected Logger logger = LoggerFactory.getLogger(this.getClass());
  private BeanFactory beanFactory;
  @Autowired
  private QuartzCronExpressionDao quartzCronExpressionDao;

  @Override
  public List<QuartzCronExpression> getAllQuartzCronExpression() throws Exception {
    return quartzCronExpressionDao.getAll();
  }

  @Override
  public void reSetScheduleJob(QuartzCronExpression qce) throws Exception {

    try {
      StdScheduler scheduler = (StdScheduler) beanFactory.getBean(SCHEDULER_FACTORY_BEAN);
      TriggerKey triggerKey = new TriggerKey(qce.getBeanName(), Scheduler.DEFAULT_GROUP);
      Object beanObject;
      try {
        beanObject = beanFactory.getBean(qce.getBeanName());
        if (beanObject == null) {
          logger.error("重新设置定时器时间配置---" + qce.getBeanName() + "未配置");
          return;
        }
      } catch (NoSuchBeanDefinitionException e) {
        logger.error("任务" + qce.getBeanName() + "为空，可能已经被删除。请检查配置，或者清理v_quartz_cron_expression表中相关触发器数据");
        return;
      }

      TriggerState triggerState = scheduler.getTriggerState(triggerKey);
      // 如果是要求关闭任务，则暂停服务
      if (STOP_STATUS.equals(qce.getStatus())) {
        if (!Trigger.TriggerState.PAUSED.equals(triggerState)) {
          scheduler.pauseTrigger(triggerKey);
        }
      } else {
        // 之前是关闭的，或者时间改变，重新启动任务
        CronTriggerImpl cronTrigger = (CronTriggerImpl) beanObject;
        String originConExpression = cronTrigger.getCronExpression();
        if (!originConExpression.equals(qce.getExpression()) || Trigger.TriggerState.PAUSED.equals(triggerState)) {
          cronTrigger.setCronExpression(qce.getExpression());
          scheduler.rescheduleJob(triggerKey, cronTrigger);
        }
      }
    } catch (Exception e) {
      logger.error("重新设置定时器时间配置出错,cronTrigger=" + qce.getBeanName(),
          e.toString().substring(0, e.toString().length() > 200 ? 200 : e.toString().length()));
    }
  }

  @Override
  public void toStopScheduleJob(String beanName) throws TaskException {
    try {
      StdScheduler scheduler = (StdScheduler) beanFactory.getBean(SCHEDULER_FACTORY_BEAN);
      TriggerKey triggerKey = new TriggerKey(beanName, Scheduler.DEFAULT_GROUP);
      Object beanObject = beanFactory.getBean(beanName);
      if (beanObject == null) {
        logger.error("关闭定时器---" + beanName + "未配置");
        return;
      }
      TriggerState triggerState = scheduler.getTriggerState(triggerKey);
      // 则停止服务器
      if (!Trigger.TriggerState.PAUSED.equals(triggerState)) {
        scheduler.pauseTrigger(triggerKey);
      }
      this.toSetTaskStatusZero(beanName);
    } catch (Exception e) {
      throw new TaskException("停止定时器任务出错", e);
    }
  }

  @Override
  public QuartzCronExpression getQuartzCronExpression(String beanName) throws Exception {

    try {
      return quartzCronExpressionDao.findQuartzCronExpression(beanName);
    } catch (Exception e) {
      logger.error("获取定时器时间配置", e);
      throw new Exception("获取定时器时间配置", e);
    }
  }

  @Override
  public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
    this.beanFactory = beanFactory;
  }

  @Override
  public void toSetTaskStatusZero(String beanName) throws Exception {
    this.quartzCronExpressionDao.stopTask(beanName);
  }

}
