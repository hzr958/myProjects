package com.smate.center.mail.service.dispatch;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.mail.connector.mailenum.MailSendStatusEnum;
import com.smate.center.mail.model.MailDispatchInfo;
import com.smate.center.mail.service.MailRecordService;
import com.smate.center.mail.service.MailSenderService;

/**
 * 邮件调度最后执行 更新发送记录
 * 
 * @author tsz
 *
 */
@Transactional(rollbackFor = Exception.class)
public class MailDispatchUpdateMailRecordServiceImpl implements MailDispatchService {

  protected Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private MailRecordService mailRecordService;
  @Autowired
  private MailSenderService mailSenderService;

  @Override
  public void excute(MailDispatchInfo mailDispatchInfo) throws Exception {
    // 更新邮件记录状态
    mailDispatchInfo.getMailRecord().setStatus(MailSendStatusEnum.STATUS_1.toInt());
    mailDispatchInfo.getMailRecord().setUpdateDate(new Date());
    mailDispatchInfo.getMailRecord().setDistributeDate(new Date());
    mailRecordService.updateMailRecord(mailDispatchInfo.getMailRecord());

    // 更新发送账号邮件发送统计
    mailSenderService.addTodayMailCount(mailDispatchInfo.getMailRecord().getSender());
  }

}
