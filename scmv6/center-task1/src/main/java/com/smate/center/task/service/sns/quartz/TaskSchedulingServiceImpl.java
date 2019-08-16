package com.smate.center.task.service.sns.quartz;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.task.sys.quartz.dao.QuartzCronExpressionDao;

/**
 * 定时任务调度实现类
 * 
 * @author LJ
 *
 */

@Service
@Transactional(rollbackFor = Exception.class)
public class TaskSchedulingServiceImpl implements TaskSchedulingService {
  private final Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  QuartzCronExpressionDao quartzCronExpressionDao;

  @Override
  public Integer getApplicationQuartzSettingValue(String name) {

    int status = 0;
    try {
      status = quartzCronExpressionDao.findQuartzCronExpression(name).getStatus();
    } catch (Exception e) {
      logger.error("TaskSchedulingServiceImpl,从V_QUARTZ_CRON_EXPRESSION获取任务状态错误:" + new Date(), e);
    }
    return status;
  }

  @Override
  public void closeQuartzApplication(String name) {

    quartzCronExpressionDao.stopTask(name);

  }



}
