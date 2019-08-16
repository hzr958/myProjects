package com.smate.center.task.sys.quartz.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;

import com.smate.center.task.sys.quartz.model.QuartzCronExpression;

/**
 * 任务调度时间重置任务
 * 
 * @author zk
 *
 */
public class QuartzCronExpressionResetTask {

  protected Logger logger = LoggerFactory.getLogger(this.getClass());

  @Autowired
  private QuartzCronExpressionService quartzCronExpressionService;

  public void run() {
    try {
      List<QuartzCronExpression> qces = quartzCronExpressionService.getAllQuartzCronExpression();

      if (CollectionUtils.isEmpty(qces)) {
        return;
      }
      for (QuartzCronExpression qce : qces) {
        try {
          quartzCronExpressionService.reSetScheduleJob(qce);
        } catch (Exception e) {
          logger.error("重新设置定时器时间配置错误，任务：" + qce.getBeanName(), e);
        }
      }
    } catch (Exception e) {
      logger.error("任务时间配置重置任务", e);
    }
  }
}
