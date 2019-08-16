package com.smate.center.mail.task;

import com.smate.center.mail.connector.model.MailOriginalData;
import com.smate.center.mail.model.MailRecord;
import com.smate.center.mail.service.MailRemovalStatisticsService;
import com.smate.center.mail.service.MailSenderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * 邮件迁移统计任务
 * 
 * @author zzx
 *
 */
public class MailRemovalStatisticsTask {
  protected Logger logger = LoggerFactory.getLogger(getClass());

  private static final int MAIL_DISPATCH_SIZE = 2000;
  @Autowired
  private MailRemovalStatisticsService mailRemovalStatisticsService;
  @Autowired
  private MailSenderService mailSenderService;

  public void execute() {
    // 初始化邮件发送数据
    initTodayMailCont();
    try {
      List<MailOriginalData> originalList = null;
      List<MailRecord> mailRecordList = null;
      do {
        // 迁移MailOriginalData数据
        originalList = mailRemovalStatisticsService.findOriginalList(MAIL_DISPATCH_SIZE);
        mailRemovalStatisticsService.handleOriginalList(originalList);
      } while (mailRemovalStatisticsService.existOriginalList());
      do {
        // 迁移MailRecord数据
        mailRecordList = mailRemovalStatisticsService.findRecordList(MAIL_DISPATCH_SIZE);
        mailRemovalStatisticsService.handleMailRecord(mailRecordList);
      } while (mailRemovalStatisticsService.existRecordList());
      // 统计今天发送邮件的情况
      mailRemovalStatisticsService.statisticsMailInfo();
    } catch (Exception e) {
      logger.error("邮件迁移统计任务出错", e);
    }

  }

  private void initTodayMailCont() {

    mailSenderService.initTodayMailCount();
    logger.info("到新的一天了.更新发送统计!!");

  }

}
