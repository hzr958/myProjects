package com.smate.center.mail.service.dispatch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.smate.center.mail.connector.model.MailTemplate;
import com.smate.center.mail.exception.NotTemplateException;
import com.smate.center.mail.model.MailDispatchInfo;
import com.smate.center.mail.service.MailTemplateService;

public class MailDispatchCheckTempLimitServiceImpl implements MailDispatchService {
  protected Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private MailTemplateService mailTemplateService;

  @Override
  public void excute(MailDispatchInfo mailDispatchInfo) throws Exception {
    MailTemplate template = mailTemplateService.getMailTemplate(mailDispatchInfo.getMailRecord().getMailTemplateCode());
    if (template == null) {
      logger.error("邮件模版不存在");
      throw new NotTemplateException("邮件模版不存在！TemplateCode=" + mailDispatchInfo.getMailRecord().getMailTemplateCode());
    }
    if (template.getStatus() == 1) {
      logger.error("邮件模板已禁用");
      throw new NotTemplateException("邮件模板已禁用！TemplateCode=" + mailDispatchInfo.getMailRecord().getMailTemplateCode());
    }
  }

}
