package com.smate.center.task.base;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import com.smate.center.task.exception.TaskException;
import com.smate.center.task.sys.quartz.model.QuartzCronExpression;
import com.smate.center.task.sys.quartz.service.QuartzCronExpressionService;

/**
 * 任务抽象类
 * 
 * @author zk
 *
 */
public class TaskAbstract {

  protected Logger logger = LoggerFactory.getLogger(getClass());
  private String beanName; // 需要关闭的任务

  public TaskAbstract() {}

  public TaskAbstract(String beanName) {
    this.beanName = beanName;
  }

  @Autowired
  private QuartzCronExpressionService quartzCronExpressionService;

  /**
   * 关闭一次性任务
   * 
   * @throws TaskException
   */
  public void closeOneTimeTask() throws TaskException {
    if (StringUtils.isBlank(beanName)) {
      throw new TaskException("close one-time task error,beanName is null!!");
    }
    quartzCronExpressionService.toStopScheduleJob(beanName);
  }

  /**
   * 是否允许执行
   * 
   * @return
   */
  public boolean isAllowExecution() {
    Assert.notNull(beanName, "beanName is null");
    try {
      QuartzCronExpression qce = quartzCronExpressionService.getQuartzCronExpression(beanName);
      if (qce == null) {
        return false;
      }
      if (qce.getStatus() == 1) {
        return true;
      } else {
        return false;
      }
    } catch (Exception e) {
      logger.error("查询是否允许执行出错,beanName=" + beanName);
      return false;
    }
  }
}
