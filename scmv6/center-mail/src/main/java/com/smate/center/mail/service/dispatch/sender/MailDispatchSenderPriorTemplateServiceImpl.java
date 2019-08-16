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
 * 邮件调度->分配发送账号->配置了邮件模板优先的
 * 
 * @author tsz
 *
 */
@Service("mailDispatchSenderPriorTemplateServiceImpl")
public class MailDispatchSenderPriorTemplateServiceImpl implements MailDispatchSenderService {

  protected Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private MailSenderService mailSenderService;

  @Resource(name = "mailDispatchSenderPriorClientServiceImpl")
  private MailDispatchSenderService mailDispatchSenderService;

  @Override
  public void excute(MailDispatchInfo mailDispatchInfo) throws Exception {
    List<MailSender> priorTemplateSender = mailSenderService
        .getMailSenderByTemplate(String.valueOf(mailDispatchInfo.getMailRecord().getMailTemplateCode()));
    if (CollectionUtils.isNotEmpty(priorTemplateSender)) {
      String account = priorTemplateSender.get(ran.nextInt(priorTemplateSender.size())).getAccount();
      mailDispatchInfo.getMailRecord().setSender(account);
    } else {
      mailDispatchSenderService.excute(mailDispatchInfo);
    }
  }
}
