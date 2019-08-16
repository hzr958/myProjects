package com.smate.center.mail.service.dispatch.sender;

import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.smate.center.mail.model.MailDispatchInfo;
import com.smate.center.mail.model.MailSender;
import com.smate.center.mail.service.MailSenderService;

/**
 * 邮件调度->分配发送账号->所有的发送帐号
 * 
 * @author tsz
 *
 */
@Service("mailDispatchSenderServiceImpl")
public class MailDispatchSenderServiceImpl implements MailDispatchSenderService {

  protected Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private MailSenderService mailSenderService;

  @Override
  public void excute(MailDispatchInfo mailDispatchInfo) throws Exception {
    List<MailSender> sender = mailSenderService.getAllAvailableSender();
    if (CollectionUtils.isNotEmpty(sender)) {
      String account = sender.get(ran.nextInt(sender.size())).getAccount();
      mailDispatchInfo.getMailRecord().setSender(account);
    }
  }
}
