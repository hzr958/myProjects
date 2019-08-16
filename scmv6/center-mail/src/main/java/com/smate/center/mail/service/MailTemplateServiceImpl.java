package com.smate.center.mail.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.mail.connector.dao.MailTemplateDao;
import com.smate.center.mail.connector.model.MailTemplate;

@Service("mailTemplateService")
@Transactional(rollbackFor = Exception.class)
public class MailTemplateServiceImpl implements MailTemplateService {
  @Autowired
  private MailTemplateDao mailTemplateDao;

  @Override
  public MailTemplate getMailTemplate(int mailTemplateCode) {
    return mailTemplateDao.get(mailTemplateCode);
  }

}
