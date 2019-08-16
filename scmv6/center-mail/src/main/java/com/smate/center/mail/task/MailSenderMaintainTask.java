package com.smate.center.mail.task;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.smate.center.mail.cache.MailCacheService;
import com.smate.center.mail.model.MailSender;
import com.smate.center.mail.service.MailSenderService;
import com.smate.center.mail.service.monitor.MailMonitorService;

/**
 * 邮件发送账号维护任务 每天 24:00执行一次 更新发送统计
 * 
 * @author tsz
 *
 */
public class MailSenderMaintainTask {

  protected Logger logger = LoggerFactory.getLogger(getClass());

  private static final String MAIL_SENDER_CHECK_MSG = "MAIL_SENDER_CHECK_MSG";
  private static final String CHECK_SENDER_EMAIL_MARK = "already_send";

  @Autowired
  private MailCacheService mailCacheService;
  @Autowired
  private MailSenderService mailSenderService;
  @Autowired
  private MailMonitorService mailMonitorService;

  public void execute() {
    // 监控是否有可用发送账号 没有就发邮件通知管理员(每小时发一次)
    if (mailSenderService.hasAvailableSender()) {
      logger.info("有可用发送账号");
      mailCacheService.remove(MAIL_SENDER_CHECK_MSG);
      // 有可用账号 并且检查各个账号 的发送情况
      List<MailSender> senderlist = mailSenderService.getAllAvailableSender();
      for (MailSender mailSender : senderlist) {
        // 判断是否超过 最大限制 如果超过 更新为不可用
        mailSenderService.checkMaxMailCountLimit(mailSender);
      }
    } else {
      logger.info("没有可用发送账号");
      if (StringUtils.isNotBlank(mailCacheService.getMailSenderCheckMsg(MAIL_SENDER_CHECK_MSG))) {
        logger.error("没有可用的发送账号! 邮件通知管理员");
        mailCacheService.putMailSenderCheckMsg(MAIL_SENDER_CHECK_MSG, CHECK_SENDER_EMAIL_MARK);
        // 邮件通知管理员
        mailMonitorService.sendMonitorMail("没有可用发送账号", "邮件调度服务没有发现可用的邮件发送账号,请尽快处理!");
      }
    }
  }

}
