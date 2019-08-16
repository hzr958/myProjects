package com.smate.center.task.sys.quartz.service;

import java.text.ParseException;
import java.util.Calendar;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.CronTriggerFactoryBean;

import com.smate.center.task.sys.quartz.model.QuartzCronExpression;

public class IrisDbCronTriggerBean extends CronTriggerFactoryBean {

  public static final String NERVER_RUN_EXPRESS = "30 0 0 1 1 ? %s";
  protected Logger logger = LoggerFactory.getLogger(getClass());
  private String triggerName;// 定时器名称
  @Autowired
  private QuartzCronExpressionService quartzCronExpressionService;
  // 设置启动延时1分钟，有可能出现服务器spring还未构造好数据源，导致数据失败.
  private long startDelay = 60000;

  @Override
  public void afterPropertiesSet() throws ParseException {

    try {
      if (StringUtils.isBlank(triggerName)) {
        logger.error("IrisDbCronTriggerBean error");
        throw new Exception("请设置IrisDbCronTriggerBean.triggerName值");
      }
      // 获取数据配置
      QuartzCronExpression expression = quartzCronExpressionService.getQuartzCronExpression(triggerName);
      if (expression == null) {
        throw new Exception("任务管理器" + triggerName + "未配置,请到表V_QUARTZ_CRON_EXPRESSION中添加配置");
      }
      this.setStartDelay(startDelay);
      this.setCronExpression(expression.getExpression());
      super.afterPropertiesSet();
      if (this.getObject() == null) {
        throw new Exception("IrisDbCronTriggerBean-->super.getObject() is null");
      }
      // 如果是停止状态，则获取一个永远达不到的日期
      if (QuartzCronExpressionService.STOP_STATUS.equals(expression.getStatus())) {
        this.setCronExpression(getNeverNeverFireExpression());
      } /*
         * else { super.setCronExpression(expression.getExpression()); }
         */

    } catch (Exception e) {
      logger.error("IrisDbCronTriggerBean error", e);
    }
  }

  /**
   * 获取一个永远达不到的执行日期：当前年份+10年的1月1日凌晨30秒.
   * 
   * @return
   */
  private String getNeverNeverFireExpression() {

    Calendar cal = Calendar.getInstance();
    cal.add(Calendar.YEAR, 10);
    int year = cal.get(Calendar.YEAR);
    return String.format("30 0 0 1 1 ? %s", year);
  }

  public void setStartDelay(long startDelay) {
    this.startDelay = startDelay;
  }

  public void setTriggerName(String triggerName) {
    this.triggerName = triggerName;
  }

}
