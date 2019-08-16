package com.smate.center.batch.service.mail;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.batch.dao.mail.emailsrv.MailTemplateInfoDao;

/**
 * 
 * @author tsz 邮件退订实现类
 * 
 */
@Service("mailUnsubscribeService")
@Transactional(rollbackFor = Exception.class)
public class MailUnsubscribeServiceImpl implements MailUnsubscribeService {

  private Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private MailTemplateInfoDao mailTemplateInfoDao;

  @Override
  public Integer findTemplateCodeByName(String templateName) {
    try {
      return mailTemplateInfoDao.findTemplateCodeByName(templateName);
    } catch (Exception e) {
      logger.error(templateName + "根据邮件模板获取模板id出错！", e);
      return null;
    }
  }

}
