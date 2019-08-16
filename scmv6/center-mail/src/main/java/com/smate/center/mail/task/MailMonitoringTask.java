package com.smate.center.mail.task;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.smate.center.mail.service.MailMonitoringService;

/**
 * 邮件监控任务
 * 
 * @author zzx
 *
 */
public class MailMonitoringTask {
  protected Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private MailMonitoringService mailMonitoringService;

  public void execute() {
    try {
      // 一个psnid一个小时内产生了 20个邮件
      mailMonitoringService.checkData1();
      // 一个接收邮箱一个小时内接收了20个邮件
      mailMonitoringService.checkData2();
    } catch (Exception e) {
      logger.error("邮件监控任务出错", e);
    }
  }
}
