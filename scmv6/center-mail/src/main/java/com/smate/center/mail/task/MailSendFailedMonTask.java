package com.smate.center.mail.task;

import java.util.List;

import javax.mail.Message;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.smate.center.mail.model.MailSender;
import com.smate.center.mail.service.MailSendFailedMonService;

/**
 * 邮件发送失败监控 遍历发件箱记录并处理发送失败、退信的邮件
 * 
 * @author zzx
 *
 */
public class MailSendFailedMonTask {
  protected Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private MailSendFailedMonService mailSendFailedMonService;

  public void execute() {
    List<MailSender> list = mailSendFailedMonService.findSenderList();
    if (list != null && list.size() > 0) {
      for (MailSender one : list) {
        try {
          Message[] msgArr = mailSendFailedMonService.findInboxImap(one.getHost(), one.getAccount(), one.getPassword());
          if (msgArr != null && msgArr.length > 0) {
            int length = msgArr.length;
            for (int i = length; i > 0; i--) {
              try {
                mailSendFailedMonService.handleMessage(msgArr[i - 1], one.getAccount());
              } catch (Exception e) {
                logger.error("遍历收件箱邮件列表-吃掉异常", e);
              }
            }
          }
        } catch (Exception e) {
          logger.error("遍历发件箱记录-吃掉异常", e);
        }
      }
    }

  }

}
