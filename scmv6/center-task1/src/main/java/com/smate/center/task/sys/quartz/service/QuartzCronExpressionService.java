package com.smate.center.task.sys.quartz.service;

import java.util.List;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;

import com.smate.center.task.exception.TaskException;
import com.smate.center.task.sys.quartz.model.QuartzCronExpression;

/**
 * 定时器时间服务类
 * 
 * @author zk
 *
 */
public interface QuartzCronExpressionService {

  public static final Integer STOP_STATUS = 0;
  public static final Integer RUN_STATUS = 1;

  /**
   * 获取所有的定时器任务
   * 
   * @return
   * @throws Exception
   */
  public List<QuartzCronExpression> getAllQuartzCronExpression() throws Exception;

  /**
   * 修改单个定时器任务时间
   * 
   * @param qce
   * @throws Exception
   */
  public void reSetScheduleJob(QuartzCronExpression qce) throws Exception;

  /**
   * 得到QuartzCronExpression
   * 
   * @param name
   * @return
   * @throws Exception
   */
  public QuartzCronExpression getQuartzCronExpression(String name) throws Exception;

  void setBeanFactory(BeanFactory beanFactory) throws BeansException;

  /**
   * 设置任务状态为0
   * 
   * @param beanName
   * @throws Exception
   */
  void toSetTaskStatusZero(String beanName) throws Exception;

  /**
   * 停止任务
   * 
   * @param beanName
   * @throws Exception
   */
  void toStopScheduleJob(String beanName) throws TaskException;
}
