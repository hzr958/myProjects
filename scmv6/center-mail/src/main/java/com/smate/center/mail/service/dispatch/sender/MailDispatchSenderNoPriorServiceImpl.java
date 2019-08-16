package com.smate.center.mail.service.dispatch.sender;

import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.smate.center.mail.model.MailDispatchInfo;
import com.smate.center.mail.model.MailSender;
import com.smate.center.mail.service.MailSenderService;

/**
 * 邮件调度->分配发送账号->没有配置优先级别的
 * 
 * @author tsz
 *
 */
@Service("mailDispatchSenderNoPriorServiceImpl")
public class MailDispatchSenderNoPriorServiceImpl implements MailDispatchSenderService {

  protected Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private MailSenderService mailSenderService;

  @Resource(name = "mailDispatchSenderServiceImpl")
  private MailDispatchSenderService mailDispatchSenderService;

  @Override
  public void excute(MailDispatchInfo mailDispatchInfo) throws Exception {
    List<MailSender> noPriorSender = mailSenderService.getMailSenderByNoPrior();
    if (CollectionUtils.isNotEmpty(noPriorSender)) {
      String account = noPriorSender.get(ran.nextInt(noPriorSender.size())).getAccount();
      mailDispatchInfo.getMailRecord().setSender(account);
    } else {
      mailDispatchSenderService.excute(mailDispatchInfo);
    }
  }
}
