package com.smate.center.mail.service;

import com.smate.center.mail.connector.model.MailTemplate;

public interface MailTemplateService {

  MailTemplate getMailTemplate(int mailTemplateCode);

}
