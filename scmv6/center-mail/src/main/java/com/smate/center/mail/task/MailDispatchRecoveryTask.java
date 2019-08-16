package com.smate.center.mail.task;

import java.util.Date;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.smate.center.mail.connector.mailenum.MailSendStatusEnum;
import com.smate.center.mail.model.MailRecord;
import com.smate.center.mail.service.MailRecordService;
import com.smate.center.mail.service.MailSenderService;

/**
 * 任务调度回收已分配未发送的邮件 5分钟 超时时间
 * 
 * @author tsz
 *
 */
public class MailDispatchRecoveryTask {

  protected Logger logger = LoggerFactory.getLogger(getClass());

  private static final int size = 100;

  private static final int recoveryTime = 300; // 回收过期时间 超过多少s 就回收 重新分配

  @Autowired
  private MailRecordService mailRecordService;
  @Autowired
  private MailSenderService mailSenderService;

  public void execute() {
    try {
      while (true) {
        List<MailRecord> list = mailRecordService.getToBeRecoveryMail(size, recoveryTime);
        if (CollectionUtils.isEmpty(list)) {
          return;
        } else {
          for (MailRecord mailRecord : list) {
            subTodayMailCount(mailRecord);
            updateMailRecord(mailRecord);
          }
        }
      }
    } catch (Exception e) {
      logger.error("回收超时未发送邮件记录出错!", e);
    }
  }

  private void updateMailRecord(MailRecord mailRecord) {
    mailRecord.setSender("");
    mailRecord.setMailClient("");
    mailRecord.setUpdateDate(new Date());
    mailRecord.setDistributeDate(null);
    mailRecord.setStatus(MailSendStatusEnum.STATUS_0.toInt());
    mailRecord.setMsg("超时未发送重新分配节点跟客户端");
    mailRecordService.updateMailRecord(mailRecord);
  }

  private void subTodayMailCount(MailRecord mailRecord) {
    String account = mailRecord.getSender();
    mailSenderService.subTodayMailCount(account);
  }
}
